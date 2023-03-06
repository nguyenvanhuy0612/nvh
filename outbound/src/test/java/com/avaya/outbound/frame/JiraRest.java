package com.avaya.outbound.frame;

import com.avaya.outbound.lib.EnvSetup;
import com.google.gson.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

public class JiraRest {
    // Define Logger
    private final Logger log = LogManager.getLogger(this);

    /**
     * Common rest utility
     *
     * @param url
     * @param body
     * @param method
     * @return
     */
    public JsonObject restMethod(String method, String url, String body) {
        log.info("-----------------------------------------------------------------------------------------------------------------");
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = null;
        String username = "";
        String password = "";
        try {
            RestAssured.reset();
            RestAssured.useRelaxedHTTPSValidation("TLSv1.2");
            if (method.toLowerCase().contentEquals("get")) {
                response = given().given().urlEncodingEnabled(false).contentType("application/json")
                        .header("X-Requested-With", "raj", "Authorization", "BasicYWRtaW46cG9ydGFsMTIz")
                        .auth().basic(username, password).body(body).when()
                        .params("limitTo", "6").get(url).then().contentType(ContentType.JSON).
                        extract().response();
                log.info("GET result --->\n " + response.print());
            } else if (method.toLowerCase().contentEquals("post")) {
                response = given().contentType("application/json")
                        .header("X-Requested-With", "raj", "Authorization", "BasicYWRtaW46cG9ydGFsMTIz")
                        .auth().basic(username, password).body(body).when().post(url).then().
                        extract().response();
                log.info("POST result --->\n " + response.print());

            } else if (method.toLowerCase().contentEquals("put")) {
                response = given().contentType("application/json")
                        .header("X-Requested-With", "raj", "Authorization", "BasicYWRtaW46cG9ydGFsMTIz")
                        .auth().basic(username, password).body(body).when().put(url).then()
                        .extract().response();
                log.info("PUT result --->\n " + response.print());
            } else if (method.toLowerCase().contentEquals("delete")) {
                response = given().contentType("application/json")
                        .header("X-Requested-With", "raj", "Authorization", "BasicYWRtaW46cG9ydGFsMTIz")
                        .auth().basic(username, password).body(body).when().delete(url).then().
                        extract().response();
                log.info("DELETE result --->\n " + response.print());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String resString = response.asString();
            JsonElement resJsonElement = gson.fromJson(resString, JsonElement.class);
            if (resJsonElement instanceof JsonObject)
                return resJsonElement.getAsJsonObject();
            else if (resJsonElement instanceof JsonArray) {
                JsonObject resJsonObject = new JsonObject();
                resJsonObject.add("result", resJsonElement);
                return resJsonObject;
            }
        }
        return null;
    }


    /**
     * Method to attached file to Jira
     *
     * @param issueKey
     * @param fullfilename
     * @return
     * @throws IOException
     */
    public boolean addAttachmentToIssue(String issueKey, String fullfilename) throws IOException {
        String url = "https://jira.forge.avaya.com/rest/api/2/issue/" + issueKey + "/attachments";
        log.info(url);
        String username = "";
        String password = "";
        String jira_attachment_authentication = new String(encodeBase64((username + ":" + password).getBytes()));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("X-Atlassian-Token", "nocheck");
        httppost.setHeader("Authorization", "Basic " + jira_attachment_authentication);
        File fileToUpload = new File(fullfilename);
        FileBody fileBody = new FileBody(fileToUpload);
        HttpEntity entity = MultipartEntityBuilder.create().addPart("file", fileBody).build();
        httppost.setEntity(entity);
        String mess = "executing request " + httppost.getRequestLine();
        log.info(mess);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httppost);
        } finally {
            httpclient.close();
        }
        return response.getStatusLine().getStatusCode() == 200;
    }


    /**
     * description: Method to get Project id
     *
     * @param projectName
     * @return
     * @throws ParseException
     */
    public String getProjectId(String projectName) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/util/project-list";
        String projectkey = "";
        log.info(url);
        JsonObject result = this.restMethod(url, "", "GET");
        JsonArray reslist = (JsonArray) result.get("options");
        for (int j = 0; j < reslist.size(); j++) {
            JsonObject res = (JsonObject) reslist.get(j);
            if (res.get("label").toString().toLowerCase().contains(projectName.toLowerCase())) {
                log.info("ProjectId : " + res.get("value"));
                projectkey = res.get("value").toString();
                break;
            }
        }
        return projectkey;
    }


    /**
     * description: Method to get release id
     *
     * @param projectName
     * @param release
     * @return
     * @throws ParseException
     */
    public String getReleaseId(String projectName, String release) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String projectId;
        if (EnvSetup.PROJECTID == null || EnvSetup.PROJECTID.isEmpty()) {
            projectId = this.getProjectId(projectName).replaceAll("\"", "");
        } else {
            projectId = EnvSetup.PROJECTID;
        }
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/util/versionBoard-list?projectId="
                + projectId.replaceAll("\"", "");
        log.info(url);
        String releaseKey = "";
        JsonObject result = this.restMethod(url, "", "GET");
        JsonArray reslist = (JsonArray) result.get("unreleasedVersions");
        for (int j = 0; j < reslist.size(); j++) {
            JsonObject res = (JsonObject) reslist.get(j);
            if (res.get("label").toString().contains(release)) {
                System.out.println("ReleaseId : " + res.get("value"));
                log.info("ReleaseId : " + res.get("value"));
                releaseKey = res.get("value").toString();
                break;
            }
        }
        if (releaseKey.isEmpty()) {
            JsonArray reslistr = (JsonArray) result.get("releasedVersions");
            for (int j = 0; j < reslistr.size(); j++) {
                JsonObject res = (JsonObject) reslistr.get(j);
                if (res.get("label").toString().contains(release)) {
                    System.out.println("ReleaseId : " + res.get("value"));
                    log.info("ReleaseId : " + res.get("value"));
                    releaseKey = res.get("value").toString();
                    break;
                }
            }
        }
        return releaseKey;
    }


    /**
     * description: Method to get cycle id
     *
     * @param projectName
     * @param release
     * @param cycleName
     * @return
     * @throws ParseException
     */
    public String getCycleId(String projectName, String release, String cycleName) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String projectId;
        if (EnvSetup.PROJECTID == null || EnvSetup.PROJECTID.isEmpty()) {
            projectId = this.getProjectId(projectName).replaceAll("\"", "");
        } else {
            projectId = EnvSetup.PROJECTID;
        }
        String versionId;
        if (EnvSetup.RELEASEID == null || EnvSetup.RELEASEID.isEmpty()) {
            versionId = this.getReleaseId(projectName, release).replaceAll("\"", "");
        } else {
            versionId = EnvSetup.RELEASEID;
        }

        String url = "https://jira.forge.avaya.com/rest/zapi/latest/cycle?versionId=" + versionId.replaceAll("\"", "");
        log.info(url);
        String cycleID = "";
        JsonObject result = this.restMethod(url, "", "GET");
        System.out.println("==============================");
        JSONObject object = new JSONObject(result.toString());
        JSONArray keys = object.names();
        for (int i = 0; i < keys.length(); i++) {
            String key = keys.getString(i); // Here's your key
            if (!key.contentEquals("recordsCount")) {
                JsonObject tmpresult = (JsonObject) result.get(key);
                if (tmpresult.get("name").toString().replaceAll("\"", "").contentEquals(cycleName)) {
                    log.info("Matched cycle found : " + result.get(key));
                    cycleID = key.replaceAll("\"", "");
                    break;
                }
            }
        }
        log.info(cycleID);
        return cycleID;
    }


    /**
     * description: Method to get issue id
     *
     * @param issueNumber
     * @return
     * @throws ParseException
     */
    public String getIssueId(String issueNumber) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = "https://jira.forge.avaya.com/rest/api/latest/issue/" + issueNumber;
        log.info(url);
        String issieID = "";
        JsonObject result = this.restMethod(url, "", "GET");
        System.out.println(result.get("id"));
        issieID = result.get("id").toString().replaceAll("\"", "");
        return issieID;
    }


    /**
     * description: Method to create Test cycle
     *
     * @param name
     * @param build
     * @param startDate
     * @param endDate
     * @param projectName
     * @param versionId
     * @throws ParseException
     */
    public void createTestCycle(String name,
                                String build,
                                String startDate,
                                String endDate,
                                String projectName,
                                String versionId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/cycle";
        log.info(url);
        String projectId;
        if (EnvSetup.PROJECTID == null) {
            projectId = this.getProjectId(projectName).replaceAll("\"", "");
        } else {
            projectId = EnvSetup.PROJECTID;
        }
        if (EnvSetup.RELEASEID == null) {
            versionId = this.getReleaseId(projectName, versionId).replaceAll("\"", "");
        } else {
            versionId = EnvSetup.RELEASEID;
        }
        String body = "{\"build\":\"" + build + "\" , \"name\":\"" + name + "\",\"startDate\":\"" + startDate
                + "\" , \"endDate\":\"" + endDate + "\", \"projectId\":\"" + projectId + "\", \"versionId\":\""
                + versionId + "\",\"environment\": \"environment\"," + "\"description\": \"Created In ZAPI\","
                + "\"clonedCycleId\": \"\" }";
        System.out.println(body);
        JsonObject result = this.restMethod(url, body, "POST");
    }


    /**
     * description: Method to create execution
     *
     * @param issueId
     * @param cycleId
     * @param projectName
     * @param release
     * @throws ParseException
     */
    public void createExecution(String issueId,
                                String cycleId,
                                String projectName,
                                String release,
                                String folderId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/execution";
        log.info(url);
        String projectId = "";
        if (EnvSetup.PROJECTID == null) {
            projectId = this.getProjectId(projectName).replaceAll("\"", "");
        } else {
            projectId = EnvSetup.PROJECTID;
        }
        issueId = this.getIssueId(issueId).replaceAll("\"", "");
        String versionId;
        if (EnvSetup.RELEASEID == null) {
            versionId = this.getReleaseId(projectName, release).replaceAll("\"", "");
        } else {
            versionId = EnvSetup.RELEASEID;
        }
        String body = "";
        if (folderId == null || folderId.length() < 2) {
            body = "{\"issueId\":\"" + issueId + "\" , \"versionId\":\"" + versionId + "\",\"cycleId\":\"" + cycleId
                    + "\" , \"projectId\":\"" + projectId + "\" }";
        } else {
            body = "{\"issueId\":\"" + issueId + "\" , \"versionId\":\"" + versionId + "\",\"cycleId\":\"" + cycleId
                    + "\" , \"projectId\":\"" + projectId + "\",\"folderId\": " + folderId + " }";
        }
        System.out.println(body);
        JsonObject result = this.restMethod(url, body, "POST");
    }


    /**
     * description: Method to get execution id
     *
     * @param projectName
     * @param release
     * @param cycleId
     * @param keyId
     * @return
     * @throws ParseException
     */
    public String getexecutionId(String projectName,
                                 String release,
                                 String cycleId,
                                 String keyId,
                                 String folderId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String issueId = this.getIssueId(keyId);
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/execution?issueId=" + issueId + "&cycleId="
                + cycleId;
        if (folderId != null && folderId.length() > 1) {
            url = "https://jira.forge.avaya.com/rest/zapi/latest/execution?issueId=" + issueId + "&cycleId=" + cycleId
                    + "&folderId=" + folderId;
        }
        log.info(url);
        String executionId = "";
        JsonObject result = this.restMethod(url, "", "GET");
        log.info(result);
        JsonArray reslist = (JsonArray) result.get("executions");
        for (int j = 0; j < reslist.size(); j++) {
            JsonObject res = (JsonObject) reslist.get(j);
            System.out.println(res.has("folderId"));
            if (res.get("issueKey").toString().contains(keyId)) {
                if (folderId != null && folderId.length() > 1) {
                    if (res.has("folderId") && res.get("folderId").toString().contains(folderId)) {
                        System.out.println("ExecutionId : " + res.get("id"));
                        log.info("ExecutionId : " + res.get("id"));
                        executionId = res.get("id").toString();
                        break;
                    }
                } else if (!res.has("folderId")) {
                    System.out.println("ExecutionId : " + res.get("id"));
                    log.info("ExecutionId : " + res.get("id"));
                    executionId = res.get("id").toString();
                    break;
                }
            }
        }
        return executionId;
    }


    /**
     * Description : Get Associated Open Bug for test case executions.
     *
     * @param issueId
     * @return
     * @throws ParseException
     */
    public ArrayList<String> getallExecutionIdsfordefects(String issueId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String issueKey = "";
        String status = "";
        String retresult = "";
        ArrayList<String> defectlist = new ArrayList<String>();
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/execution?issueId=" + issueId;
        log.info(url);
        JsonObject result = this.restMethod(url, "", "GET");
        JsonArray reslist = (JsonArray) result.get("executions");
        log.info(reslist.size());
        if (reslist.size() != 0) {
            for (int i = 0; i < reslist.size(); i++) {
                JsonObject tmpresult = (JsonObject) reslist.get(i);
                if (tmpresult.has("defects")) {
                    if (tmpresult.get("defects").toString().length() > 1) {
                        log.info("Defects " + tmpresult.get("defects"));
                        JsonArray defectsList = (JsonArray) tmpresult.get("defects");
                        for (int j = 0; j < defectsList.size(); j++) {
                            JsonObject curdefect = (JsonObject) defectsList.get(j);
                            if (!curdefect.get("status").toString().toLowerCase().contains("closed")) {
                                log.info("Defect is not closed..no need to open new");
                                defectlist.add(curdefect.get("key").toString().replaceAll("\"", ""));
                            }
                        }
                    }
                }
            }
        }
        return defectlist;
    }


    /**
     * Description : Get Defect associated with test execution
     *
     * @param executionId
     * @return
     * @throws ParseException
     */
    public String getexecutionDefectId(String executionId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String issueKey = "";
        String status = "";
        String retresult = "";
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/execution/" + executionId + "/defects";
        log.info(url);
        JsonObject result = this.restMethod(url, "", "GET");
        JsonObject reslist = (JsonObject) result.get(executionId);
        log.info("Response " + reslist);
        if (reslist.isJsonNull()) {
            log.info("No Defect ");
        } else {
            log.info(reslist);
            Set<Map.Entry<String, JsonElement>> entries = reslist.entrySet();//will return members of your object
            for (Map.Entry<String, JsonElement> entry : entries) {
                System.out.println(entry.getKey());
                JsonObject issueKeyJson = (JsonObject) reslist.get(entry.getKey());
                issueKey = issueKeyJson.get("key").toString().replaceAll("\"", "");
                status = issueKeyJson.get("status").toString().replaceAll("\"", "");
                retresult = issueKey + "|" + status;
            }
        }
        return retresult;
    }


    /**
     * Description : Get execution status associated with test execution
     *
     * @param executionId
     * @return
     * @throws ParseException
     */
    public String getexecutionstatus(String executionId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        JsonObject jsexecutionStatus = null;
        int sexecutionStatus;
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/execution/" + executionId;
        log.info(url);
        JsonObject result = this.restMethod(url, "", "GET");
        log.info(result);
        if (result.isJsonNull()) {
            log.info("No execution status ");
            return "Nostatus";
        } else {
            jsexecutionStatus = (JsonObject) result.get("execution");
            if (jsexecutionStatus.isJsonNull()) {
                return "Nostatus";
            } else {
                sexecutionStatus = jsexecutionStatus.get("executionStatus").getAsInt();
                if (sexecutionStatus == 1) //"description": "Test was executed and passed successfully.",
                {
                    return "PASS";
                }
                if (sexecutionStatus == 2) //"description": "Test was executed and failed.",
                {
                    return "FAIL";
                }
                if (sexecutionStatus == 3) //"description": "Test execution is a work-in-progress.",
                {
                    return "WIP";
                }
                if (sexecutionStatus == 4) //"description": "The test execution of this test was blocked for some reason.",
                {
                    return "BLOCKED";
                }
                if (sexecutionStatus == 5) //"description": "Test was deferred",
                {
                    return "DEFERRED";
                }
                if (sexecutionStatus == -1) //"description": "The test has not yet been executed.",
                {
                    return "UNEXECUTED";
                }
            }
            return "Nostatus";
        }
    }


    /**
     * description: Method to execute test
     *
     * @param projectName
     * @param release
     * @param cycleId
     * @param keyId
     * @param status
     * @param folderid
     * @throws ParseException
     */
    public void executeTest(String projectName,
                            String release,
                            String cycleId,
                            String keyId,
                            String status,
                            String folderid) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String executionId = this.getexecutionId(projectName, release, cycleId, keyId, folderid).replaceAll("\"", "");
        String comment = "Executed using Automation";
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/execution/" + executionId + "/execute";
        log.info(url);
        String body = "{\"status\":\"" + status + "\" , \"comment\":\"" + comment + "\" }";
        System.out.println(body);
        JsonObject result = this.restMethod(url, body, "PUT");
        log.info(result);
    }


    /**
     * Description : Associate bug to test execution record
     *
     * @param projectName
     * @param release
     * @param cycleId
     * @param keyId
     * @param bugId
     * @param folderId
     * @throws ParseException
     */
    public void associateBugtoExecution(String projectName,
                                        String release,
                                        String cycleId,
                                        String keyId,
                                        String bugId,
                                        String folderId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String executionId = this.getexecutionId(projectName, release, cycleId, keyId, folderId).replaceAll("\"", "");
        String comment = "Executed using Automation";
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/execution/" + executionId + "/execute";
        log.info(url);
        String body = "{\"defectList\":[\"" + bugId + "\"] , \"updateDefectList\":\"true\" }";
        System.out.println(body);
        JsonObject result = this.restMethod(url, body, "PUT");
        log.info(result);
    }


    /**
     * Description : Delete current execution record
     *
     * @param projectName
     * @param release
     * @param cycleName
     * @param keyId
     * @throws ParseException
     */
    public void Deleteexecution(String projectName,
                                String release,
                                String cycleName,
                                String keyId,
                                String folderId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String executionId = this.getexecutionId(projectName, release, cycleName, keyId, folderId).replaceAll("\"", "");
        String comment = "Executed using Automation";
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/execution/" + executionId;
        log.info(url);
        JsonObject result = this.restMethod(url, "", "DELETE");
        log.info(result);
    }


    /**
     * Description : Method to create Bug in Jira
     *
     * @param Summary
     * @param Description
     * @param Component
     * @param Build
     * @throws ParseException
     */
    public String createBug(String Summary, String Description, String Component, String Build) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = "https://jira.forge.avaya.com/rest/api/2/issue/";
        log.info(url);
        String bugId = "";
        String data = "{\"fields\": {\"summary\": \"" + Summary + "\"" + ",\"description\": \"" + Description + "\""
                + ",\"components\": [{ \"name\": \"" + Component + "\" }]"
                + ",\"timetracking\": {\"originalEstimate\": \"2h\"}" + ",\"project\": { \"key\": \"OUTREACH\"}"
                + ",\"issuetype\": {\"id\": \"1\" }" + ",\"reporter\":{\"name\":\"rmujumale\"}"
                + ",\"customfield_11272\": {\"value\" : \"Development Team (Best)\""
                + ",\"child\": {\"value\": \"Automated Test\"}}" + ",\"fixVersions\":[{ \"name\": \""
                + EnvSetup.RELEASE + "\" }]" + ",\"customfield_10120\": {\"value\": \"3-Medium\"}"
                + ",\"customfield_11870\": [{\"name\": \"" + EnvSetup.RELEASE + "\"}]"
                + ",\"priority\": {\"name\": \"P3 - Normal Queue\"}"
                + ",\"customfield_11373\": {\"value\": \"Breakage of Feature\"}" + ",\"customfield_11874\": \""
                + Build + "\"" + ",\"labels\": [\"POMCoreFeatures\"]"
                + ",\"customfield_11374\": {\"value\": \"Not Present in Any Prior Release\"}"
                + ",\"customfield_11871\": [{\"name\": \"" + EnvSetup.RELEASE + "\"}]}}";
        System.out.println(data);
        JsonObject result = this.restMethod(url, data, "POST");
        log.info(result);
        bugId = result.get("key").toString().replaceAll("\"", "");
        return bugId;
    }


    /**
     * Description Resolve bug in Jira
     *
     * @param Build
     * @throws ParseException
     */
    public void resolveBug(String Build, String jiraId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = "https://jira.forge.avaya.com/rest/api/2/issue/" + jiraId + "/transitions";
        log.info(url);
        String projectId;
        String data = "{\"update\": {\"comment\": [ {\"add\": {\"body\": \"Resolved via automated process.\"}}]},\"transition\": {\"id\": \"5\"}"
                + ",\"fields\": {\"versions\": [{\"description\": \"" + EnvSetup.RELEASE + "\", \"name\": \""
                + EnvSetup.RELEASE + "\"}], \"customfield_11871\": [{\"description\": \"" + EnvSetup.RELEASE
                + "\", \"name\": \"" + EnvSetup.RELEASE + "\"}], \"customfield_11872\": [{\"description\": \""
                + EnvSetup.RELEASE + "\", \"name\": \"" + EnvSetup.RELEASE + "\"}], \"customfield_11875\": \""
                + Build + "\", \"customfield_11876\": \"" + Build + "\", \"fixVersions\": [{\"description\": \""
                + EnvSetup.RELEASE + "\", \"name\": \"" + EnvSetup.RELEASE
                + "\"}], \"resolution\": {\"name\": \"Fixed\"}}}";
        System.out.println(data);
        this.restMethod(url, data, "POST");
    }


    /**
     * Description : Close the Bug created by Automation
     *
     * @param Build
     * @param jiraId
     * @throws ParseException
     */
    public void CloseBug(String Build, String jiraId) throws ParseException {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = "https://jira.forge.avaya.com/rest/api/2/issue/" + jiraId + "/transitions";
        log.info(url);
        String data = "{\"update\": {\"comment\": [ {\"add\": {\"body\": \"Closed via automated process.\"}}]},\"transition\": {\"id\": \"701\"}"
                + ",\"fields\": {\"versions\": [{\"description\": \"" + EnvSetup.RELEASE + "\", \"name\": \""
                + EnvSetup.RELEASE + "\"}], \"customfield_11871\": [{\"description\": \"" + EnvSetup.RELEASE
                + "\", \"name\": \"" + EnvSetup.RELEASE + "\"}], \"customfield_11872\": [{\"description\": \""
                + EnvSetup.RELEASE + "\", \"name\": \"" + EnvSetup.RELEASE + "\"}], \"customfield_11875\": \""
                + Build + "\", \"customfield_11876\": \"" + Build + "\", \"fixVersions\": [{\"description\": \""
                + EnvSetup.RELEASE + "\", \"name\": \"" + EnvSetup.RELEASE + "\"}]}}";
        System.out.println(data);
        log.info(data);
        this.restMethod(url, data, "POST");
    }


    /**
     * Description : Get bug summary
     *
     * @param jiraId
     * @return
     * @throws ParseException
     */
    public String getBugSummary(String jiraId) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String retresult = "";
        String url = "https://jira.forge.avaya.com/rest/api/2/issue/" + jiraId;
        log.info(url);
        JsonObject result = this.restMethod(url, "", "GET");
        JsonObject jiradata = (JsonObject) result.get("fields");
        log.info("Jira Summary : " + jiradata.get("summary"));
        retresult = jiradata.get("summary").toString();
        return retresult;
    }


    /**
     * Description : Get Jira epic
     *
     * @param jiraId
     * @return
     * @throws ParseException
     */
    public String getJiraEpic(String jiraId) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String retresult = "";
        String url = "https://jira.forge.avaya.com/rest/api/2/issue/" + jiraId;
        log.info(url);
        JsonObject result = this.restMethod(url, "", "GET");
        JsonObject jiradata = (JsonObject) result.get("fields");
        log.info("Jira Epic : " + jiradata.get("customfield_12973"));
        retresult = jiradata.get("customfield_12973").toString();
        log.info("Get Epic Summary" + retresult);
        if (retresult.contains("null")) {
            return EnvSetup.TESTCYCLE;
        } else {
            retresult = this.getBugSummary(retresult.replaceAll("\"", ""));
            return retresult.replaceAll(" ", "_").replaceAll("\"", "");
        }
    }


    /**
     * Method to create tet cycle
     *
     * @param cycleName
     */
    public void createCycle(String cycleName) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String buildver = "AutomationRegression";
        log.info("Create test cycle");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMM/yy");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowmin4 = now.minusDays(4);
        LocalDateTime nowpls4 = now.plusDays(4);
        String startDate = dtf.format(nowmin4);
        String endDate = dtf.format(nowpls4);
        System.out.println(startDate);
        log.info(endDate);
        String release = EnvSetup.RELEASE;
        JiraRest JiraRestObj = new JiraRest();
        String cycleId = "";
        try {
            cycleId = JiraRestObj.getCycleId("IX Outbound", release, cycleName).replaceAll("\"", "");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (cycleId.isEmpty()) {
            try {
                JiraRestObj.createTestCycle(cycleName, buildver, startDate, endDate, "Outreach", release);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } //"3.1.2.1.0");
        }
    }


    /**
     * Method to ceate test folder
     *
     * @param cycleId
     * @param folderName
     * @param projectId
     * @param releaseID
     */
    public void createTestFolder(String cycleId, String folderName, String projectId, String releaseID) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String retresult = "";
        String data = "{\"cycleId\": " + cycleId + ",\"name\": \"" + folderName + "\","
                + "\"description\": \"created test folder for this cycle\",\"projectId\":" + projectId
                + ",\"versionId\": " + releaseID + ",\"clonedFolderId\": -1}";
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/folder/create";
        log.info(url);
        log.info(data);
        System.out.println(url);
        System.out.println(data);
        JsonObject result = this.restMethod(url, data, "POST");
        log.info(result);
    }


    /**
     * Methos to get folder id
     *
     * @param cycleid
     * @param projectId
     * @param folderName
     * @param releaseID
     * @return
     */
    public String getFolderId(String cycleid, String projectId, String folderName, String releaseID) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String retresult = "";
        String url = "https://jira.forge.avaya.com/rest/zapi/latest/cycle/" + cycleid + "/folders?projectId="
                + projectId + "&versionId=" + releaseID;
        log.info(url);
        JsonObject result = this.restMethod(url, "", "GET");
        log.info(result);
        System.out.println(result);
        JsonArray flist = (JsonArray) result.get("result");
        for (JsonElement folder : flist) {
            JsonObject list = (JsonObject) folder;
            log.info(list);
            System.out.println("-----------------------" + list);
            log.info(list.get("folderName"));
            retresult = list.get("folderId").toString();
            if (list.get("folderName").toString().replaceAll("\"", "").contentEquals(folderName)) {
                break;
            }
        }
        return retresult;
    }
}
