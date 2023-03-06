package com.avaya.outbound.frame;

import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Map;


public class DataSourcePage extends CommonFunction {
    private final Logger log = LogManager.getLogger(DataSourcePage.class);
    public Locator locator = new Locator(this);

    public DataSourcePage(WebDriver driver) {
        super(driver);
    }

    public void addFTPdataSourceN(Map<String, String> testData) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("TestCase Data: " + testData);
        clickButton("add");
        sendKeysToTextBox("name", testData.get("name"));
        sendKeysToTextBox("description", testData.get("description"));
        clickRadioButton(testData.get("dataSourceType"));
        sendKeysToTextBox("ftpIPHostName", testData.get("ftpIPHostName"));
        sendKeysToTextBox("ftpUserName", testData.get("ftpUserName"));
        sendKeysToTextBox("ftpPassword", testData.get("ftpPassword"));
        sendKeysToTextBox("ftpRemoteFilePath", testData.get("ftpRemoteFilePath"));
        clickRadioButton(testData.get("fieldDelimiter"));
        if (testData.get("TestConnection").equalsIgnoreCase("yes")) {
            clickButton("test-connection");
        } else if (testData.get("Verify").equalsIgnoreCase("sameform")) {
            log.info("Do not Save New datasource");
        } else {
            log.info("Save New datasource");
            clickButton("save");
        }
    }

    public void VerifyMandatoryFields(Map<String, String> testData) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        testData.keySet().forEach((key) -> {
            verifyFieldError(key, testData.get(key));
        });
    }
}
