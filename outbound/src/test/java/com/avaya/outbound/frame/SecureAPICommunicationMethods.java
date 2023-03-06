package com.avaya.outbound.frame;

import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Properties;

public class SecureAPICommunicationMethods extends CommonFunction {
    private final Logger log = LogManager.getLogger(SecureAPICommunicationMethods.class);
    public Locator locator = new Locator(this);

    public SecureAPICommunicationMethods(WebDriver driver) {
        super(driver);
    }


    public boolean verifyCannotPOSTWithHTTP(Map<String,String> data, String pageName) throws JsonProcessingException, URISyntaxException {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        log.info(body);
        Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
        String url;
        if(pageName.equals("contact-lists") || pageName.equals("data-sources")){
            url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.replaceAll("https","http") + "/" + pageName;
        }else {
            url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.replaceAll("https","http") + "/" + pageName;
        }
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("reponse status code:" + response.statusCode());
            if(response!=null){
                log.info("HTTP should be disabled - failed");
                return false;
            }
        }catch (Exception e){
            log.info(e);
            log.info("HTTP was disabled - passed");
            return true;
        }
        return false;
    }

    public boolean verifyCannotGETTWithHTTP(String pageName) throws JsonProcessingException, URISyntaxException {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ObjectMapper mapper = new ObjectMapper();
        Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
        String url;
        if(pageName.equals("contact-lists") || pageName.equals("data-sources") || pageName.contains("contact-list")){
            if(pageName.contains("swagger")){
                url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.replaceAll("https","http") + "/" + "/v3/api-docs";
            }else if(pageName.contains("health")){
                url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.replaceAll("https","http") + "/health";
            }else {
                url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.replaceAll("https","http") + "/" + pageName;
            }
        }else {
            if(pageName.contains("swagger")){
                url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.replaceAll("https","http") + "/" + "/v3/api-docs";
            }else if(pageName.contains("health")) {
                url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.replaceAll("https","http") + "/health";
            }else {
                url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.replaceAll("https","http") + "/" + pageName;
            }
        }
        log.info("url: " + url );
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("reponse status code:" + response.statusCode());
            if(response!=null){
                log.info("HTTP should be disabled - failed");
                return false;
            }
        }catch (Exception e){
            log.info(e);
            log.info("HTTP was disabled - passed");
            return true;
        }
        return false;
    }

    public boolean verifyCannotPUTWithHTTP(Map<String,String> data, String pageName, String ID) throws JsonProcessingException, URISyntaxException {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        log.info(body);
        Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
        String url;
        if(pageName.equals("contact-lists") || pageName.equals("data-sources")){
            url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.replaceAll("https","http") + "/" + pageName + "/" + ID;
        }else {
            url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.replaceAll("https","http") + "/" + pageName + "/" + ID;
        }
        log.info("URL: " + url);
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(new URI(url))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("reponse status code:" + response.statusCode());
            if(response!=null){
                log.info("HTTP should be disabled - failed");
                return false;
            }
        }catch (Exception e){
            log.info(e);
            log.info("HTTP was disabled - passed");
            return true;
        }
        return false;
    }

    public JsonObject createCampaign(Map<String, String> testData) throws JsonProcessingException {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL + "/campaigns";
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(testData);
        log.info(url);
        log.info(body);
        JsonObject result = RestMethodsObj.rest(RestMethods.Method.POST, url, body);
        log.info(result);
        return result;
    }

    public boolean verifyCannotStartWithHTTP(Map<String,String> data, String pageName, String ID) throws JsonProcessingException, URISyntaxException {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ObjectMapper mapper = new ObjectMapper();
        String body = "";
        log.info(body);
        Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
        String url;
        if(pageName.equals("contact-lists") || pageName.equals("data-sources")){
            url = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.replaceAll("https","http") + "/" + pageName + "/" + ID;
        }else {
            url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.replaceAll("https","http") + "/" + pageName + "/" + ID + "/execute";
        }
        log.info("URL: " + url);
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("reponse status code:" + response.statusCode());
            if(response!=null){
                log.info("HTTP should be disabled - failed");
                return false;
            }
        }catch (Exception e){
            log.info(e);
            log.info("HTTP was disabled - passed");
            return true;
        }
        return false;
    }
}
