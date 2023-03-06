package com.avaya.outbound.steps;

import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.netty.channel.ConnectTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SecureAPICommunicationDefs extends StepContext {
    private final Locator locator = new Locator();

    public SecureAPICommunicationDefs(Context context) {
        context.init(this);
    }

    @Before(order = 9000, value = "@SecureAPI")
    public void initPageObject() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
    @After(order = 9000, value = "@SecureAPI")
    public void afterCamp(Scenario scenario) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
    }
    @Then("^Verify all urls start with https after .+$")
    public void verifyAllUrlsInCurrentPageStartWithHTTPS() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ArrayList<String> urls = commonFunction.getURLs();
        log.info(urls);
        Assert.assertTrue("!!ERR: some urls is not start by https!!", commonFunction.isHttpsUrl(urls));
    }

    @Given("Verify user can {string} through https protocol")
    public void verifyConnectionToOutboundServices(String connection, List<Map<String, String>> table) throws IOException, InterruptedException {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ArrayList<String> urlList = new ArrayList<>();
        ArrayList<String> statusCodeList = new ArrayList<>();
        String url = null;
        Map<String, String> strategyData = (Map) testData.getData("Strategy");
        Map<String, String> campaign = (Map) testData.getData("Campaign");
        Map<String, String> contactList = (Map) testData.getData("ContactList");
        Map<String, String> dataSource = (Map) testData.getData("DataSource");
        for (Map<String, String> stringMap : table) {
            String body = null;
            String pageName = stringMap.get("PageName");
            switch (pageName) {
                case "strategies", "campaigns", "completion-codes" -> {
                    if (!stringMap.get("Method").equals("PUT")) {
                        url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL + "/" + pageName;
                    } else {
                        String id = null;
                        if (pageName.equals("strategies")) {
                            id = RestMethodsObj.getStrategyID(strategyData.get("name"));
                        } else {
                            id = RestMethodsObj.getCampaignID(campaign.get("name"));
                        }
                        url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL + "/" + pageName + "/" + id;
                    }
                    break;
                }
                case "contact-lists", "data-sources" -> {
                    url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL + "/" + pageName;
                    break;
                }
                case "Swagger-campaigns" -> {
                    url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL + "/" + "webjars/swagger-ui/index.html";
                    break;
                }
                case "Swagger-contacts" -> {
                    url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL + "/" + "webjars/swagger-ui/index.html";
                    break;
                }
                case "health-contacts" -> {
                    url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL + "/" + pageName.split("-")[0];
                    break;
                }
                case "health-campaigns" -> {
                    url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL + "/" + pageName.split("-")[0];
                    break;
                }
            }
            if (!(url == null)) {
                try {
                    Properties props = System.getProperties();
                    props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
                    HttpResponse<String> response;
                    HttpClient client = HttpClient.newHttpClient();
                    if (stringMap.get("Method").equals("GET") || stringMap.get("Method").equals("DELETE")) {
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .build();
                        response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    } else if (stringMap.get("Method").equals("POST")) {
                        ObjectMapper mapper = new ObjectMapper();
                        switch (pageName) {
                            case "strategies" ->
                                    body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(strategyData);
                            case "campaigns" ->
                                    body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(campaign);
                            case "contact-lists" ->
                                    body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(contactList);
                            case "data-sources" ->
                                    body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataSource);
                        }
                        HttpRequest request = HttpRequest.newBuilder()
                                .header("Content-Type", "application/json")
                                .uri(new URI(url))
                                .POST(HttpRequest.BodyPublishers.ofString(body))
                                .build();
                        response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    } else {
                        ObjectMapper mapper = new ObjectMapper();
                        switch (pageName) {
                            case "strategies" ->
                                    body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(strategyData);
                            case "campaigns" ->
                                    body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(campaign);
                        }
                        HttpRequest request = HttpRequest.newBuilder()
                                .header("Content-Type", "application/json")
                                .uri(new URI(url))
                                .PUT(HttpRequest.BodyPublishers.ofString(body))
                                .build();
                        response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    }
                    log.info("Verify URI must start with https");
                    log.info("Headers: " + response.headers());
                    log.info("Body: " + response.body());
                    log.info("Request URL: " + response.request());
                    log.info("Status code: " + response.statusCode());
                    if (!(response.request() == null)) {
                        urlList.add(response.request().toString().split(" ")[0]);
                    }
                    statusCodeList.add(String.valueOf(response.statusCode()));
                    Assert.assertTrue("FAILURE!!!URL do not start with https", commonFunction.isHttpsUrl(urlList));
                    Assert.assertTrue("FAILURE!!!Status code do not return as 200", commonFunction.verifyStatusCode(statusCodeList));
                } catch (ConnectTimeoutException e) {
                    System.out.println("Time out " + e);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                if (pageName.equals("campaigns") && stringMap.get("Method").equals("PUT")) {
                    log.info("Verify starting campaign run through https protocol");
                    JsonObject result = RestMethodsObj.startCampaignByAPI(campaign.get("name"), "");
                    Assert.assertEquals("FAILURE!!!Cannot start campaign", "CREATED", result.get("status").getAsString());
                    Assert.assertEquals("FAILURE!!!URL does not start as https", "https", EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.split(":")[0]);
                    log.info("Deleting contact list...");
                    RestMethodsContactListObj.deleteContactList(contactList.get("name"), "");
                    log.info("Deleting campaign strategy...");
                    RestMethodsObj.deleteStrategy(strategyData.get("name"), "");
                    log.info("Deleting campaign...");
                    RestMethodsObj.deleteCampaign(campaign.get("name"), "");
                }
                if (pageName.equals("data-sources") && stringMap.get("Method").equals("POST")) {
                    log.info("Verify importing data source run through https protocol");
                    JsonObject result = RestMethodsContactListObj.runDataSourceByAPI(contactList.get("name"), dataSource.get("name"));
                    Assert.assertEquals("FAILURE!!!URL does not start as https", "https", EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.split(":")[0]);
                    Assert.assertEquals("FAILURE!!!Cannot import data source", "true", result.get("result").toString());
                    log.info("Deleting data source...");
                    RestMethodsContactListObj.deleteDataSource(dataSource.get("name"), "");
                }
            }
        }
    }

    @When("Verify cannot send API create {string} via HTTP protocol")
    public void verifyHTTPDisabledWhenCreate(String pageName) throws IOException, URISyntaxException, InterruptedException {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (pageName.toLowerCase()){
            case "campaigns":
                log.info("create campaigns - get data from test data");
                Map<String, String> campaignsData = (Map) testData.getData("campaign");
                log.info(campaignsData);
                Assert.assertTrue("HTTP should be disabled when create campaign", SecureAPICommunicationMethodsObj.verifyCannotPOSTWithHTTP(campaignsData, pageName));
                break;
            case "strategies":
                log.info("create strategy - get data from test data");
                Map<String, String> strategyData = (Map) testData.getData("strategy");
                log.info(strategyData);
                Assert.assertTrue("HTTP should be disabled when create Strategy", SecureAPICommunicationMethodsObj.verifyCannotPOSTWithHTTP(strategyData, pageName));
                break;
            case "contact-lists":
                log.info("create Contact list - get data from test data");
                Map<String, String> contactListData = (Map) testData.getData("contactList");
                log.info(contactListData);
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotPOSTWithHTTP(contactListData, pageName));
                break;
            case "data-sources":
                log.info("create Contact list - get data from test data");
                Map<String, String> dataSourceData = (Map) testData.getData("dataSource");
                log.info(dataSourceData);
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotPOSTWithHTTP(dataSourceData, pageName));
                break;
            default:
                log.info("invalid input - please using: campaign, strategy or contact-lists");
                Assert.fail();
        }
    }

    @When("Verify cannot send API to get list {string} via HTTP protocol")
    public void verifyHTTPDisabledWhenGetList(String pageName) throws IOException, URISyntaxException, InterruptedException {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (pageName.toLowerCase()){
            case "campaigns":
                log.info("GET list campaigns");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotGETTWithHTTP(pageName));
                break;
            case "strategies":
                log.info("Get list - strategies");
                Assert.assertTrue("HTTP should be disabled when create Strategy", SecureAPICommunicationMethodsObj.verifyCannotGETTWithHTTP(pageName));
                break;
            case "contact-lists":
                log.info("GET list - contact lists");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotGETTWithHTTP(pageName));
                break;
            case "data-sources":
                log.info("GET list data-source");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotGETTWithHTTP(pageName));
                break;
            case "completion-codes":
                log.info("GET list completion-codes");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotGETTWithHTTP(pageName));
                break;
            case "swagger links for campaigns":
                log.info("swagger links for campaigns");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotGETTWithHTTP(pageName));
                break;
            case "swagger links for contact-list":
                log.info("swagger links for contact-list");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotGETTWithHTTP(pageName));
                break;
            case "health for campaigns":
                log.info("swagger links for campaigns");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotGETTWithHTTP(pageName));
                break;
            case "health for contact-list":
                log.info("swagger links for contact-list");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotGETTWithHTTP(pageName));
                break;
            default:
                log.info("invalid input - please using: campaign, strategy or contact-lists");
                Assert.fail();
        }
    }

    @And("Create {string} by API via HTTPS protocol")
    public void createUsingHTTPS(String pageName) throws URISyntaxException, JsonProcessingException {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategyData;
        Map<String, String> contactListData;
        Map<String, String> dataSourceData;
        Map<String, String> campaignsData;
        switch (pageName){
            case "campaigns":
                log.info("create campaigns - get data from test data");
                strategyData = (Map) testData.getData("strategy");
                log.info(strategyData);
                if(!strategyData.containsKey("type") && !strategyData.containsKey("strategyType")){
                    strategyData.put("type","sms");
                }
                RestMethodsObj.createStrategy(strategyData);
                contactListData = (Map) testData.getData("contactList");
                log.info(contactListData);
                RestMethodsContactListObj.createContactList(contactListData);
                campaignsData = (Map) testData.getData("campaign");
                log.info(campaignsData);
                SecureAPICommunicationMethodsObj.createCampaign(campaignsData);
                break;
            case "strategies":
                log.info("create strategy - get data from test data");
                strategyData = (Map) testData.getData("strategy");
                if(!strategyData.containsKey("type") && !strategyData.containsKey("strategyType")){
                    strategyData.put("type","sms");
                }
                log.info(strategyData);
                RestMethodsObj.createStrategy(strategyData);
                strategyData.remove("type");
                break;
            case "contact-lists":
                log.info("create Contact list - get data from test data");
                contactListData = (Map) testData.getData("contactList");
                log.info(contactListData);
                RestMethodsContactListObj.createContactList(contactListData);
                break;
            case "data-sources":
                log.info("create Contact list - get data from test data");
                dataSourceData = (Map) testData.getData("dataSource");
                log.info(dataSourceData);
                RestMethodsContactListObj.createDataSource(dataSourceData);
                break;
            default:
                log.info("invalid input - please using: campaigns | strategies | contact-lists | data-sources");
                Assert.fail();
        }
    }

    @When("Verify cannot send API update {string} via HTTP protocol")
    public void verifyHTTPDisabledWhenUpdate(String pageName) throws IOException, URISyntaxException, InterruptedException {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String strategyID, campaignID, contactlistID, datasourceID;
        switch (pageName.toLowerCase()) {
            case "campaigns":
                log.info("create campaigns - get data from test data");
                Map<String, String> campaignsData = (Map) testData.getData("campaign");
                log.info(campaignsData);
                campaignID = RestMethodsObj.getCampaignID(campaignsData.get("name"));
                campaignsData.put("description",campaignsData.get("description") + "_update");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotPUTWithHTTP(campaignsData, pageName, campaignID));
                RestMethodsObj.deleteCampaign("",campaignID);
                RestMethodsObj.deleteStrategy(campaignsData.get("strategy"),"");
                RestMethodsObj.deleteContactList(campaignsData.get("contactList"),"");
                break;
            case "strategies":
                log.info("create strategy - get data from test data");
                Map<String, String> strategyData = (Map) testData.getData("strategy");
                log.info(strategyData);
                strategyID = RestMethodsObj.getStrategyID(strategyData.get("name"));
                strategyData.put("description", strategyData.get("description") + "_update");
                Assert.assertTrue("HTTP should be disabled when create Strategy", SecureAPICommunicationMethodsObj.verifyCannotPUTWithHTTP(strategyData, pageName, strategyID));
                RestMethodsObj.deleteStrategy(strategyData.get("name"),strategyID);
                break;
            case "contact-lists":
                log.info("create Contact list - get data from test data");
                Map<String, String> contactListData = (Map) testData.getData("contactList");
                log.info(contactListData);
                contactlistID = RestMethodsContactListObj.getContactListID(contactListData.get("name"));
                contactListData.put("description",contactListData.get("description") + "_update");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotPUTWithHTTP(contactListData, pageName, contactlistID));
                RestMethodsContactListObj.deleteContactList(contactListData.get("name"),contactlistID);
                break;
            case "data-sources":
                log.info("create Contact list - get data from test data");
                Map<String, String> dataSourceData = (Map) testData.getData("dataSource");
                log.info(dataSourceData);
                datasourceID = RestMethodsContactListObj.getDataSourceID(dataSourceData.get("name"));
                dataSourceData.put("description", dataSourceData.get("description") + "_update");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotPUTWithHTTP(dataSourceData, pageName, datasourceID));
                RestMethodsContactListObj.deleteDataSource(dataSourceData.get("name"),datasourceID);
                break;
            default:
                log.info("invalid input - please using: campaigns | strategies | contact-lists | data-sources");
                Assert.fail();
        }
    }

    @When("Verify cannot send API execute {string} via HTTP protocol")
    public void verifyHTTPDisabledWhenExecute(String pageName) throws IOException, URISyntaxException, InterruptedException {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String strategyID, campaignID, contactlistID, datasourceID;
        switch (pageName.toLowerCase()) {
            case "campaigns":
                log.info("create campaigns - get data from test data");
                Map<String, String> campaignsData = (Map) testData.getData("campaign");
                log.info(campaignsData);
                campaignID = RestMethodsObj.getCampaignID(campaignsData.get("name"));
                campaignsData.put("description",campaignsData.get("description") + "_update");
                Assert.assertTrue("HTTP should be disabled when create Contact List", SecureAPICommunicationMethodsObj.verifyCannotPUTWithHTTP(campaignsData, pageName, campaignID));
                RestMethodsObj.deleteCampaign("",campaignID);
                RestMethodsObj.deleteStrategy(campaignsData.get("strategy"),"");
                RestMethodsObj.deleteContactList(campaignsData.get("contactList"),"");
                break;
            default:
                log.info("invalid input - please using: campaigns | strategies | contact-lists | data-sources");
                Assert.fail();
        }
    }

    @Then("Verify the system supports {string} and cipher {string} with {string}")
    public void verifySupportCipherTLS(String tlsver, String cipher, String service) throws IOException {
        log.info("-----------------------------------------------------------------------------------------------------");
        String server = "";
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (service.equalsIgnoreCase("CampaignService")) {
            server = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.split("//")[1];
        } else if (service.equalsIgnoreCase("ContactService")) {
            server = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.split("//")[1];
        } else {
            Assert.assertTrue("!!ERR: currently only support CampaignService and ContactService!!", false);
        }
        String res = commonFunction.sendCMDToServer(tlsver, cipher, server);
        Assert.assertTrue("The TLS version: " + tlsver + " and cipher: " + cipher + " are not supported!!", commonFunction.checkSupportedSSLCipher(tlsver, cipher, res));
    }

    @Then("Verify the system does not supports {string} and cipher {string} with {string}")
    public void verifyDoNotSupportCipherTLS(String tlsver, String cipher, String service) throws IOException {
        log.info("-----------------------------------------------------------------------------------------------------");
        String server = "";
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (service.equalsIgnoreCase("CampaignService")) {
            server = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.split("//")[1];
        } else if (service.equalsIgnoreCase("ContactService")) {
            server = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.split("//")[1];
        } else {
            Assert.assertTrue("!!ERR: currently only support CampaignService and ContactService!!", false);
        }
        String res = commonFunction.sendCMDToServer(tlsver, cipher, server);
        Assert.assertFalse("The TLS version: " + tlsver + " and cipher: " + cipher + " are supported!!", commonFunction.checkSupportedSSLCipher(tlsver, cipher, res));
    }

    @Given("The user navigate to {string} health link")
    public void navigateToHealthUrl(String page) {
        log.info("Entering step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify the page " + page + " health URL");
        if (page.equalsIgnoreCase("campaign")) {
            driver.navigate().to(EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL + "/health");
        } else if (page.equalsIgnoreCase("contact")) {
            driver.navigate().to(EnvSetup.OUTBOUND_CONTACTS_API_BASEURL + "/health");
        }
        commonFunction.waitForPageLoadComplete(20, 1);
        String content = driver.findElement(By.xpath(locator.getLocator("health_Content"))).getText();
        Assert.assertTrue("!!ERR: Health link https is not working!!", content.contains("UP") && content.contains("status"));
        String url = driver.getCurrentUrl();
        Assert.assertTrue("!!ERR: the url not start from https: " + url, url.startsWith("https"));
    }

    @Given("The user navigate to {string} swagger link")
    public void navigateToSwaggerUrl(String page) {
        log.info("Entering step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify the page " + page + " swagger URL");
        if (page.equalsIgnoreCase("campaign")) {
            driver.navigate().to(EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL + "/webjars/swagger-ui/index.html");
            commonFunction.waitForPageLoadComplete(20, 1);
            String content = driver.findElement(By.xpath(locator.getLocator("swagger_title"))).getText();
            Assert.assertTrue("!!ERR: swagger link https is not working!!", content.contains("Outbound Campaign REST API"));
        } else if (page.equalsIgnoreCase("contact")) {
            driver.navigate().to(EnvSetup.OUTBOUND_CONTACTS_API_BASEURL + "/webjars/swagger-ui/index.html");
            commonFunction.waitForPageLoadComplete(20, 1);
            String content = driver.findElement(By.xpath(locator.getLocator("swagger_title"))).getText();
            Assert.assertTrue("!!ERR: swagger link https is not working!!", content.contains("Outbound Contact REST API"));
        }
        String url = driver.getCurrentUrl();
        Assert.assertTrue("!!ERR: the url not start from https: " + url, url.startsWith("https"));
    }
}
