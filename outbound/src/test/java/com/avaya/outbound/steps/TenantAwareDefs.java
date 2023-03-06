package com.avaya.outbound.steps;
import com.avaya.outbound.frame.ContactListPage;
import com.avaya.outbound.frame.RestMethods;
import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
public class TenantAwareDefs extends StepContext {
    public Locator locator = new Locator();

    public TenantAwareDefs(Context context) {
        context.init(this);
    }

    @Before(order = 9000, value = "@IXOUTREACH-6653")
    public void initPageObject() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @After(order = 9000, value = "@IXOUTREACH-6653")
    public void after(Scenario scenario) {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
    }

    @Then("Verify error message and error code when send request {string} API with wrong account ID to {string} - {string} service")
    public void sendAPIRequestWrongAccountID(String methods, String pageName, String service) throws JsonProcessingException {
        String url = switch (service){
            case "campaigns" -> EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL;
            case "contacts" -> EnvSetup.OUTBOUND_CONTACTS_API_BASEURL;
            default -> null;
        };
        String pageData = switch (pageName) {
            case "contact-lists" -> "contactList";
            case "data-sources" -> "dataSource";
            case "campaigns" -> "campaign";
            case "strategies" -> "strategy";
            case "completion-codes" -> "completionCode";
            default -> null;
        };
        url = url.replaceAll("/accounts/neo4j", "/accounts/wrongId");
        RestMethods restMethodWrongAccountid = new RestMethods(url, EnvSetup.DB_USER, EnvSetup.DB_PASSWORD);
        JsonObject response = null;
        JsonObject result = null;
        switch (methods){
            case "GET":
                response = restMethodWrongAccountid.rest(RestMethods.Method.GET, url + "/" + pageName, "");
                log.info(response);
                result = response.get("result").getAsJsonArray().get(0).getAsJsonObject();
                Assert.assertEquals("error code not match", testData.getString(pageData,"errorCode"), result.get("errorCode").toString());
                Assert.assertEquals("Error message not match", testData.getString(pageData,"errorMessage"), result.get("errorMessage").toString().replaceAll("\"",""));
                break;
            case "POST":
                Map<String, String> bodyMap = (Map) testData.getData(pageData,"body");
                ObjectMapper mapper = new ObjectMapper();
                String body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyMap);
                log.info(body);
                response = restMethodWrongAccountid.rest(RestMethods.Method.POST,url + "/" + pageName,body);
                log.info(response);
                result = response.get("result").getAsJsonArray().get(0).getAsJsonObject();
                Assert.assertEquals("error code not match", testData.getString(pageData,"errorCode"), result.get("errorCode").toString());
                Assert.assertEquals("Error message not match", testData.getString(pageData,"errorMessage"), result.get("errorMessage").toString().replaceAll("\"",""));
                break;
            case "RUN":
                url = switch (service){
                    case "campaigns" -> url + "/campaigns/" + testData.getString(pageData,"campaignID") + "/execute";
                    case "contacts" -> url + "/contact-lists/" + testData.getString(pageData,"contactListID") + "/data-sources/"
                            + testData.getString("dataSourceID") + "/run";
                    default -> null;
                };
                response = restMethodWrongAccountid.rest(RestMethods.Method.POST,url,"");
                log.info(response);
                result = response.get("result").getAsJsonArray().get(0).getAsJsonObject();
                Assert.assertEquals("error code not match", testData.getString(pageData,"errorCode"), result.get("errorCode").toString());
                Assert.assertEquals("Error message not match", testData.getString(pageData,"errorMessage"), result.get("errorMessage").toString().replaceAll("\"",""));
                break;
            case "STOP":
                url = url + "/campaigns/" + testData.getString(pageData,"campaignID") + "/jobs/"
                        + testData.getString("jobID") + "/stop";
                response = restMethodWrongAccountid.rest(RestMethods.Method.POST,url,"");
                log.info(response);
                result = response.get("result").getAsJsonArray().get(0).getAsJsonObject();
                Assert.assertEquals("error code not match", testData.getString(pageData,"errorCode"), result.get("errorCode").toString());
                Assert.assertEquals("Error message not match", testData.getString(pageData,"errorMessage"), result.get("errorMessage").toString().replaceAll("\"",""));
                break;
            case "PUT":
                url = url + "/" + pageName + "/" + testData.getString("id");
                Map<String, String> bodyMaps = (Map) testData.getData(pageData,"body");
                ObjectMapper mappers = new ObjectMapper();
                String bodys = mappers.writerWithDefaultPrettyPrinter().writeValueAsString(bodyMaps);
                log.info(bodys);
                response = restMethodWrongAccountid.rest(RestMethods.Method.PUT,url,bodys);
                log.info(response);
                result = response.get("result").getAsJsonArray().get(0).getAsJsonObject();
                Assert.assertEquals("error code not match", testData.getString(pageData,"errorCode"), result.get("errorCode").toString());
                Assert.assertEquals("Error message not match", testData.getString(pageData,"errorMessage"), result.get("errorMessage").toString().replaceAll("\"",""));
                break;
        }
    }

    @Then("Verify response API when send request {string} API without account ID to {string} - {string} service")
    public void sendAPIRequestWithoutAccountID(String methods, String pageName, String service) throws JsonProcessingException {
        String urlDefault = switch (service){
            case "campaigns" -> EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL;
            case "contacts" -> EnvSetup.OUTBOUND_CONTACTS_API_BASEURL;
            default -> null;
        };
        String urlWithoutID = urlDefault.replaceAll("accounts/neo4j","");
        String pageData = switch (pageName) {
            case "contact-lists" -> "contactList";
            case "data-sources" -> "dataSource";
            case "campaigns" -> "campaign";
            case "strategies" -> "strategy";
            case "completion-codes" -> "completionCode";
            default -> null;
        };
        RestMethods restMethodDefaultId = new RestMethods(urlDefault , EnvSetup.DB_USER, EnvSetup.DB_PASSWORD);
        RestMethods restMethodWithoutId = new RestMethods(urlWithoutID , EnvSetup.DB_USER, EnvSetup.DB_PASSWORD);
        JsonObject resDefault = null;
        JsonObject resWithoutID = null;
        String urltmp = null;
        String cmpID, jobID = null;
        String cmpDefaultName = null;
        switch (methods){
            case "GET":
                resWithoutID = restMethodWithoutId.rest(RestMethods.Method.GET,urlWithoutID + pageName,"");
                resDefault = restMethodDefaultId.rest(RestMethods.Method.GET,urlDefault + "/" +pageName,"");
                log.info("Total records of default account: " + resDefault.get("totalRecords"));
                log.info("Total records of without accountid: " + resWithoutID.get("totalRecords"));
                Assert.assertEquals("total records not match", resDefault.get("totalRecords"),resWithoutID.get("totalRecords"));
                break;
            case "POST":
                Map<String, String> bodyMap = (Map) testData.getData(pageData,"body");
                ObjectMapper mapper = new ObjectMapper();
                String body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyMap);
                log.info(body);
                switch (pageName){
                    case "contact-lists":
                        restMethodDefaultId.deleteContactList(testData.getString(pageData,"body","name"),"");
                        resWithoutID = restMethodWithoutId.rest(RestMethods.Method.POST,urlWithoutID + pageName,body);
                        String clId = resWithoutID.get("id").getAsString();
                        String clIdDefault = null;
                        log.info(pageName + " just created id: " + clId);
                        clIdDefault = restMethodDefaultId.getContactListID(testData.getString(pageData,"body","name"));
                        log.info(pageName + " id on default: " + clIdDefault);
                        Assert.assertTrue("id not match", clId.equals(clIdDefault));
                        restMethodDefaultId.deleteContactList("",clIdDefault);
                        break;
                    case "data-sources":
                        restMethodDefaultId.deleteDataSource(testData.getString(pageData,"body","name"),"");
                        resWithoutID = restMethodDefaultId.rest(RestMethods.Method.POST,urlWithoutID + pageName,body);
                        String dataSourceId = resWithoutID.get("id").getAsString();
                        String dataSourceIdDefault = null;
                        log.info(pageName + " just created id: " + dataSourceId);
                        dataSourceIdDefault = restMethodDefaultId.getDataSourceID(testData.getString(pageData,"body","name"));
                        log.info(pageName + " id on default: " + dataSourceIdDefault);
                        Assert.assertTrue("id not match", dataSourceId.equals(dataSourceIdDefault));
                        restMethodDefaultId.deleteDataSource("",dataSourceIdDefault);
                        break;
                    case "campaigns":
                        restMethodDefaultId.deleteCampaign(testData.getString(pageData,"body", "name"),"");
                        resWithoutID = restMethodDefaultId.rest(RestMethods.Method.POST,urlWithoutID + pageName,body);
                        cmpID = resWithoutID.get("id").getAsString();
                        String cmpIDDefault = null;
                        log.info(pageName + " just created id: " + cmpID);
                        cmpIDDefault = restMethodDefaultId.getCampaignID(testData.getString(pageData,"body","name"));
                        log.info(pageName + " id on default: " + cmpIDDefault);
                        Assert.assertTrue("id not match", cmpID.equals(cmpIDDefault));
                        restMethodDefaultId.deleteCampaign("",cmpIDDefault);
                        restMethodDefaultId.deleteStrategy(testData.getString(pageData,"body","strategy"),"");
                        restMethodDefaultId.deleteContactList(testData.getString(pageData,"body","contactList"),"");
                        break;
                    case "strategies":
                        restMethodDefaultId.deleteStrategy(testData.getString(pageData,"body","name"),"");
                        resWithoutID = restMethodWithoutId.rest(RestMethods.Method.POST,urlWithoutID + pageName,body);
                        String strID = resWithoutID.get("id").getAsString();
                        String strIDefault = null;
                        log.info(pageName + " just created id: " + strID);
                        strIDefault = restMethodDefaultId.getStrategyID(testData.getString(pageData,"body","name"));
                        log.info(pageName + " id on default: " + strIDefault);
                        Assert.assertTrue("id not match", strID.equals(strIDefault));
                        restMethodDefaultId.deleteContactList("",strIDefault);
                        break;
                }
                break;
            case "RUN":
                restMethodDefaultId.createContactList(testData.getData("contactlist"));
                ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
                String contactlistName = testData.getData("contactlist", "name");
                for (Map<String, String> attribute : attlist) {
                    if (!attribute.get("dataType").equals("SYSTEM")) {
                        restMethodDefaultId.createAttribute(attribute, contactlistName);
                    }
                }
                String dataSourceID = restMethodDefaultId.getDataSourceID(testData.getString("datasource","name"));
                String contactListID = restMethodDefaultId.getContactListID(testData.getString("contactlist","name"));
                urltmp = urlWithoutID + "contact-lists/" + contactListID
                        + "/data-sources/" + dataSourceID + "/run";
                log.info("Import contact using API with without account id");
                resWithoutID = restMethodWithoutId.rest(RestMethods.Method.POST,urltmp,"");
                utilityFun.wait(3);
                log.info("check job status using API with account id default");
                urltmp = urlDefault + "/" + "contact-lists/" + contactListID
                        + "/jobs/latest/";
                resDefault = restMethodDefaultId.rest(RestMethods.Method.GET,urltmp,"");
                log.info("total record is: " + resDefault.get("successCount").getAsString());
                Assert.assertEquals("total record not match", resDefault.get("successCount").getAsString(), testData.getString("contactlist","totalRecords"));
                break;
            case "START":
                restMethodDefaultId.deleteCampaign(testData.getString(pageData,"body", "name"),"");
                restMethodDefaultId.createCampaign(testData.getData(pageData,"body"));
                cmpID = restMethodDefaultId.getCampaignID(testData.getString(pageData,"body", "name"));
                urltmp = urlWithoutID + pageName + "/" + cmpID + "/execute";
                resWithoutID = restMethodWithoutId.rest(RestMethods.Method.POST,urltmp,"");
                jobID = resWithoutID.get("id").getAsString();
                log.info("get status with account id default");
                urltmp = urlDefault + "/" + pageName + "/jobs/" + jobID;
                resDefault = restMethodDefaultId.rest(RestMethods.Method.GET,urltmp,"");
                cmpDefaultName = resDefault.get("campaign").getAsJsonObject().get("name").getAsString();
                Assert.assertEquals("Name not match", cmpDefaultName, testData.getString(pageData,"body", "name"));
                restMethodDefaultId.deleteCampaign("",cmpID);
                restMethodDefaultId.deleteStrategy(testData.getString(pageData,"body","strategy"),"");
                restMethodDefaultId.deleteContactList(testData.getString(pageData,"body","contactList"),"");
                break;
            case "STOP":
                restMethodDefaultId.deleteCampaign(testData.getString(pageData,"body", "name"),"");
                restMethodDefaultId.createCampaign(testData.getData(pageData,"body"));
                cmpID = restMethodDefaultId.getCampaignID(testData.getString(pageData,"body", "name"));
                urltmp = urlDefault + "/" + pageName + "/" + cmpID + "/execute";
                resDefault = restMethodDefaultId.rest(RestMethods.Method.POST,urltmp,"");
                jobID = resDefault.get("id").getAsString();
                urltmp = urlWithoutID + pageName + "/" + cmpID + "/jobs/" + jobID + "/stop";
                resWithoutID = restMethodWithoutId.rest(RestMethods.Method.POST,urltmp,"");
                JsonObject  result = resWithoutID.get("result").getAsJsonArray().get(0).getAsJsonObject();
                Assert.assertEquals("error code not match", testData.getString(pageData, "errorCode"), result.get("errorCode").toString());
                restMethodDefaultId.deleteCampaign("",cmpID);
                restMethodDefaultId.deleteStrategy(testData.getString(pageData,"body","strategy"),"");
                restMethodDefaultId.deleteContactList(testData.getString(pageData,"body","contactList"),"");
                break;
            case "PUT":
                switch (pageName){
                    case "campaigns":
                        restMethodDefaultId.deleteCampaign(testData.getString(pageData,"body", "name"),"");
                        restMethodDefaultId.createCampaign(testData.getData(pageData,"body"));
                        cmpID = restMethodDefaultId.getCampaignID(testData.getString(pageData,"body", "name"));
                        urltmp = urlWithoutID + pageName + "/" + cmpID;
                        Map<String, String> bodyUpdateMap = (Map) testData.getData(pageData,"bodyUpdate");
                        ObjectMapper mapperUpdate = new ObjectMapper();
                        String bodyUpdate = mapperUpdate.writerWithDefaultPrettyPrinter().writeValueAsString(bodyUpdateMap);
                        log.info(bodyUpdate);
                        resWithoutID = restMethodWithoutId.rest(RestMethods.Method.PUT,urltmp,bodyUpdate);
                        Assert.assertEquals("campaign ID not match", cmpID, resWithoutID.get("id").getAsString());
                        restMethodDefaultId.deleteCampaign("",cmpID);
                        restMethodDefaultId.deleteStrategy(testData.getString(pageData,"body","strategy"),"");
                        restMethodDefaultId.deleteContactList(testData.getString(pageData,"body","contactList"),"");
                        break;
                    case "strategies":
                        restMethodDefaultId.deleteStrategy(testData.getString(pageData,"body", "name"), "");
                        restMethodDefaultId.createStrategy(testData.getData(pageData,"body"));
                        cmpID = restMethodDefaultId.getStrategyID(testData.getString(pageData,"body", "name"));
                        urltmp = urlWithoutID + pageName + "/" + cmpID;
                        Map<String, String> bodyStrUpdateMap = (Map) testData.getData(pageData,"bodyUpdate");
                        ObjectMapper mapperStrUpdate = new ObjectMapper();
                        String bodyStrUpdate = mapperStrUpdate.writerWithDefaultPrettyPrinter().writeValueAsString(bodyStrUpdateMap);
                        log.info(bodyStrUpdate);
                        resWithoutID = restMethodWithoutId.rest(RestMethods.Method.PUT,urltmp,bodyStrUpdate);
                        Assert.assertEquals("campaign ID not match", cmpID, resWithoutID.get("id").getAsString());
                        restMethodDefaultId.deleteStrategy(testData.getString(pageData,"body","strategy"),"");
                        break;
                }
                break;
        }
    }
}
