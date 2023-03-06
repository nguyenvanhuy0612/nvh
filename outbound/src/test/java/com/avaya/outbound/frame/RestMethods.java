package com.avaya.outbound.frame;

import com.avaya.outbound.lib.EnvSetup;
import com.google.gson.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;

public class RestMethods {
    static {
        RestAssured.useRelaxedHTTPSValidation("TLSv1.2");
    }

    // Define Logger
    private final Logger log = LogManager.getLogger(RestMethods.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
    public String BASEURL;
    public String DB_HOST;
    public String DB_USER;
    public String DB_PASSWORD;
    public String DB_PORT;
    public RequestSpecification requestSpec;
    public String token;

    public RestMethods(String BASEURL, String DB_USER, String DB_PASSWORD) {
        this.BASEURL = BASEURL;
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;
        this.DB_HOST = EnvSetup.extractHostname(BASEURL);
        this.DB_PORT = EnvSetup.extractPort(BASEURL);
        // todo pending: use username and password to get sso-token as default, this is temporary use token from browser cookie
        // todo username and password will no longer support on OB
        this.token = EnvSetup.ssoToken.get();
        configureToken(this.token);
    }

    public RestMethods(String BASEURL, String SSOTOKEN) {
        this.token = SSOTOKEN;
        this.BASEURL = BASEURL;
        this.DB_HOST = EnvSetup.extractHostname(BASEURL);
        this.DB_PORT = EnvSetup.extractPort(BASEURL);
        configureToken(SSOTOKEN);
    }

    public RestMethods() {
        this(EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL, EnvSetup.DB_USER, EnvSetup.DB_PASSWORD);
    }

    public void configureToken(String SSOTOKEN) {
        if (SSOTOKEN == null || SSOTOKEN.isEmpty()) {
            this.requestSpec = new RequestSpecBuilder().build()
                    .header("X-Requested-With", "RESTWeb")
                    .header("Cache-Control", "no-cache");
        } else if (SSOTOKEN.equalsIgnoreCase("BrowserCookie")) {
            this.token = EnvSetup.ssoToken.get();
            this.requestSpec = new RequestSpecBuilder().build()
                    .header("Authorization", "Bearer " + this.token)
                    .header("X-Requested-With", "RESTWeb")
                    .header("Cache-Control", "no-cache");
        } else {
            this.requestSpec = new RequestSpecBuilder().build()
                    .header("Authorization", "Bearer " + SSOTOKEN)
                    .header("X-Requested-With", "RESTWeb")
                    .header("Cache-Control", "no-cache");
        }
    }

    public static List<Cookie> fetchNGMCookies(String username, String password) {
        String baseUrl = "https://" + EnvSetup.NGM_URL;
        String resolverUrl = baseUrl + "/api/admin/account/v1alpha/ui/account-resolver";
        String body = """
                {
                	"username": "%s",
                	"state": "%s/services/ApplicationCenter",
                	"accountName": ""
                }
                """.formatted(username, baseUrl);
        // account-resolver
        Response response = given().redirects().follow(false).contentType(ContentType.JSON)
                .when().body(body).post(resolverUrl)
                .then().contentType(ContentType.JSON).extract().response();
        String redirectUrl = response.path("redirect_url");

        // ?tenantId=UCYUNV
        response = given().redirects().follow(false)
                .get(redirectUrl);
        String location = response.header("location");
        List<Cookie> allList = new ArrayList<>(response.detailedCookies().asList());

        // /uwf/oauth/authorize?state=L3NlcnZpY2VzL0FwcGxpY2F0aW9uQ2VudGVy
        response = given().redirects().follow(false)
                .cookies(new Cookies(allList))
                .get(baseUrl + location);
        location = response.header("location");
        allList.addAll(response.detailedCookies().asList());

        // https://dev-8.ixcc-sandbox.avayacloud.com/auth/realms/UCYUNV/protocol/openid-connect/auth?client_id=uwf...
        allList.add(new Cookie.Builder("auth_TR_USERNAME", URLEncoder.encode(username, StandardCharsets.UTF_8)).build());
        response = given().redirects().follow(false)
                .urlEncodingEnabled(false).cookies(new Cookies(allList))
                .get(location);
        location = response.getBody().asString()
                .replaceAll("[\\s\\S]+?action=\"(" + baseUrl + "[^\"]*)\"[\\s\\S]+", "$1")
                .replaceAll("&amp;", "&");
        allList.addAll(response.detailedCookies().asList());

        // https://dev-8.ixcc-sandbox.avayacloud.com/auth/realms/UCYUNV/login-actions/authenticate?session_code=vT9pcDre-bbGghN0AUeyeAdkx01N
        response = given().redirects().follow(false)
                .cookies(new Cookies(allList))
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", username)
                .formParam("password", password)
                .post(location);
        location = response.header("location");
        allList.addAll(response.detailedCookies().asList());

        // https://dev-8.ixcc-sandbox.avayacloud.com/uwf/oauth/callback?state=L3NlcnZpY2VzL0FwcGxpY2F0aW9uQ2VudGVy&sessio
        response = given().redirects().follow(false)
                .contentType(ContentType.HTML)
                .cookies(new Cookies(allList))
                .get(location);
        allList.addAll(response.detailedCookies().asList());

        // return simple map cookies
        // Map<String, String> simpleCookies = allList.stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue, (existing, replacement) -> existing));
        // System.out.println(simpleCookies);
        // String sso_access = response.cookies().get("sso-access");
        // return simpleCookies;
        return allList;
    }

//    public static void main(String[] args) {
//        new EnvSetup();
//        List<Cookie> cookieList = fetchNGMCookies("admin2@pomdev1.com", "Avaya@123$");
//        System.out.println(cookieList);
//        WebDriver driver = new ChromeDriver();
//        List<org.openqa.selenium.Cookie> cookies = cookieList.stream().map(cookie -> new org.openqa.selenium.Cookie(
//                cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getExpiryDate(),
//                cookie.isSecured(), cookie.isHttpOnly(), cookie.getSameSite())
//        ).toList();
//        driver.get("https://dev-8.ixcc-sandbox.avayacloud.com/services/ApplicationCenter");
//        for (org.openqa.selenium.Cookie cookie : cookies) {
//            try {
//                driver.manage().addCookie(cookie);
//            }catch (Exception e){}
//        }
//        driver.get("https://dev-8.ixcc-sandbox.avayacloud.com/services/ApplicationCenter");
//        System.out.println();
//    }

    public JsonObject rest(Method method, String url, String body, String username, String password, String filename) {
        log.info("-----------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("URL <" + url + ">");
        log.info("body <" + body + ">");
        log.info("method <" + method.name() + ">");
        log.info("filename <" + filename + ">");
        log.info("token <" + this.token + ">");
        try {
            Response response = switch (method) {
                case GET -> given().spec(this.requestSpec).contentType(ContentType.JSON).body(body)
                        .when().get(url)
                        .then().contentType(ContentType.JSON).extract().response();
                case POST -> given().spec(this.requestSpec).contentType(ContentType.JSON).body(body)
                        .when().post(url)
                        .then().extract().response();
                case PUT -> given().spec(this.requestSpec).contentType(ContentType.JSON).body(body)
                        .when().put(url)
                        .then().extract().response();
                case DELETE -> given().spec(this.requestSpec).contentType(ContentType.JSON).body(body)
                        .when().delete(url)
                        .then().extract().response();
                case FILE -> given().spec(this.requestSpec).contentType(ContentType.MULTIPART)
                        .multiPart("uploadedXMLFile", new File(filename), "multipart/form-data")
                        .when().post(url)
                        .then().extract().response();
                default -> null;
            };
            String resString = response == null ? null : response.asString();
            log.info(method.name() + " result ---> " + resString);
            log.info("resString = " + resString);
            if (resString == null || resString.isEmpty()) {
                return null;
            }
            JsonElement resJsonElement = gson.fromJson(resString, JsonElement.class);
            if (resJsonElement.isJsonObject()) {
                return resJsonElement.getAsJsonObject();
            } else if (resJsonElement.isJsonArray() || resJsonElement.isJsonPrimitive()) {
                JsonObject resJsonObject = new JsonObject();
                resJsonObject.add("result", resJsonElement);
                return resJsonObject;
            }
        } catch (Exception e) {
            log.info("Exception while making API request", e);
            e.printStackTrace();
        }
        return null;
    }

    public JsonObject rest(Method method, String url, String body) {
        if (method == Method.FILE) {
            log.info("Not support `FILE` method in the short invoke, use full invoke instead");
            return null;
        }
        return rest(method, url, body, DB_USER, DB_PASSWORD, "");
    }

    //================================================ common ==========================================================

    public JsonArray getAll(Page pageName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("PageName: " + pageName);
        JsonArray totals = new JsonArray();
        try {
            String baseUrl = BASEURL + "/" + pageName.value + "?pageSize=1000&pageNumber=";
            JsonObject result = rest(Method.GET, baseUrl + 0, "");
            JsonElement element = result.getAsJsonObject().get("result");
            if (element == null || !element.isJsonArray()) {
                return totals;
            }
            totals.addAll(element.getAsJsonArray());
            long totalRecords = result.get("totalRecords").getAsLong();
            if (totalRecords > 1000) {
                long pages = totalRecords / 1000 + 1;
                for (int i = 1; i <= pages; i++) {
                    result = rest(Method.GET, baseUrl + i, "");
                    element = result.getAsJsonObject().get("result");
                    if (element == null || !element.isJsonArray()) {
                        break;
                    }
                    totals.addAll(element.getAsJsonArray());
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return totals;
    }

    public List<String> getAll(Page pageName, String field) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("PageName: " + pageName);
        log.info("Field: " + field);
        List<String> totals = new ArrayList<>();
        JsonArray jsonArray = this.getAll(pageName);
        for (JsonElement element : jsonArray) {
            totals.add(elementToString(element.getAsJsonObject().get(field)));
        }
        log.info("Totals: " + totals.size());
        return totals;
    }

    public JsonArray getAllFilterNameLike(Page pageName, String name) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("PageName: " + pageName);
        log.info("name: " + name);
        JsonArray totals = new JsonArray();
        try {
            String baseUrl = BASEURL + "/" + pageName.value + "?pageSize=1000&searchValue=" + name + "&searchBy=name&searchOperator=like&pageNumber=";
            JsonObject result = rest(Method.GET, baseUrl + 0, "");
            JsonElement element = result.getAsJsonObject().get("result");
            if (element == null || !element.isJsonArray()) {
                return totals;
            }
            totals.addAll(element.getAsJsonArray());
            long totalRecords = result.get("totalRecords").getAsLong();
            if (totalRecords > 1000) {
                long pages = totalRecords / 1000 + 1;
                for (int i = 1; i <= pages; i++) {
                    result = rest(Method.GET, baseUrl + i, "");
                    element = result.getAsJsonObject().get("result");
                    if (element == null || !element.isJsonArray()) {
                        break;
                    }
                    totals.addAll(element.getAsJsonArray());
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return totals;
    }

    public void deleteAll(Page page) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            this.getAll(page, "id").forEach(id -> {
                String url = BASEURL + "/" + page.value + "/" + id;
                log.info("Deleting : " + url);
                rest(Method.DELETE, url, "");
            });
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public void deleteAll(Page page, String name) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            this.getAllFilterNameLike(page, name).forEach(element -> {
                String url = BASEURL + "/" + page.value + "/" + element.getAsJsonObject().get("id").getAsString();
                log.info("Deleting : " + url);
                rest(Method.DELETE, url, "");
            });
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private String elementToString(JsonElement element) {
        Objects.requireNonNull(element);
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsJsonPrimitive().getAsString();
        } else {
            return String.valueOf(element);
        }
    }

    //=================================================== Campaign  =================================================

    public String getCampaignID(String campaignName) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("campaignName: " + campaignName);
            JsonArray array = getAllFilterNameLike(Page.campaign, campaignName);
            for (JsonElement element : array) {
                if (element.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(campaignName)) {
                    return element.getAsJsonObject().get("id").getAsString();
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "";
    }

    public int getAllNumberOfRecord() {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            String url = BASEURL + "/campaigns";
            log.info("url: " + url);
            JsonObject result = rest(Method.GET, url, "");
            log.info(result);
            int total = result.getAsJsonObject().get("totalRecords").getAsInt();
            log.info("totalRecords: " + total);
            return total;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception while getting number of records, return: -1");
            return -1;
        }
    }

    public JsonObject deleteCampaign(String campaignName, String id) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("campaignName: " + campaignName + "\nid: " + id);
            if (id == null || id.isEmpty()) {
                id = this.getCampaignID(campaignName);
            }
            if (id.isEmpty()) {
                log.info("Not found campaign");
                return null;
            }
            String url = BASEURL + "/campaigns/" + id;
            log.info("url: " + url);
            JsonObject result = rest(Method.DELETE, url, "");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    public void deleteAllCampaign() {
        this.deleteAll(Page.campaign);
    }

    public void deleteAllCampaign(String containName) {
        this.deleteAll(Page.campaign, containName);
    }

    private String getCampaignBody(Map<String, String> testData) {
        JsonObject bodyJson = new JsonObject();
        bodyJson.addProperty("name", testData.get("name") == null ? testData.get("Campaign Name") : testData.get("name"));
        bodyJson.addProperty("description", testData.get("description") == null ? testData.get("Description") : testData.get("description"));
        bodyJson.addProperty("type", testData.get("type") == null ? testData.get("Campaign Type").toUpperCase() : testData.get("type").toUpperCase());
        bodyJson.addProperty("contactList", testData.get("contactList") == null ? testData.get("name") : testData.get("contactList"));
        bodyJson.addProperty("strategy", testData.get("strategy"));
        bodyJson.addProperty("senderDisplayName", testData.get("senderDisplayName") == null ? testData.getOrDefault("SenderName", "") : testData.getOrDefault("senderDisplayName", ""));
        bodyJson.addProperty("senderAddress", testData.getOrDefault("senderAddress", ""));
        String dataDialingOrder = testData.get("dialingOrder") == null ? testData.get("Dialing Order") : testData.get("dialingOrder");
        JsonArray orderArray = Stream.of(dataDialingOrder.toUpperCase().split(",")).collect(JsonArray::new,
                (array, str) -> array.add(str.trim()), JsonArray::add);
        bodyJson.add("dialingOrder", orderArray);
        if (testData.getOrDefault("finishType", "").isEmpty()) {
            bodyJson.addProperty("finishType", "FINISH_AFTER");
            bodyJson.addProperty("finishAfter", "0");
        } else if (testData.get("finishType").equalsIgnoreCase("FINISH_AFTER")) {
            bodyJson.addProperty("finishType", "FINISH_AFTER");
            bodyJson.addProperty("finishTime", testData.getOrDefault("finishTime", null));
            bodyJson.addProperty("finishAfter", testData.getOrDefault("finishAfter", "0"));
        } else {
            bodyJson.addProperty("finishType", "FINISH_AT");
            bodyJson.addProperty("finishTime", testData.get("finishTime"));
            bodyJson.addProperty("finishAfter", "null");
        }
        bodyJson.addProperty("checkTimeBasedFinishCriteria", testData.getOrDefault("checkTimeBasedFinishCriteria", "false"));
        log.info(bodyJson);
        return bodyJson.toString();
    }

    public JsonObject createCampaign(Map<String, String> testData) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = BASEURL + "/campaigns";
        String body = getCampaignBody(testData);
        log.info(url);
        log.info(body);
        JsonObject result = rest(Method.POST, url, body);
        log.info(result);
        return result;
    }

    /**
     * Method to start campaign by API
     *
     * @param campaignName: Name of campaign
     * @param id:           id of campaign
     *                      return result
     */
    public JsonObject startCampaignByAPI(String campaignName, String id) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            if (id == null || id.isEmpty()) {
                id = this.getCampaignID(campaignName);
            }
            if (id.isEmpty()) {
                log.info("Not found campaign name as: " + campaignName);
                return null;
            }
            log.info("Trying stating campaign name: " + campaignName);
            String url = BASEURL + "/campaigns/" + id + "/execute";
            return rest(Method.POST, url, "");
        } catch (Exception e) {
            log.info("!!!ERR: Some thing went wrong while starting campaign!!!", e);
            return null;
        }
    }

    /**
     * Method to get Last Executed value of a campaign
     *
     * @param campaignName: Name of campaign
     * @return Last Executed value with LocalDateTime type
     */
    public LocalDateTime getLastExecutedCampaign(String campaignName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("Campaign Name: " + campaignName);
            JsonArray array = this.getAllFilterNameLike(Page.campaign, campaignName);
            log.info(array);
            for (JsonElement element : array) {
                if (element.getAsJsonObject().get("name").getAsString().contentEquals(campaignName)) {
                    String lastExecutedOn = element.getAsJsonObject().get("lastExecutedOn").getAsString().trim().replaceAll("([\\d-]*)T([\\d:].*)\\.\\d*Z", "$1 $2");
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                    LocalDateTime lastExecutedOnTime = LocalDateTime.parse(lastExecutedOn, dateTimeFormatter);
                    ZonedDateTime lastExecutedOnTimeZone = lastExecutedOnTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault());
                    return lastExecutedOnTimeZone.toLocalDateTime().withSecond(0);
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    public JsonObject getActiveJob(String campName, String id) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Campaign Name: " + campName);
        log.info("Campaign id: " + id);
        try {
            if (id == null || id.isEmpty()) {
                id = this.getCampaignID(campName);
            }
            if (id.isEmpty()) {
                log.info("Not found campaign name as: " + campName);
                return null;
            }
            String url = BASEURL + "/" + Page.campaign.value + "/" + id + "/active-jobs";
            return rest(Method.GET, url, "");
        } catch (Exception e) {
            log.info("!!!ERR: Some thing went wrong while getActiveJob!!!", e);
        }
        return null;
    }

    /**
     * @param campName
     */
    public List<JsonObject> getAttempts(String campName) {
        List<JsonObject> results = new ArrayList<>();
        log.info("Campaign Name: " + campName);
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            String url = BASEURL + "/campaigns/" + campName + "/latest-attempts?pageSize=1000";
            JsonObject result = rest(Method.GET, url, "");
            for (JsonElement jsonElement : result.get("result").getAsJsonArray()) {
                results.add(jsonElement.getAsJsonObject());
            }
            log.info("results: " + results);
            return results;
        } catch (Exception e) {
            log.info("Exception while getting the list of attempt nodes: return an empty list", e);
            return results;
        }
    }

    /**
     * @param campName
     * @param keys
     */
    public Map<String, List<String>> getAttempts(String campName, String... keys) {
        List<JsonObject> attempts = getAttempts(campName);
        Map<String, List<String>> results = new HashMap<>();
        for (String key : keys) {
            try {
                List<String> r = new ArrayList<>();
                for (JsonObject object : attempts) {
                    Objects.requireNonNull(object);
                    r.add(elementToString(object.get(key)));
                }
                results.put(key, r);
            } catch (Exception e) {
                log.info("Cannot get attempt list with key " + key, e);
            }
        }
        return results;
    }

    /**
     * @param campaignName : name campaign get time attribute
     * @param key:         attribute user get time
     * @return: localDateTime
     */
    public LocalDateTime getTimeForRunCampaign(String campaignName, String key) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Campaign Name: " + campaignName);
        log.info("attribute get time: " + key);
        try {
            String url = BASEURL + "/jobs/" + campaignName;
            JsonObject result = rest(Method.GET, url, "");
            String timeString = result.getAsJsonObject().get(key).getAsString().trim().replaceAll("([\\d-]*)T([\\d:].*)\\.\\d*Z", "$1 $2");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
            LocalDateTime lastExecutedOnTime = LocalDateTime.parse(timeString, dateTimeFormatter);
            ZonedDateTime lastExecutedOnTimeZone = lastExecutedOnTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault());
            return lastExecutedOnTimeZone.toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method to get attribute value of job-campaign result
     */
    public String getAttributeValueOfJobCampaign(String campaignName, String attribute) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Campaign Name: " + campaignName);
        try {
            String url = BASEURL + "/jobs/" + campaignName;
            JsonObject result = rest(Method.GET, url, "");
            return result.get(attribute).getAsString();
        } catch (Exception e) {
            log.info("!!!ERR: Some thing went wrong while get campaign job latest!!!");
            return null;
        }
    }

    /**
     * @param campaignName: campaign user want stop
     * @param id:           campaign id
     * @return
     */
    public JsonObject stopCampaignByAPI(String campaignName, String id) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            if (id == null || id.isEmpty()) {
                id = this.getCampaignID(campaignName);
            }
            if (id.isEmpty()) {
                log.info("Not found campaign name as: " + campaignName);
                return null;
            }
            log.info("Trying stop campaign name: " + campaignName);
            String jobId = this.getAttributeValueOfJobCampaign(campaignName, "id");
            String url = BASEURL + "/campaigns/" + id + "/jobs/" + jobId + "/stop";
            return rest(Method.POST, url, "");
        } catch (Exception e) {
            log.info("!!!ERR: Some thing went wrong while stop campaign!!!", e);
            return null;
        }
    }

    public void stopAllCampaigns() {
        stopAllCampaigns(0);
    }

    public void stopAllCampaigns(int timeMonitor) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            this.getAll(Page.monitorActive).forEach(obj -> {
                JsonObject object = obj.getAsJsonObject();
                String id = object.get("id").getAsString();
                String campaignId = object.get("campaignId").getAsString();
                log.info("Stop campaign " + object.get("campaignName").getAsString());
                String url = BASEURL + "/campaigns/" + campaignId + "/jobs/" + id + "/stop";
                log.info("url " + url);
                rest(Method.POST, url, "");
            });
            if (timeMonitor > 0) {
                log.info("Wait for all campaigns stop successfully");
                Instant start = Clock.systemDefaultZone().instant();
                Instant end = start.plus(Duration.ofSeconds(timeMonitor));
                while (end.isAfter(start)) {
                    if (getAll(Page.monitorActive).size() == 0) {
                        break;
                    }
                    Thread.sleep(3000);
                }
                log.info("Total monitor time: " + Duration.between(start, Clock.systemDefaultZone().instant()).toSeconds() + " Second(s)");
            }
        } catch (Exception e) {
            log.info("!!!ERR: Some thing went wrong while stop campaign!!!", e);
        }
    }

    public void stopAllCampaigns(String campaignName) {
        stopAllCampaigns(campaignName, 0);
    }

    public void stopAllCampaigns(String campaignName, int timeMonitor) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("Stop campaign with name contains " + campaignName);
            this.getAll(Page.monitorActive).forEach(obj -> {
                JsonObject object = obj.getAsJsonObject();
                String campName = object.get("campaignName").getAsString();
                if (campName.contains(campaignName)) {
                    String id = object.get("id").getAsString();
                    String campaignId = object.get("campaignId").getAsString();
                    String url = BASEURL + "/campaigns/" + campaignId + "/jobs/" + id + "/stop";
                    log.info("Stop campaign using url: " + url);
                    rest(Method.POST, url, "");
                }
            });
            if (timeMonitor > 0) {
                log.info("wait for all campaign contains name \"" + campaignName + "\" stop successfully");
                Instant start = Clock.systemDefaultZone().instant();
                Instant end = start.plus(Duration.ofSeconds(timeMonitor));
                wait:
                while (end.isAfter(start)) {
                    JsonArray elements = getAll(Page.monitorActive);
                    boolean isCompleted = true;
                    checkCamp:
                    for (JsonElement obj : elements) {
                        JsonObject object = obj.getAsJsonObject();
                        String campName = object.get("campaignName").getAsString();
                        if (campName.contains(campaignName)) {
                            isCompleted = false;
                            Thread.sleep(3000);
                            break checkCamp;
                        }
                    }
                    if (isCompleted) {
                        break wait;
                    }
                }
                log.info("Total monitor time: " + Duration.between(start, Clock.systemDefaultZone().instant()).toSeconds() + " Second(s)");
            }
        } catch (Exception e) {
            log.info("!!!ERR: Some thing went wrong while stop campaign!!!", e);
        }
    }

    // ============================== Strategy ===========================================

    private String getStrategySMSBody(Map<String, String> testData) {
        String strategyName = testData.get("name");
        String strategyDescription = testData.get("description");
        String strategySmsText = testData.get("smsText");
        String strategySmsPace = testData.get("smsPace");
        String strategySmsPacingTimeUnit = testData.get("smsPacingTimeUnit").toUpperCase();
        log.info("Strategy name: " + strategyName);
        log.info("Strategy Description: " + strategyDescription);
        log.info("Strategy sms text: " + strategySmsText);
        log.info("Strategy sms pace: " + strategySmsPace);
        log.info("Strategy sms text: " + strategySmsPacingTimeUnit);
        String body = "{\"name\": \"" + strategyName + "\",\"description\": \"" + strategyDescription + "\",\"smsText\": \""
                + strategySmsText + "\", \"smsPace\": " + strategySmsPace + ",\"smsPacingTimeUnit\": \"" + strategySmsPacingTimeUnit + "\" }";
        return body;
    }

    public JsonObject createStrategy(Map<String, String> testData) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = BASEURL + "/strategies";
        JsonObject result = null;
        String strategyType = testData.get("type") == null ? testData.get("strategyType") : testData.get("type");
        if (strategyType.equalsIgnoreCase("sms")) {
            String body = getStrategySMSBody(testData);
            log.info("Body: " + body);
            log.info("URL: " + url);
            result = rest(Method.POST, url, body);
            log.info(result);
        } else {
            log.info("Currently only support create strategy with sms type");
        }
        return result;
    }

    /**
     * Method to get campaign strategies
     *
     * @param
     * @return
     */
    public JsonArray getStrategyList(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL + "/strategies?pageNumber=" + data.get("pageNumber")
                + "&pageSize=" + data.get("pageSize") + "";
        log.info("URL: " + url);
        JsonObject result = null;
        result = rest(Method.GET, url, "");
        log.info(result);
        JsonArray res = null;
        if (result.has("result")) {
            res = result.get("result").getAsJsonArray();
            log.info(res);
        }
        return res;
    }

    public String getStrategyID(String name) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("name: " + name);
            JsonArray array = this.getAllFilterNameLike(Page.strategy, name);
            for (JsonElement element : array) {
                if (element.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(name)) {
                    return element.getAsJsonObject().get("id").getAsString();
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "";
    }

    public JsonObject deleteStrategy(String name, String id) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("name: " + name + "\nid: " + id);
            if (id == null || id.isEmpty()) {
                id = this.getStrategyID(name);
            }
            if (id.isEmpty()) {
                log.info("Not found strategy");
                return null;
            }
            String url = BASEURL + "/strategies/" + id;
            log.info("url: " + url);
            JsonObject result = rest(Method.DELETE, url, "");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public void deleteAllStrategies(String name) {
        this.deleteAll(Page.strategy, name);
    }

    public void deleteAllStrategies() {
        this.deleteAll(Page.strategy);
    }

    /**
     * Method to get Strategy ID
     *
     * @param strategyName
     * @return null
     * String ID
     */
    public String getStrategyID(String strategyName, String strategyType) {
        return this.getStrategyID(strategyName);
    }

    /**
     * Method to delete Strategy by ID or Name
     *
     * @param strategyName, id
     * @return True: delete successfully
     * False: delete unsuccessfully
     */
    public boolean deleteStrategy(String strategyType, String strategyName, String id) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("Strategy Name: " + strategyName + "\nid: " + id);
            if (id == null || id.isEmpty()) {
                id = this.getStrategyID(strategyName, strategyType);
            }
            if (id == null || id.isEmpty()) {
                log.info("!!!Not found contactList!!!");
                return false;
            }
            String url = BASEURL + "/strategies/" + id;
            log.info("url: " + url);
            JsonObject result = this.rest(Method.DELETE, url, "");
            //handle result to make sure the result is correct: todo later
            log.info(result);
            return true;
        } catch (Exception e) {
            log.info("!!!ERR: something went wrong when deleting strategy!!!");
            log.info(e.getMessage());
            return false;
        }
    }

    /**
     * Method to get list strategies that contains specific string from API
     *
     * @param string
     * @return return List strategies that contains string
     */
    public List<String> getStrategyContainStringFromAPI(String string, Map<String, String> data) {
        List<String> listStrategy = new ArrayList<String>();
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            JsonArray res = this.getStrategyList(data);
            log.info(res);
            for (int i = 0; i < res.size(); i++) {
                JsonObject strategyJsonObj = (JsonObject) res.get(Integer.parseInt(String.valueOf(i)));
                if (strategyJsonObj.get("name").getAsString().toLowerCase().contains(string.toLowerCase())) {
                    listStrategy.add(strategyJsonObj.get("name").getAsString());
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return listStrategy;
    }

    /**
     * Method to get list all strategies and sort acesding or desceding specific string from API
     *
     * @param
     * @return return List strategy that sorted following option, default is not sort
     */
    public List<String> getAllStrategyAndSortStringFromAPI(String option, Map<String, String> data) {
        List<String> listStrategy = new ArrayList<String>();
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            JsonArray res = this.getStrategyList(data);
            log.info(res);
            for (int i = 0; i < res.size(); i++) {
                JsonObject strategyJsonObj = (JsonObject) res.get(Integer.parseInt(String.valueOf(i)));
                listStrategy.add(strategyJsonObj.get("name").getAsString());
            }
            switch (option) {
                case "asc":
                    Collections.sort(listStrategy);
                    break;
                case "dsc":
                    listStrategy.sort(Comparator.reverseOrder());
                    break;
                default:
                    log.info("return list strategy by default");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return listStrategy;
    }

    // ============================== contact list ===========================================

    public int getAllNumberOfContactList() {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            String url = BASEURL + "/contact-lists?pageSize=1000";
            log.info("url: " + url);
            JsonObject result = rest(Method.GET, url, "");
            log.info(result);
            int total = result.get("totalRecords").getAsInt();
            log.info("totalRecords: " + total);
            return total;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception while getting number of records, return: -1");
            return -1;
        }
    }

    public String getContactListID(String contactListName) {
        try {
            log.info("contactListName: " + contactListName);
            JsonArray array = this.getAllFilterNameLike(Page.contactlist, contactListName);
            for (JsonElement element : array) {
                if (element.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(contactListName)) {
                    return element.getAsJsonObject().get("id").getAsString();
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "";
    }

    public JsonObject createContactList(Map<String, String> testData) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            String name = testData.get("name");
            String description = testData.get("description");
            String body = "{\"name\": \"" + name + "\",\"description\": \"" + description + "\"}";
            String url = BASEURL + "/contact-lists";
            log.info("url: " + url);
            log.info("body: " + body);
            JsonObject result = rest(Method.POST, url, body);
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    public JsonObject deleteContactList(String contactListName, String id) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("contactListName: " + contactListName + "\nid: " + id);
            if (id == null || id.isEmpty()) {
                id = this.getContactListID(contactListName);
            }
            if (id.isEmpty()) {
                log.info("Not found contactList");
                return null;
            }
            String url = BASEURL + "/contact-lists/" + id;
            log.info("url: " + url);
            JsonObject result = rest(Method.DELETE, url, "");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    public void deleteAllContactList() {
        deleteAll(Page.contactlist);
    }

    public void deleteAllContactList(String containsName) {
        this.deleteAll(Page.contactlist, containsName);
    }

    /**
     * Method to get all contacts from List
     *
     * @param lname
     * @return
     */
    public JsonArray getAllContactsFromList(String lname, int pagenumber) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        JsonArray contacts = null;
        String lid = this.getContactListID(lname);
        String url = String.format(BASEURL + "/contact-lists/%s/contacts?sortBy=createdOn&sortDirection=DESC&pageNumber=%s&pageSize=100", lid, pagenumber);
        log.info("url: " + url);
        JsonObject result = rest(Method.GET, url, "");
        //below code needs to be changed based on API changes in future
        if (!result.get("totalRecords").toString().contentEquals("0")) {
            log.info(result.size());
            log.info(result.get("result"));
            //contacts = (JsonArray) result.get("contacts");
            contacts = (JsonArray) result.get("result");
        } else {
            log.info("no contacts found");
        }
        return contacts;
    }

    /**
     * Method to get Last updated value of a contact list
     *
     * @param contactListName: Name of contact list
     * @return Last updated value with LocalDateTime type
     */
    public LocalDateTime getContactListLastUpdate(String contactListName) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("contactListName: " + contactListName);
            String url = BASEURL + "/contact-lists?pageSize=1000&searchValue=" + contactListName + "&searchBy=name&searchOperator=LIKE";
            log.info("url: " + url);
            JsonObject result = rest(Method.GET, url, "");
            log.info(result);
            for (JsonElement element : result.get("result").getAsJsonArray()) {
                if (element.getAsJsonObject().get("name").getAsString().contentEquals(contactListName)) {
                    String updatedOn = element.getAsJsonObject().get("updatedOn").getAsString().trim().replaceAll("([\\d-]*)T([\\d:].*)\\.\\d*Z", "$1 $2");
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                    String[] arr = updatedOn.trim().split(":");
                    updatedOn = arr[0] + ":" + arr[1] + ":00" + arr[2].substring(2);
                    LocalDateTime updatedOnTime = LocalDateTime.parse(updatedOn, dateTimeFormatter);
                    ZonedDateTime updatedOnTimeZone = updatedOnTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault());
                    return updatedOnTimeZone.toLocalDateTime();
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    /**
     * Method to get list data for Last Updated column by API
     *
     * @param
     * @return List data of Last updated column
     */
    public List<LocalDateTime> getListDataLastUpdatedByAPI() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<LocalDateTime> resultList = new ArrayList<>();
        try {
            String url = BASEURL + "/contact-lists?pageSize=1000";
            log.info("url: " + url);
            JsonObject result = rest(Method.GET, url, "");
            log.info(result);
            for (JsonElement element : result.get("result").getAsJsonArray()) {
                String updatedOn = element.getAsJsonObject().get("updatedOn").getAsString().trim();
                updatedOn = updatedOn.substring(0, updatedOn.indexOf(".")).replaceAll("T", " ");
                LocalDateTime updatedOnTime = LocalDateTime.parse(updatedOn, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
                ZonedDateTime updatedOnTimeZone = updatedOnTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault());
                resultList.add(updatedOnTimeZone.toLocalDateTime());
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return resultList;
    }

    public int getAllRecordContactList() {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            String url = BASEURL + "/contact-lists?sortBy=updatedOn&sortDirection=DESC&pageNumber=0&pageSize=1000";
            log.info("url: " + url);
            JsonObject result = rest(Method.GET, url, "");
            log.info(result);
            int total = result.getAsJsonObject().get("totalRecords").getAsInt();
            log.info("totalRecords: " + total);
            return total;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception while getting number of records, return: -1");
            return -1;
        }
    }

    /**
     * Method to get list data follow column name by API
     *
     * @param colName: name of column
     *                 return List data for column with column name inputted
     */
    public List<String> getListDataForEachColumnByAPI(String colName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> resultList = new ArrayList<>();
        try {
            String url = BASEURL + "/contact-lists?pageSize=1000";
            log.info("url: " + url);
            JsonObject result = rest(Method.GET, url, "");
            log.info(result);
            for (JsonElement element : result.get("result").getAsJsonArray()) {
                String value = element.getAsJsonObject().get(colName).getAsString().trim();
                resultList.add(value);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return resultList;
    }

    /**
     * Method to get Import Job details
     *
     * @param contactListName
     * @return
     */
    public JsonObject getImportJobDetails(String contactListName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("contactListName: " + contactListName);
            String contactID = this.getContactListID(contactListName);
            String url = BASEURL + "/contact-lists/" + contactID + "/jobs/latest/";
            log.info(url);
            JsonObject result = rest(Method.GET, url, "");
            return result;
        } catch (Exception e) {
            log.info("!!!ERR: Get Import Job details API failed !!!");
            log.info(e.getMessage());
            return null;
        }
    }

    public JsonObject runDataSourceByAPI(String contactListName, String dataSourceName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("contactListName: " + contactListName);
            log.info("contactListName: " + dataSourceName);
            String contactID = this.getContactListID(contactListName);
            String datasourceID = this.getDataSourceID(dataSourceName);
            String url = BASEURL + "/contact-lists/" + contactID + "/data-sources/" + datasourceID + "/run";
            JsonObject result = rest(Method.POST, url, "");
            return result;
        } catch (Exception e) {
            log.info("!!!ERR: something went wrong when import contact list!!!");
            log.info(e.getMessage());
            return null;
        }
    }

    /**
     * Description: Method API to get the current attributes of specific contact list
     *
     * @param contactListName
     * @return
     */
    public JsonObject getAllAttributeContactList(String contactListName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("contactListName: " + contactListName);
            String contactID = this.getContactListID(contactListName);
            String url = BASEURL + "/contact-lists/" + contactID + "/attributes?sortBy=updatedOn&sortDirection=DESC&pageSize=1000&pageNumber=0";
            JsonObject result = rest(Method.GET, url, "");
            return result;
        } catch (Exception e) {
            log.info("!!!ERR: Something went wrong when Get all Attributes of contact list!!!");
            log.info(e.getMessage());
            return null;
        }
    }

    /**
     * Method to add attributes by API
     *
     * @param data
     */
    public JsonObject createAttribute(Map<String, String> data, String contactList) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            JsonObject bodyJson = new JsonObject();
            bodyJson.addProperty("name", data.get("name"));
            bodyJson.addProperty("dataType", data.get("dataType"));
            bodyJson.addProperty("type", data.get("type"));
            String body = bodyJson.toString();
            String contactID = this.getContactListID(contactList);
            String url = BASEURL + "/contact-lists/" + contactID + "/attributes";
            log.info("url: " + url);
            log.info("body: " + body);
            JsonObject result = rest(Method.POST, url, body);
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    /**
     * Method to get current attribute list by API
     *
     * @param contactListName
     */
    public List<String> getListAttributeName(String contactListName) {
        List<String> records = new ArrayList<>();
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            JsonObject result = this.getAllAttributeContactList(contactListName);
            log.info(result);
            for (JsonElement jsonElement : result.getAsJsonObject().get("result").getAsJsonArray()) {
                records.add(jsonElement.getAsJsonObject().get("name").getAsString());
            }
            log.info("Attribute list: " + records);
            return records;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Exception while getting number of records, return: empty list");
            return records;
        }
    }

    /**
     * Description: Method to return current number attribute of a contact list
     *
     * @param contactListName
     * @return totalAttr
     */
    public int getNumberAttContactList(String contactListName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String totalAttr = this.getAllAttributeContactList(contactListName).getAsJsonObject().get("totalRecords").getAsString();
        return Integer.parseInt(totalAttr);
    }

    public JsonObject getCListJobLatest(String clistName, String id) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Contact List Name: " + clistName);
        log.info("Contact List id: " + id);
        try {
            if (id == null || id.isEmpty()) {
                id = this.getContactListID(clistName);
            }
            if (id.isEmpty()) {
                log.info("Not found contact list name as: " + clistName);
                return null;
            }
            String url = BASEURL + "/" + Page.contactlist.value + "/" + id + "/jobs/latest";
            return rest(Method.GET, url, "");
        } catch (Exception e) {
            log.info("!!!ERR: Some thing went wrong while getCListJobLatest!!!", e);
        }
        return null;
    }

    // ============================== data source ======================================================================

    public JsonObject createDataSource(Map<String, String> testData) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            JsonObject bodyJson = new JsonObject();
            bodyJson.addProperty("name", testData.get("name"));
            bodyJson.addProperty("description", testData.getOrDefault("description", ""));
            bodyJson.addProperty("ftpUserName", testData.get("ftpUserName"));
            bodyJson.addProperty("ftpPassword", testData.get("ftpPassword"));
            bodyJson.addProperty("ftpRemoteFilePath", testData.get("ftpRemoteFilePath"));
            bodyJson.addProperty("ftpIPHostName", testData.get("ftpIPHostName"));
            bodyJson.addProperty("dataSourceType", testData.get("dataSourceType"));
            if (testData.get("fieldDelimiter").contains("null")) {
                bodyJson.addProperty("fieldDelimiter", ",");
            } else {
                bodyJson.addProperty("fieldDelimiter", testData.get("fieldDelimiter"));
            }
            String body = bodyJson.toString();
            String url = BASEURL + "/data-sources";
            log.info("url: " + url);
            log.info("body: " + body);
            JsonObject result = rest(Method.POST, url, body);
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    public String getDataSourceID(String dataSourceName) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("dataSourceName: " + dataSourceName);
            JsonArray array = this.getAllFilterNameLike(Page.datasource, dataSourceName);
            for (JsonElement element : array) {
                if (element.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(dataSourceName)) {
                    return element.getAsJsonObject().get("id").getAsString();
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "";
    }

    public JsonObject deleteDataSource(String dataSourceName, String id) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("dataSourceName: " + dataSourceName + "\nid: " + id);
            if (id == null || id.isEmpty()) {
                id = this.getDataSourceID(dataSourceName);
            }
            if (id.isEmpty()) {
                log.info("Not found getDataSource");
                return null;
            }
            String url = BASEURL + "/data-sources/" + id;
            log.info("url: " + url);
            JsonObject result = rest(Method.DELETE, url, "");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public void deleteAllDatasource(String containsName) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            String url = BASEURL + "/data-sources?pageSize=1000&searchValue=" + containsName + "&searchBy=name&searchOperator=like";
            JsonObject result = rest(Method.GET, url, "");
            log.info(result);
            JsonArray jsonArray = result.getAsJsonObject().get("result").getAsJsonArray();
            log.info("Total data source: " + jsonArray.size());
            for (JsonElement element : jsonArray) {
                String id = element.getAsJsonObject().get("id").getAsString();
                String name = element.getAsJsonObject().get("name").getAsString();
                if (!name.contains(containsName))
                    continue;
                url = BASEURL + "/data-sources/" + id;
                log.info("Deleting : " + url);
                rest(Method.DELETE, url, "");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * Description: Method to delete Attribute of a Contact List
     *
     * @param contactListName
     * @param attributeName
     * @return
     */
    public JsonObject deleteAttributeContactList(String contactListName, String attributeName) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("ContactListName: " + contactListName + "\nAttribute Name: " + attributeName);
            String attrId = this.getAttributeID(contactListName, attributeName);
            String url = BASEURL + "/contact-lists/" + attrId + "/attributes";
            log.info("url: " + url);
            JsonObject result = rest(Method.DELETE, url, "");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    /**
     * Description: Method to return Attribute ID base on Attribute ID of a contact list
     *
     * @param contactListName
     * @param attributeName
     * @return Attribute ID
     */
    public String getAttributeID(String contactListName, String attributeName) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("Attribute Name: " + attributeName);
            String clistId = this.getContactListID(contactListName);
            String baseUrl = BASEURL + "/contact-lists/" + clistId + "/attributes?pageSize=1000&searchValue=" + attributeName + "&searchBy=name&searchOperator=" + "%3D" + "&pageNumber=";
            JsonObject result = rest(Method.GET, baseUrl + 0, "");
            JsonArray element = (JsonArray) result.get("result");
            log.info(element);
            return element.get(0).getAsJsonObject().get("id").getAsString();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "";
    }

    /**
     * enum method for rest method
     */
    public enum Method {
        GET, POST, PUT, DELETE, FILE
    }

    public enum Page {
        campaign("campaigns"),
        strategy("strategies"),
        contactlist("contact-lists"),
        datasource("data-sources"),
        monitorActive("campaigns/jobs/active");

        public final String value;

        Page(String value) {
            this.value = value;
        }
    }

    public JsonObject pauseCampaignByAPI(String campaignName, String id) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            if (id == null || id.isEmpty()) {
                id = this.getCampaignID(campaignName);
            }
            if (id.isEmpty()) {
                log.info("Not found campaign name as: " + campaignName);
                return null;
            }
            log.info("Trying pause campaign name: " + campaignName);
            String jobId = this.getAttributeValueOfJobCampaign(campaignName, "id");
            String url = BASEURL + "/campaigns/" + id + "/jobs/" + jobId + "/pause";
            return rest(Method.POST, url, "");
        } catch (Exception e) {
            log.info("!!!ERR: Some thing went wrong while pause campaign!!!", e);
            return null;
        }
    }
}
