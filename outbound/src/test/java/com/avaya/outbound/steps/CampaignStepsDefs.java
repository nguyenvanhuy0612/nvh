package com.avaya.outbound.steps;

import com.avaya.outbound.frame.CampaignManagerPage;
import com.avaya.outbound.frame.RestMethods;
import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
import com.google.common.collect.Comparators;
import com.google.gson.JsonObject;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class CampaignStepsDefs extends StepContext {
    public Locator locator = new Locator(CampaignManagerPage.class);

    public CampaignStepsDefs(Context context) {
        context.init(this);
    }

    @Before(order = 9000, value = "@ListCampaign or @Campaign or @CampaignSummaryView or @UpdateCampaign or @CampaignCompletionProcessing")
    public void initPageObject() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @After(order = 9000, value = "@ListCampaign or @Campaign or @CampaignSummaryView or @UpdateCampaign or @CampaignCompletionProcessing")
    public void afterCamp(Scenario scenario) {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
    }

    @After(order = 9001, value = "@IXOUTREACH-4805 or @ANIinformation")
    public void afterCmpPage(Scenario scenario) {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
        this.cleanUpDataCreateCampaign();
    }

    /**
     * method using verify no record display on campaign landing page
     */
    @Given("There are no campaigns display on campaign landing page")
    public void thereAreNoCampaignsDisplayOnCampaignLandingPage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.search("Invalid Name Campaign");
        Assert.assertEquals("More than 0 campagin display on campaign landing page", 0, commonFunction.numberRecordOnCurrentPage());
    }

    @When("The Campaign management URL is hit to view the existing campaigns")
    public void openURL() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("The Campaign management URL is hit to view the existing campaigns " + EnvSetup.OUTBOUND_CAMPAIGNS_UI_BASEURL);
        commonFunction.navigateToOutboundPage("campaigns-manager");
    }

    @Then("Message \"Record list is empty\" is displayed in place of row on campaign manager landing page")
    public void verifyMessageDisplayCorrectlyWithNoRecord() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String expected_message = "Record list is empty";
        String actualMessage = CampaignManagerPageObj.getMessageNoRecord();
        Assert.assertTrue("The message is not display correctly on campaign landing page", expected_message.equalsIgnoreCase(actualMessage));
        log.info("Message display correctly with no campaigns configured");
    }

    @And("The checkbox select all campaign should display")
    public void verifyTheCheckboxSelectAllCampaignShouldDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean isDisplayed = CampaignManagerPageObj.btnCheckAll.isDisplayed();
        Assert.assertTrue("Checkbox select all campagin is not display on campaign landing page", isDisplayed);
    }

    @Then("The search box should display")
    public void verifyTheSearchBoxShouldDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Search box is not display on campaign landing page", commonFunction.isDisplayed("searchTable", 10));
    }

    @And("The filter campaign button should display")
    public void verifyTheFilterButtonShouldDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("filter campagin button is not display on campaign landing page", commonFunction.isDisplayed("columnFilters", 10));
    }

    @And("The refresh button should display")
    public void verifyTheRefreshButtonShouldDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Refresh button is not display on campaign landing page", commonFunction.isDisplayed("refreshTable", 5));
    }

    @And("Option to select columns should display")
    public void verifyOptionToSelectColumnsShouldDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean isDisplayed = CampaignManagerPageObj.btnSelectColumn.isDisplayed();
        Assert.assertTrue("Option to select columns is not display on campaign landing page", isDisplayed);
    }

    @Given("Fetch lab information and test data")
    public void fetchLabInformationAndTestData(Map<String, String> info) {
        testData.load(info.get("DataFile"), info.get("Data"));
    }

    @And("(?i)^Load test data:\\s*DataFile\\s*=\\s*([\\w\\.\\-\\+]+)\\s*,\\s*Data\\s*=\\s*([\\w\\.\\-\\+]+)\\s*$")
    public void loadDataForTest(String dataFile, String data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        testData.load(dataFile, data);
        log.info("testData: " + testData);
    }

    @When("Clicks the three dots beside campaign name")
    public void clicksTheThreeDotsBesideCampaignName() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            commonFunction.sendKeysToTextBox("searchTable", testData.getStrict("campaign", "name"));
            commonFunction.waitForPageLoadComplete(30, 2);
            commonFunction.clickTakeMoreActionElipse(testData.getStrict("campaign", "name"));
        } catch (Exception e) {
            log.info("------------------------------Test is Failed--------------------");
            Assert.fail("Can not click three dots beside campaign");
        }
    }

    @And("Click campaign summary view on dropdown")
    public void clickCampaignSummaryViewOnDropdown() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify Campaign Summary menu display on three dots dropdown");
        Assert.assertTrue("Campaign Summary menu is not display on three dots dropdown",
                commonFunction.isDisplayed("action_campaignSummary", 10));
        log.info("Click Campaign Summary on DropDown");
        commonFunction.tryClick("action_campaignSummary", 10);
        commonFunction.waitForPageLoadComplete(10, 1);
    }

    @Then("{string} page display after click menu on dropdown")
    public void verifyPageDisplay(String namePage) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String headerPageDisplay = CampaignManagerPageObj.getHeaderPage();
        Assert.assertTrue(namePage + " page is not display after click menu on dropdown", headerPageDisplay.equalsIgnoreCase(namePage));
        log.info(namePage + " page load Successful");
    }

    @Then("Value {string} field should display correctly in campaign summary page")
    public void informationDisplayCorrectlyInCampaignSummaryPage(String field) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String valueActual = CampaignManagerPageObj.getValueOfFieldOnCampaignSummary(field);
        valueActual = valueActual.equalsIgnoreCase("-") ? "" : valueActual;
        valueActual = field.equalsIgnoreCase("Dialing Order") ? valueActual.replaceAll(" Records", "") : valueActual;
        switch (field) {
            case "Name" -> field = "name";
            case "Campaign Type" -> field = "type";
            case "Description" -> field = "description";
            case "Campaign Strategy" -> field = "strategy";
            case "Dialing Order" -> field = "dialingOrder";
            case "Contact List" -> field = "contactList";
            default -> Assert.fail("Input parameter invalid - Using field name show on UI");
        }
        String valueExpected = testData.getStrict("campaign", field);
        log.info("Value of " + field + " is " + valueActual);
        Assert.assertTrue("Value of " + field + " no same  with configure when create Campaign with configure when " +
                "create Campaign ", valueActual.equalsIgnoreCase(valueExpected));
    }

    @Then("Campaign summary page should close when click close button")
    public void campaignSummaryPageShouldClose() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify close button enabled on campaign summary view");
        Assert.assertTrue("Close button is not enable on campaign summary page", commonFunction.isDisplayed("close", 5));
        log.info("Click close button on campaign summary page");
        commonFunction.tryClick("close", 10);
        commonFunction.waitForPageLoadComplete(30, 2);
        Assert.assertTrue("Campaign Summary still display on campaign landing page", CampaignManagerPageObj.getHeaderPage().isEmpty());
    }

    @And("Campaign Manager landing page displayed")
    public void campaignManagerLandingPageShouldBeDisplayedBack() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Campaign Manager landing page is not display ", CampaignManagerPageObj.verifyCampaignPage());
        log.info("Campaign Manager landing page displayed");
        log.info("------------------------Clean Data added---------------------------------------");
    }

    @Then("All value of field on the campaign summary sheet should be non editable")
    public void verifyAllTheFieldSValueOnTheCampaignSummarySheetShouldBeNonEditable() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify All the field's value on the campaign summary sheet should be non editable");
        int numberButtonEdit = CampaignManagerPageObj.getNumberIconEditOnCampaignSummaryPage();
        Assert.assertEquals("Edit button display on campaign summary page", 0, numberButtonEdit);
        log.info("All the field's value on the campaign summary sheet should be non editable");
    }

    @Given("The Campaign management URL is hit")
    public void the_campaign_management_url_is_hit() {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify the Campaign management URL is hit");
        commonFunction.navigateToOutboundPage("campaigns-manager");
    }

    @Then("Verify Add Campaign button is displayed on landing page")
    public void verify_add_campaign_button_is_displayed_on_landing_page() {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify element is exist on Landing page");
        Assert.assertTrue("Button is not displayed on Landing page", commonFunction.isDisplayed("addNewCampaign", 2));
    }

    @Then("No record is displayed on landing page")
    public void no_record_is_displayed_on_landing_page() {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.search("this is invalid campaign");
        int numberOfRecords = CampaignManagerPageObj.numberRecordOnCurrentPage();
        log.info("Total Record: " + numberOfRecords);
        Assert.assertEquals("Total record on page is not equal: 0", 0, numberOfRecords);
    }

    @Given("User click {string} button")
    public void user_click_button(String buttonName) {
        log.info("User try to click on " + buttonName);
        switch (buttonName) {
            case "New Campaign": {
                Assert.assertTrue("Button is not displayed on Landing page", commonFunction.isDisplayed("addNewCampaign", 2));
                commonFunction.clickButton("addNewCampaign");
                break;
            }
            case "Save": {
                Assert.assertTrue("Button is not displayed on Configure Detail Page", commonFunction.isDisplayed("save", 2));
                commonFunction.tryClick("save", 10);
                commonFunction.waitForPageLoadComplete(30, 0.5);
                break;
            }
        }
    }

    @Then("Page navigate to Campaign detail configuration")
    public void page_navigate_to_campaign_detail_configuration() {
        log.info("Verify Detail configuration page include tab Detail, Campaign, Contact and CompletionProcessing");
        Assert.assertTrue("Tab is not displayed on Campaign Detail page", commonFunction.isDisplayed("tab-details"));
        Assert.assertTrue("Tab is not displayed on Campaign Detail page", commonFunction.isDisplayed("tab-campaign"));
        Assert.assertTrue("Tab is not displayed on Campaign Detail page", commonFunction.isDisplayed("tab-completionProcessing"));
    }

    @When("^All the valid and mandatory values are provided$" + "|^User send invalid data to field$")
    public void all_the_valid_and_mandatory_values_are_provided() {
        log.info("All the valid and mandatory values are provided");
        CampaignManagerPageObj.addCampaignDetail(testData);
    }

    @And("User should be redirected to campaign landing page")
    public void user_should_be_redirected_to_campaign_landing_page() {
        log.info("User should be redirected to campaign landing page");
        commonFunction.waitForPageLoadComplete(10, 0.5);
        Assert.assertTrue("Page not navigate to campaign landing page", commonFunction.isDisplayed("addNewCampaign", 3));
        Assert.assertTrue("Page not navigate to campaign landing page", commonFunction.isDisplayed("refreshTable", 3));
    }

    @Then("Verify {string} campaign is added to landing page")
    public void verify_new_campaign_is_added_to_landing_page(String searchName) {
        log.info("Verify notification is displayed when new campaign is added successfully");
        String confirmMSg = "Campaign added successfully.";
        String pageToastMessage = CampaignManagerPageObj.getToastMessage();
        log.info("ToastMessage: " + pageToastMessage);
        Assert.assertEquals(confirmMSg, pageToastMessage);
        log.info("Verify new campaign is added to landing page");
        Assert.assertTrue("campaign not display", CampaignManagerPageObj.isCampaignExist(searchName));
    }

    /**
     * Method using add campaign on UI and verify display correctly on campaign landing page
     *
     * @param numberRecordExpected: number campaign that user want to add with input number from step
     */
    @Given("There are at least {int} campaign is created")
    public void thereIsAtLeastCampaignIsCreated(int numberRecordExpected) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("numberRecordExpected: " + numberRecordExpected);
        int numberRecord = CampaignManagerPageObj.countAllRecord();
        String nameCampaignDefault = testData.get("Campaign Name");
        int campIndex = 0;
        long start = new Date().getTime();
        while (numberRecord < numberRecordExpected && (new Date().getTime() - start) / 1000 < 600) {
            log.info("Add campaign to campaign landing page");
            int count = numberRecordExpected - numberRecord;
            for (int i = 0; i < count; i++) {
                testData.put("Campaign Name", nameCampaignDefault + "_" + ++campIndex);
                RestMethodsObj.createCampaign(testData);
                commonFunction.sleepInSec(0.5);
            }
            numberRecord = CampaignManagerPageObj.countAllRecord();
        }
        log.info("numberRecord: " + numberRecord);
        if (numberRecord > 0) {
            log.info("Set campaign name with 1st campaign in list");
            testData.put("Campaign Name", RestMethodsObj.getAll(RestMethods.Page.campaign, "name").get(0));
        }
    }

    /**
     * method using to verify information campaign display correctly with name column field on campaign landing page
     *
     * @param columnName is name column that user want verify display and input by user from step
     */
    @Then("{string} column displays correctly with individual campaigns")
    public void columnDisplaysCorrectlyWithIndividualCampaigns(String columnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sendKeysToTextBox("searchTable", testData.getData("campaign", "name"));
        utilityFun.wait(2);
        log.info("Get information column of record");
        String expectedValue = testData.getData("campaign", columnName.toLowerCase());
        expectedValue = columnName.equalsIgnoreCase("Type") ? expectedValue.toUpperCase() : expectedValue;
        Assert.assertTrue("Information campaign not match to name column",
                commonFunction.getListColumnDataOnCurPage(columnName).contains(expectedValue));
    }

    /**
     * method using verify number record display correctly size of record
     */
    @Then("Number record display correctly at 1 - {int} in the left bottom page")
    public void numberRecordDisplayCorrectlyAtInTheLeftBottomPage(int numberRecordExpected) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify there are " + numberRecordExpected + " displayed on campaign landing page");
        thereIsAtLeastCampaignIsCreated(numberRecordExpected);
        log.info("Verify Number record display correctly at 1 - " + numberRecordExpected + " in the left bottom page");
        int valueRecord = CampaignManagerPageObj.getValueOfPageDetails();
        Assert.assertEquals(valueRecord, numberRecordExpected);
        log.info("Number record display correctly at 1 - " + numberRecordExpected + " in the left bottom page");
    }

    /**
     * method using verify number record display correctly size of record
     */
    @Then("Number record display correctly in the left bottom page")
    public void numberRecordDisplayCorrectlyAtInTheLeftBottomPage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int numberRecordExpected = CampaignManagerPageObj.numberRecordOnCurrentPage();
        log.info("Number record display correctly in the left bottom page");
        int numberRecordActual = CampaignManagerPageObj.getValueOfPageDetails();
        Assert.assertEquals("Number Record display no correctly in the left bottom page", numberRecordExpected, numberRecordActual);
        log.info("Number record display correctly at 1 - " + numberRecordExpected + " in the left bottom page");
    }

    @And("Previous and next button page having provision and disable to click in the page")
    public void verifyPreviousAndNextButton() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify previous and next button disabled on campaign landing page when the number of page displays is 1");
        commonFunction.selectDropDownOptionEqual("pageSizeDropdown", "100");
        Assert.assertFalse("Previous button enable on campaign landing page", commonFunction.isEnable("previousPage"));
        Assert.assertFalse("Next button enable on campaign landing page", commonFunction.isEnable("nextPage"));
        log.info("previous and next button page having provision and disable to click in the page");
    }

    /**
     * method using get number page displayed campaign landing page
     *
     * @param numberPageExpected: number page expected displayed on campaign landing page
     */
    @And("The number page display is {int} page")
    public void verifyTheNumberPageDisplay(int numberPageExpected) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify number page display is " + numberPageExpected);
        int numberPageActual = CampaignManagerPageObj.checkPageDisplay();
        Assert.assertEquals("Page display more than 1 when have 1 campaign configured", numberPageExpected, numberPageActual);
        log.info("The number page display is " + numberPageExpected);
    }

    @And("Change number page record option is display")
    public void verifyChangeANumberRecordOptionIsDisplay() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify change number page record option is display");
        Assert.assertTrue("change number page display on campaign landing page", commonFunction.isDisplayed("pageSizeDropdown", 10));
    }

    @Then("Each row should have a check box to select the individual campaign")
    public void eachRowShouldHaveACheckBoxToSelectTheIndividualCampaign() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean isDisplayed = CampaignManagerPageObj.verifyCheckBoxSelectEachCamp(testData.get("Campaign Name"));
        Assert.assertTrue("Check box to select the individual campaign is not display", isDisplayed);
        log.info("Check box to select the individual campaign displayed on campaign landing");
    }

    @Given("Several records are displayed on landing page")
    public void several_records_are_displayed_on_landing_page() {
        log.info("Several records are displayed on landing page");
        int totalRecords = CampaignManagerPageObj.countAllRecord();
        log.info("Total Record: " + totalRecords);
        if (totalRecords == 0) {
            log.info("Create a new campaign on UI");
            log.info("Click Add New campaign: ");
            commonFunction.clickButton("addNewCampaign");
            CampaignManagerPageObj.addCampaignDetail(testData);
            log.info("Click save the campaign: ");
            commonFunction.clickButton("save");
            commonFunction.waitForPageLoadComplete(10, 1);
            totalRecords = CampaignManagerPageObj.countAllRecord();
        }
        commonFunction.waitForPageLoadComplete(10, 1);
        Assert.assertTrue("Total record on page is equal: 0", totalRecords != 0);
    }

    @Given("A {string} campaign is available on campaign landing page")
    public void a_campaign_is_available_on_campaign_landing_page(String searchName, Map<String, String> data) {
        // Write code here that turns the phrase above into concrete actions
        log.info("Verify campaign is available on campaign landing page");
        boolean exist = CampaignManagerPageObj.isCampaignExist(searchName);
        if (exist) {
            log.info("Campaign Name is exist on campaign landing page: " + searchName);
        } else {
            log.info("Create New Campaign Name with name: " + searchName);
            log.info("Click Add New campaign: " + searchName);
            CampaignManagerPageObj.btnAddNewCampaign.click();
            utilityFun.wait(3);
            testData.load(data.get("DataFile"), data.get("Data"));
            CampaignManagerPageObj.addCampaignDetail(testData);
            log.info("Click save the campaign: " + searchName);
            CampaignManagerPageObj.btnSaveCampaign.click();
            utilityFun.wait(3);
        }
    }

    @When("User input same name on Campaign Name field")
    public void user_input_same_name_on_campaign_name_field() {
        log.info("Input the same name on on Campaign Name field");
        // This step will be covered on next step on feature file
    }

    @Then("Verify notification/failure {string} is displayed")
    public void verify_notification_message_is_displayed(String expectedMsg) {
        log.info("Confirm Error/Notification message is displayed on UI: " + expectedMsg);
        CampaignManagerPageObj.isTextDisplayed(expectedMsg);
    }

    @Then("Verify campaign name showing hint error message {string}")
    public void verifyHintError(String expMgs) {
        log.info("Expected message: " + expMgs);
        commonFunction.clickButton("details");
        String xpath = String.format(locator.getLocator("message"), expMgs);
        commonFunction.presentElement(By.xpath(xpath), 5);
        commonFunction.verifyFieldError("name", expMgs);
    }

    @Then("Verify user should remain on campaign creation page")
    public void verify_user_should_remain_on_campaign_creation_page() {
        log.info("Verify user should remain on campaign creation page");
        Assert.assertTrue("Tab is not displayed on Campaign Detail page", commonFunction.isDisplayed("tab-details"));
    }

    @When("Admin user click on tab {string}")
    public void admin_user_click_on_tab(String tab) {
        log.info("Admin user click on tab: " + tab);
        String firstLetter = tab.substring(0, 1);
        String remainLetter = tab.substring(1, tab.length());
        firstLetter = firstLetter.toLowerCase();
        tab = firstLetter + remainLetter;
        commonFunction.clickButton("tab-" + tab.replaceAll(" ", ""));
    }

    @Then("Verify active {string} tab include field/description {string}")
    public void verify_active_tab_include_field(String tabName, String fieldName) {
        log.info("Verify active tab include field: " + fieldName);
        CampaignManagerPageObj.verifyActiveTab(tabName, fieldName);
    }

    @Then("Verify field {string} is {string}")
    public void verify_field_is_empty(String inputField, String value) {
        log.info("Verify field " + inputField + " is: " + value);
        if (value.equals("empty")) {
            if (inputField.equals("Name")) {
                String text = commonFunction.getTextField("name");
                Assert.assertTrue("!!!ERR. Campaign Name should be empty as default value", text.isEmpty());
            } else if (inputField.equals("Description")) {
                String text = commonFunction.getTextField("description");
                Assert.assertTrue("!!!ERR. Description should be empty as default value", text.isEmpty());
            } else if (inputField.equals("Sender's Display Name")) {
                String text = commonFunction.getTextField("senderDisplayName");
                Assert.assertTrue("!!!ERR. Sender's Display Name should be empty as default value", text.isEmpty());
            } else if (inputField.equals("Sender's Address")) {
                String text = commonFunction.getTextField("senderAddress");
                Assert.assertTrue("!!!ERR. Sender's Address should be empty as default value", text.isEmpty());
            } else if (inputField.equals("Finish At (Date)")) {
                System.out.println(commonFunction.getAttributeField("finishAtDate", "value"));
                String text = commonFunction.getAttributeField("finishAtDate", "value");
                Assert.assertTrue("!!!ERR. Sender's Address should be empty as default value", text.isEmpty());
            } else if (inputField.equals("Finish At (Time)")) {
                String text = commonFunction.getAttributeField("finishAtTime", "value");
                Assert.assertTrue("!!!ERR. Sender's Address should be empty as default value", text.isEmpty());
            } else {
                log.info("Something wrong with " + inputField + " and " + value);
                Assert.fail("Something wrong with " + inputField + " and " + value);
            }
        } else {
            if (inputField.equals("Finish After (Hours)")) {
                String text = commonFunction.getAttributeField("finishAfterHrs", "value");
                Assert.assertTrue("!!!ERR. Finish After (Hours) should be equal " + value + " as default", text.equalsIgnoreCase(value));
            } else if (inputField.equals("Finish After (Minutes)")) {
                String text = commonFunction.getAttributeField("finishAfterMins", "value");
                Assert.assertTrue("!!!ERR. Finish After (Minutes) should be equal " + value + " as default", text.equalsIgnoreCase(value));
            } else {
                Assert.fail("Something wrong with " + inputField + " and " + value);
            }
        }
    }

    @When("User send {string} to field {string}")
    public void user_send_characters_to_field(String character, String field, Map<String, String> dataTable) {
        String fieldName = dataTable.get("Field Name");
        String inputText = dataTable.get("Name");
        log.info("Test input data: " + inputText);
        log.info("Data length: " + inputText.length());
        if (field.equals("Name")) {
            commonFunction.sendKeysToTextBox("name", inputText);
        } else if (field.equals("Description")) {
            commonFunction.sendKeysToTextBox("description", inputText);
        } else if (field.equals("Sender's Display Name")) {
            commonFunction.sendKeysToTextBox("senderDisplayName", inputText);
        } else if (field.equals("Sender's Address")) {
            commonFunction.sendKeysToTextBox("senderAddress", inputText);
        } else if (field.equals("Finish After (Hours)")) {
            commonFunction.findElementByDataID("finishAfterHrs").clear();
            commonFunction.waitForPageLoadComplete(10, 1);
            commonFunction.sendKeysToTextBox("finishAfterHrs", inputText);
        } else if (field.equals("Finish After (Minutes)")) {
            commonFunction.findElementByDataID("finishAfterMins").clear();
            commonFunction.waitForPageLoadComplete(10, 1);
            commonFunction.sendKeysToTextBox("finishAfterMins", inputText);
        } else if (field.equals("Finish At (Time)")) {
            commonFunction.sendKeysToTextBox("finishAtTime", character);
        } else {
            Assert.fail("Something wrong with " + character + " and " + field);
        }
    }

    @Then("Verify maximum length of field {string} is {string}")
    public void verify_maximum_length_of_field_is(String inputField, String length) {
        log.info("Verify maximum length of field: " + inputField + " is " + length);
        if (inputField.equals("Name")) {
            String inputText = commonFunction.getAttributeField("name", "value");
            log.info("Verify campaign name length:" + inputText);
            Assert.assertEquals("!!!ERR. String length is not match", Integer.parseInt(length), inputText.length());
        } else if (inputField.equals("Description")) {
            String inputText = commonFunction.getTextField("description");
            log.info("Verify campaign Description length" + inputText);
            Assert.assertEquals("!!!ERR. String length is not match", Integer.parseInt(length), inputText.length());
        } else if (inputField.equals("Sender's Display Name")) {
            String inputText = commonFunction.getAttributeField("senderDisplayName", "value");
            log.info("Verify Sender's Display Name length" + inputText);
            Assert.assertEquals("!!!ERR. String length is not match", Integer.parseInt(length), inputText.length());
        } else if (inputField.equals("Sender's Address")) {
            String inputText = commonFunction.getAttributeField("senderAddress", "value");
            log.info("Verify Sender's Address length" + inputText);
            Assert.assertEquals("!!!ERR. String length is not match", Integer.parseInt(length), inputText.length());
        } else {
            Assert.fail("Something wrong with " + inputField + " and " + length);
        }
    }

    @And("Verify new campaign information are correct on landing page")
    public void verify_campaign_information() {
        log.info("Verify new campaign information are correct on landing page");
        log.info("TestCase Data: " + testData);
        String campaignName = testData.getString("campaign", "name");
        String campaignType = testData.getString("campaign", "type");
        log.info("Search campaign on UI: " + campaignName);
        commonFunction.findElementByDataID("searchTable").clear();
        commonFunction.sendKeysToTextBox("searchTable", campaignName);
        commonFunction.waitForPageLoadComplete(10, 3);
        List<WebElement> row = commonFunction.findElementsByXpath(locator.getLocator("rowsList"));
        List<WebElement> cell = commonFunction.findElementsByXpath(locator.getLocator("cellsList"));
        Assert.assertEquals("ERR!!!. Should found only 1 record on landing page", 1, row.size());
        String name = cell.get(1).getText();
        String type = cell.get(2).getText();
        log.info("Verify campaign name: " + campaignName);
        Assert.assertEquals("ERR!!!. Campaign Name is not match", campaignName, name);
        log.info("Verify campaign type: " + campaignType);
        Assert.assertEquals("ERR!!!. Campaign Name is not match", campaignType.toLowerCase(), type.toLowerCase());
    }

    @Then("Verify default value of {string} is {string}")
    public void verify_default_value(String inputField, String value) {
        log.info("Verify default value is: " + inputField);
        if (inputField.equals("Campaign Type")) {
            Assert.assertTrue("!!!ERR. Campaign type finite is not a default value", commonFunction.findElementByDataID("FINITE").isSelected());
        } else if (inputField.equals("Do not associate any contact list at start")) {
            Assert.assertEquals("!!!ERR. Do not associate should false as default value", "false", commonFunction.getAttributeField("noAssociation", "aria-checked"));
        } else if (inputField.equals("Check Time Based Finish Criteria For Paused Campaign")) {
            Assert.assertEquals("!!!ERR. Check Time Based Finish Criteria should false as default value", "false", commonFunction.getAttributeField("checkTimeBasedFinishCriteria", "aria-checked"));
        } else {
            log.info("Something wrong with " + inputField + " and " + value);
            Assert.fail("Something wrong with " + inputField + " and " + value);
        }
    }

    @Then("Verify support option are Finite and Infinite")
    public void verify_support_option_of_campaign_type() {
        log.info("Verify support option are Finite and Infinite");
        Assert.assertTrue("!!!ERR. Campaign type finite is not supported", commonFunction.findElementByDataID("FINITE").isSelected());
        Assert.assertFalse("!!!ERR. Campaign type infinite is not supported", commonFunction.findElementByDataID("INFINITE").isSelected());
    }

    @And("User select {string} option {string}")
    public void user_select_option(String inputField, String value) {
        if (inputField.equals("Campaign Type")) {
            if (value.equals("Finite")) {
                commonFunction.clickAnElementUsingJavaScript(commonFunction.findElementByDataID("FINITE"));
            } else {
                commonFunction.clickAnElementUsingJavaScript(commonFunction.findElementByDataID("INFINITE"));
            }
        }
    }

    @Then("Verify default value is Priority then Retry then Regular")
    public void verify_Dialing_Order() {
        log.info("Verify default value is Priority then Retry then Regular");
        String xpathCard = locator.getLocator("dialingOrderTable");
        String priorityRecords = locator.getLocator("priorityRecords");
        String retryRecords = locator.getLocator("retryRecords");
        String regularRecords = locator.getLocator("regularRecords");
        List<WebElement> items = commonFunction.findElementsByXpath(xpathCard);
        Assert.assertTrue("!!!ERR. Order is not corrected", items.get(0).findElement(By.xpath(priorityRecords)).isDisplayed());
        Assert.assertTrue("!!!ERR. Order is not corrected", items.get(1).findElement(By.xpath(retryRecords)).isDisplayed());
        Assert.assertTrue("!!!ERR. Order is not corrected", items.get(2).findElement(By.xpath(regularRecords)).isDisplayed());
    }

    @When("User clicks the refresh button")
    public void userClicksOnRefreshButton() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean isDisplayed = CampaignManagerPageObj.refreshButton.isDisplayed();
        Assert.assertTrue(isDisplayed);
        CampaignManagerPageObj.refreshButton.click();
    }

    @And("Click on type column and verify its sort")
    public void verifySortingAscAndDesc() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.selectTypeColumn.click();
        utilityFun.wait(2);
        boolean isDisplayed = CampaignManagerPageObj.sortingArrowUp.isDisplayed();
        Assert.assertTrue(isDisplayed);
        CampaignManagerPageObj.selectTypeColumn.click();
        utilityFun.wait(2);
        isDisplayed = CampaignManagerPageObj.sortingArrowDown.isDisplayed();
        Assert.assertTrue(isDisplayed);
    }

    @And("Verify pagination reset to default values")
    public void verifyPaginationResetsToDefault() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.selectDropDownOptionEqual("pageSizeDropdown", "10");
        log.info("Default number of record configured on campaign landing page for pagination");
    }

    @When("Admin user click on Finish At Date Time Button")
    public void adminUserClickOnFinishAtDateTimeButton() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickAnElementUsingJavaScript(commonFunction.findElementByDataID("FINISH_AT"));
    }

    @Then("Triple dot should display each row on campaign manager page")
    public void verifyATripleDotShouldDisplayEachRowOnCampaignManagerPage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify triple dots display");
        Assert.assertTrue("Not found any campaign on the landing page", commonFunction.isDisplayed("more-actions", 10));
    }

    @Then("Default page size display at {int}")
    public void defaultPageSizeDisplayAt(int numberPageSizeExpected) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify default page size display at " + numberPageSizeExpected);
        int numberPageSizeActual = commonFunction.getCurrentPageSize();
        Assert.assertEquals("The default page size is not display correctly", numberPageSizeExpected, numberPageSizeActual);
    }

    @And("Number record page display correctly when change the page size to {string}")
    public void verifyNumberRecordDisplayCorrectlyWhenChangePageSize(String pageSizes) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify number record display correctly when change the page size");
        List<String> listPageSizes = Arrays.asList(pageSizes.split(","));
        Assert.assertTrue("Number record page is not display correctly", CampaignManagerPageObj.verifyRecordDisplayCorrectly(listPageSizes));
        log.info("Number record display correctly when change the page size");
    }

    @Then("User able to change the page size {string}, record should reloaded on page size changed and should take user to first page")
    public void verifyPageSizeWorkProperly(String pageSizes) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.verifyNumberRecordDisplayCorrectlyWhenChangePageSize(pageSizes);
        log.info("Number record display correctly when change the page size");
    }

    @When("User input text to field search")
    public void userInputTextToFieldSearch() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify Campaign landing display at least one record");
        commonFunction.selectDropDownOptionEqual("pageSizeDropdown", "100");
        commonFunction.sendKeysToTextBox("searchTable", testData.getData("campaign", "name"));
        commonFunction.waitForPageLoadComplete(30, 2);
    }

    @Then("The quick search should start displaying the matched campaigns details if the search text entered is present anywhere in the campaign name")
    public void theQuickSearchWorkProperly() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify quick search work correctly");
        Assert.assertTrue("The quick search work not properly", commonFunction.verifyBasicSearchWorkCorrect(testData.getData("campaign", "name"), "Name"));
    }

    @Then("The search results should keep getting updated as the campaign name characters are entered by the user in the search box")
    public void verifyResultSearchUpdateWhenInputCharacter() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Update character on quickly search ");
        boolean expected = CampaignManagerPageObj.updateCharacterOnSearchTextBox("1");
        Assert.assertTrue("The result campagin list can't update when update search box", expected);
    }

    @Then("User can select any campaign that from list result without the need to click on any other button")
    public void userCanSelectAnyCampaignFromResultSearch() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify that list result displayed corectly ");
        boolean result = CampaignManagerPageObj.verifyQuickSearchWorkCorrect(testData.get("Campaign Name"));
        Assert.assertTrue("The basic search is not working properly", result);
    }

    @Then("User should be able to View and Edit operations for the searched campaign\\(s)")
    public void verifyUserCanPracticeWhenSearch() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify User Can View campaign when search");
        commonFunction.sendKeysToTextBox("searchTable", testData.getData("campaign", "name"));
        log.info("Verify User Can Edit any campaign on list result search");
        Assert.assertTrue("Page Edit is not display", CampaignManagerPageObj.verifyUserCanClickEditCampaign());
    }

    @Then("The default display order of records should be based on update time of the record")
    public void verifyDefaulSortWorkCorrectly() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify default display order of record should be based on update time of the record");
        log.info("Verify newly added campaign is displayed on first rows");
        Map<String, String> campaign = testData.getData("campaign");
        String defCamp = campaign.get("name");
        campaign.put("name", defCamp + "update");
        RestMethodsObj.createCampaign(campaign);
        campaign.put("name", defCamp);
        commonFunction.tryClick("refreshTable", 10);
        utilityFun.wait(2);
        String nameCampaignDisplayActual = CampaignManagerPageObj.getListColumnDataOnCurPage("Name").get(0);
        log.info("nameCampaignDisplayActual: " + nameCampaignDisplayActual);
        Assert.assertTrue("Default display order of records is not based on update time of the " + "record", nameCampaignDisplayActual.equalsIgnoreCase(defCamp + "update"));
        log.info("Verify newly edited campaign is displayed on first rows");
        String editedCampName = CampaignManagerPageObj.getNameCampaignJustEdit("Edit description for check");
        nameCampaignDisplayActual = CampaignManagerPageObj.getListColumnDataOnCurPage("Name").get(0);
        log.info("nameCampaignDisplayActual: " + nameCampaignDisplayActual);
        Assert.assertTrue("Default display order of records is not based on update time of the " + "record", nameCampaignDisplayActual.equalsIgnoreCase(editedCampName));
    }

    /**
     * method using to verify sort work properly with Name and Type column
     *
     * @param nameColumn: Name of column on campaign landing page and user input from step
     */
    @Then("The {string} column should be sorted in desired order as per the admin's selection.")
    public void verifySortedInDesiredOrderAsPerTheAdminSSelection(String nameColumn) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify sort work properly with " + nameColumn + " column");
        boolean expected = CampaignManagerPageObj.verifySortWorkProperly(nameColumn);
        Assert.assertTrue("Sort can work on campaign landing page", expected);
        log.info(nameColumn + " sorted in desired order as per the admin's selection.");
    }

    /**
     * method using to verify sort display correctly when user click on column header
     *
     * @param nameColumn: Name of column on campaign landing page and user input from step
     */
    @Then("The {string} column toggle between Asc and Desc orders with UI indication transitioning from one order to another when clicked.")
    public void verifyTogglebetweenAscAndDescWhenClickedagain(String nameColumn) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify sort work properly with " + nameColumn + " column");
        boolean expected = CampaignManagerPageObj.verifySortIconDisplayInColumn(nameColumn);
        Assert.assertTrue("Sort icon is not displayed correctly when use click " + nameColumn + " column on campaign landing page", expected);
        log.info("Sort icon displayed correctly when use click " + nameColumn + " column on campaign landing page");
    }

    @When("User clicks on any one of these table column - Name, Type")
    public void userClicksOnAnyOneOfTheseTableColumnNameType() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.verifySortIconDisplayInColumn("Name");
        CampaignManagerPageObj.verifySortIconDisplayInColumn("Type");
        log.info("Sort icon displayed correctly when use click name column on campaign landing page");
    }

    @Then("Default sorting order would be - Campaign update time Ascending order")
    public void defaultSortingOrderWouldBeCampaignUpdateTimeAscendingOrder() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify Default sorting order would be - Campaign update time Ascending order");
        String nameCampaignDisplayActual = CampaignManagerPageObj.getNameCampaignFirstRow();
        log.info("nameCampaignDisplayActual: " + nameCampaignDisplayActual);
        log.info("Expected name: " + testData.getData("campaign", "name"));
        Assert.assertTrue("Default display order of records is not based on update time of the " +
                "record", nameCampaignDisplayActual.contains(testData.getData("campaign", "name")));
        log.info("Default sorting order would be - Campaign update time Ascending order");
    }

    @And("User enters a text that has no match for campaign name")
    public void searchCampaignName() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Search Campaign----------------------------");
        RestMethodsObj.deleteCampaign(testData.getOrDefault("Campaign Name", ""), "");
        CampaignManagerPageObj.searchCampaign(testData.get("Campaign Name"));
    }

    @Then("List of campaigns table is displayed with {string} message in place of rows")
    public void recordListEmptyDisplay(String expectedMessage) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String messageOnPage = driver.findElement(locator.get("emptyList")).getText();
        log.info("messageOnPage: " + messageOnPage);
        Assert.assertTrue("Record not display in place of rows", messageOnPage.contains(expectedMessage));
    }


    @And("Previous page, next page, number page and change number page record option is not display on campaign landing page")
    public void checkButtonNotDisplay() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            commonFunction.waitForPageLoadComplete(30, 2);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
            List<WebElement> previousPage = driver.findElements(locator.get("btnPreviousPage"));
            log.info("previousPage: " + previousPage);
            Assert.assertFalse("Button previous Page display", previousPage.size() > 0);
            List<WebElement> nextPage = driver.findElements(locator.get("btnNextPage"));
            log.info("nextPage: " + nextPage);
            Assert.assertFalse("Button Next Page display", nextPage.size() > 0);
            List<WebElement> numberPage = driver.findElements(By.xpath("//button[@data-testid='page_1']"));
            log.info("numberPage: " + numberPage);
            Assert.assertFalse("Button Page Number display", numberPage.size() > 0);
            List<WebElement> goToPage = driver.findElements(By.id("goToPage"));
            log.info("goToPage: " + goToPage);
            Assert.assertFalse("Button Goto Page display", goToPage.size() > 0);
            List<WebElement> pageSizeDropdown = driver.findElements(locator.get("pageSizeDropdown"));
            log.info("pageSizeDropdown: " + pageSizeDropdown);
            Assert.assertFalse("Button select page size display", pageSizeDropdown.size() > 0);
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        }
    }

    @When("User clicks the filter button, selects the filter criteria and types in the filter search box with {string} {string} {string} and verify results")
    public void filterCampaign(String colName, String operator, String searchText) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> expectedList = new ArrayList<>();
        List<String> actualList = new ArrayList<>();
        expectedList = commonFunction.getExpectedListNameByOperator(operator, searchText);
        commonFunction.applyAdvanceSearch(colName, operator, searchText);
        commonFunction.verifyAdvanceSearchForNameCol();
        if (commonFunction.presentElement(By.xpath(locator.getLocator("emptyListMessage")), 3) == null) {
            if (commonFunction.getNumberPage() > 1) {
                commonFunction.navigateToPageByGoToPage(1);
                commonFunction.waitForPageLoadComplete(10, 1);
            }
        }
        actualList = commonFunction.getListColumnDataOnAllPage(colName);
        log.info("Size actual list: " + actualList.size());
        log.info("Size expected list: " + expectedList.size());
        log.info("Expected list: " + expectedList);
        log.info("Actual list: " + actualList);
        Assert.assertTrue("Count contact list not match", expectedList.size() == actualList.size());
    }

    @Then("The filter should start displaying the matched campaigns details based on the filter criteria applied on the search text {string}")
    public void checkListCampaign(String listExpectedCamp) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.checkListCampaign(listExpectedCamp);
    }

    @When("Check option column and operation")
    public void checkValueSelectDropDownOption() {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (!CampaignManagerPageObj.btnFilterCampaign.getAttribute("class").contains("neo-btn-square neo-icon-filter-filled")) {
            CampaignManagerPageObj.btnFilterCampaign.click();
            utilityFun.wait(1);
        }
        Assert.assertTrue("Name column is not display", commonFunction.isDisplayed("columnName-wrapper", 10));
        Assert.assertTrue("Operator column is not display", commonFunction.isDisplayed("operator-wrapper", 10));
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    @Then("User should be able to clear the filter by clicking the filter button again")
    public void checkListCampaignAfterClickFilterAgain() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Clicking the filter button again----------------------------");
        commonFunction.tryClick("columnFilters", 10);
        utilityFun.wait(1);
    }

    @Then("The search results should keep getting updated as the characters are entered by the user in the filter search box")
    public void addCharacters() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("----------------------------Filter Campaign----------------------------");
        String newCampName = testData.getData("campaign", "name") + "_1";
        CampaignManagerPageObj.findElementByDataID("searchValue").sendKeys("_1");
        commonFunction.waitForPageLoadComplete(30, 2);
        Assert.assertTrue("Campaign not match", commonFunction.getListColumnDataOnAllPage("Name").contains(newCampName));
    }

    @And("User Clicks a campaign name link")
    public void clickCampaignLink() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Click  campaign link");
        commonFunction.waitForPageLoadComplete(30, 2);
        commonFunction.clickTakeMoreActionElipse(testData.getStrict("campaign", "name"));
    }

    @Then("User should be able to select desired campaign from the displayed campaigns at any point of time without the need to click on any other button")
    public void selectDesiredCampaign() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        clickCampaignLink();
    }

    @Then("Verify session Time Based Finish Criteria is only visible on Finite Campaign")
    public void verify_session_not_visible() {
        log.info("Verify session Time Based Finish Criteria is only visible on Finite Campaign");
        log.info("Check Time Based Finish Criteria For Paused Campaign should not displayed");
        try {
            commonFunction.findElementByDataID("checkTimeBasedFinishCriteria").isDisplayed();
            Assert.fail("ERR!!! Option for Paused Campaign is displayed on UI");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @When("User should be able to initiate {string}")
    public void checkFunctionCampaign(String action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (action) {
            case ("View"):
                CampaignManagerPageObj.campaignView();
                break;
            case ("Edit"):
                CampaignManagerPageObj.editDesCamp(testData.getStrict("campaign", "EditDescription"));
                break;
            case ("Delete"):
                CampaignManagerPageObj.deleteFirstCamp();
                break;
            default:
                Assert.fail("Cannot perform this action: " + action);
        }
    }

    @And("The new values are saved successfully for the selected campaign")
    public void checkValuesSaved() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.verifyCampaign(testData.getData("campaign"));
    }

    @Then("Campaign {string} after delete no display")
    public void verifyCampaignDelete(String campName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int checkCampaignName = driver.findElements(By.xpath("//*[contains(text(),'" + campName + "')]")).size();
        Assert.assertFalse("Campaign not delete success", checkCampaignName > 0);
    }

    @And("Create a list of {int} consecutive campaigns")
    public void createAListOfConsecutiveCampaigns(int numOfCamp) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.createCampaignForTesting(numOfCamp);
        try {
            commonFunction.clickAnElementUsingJavaScript("refreshTable", 5);
        } catch (Exception e) {
        }
    }

    @Given("Read data from json file")
    public void ReadData(Map<String, String> info) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        fetchLabInformationAndTestData(info);
    }

    @When("User {string} for update campaign")
    public void updateCampaign(String action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Update campaign: " + action.toLowerCase());
        Map<String, String> dataUpdate = (Map) testData.getAsMap("dataUpdate");
        CampaignManagerPageObj.updateCampaign(action, dataUpdate.get("name"));
    }

    @Then("Name campaign and type can not edit")
    public void CampaignNameTypeNoteEdit() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Check Name campaign not edt");
        Assert.assertFalse(commonFunction.findElementByDataID("name").isEnabled());
        log.info("Check type campaign not edit");
        Assert.assertFalse("Type campaign can edit", CampaignManagerPageObj.selectTypeInfinite.isEnabled());
        Assert.assertFalse("Type campaign can edit", CampaignManagerPageObj.selectTypeFinite.isEnabled());
    }

    @And("Edit value for campaign {string}")
    public void EditValue(String value) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Edit value for campaign" + value);
        Map<String, String> dataUpdate = (Map) testData.getAsMap("dataUpdate");
        CampaignManagerPageObj.editCampaignValue(dataUpdate);
    }

    @When("User clicks {string}")
    public void SaveOrCancel(String Action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.SaveOrCancelCampaignEdit(Action);
    }

    @Then("A user friendly toast message indicating that the edited values are saved successfully is displayed")
    public void checkNotification() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        testData.put("NotificationAlert", "Campaign updated successfully.");
        commonFunction.verifyNotification(testData);
    }

    @And("The new values are saved successfully for the selected campaign.")
    public void checkValuesAfterSaved() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        utilityFun.wait(2);
        Map<String, String> dataUpdate = (Map) testData.getAsMap("dataUpdate");
        CampaignManagerPageObj.checkValueSave(dataUpdate);
    }

    @Then("The new variable {string} has a maximum length of {int} characters")
    public void CheckCharacterLength(String value, Integer chrLimit) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Check character length: <" + value + "> is <" + chrLimit + ">");
        CampaignManagerPageObj.CheckCharacterLength(value);
    }

    @And("Edit {string} value contain special characters then an error message {string} will be displayed")
    public void EditValueSpecialCharacters(String value, String Error) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.ValueSpecialCharacters(value, Error, testData);
    }

    @And("User click {string} after cancel")
    public void leaveThisPageOrStayThisPage(String action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.leaveThisPageOrStayThisPage(action);
    }

    @And("The admin has made changes for one or more editable values for campaign {string}")
    public void editValue(String CampaignName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Edit value for campaign" + CampaignName);
        EditValue(CampaignName);
    }

    @Then("Edited variables are kept on the page")
    public void checkValueAfterCancel() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> dataUpdate = (Map) testData.getAsMap("dataUpdate");
        CampaignManagerPageObj.CheckValueAfterCancel(dataUpdate);
        commonFunction.clickButton("cancel");
        commonFunction.waitForPageLoadComplete(10, 2);
        commonFunction.clickButton("positive-action");
    }

    @And("The admin has made changes for one or more editable values for campaign {string}, user clicks {string},user click {string} after cancel. Then Variables are not changed for the campaign")
    public void editableValues(String CampaignName, String Action, String ActionAfterCancel) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Edit value for campaign" + CampaignName);
        Map<String, String> dataUpdate = (Map) testData.getAsMap("dataUpdate");
        CampaignManagerPageObj.CheckOldValueAfterCancel(Action, ActionAfterCancel, dataUpdate);
    }

    @Then("An error toast message for re-trial after sometime should be displayed with reason for failure as Unknow error occurred")
    public void VerifyUnknowErrorDisplayed() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.UnknowError();
    }

    @And("Using {string} to edit the mandatory fields to empty")
    public void MandatoryFieldsIsEmpty(String action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Search campaign " + testData.get("Campaign Name"));
        CampaignManagerPageObj.SearchCampaignName(testData.get("Campaign Name"), action);
        log.info("Edit mandatory fields to empty");
        CampaignManagerPageObj.EditMandatoryField(testData.get("Campaign Name"));
    }

    @When("Admin tries to save the campaign")
    public void TryToSave() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.SaveOrCancelCampaignEdit("save");
    }

    @Then("Failure message with indicating the missing mandatory field should be displayed, campaign should not be updated or saved and User should remain on campaign update page")
    public void VerifyCampaignWithError() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.SaveWithErrorMessage();
    }

    @Given("Delete all campaign")
    public void deleteAllCampaign() {
        RestMethodsObj.deleteAllCampaign();
        try {
            CampaignManagerPageObj.refreshButton.click();
        } catch (Exception e) {
        }
    }

    @Then("Add data for story")
    public void AddData() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> campaign = (Map) testData.getAsMap("campaign");
        CampaignManagerPageObj.addSimpleCampaign(campaign.get("name"), campaign.get("description"), campaign.get("type"), campaign.get("strategy"), campaign.get("senderDisplayName"), campaign.get("senderAddress"), campaign.get("contactList"), campaign.get("finishTypeAndTime"));
    }

    @When("Click Add New Campaign button, fill the name and description then associate strategy to campaign")
    public void clickAddNewCampaignButtonFillTheNameAndDescription() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.clickAddNewCampaignButtonFillTheNameAndDescription(testData.getString("campaign", "name"), testData.getString("campaign", "description"));
        CampaignManagerPageObj.associateStrategyToCamp(testData.getString("strategy", "name"));
    }

    @And("In the Contact List Configuration, click select box to open the dropdown menu")
    public void inTheContactListConfigurationClickSelectBoxToOpenTheDropdownMenu() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Click to `Contacts` tab");
        commonFunction.clickTab("tab-contacts");
        utilityFun.wait(0.5f);
        commonFunction.findElementByDataID("contactList").click();
    }

    @Then("Response time to display contact list should not exceed {int} seconds")
    public void responseTimeToDisplayContactListShouldNotExceedSeconds(int responseTime) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertNotNull("The dropdown menu not display within " + responseTime + " sec(s)",
                commonFunction.presentElement(By.xpath(locator.getLocator("listContactListOnDropdown")), responseTime));
    }

    @Then("Contact list display correctly in alphabetical sort order in dropdown")
    public void contactListDisplayCorrectlyInAlphabeticalSortOrderInDropdown() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> clList = CampaignManagerPageObj.newCampaignPage_getContactListInDropDown();
        Assert.assertTrue("Contact list is not sorted", Comparators.isInOrder(clList, Comparator.naturalOrder()));
    }

    @Then("Associate contact list field display on create campaign page")
    public void verifyAssociateContactListFieldDisplayOnCreateCampaignPage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Associate contact list field is not display on create campaign page" +
                "", commonFunction.isDisplayed("contactList-wrapper", 5));
    }

    @Then("Dropdown contact list displayed on contact list field")
    public void dropdownContactListDisplayedOnContactListField() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Dropdown contact list is not display on contact list field" +
                "", commonFunction.presentElement(locator.get("dropdownContactList"), 10).isDisplayed());
    }

    @Then("Dropdown contact list displayed max {int} value")
    public void dropdownContactListDisplayed20Value(int numberRecord) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Expected size of dropdown :" + numberRecord);
        log.info("Get size contact list on dropdown");
        int numberValueDropdown = commonFunction.findElementsByXpath(locator.getLocator("numberValueContactListOnDropdown")).size();
        log.info("size of dropdown : " + numberValueDropdown);
        log.info("Verify size contact list on dropdown is max 100 value");
        Assert.assertEquals("Size of dropdown display incorrectly", numberRecord, numberValueDropdown);
    }

    @And("Input campaign name and campaign strategy after click save campaign button")
    public void inputCampNameAndCampaignStrategyAfterClickSaveCampaign() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Input name campaign on campaign field and campaign strategy after click save campaign button");
        commonFunction.sendKeysToTextBox("name", testData.getString("campaign", "name"));
        utilityFun.wait(1);
        CampaignManagerPageObj.associateStrategyToCamp(testData.getString("strategy", "name"));
        commonFunction.tryClick("save", 10);
    }

    @Then("User can not save when do not select contact list")
    public void userCanNotSaveWhenDoNotSelectContactList() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Click save contact list icon on contact list field");
        commonFunction.tryClick("checkIcon", 10);
        WebElement hintMessageElement = commonFunction.presentElement(commonFunction.findElementByXpath(locator.getLocator("hintMessageAssociateContactList")), 10);
        Assert.assertEquals("Hint Message is not display correctly", "This is a required field.", hintMessageElement.getText());
    }

    @And("Select contact list {string} and click save icon")
    public void selectContactListAndClickSaveIcon(String nameContactList) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Name Contact list : " + nameContactList);
        log.info("Select contact list " + nameContactList + " to associate campaign");
        commonFunction.selectDropDownOptionEqual(commonFunction.findElementByDataID("contactDataType_contactList"), nameContactList);
        utilityFun.wait(1);
        log.info("Click save contact list icon on contact list field");
        commonFunction.tryClick("checkIcon", 10);
    }

    @Then("User can save contact list {string}")
    public void userCanSaveContactList(String nameContactList) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Name Contact list : " + nameContactList);
        log.info("verify contact list " + nameContactList + " saved successful");
        Assert.assertTrue("Dropdown contact list is not display on contact list field" + "", commonFunction.isDisplayed("editIcon", 10));
        String actualNameCTL = commonFunction.findElementByDataID("contactDataType_contactList").getAttribute("title");
        Assert.assertEquals("Contact list display incorrectly with selected by user", nameContactList, actualNameCTL);
    }

    @And("Input all the valid in Mandatory field without contact list and click save campaign")
    public void inputAllTheValidWithoutContactListAndClickSaveCampaign() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Input name campaign on campaign field and click save campaign button");
        CampaignManagerPageObj.txtBoxCampaignName.sendKeys("Automation_AC1_TC6");
        commonFunction.tryClick("checkIcon", 10);
    }

    @Then("user can not create campaign without associate contact list")
    public void userCanNotCreateCampaignWithoutAssociateContactList() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("verify user can not create campaign without associate contact list");
        WebElement hintMessageElement = commonFunction.presentElement(commonFunction.findElementByXpath(locator.getLocator("hintMessageAssociateContactList")), 10);
        Assert.assertEquals("Hint Message is not display correctly", "This is a required field.", hintMessageElement.getText());
    }

    @When("Create a campaign with contact list")
    public void createCampWithContactList() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.clickAddNewCampaignButtonFillTheNameAndDescription(testData.getString("campaign", "name"), testData.getString("campaign", "description"));
        CampaignManagerPageObj.associateContactListToCamp(testData.getString("contactlist", "name"));
        CampaignManagerPageObj.associateStrategyToCamp(testData.getString("strategy", "name"));
        commonFunction.clickButton("save");
        commonFunction.waitForPageLoadComplete(10, 1);
    }

    @Then("Create campaign message successfully display")
    public void verifySuccessToastMessageDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> data = testData.getData("campaign");
        commonFunction.verifyNotification(data);
    }

    @And("Update contact list for campaign")
    public void changeContactListForCamp() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String newCl = testData.getString("contactlist", "updateContactList");
        try {
            commonFunction.clickTab("tab-campaign");
            commonFunction.sleepInSec(0.2);
            WebElement element = commonFunction.findElementByDataID("strategy");
            if (element != null && (element.getAttribute("value") == null || element.getAttribute("value").isEmpty())) {
                element.click();
                commonFunction.sleepInSec(0.5);
                commonFunction.tryClick(By.xpath(locator.getLocator("numberValueStrategyOnDropdown")), 10);
                commonFunction.sleepInSec(0.5);
            }
        } catch (Exception exception) {
        }
        CampaignManagerPageObj.associateContactListToCamp(newCl);
    }

    @And("Verify contact list associated information display correctly on campaign landing page")
    public void verifyContactListAssociatedOnLandingPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String expCtlist = testData.getString("contactlist", "updateContactList");
        String campName = testData.getString("campaign", "name");
        String actCtlName = commonFunction.getSingleDataFromTable(campName, "Contact List");
        Assert.assertEquals("Information of contact list associated to campaign not match with expected value", expCtlist, actCtlName);
    }

    @And("User click on Edit option of campaign")
    public void userClickOnEditOptionOfStrategy() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickTakeMoreActionElipse(testData.get("Campaign Name"));
        commonFunction.clickAction(testData.get("Campaign Name"), "Edit");
        commonFunction.waitForPageLoadComplete(10, 1);
    }

    @And("Select campaign name link on landing page")
    public void selectCampLinkName() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.searchCampaign(testData.get("Campaign Name"));
        CampaignManagerPageObj.clickToCampaign(testData.get("Campaign Name"));
    }

    @Then("Name of contact list associated to campaign should be display correctly on add new campaign page")
    public void getContactListTextOnEditCampPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement ctlName = commonFunction.findElementByXpath(locator.getLocator("inputDropdownContactList"));
        log.info("sItem on ui: " + ctlName.getAttribute("value"));
        log.info("sItem is selected: " + testData.getString("contactlist", "updateContactList"));
        Assert.assertEquals("Contast list associated to campaign display not match name", ctlName.getAttribute("value"), testData.getString("contactlist", "updateContactList"));
    }

    @Then("Un-associate contact lists existing to campaign and verify save button is disable")
    public void getNotifyMessageDisplayWhenUnassociateContactListToCampaign() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickTab("tab-contacts");
        commonFunction.sleepInSec(0.2);
        WebElement ctlistField = commonFunction.findElementByXpath(locator.getLocator("inputDropdownContactList"));
        ctlistField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        commonFunction.sleepInSec(0.5);
        Assert.assertFalse("Save button still enable on edit campaign page", commonFunction.isEnable("save"));
        this.userCanNotCreateCampaignWithoutAssociateContactList();
    }

    @And("Click to {string} button")
    public void clickToSaveButton(String button) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton(button);
        utilityFun.wait(1);
    }

    @And("Clean up campaign and contact list data")
    public void cleanUpData() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsObj.deleteAllCampaign(testData.get("Campaign Name"));
        RestMethodsContactListObj.deleteAllContactList(testData.get("name"));
    }

    @And("Search contact list added and assign to campaign")
    public void searchContactListAndAssignToCampaign() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String clName = testData.getString("contactlist", "name");
        log.info("Contact List Name: " + clName);
        commonFunction.clickTab("tab-contacts");
        utilityFun.wait(0.5f);
        commonFunction.findElementByDataID("contactList").click();
        utilityFun.wait(0.5f);
        commonFunction.findElementByDataID("contactList").sendKeys(clName);
        utilityFun.wait(0.5f);
        commonFunction.findElementByXpath(String.format(locator.getLocator("contactListOnDropdown"), clName)).click();
        utilityFun.wait(0.3f);
    }

    @And("Delete all campaign with Name contains {string}")
    public void deleteAllCampWithNameContains(String name) {
        RestMethodsObj.deleteAllCampaign(name);
    }

    @And("Delete all contact list contains name {string} and campaign contains Name {string}")
    public void deleteAllContactListAndCampWithNameContains(String clName, String campName) {
        RestMethodsObj.deleteAllCampaign(campName);
        RestMethodsContactListObj.deleteAllContactList(clName);
    }

    @Then("Click save button, the campaign created with this contact list")
    public void clickSaveButtonTheCampaignCreatedWithThisContactList() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("save");
        utilityFun.wait(1);
        List<String> messages = commonFunction.getListToastMessage();
        log.info("Toast message: " + messages);
        Assert.assertTrue("Message not match",
                messages.stream().anyMatch(m -> m.contentEquals("Campaign added successfully.")));
    }

    @And("Enter the invalid or unmatch contact list name to search box")
    public void enterTheInvalidOrUnmatchContactListNameToSearchBox() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.newCampaignPage_searchContactList(testData.getString("contactlist", "unmatchName"));
        utilityFun.wait(1);
    }

    @Then("Verify the list contact list in the search results is empty")
    public void verifyTheListContactListInTheSearchResultsIsEmpty() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> contactListInDropDown = CampaignManagerPageObj.newCampaignPage_getContactListInDropDown();
        log.info("contactListInDropDown: " + contactListInDropDown);
        Assert.assertTrue("Still found contact list", contactListInDropDown.isEmpty());
    }

    @And("Campaign cannot be save")
    public void campaignCannotBeSave() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertFalse("Save button still Enable", commonFunction.isEnable("save"));
    }

    @Then("The Contact List attribute will display on the Header")
    public void theContactListAttributeWillDisplayOnTheHeader() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertNotEquals("Contact List not display in Header", -1, commonFunction.getColumnIndex("Contact List"));
    }

    @And("Associate contact list to campaign and click save button")
    public void associateContactListToCampaignAndClickSaveButton() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        searchContactListAndAssignToCampaign();
        commonFunction.clickButton("save");
    }

    @Then("Information of campaign display correctly on campaign landing page with contact list associated")
    public void informationCampaignDisplayCorrectlyOnCampaignLandingPageWithAssociateContactList() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campName = testData.getString("campaign", "name");
        String clName = testData.getString("contactlist", "name");
        log.info("Search Campaign on campaign landing page");
        commonFunction.findElementByDataID("searchTable").click();
        commonFunction.sendKeysToTextBox("searchTable", campName);
        utilityFun.wait(2);
        Assert.assertTrue("Campaign " + campName + " not display as top of table",
                commonFunction.getListColumnDataOnCurPage("Name").get(0).contentEquals(campName));
        Assert.assertTrue("contact list " + clName + " is not assign to campaign " + campName,
                commonFunction.getListColumnDataOnCurPage("Contact List").get(0).contentEquals(clName));
    }

    @Then("Search contact list added then verify the list contact list in the search results is sort by name")
    public void searchContactListThenVerifyTheListContactListInTheSearchResultsIsSortByName() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String clName = testData.getString("contactlist", "name");
        CampaignManagerPageObj.newCampaignPage_searchContactList(clName);
        List<String> clList = CampaignManagerPageObj.newCampaignPage_getContactListInDropDown();
        Assert.assertTrue("Contact list is not sorted", Comparators.isInOrder(clList, Comparator.naturalOrder()));
        commonFunction.findElementByXpath(String.format(locator.getLocator("contactListOnDropdown"), clName)).click();
        utilityFun.wait(0.3f);
    }

    @And("Create a campaign {string} with strategy {string} and contact list {string} using api")
    public void createACampaignWithStrategyAndContactListUsingApi(String campName, String strategyName, String contactListName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Campaign Name: " + campName);
        log.info("Strategy Name: " + strategyName);
        log.info("Contact List Name: " + contactListName);
        try {
            RestMethodsContactListObj.deleteContactList(contactListName, "");
            Map<String, String> clData = new HashMap<>();
            clData.put("name", contactListName);
            clData.put("description", "description");
            RestMethodsContactListObj.createContactList(clData);
            Map<String, String> strategyData = new HashMap<>();
            strategyData.put("name", strategyName);
            strategyData.put("description", "description");
            strategyData.put("strategyType", "sms");
            strategyData.put("smsText", strategyName);
            strategyData.put("smsPace", "10");
            strategyData.put("smsPacingTimeUnit", "Second");
            log.info(strategyData);
            RestMethodsObj.createStrategy(strategyData);
            RestMethodsObj.deleteCampaign(campName, "");
            Map<String, String> campData = new HashMap<>();
            campData.put("name", campName);
            campData.put("strategy", strategyName);
            campData.put("description", "description");
            campData.put("type", "Finite");
            campData.put("dialingOrder", "Priority, Retry, Regular");
            campData.put("contactList", contactListName);
            RestMethodsObj.createCampaign(campData);
        } catch (Exception e) {
            log.info("Exception when create campaign ", e);
        }
    }

    @And("Click Add New Campaign button, fill the name {string} with strategy {string} and contact list {string}")
    public void clickAddNewCampaignButtonFillTheNameWithStrategyAndContactList(String campName, String strategyName, String contactListName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Campaign Name: " + campName);
        log.info("Strategy Name: " + strategyName);
        log.info("Contact List Name: " + contactListName);
        commonFunction.clickButton("addNewCampaign");
        commonFunction.enterToTheField("Name", campName);
        commonFunction.enterToTheField("Select Campaign Strategy", strategyName);
        commonFunction.enterToTheField("Contact List", contactListName);
    }

    @And("Create contact list using API")
    public void createContactListusingAPI() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Contact list infor \r\n: " + testData.getData("contactlist"));
        RestMethodsContactListObj.createContactList(testData.getData("contactlist"));
    }

    @And("Create {int} campaigns")
    public void addCampaignsByAPI(int numberCampaigns) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> campaign = testData.getData("campaign");
        String name = campaign.get("name");
        String strategy = campaign.get("strategy");
        for (int i = 0; i < numberCampaigns; i++) {
            campaign.put("name", (name + "_" + i));
            campaign.put("strategy", (strategy + "_" + i));
            RestMethodsObj.createCampaign(campaign);
        }
        campaign.put("name", name);
        campaign.put("strategy", strategy);
        try {
            commonFunction.clickAnElementUsingJavaScript("refreshTable", 5);
        } catch (Exception error) {
            log.info("Exception: " + error);
        }
    }

    @And("Create {int} campaign strategies using API to check associate to campaign")
    public void createSomeStrategies_AssociatetoCmp(int count) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategy = testData.getData("strategy");
        CampaignStrategyPageObj.createListStrategiesPage(count, strategy);
    }

    @And("Create campaign strategies using API to associate to campaign")
    public void createStrategies_AssociatetoCmp() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignStrategyPageObj.createStrategyAPI(testData.getData("strategy"));
    }

    @And("Create multiple campaign strategies using API to associate to campaign")
    public void createMultipleStrategies_AssociatetoCmp() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("---------------------------------------------------------------------------------------");
        log.info("Create Strategy by API");
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        log.info(strategiesData);
        String[] typeArray = strategiesData.get("type").split("\\|");
        String[] nameArray = strategiesData.get("name").split("\\|");
        String[] desArray = strategiesData.get("description").split("\\|");
        String[] smsTextArray = strategiesData.get("smsText").split("\\|");
        String[] smsPaceArray = strategiesData.get("smsPace").split("\\|");
        String[] smsPacingTimeUnitArray = strategiesData.get("smsPacingTimeUnit").split("\\|");
        HashMap<String, String> strategyData = new HashMap<String, String>();
        for (int i = 0; i < nameArray.length; i++) {
            if (typeArray.length == 1) {
                strategyData.put("strategyType", typeArray[0]);
            } else {
                strategyData.put("strategyType", typeArray[i]);
            }
            strategyData.put("name", nameArray[i]);
            strategyData.put("description", desArray[i]);
            strategyData.put("smsText", smsTextArray[i]);
            strategyData.put("smsPace", smsPaceArray[i]);
            strategyData.put("smsPacingTimeUnit", smsPacingTimeUnitArray[i]);
            boolean flag = CampaignStrategyPageObj.createStrategyAPI(strategyData);
            log.info("Strategy " + strategyData.get("name") + " was create success: " + flag);
        }
    }

    @And("Delete campaign strategies was created - {int} to check associtate to campaign")
    public void deleteStrategyCreatedforAssociate(int number) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (number == 1) {
            RestMethodsObj.deleteStrategy(testData.getString("strategy", "name"), "");
        } else {
            for (int i = 0; i < number; i++) {
                String nameStrategy = testData.getString("strategy", "name") + "_" + i;
                RestMethodsObj.deleteStrategy(nameStrategy, "");
            }
        }
    }

    @And("Delete contact list was created")
    public void deleteContactListCreatedforAssociate() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.deleteContactList(testData.getString("contactlist", "name"), "");
    }

    @Then("Associate strategy field display on create campaign page")
    public void verifyAssociateStrategyFieldDisplayOnCreateCampaignPage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Associate contact list field is not display on create campaign page" +
                "", commonFunction.isDisplayed("strategy-wrapper", 5));
    }

    @Then("Dropdown strategy list displayed on strategy field")
    public void dropdownStrategyListDisplayedOnContactListField() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Dropdown strategy list is not display on contact list field" +
                "", commonFunction.dropdownListDisplay("strategy"));
    }

    @Then("Dropdown strategy list displayed max {int} value")
    public void dropdownStrategyListDisplayedMaxValue(int numberRecord) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Expected size of dropdown :" + numberRecord);
        Assert.assertTrue("The list shows more than the limited number", commonFunction.dropdownListDisplayMaxValue("strategy", 100));
    }

    @And("Click Add New campaign button, fill name and description")
    public void addNewCmp_fillNameDes() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.clickAddNewCampaignButtonFillTheNameAndDescription(testData.getString("campaign", "name"), testData.getString("campaign", "description"));
    }

    @And("Associate Contact list to campaign")
    public void associateContactListtoCmp() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.associateContactListToCamp(testData.getString("contactlist", "name"));
    }

    @Then("User can not create campaign without associate strategy")
    @Then("User can not update campaign without associate strategy")
    public void userCanNotCreateCampaignWithoutAssociatestrategy() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("verify user can not create campaign without associate contact list");
        WebElement hintMessageElement = commonFunction.presentElement(commonFunction.findElementByXpath("//div[@data-testid='strategy-wrapper']/div[@class='neo-input-hint']"), 10);
        Assert.assertEquals("Hint Message is not display correctly", "This is a required field.", hintMessageElement.getText());
    }

    @Then("Response time to display Strategy list should not exceed {int} seconds")
    public void responseTimeToDisplayStrategyListShouldNotExceedSeconds(int responseTime) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("", commonFunction.responseTimeToDisplayExceedSec("strategy", responseTime));
    }

    @And("Associate Strategy to campaign")
    public void associateStrategytoCampaign() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getAsMap("strategy");
        String[] nameArray = strategiesData.get("name").split("\\|");
        if (nameArray.length > 1) {
            CampaignManagerPageObj.associateStrategyToCamp(nameArray[0]);
        } else {
            CampaignManagerPageObj.associateStrategyToCamp(strategiesData.get("name"));
        }
    }

    @And("Associate Contact lists to campaign")
    public void associateContactListstoCmp() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.associateContactListToCamp(testData.getString("contactlist", "name"));
    }

    @Then("Verify drop-down result is disappeared when the user selected a stratregy")
    public void verifyDropdownDisappearedWhenStrategywasSeletced() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Drop-down result should be disappeared when the user selected a stratregy", commonFunction.verifyDropdownDisappearedWhenValueSeletced("strategy"));
    }

    @And("Create Contact List and Strategy for campaign")
    public void createStrategyAndContactList() {
        Map<String, String> strategy = testData.getData("strategy");
        log.info("strategy: " + strategy);
        Map<String, String> contactlist = testData.getData("contactlist");
        log.info("contactlist: " + contactlist);
        RestMethodsContactListObj.createContactList(contactlist);
        RestMethodsObj.createStrategy(strategy);
    }

    @And("Create {int} contact list(s) and {int} campaign(s) using the API")
    public void createContactListSAndCampaignSUsingTheAPI(int numCL, int numCamp) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategyData = new HashMap<>();
        strategyData.put("strategyType", "sms");
        strategyData.put("name", testData.getOrDefault("strategyName", "strategy1"));
        strategyData.put("description", "Strategy test");
        strategyData.put("smsText", testData.getOrDefault("smsText", "smsText"));
        strategyData.put("smsPace", "10");
        strategyData.put("smsPacingTimeUnit", "Second");
        RestMethodsObj.createStrategy(strategyData);
        log.info("noContact: " + numCL);
        if (numCL > 0) {
            RestMethodsContactListObj.createContactList(testData);
            if (numCL > 1) {
                String clNameDefault = testData.get("name");
                for (int i = 1; i < numCL; i++) {
                    testData.put("name", clNameDefault + i);
                    RestMethodsContactListObj.createContactList(testData);
                }
                testData.put("name", clNameDefault);
            }
        }
        createAListOfConsecutiveCampaigns(numCamp);
    }

    @And("Create {int} contact lists {int} strategy and {int} campaigns using the API")
    public void createContactListsAndStrategysAndCampaignsByAPI(int numCL, int numStra, int numCamp) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Add : " + numCL + " contact lists");
        Map<String, String> contactlistData = testData.getData("contactlist");
        RestMethodsContactListObj.createContactList(contactlistData);
        if (numCL > 1) {
            String clNameDefault = contactlistData.get("name");
            for (int i = 1; i < numCL; i++) {
                contactlistData.put("name", (clNameDefault + i));
                contactlistData.put("updateContactList", clNameDefault + i);
                RestMethodsContactListObj.createContactList(contactlistData);
            }
            contactlistData.put("name", clNameDefault);
        }

        log.info("Add : " + numStra + " strategys");
        Map<String, String> strategyData = testData.getData("strategy");
        RestMethodsObj.createStrategy(strategyData);
        if (numStra > 1) {
            String strategyDefault = strategyData.get("name");
            for (int i = 1; i < numStra; i++) {
                strategyData.put("name", (strategyDefault + "_" + i));
                RestMethodsObj.createStrategy(strategyData);
            }
            strategyData.put("name", strategyDefault);
        }

        log.info("Add : " + numCamp + " campaigns");
        Map<String, String> campaignData = testData.getData("campaign");
        RestMethodsObj.createCampaign(campaignData);
        if (numCamp > 1) {
            String camapignDefault = campaignData.get("name");
            for (int i = 1; i < numCamp; i++) {
                campaignData.put("name", (camapignDefault + "_" + i));
                RestMethodsObj.createCampaign(campaignData);
            }
            campaignData.put("name", camapignDefault);
        }
    }


    @And("User search and click on {string} option of campaign {string}")
    public void userSearchAndClickOnOptionOfCampaign(String menu, String camp) {
        commonFunction.sendKeysToTextBox("searchTable", camp);
        utilityFun.wait(1);
        commonFunction.clickAnElementUsingJavaScript(By.xpath("//button[@id='dropdown_0']"), 5);
        String editLoc = "//div[@data-testid='action_editCampaign' and ./div[text()='" + menu + "']]";
        commonFunction.tryClick(By.xpath(editLoc), 10);
        utilityFun.wait(1);
    }

    @And("Search campaign {string}")
    public void searchCampaign(String campName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.search(campName);
    }

    @And("Search and click to go to edit campaign")
    public void searchAndClickToCampaign() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campName = testData.getString("campaign", "name");
        searchCampaign(campName);
        CampaignManagerPageObj.clickToCampaign(campName);
    }

    @Then("Clean up data create campaign")
    public void cleanUpDataCreateCampaign() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsObj.deleteCampaign(testData.getString("campaign", "name"), "");
        String[] strategyNameList = testData.getString("strategy", "name").split("\\|");
        for (String strategyName : strategyNameList) {
            RestMethodsObj.deleteStrategy(strategyName, "");
        }
        RestMethodsContactListObj.deleteContactList(testData.getString("contactlist", "name"), "");
    }

    @Then("Verify toast message create campaign successfully")
    @Then("Verify toast message updated campaign successfully")
    public void verifyToastMessageCreateSuccess() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify toast message create campaign successfully");
        commonFunction.verifyNotification(testData.getData("campaign"));
    }

    @Then("Strategy name displayed as part of campaign list")
    public void strategyNameDisplayedasPartofCampaignList() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("verify all colum header campaign page displayed exactly");
        Assert.assertTrue("Column header show not exactly", commonFunction.verifyColumnHeader(testData.getData("campaign")));
        log.info("verify data of campaign just created shown on list campaign exactly");
        Map<String, String> cmpData = new HashMap<String, String>();
        cmpData.put("Name", testData.getString("campaign", "name"));
        cmpData.put("Type", testData.getString("campaign", "Type").toUpperCase());
        cmpData.put("Contact List", testData.getString("contactlist", "name"));
        String[] strategyNameArray = testData.getString("strategy", "name").split("\\|");
        if (strategyNameArray.length > 1) {
            cmpData.put("Campaign Strategy", strategyNameArray[1]);
        } else {
            cmpData.put("Campaign Strategy", strategyNameArray[0]);
        }
        Assert.assertTrue("campaign just created shown not exactly", CampaignManagerPageObj.verifyCampaignAfterCreated(cmpData));
    }

    @Then("Newly created campaign should be displayed at top of the lists")
    public void newlyCreatedCmpOnTopofList() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Newly created campaign should be displayed at top of the lists");
        Assert.assertTrue("Newly created not displayed at top of the lists", CampaignManagerPageObj.campaignInFirstRow(testData.getData("campaign")));
    }

    @When("User {string} for update campaign Strategy")
    @When("User {string} for update campaign page")
    public void updateCampaignStrategy(String action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Update campaign: " + action.toLowerCase());
        CampaignManagerPageObj.updateCampaign(action, testData.getString("campaign", "name"));
    }

    @And("Update Associate Strategy to campaign")
    public void updateAssociateStrategytoCampaign() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] strategyNames = testData.getString("strategy", "name").split("\\|");
        CampaignManagerPageObj.associateStrategyToCamp(strategyNames[1]);
    }

    @And("Clear Associate Strategy to campaign")
    public void clearAssociateStrategytoCampaign() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.clearAssociateStrategyToCamp();
    }

    @And("Create list strategies by API to Associate campaign")
    public void createStrategies(String method) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("---------------------------------------------------------------------------------------");
        log.info("Create Strategy by " + method);
        if (testData.getString("strategy", "strategyType").equalsIgnoreCase("sms")) {
            String[] nameArray = testData.getString("strategy", "name").split("\\|");
            String[] desArray = testData.getString("strategy", "description").split("\\|");
            String[] smsTextArray = testData.getString("strategy", "smsText").split("\\|");
            String[] smsPaceArray = testData.getString("strategy", "smsPace").split("\\|");
            String[] smsPacingTimeUnitArray = testData.getString("strategy", "smsPacingTimeUnit").split("\\|");
            HashMap<String, String> testData1 = new HashMap<String, String>();
            testData1.put("strategyType", "sms");
            testData1.put("name", null);
            testData1.put("description", null);
            testData1.put("smsText", null);
            testData1.put("smsPace", null);
            testData1.put("smsPacingTimeUnit", null);
            for (int i = 0; i < nameArray.length; i++) {
                log.info("!!!Create Strategy: " + nameArray[i] + " !!!");
                testData1.replace("name", nameArray[i]);
                testData1.replace("description", desArray[i]);
                testData1.replace("smsText", smsTextArray[i]);
                testData1.replace("smsPace", smsPaceArray[i]);
                testData1.replace("smsPacingTimeUnit", smsPacingTimeUnitArray[i]);
                JsonObject result = RestMethodsObj.createStrategy(testData1);
                Assert.assertFalse("ERR!!!. The Create Strategy by API unsuccessfully", StringUtils.containsIgnoreCase(result.toString(), "errorcode") || StringUtils.containsIgnoreCase(result.toString(), "errormessage"));
            }
        } else {
            Assert.fail("!!ERR!!: currently ony support SMS type strategy");
        }
    }

    @And("Delete Strategies after tested")
    public void deleteStrategies() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            List<String> strategies = Arrays.asList(testData.getString("strategy", "name").split("\\|"));
            for (int i = 0; i < strategies.size(); i++) {
                RestMethodsObj.deleteStrategy(strategies.get(i), null);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @And("In the Strategy Configuration, click select box to open the dropdown menu")
    public void ClickSelectBoxToOpenTheDropdownMenu() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Click to campaign tab");
        commonFunction.clickTab("tab-campaign");
        commonFunction.waitForPageLoadComplete(60, 1);
        commonFunction.openDropdownList("strategy");
        commonFunction.waitForPageLoadComplete(60, 1);
    }

    @And("Verify The Select Campaign Strategy field is empty")
    public void verifySelectCampaignStrategyFieldEmpty() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Click to campaign tab");
        commonFunction.clickTab("tab-campaign");
        commonFunction.waitForPageLoadComplete(60, 1);
        Assert.assertTrue("!!ERR: The Select Campaign Strategy field is not empty!!", commonFunction.selectedFieldListIsEmpty("strategy"));
    }

    @And("Verify the dropdown menu to select strategy is displayed")
    public void verifyDropDownMenuStrategyDisplayed() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> strategiesList = CampaignManagerPageObj.getStrategyInListDropDownInCampaignPage();
        Assert.assertTrue("!!ERR: The list dropdown does not show!!", (strategiesList.size() > 0));
    }

    @And("Strategies display correctly in ascending order name in dropdown")
    public void verifyAscendingOrderNameStrategyInDropdown() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> strategiesList = CampaignManagerPageObj.getStrategyInListDropDownInCampaignPage();
        log.info("Actual strategies sorted ascending in Dropdown name box: " + strategiesList);
        List<String> listDataAfterSort = strategiesList;
        Collections.sort(listDataAfterSort);
        log.info("Expected strategies sorted ascending in Dropdown name box: " + listDataAfterSort);
        Assert.assertEquals("!!ERR: The list dropdown does not sort ascending order name!!", strategiesList, listDataAfterSort);
    }

    @And("The strategies is showed fully in the dropdown")
    public void verifyTheStrategiesIsShowedFullyInDropdown() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> strategiesList = CampaignManagerPageObj.getStrategyInListDropDownInCampaignPage();
        List<String> strategiesListExp = RestMethodsObj.getAllStrategyAndSortStringFromAPI("default", testData.getData("strategy"));
        log.info("Expected strategies in DB: " + strategiesListExp);
        log.info("Actual strategies in Dropdown box: " + strategiesList);
        Assert.assertTrue("!!ERR: The list dropdown does not show fully, size incorrect!!", (strategiesList.size() == strategiesListExp.size()));
        boolean flag = false;
        for (int i = 0; i < strategiesList.size(); i++) {
            for (int j = 0; j < strategiesListExp.size(); j++) {
                if (strategiesList.get(i).equals(strategiesListExp.get(j))) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                log.info("Strategy: " + strategiesList.get(i) + " is missed");
                break;
            }
        }
        Assert.assertTrue("!!ERR: The list dropdown does not show fully!!", flag);
    }

    @And("Search strategy in Select Campaign Strategy field")
    public void searchStrategyInSelectCampaignStrategyField(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        HashMap<String, String> testData1 = utilityFun.readJsonToHashMap(data.get("DataFile"), data.get("Data"));
        Assert.assertTrue("!!ERR: Cannot input string to search strategy field!!", CampaignManagerPageObj.inputSearchStrategyToDropDownInCampaignPage(testData1));
        commonFunction.waitForPageLoadComplete(60, 1);
    }

    @And("Verify the result searching in dropdown box is correct")
    public void verifyResultSearchingStrategyInDropdownBox(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        HashMap<String, String> testData1 = utilityFun.readJsonToHashMap(data.get("DataFile"), data.get("Data"));
        List<String> strategiesList = CampaignManagerPageObj.getStrategyInListDropDownInCampaignPage();
        List<String> strategiesListExp = RestMethodsObj.getStrategyContainStringFromAPI(testData1.get("name"), testData1);
        Collections.sort(strategiesListExp);
        log.info("Actual strategies searched in Dropdown box: " + strategiesList);
        log.info("Expected strategies sorted ascending in Dropdown name box: " + strategiesListExp);
        Assert.assertEquals("!!ERR: The list dropdown does not sort ascending order name or searching incorrectly!!", strategiesList, strategiesListExp);
    }

    @And("Clear the search box of \"Select Campaign Strategy\" field")
    public void clearInput() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clearSearchInput("strategy");
    }

    @Then("Verify strategy name keep in select campaign strategy after {string}")
    public void strategyNameKeepinStrategyEditor(String action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] strategynameArray = testData.getString("strategy", "name").split("\\|");
        if (action.equals("saved")) {
            Assert.assertTrue("campaign strategy not keep in campaign editor", CampaignManagerPageObj.verifyStrategyNamekeepinCampaignEditorAfterSave(strategynameArray[0]));
        } else if (action.equals("updated")) {
            Assert.assertTrue("campaign strategy not keep in campaign editor", CampaignManagerPageObj.verifyStrategyNamekeepinCampaignEditorAfterSave(strategynameArray[1]));
        }
    }

    @Then("Verify {string} sort icons on {string} column")
    public void verifySortIconsOnColumn(String sortIcon, String listColumnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] columns = listColumnName.split(",");
        for (String column : columns) {
            log.info("Verify the supporting for sorting on column: " + column);
            Assert.assertTrue("FAILURE to verify the sorting order icons", commonFunction.verifySortOrderIconsOnColumn(sortIcon, column));
        }
    }

    @Then("Verify the sorting as {string} on {string} column work properly")
    public void verifyAllDataOnSortedColumn(String sortOrder, String sortColumn) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Searching campaign...");
        commonFunction.sendKeysToTextBox("searchTable", testData.getData("campaign", "name"));
        commonFunction.waitForPageLoadComplete(10, 1);
        Hashtable[] defaultSort = commonFunction.getListColumnDataOnPage("All", "", "", "", "");
        Hashtable[] noneDefaultSort = commonFunction.getListColumnDataOnPage("All", "", "", sortColumn, sortOrder);
        ArrayList<String> cmpStrListSortedUI = new ArrayList<>();
        ArrayList<String> listCampaignStrategies = new ArrayList<>();
        log.info("Get list of campaign strategy with default sort");
        for (Hashtable record : defaultSort) {
            Set keys = record.keySet();
            for (Object key : keys) {
                String str = (String) key;
                if (str.equals(sortColumn)) {
                    listCampaignStrategies.add(record.get(str).toString());
                }
            }
        }
        if (sortOrder.contains("Asc")) {
            Collections.sort(listCampaignStrategies);
        } else {
            listCampaignStrategies.sort(Comparator.reverseOrder());
        }
        log.info("Get list of campaign strategy with sort order as " + sortOrder);
        for (Hashtable record : noneDefaultSort) {
            Set keys = record.keySet();
            for (Object key : keys) {
                String str = (String) key;
                if (str.equals(sortColumn)) {
                    // System.out.println("Key: " + str + " & Value: " + record.get(str));
                    cmpStrListSortedUI.add(record.get(str).toString());
                }
            }
        }
        log.info("List campaign strategies after applying the sort function: " + listCampaignStrategies);
        log.info("List campaign strategies get on UI with sort order as " + sortOrder + ": " + cmpStrListSortedUI);
        log.info("Verify campaign strategy name sort correctly");
        Assert.assertEquals("Sort function do not work properly", listCampaignStrategies, cmpStrListSortedUI);
        log.info("Verify all data will be shown correctly for each campaign");
        CampaignStrategyPageObj.compareAllDataOfRecordsWithSort(defaultSort, noneDefaultSort);
    }

    @And("Delete {int} campaigns")
    public void deleteMultiCampaigns(int campaigns, Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        HashMap<String, String> testData = utilityFun.readJsonToHashMap(data.get("DataFile"), data.get("Data"));
        String name = testData.get("Campaign Name");
        for (int i = 0; i < campaigns; i++) {
            testData.put("Campaign Name", (name + "_" + i));
            RestMethodsObj.deleteCampaign(testData.get("Campaign Name"), "");
            testData.put("Campaign Name", name);
        }
    }

    @Then("Clean up campaign and contact list and strategy")
    public void cleanUpCampaignAndContactListAndStrategy() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> campaign = (Map) testData.getAsMap("campaign");
        Map<String, String> contactlist = (Map) testData.getAsMap("contactlist");
        Map<String, String> strategy = (Map) testData.getAsMap("strategy");
        RestMethodsObj.deleteAllCampaign(campaign.get("name"));
        RestMethodsContactListObj.deleteAllContactList(contactlist.get("name"));
        RestMethodsObj.deleteAllStrategies(strategy.get("name"));
    }

    @Then("Clean up campaign and contact list and strategy data for testing")
    @Then("Clean up campaign and contact list and strategy data added for testing")
    @Then("Clean up campaign and contact list and strategy before run")
    public void cleanUpCampaignAndContactListAndStrategyBefore() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.cleanUpCampaignAndContactListAndStrategyBefore(testData);
    }

    @Then("Verify the Finish At \\(Date : Time) changes when the time zone changes accordingly")
    public void verifyTheFinishAtDateTimeChangesWhenTheTimeZoneChangesAccordingly() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.verifyTheFinishAtDateTimeChangesWhenTheTimeZoneChangesAccordingly();
    }

    @And("Quick search campaign")
    public void quickSearchCampaign(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Quick search Campaign <" + data.get("CampaignNames") + ">");
        commonFunction.sendKeysToTextBox("searchTable", data.get("CampaignNames"));
    }

    @Then("Verify searched campaign length")
    public void verifySearchedCampaignLength(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int iSearchedTextLength = commonFunction.getAttributeField("searchTable", "value").length();
        log.info("Expected Searched Text String<" + data.get("SearchStringLength") + ">");
        log.info("Actual Searched Text String length <" + iSearchedTextLength + ">");
        Assert.assertEquals(data.get("SearchStringLength").toString(), Integer.toString(iSearchedTextLength));
    }

    @And("Quick search strategy")
    public void quickSearchStrategy(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Quick search strategy <" + data.get("StrategyName") + ">");
        CampaignManagerPageObj.searchStrategyOnCampaignManagerPage(data.get("StrategyName"));
    }

    @Then("Verify searched strategy length and value")
    public void verifySearchedStrategyLength(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String valueInSearchBox = CampaignManagerPageObj.getValueOfSearchedStrategyOnCampaignManagerPage();
        log.info("Actual search string <" + valueInSearchBox + ">");
        log.info("Expected search string <" + data.get("StrategyName").substring(0, 40) + ">");
        Assert.assertEquals("Value do not match", valueInSearchBox, data.get("StrategyName").substring(0, 40));
        if (data.containsKey("SearchStringLength")) {
            Assert.assertEquals(data.get("SearchStringLength"), Integer.toString(valueInSearchBox.length()));
        }
    }

    @And("Quick search contact list")
    public void quickSearchContactList(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Quick search contact list <" + data.get("ContactListName") + ">");
        CampaignManagerPageObj.searchContactListOnCampaignManagerPage(data.get("ContactListName"));
    }

    @Then("Verify searched contact list length and value")
    public void verifySearchedContactListLength(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String valueInSearchBox = CampaignManagerPageObj.getValueOfSearchedContactListOnCampaignManagerPage();
        log.info("Actual search string <" + valueInSearchBox + ">");
        log.info("Expected search string<" + data.get("ContactListName").substring(0, 40) + ">");
        Assert.assertEquals("Value do not match", valueInSearchBox, data.get("ContactListName").substring(0, 40));
        if (data.containsKey("SearchStringLength")) {
            Assert.assertEquals(data.get("SearchStringLength"), Integer.toString(valueInSearchBox.length()));
        }
    }

    @Given("Create {int} campaign using API")
    public void createCampaignForTesting(int numberCampaign) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("-------------------------------------Start delete old data ----------------------------------");
        RestMethodsObj.deleteAllCampaign(testData.getString("campaign", "name"));
        RestMethodsObj.deleteAllStrategies(testData.getString("campaign", "strategy"));
        RestMethodsContactListObj.deleteAllContactList(testData.getString("campaign", "contactList"));
        log.info("-------------------------------------Start add new data ----------------------------------");
        RestMethodsContactListObj.createContactList(testData.getData("contactlist"));
        RestMethodsObj.createStrategy(testData.getData("strategy"));
        if (testData.getString("campaign", "importContact").equalsIgnoreCase("yes")) {
            Map<String, String> data = testData.getData("datasource");
            if (data.get("ftpIPHostName").equalsIgnoreCase("usecommon")) {
                data.put("ftpIPHostName", EnvSetup.SFTP_HOSTNAME);
                data.put("ftpUserName", EnvSetup.SFTP_USERNAME);
                data.put("ftpPassword", EnvSetup.SFTP_PASSWORD);
                data.put("ftpRemoteFilePath", EnvSetup.SFTP_REMOTE_FILEPATH + "/" + data.get("ftpRemoteFilePath"));
            }
            String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + testData.getString("datasource", "sourceFile");
            log.info(sourceFile);
            utilityFun.copyFile(data.get("ftpIPHostName"), data.get("ftpUserName"), data.get("ftpPassword"), sourceFile, data.get("ftpRemoteFilePath"));
            RestMethodsContactListObj.deleteDataSource(data.get("name"), "");
            if (testData.getData("contactAttribute") != null) {
                log.info("-------------------------------------Create Required Attributes ----------------------------------");
                List<Map<String, String>> attlist = testData.getData("contactAttribute");
                String contactlistName = testData.getData("contactlist", "name");
                for (Map<String, String> attribute : attlist) {
                    RestMethodsContactListObj.createAttribute(attribute, contactlistName);
                }
            }
            RestMethodsContactListObj.createDataSource(data);
            if (testData.getData("contactAttribute") != null) {
                log.info("-------------------------------------Create Required Attributes ----------------------------------");
                List<Map<String, String>> attlist = testData.getData("contactAttribute");
                String contactlistName = testData.getData("contactlist", "name");
                for (Map<String, String> attribute : attlist) {
                    RestMethodsContactListObj.createAttribute(attribute, contactlistName);
                }
            }
            RestMethodsContactListObj.runDataSourceByAPI(testData.getStrict("contactlist", "name"), data.get("name"));
            utilityFun.wait(2);
            commonFunction.monitorImportContactListImportCompleted(testData.getStrict("campaign", "contactList"), "", 10);
        }
        if (numberCampaign <= 1) {
            RestMethodsObj.createCampaign(testData.getData("campaign"));
        } else {
            Map<String, String> campaign = testData.getData("campaign");
            String defCamp = testData.getStrict("campaign", "name");
            for (int i = 1; i <= numberCampaign; i++) {
                campaign.put("name", (defCamp + "_" + i));
                RestMethodsObj.createCampaign(campaign);
            }
            campaign.put("name", defCamp);
        }
    }

    @Then("Start campaign option display on option menu dropdown")
    public void verifyStartCampaignOption() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sendKeysToTextBox("searchTable", testData.getStrict("campaign", "name"));
        commonFunction.waitForPageLoadComplete(30, 2);
        commonFunction.clickTakeMoreActionElipse(testData.getStrict("campaign", "name"));
        Assert.assertTrue("Start campaign is not display on option menu dropdown", commonFunction.isDisplayed("action_startCampaign", 10));
    }

    @Then("{string} column displayed on header of campaign landing page")
    public void columnDisplayedOnHeaderOfCampaignLandingPage(String columnName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyHeaderLabelDisplay(columnName);
    }

    @When("Start campaign with start campaign option")
    public void startCampaignWithStartCampaignOption() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getStrict("campaign", "name");
        commonFunction.sendKeysToTextBox("searchTable", campaignName);
        commonFunction.waitForPageLoadComplete(30, 1);
        commonFunction.clickAction(campaignName, "Start");
        commonFunction.waitForPageLoadComplete(30);
        String status = RestMethodsObj.getAttributeValueOfJobCampaign(campaignName, "status");
        Assert.assertNotNull("The user can not start campaign", status);
        boolean state = List.of("running", "completed", "starting").contains(status.toLowerCase());
        Assert.assertTrue("Campaign start failed", state);
    }

    @Given("Delete contact list that associated with campaign")
    public void deleteContactListThatAssociatedWithCampaign() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.deleteContactList(testData.getStrict("campaign", "contactList"), "");
    }

    @And("Delete campaign strategy that associated with campaign")
    public void deleteCampaignStrategyThatAssociatedWithCampaign() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsObj.deleteAllStrategies(testData.getStrict("campaign", "strategy"));
    }

    @Then("Status campaign display {string} in {string} column campaign landing page")
    public void verifyStatusCampaignDisplayCorrectly(String status, String columnName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Status campaign displayed incorrectly", commonFunction.getSingleDataFromTable(testData.getStrict("campaign",
                "name"), columnName).equalsIgnoreCase(status));
    }

    @Then("Start campaign option disable when campaign running")
    public void startCampaignOptionDisableWhenCampaignRunning() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.waitForPageLoadComplete(30, 2);
        commonFunction.clickTakeMoreActionElipse(testData.getStrict("campaign", "name"));
        Assert.assertTrue("Start campaign is not display on option menu dropdown", commonFunction.getAttributeField("action_startCampaign",
                "aria-disabled").equalsIgnoreCase("true"));
    }

    @Then("Start campaign option enable again when campaign stopped")
    public void startCampaignOptionEnableAgainWhenCampaignStopped() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Wait campaign stopped");
        utilityFun.wait(10);
        Assert.assertTrue("Start campaign is not display on option menu dropdown", commonFunction.isEnable("action_startCampaign"));
    }

    @Then("Toast message {string} displayed correctly when run campaign")
    public void verifyToastMessageDisplayWhenRunCampaignSuccessful(String message) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> toastMessage = commonFunction.getListToastMessage();
        Assert.assertTrue("More than 1 message when run campaign", toastMessage.size() == 1);
        if (message.contains("started successfully")) {
            message = message.replace("name", testData.getStrict("campaign", "name"));
        }
        log.info("Message is:" + message);
        Assert.assertTrue("Message in not display correctly", toastMessage.get(0).equals(message));
    }

    @Then("Failure toast message {string} displayed correctly when run campaign")
    public void failureToastMessageDisplayedCorrectlyWhenRunCampaign(String message) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.verifyToastMessageDisplayWhenRunCampaignSuccessful(message);
    }

    @And("Campaign is not running")
    public void campaignIsNotRunningOnMenuDropdown() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Start campaign is not display on option menu dropdown", commonFunction.findElementByDataID("action_startCampaign").isEnabled());
    }

    @And("Clean up all data for testing")
    public void cleanUpAllDataForTesting() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsObj.deleteAllCampaign(testData.getString("campaign", "name"));
        RestMethodsObj.deleteAllStrategies(testData.getString("campaign", "strategy"));
        RestMethodsContactListObj.deleteContactList(testData.getString("campaign", "contactList"), "");
        if (testData.getData("datasource") != null) {
            RestMethodsContactListObj.deleteDataSource(testData.getStrict("datasource", "name"), "");
        }
        RestMethodsObj.deleteAllStrategies(testData.getString("strategy", "name"));
        RestMethodsContactListObj.deleteContactList(testData.getString("contactlist", "name"), "");
    }

    @Given("The Campaign Manager URL is hit to view the existing campaigns")
    public void userClicksOnTheCampaignManagerURL() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.navigateToOutboundPage("campaigns-manager");
    }

    @Then("User Sorting and the sort icon should be displayed on the Last Executed column")
    public void sortingAndTheSortIconDisplayedOnTheLastExecutedColumn() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.basicSearchOnTable(testData.getData("campaign", "name"));
        commonFunction.clickHeaderColumn("Last Executed");
        Assert.assertTrue(commonFunction.isDisplayedByXpath(locator.getLocator("sortIconArrowUp"), 30));
        commonFunction.clickHeaderColumn("Last Executed");
        Assert.assertTrue(commonFunction.isDisplayedByXpath(locator.getLocator("sortIconArrowDown"), 30));
    }

    @Given("User is on the Campaign Manager landing Page")
    public void heIsOnTheCampaignManagerLandingPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("verify the 'Campaign Manager' header page is displayed");
        String headerPage = commonFunction.findElementByDataID("page").getText();
        Assert.assertEquals("the 'Campaign Manager' header page is not displayed", "Campaign Manager", headerPage);
    }

    @Given("User should be able to sorting on the Last Executed column")
    public void heShouldAbleToSortingOnTheLastExecutedColumn() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifySortDateTimeColumn("Last Executed");
    }

    @Then("Verify list Operators on dropdown option displayed correctly")
    public void verifyListOnDropdownOptionDisplayedCorrectly(Map<String, String> mapData) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String operators = mapData.get("Operator");
        String attr = mapData.get("Attribute");
        commonFunction.verifyOperatorDisplayCorrectWithAttribute(attr, operators);
    }

    @Then("Advance search on campaign manager page and verify advance search result")
    public void advanceSearchForByWithOnContactListPage(List<Map<String, String>> mapList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (Map<String, String> data : mapList) {
            commonFunction.advanceSearchFilter(data.get("Column Name"), data.get("Operator"), data.get("Input type"), data.get("Value"));
            commonFunction.verifyAdvanceSearchForDateTimeCol();
        }
    }

    @When("Run a campaigns using UI")
    public void runTheListCampaignsUsingUI() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.search(testData.getString("campaign", "name"));
        commonFunction.clickAction(testData.getString("campaign", "name"), "Start");
    }

    @Then("Verify the SMS status")
    public void verifyTheSMSStatus(List<List<String>> smsCodes) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> smsCodeList = smsCodes.get(0);
        Map<String, List<String>> attempts = RestMethodsObj.getAttempts(testData.getString("campaign", "name"), "address", "completionCode");
        List<String> addresses = attempts.get("address");
        List<String> completionCodes = attempts.get("completionCode");
        for (int i = 0; i < addresses.size(); i++) {
            commonFunction.verifySMSCompletionCode(addresses.get(i), completionCodes.get(i));
        }
    }

    @Then("Result ratio run SMS campaign should be display correctly on attempt output")
    public void verifyThatResultRatioRunSMSCampaignShouldBeDisplayCorrectlyOnAttemptOutput() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.monitorCampaignTillCompleted(testData.getString("campaign", "name"), 10);
        Map<String, List<String>> attempts = RestMethodsObj.getAttempts(testData.getString("campaign", "name"), "address", "completionCode");
        List<String> addresses = attempts.get("address");
        List<String> completionCodes = attempts.get("completionCode");
        int total = completionCodes.size();
        long sent = completionCodes.stream().filter(cc -> cc.equalsIgnoreCase("SMS_Sent")).count();
        long reject = completionCodes.stream().filter(cc -> cc.equalsIgnoreCase("SMS_Rejected")).count();
        long fail = completionCodes.stream().filter(cc -> cc.equalsIgnoreCase("SMS_Failed")).count();
        log.info("Total: " + total);
        log.info("SMS_SENT: " + sent);
        log.info("SMS_REJECTED: " + reject);
        log.info("SMS_FAILED: " + fail);
        Assert.assertEquals(total, 100);
        Assert.assertEquals(Double.compare((double) sent / total, 0.4), 0);
        Assert.assertEquals(Double.compare((double) reject / total, 0.3), 0);
        Assert.assertEquals(Double.compare((double) fail / total, 0.3), 0);
    }

    @Then("Status in {string} column change instantly when run campaign successful")
    public void verifyStatusCampaignDisplay(String nameColumn) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.tryClick("refreshTable", 10);
        commonFunction.waitForPageLoadComplete(10, 1);
        Assert.assertFalse("Status campaign is not change when run campaign", commonFunction.getSingleDataFromTable(
                testData.getStrict("campaign", "name"), nameColumn).contains("In Progress"));
    }

    @Then("Start campaign option enable when campaign running completed")
    public void startCampaignOptionEnableWhenCampaignRunningCompleted() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String str = commonFunction.getSingleDataFromTable(testData.getStrict("campaign", "name"), "Last Executed");
        int count = 0;
        while (str.equals("In Progress") && count < 30) {
            commonFunction.sleepInSec(2);
            commonFunction.tryClick("refreshTable", 1);
            str = commonFunction.getSingleDataFromTable(testData.getStrict("campaign", "name"), "Last Executed");
            count++;
        }
        commonFunction.sendKeysToTextBox("searchTable", testData.getStrict("campaign", "name"));
        commonFunction.waitForPageLoadComplete(30, 2);
        commonFunction.clickTakeMoreActionElipse(testData.getStrict("campaign", "name"));
        Assert.assertTrue("Start campaign is not display on option menu dropdown", commonFunction.getAttributeField("action_startCampaign",
                "aria-disabled").equalsIgnoreCase("false"));
    }

    @Then("Verify expected campaign on campaign List page")
    public void verifyExpectedCampaignOnCampaignListPage(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyAdvanceSearchResult(data.get("CampaignNames"), true);
    }

    @And("Pagination work correctly on campaign landing page")
    public void paginationWorkCorrectlyOnCampaignLandingPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyPageNavigation("10");
    }

    @And("Failure toast message is not display when user try entering the characters {string} in text box {string} search")
    public void tryEnteringTheCharactersInTextBoxSearch(String character, String typeInput) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyFailedToastMessageIsNotDisplayWhenInputSpecialCharacter(character, typeInput);
    }

    @When("Enter the invalid character using the Num Lock external keyboard for Time Based Finish Criteria field")
    public void enterTheInvalidCharacterUsingTheNumLockExternalKeyboardForTimeBasedFinishCriteriaField() {
        commonFunction.enterAndVerifyField("Finish After (Hours)", Keys.SUBTRACT, text -> text.isEmpty());
        commonFunction.enterAndVerifyField("Finish After (Hours)", "-", text -> text.isEmpty());
        commonFunction.enterAndVerifyField("Finish After (Minutes)", Keys.SUBTRACT, text -> text.isEmpty());
        commonFunction.enterAndVerifyField("Finish After (Minutes)", "-", text -> text.isEmpty());
    }

    @Then("Campaign works correctly follow {string} Pacing Parameters defined in its strategy")
    public void verifyPacingConfiguredWorkCorrectly(String timeUnit) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int expectedTime = CampaignManagerPageObj.coverPacingToTime(timeUnit, testData.getString("strategy", "smsPace"),
                testData.getString("contactlist", "totalContact"));
        commonFunction.monitorCampaignTillCompleted(testData.getData("campaign", "name"), 100);
        LocalDateTime startCampaignTime = RestMethodsObj.getTimeForRunCampaign(testData.getData("campaign", "name"), "startedOn");
        LocalDateTime endCampaignTimeExpected = startCampaignTime.plusSeconds(expectedTime);
        LocalDateTime endCampaignTimeActual = RestMethodsObj.getTimeForRunCampaign(testData.getData("campaign", "name"), "finishedOn");
        Duration duration = Duration.between(endCampaignTimeExpected, endCampaignTimeActual);
        Assert.assertTrue("Campaign works incorrect follow " + timeUnit + " Pacing Parameters defined in its strategy", Math.abs(duration.toSeconds()) < 10);
    }

    @Then("Campaign sent SMS to default address list in the contact")
    public void verifyCampaignSendedSMSToDefaultAddress() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.monitorCampaignTillCompleted(testData.getData("campaign", "name"), 60);
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + testData.getData("datasource", "sourceFile");
        List<String> phoneDefault = commonFunction.getValueColumnOfFile(sourceFile, "phoneNumber");
        Collections.sort(phoneDefault);
        Map<String, List<String>> attempts = RestMethodsObj.getAttempts(testData.getString("campaign", "name"), "address");
        List<String> address = attempts.get("address");
        Collections.sort(address);
        Assert.assertTrue("campaign is not send SMS to default address list in the contact", phoneDefault.equals(address));
    }

    @Then("Campaign is stated successfully and there are {int} SMS that is sent")
    public void verifyCampaignSendSMSSuccessful(int numberRecordExpected) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.monitorCampaignTill("COMPLETED", testData.getData("campaign", "name"), 250, 10);
        List<JsonObject> listAttempt = RestMethodsObj.getAttempts(testData.getData("campaign", "name"));
        log.info("Size of record sent SMS: " + listAttempt.size());
        Assert.assertEquals("Campaign not send SMS to full record", listAttempt.size(), numberRecordExpected);
    }

    @Then("The event is generated on attempts node")
    public void theIsGeneratedOnAttemptsNode(List<Map<String, String>> mapList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (Map<String, String> data : mapList) {
            String event = data.get("Event");
            List<String> duplicateList = null;
            Map<String, List<String>> attempts = null;
            List<String> attemptList = null;
            int expRecords = Integer.parseInt(testData.getData("totalRecord"));
            try {
                attempts = RestMethodsObj.getAttempts(context.testData.getStrict("campaign", "name"), event);
                attemptList = attempts.get(event);
                duplicateList = attemptList.stream().distinct().collect(Collectors.toList());
            } catch (Exception e) {
                log.error("get API not successfully!!!!");
                Assert.assertTrue("May %s event isn't generated on attempts node".formatted(event), false);
            }
            SoftAssert softAssert = new SoftAssert();
            switch (event) {
                case "attemptId", "contactId":
                    softAssert.assertEquals(duplicateList.size(), expRecords, "Number of " + event + " not match with total records");
                    break;
                case "jobId", "contactList", "campaign":
                    softAssert.assertEquals(duplicateList.size(), 1, "Number of " + event + " not equal 1");
                    if (event.equals("contactList")) {
                        log.info("contactList Actual: " + duplicateList.get(0).replaceAll("\"", ""));
                        log.info("contactList Expected: " + testData.getString("campaign", "contactList"));
                        softAssert.assertEquals(duplicateList.get(0).replaceAll("\"", ""), testData.getString("campaign", "contactList"), "Contact list name not match as expected");
                    } else if (event.equals("campaign")) {
                        log.info("campaign Actual: " + duplicateList.get(0).replaceAll("\"", ""));
                        log.info("campaign Expected: " + testData.getString("campaign", "name"));
                        softAssert.assertEquals(duplicateList.get(0).replaceAll("\"", ""), testData.getString("campaign", "name"), event + " value not match as expected");
                    }
                    break;
                case "channelType":
                    softAssert.assertEquals(duplicateList.size(), 1, "Exist other change types, not is SMS");
                    softAssert.assertEquals(duplicateList.get(0).replaceAll("\"", ""), "SMS", "Channel Type event be not SMS");
                    break;
                case "lastDispositionTime", "attemptTime", "completionCode", "systemCompletionCode", "address":
                    softAssert.assertEquals(attemptList.size(), expRecords, "Number of " + event + " not match with total records");
                    break;
            }
            softAssert.assertAll();
        }
    }

    @Given("There are at least {int} campaign was added and run completed")
    public void ThereAreSomeCampaignWasAddedAndRunCompleted(int numberCampaign) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getStrict("campaign", "name");
        this.createCampaignForTesting(numberCampaign);
        commonFunction.basicSearchOnTable(campaignName);
        List<String> campaignNames = commonFunction.getListColumnData("Name");
        double intervalTime = 1;
        if (testData.containsKey("runInterval")) {
            intervalTime = Double.parseDouble(testData.get("runInterval"));
        }
        if (numberCampaign == 1) {
            RestMethodsObj.startCampaignByAPI(campaignName, "");
            commonFunction.monitorCampaignTillCompleted(campaignName, 60);
        } else {
            for (int i = 1; i <= campaignNames.size(); i++) {
                RestMethodsObj.startCampaignByAPI(campaignName + "_" + i, "");
                RestMethodsObj.stopCampaignByAPI(campaignName + "_" + i, "");
                commonFunction.monitorCampaignTillCompleted(campaignName + "_" + i, 60);
                commonFunction.sleepInSec(intervalTime);
            }
        }
        commonFunction.clickRefreshButton();
    }

    @And("Monitor campaign status running completed")
    public void monitorCampaignStatusRunningCompleted() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.monitorCampaignTillCompleted(testData.getData("campaign", "name"), 60);
    }

    @When("Run a campaigns using API")
    public void runACampaignsUsingAPI() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getData("campaign", "name");
        RestMethodsObj.startCampaignByAPI(campaignName, "");
        commonFunction.sleepInSec(1);
        String status = RestMethodsObj.getAttributeValueOfJobCampaign(campaignName, "status");
        boolean isRunningOrComplete = status.equalsIgnoreCase("running") || status.equalsIgnoreCase("completed");
        Assert.assertTrue("Campaign can't start", isRunningOrComplete);
    }


    @Then("verify save button is disable and error message when input special character to {string}")
    public void verifyInvalidValueSenderDisplayName(String pageName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (pageName) {
            case "Sender's Display Name":
                Assert.assertTrue("Error message not match", commonFunction.verifyInputSpecialCharacters(pageName,
                        testData.getString("campaign", "senderDisplayName"), testData.getString("campaign", "errorMessage")));
                break;
            case "Sender's Address":
                Assert.assertTrue("Error message not match", commonFunction.verifyInputSpecialCharacters(pageName,
                        testData.getString("campaign", "senderAddress"), testData.getString("campaign", "errorMessage")));
                break;
        }

    }

    @Then("Input {string} to {string} field")
    public void inputvalidValuetoField(String value, String field) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.inputValueToField(field, value);
    }

    @And("Input value to ANI Configuration")
    public void inputvalidValuetoANIField() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.inputValueToField("Sender's Display Name", testData.getString("campaign", "senderDisplayName"));
        commonFunction.inputValueToField("Sender's Address", testData.getString("campaign", "senderAddress"));
    }

    @And("Input update value to ANI Configuration")
    public void inputupdatevalidValuetoANIField() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.inputValueToField("Sender's Display Name", testData.getString("campaign", "senderDisplayNameUpdate"));
        commonFunction.inputValueToField("Sender's Address", testData.getString("campaign", "senderAddressUpdate"));
    }

    @Then("verify the max length of {string} is {int}")
    public void verifyTheMaxLengthOfField(String fieldName, int maxLength) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("max lenght of " + fieldName + " not expected", CampaignStrategyPageObj.maxLengthOfField(fieldName, maxLength));
    }

    @And("Verify {string} - ANI information keep in campaign editor after {string}")
    public void verifyANIinforKeepInCmpEditor(String field, String Action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (field) {
            case "Sender's Display Name":
                String senderNameAct = commonFunction.getAttributeField("senderDisplayName", "value");
                log.info("Sender's Display Name Actual on campaign editor: " + senderNameAct);
                if (Action.equals("updated")) {
                    log.info("Sender's Display Name Expected:" + testData.getString("campaign", "senderDisplayNameUpdate"));
                    Assert.assertEquals("Sender's Display Name not expected", testData.getString("campaign", "senderDisplayNameUpdate"), senderNameAct);
                } else {
                    log.info("Sender's Display Name Expected:" + testData.getString("campaign", "senderDisplayName"));
                    Assert.assertEquals("Sender's Display Name not expected", testData.getString("campaign", "senderDisplayName"), senderNameAct);
                }
                break;
            case "Sender's Address":
                String senderAddrAct = commonFunction.getAttributeField("senderAddress", "value");
                log.info("Sender's Address Actual on campaign editor: " + senderAddrAct);
                if (Action.equals("updated")) {
                    log.info("Sender's Address Expected:" + testData.getString("campaign", "senderAddressUpdate"));
                    Assert.assertEquals("Sender's Address not expected", testData.getString("campaign", "senderAddressUpdate"), senderAddrAct);
                } else {
                    log.info("Sender's Address Expected:" + testData.getString("campaign", "senderAddress"));
                    Assert.assertEquals("Sender's Address not expected", testData.getString("campaign", "senderAddress"), senderAddrAct);
                }
                break;
            default:
                log.info("input parameter invalid - Using field name on the UI: Sender's Display Name | Sender's Address");
                Assert.fail();
        }
    }


    @Then("Verify that a error message {string} will be display")
    public void verifyThatAErrorMessageWillBeDisplay(String expectToastMessage) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String actualToastMessage = commonFunction.getToastMessage();
        log.info("expectToastMessage: " + expectToastMessage);
        log.info("actualToastMessage: " + actualToastMessage);
        Assert.assertEquals(expectToastMessage.trim(), actualToastMessage.trim());
    }

    @And("Verify an error toast message will {string} shown")
    public void isErrorToastMessageDisplayed(String displayed) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (displayed.equalsIgnoreCase("not")) {
            List<WebElement> existFlag = commonFunction.presentElements(locator.getLocator("toastMessage"), 3);
            Assert.assertEquals("FAILURE!!!Toast message is displayed", existFlag.size(), 0);
        }
    }

    @And("Verify {string} combo-box size will be shown as {string}")
    public void verifyComboBoxSize(String comboBoxName, String size) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (comboBoxName) {
            case "Contact List" -> {
                List<WebElement> result = commonFunction.presentElements(locator.getLocator("contactList_ComboBox"), 3);
                Assert.assertEquals("FAILURE!!!Searching function do not work properly", result.size(), Integer.parseInt(size));
            }
            case "Strategy" -> {
                List<WebElement> result = commonFunction.presentElements(locator.getLocator("strategy_ComboBox"), 3);
                Assert.assertEquals("FAILURE!!!Searching function do not work properly", result.size(), Integer.parseInt(size));
            }
        }
    }

    @Then("Verify the toast message is displayed correctly")
    public void verifyTheToastMessageIsDisplayedCorrectly() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String toastMessageAct = commonFunction.getToastMessage();
        String actualMessage = "Stop request accepted for campaign " + testData.getString("campaign", "name") + ".";
        Assert.assertEquals(actualMessage, toastMessageAct);
    }

    @Then("Verify campaign job status should display {string} until it finishes the outstanding SMS")
    public void verifyCampaignJobStatusShouldDisplayUntilItFinishesTheOutstandingSMS(String expectedStatus) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String status = RestMethodsObj.getAttributeValueOfJobCampaign(testData.getData("campaign", "name"), "status");
        Assert.assertTrue("Current status is " + status + " , expected status is " + expectedStatus.toUpperCase(),
                status.contentEquals(expectedStatus.toUpperCase()));
    }

    @Then("Verify no new SMS should be placed once campaign enters to \"Stopping\" state")
    public void verifyNoNewSMSShouldBePlacedOnceCampaignEntersToState() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.monitorCampaignTillCompleted(testData.getData("campaign", "name"), 60);
        int curAttemptNum = RestMethodsObj.getAttempts(testData.getString("campaign", "name")).size();
        commonFunction.sleepInSec(5);
        int newAttemptNum = RestMethodsObj.getAttempts(testData.getString("campaign", "name")).size();
        Assert.assertEquals("Attempt still generate", curAttemptNum, newAttemptNum);
    }

    @When("Stop campaign with stop campaign option")
    public void selectStopCampaignOption() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campName = testData.getData("campaign", "name");
        commonFunction.applyAdvanceSearch("Name", "=", campName);
        commonFunction.clickAction(campName, "Stop");
        commonFunction.waitForPageLoadComplete(30, 1);
    }


    @Then("In-progress status continue to be displayed in Last Executed column till campaign is completed")
    public void inProgressStatusContinueToBeDisplayedInLastExecutedColumnTillCampaignIsCompleted() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campName = testData.getData("campaign", "name");
        commonFunction.applyAdvanceSearch("Name", "=", campName);
        String campStatusOnLastExecutedCol = commonFunction.getSingleDataFromTable(campName, "Last Executed");
        Assert.assertEquals("Status of campaign on Last executed column is not In Progress", campStatusOnLastExecutedCol, "In Progress");
        commonFunction.monitorCampaignTillCompleted(campName, 30);
        commonFunction.clickButton("refreshTable");
        commonFunction.waitForPageLoadComplete(10, 1);
        campStatusOnLastExecutedCol = commonFunction.getSingleDataFromTable(campName, "Last Executed");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");
        try {
            LocalDateTime timeValue = LocalDateTime.parse(campStatusOnLastExecutedCol, format);
        } catch (Exception e) {
            Assert.fail("Date time value format not match as expected");
        }
    }


    @And("Campaign status is {string}")
    public void campaignStatusIsRunning(String expectedStatus) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String status = RestMethodsObj.getAttributeValueOfJobCampaign(testData.getData("campaign", "name"), "status");
        Assert.assertTrue("Current status is " + status + " , expected status is " + expectedStatus.toUpperCase(), status.contentEquals(expectedStatus.toUpperCase()));
    }


    @Then("Campaign status get transition to {string} till campaign is completed")
    @And("Campaign be kept status {string} till campaign is completed")
    public void campaignStatusGetTransitionToTillCampaignIsCompleted(String expectedStatus) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getString("campaign", "name");
        Assert.assertNotNull(commonFunction.presentElement(By.xpath(locator.locatorFormat("statusCampaignFormat", campaignName, expectedStatus)), 60));
        commonFunction.monitorCampaignTillCompleted(campaignName, 60);
    }

    @When("User click option stop campaign and click {string} button")
    public void StopCampaign(String confirm) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.tryClick("clearSearch", 10);
        commonFunction.search(testData.getStrict("campaign", "name"));
        commonFunction.clickAction(testData.getStrict("campaign", "name"), "Stop");
        commonFunction.waitForPageLoadComplete(60, 0.5);
        if (confirm.equalsIgnoreCase("Stop")) {
            commonFunction.tryClick("done-action", 10);
        } else {
            commonFunction.tryClick("cancel-action", 10);
        }
        commonFunction.waitForPageLoadComplete(10, 2);
    }

    @When("User stop campaign and campaign stopped")
    public void userStopCampaignAndCampaignStopped() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.StopCampaign("Stop");
        commonFunction.monitorCampaignTillCompleted(testData.getData("campaign", "name"), 60);
    }

    @Then("Campaign job state should change from “Stopping” state to “Completed” state")
    public void verifyStatusCampagin() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignManagerPageObj.verifyStatusCampaignDisplayCorrectly(testData.getData("campaign", "name"), "STOPPING");
        commonFunction.monitorCampaignTillCompleted(testData.getData("campaign", "name"), 100);
        CampaignManagerPageObj.verifyStatusCampaignDisplayCorrectly(testData.getData("campaign", "name"), "COMPLETED");
    }

    @Then("User can start campaign again")
    public void userCanStartCampaignAgain() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.verifyStatusCampagin();
        RestMethodsObj.startCampaignByAPI(testData.getData("campaign", "name"), "");
        commonFunction.tryClick("refreshTable", 10);
        commonFunction.waitForPageLoadComplete(10, 2);
        String statusCampaignUI = commonFunction.getSingleDataFromTable(testData.getData("campaign", "name"), "Last Executed");
        log.info("Status display on Last Executed column is: " + statusCampaignUI);
        Assert.assertTrue("Campaign can not start again after stop", statusCampaignUI.equalsIgnoreCase("In Progress"));
    }

    @Then("User can edit and save campaign successful when campaign stopped")
    public void userCanEditAndSaveCampaignSuccessfulWhenCampaignStopped() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campName = testData.getData("campaign", "name");
        this.verifyStatusCampagin();
        String valueEdit = "Edit description with campaign stopped";
        commonFunction.applyAdvanceSearch("Name", "=", campName);
        commonFunction.clickAction(campName, "Edit");
        commonFunction.waitForPageLoadComplete(10, 1);
        Assert.assertTrue("Edit Campaign page is not display", commonFunction.isDisplayed("save", 10));
        CampaignManagerPageObj.editDesCamp(valueEdit);
        Assert.assertTrue("Campaign stopped can not edit", commonFunction.getToastMessage().contentEquals("Campaign updated successfully."));
        commonFunction.applyAdvanceSearch("Name", "=", campName);
        commonFunction.clickAction(campName, "Edit");
        commonFunction.waitForPageLoadComplete(10, 2);
        Assert.assertTrue("Description is not change when user edit", commonFunction.findElementByDataID("description").getAttribute("value").contentEquals(valueEdit));
    }

    @Then("Verify that the Stop option {string}")
    public void verifyThatTheStopOptionEnabled(String status) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getStrict("campaign", "name");
        commonFunction.clickRefreshButton();
        commonFunction.sendKeysToTextBox("searchTable", campaignName);
        commonFunction.waitForPageLoadComplete(30, 2);
        commonFunction.clickTakeMoreActionElipse(campaignName);
        if (status.equalsIgnoreCase("disabled")) {
            Assert.assertTrue("Stop option not be disabled while campaign is not run",
                    commonFunction.getAttributeField("action_stopCampaign", "aria-disabled").equalsIgnoreCase("true"));
        } else {
            Assert.assertTrue("Stop option not be enabled while campaign is running",
                    commonFunction.getAttributeField("action_stopCampaign", "aria-disabled").equalsIgnoreCase("false"));
        }
    }

    @And("The campaign is stopping and click stop button one more time")
    public void theCampaignIsStoppingAndClickStopButtonOneMoreTime() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            this.StopCampaign("Stop");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.StopCampaign("Stop");
    }

    @Then("Verify that a failure toast message is displayed")
    public void verifyThatFailureToastMessageIsDisplayed() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getStrict("campaign", "name");
        List<String> messages = commonFunction.getListToastMessage();
        log.info("Toast message: " + messages);
        Assert.assertTrue("Message not match",
                messages.stream().anyMatch(m -> m.contentEquals("Campaign %s is already in STOPPING state.".formatted(campaignName))));
    }

    @When("User selects one of the campaigns")
    public void userSelectsOneOfTheCampaigns() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sendKeysToTextBox("searchTable", testData.getStrict("campaign", "name"));
        commonFunction.waitForPageLoadComplete(30, 2);
        commonFunction.clickTakeMoreActionElipse(testData.getStrict("campaign", "name"));
    }

    @Then("Verify that the Stop option displayed in order Edit-Start-Stop")
    public void verifyThatTheStopOptionDisplayedInOrderEditStartStop() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.waitForPageLoadComplete(30);
        List<String> expectedList = new ArrayList<String>();
        expectedList.add("Edit");
        expectedList.add("Start");
        expectedList.add("Stop");
        CampaignManagerPageObj.verifyMoreOptionDisplayOrder(expectedList);
    }

    @Then("Verify that the Start option is disabled")
    public void verifyThatTheStartOptionIsDisabled() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getStrict("campaign", "name");
        commonFunction.clickRefreshButton();
        commonFunction.sendKeysToTextBox("searchTable", campaignName);
        commonFunction.waitForPageLoadComplete(30, 2);
        commonFunction.clickTakeMoreActionElipse(campaignName);
        commonFunction.waitForPageLoadComplete(30);
        boolean condition = commonFunction.getAttributeField("action_startCampaign", "aria-disabled").equalsIgnoreCase("true");
        Assert.assertTrue("Start campaign is not disabled on option menu dropdown when campaign stopping", condition);
    }

    @Given("Run {int} campaign using API")
    public void runCampaignUsingAPI(int numberCampaign) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> campaignNames = new ArrayList<>();
        List<String> campaignIDs = new ArrayList<>();
        RestMethodsObj.getAllFilterNameLike(RestMethods.Page.campaign, testData.getData("campaign", "name")).forEach(obj -> {
            campaignNames.add(obj.getAsJsonObject().get("name").getAsString());
            campaignIDs.add(obj.getAsJsonObject().get("id").getAsString());
        });
        log.info("List Campaign name: " + campaignNames);
        log.info("List Campaign ID: " + campaignIDs);
        Assert.assertTrue("The number of campaigns you want to run is larger than the current campaign on your Page",
                numberCampaign <= campaignIDs.size());
        double intervalTime =0.4;
        if (testData.containsKey("runInterval")) {
            intervalTime = Double.parseDouble(testData.get("runInterval"));
        }
        for (int i = 0; i < numberCampaign; i++) {
            RestMethodsObj.startCampaignByAPI(campaignNames.get(i), campaignIDs.get(i));
            commonFunction.sleepInSec(intervalTime);
            String status = RestMethodsObj.getAttributeValueOfJobCampaign(campaignNames.get(i), "status");
            log.info("Running a campaign " + campaignNames.get(i) + " return jobs status: " + status);
            Assert.assertNotNull("runCampaignUsingAPI: failed. Cannot get campaign jobs's %s campaign".formatted(campaignNames.get(i)), status);
            boolean state = List.of("running", "completed", "starting").contains(status.toLowerCase());
            Assert.assertTrue("runCampaignUsingAPI: failed. Campaign %s can't start".formatted(campaignNames.get(i)), state);
        }
    }

    @Given("Run {int} campaigns by campaign name using API")
    public void runCampaignsByNameUsingAPI(int numberCampaign) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> campaignNames = new ArrayList<>();
        RestMethodsObj.getAllFilterNameLike(RestMethods.Page.campaign, testData.getData("campaign", "name")).forEach(obj -> {
            campaignNames.add(obj.getAsJsonObject().get("name").getAsString());
        });
        Collections.sort(campaignNames);
        for (int i = 0; i < numberCampaign; i++) {
            log.info("Start Campaign name: " + campaignNames.get(i));
            RestMethodsObj.startCampaignByAPI(campaignNames.get(i), "");
        }
        commonFunction.sleepInSec(0.4);
        for (int i = 0; i < numberCampaign; i++) {
            String status = RestMethodsObj.getAttributeValueOfJobCampaign(campaignNames.get(i), "status");
            log.info("Running a campaign " + campaignNames.get(i) + " return jobs status: " + status);
            Assert.assertNotNull("Cannot get campaign jobs", status);
            boolean state = List.of("running", "completed", "starting", "stopping").contains(status.toLowerCase());
            Assert.assertTrue("Campaign can't start with name: " + campaignNames.get(i), state);
        }
    }

    @Given("Run {int} campaign more by campaign name using API")
    public void runCampaignsMoreByNameUsingAPI(int numberCampaign) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> campaignNames = new ArrayList<>();
        List<String> newActiveCampaign = new ArrayList<>();
        RestMethodsObj.getAllFilterNameLike(RestMethods.Page.campaign, testData.getData("campaign", "name")).forEach(obj -> {
            campaignNames.add(obj.getAsJsonObject().get("name").getAsString());
        });
        Collections.sort(campaignNames);
        for (int i = campaignNames.size() - 1; i >= campaignNames.size() - numberCampaign; i--) {
            log.info("Start Campaign name: " + campaignNames.get(i));
            RestMethodsObj.startCampaignByAPI(campaignNames.get(i), "");
            String status = RestMethodsObj.getAttributeValueOfJobCampaign(campaignNames.get(i), "status");
            boolean state = List.of("running", "completed", "starting", "stopping").contains(status.toLowerCase());
            Assert.assertTrue("Campaign can't start with name: " + campaignNames.get(i), state);
            newActiveCampaign.add(campaignNames.get(i));
        }
        testDataObject.put("newActiveCampaign", newActiveCampaign);
        log.info("new data: " + testDataObject.getString("newActiveCampaign"));
    }

    @Given("Create and run {int} campaign using API")
    public void create_and_run_campaign_using_api(int numCamp) {
        createCampaignForTesting(numCamp);
        runCampaignUsingAPI(numCamp);
    }

    @When("User click start campaign option")
    public void userClickStartCampaignOption() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getStrict("campaign", "name");
        commonFunction.search(campaignName);
        commonFunction.waitForPageLoadComplete(30, 1);
        commonFunction.clickAction(campaignName, "Start");
        commonFunction.waitForPageLoadComplete(30);
    }

    @Then("Action menu option {string} will be disappeared after user select action")
    public void action_menu_option_will_be_disappeared(String action) {
        log.info("Action menu option will be disappeared after user select the action");
        Assert.assertNull("ERR!!!. Menu option still do show after user select the option", commonFunction.presentElement(By.xpath("//*[@role='menu']//*[text()='%s']".formatted(action)), 2));
    }
}

