package com.avaya.outbound.steps;

import com.avaya.outbound.frame.DataSourcePage;
import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
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
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSourceStepsDefs extends StepContext {
    public static final String REGEX_ALPHANUMERIC_UNDERSCORE_HYPHEN = "^[\\pL\\pN\\-_]+$|^$";
    public Locator locator = new Locator(DataSourcePage.class);

    public DataSourceStepsDefs(Context context) {
        context.init(this);
    }

    @Before(order = 9000, value = "@DataSource")
    public void initPageObject(Scenario scenario) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @After(order = 9000, value = "@DataSource")
    public void afterCamp(Scenario scenario) {
        log.info("Test Status: " + scenario.getStatus());
        String feature = scenario.getUri().toString();
    }

    @Given("Admin hit the Data source URL")
    public void admin_hit_the_data_source_url() {
        commonFunction.navigateToOutboundPage("datasource");
    }

    @Then("Verify Data source page is loaded")
    public void verify_data_source_page_is_loaded() {
        log.info("Verify Data source page is loaded");
        commonFunction.verifyMessageWithNoRecord("Data Source");
    }

    @Then("Verify New data source button is displayed on landing page")
    public void verify_new_data_source_button_is_displayed_on_landing_page() {
        log.info("Verify New data source button is displayed on landing page");
        commonFunction.presentElement("add", 5);
    }


    @Given("Admin user clicks {string} button")
    public void user_click_button(String buttonName) {
        log.info("User try to click on " + buttonName);
        switch (buttonName) {
            case "New Data Source": {
                Assert.assertTrue("Button is not displayed on Landing page", commonFunction.isDisplayed("add", 10));
                commonFunction.clickButton("add");
                commonFunction.waitForPageLoadComplete(5, 1);
                break;
            }
            case "Save": {
                Assert.assertTrue("Button is not displayed on Configure Detail Page", commonFunction.isDisplayed("save", 10));
                commonFunction.clickButton("save");
                commonFunction.waitForPageLoadComplete(5, 1);
                break;
            }
            case "Cancel": {
                Assert.assertTrue("ERR!!! .Button is not displayed on Configure Detail Page", commonFunction.isDisplayed("cancel", 10));
                commonFunction.clickButton("cancel");
                commonFunction.waitForPageLoadComplete(5, 1);
                break;
            }
            case "Leave this page": {
                Assert.assertTrue("ERR!!! .Button is not displayed on Configure Detail Page", commonFunction.isDisplayed("leave-page", 10));
                commonFunction.clickButton("leave-page");
                commonFunction.waitForPageLoadComplete(5, 1);
                break;
            }
        }
    }

    @Then("Verify require fields for Data Source page are displayed")
    public void verify_require_fields(List<Map<String, String>> table) {
        log.info("Verify require fields for Data Source page");
        try {
            log.info("Verify require field are empty");
            commonFunction.getAttributeField("name", "value").isEmpty();
            commonFunction.getTextField("description").isEmpty();
            commonFunction.getAttributeField("ftpIPHostName", "value").isEmpty();
            commonFunction.getAttributeField("ftpUserName", "value").isEmpty();
            commonFunction.getAttributeField("ftpPassword", "value").isEmpty();
            commonFunction.getAttributeField("ftpRemoteFilePath", "value").isEmpty();

            log.info("Verify require field description are displayed");
            for (int i = 0; i < table.size(); i++) {
                String fieldDescription = table.get(i).get("Field Description");
                log.info("Verify require field: " + fieldDescription);
                commonFunction.verifyMessageWithNoRecord(fieldDescription);
            }
        } catch (Exception e) {
            log.info("----------------------Test is Failed--------------------");
            Assert.fail("Can not found element on UI");
        }
    }

    @When("User send test data to field")
    public void user_send_characters_to_field(Map<String, String> dataTable) {
        String fieldName = dataTable.get("Field Name");
        String inputText = dataTable.get("Input Text");
        switch (fieldName) {
            case "Data Source Name": {
                commonFunction.sendKeysToTextBox("name", inputText);
                break;
            }
            case "Data Source Description": {
                commonFunction.sendKeysToTextBox("description", inputText);
                break;
            }
            case "Data Source Host": {
                commonFunction.sendKeysToTextBox("ftpIPHostName", inputText);
                break;
            }
        }
    }

    @Then("Verify data source name field does not contain special characters")
    public void verify_data_source_name_has_no_special_characters() {
        String dataSourceName = commonFunction.getAttributeField("name", "value");
        log.info("Verify Data Source Name does not have special characters: " + dataSourceName);
        Pattern p = Pattern.compile(REGEX_ALPHANUMERIC_UNDERSCORE_HYPHEN);
        Matcher m = p.matcher(dataSourceName);
        log.info("Data Source Name does not have special characters: " + m.matches());
        Assert.assertFalse(m.matches());
    }

    @Then("Verify max length of field {string} is {string}")
    public void verify_maximum_length_of_field_is(String inputField, String length) {
        log.info("Verify maximum length of field: " + inputField + " is " + length);
        switch (inputField) {
            case "Data Source Name": {
                Assert.assertEquals("ERR!!!. Datasource Name not match with expected length.", Integer.parseInt(length), commonFunction.getAttributeField("name", "value").length());
                break;
            }
            case "Data Source Description": {
                Assert.assertEquals("ERR!!!. Datasource Description not match with expected length.", Integer.parseInt(length), commonFunction.getTextField("description").length());
                break;
            }
        }
    }

    @When("Admin user add new Data Source with details configuration")
    public void user_add_new_data_source(Map<String, String> data) {
        log.info("Admin user add new Data Source with details configuration");
        testData.load(data.get("DataFile"), data.get("Data"));
        Map<String, String> sData = (Map) testData.getData("datasource");
        if (sData.get("ftpIPHostName").equalsIgnoreCase("usecommon")) {
            sData.put("ftpIPHostName", EnvSetup.SFTP_HOSTNAME);
            sData.put("ftpUserName", EnvSetup.SFTP_USERNAME);
            sData.put("ftpPassword", EnvSetup.SFTP_PASSWORD);
            sData.put("ftpRemoteFilePath", EnvSetup.SFTP_REMOTE_FILEPATH + "/" + sData.get("ftpRemoteFilePath"));
        }
        log.info(sData);
        if (sData.containsKey("Delete") && sData.get("Delete").equalsIgnoreCase("no")) {
            //pass
        } else {
            RestMethodsContactListObj.deleteDataSource(sData.get("name"), "");
        }
        DataSourcePageObj.addFTPdataSourceN(sData);
    }

    @Then("User should be redirected to Data Source landing page")
    public void user_should_be_redirected_to_DS_landing_page(Map<String, String> dataTable) {
        log.info("User should be redirected to datasource landing page");
        log.info("Verify confirm message on landing page");
        commonFunction.verifyNotification(dataTable);
        log.info("Wait until notification is removed");
        Assert.assertTrue("Page not navigate to datasource landing page", commonFunction.isDisplayed("add", 3));
    }

    @Then("Verify new Data Source information are correct on landing page")
    public void verify_datasource_information() {
        log.info("Verify new data source are correct on landing page");
        log.info("TestCase Data: " + testData);
        String datasourceName = testData.getData("datasource", "name");
        String dataSourceType = testData.getData("datasource", "dataSourceType");
        log.info("Search Data Source on UI: " + datasourceName);
        String name = commonFunction.getSingleDataFromTable(datasourceName, "Name");
        String type = commonFunction.getSingleDataFromTable(datasourceName, "Type");
        log.info("Verify data source name: " + datasourceName);
        Assert.assertEquals("ERR!!!. Data source Name is not match", datasourceName, name);
        log.info("Verify data source type: " + dataSourceType);
        Assert.assertEquals("ERR!!!. Data source type is not match", dataSourceType.toLowerCase(), type.toLowerCase());
    }

    @When("Admin user add new Data Source when missing some fields")
    public void userFillInDataSourcePage(Map<String, String> data) {
        log.info("User fill in data source page");
        testData.load(data.get("DataFile"), data.get("Data"));
        Map<String, String> sData = (Map) testData.getData("datasource");
        DataSourcePageObj.addFTPdataSourceN(sData);
    }

    @And("Verify the datasource can not save and the error message is displayed")
    public void verifyErrorMessageDisplay() {
        log.info("Check error message and user can not save the datasource");
        commonFunction.verifyFieldError(testData.getData("datasource", "VerifyField"), testData.getData("datasource", "ExpErrorMessage"));
    }

    @Then("Verify password field must not be visible in the UI")
    public void verify_password_field_is_not_visible() {
        log.info("Verify password field must not be visible in the UI");
        Assert.assertEquals("ERR!!!. Attribute Type is mot correct", "password", commonFunction.getAttributeField("ftpPassword", "type"));
    }

    @And("Verify two new data source are displayed on list page")
    public void verify_two_new_data_source_are_displayed_on_list_page(Map<String, String> data) {
        log.info("Verify two new data source are displayed on list page");
        log.info("Verify data source 2 is the first on list page: " + data.get("Data_2"));
        Assert.assertEquals("ERR!!!. The datasource name 2 order is not match", data.get("Data_2"), commonFunction.getSingleDataFromTable(data.get("Data_2"), "Name"));
        log.info("Verify data source 2 is the second on list page: " + data.get("Data_1"));
        Assert.assertEquals("ERR!!!. The datasource name 1 order is not match", data.get("Data_1"), commonFunction.getSingleDataFromTable(data.get("Data_1"), "Name"));
    }

    @Then("Verify the datasource can not save")
    public void messageDisplay() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Verify the datasource can not save and user should stay on datasource page");
        commonFunction.verifyFieldError(testData.getData("datasource", "VerifyField"), testData.getData("datasource", "ExpErrorMessage"));
        Assert.assertNotNull("Error!! User should stay on datasource configuration page", commonFunction.presentElement("cancel", 5));
    }

    @Then("User update valid information")
    public void UpdateDataSourceSFTPValidInformation() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("User update valid data");
        commonFunction.sendKeysToTextBox("ftpIPHostName", testData.getData("datasource", "ftpIPHostName_Correct"));
    }


    @When("Admin user add new Data Source with details configuration and click on test connection")
    public void admin_user_add_new_data_source_with_details_configuration_and_click_on_test_connection(Map<String, String> data) {
        log.info("Admin user add new Data Source with details configuration and click on test connection");
        testData.load(data.get("DataFile"), data.get("Data"));
        if (testData.get("ftpIPHostName").equalsIgnoreCase("usecommon")) {
            testData.put("ftpIPHostName", EnvSetup.SFTP_HOSTNAME);
            testData.put("ftpUserName", EnvSetup.SFTP_USERNAME);
            testData.put("ftpPassword", EnvSetup.SFTP_PASSWORD);
            testData.put("ftpRemoteFilePath", EnvSetup.SFTP_REMOTE_FILEPATH + "/" + testData.get("ftpRemoteFilePath"));
        }
        log.info(testData);
        DataSourcePageObj.addFTPdataSourceN(testData);
    }

    @Then("Hint message display {string} is displayed for {string}")
    public void hint_message_display_is_displayed_for(String notifyMessageExpected, String fieldname) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify notify message display " + notifyMessageExpected);
        commonFunction.verifyFieldError(fieldname, notifyMessageExpected);

    }

    @Then("User test connection successful message should be displayed")
    public void user_test_connection_successful_message_should_be_displayed(Map<String, String> data) {
        testData.load(data.get("DataFile"), data.get("Data"));
        log.info(testData);
        commonFunction.verifyNotification(testData);

    }

    @Then("User test connection failed message should be displayed")
    public void user_test_connection_failed_message_should_be_displayed(Map<String, String> data) {
        testData.load(data.get("DataFile"), data.get("Data"));
        log.info(testData);
        commonFunction.verifyNotification(testData);
    }

    @When("Admin user add new Data Source with empty data in mandatory fields")
    public void admin_user_add_new_data_source_with_empty_data_in_mandatory_fields(Map<String, String> data) {
        testData.load(data.get("DataFile"), data.get("Data"));
        log.info(testData);
        commonFunction.clickButton("add");
        commonFunction.clickButton("save");
    }

    @Then("verify error message displayed for mandatory fields")
    public void verify_error_message_displayed_for_mandatory_fields(Map<String, String> data) {
        testData.load(data.get("DataFile"), data.get("Data"));
        log.info(testData);
        DataSourcePageObj.VerifyMandatoryFields(testData);

    }

    @And("User input name with {string} include special character")
    public void addNameWithSpecialCharacter(String data) {
        log.info("User input name with name include special character");
        commonFunction.sendKeysToTextBox("name", data);
    }

    @Then("Verify new Data Source is created on landing page")
    public void verifyDataSourceCreation() {
        log.info("Verify new data source are correct on landing page");
        log.info("TestCase Data: " + testData);
        String datasourceName = testData.getData("datasource", "name");
        List<String> nameList = commonFunction.getListColumnDataOnCurPage("Name");
        Assert.assertTrue(nameList.contains(datasourceName));
    }

    @And("Verify the test connection button is disable")
    public void verifyTheTestConnectionButtonIsDisable() {
        WebElement testConnection = commonFunction.findElementByDataID("test-connection");
        boolean boolConnection = commonFunction.verifyAttributeAvailable(testConnection, "disabled");
        Assert.assertTrue("Button still visible", boolConnection);
    }

    @Then("User clean datasource after completing TC")
    public void deleteDataSourceData() {
        log.info("User clean datasource after completing TC");
        RestMethodsContactListObj.deleteDataSource(testData.getData("datasource", "name"), "");
    }

    @Then("User create new Data Source with the same name")
    public void addDatasourceWithSameName(Map<String, String> data) {
        log.info("User create new Data Source with the same name");
        testData.load(data.get("DataFile"), data.get("Data"));
        Map<String, String> sData = (Map) testData.getData("datasource");
        RestMethodsContactListObj.deleteDataSource(sData.get("name"), "");
        DataSourcePageObj.addFTPdataSourceN(sData);
        commonFunction.waitForPageLoadComplete(5, 1);
        Assert.assertTrue("Page not navigate to datasource landing page", commonFunction.isDisplayed("add", 3));
        DataSourcePageObj.addFTPdataSourceN(sData);
    }

    @And("Delete data source")
    public void deleteDataSource(Map<String, String> data) {
        log.info("Loading test data...");
        testData.load(data.get("DataFile"), data.get("Data"));
        Map<String, String> sData = (Map) testData.getData("datasource");
        if (sData.get("ftpIPHostName").equalsIgnoreCase("usecommon")) {
            sData.put("ftpIPHostName", EnvSetup.SFTP_HOSTNAME);
            sData.put("ftpUserName", EnvSetup.SFTP_USERNAME);
            sData.put("ftpPassword", EnvSetup.SFTP_PASSWORD);
            sData.put("ftpRemoteFilePath", EnvSetup.SFTP_REMOTE_FILEPATH + "/" + sData.get("ftpRemoteFilePath"));
        }
        log.info(sData);
        RestMethodsContactListObj.deleteDataSource(sData.get("name"), "");
    }
}
