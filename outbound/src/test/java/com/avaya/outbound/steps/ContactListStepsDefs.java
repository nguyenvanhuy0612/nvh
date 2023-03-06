package com.avaya.outbound.steps;


import com.avaya.outbound.frame.ContactListPage;
import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
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
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v108.network.Network;
import org.openqa.selenium.support.ui.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContactListStepsDefs extends StepContext {
    private final List<Object> tempDataToCleanUp = new ArrayList<>();
    public Locator locator = new Locator(ContactListPage.class);


    public ContactListStepsDefs(Context context) {
        context.init(this);
    }

    @Before(order = 9000, value = "@ContactList or @ContactListImport or @IXOUTREACH-6020")
    public void initPageObject() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @After(order = 9000, value = "(@IXOUTREACH-4770 and @AC6) or (@AC7 and @IXOUTREACH-4860)")
    public void after(Scenario scenario) {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
        DevTools devTools = testDataObject.getData("devTools");
        if (devTools != null) {
            devTools.send(Network.setBlockedURLs(new ArrayList<>()));
        }
    }

    @After(order = 9001, value = "@IXOUTREACH-5398 and (@ContactListImport or @IXOUTREACH-4801)")
    public void cleanTempData(Scenario scenario) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
        if (scenario.isFailed() && tempDataToCleanUp != null && !tempDataToCleanUp.isEmpty()) {
            try {
                HashMap<String, Object> testData = (HashMap) tempDataToCleanUp.get(0);
                Map<String, String> datasource = (Map) testData.get("datasource");
                if (datasource.get("ftpIPHostName").equalsIgnoreCase("usecommon")) {
                    datasource.put("ftpIPHostName", EnvSetup.SFTP_HOSTNAME);
                    datasource.put("ftpUserName", EnvSetup.SFTP_USERNAME);
                    datasource.put("ftpPassword", EnvSetup.SFTP_PASSWORD);
                }
                log.info("Deleting file: " + datasource.get("ftpRemoteFilePath"));
                utilityFun.deletefile(datasource.get("ftpIPHostName"), datasource.get("ftpUserName"), datasource.get("ftpPassword"), datasource.get("ftpRemoteFilePath"));
                Map<String, String> clist = (Map) testData.get("contactlist");
                //add code to clean contact list here
                log.info("Deleting data: " + datasource.get("name"));
                RestMethodsContactListObj.deleteDataSource(datasource.get("name"), "");
                log.info("Deleting contact list: " + clist.get("name"));
                RestMethodsContactListObj.deleteContactList(clist.get("name"), "");
            } catch (Exception e) {
            }
        }
    }

    @Given("The Contact list URL is hit to view the existing contact")
    public void openURL() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("The Contact list URL is hit to view the existing contact " + EnvSetup.OUTBOUND_CONTACTS_UI_BASEURL);
        commonFunction.navigateToOutboundPage("contacts");
    }

    @Then("Hint message display {string} under name field")
    public void notifyMessageDisplayUnderNameField(String notifyMessageExpected) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify notify message display " + notifyMessageExpected);
        commonFunction.verifyFieldError("name", notifyMessageExpected);
    }

    @When("Create Contact List with information Name and Description")
    public void createContactListWithName() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameContactList = testData.getData("contactlist", "name");
        String descriptionContactList = testData.getData("contactlist", "description");
        log.info("Name Contact List want add " + nameContactList);
        log.info("Description Contact List want add " + descriptionContactList);
        log.info("Create Contact List with Name " + nameContactList + " and Description " + descriptionContactList);
        RestMethodsContactListObj.deleteContactList(nameContactList, "");
        ContactListPageObj.createContactList(nameContactList, descriptionContactList);
    }

    @When("Create Contact List with existed name {string}")
    public void createContactListWithNameOnceAgain(String insensitiveCase) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean insensitiveFlag = !insensitiveCase.contains("none");
        String nameContactList = testData.getData("contactlist", "name");
        log.info("Name Contact List want add " + nameContactList);
        log.info("Create Contact List with Name " + nameContactList + " and once again ");
        commonFunction.clickButton("add");
        commonFunction.waitForPageLoadComplete(30, 1);
        ContactListPageObj.inputNameAndDescriptionThenClickSave(nameContactList, "", insensitiveFlag);
    }

    @Then("Verify button Add New Contact List display")
    public void verifyButtonAddNewContactListDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Button Add New Contact List is not display on Contact landing page",
                ContactListPageObj.isDisplayed("add"));
        Assert.assertEquals("Button Add New Contact List is not display with correct text",
                "Add New Contact List", ContactListPageObj.getTextField("add"));
    }

    @When("Click to Create Contact List Button")
    public void clickToCreateContactListButton() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.clickButton("add");
        commonFunction.waitForPageLoadComplete(10, 1);
    }

    @Then("Should get one new page to create the contact list")
    public void shouldGetOneNewPageToCreateTheContactList() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertEquals("Create contact list page not display",
                "Contact List", ContactListPageObj.getTextField("page"));
    }

    @Then("The Name field should be displayed with highlight as mandatory field")
    public void theNameFieldShouldBeDisplayedWithHighlightAsMandatoryField() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String classAttrNameField = ContactListPageObj.nameFieldWrapper.getAttribute("class");
        log.info("classAttrNameField: " + classAttrNameField);
        Assert.assertTrue("Name field not required", classAttrNameField.contains("required"));
    }

    @And("Verify Name field allowing up to 40 characters including underscores, dashes, alphanumerics")
    public void verifyNameFieldAllowingUpToCharactersIncludingUnderscoresDashesAlphanumerics() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        log.info("Verify that the user can enter 40 valid characters");
        String inpText = "etiamtempororcieulobortisele-1234567890_";
        log.info("Input text: " + inpText);
        Assert.assertEquals("The input text is not match", inpText, ContactListPageObj.getValueAfterInputName(inpText));

        log.info("Verify when a user enters 80 valid characters in the name field, the value will be truncated to 40 characters");
        inpText = "etiamtempororcieulobortisele-1234567890_etiamtempororcieulobortisele-1234567890_";
        log.info("Input text: " + inpText);
        Assert.assertTrue("The input text is not match", inpText.startsWith(ContactListPageObj.getValueAfterInputName(inpText)));
    }

    @Then("The Description field should be displayed")
    public void theDescriptionFieldShouldBeDisplayed() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Description field is not display", ContactListPageObj.isDisplayed("description"));
    }

    @And("Verify Description field allowing up to 128 characters")
    public void verifyDescriptionFieldAllowingUpToCharacters() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String inpText = "~`!@#$%^&*()_+-=/-+,./<>?~`!@#$%^&*()_+-=/\\|[]-+thisisvalidcharactes123456-_thisisvalidcharactes123456-cteds123dd45ds6 _Việt Nam";
        log.info("Input text: " + inpText);
        String afterInputDescription = ContactListPageObj.getTextAfterInputDescription(inpText);
        Assert.assertEquals("Description field is not match", inpText, afterInputDescription);
    }

    @When("He fills information in the Name field and leaves the Description field blank in the contact list form and click on Save button")
    @When("He fills information in the Name and Description fields in the contact list form and click on Save button")
    public void heFillsInTheNameIsAndDescriptionFieldsIsInTheContactListFormAndClickOnSaveButton() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.deleteContactList(testData.getData("contactlist", "name"), "");
        ContactListPageObj.inputNameAndDescriptionThenClickSave(testData.getData("contactlist", "name"), testData.getData("contactlist", "description"), false);
    }

    @Then("^Contact list is created successfully$|^Message should be displayed that - contact list is created successfully$")
    public void contactListIsCreatedSuccessfully() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> confirmMessage = new HashMap<>();
        confirmMessage.put("NotificationAlert", "Contact List created successfully");
        ContactListPageObj.verifyNotification(confirmMessage);
    }

    @And("Admin should landed to contact list page")
    public void adminShouldLandedToContactListPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.waitForPageLoadComplete(10, 1);
        Assert.assertNotNull("This is not landing page", ContactListPageObj.presentElement("add", 10));
        Assert.assertTrue("This is not landing page", ContactListPageObj.isDisplayed("table"));
    }

    @When("He fills in the Name as {string} and leaves the Description field blank in the contact list form and click on Save button")
    public void heFillsInTheNameAndLeavesTheDescriptionFieldBlankInTheContactListFormAndClickOnButton(String name) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.deleteContactList(name, "");
        ContactListPageObj.inputNameAndDescriptionThenClickSave(name, "", false);
    }

    @Then("Name displayed on header Table")
    public void nameDisplayedOnHeaderTable() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.verifyHeaderLabelDisplay("Name");
    }

    @Then("Description displayed on header Table")
    public void descriptionDisplayedOnHeaderTable() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.verifyHeaderLabelDisplay("Description");

    }

    @When("There are no contact list display on contact list landing page")
    public void thereAreNoContactListDisplayContactListLandingPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sendKeysToTextBox("searchTable", "invalidContactList");
        commonFunction.waitForPageLoadComplete(30, 2);
        Assert.assertEquals("Contact List record still present on the page", 0, commonFunction.numberRecordOnCurrentPage());
    }

    @When("Cleanup all contact list data")
    public void cleanUp() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.deleteAllContactList();
    }

    @Then("Message \"Record list is empty\" is displayed in place of row on contact list landing page")
    public void messageIsDisplayedInPlaceOfRowOnContactListLandingPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.verifyMessageWithNoRecord("Record list is empty");
    }

    @Then("Refresh button will be displayed")
    public void refreshButtonWillBeDisplayed() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Refresh button not display", ContactListPageObj.isDisplayed("refresh"));
    }

    @And("Click to refresh button")
    public void clickToRefreshButton() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.clickButton("refresh");
        utilityFun.wait(1);
    }

    @Then("Contacts added will show up at top of table")
    public void contactsWithNameWillShowUpAtTopOfTable() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String name = testData.getData("contactlist", "name");
        log.info("Expecting Name: " + name);
        String firstRecordName = ContactListPageObj.getFirstRecordName();
        Assert.assertEquals("The name is not match", name, firstRecordName);
    }

    @When("Create a list of {int} consecutive contact lists using the API")
    public void createAListOfConsecutiveContactListsUsingTheAPI(int noContact) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("noContact: " + noContact);
        if (noContact > 0) {
            RestMethodsContactListObj.createContactList(testData);
            if (noContact > 1) {
                String clNameDefault = testData.get("name");
                for (int i = 1; i < noContact; i++) {
                    testData.put("name", clNameDefault + i);
                    RestMethodsContactListObj.createContactList(testData);
                }
                testData.put("name", clNameDefault);
            }
        }
        try {
            commonFunction.clickAnElementUsingJavaScript("refresh", 5);
        } catch (Exception e) {
        }
    }

    @And("Click to next page button")
    public void clickToNextPageButton() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.clickButton("nextPage");
    }

    @Then("User be back to first page with default sort option")
    public void userBeBackToFirstPageWithDefaultSortOption() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.isDisplayed("page_1");
        utilityFun.wait(1);
        boolean page1asDefaults = commonFunction.findElementByXpath("//button[@data-testid='page_1']").getAttribute("class").contains("secondary");
        Assert.assertTrue("Not back to default page", page1asDefaults);
    }

    @Then("Change the page size to {string} values one by one then click refresh button then make sure the page size restore back to {int} as default")
    public void pageSizeRestore(String pageSizeList, int defaultPageSize) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Not found dropdown option", ContactListPageObj.isDisplayed("pageSizeDropdown"));
        for (String pageSize : pageSizeList.split(",")) {
            log.info("Change page size to: " + pageSize);
            ContactListPageObj.selectDropDownOptionEqual("pageSizeDropdown", pageSize);
            utilityFun.wait(2);
            commonFunction.clickRefreshButton();
            commonFunction.waitForPageLoadComplete(10, 1);
            int actPageSize = commonFunction.getCurrentPageSize();
            Assert.assertEquals("User is not backed to default page size", defaultPageSize, actPageSize);
        }
    }

    @When("Click to Cancel Button")
    public void clickToCancelButton() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("cancel");
        utilityFun.wait(2);
    }

    @And("Provides name and description in the contact list form")
    public void inputDataInTheContactListForm() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sendKeysToTextBox("name", testData.getData("contactlist", "name"));
        commonFunction.sendKeysToTextBox("description", testData.getData("contactlist", "description"));
    }

    @Then("User is backed to the contact list page")
    public void backToContactListPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("User is not backed to contact list page", ContactListPageObj.isDisplayed("add"));
    }

    @Then("User still stay on Add New Contact List page")
    public void stayOnAddNewContactListPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("User is not on the add new contact list page", ContactListPageObj.isDisplayed("save"));
    }

    @Then("Cancellation confirmation box displays")
    public void cancellationConfirmationBoxDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Cancellation confirmation box is not display after clicking cancel button", ContactListPageObj.isDisplayed("leave-page"));
    }

    @Then("Information on Cancellation confirmation box display correctly")
    public void verifyInformationOnCancellationConfirmationBox() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Cancellation confirmation header is missing", ContactListPageObj.popUpHeader.isDisplayed());
        Assert.assertEquals("Header Popup text not match", "Leave this page?", ContactListPageObj.popUpHeader.getText());
        Assert.assertTrue("Cancellation confirmation message is missing", ContactListPageObj.popupMessage.isDisplayed());
        Assert.assertEquals("Message Popup text not match", "The changes will be lost if you navigate away from this page.", ContactListPageObj.popupMessage.getText());
        Assert.assertTrue("Stay on this page button is missing", ContactListPageObj.isDisplayed("stay-page"));
        Assert.assertEquals("Stay page option text not match", "Stay on this page", ContactListPageObj.getTextField("stay-page"));
        Assert.assertTrue("Leave this page button is missing", ContactListPageObj.isDisplayed("leave-page"));
        Assert.assertEquals("Leave page option text not match", "Leave this page", ContactListPageObj.getTextField("leave-page"));
    }

    @And("Click {string} on Cancellation confirmation box")
    public void selectActionOnCancellationConfirmationBox(String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.selectAction(action);
    }

    @Then("Try entering the characters {string} one by one in the Name field, then verify the hint error message appears and the contact list cannot be created with these characters")
    public void verifyNameField(String characters) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("List of invalid characters: " + characters);
        for (char input : characters.toCharArray()) {
            log.info("Try to input character: " + input);
            commonFunction.findElementByDataID("name").clear();
            ContactListPageObj.sleepInSec(0.2);
            commonFunction.sendKeysToTextBox("name", String.valueOf(input));
            ContactListPageObj.sleepInSec(0.3);
            commonFunction.clickButton("save");
            ContactListPageObj.sleepInSec(0.5);
            commonFunction.verifyFieldError("name", "Name should start with alphabet and allowed characters are alphanumeric, hyphen and underscore.");
            Assert.assertTrue("Name field is not present", commonFunction.isDisplayed("name"));
        }
    }

    @And("There is a problem that cannot connect to the backend")
    public void thereIsAProblemThatCannotConnectToTheBackend() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        utilityFun.wait(2);
        DevTools devTools = commonFunction.blockRequestViaDevToolsAPI(List.of(EnvSetup.OUTBOUND_CONTACTS_API_BASEURL));
        testDataObject.put("devTools", devTools);
    }

    @Then("An unexpected error occurred with the message: {string}")
    public void anUnexpectedErrorOccurredWithTheMessage(String message) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String actualMessage = ContactListPageObj.getToastMessage();
        log.info("Toast Message: " + actualMessage);
        Assert.assertEquals("Message not match", message, actualMessage);
    }

    @When("Click to Name column")
    public void clickToNameColumn() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.tryClick(By.xpath("//div[@data-testid='header-cell']/span[text()='Name']"), 60);
    }

    @When("^Change the page size to (\\d+)$")
    public void changeThePageSizeTo(String size) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.selectDropDownOptionEqual("pageSizeDropdown", size);
    }

    @Given("The Contact list is created")
    public void the_contact_list_is_created() {
        tempDataToCleanUp.add(testData);
        commonFunction.navigateToOutboundPage("contacts");
        log.info("Create contact List");
        Map<String, String> clist = testData.getData("contactlist");
        Map<String, String> datasource = testData.getData("datasource");
        log.info(clist);
        log.info("Delete Contact list if available");
        RestMethodsContactListObj.deleteDataSource(datasource.get("name"), "");
        RestMethodsContactListObj.deleteContactList(clist.get("name"), "");
        ContactListPageObj.createContactList(clist);
        Map<String, String> confirmMessage = new HashMap<>();
        confirmMessage.put("NotificationAlert", clist.get("NotificationAlert"));
        ContactListPageObj.verifyNotification(confirmMessage);
        commonFunction.waitForPageLoadComplete(10, 1);
        commonFunction.clickButton("cancel");
    }

    @Given("contactlist csv file available on SFTP server")
    public void contactlist_csv_file_available_on_sftp_server() {
        Map<String, String> datasource = testData.getData("datasource");
        log.info(datasource);
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + datasource.get("sourceFile");
        log.info(sourceFile);
        String remotefile = EnvSetup.SFTP_REMOTE_FILEPATH + "/" + datasource.get("ftpRemoteFilePath");
        utilityFun.copyFile(datasource.get("ftpIPHostName"), datasource.get("ftpUserName"), datasource.get("ftpPassword"), sourceFile, remotefile);
    }

    @Given("new updated contactlist csv file available on SFTP server")
    public void new_updated_contactlist_csv_file_available_on_sftp_server() {
        Map<String, String> datasource = testData.getData("datasource");
        log.info(datasource);
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + datasource.get("updatedsourceFile");
        String remotefile = EnvSetup.SFTP_REMOTE_FILEPATH + "/" + datasource.get("ftpRemoteFilePath");
        log.info(sourceFile);
        utilityFun.copyFile(datasource.get("ftpIPHostName"), datasource.get("ftpUserName"), datasource.get("ftpPassword"), sourceFile, remotefile);
    }


    @When("user run data source")
    public void user_run_data_source() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");
        ContactListPageObj.runDataSource(contactlist.get("name"), datasource.get("name"));
        if (contactlist.containsKey("NotificationAlert-RunDataSource") && !contactlist.get("NotificationAlert-RunDataSource").isEmpty()) {
            Map<String, String> confirmMessage = new HashMap<>();
            confirmMessage.put("NotificationAlert", contactlist.get("NotificationAlert-RunDataSource") + " " + contactlist.get("name"));
            ContactListPageObj.verifyNotification(confirmMessage);
        }
    }


    @Then("contact list page should show import in progress status")
    public void contact_list_page_should_show_import_in_progress_status() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");
        if (contactlist.containsKey("TimeRequiredToUpload")) {
            ContactListPageObj.monitorImportProgress(contactlist.get("name"), Integer.parseInt(contactlist.get("TimeRequiredToUpload")));
        } else {
            ContactListPageObj.monitorImportProgress(contactlist.get("name"), 60);
        }
    }

    @Then("once import is completed last import status should be updated")
    public void once_import_is_completed_last_import_status_should_be_updated() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");
        ContactListPageObj.verifyContactListInformationByColumn("Total Contacts", contactlist.get("Total Contacts"), contactlist.get("name"), true);
        ContactListPageObj.verifyContactListInformationByColumn("Last Success Count", contactlist.get("Last Success"), contactlist.get("name"), true);
        ContactListPageObj.verifyContactListInformationByColumn("Last Failed Count", contactlist.get("Last Failed"), contactlist.get("name"), true);
        String lastupdated = ContactListPageObj.verifyContactListInformationByColumn("Last Updated", contactlist.get("Last Updated"), contactlist.get("name"), false);
        utilityFun.compareLastUpdatedTime(lastupdated);
        log.info("Verify Job status and reason");
        if (!contactlist.get("ImportJobAlert").equalsIgnoreCase("COMPLETED")) {
            log.info("Error is expected verify Failed import icon is displayed and tooltip is avaiable");
            commonFunction.verifytoolTip(contactlist.get("name"), contactlist.get("ImportJobAlert"), true, contactlist.get("VerifyHeader") != null);
        } else {
            JsonObject importJobData = RestMethodsContactListObj.getImportJobDetails(contactlist.get("name"));
            log.info("------------------------------------------------------------------");
            log.info("Import Job reason -->" + importJobData.get("reason").toString().replaceAll("\"", "") + " " + contactlist.get("ImportJobAlert"));
            Assert.assertEquals("Import Job reason not matched", contactlist.get("ImportJobAlert"), importJobData.get("reason").toString().replaceAll("\"", ""));
            log.info("------------------------------------------------------------------");
        }
    }

    @Then("Verify list is imported successfully")
    public void verify_list_is_imported_successfully() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");
        JsonArray contacts = RestMethodsContactListObj.getAllContactsFromList(contactlist.get("name"), 0);
        Assert.assertNotNull("No contacts found", contacts);
        String[] ignoreAttributes = contactlist.get("AttributesToIgnoreFromVerification").split("\\,");
        String outfile = System.getProperty("user.dir") + "/src/test/resources/data/outfile/" + contactlist.get("name") + "_importList.out";
        String expfile = System.getProperty("user.dir") + "/src/test/resources/data/expfile/" + contactlist.get("name") + "_importList.exp";

        ContactListPageObj.writeContactDatatoOutFile(contacts, outfile, ignoreAttributes, "firstname", true);
        log.info("Verify Result file");
        Assert.assertTrue(utilityFun.compareResultFile(outfile, expfile));

    }

    @Then("Verify list is imported successfully and all contacts are correct")
    public void verify_list_is_imported_successfully_and_all_contacts_are_correct() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");
        int totalcontacts = Integer.parseInt(contactlist.get("Total Contacts"));
        int totalpages = totalcontacts / 100;
        log.info("Total Contacts : " + totalcontacts);
        log.info("Total Pages : " + totalpages);
        String[] ignoreAttributes = contactlist.get("AttributesToIgnoreFromVerification").split("\\,");
        String outfile = System.getProperty("user.dir") + "/src/test/resources/data/outfile/" + contactlist.get("name") + "_importList.out";
        String expfile = System.getProperty("user.dir") + "/src/test/resources/data/expfile/" + contactlist.get("name") + "_importList.exp";

        JsonArray contacts = RestMethodsContactListObj.getAllContactsFromList(contactlist.get("name"), 0);
        Assert.assertNotNull("No contacts found", contacts);
        for (int i = 1; i < totalpages; i++) {
            JsonArray tmpcontacts = RestMethodsContactListObj.getAllContactsFromList(contactlist.get("name"), i);
            Assert.assertNotNull("No contacts found", tmpcontacts);
            contacts.addAll(tmpcontacts);


        }
        ContactListPageObj.writeContactDatatoOutFile(contacts, outfile, ignoreAttributes, "firstname", true);
        log.info("Verify Result file");
        Assert.assertTrue("Expected and actual files not equal ", utilityFun.compareResultFile(outfile, expfile));

    }

    @Then("Verify id is unique in imported list")
    public void Verify_id_is_unique_in_imported_list() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");

        JsonArray contacts = RestMethodsContactListObj.getAllContactsFromList(contactlist.get("name"), 0);
        Assert.assertNotNull("No contacts found", contacts);

        contacts = utilityFun.JsonObjectSort(contacts, "Id");
        utilityFun.verifyDuplicateElementinResult(contacts, "Id");

    }

    @Then("Verify existing data is deleted and new list is imported")
    public void Verify_existing_data_is_deleted_and_new_list_is_imported() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");

        JsonArray contacts = RestMethodsContactListObj.getAllContactsFromList(contactlist.get("name"), 0);
        Assert.assertNotNull("No contacts found", contacts);

        String[] ignoreAttributes = contactlist.get("AttributesToIgnoreFromVerification").split("\\,");
        String outfile = System.getProperty("user.dir") + "/src/test/resources/data/outfile/" + contactlist.get("name") + "reimport_importList.out";
        String expfile = System.getProperty("user.dir") + "/src/test/resources/data/expfile/" + contactlist.get("name") + "reimport_importList.exp";

        ContactListPageObj.writeContactDatatoOutFile(contacts, outfile, ignoreAttributes, "firstname", true);
        log.info("Verify Result file");
        Assert.assertTrue(utilityFun.compareResultFile(outfile, expfile));
    }

    @Given("The SFTP data source is created")
    public void the_sftp_data_source_is_created() {
        Map<String, String> datasourcetmp = new HashMap<>();
        datasourcetmp.putAll(testData.getData("datasource"));
        Map<String, String> contactlist = testData.getData("contactlist");
        if (datasourcetmp.get("ftpIPHostName").equalsIgnoreCase("usecommon")) {
            datasourcetmp.put("ftpIPHostName", EnvSetup.SFTP_HOSTNAME);
            datasourcetmp.put("ftpUserName", EnvSetup.SFTP_USERNAME);
            datasourcetmp.put("ftpPassword", EnvSetup.SFTP_PASSWORD);
            datasourcetmp.put("ftpRemoteFilePath", EnvSetup.SFTP_REMOTE_FILEPATH + "/" + datasourcetmp.get("ftpRemoteFilePath"));
        }
        RestMethodsContactListObj.deleteDataSource(datasourcetmp.get("name"), "");
        commonFunction.navigateToOutboundPage("datasource");
        DataSourcePageObj.addFTPdataSourceN(datasourcetmp);
        Map<String, String> confirmMessage = new HashMap<>();
        confirmMessage.put("NotificationAlert", datasourcetmp.get("NotificationAlert"));
        commonFunction.verifyNotification(confirmMessage);
    }


    @Then("clean the files on SFTP server")
    public void clean_the_files_on_sftp_server() {
        Map<String, String> datasourcetmp = new HashMap<>();
        datasourcetmp.putAll(testData.getData("datasource"));
        if (datasourcetmp.get("ftpIPHostName").equalsIgnoreCase("usecommon")) {
            datasourcetmp.put("ftpIPHostName", EnvSetup.SFTP_HOSTNAME);
            datasourcetmp.put("ftpUserName", EnvSetup.SFTP_USERNAME);
            datasourcetmp.put("ftpPassword", EnvSetup.SFTP_PASSWORD);
            datasourcetmp.put("ftpRemoteFilePath", EnvSetup.SFTP_REMOTE_FILEPATH + "/" + datasourcetmp.get("ftpRemoteFilePath"));
        }
        utilityFun.deletefile(datasourcetmp.get("ftpIPHostName"), datasourcetmp.get("ftpUserName"), datasourcetmp.get("ftpPassword"), datasourcetmp.get("ftpRemoteFilePath"));
    }

    @Then("Page size options should be displayed")
    public void pageSizeDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Page size options not display", commonFunction.findElementByDataID("pageSizeDropdown").isDisplayed());
    }

    @And("Page number should be displayed")
    public void pageNumberDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("List page number not display", commonFunction.findElementByXpath(locator.getLocator("numberPage")).isDisplayed());
    }

    @Then("Previous and next button should be displayed")
    public void btnGoPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Next page button not display", commonFunction.findElementByDataID("nextPage").isDisplayed());
        Assert.assertTrue("Previous page button not display", commonFunction.findElementByDataID("previousPage").isDisplayed());
    }

    @And("Page detail should be displayed")
    public void pageDetailDisplay() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Number of record - page detail not display", commonFunction.findElementByDataID("pageDetails").isDisplayed());
    }

    @Then("Page size value displays {string} by default")
    public void getDefaultPageSize(String defaultValue) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String actPageSize = String.valueOf(ContactListPageObj.getCurrentPageSize());
        Assert.assertEquals("Currently default page size is " + actPageSize + ", not as expected " + defaultValue, defaultValue, actPageSize);
    }

    @Then("Change the page size {string} values one by one then page size value display correctly")
    public void changePageSizeThenItWorkFine(String defaultPageSizeList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] pageSizeValue = defaultPageSizeList.split("\\,");
        Map<String, String> ctlData = testData.getData("contactlist");
        String clNameDefault = ctlData.get("name");
        commonFunction.basicSearchOnTable(clNameDefault);
        int totalRecords = ContactListPageObj.countAllRecord();
        for (String select : pageSizeValue) {
            int pageSize = Integer.parseInt(select);
            ContactListPageObj.selectPageSize(select);
            String curSize = String.valueOf(ContactListPageObj.getCurrentPageSize());
            Assert.assertEquals("Page size is not displayed as expected", select, curSize);
            ContactListPageObj.verifyNumberOfRecordsEachPage(totalRecords, pageSize);
        }
    }

    @Then("Change the page size {string} values one by one then verify number of page display correctly")
    public void getNumberOfPageThenVerify(String pageSizeList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> ctlData = testData.getData("contactlist");
        String clNameDefault = ctlData.get("name");
        commonFunction.basicSearchOnTable(clNameDefault);
        int totalRecords = ContactListPageObj.countAllRecord();
        String[] pageSizeValue = pageSizeList.split("\\,");
        for (String pageSize : pageSizeValue) {
            ContactListPageObj.selectPageSize(pageSize);
            ContactListPageObj.verifyNumberOfPage(pageSize, totalRecords);
        }
    }

    @Then("Change the page size {string} values one by one then verify number of records each page display correctly")
    public void getPageDetailWithPageSizeSelectedAndVerify(String pageSize) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> ctlData = testData.getData("contactlist");
        String clNameDefault = ctlData.get("name");
        commonFunction.basicSearchOnTable(clNameDefault);
        int totalRecords = ContactListPageObj.countAllRecord();
        String[] pageSizeList = pageSize.split("\\,");
        for (String select : pageSizeList) {
            ContactListPageObj.selectPageSize(select);
            ContactListPageObj.verifyPageDetail(select, totalRecords);
        }
    }

    @Then("Change the page size {string} values one by one then next and previous page button work fine")
    public void verifyNextOrPreviousPage(String pageSize) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> ctlData = testData.getData("contactlist");
        String clNameDefault = ctlData.get("name");
        commonFunction.basicSearchOnTable(clNameDefault);
        int totalRecords = ContactListPageObj.countAllRecord();
        ContactListPageObj.selectPageSize(pageSize);
        int countNumberPage = ContactListPageObj.numberPageWithPageSize(pageSize, totalRecords);
        ContactListPageObj.verifyNextAndPreviousButton(countNumberPage);
    }

    @Then("{string} of contact list displayed correctly with information contact list created")
    public void verifyColumnListContactLists(String colName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String value = testData.getData("contactlist", "name");
        ContactListPageObj.verifyContactListInformationByColumn(colName, value, value, true);
    }

    @Then("Input page number value {string} which is not exist on landing page into Go to page field and verify message display")
    public void inputPageNumberAndGetResponse(String pageNumberList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] list = pageNumberList.split("\\,");
        for (String pageNumber : list) {
            ContactListPageObj.sendKeysToTextBox("goToPage", pageNumber);
            ContactListPageObj.waitForPageLoadComplete(30, 1);
            String toastMessage = ContactListPageObj.getToastMessage();
            log.info("toastMessage: " + toastMessage);
            Assert.assertEquals("Toast message display incorrectly", "Please enter valid page number", toastMessage);
            utilityFun.wait(1);
        }
    }

    @Then("The default display order of contact list displayed based on update time of the contact list")
    public void verifyDefaultOrderDisplayCorrectly() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String newNameContactList = "contact_AC2_TC1__0";
        RestMethodsContactListObj.deleteContactList(newNameContactList, "");
        ContactListPageObj.createContactList(newNameContactList, "");
        String nameContactFirstRow = ContactListPageObj.getListColumnDataOnCurPage("Name").get(0);
        Assert.assertTrue("Default order is not display correctly", newNameContactList.contains(nameContactFirstRow));
    }

    @Then("The {string} column toggle between Asc and Desc orders with UI indication transitioning from one order to another when clicked")
    public void verifyTogglebetweenAscAndDescWhenClickedagain(String columnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify sort work properly with " + columnName + " column");
        boolean expected = ContactListPageObj.verifySortIconDisplayInColumn(columnName);
        Assert.assertTrue("Sort icon is not displayed correctly when use click " + columnName + " column on contact list landing page", expected);
        log.info("Sort icon displayed correctly when use click " + columnName + " column on contact list landing page");
    }

    @Then("{string} column should be sorted in desired order as per the admin's selection.")
    public void verifySortedInDesiredOrderAsPerTheAdminSSelection(String columnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify sort work properly with " + columnName + " column");
        boolean expected = ContactListPageObj.verifySortWorkProperly(columnName);
        Assert.assertTrue("Sort can work on contact list landing page", expected);
        log.info(columnName + " sorted in desired order as per the admin's selection.");
    }

    @Then("Default order contact list of {string} column displayed correctly after toggle Asc and Desc orders and go back to the default")
    public void defaultOrderContactListDisplayedCorrectlyAfterToggleAscAndDescOrdersAndGoBackToTheDefault(String columnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> listDefaultBeforeSort = ContactListPageObj.getListColumnDataOnCurPage(columnName);
        boolean expected = ContactListPageObj.verifySortIconDisplayInColumn(columnName);
        Assert.assertTrue("Sort icon is not displayed correctly when use click " + columnName + " column on contact list landing page", expected);
        List<String> listDefaultAfterSort = ContactListPageObj.getListColumnDataOnCurPage(columnName);
        boolean result = listDefaultBeforeSort.equals(listDefaultAfterSort);
        Assert.assertTrue("Default order is not display correctly when toggle sort", result);
        log.info("Default order contact list of " + columnName + " column displayed correctly after toggle Asc and Desc orders and go back to the default");
    }

    @Then("The Sort work correctly with different page and {string} header")
    public void theSortWorkCorrectlyWithDifferentPage(String columnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean result = ContactListPageObj.verifySortWorkProperlyWithDifferentPage(columnName);
        log.info("Result sort with different page " + result);
        Assert.assertTrue("Sort is not work correctly with different page", result);
    }

    @When("Create a contact list with name with Name {string} and Description {string}")
    public void createContactList(String nameRecord, String description) {
        RestMethodsContactListObj.deleteContactList(nameRecord, "");
        createListOfConsecutiveContactList(1, nameRecord, description);
    }

    @When("Create a list of {int} consecutive contact list with name with Name start with {string} and Description {string}")
    public void createListOfConsecutiveContactList(int numberContactList, String nameRecord, String description) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", nameRecord);
        data.put("description", description);
        if (numberContactList > 0) {
            RestMethodsContactListObj.createContactList(data);
            if (numberContactList > 1) {
                String clNameDefault = data.get("name");
                for (int i = 1; i < numberContactList; i++) {
                    data.put("name", (clNameDefault + "_" + i));
                    RestMethodsContactListObj.createContactList(data);
                }
                data.put("name", clNameDefault);
            }
        }
        try {
            WebElement refresh = commonFunction.presentElement("refresh", 5);
            if (refresh != null)
                refresh.click();
        } catch (Exception e) {
        }
    }

    @Given("User select contact list and clicks import contact")
    public void user_select_contact_list_and_clicks_import_contact() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");
        ContactListPageObj.navigateToOutboundPage("contacts");
        ContactListPageObj.basicSearchOnTable(contactlist.get("name"));
        ContactListPageObj.clickAction(contactlist.get("name"), "Import Contacts");
    }

    @Then("Verify Import Contacts page is displayed")
    public void verify_import_contacts_page_is_displayed() {
        log.info("Verify Import Contacts page is displayed");
        ContactListPageObj.isDisplayed("dataSource", 2);
        log.info("Verify Run button is disabled");
        Assert.assertFalse("ERR!!!. Run button must disabled at the beginning", commonFunction.findElementByDataID("run").isEnabled());
    }

    @Then("Users able to select one data source from dropdown list")
    public void users_able_to_select_one_data_source_from_dropdown_list() {
        Map<String, String> datasource = testData.getData("datasource");
        ContactListPageObj.selectDropDownOptionEqual("dataSource", datasource.get("name"));
        Assert.assertTrue("ERR!!!. Run button must enable at the after selecting DS", commonFunction.findElementByDataID("run").isEnabled());
    }

    @When("user click more actions")
    public void userClickMoreActions() {
        Map<String, String> clist = testData.getData("contactlist");
        ContactListPageObj.clickTakeMoreActionElipse(clist.get("name"));
    }


    @Then("Verify the {string} is displayed")
    public void verifyTheIsDisplayed(String importContactE) {
        ContactListPageObj.verifyImportDSVisible();
    }

    @When("User clicks the refresh button on contact list page")
    public void userClicksTheRefreshButtonOnContactListPage() {
        commonFunction.tryClick("refresh", 3);
        commonFunction.waitForPageLoadComplete(30, 1);
    }

    @Then("Verify two columns named Last Updated and Total Contacts are displayed")
    public void verifyTotalcontactandLastUpdatedColumn() {
        //show 2 column here
        ContactListPageObj.verifyTotalContactsAndLastUpdateVisible();
    }

    @When("Users select one data source to import")
    public void userImportDS() {
        Map<String, String> clist = testData.getData("contactlist");
        Map<String, String> ds = testData.getData("datasource");
        ContactListPageObj.runDataSource(clist.get("name"), ds.get("name"));
        if (clist.containsKey("NotificationAlert-RunDataSource") && !clist.get("NotificationAlert-RunDataSource").isEmpty()) {
            Map<String, String> confirmMessage = new HashMap<>();
            confirmMessage.put("NotificationAlert", clist.get("NotificationAlert-RunDataSource") + " " + clist.get("name"));
            ContactListPageObj.verifyNotification(confirmMessage);
        }
    }


    @Then("Verify User should be redirected to {string} landing page")
    public void verifyUserShouldBeRedirectedToLandingPage(String pageName) {
        ContactListPageObj.verifyPageVisible(pageName);
    }

    @And("Verify Total contacts on the Total Contacts column")
    public void verifyTotalContactsOnTheTotalContactsColumn() {
        Map<String, String> clist = testData.getData("contactlist");
        ContactListPageObj.verifyTotalContacts(clist.get("name"), Integer.parseInt(clist.get("numRecords")));
    }

    @When("user search contact list")
    public void userSearchContactList() {
        Map<String, String> clist = testData.getData("contactlist");
        commonFunction.navigateToOutboundPage("contacts");
        ContactListPageObj.basicSearchOnTable(clist.get("name"));
    }

    @When("clean the contact list")
    public void clean_the_contact_list() {
        RestMethodsContactListObj.deleteDataSource(testData.getStrict("datasource", "name"), "");
        RestMethodsContactListObj.deleteContactList(testData.getStrict("contactlist", "name"), "");
    }

    @Then("Verify records show timestamp on Create on column")
    public void verifyRecordsShowTimestampOnCreateOnColumn() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");

        JsonArray contacts = RestMethodsContactListObj.getAllContactsFromList(contactlist.get("name"), 0);
        Assert.assertNotNull("No contacts found", contacts);

        ContactListPageObj.verifyTimestampRecordsByRestAPI(contacts);
    }

    @And("Create a contact list and add records through a data source")
    public void createContactListWithRecord(List<Map<String, String>> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> colData = data.get(0);
        testData.load(colData.get("DataFile"), colData.get("Data"));
        driver.get(EnvSetup.OUTBOUND_CONTACTS_UI_BASEURL);

        log.info("Copy csv file to FTP Host");
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + testData.get("sourceFile");
        utilityFun.copyFile(testData.get("ftpIPHostName"), testData.get("ftpUserName"), testData.get("ftpPassword"), sourceFile, testData.get("ftpRemoteFilePath"));

        log.info("Create data source name: " + testData.get("name"));
        RestMethodsContactListObj.deleteContactList(testData.get("name"), "");
        RestMethodsContactListObj.createDataSource(testData);

        log.info("Create contact list name: " + colData.get("clName"));
        RestMethodsContactListObj.deleteContactList(colData.get("clName"), "");
        this.createContactList(colData.get("clName"), colData.get("clDescription"));

        log.info("Import contact to list: " + colData.get("clName"));
        commonFunction.waitForPageLoadComplete(10, 1);
        log.info("Run data source");
        commonFunction.basicSearchOnTable(colData.get("clName"));
        commonFunction.sleepInSec(0.5);
        commonFunction.clickAction(colData.get("clName"), "Import Contacts");
        commonFunction.sleepInSec(0.5);
        commonFunction.selectDropDownOptionEqual("dataSource", testData.get("name"));
        commonFunction.sleepInSec(0.5);
        commonFunction.clickButton("run");
        commonFunction.sleepInSec(5);
    }

    @Given("Click on Contact browser page of contact list")
    public void clickOnContactBrowserPageOfContactList() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String clName = testData.getData("contactlist", "name");
        log.info("clName: " + clName);
        commonFunction.sleepInSec(1.5);
        commonFunction.basicSearchOnTable(clName);
        commonFunction.sleepInSec(1.5);
        log.info("Click hyperlink on total contact of contact list");
        commonFunction.tryClick(By.xpath(String.format(locator.getLocator("hyperlinkTotalContact"), clName)), 30);
        commonFunction.waitForPageLoadComplete(10, 1);
    }

    @Then("The default page size value is 10")
    public void theDefaultPageSizeValue() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sleepInSec(1.5);
        Assert.assertEquals("Number of record is not equal to 10", commonFunction.getListColumnDataOnCurPage("firstname").size(), 10);
        Assert.assertTrue("Page size is not 10", commonFunction.getAttributeField("pageSizeDropdown", "title").equalsIgnoreCase("10"));
    }

    @Then("Change page size to 10, 20, 50, 100 and verify its working correctly")
    public void VerifyPageSizeWorkingCorrectly() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<Integer> pageSizeList = Arrays.asList(10, 20, 50, 100);
        for (Integer pageSize : pageSizeList) {
            commonFunction.selectDropDownOptionEqual("pageSizeDropdown", String.valueOf(pageSize));
            commonFunction.sleepInSec(1);
        }
    }

    @Then("Change page size to 10, 20, 50, 100 and verify the number of page display correctly")
    public void VerifyTheNumberOfPageDisplayCorrectly() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<Integer> pageSizeList = Arrays.asList(10, 20, 50, 100);
        int total = 24;
        for (Integer pageSize : pageSizeList) {
            int actualPages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            commonFunction.selectDropDownOptionEqual("pageSizeDropdown", String.valueOf(pageSize));
            commonFunction.sleepInSec(1.5);
            int numberOfPages = commonFunction.findElementsByXpath(locator.getLocator("numberOfPageDisplay")).size();
            Assert.assertEquals("The number of page not match", actualPages, numberOfPages);
        }
    }


    @Then("Change page size to 10, 20, 50, 100 and verify each page will " + "display no more than the number of configuration records in page size")
    public void VerifyPageWillDisplayNoMoreThanTheNumberOfRecordsConfigured() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<Integer> pageSizeList = Arrays.asList(10, 20, 50, 100);
        for (Integer pageSize : pageSizeList) {
            commonFunction.selectDropDownOptionEqual("pageSizeDropdown", String.valueOf(pageSize));
            commonFunction.sleepInSec(1.5);
            int numberOfRecords = commonFunction.getListColumnDataOnCurPage("First Name").size();
            Assert.assertTrue(numberOfRecords <= pageSize);
        }
    }

    @Then("Verify record name will display correctly on contact browser")
    public void verifyRecordNameWillDisplayCorrectlyOnContactBrowser() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + testData.getData("datasource", "sourceFile");
        List<String> actualFNames = commonFunction.getListColumnDataOnCurPage("First Name");
        List<String> fileFName = commonFunction.getValueColumnOfFile(sourceFile, "firstname");
        Assert.assertTrue(fileFName.containsAll(actualFNames));
    }

    @When("The record number, page number, previous, next page buttons, page size options will not be displayed")
    public void theRecordNumberPageNumberPreviousNextPageButtonsPageSizeOptionsWillNotBeDisplayed() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertNull("No records but Next button is still display", commonFunction.presentElement("pageDetails", 5));
        Assert.assertNull("No records but Next button is still display", commonFunction.presentElement("previousPage", 5));
        Assert.assertNull("No records but Next button is still display", commonFunction.presentElement("nextPage", 5));
        Assert.assertNull("No records but Next button is still display", commonFunction.presentElement("pageSizeDropdown", 5));
    }

    @Then("Verify the record number, page number, previous, next page buttons, page size options display correctly")
    public void verifyTheRecordNumberPageNumberPreviousNextPageButtonsPageSizeOptionsDisplayCorrectly() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue(commonFunction.isDisplayed("pageDetails", 10));
        Assert.assertTrue(commonFunction.isDisplayed("previousPage", 10));
        Assert.assertTrue(commonFunction.isDisplayed("nextPage", 10));
        Assert.assertTrue(commonFunction.isDisplayed("pageSizeDropdown", 10));
    }

    @Then("Verify user should able to go to previous and next page")
    public void verifyUserShouldAbleToGoToPreviousAndNextPage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.selectDropDownOptionEqual("pageSizeDropdown", "10");
        commonFunction.sleepInSec(1.5);
        Assert.assertTrue(commonFunction.tryClick("nextPage", 10));
        commonFunction.sleepInSec(1.5);
        Assert.assertTrue(commonFunction.tryClick("previousPage", 10));
    }

    @Then("The message Record list is empty displayed on the pagination because there are no records configured")
    public void theMessageDisplayedOnThePaginationIsBecauseThereAreNoRecordsConfigured() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue(commonFunction.getTextField("emptylist").equalsIgnoreCase("Record list is empty"));
    }

    @And("Delete all contact list with Name contains {string}")
    public void deleteAllContactListWithNameContains(String name) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.deleteAllContactList(name);
    }

    @And("Delete contact lists added")
    public void deleteContactListsAdded() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.deleteAllContactList(testData.get("name"));
    }

    @And("Search contact list {string}")
    public void searchContactList(String clName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sendKeysToTextBox("searchTable", clName);
        utilityFun.wait(1);
    }

    @Then("Contact page display after click View contact option")
    public void contactPageDisplayAfterClickViewContactOption() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.tryClick("action_viewContacts", 5);
        Assert.assertTrue("Contact page is not display after click Hyper link", commonFunction.presentElement(By.xpath(locator.getLocator("contactPageName")), 10) != null);
    }

    @Then("View contact option display on three dot dropdown of contact list")
    public void viewContactOptionDisplayOnThreeDotDropdown() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.searchAndClickThreeDotsContactList(testData.getData("contactlist", "name"));
        Assert.assertTrue("View contact option is not display on three dots dropdown", commonFunction.isDisplayed("action_viewContacts"));
    }

    @When("Click View contact option on three dot dropdown of contact list")
    public void clickViewContactOption() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.searchAndClickThreeDotsContactList(testData.getData("contactlist", "name"));
        commonFunction.tryClick("action_viewContacts", 10);
        commonFunction.waitForPageLoadComplete(60, 1);
    }

    @Then("Message {string} is displayed in place of row on contact landing page")
    public void messageIsDisplayedInPlaceOfRowOnContactLandingPage(String message) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.verifyMessageWithNoRecord(message);
    }

    @Given("Copy file contact and create data source")
    public void coppyFileContactAndCreateDataSourceWithContactAdded() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + testData.get("sourceFile");
        log.info(sourceFile);
        utilityFun.copyFile(testData.get("ftpIPHostName"), testData.get("ftpUserName"), testData.get("ftpPassword"), sourceFile, testData.get("ftpRemoteFilePath"));
        RestMethodsContactListObj.createDataSource(testData);
    }

    @Given("Import and run Data source with contact list {string}")
    public void importAndRunDataSourceWithContactListCreated(String contactListName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.searchAndClickThreeDotsContactList(contactListName);
        commonFunction.tryClick("action_importContacts", 10);
        commonFunction.selectDropDownOptionEqual("dataSource", testData.get("name"));
        commonFunction.clickButton("run");
        String toastMessage = ContactListPageObj.getToastMessage();
        Assert.assertEquals("Message display is incorrect", "Contact import started successfully.", toastMessage);
        log.info("Monitor import process status");
        ContactListPageObj.monitorImportProgress(contactListName, 60);
    }

    @Then("The column headers display correctly as per the business attributes associated with that list")
    public void verifyHeaderColumnDisplayCorrectly() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + testData.getData("datasource", "sourceFile");
        List<String> headerData = List.of(commonFunction.getValueHeaderFileImport(sourceFile).get(0).split(","));
        for (String header : headerData) {
            ContactListPageObj.verifyHeaderLabelDisplay(header);
        }
    }

    @Then("Information contact displayed correctly with business attributes associated")
    public void verifyInformationContactDisplayedCorrectlyWithNumberAttribute() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + testData.getData("datasource", "sourceFile");
            String[] dataNameColumn = commonFunction.getValueHeaderFileImport(sourceFile).get(0).split(",");
            for (String nameColumn : dataNameColumn) {
                List<String> dataColumnExpected = commonFunction.getValueColumnOfFile(sourceFile, nameColumn);
                Collections.sort(dataColumnExpected);
                List<String> dataColumnActual = ContactListPageObj.getListColumnDataOnCurPage(nameColumn);
                Collections.sort(dataColumnActual);
                Assert.assertEquals("Value of column " + nameColumn + " display incorrectly on contact " + "page", dataColumnExpected, dataColumnActual);
            }
        } catch (Exception e) {
            Assert.fail("information contact display incorrectly on contact landing page");
        }
    }

    @Then("Hyper link is not create on total contact field of empty contact list")
    public void hyperLinkIsNotCreateOnTotalContactField() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertFalse("Hyper link created on total contact with number contact is 0" + "", ContactListPageObj.verifyHyperLink(testData.getData("contactlist", "name")));
    }

    @Then("Hyper link created on total contact field of contact list contain records")
    public void hyperLinkDisplayedOnTotalContactField() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Hyper link created on total contact with number contact more " + "than " + "0", ContactListPageObj.verifyHyperLink(testData.getString("contactlist", "name")));
    }

    @And("Click Hyper link on total contact with contact list")
    public void clickHyperLinkOnTotalContactWithContactListCreated() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Search and verify number contact more than 0 contact");
        this.hyperLinkDisplayedOnTotalContactField();
        log.info("Click hyperlink on total contact of contact list");
        commonFunction.tryClick(By.xpath(String.format(locator.getLocator("hyperlinkTotalContact"), testData.getString("contactlist", "name"))), 30);
        commonFunction.waitForPageLoadComplete(60, 1);
    }

    @Then("Contact page display after click Hyper link")
    public void contactPageDisplayAfterClickHyperLink() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Contact page is not display after click Hyper link", commonFunction.findElementByXpath(locator.getLocator("contactPageName")).isDisplayed());
    }


    @And("Get value total contact of contact list")
    public void getValueTotalContact() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        utilityFun.wait(3);
        commonFunction.clickButton("refresh");
        commonFunction.waitForPageLoadComplete(30, 1);
        String numberTotalContact = commonFunction.findElementByXpath(String.format(locator.getLocator("hyperlinkTotalContact"), testData.getString("contactlist", "name"))).getText();
        testData.put("Total Contact", numberTotalContact);
    }

    @Given("Create contact list with name {string} and description {string} by API")
    public void createContactListWithNameAndDescription(String contactListName, String description) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", contactListName);
        data.put("description", description);
        try {
            RestMethodsContactListObj.createContactList(data);
            utilityFun.wait(1);
            commonFunction.tryClick("refreshTable", 5);
        } catch (Exception e) {
            log.info("Can not click button refresh");
        }
    }

    @Then("Number contact displayed on contact page same with total contact on contact list page")
    public void numberContactDisplayedOnContactPageSameWithTotalContactOnContactListPage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int numberContactDisplay = commonFunction.countAllRecord();
        Assert.assertEquals("Number contact display on contact landing page incorrectly", numberContactDisplay, Integer.parseInt(testData.get("Total Contact")));
    }

    @Given("Go to Contact browser page of contact list")
    public void goToContactBrowser() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String ctlName = testData.getString("contactlist", "name");
        commonFunction.actionOnContact(ctlName, "View Contacts");
    }

    @Then("Sort all column on header and verify sort function is working")
    public void verifySortFunction() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> listColName = ContactListPageObj.getAllAttributeOnHeaderContact();
        for (String col : listColName) {
            if (col.contains("Created On") || col.contains("firstname") || col.contains("lastname") || col.equalsIgnoreCase("phonenumber")) {
                if (col.contains("Created On")) {
                    commonFunction.clickButton("refresh");
                    commonFunction.waitForPageLoadComplete(10, 1);
                    ContactListPageObj.clickSort(col, 1);
                }
                ContactListPageObj.verifySortFunctionOnEveryColumn(col);
            }
        }
    }

    @Then("Sort by descending order and verify that all page are applied follow this sort")
    public void verifyDescendingSortIsAppliedAllPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> listColName = ContactListPageObj.getAllAttributeOnHeaderContact();
        for (String col : listColName) {
            if (col.contains("Created On") || col.contains("firstname") || col.contains("lastname") || col.equalsIgnoreCase("phonenumber")) {

                if (col.contains("Created On")) {
                    ContactListPageObj.clickSort(col, 1);
                }
                ContactListPageObj.clickSort(col, 2);
                commonFunction.clickButton("nextPage");
                commonFunction.waitForPageLoadComplete(10, 1);
                List<String> listSort = ContactListPageObj.getListColumnDataOnCurPage(col);
                List<String> list = new ArrayList<>(listSort);
                Collections.sort(list, Collections.reverseOrder());
                log.info("listSort :" + listSort);
                log.info("list :" + list);
                Assert.assertEquals("This page is not sorted when sorting descending on other page on column: " + col, list, listSort);
                commonFunction.clickButton("refresh");
                commonFunction.waitForPageLoadComplete(10, 1);
            }
        }
    }

    @Then("Sort by ascending order and verify that all page are applied follow this sort")
    public void verifyAscendingSortIsAppliedAllPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> listColName = ContactListPageObj.getAllAttributeOnHeaderContact();
        for (String col : listColName) {
            if (col.contains("Created On") || col.contains("firstname") || col.contains("lastname") || col.equalsIgnoreCase("phonenumber")) {

                if (col.contains("Created On")) {
                    ContactListPageObj.clickSort(col, 1);
                }
                ContactListPageObj.clickSort(col, 1);
                commonFunction.clickButton("nextPage");
                commonFunction.waitForPageLoadComplete(10, 1);
                List<String> listSort = ContactListPageObj.getListColumnDataOnCurPage(col);
                List<String> list = new ArrayList<>(listSort);
                Collections.sort(list);
                log.info("listSort :" + listSort);
                log.info("list :" + list);
                Assert.assertEquals("This page is not sorted when sorting ascending on other page on column: " + col, list, listSort);
                commonFunction.clickButton("refresh");
                commonFunction.waitForPageLoadComplete(10, 1);
            }
        }
    }

    @When("Go to page {string} and click refresh button")
    public void goToPageAndClickRefresh(String pageNumber) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sendKeysToTextBox("goToPage", pageNumber);
        commonFunction.waitForPageLoadComplete(10, 1);
        commonFunction.clickButton("refresh");
        commonFunction.waitForPageLoadComplete(10, 1);
    }

    @Then("User should be backed to first page")
    public void userBackToFirstPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertEquals("User is not yet back to first page after clicking refresh button", "1", ContactListPageObj.getNumberPageIsSelected());
    }

    @When("Change page size to {string} values one by one and click refresh button then verify user is backed to default page size")
    public void changePageSizeAndClickRefresh(String defaultPageSizeList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] pageSizeValue = defaultPageSizeList.split("\\,");
        for (String size : pageSizeValue) {
            ContactListPageObj.selectPageSize(size);
            commonFunction.clickButton("refresh");
            Assert.assertEquals("User is not yet back to default page size after clicking refresh button", ContactListPageObj.getCurrentPageSize(), 10);
        }
    }

    @Given("Create a data source by API")
    public void createDatasourceByAPI() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.createDataSource(testData);
    }

    @When("Copy file and import datasource to contact list {string}")
    public void copyFileAndImportDataSource(String ctlName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + testData.get("sourceFile");
        log.info("Copy csv file to FTP Host");
        utilityFun.copyFile(testData.get("ftpIPHostName"), testData.get("ftpUserName"), testData.get("ftpPassword"), sourceFile, testData.get("ftpRemoteFilePath"));
        log.info("Run data source");
        commonFunction.basicSearchOnTable(ctlName);
        commonFunction.sleepInSec(0.5);
        commonFunction.clickAction(ctlName, "Import Contacts");
        commonFunction.sleepInSec(0.5);
        commonFunction.selectDropDownOptionEqual("dataSource", testData.get("name"));
        commonFunction.sleepInSec(0.5);
        commonFunction.clickButton("run");
        commonFunction.sleepInSec(0.5);
        log.info("Monitor import process status");
        ContactListPageObj.monitorImportProgress(ctlName, 60);
    }

    @Then("User should be backed to default sort option of column {string}")
    public void verifyUserIsBackToDefaultSort(String column) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertNull("Sort arrow up icon is still display on contact landing page", commonFunction.presentElement(By.xpath(String.format(locator.getLocator("iconSortArrowUp"), column)), 5));
        Assert.assertNull("Sort arrow down icon is still display on contact landing page", commonFunction.presentElement(By.xpath(String.format(locator.getLocator("iconSortArrowDown"), column)), 5));
    }

    @Then("Click sort on column {string} and click refresh button")
    public void clickSortAndClickRefreshPage(String colName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ContactListPageObj.clickSort(colName, 2);
        commonFunction.clickButton("refresh");
        commonFunction.waitForPageLoadComplete(10, 1);
    }

    @Given("Create a list of {int} contact lists start with name {string} and description {string}")
    public void createAContactListByAPI(int numberContactList, String nameRecord, String description) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", nameRecord);
        data.put("description", description);
        if (numberContactList > 0) {
            RestMethodsContactListObj.createContactList(data);
            if (numberContactList > 1) {
                String clNameDefault = data.get("name");
                for (int i = 1; i < numberContactList; i++) {
                    data.put("name", (clNameDefault + "_" + i));
                    RestMethodsContactListObj.createContactList(data);
                }
                data.put("name", clNameDefault);
            }
        }
    }

    @Given("Clean up data contact list {string} and datasource")
    public void cleanUpDataContactList(String ctlName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.deleteContactList(ctlName, "");
        RestMethodsContactListObj.deleteDataSource(testData.get("name"), "");
    }

    @Then("Verify records name is sort by createdOn \\(updatedOn) and in descending order")
    public void verifyRecordsNameIsByCreatedOnUpdatedOnAndInDescendingOrder() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        utilityFun.wait(1);
        List<String> clCreateOn = commonFunction.getListColumnDataOnCurPage("Created On");
        // 11/1/2022 2:59:04 AM
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Function<String, Date> convert = text -> {
            try {
                return format.parse(text);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        };
        List<Date> dates = clCreateOn.stream().map(convert).collect(Collectors.toList());
        log.info("dates: " + dates);
        List<Date> sortedDate = dates.stream().map(d -> (Date) d.clone()).sorted(Date::compareTo).collect(Collectors.toList());
        log.info("sortedDate: " + sortedDate);
        Assert.assertEquals(sortedDate, dates);
    }

    @When("Click to firstName column")
    public void clickToFirstNameColumn() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.tryClick(By.xpath(String.format(locator.getLocator("headerName"), "firstname")), 60);
    }

    @Then("Show \"count of current contacts\" instead of \"In progress\" for running import contacts in Total Contacts column")
    public void showCurrentContactCountForRunningImportContacts() {
        log.info(testData);
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> contactlist = testData.getData("contactlist");
        String listName = contactlist.get("name");
        int nameIndex = commonFunction.getColumnIndex("Name") + 1;
        int totalContactsIndex = commonFunction.getColumnIndex("Total Contacts") + 1;
        commonFunction.basicSearchOnTable(listName);

        String loc = "//tbody/tr[./td[" + nameIndex + "]//span[text()='" + listName + "']]/td[" + totalContactsIndex + "]//span";
        log.info("totalContactText location: " + loc);
        String totalContactText = commonFunction.findElementByXpath(loc).getText();
        log.info("totalContactText: " + totalContactText);
        Assert.assertNotEquals("Total Contacts still show `In Progress`", "In Progress", totalContactText);
    }

    @Given("contactlist csv having 750k records available on SFTP server")
    public void contactlist_csv_having_750k_records_available_on_sftp_server() {
        Map<String, String> datasource = testData.getData("datasource");
        Map<String, String> clist = testData.getData("contactlist");
        log.info(datasource);
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + datasource.get("sourceFile");
        String remotefile = EnvSetup.SFTP_REMOTE_FILEPATH + "/" + datasource.get("ftpRemoteFilePath");
        boolean validations = Boolean.parseBoolean(clist.get("validations"));
        int total = Integer.parseInt(clist.get("Last Failed")) + Integer.parseInt(clist.get("Last Success"));
        ContactListPageObj.createImportCSV(sourceFile, total, validations);
        log.info(sourceFile);

        utilityFun.copyFile(datasource.get("ftpIPHostName"), datasource.get("ftpUserName"), datasource.get("ftpPassword"), sourceFile, remotefile);
    }

    @Then("Change the time zone and verify the time in the Last Updated column is changed respectively")
    public void changeTheTimeZoneAndVerifyTheTimeInTheLastUpdatedColumnIsChangedRespectively(List<List<String>> timezones) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> timezone = timezones.get(0);
        ContactListPageObj.verifyTimeZoneForLastUpdate(timezone);
    }

    //==========================Advance search to contact list=========================================

    @And("Delete all contact list on landing page")
    public void deleteAllContactList() {
        RestMethodsContactListObj.deleteAllContactList();
    }

    @Then("Verify all option search are displayed")
    public void verifyAllOptionSearchAreDisplayed() {
        ContactListPageObj.verifyAllOptionSearchAreDisplayed();
    }

    @And("Name Column, Operator and Search field display on contact list landing page after click filter button")
    public void nameColumnOperatorAndSearchFieldDisplayOnContactListLandingPageAfterClickFilterButton() {
        ContactListPageObj.clickFilterButton();
        ContactListPageObj.verifyFilterComponentsDisplay();
    }

    @Then("Option advance search displayed with 1 contact list configured on contact list landing page")
    public void optionAdvanceSearchDisplayedOnContactListLandingPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Option advance search is not display on contact list landing page", commonFunction.isDisplayed("columnFilters"));
    }

    @When("Search contact list with operator and search text")
    public void searchContactListByTimeWithOperator() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String columnName = testData.getData("contactlist", "advanceSearchColumn");
        String operator = testData.getData("contactlist", "operator");
        String searchText = testData.getData("contactlist", "searchValue");
        commonFunction.applyAdvanceSearch(columnName, operator, searchText);
        commonFunction.waitForPageLoadComplete(30);
    }

    @Then("Verify search result displayed correctly with attribute last updated and operator Equal to")
    public void verifySearchResultDisplayedCorrectlyWithAttributeLastUpdatedAndOperatorEqualTo() {
    }

    @And("Clear text on Search Value text box")
    public void clearTextOnSearchValueTextBox() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.presentElement("searchValue", 3).clear();
        commonFunction.waitForPageLoadComplete(60, 2);
    }

    @Then("Full contact lists display on landing page after {string}")
    public void fullContactListsDisplayOnLandingPageAfterClearSearchText(String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int recordExpected = commonFunction.countAllRecord();
        if (action.equalsIgnoreCase("clear search text")) {
            commonFunction.findElementByDataID("searchValue").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            utilityFun.wait(3);
        } else if (action.equalsIgnoreCase("click refresh button")) {
            commonFunction.tryClick("refresh", 3);
            commonFunction.waitForPageLoadComplete(60, 1);
        }
        int recordActual = commonFunction.countAllRecord();
        log.info("Result advance search: " + recordExpected);
        log.info("All Record: " + recordActual);
        Assert.assertTrue("Search Value is not return full contact list when delete search", recordActual >= recordExpected);
    }

    @Then("No record show on contact list landing page")
    public void noRecordShowOnContactListLandingPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertEquals("There are records displayed on contact list landing page", 0, commonFunction.numberRecordOnCurrentPage());
    }

    @Then("User can view contact for contact list")
    public void userCanViewAndImportDataSourceForContactList() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameContactList = testData.getString("contactlist", "name");
        try {
            ContactListPageObj.clickThreeDotsBesideContactList(nameContactList);
            this.contactPageDisplayAfterClickViewContactOption();
        } catch (Exception e) {
            Assert.fail("User can not view contact of contact list when using advance search");
        }
    }

    @And("User can import contact for contact list searched")
    public void userCanImportContactForContactList() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameContactList = testData.getString("contactlist", "name");
        try {
            ContactListPageObj.clickThreeDotsBesideContactList(nameContactList);
            ContactListPageObj.importContactToList(testData.getData("datasource"), nameContactList);
            Assert.assertTrue("Import contact is not successful when using advanced search",
                    commonFunction.presentElements(locator.locatorFormat("hyperlinkTotalContact", nameContactList), 10).size() != 0);
        } catch (Exception e) {
            Assert.fail("User can not import contact of contact list when using advance search");
        }
    }

    @Then("Search contact list with attribute, operator, type input and value search")
    public void searchContactListWithAdvanceSearch() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String field = testData.getData("contactlist", "advanceSearchColumn");
        String operator = testData.getData("contactlist", "operator");
        String typeInput = testData.getData("contactlist", "typeInput");
        String contents = testData.getData("contactlist", "searchValue");
        commonFunction.advanceSearchFilter(field, operator, typeInput, contents);
    }

    @Then("Name and Last updated column only be used to advance search")
    public void verifyColumnIsUsedToAdvanceSearch(List<List<String>> columnToAdvanceSearch) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("columnFilters");
        utilityFun.wait(1);
        commonFunction.clickButton("columnName");
        Select select = new Select(commonFunction.findElementByDataID("columnName"));
        List<WebElement> listOptions = select.getOptions();
        List<String> strings = new ArrayList<>();
        for (WebElement e : listOptions) {
            strings.add(e.getText());
        }
        log.info("Expected list: " + columnToAdvanceSearch.get(0));
        log.info("Actual list: " + strings);
        Assert.assertEquals("List column names are supported to advance search on contact list page", columnToAdvanceSearch.get(0), strings);
    }


    @Then("Verify list {string} on dropdown display correctly for attribute {string}")
    public void valueOnDropdownDisplayCorrectlyWithAttributeLastUpdated(String allOperator, String colAdvanceSearch) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyOperatorDisplayCorrectWithAttribute(colAdvanceSearch, allOperator);
    }

    @And("Update input value for advance search {string} to {string} by {string}")
    public void updateSearchTextForAdvanceSearchTo(String colName, String newText, String inputTypes) {
        if (commonFunction.presentElement(By.xpath(locator.getLocator("emptyListMessage")), 3) == null) {
            if (commonFunction.getNumberPage() > 1) {
                commonFunction.navigateToPageByGoToPage(1);
                commonFunction.waitForPageLoadComplete(10, 1);
            }
        }
        if (colName.equalsIgnoreCase("name")) {
            commonFunction.sendKeysToTextBox("searchValue", newText);
            commonFunction.waitForPageLoadComplete(10, 1);
        } else {
            List<WebElement> contentsElement = commonFunction.findElementsByXpath(locator.getLocator("dateTextAdvanceSearch"));
            List<LocalDateTime> contentsList = commonFunction.extractContentToDateTime(newText);
            String[] type = inputTypes.split("\\,");
            for (int i = 0; i < contentsElement.size(); i++) {
                contentsElement.get(i).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                commonFunction.fillDateTimeField(type[i], contentsElement.get(i), contentsList.get(i));
            }
        }
    }

    @And("Update operator of advance search column {string}")
    public void updateOperatorForAdvanceSearchTo(String newOperator) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.selectDropDownOption(commonFunction.findElementByDataID("operator"), newOperator);
        commonFunction.waitForPageLoadComplete(30);
    }

    @And("Verify advance search result")
    @And("Verify advance search result after updating input value")
    @Then("Verify advance search result after updating operator")
    public void verifyAdvanceSearchResult() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyAdvanceSearchResult();
    }

    @When("Create a list contact lists with interval time")
    public void createListContactListWithIntervalTime() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> currentListCtlist = RestMethodsContactListObj.getListDataForEachColumnByAPI("name");
        Map<String, String> ctlData = testData.getData("contactlist");
        String originNames = ctlData.get("name");
        String originDescriptions = ctlData.get("description");
        String[] listCtl = originNames.split("\\|");
        String[] listDes = originDescriptions.split("\\|");
        int interval = Integer.parseInt(ctlData.getOrDefault("interval", "30"));
        for (int i = 0; i < listCtl.length; i++) {
            ctlData.put("name", listCtl[i]);
            ctlData.put("description", listDes[i]);
            log.info("testData : " + i + " : " + ctlData);
            if (!currentListCtlist.contains(ctlData.get("name"))) {
                RestMethodsContactListObj.deleteContactList(ctlData.get("name"), "");
                RestMethodsContactListObj.createContactList(ctlData);
                if (i != listCtl.length - 1) {
                    log.info("Waiting to: " + interval);
                    utilityFun.wait(interval);
                }
            }
        }
        ctlData.put("name", originNames);
        ctlData.put("description", originDescriptions);
    }

    @Then("Verify searched contact list length")
    public void verifySearchedcontactlistLength(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int iSearchedTextLength = commonFunction.getAttributeField("searchTable", "value").length();
        log.info("Expected Searched Text String<" + data.get("SearchStringLength") + ">");
        log.info("Actual Searched Text String length <" + iSearchedTextLength + ">");
        Assert.assertEquals(data.get("SearchStringLength").toString(), Integer.toString(iSearchedTextLength));
    }

    @Then("All field return default value after click refresh button")
    public void verifyValueReturnDefault() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyAdvancedSearchFieldBackToDefaultAfterClickRefreshButton();
    }

    @Then("Advance search for field {string} by {string} with {string} text {string} on contact list page an verify advance search result")
    public void advanceSearchAndVerifySearchResultOnContactListPage(String colName, String operator, String inputType, String searchText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> expectedList = new ArrayList<>();
        List<String> actualList = new ArrayList<>();
        if (colName.equalsIgnoreCase("name")) {
            expectedList = commonFunction.getExpectedListNameByOperator(operator, searchText);
            commonFunction.advanceSearchFilter(colName, operator, inputType, searchText);
            commonFunction.verifyAdvanceSearchForNameCol();
        } else {
            expectedList = commonFunction.getExpectedListLastUpdatedByOperator(operator, searchText);
            commonFunction.advanceSearchFilter(colName, operator, inputType, searchText);
            commonFunction.verifyAdvanceSearchForDateTimeCol();
        }
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

    @When("Advance search for field {string} by {string} with {string} text {string} on contact list page")
    public void advanceSearchForFieldByWithTextOnContactListPage(String colName, String operator, String inputType, String searchText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.advanceSearchFilter(colName, operator, inputType, searchText);
    }

    @Then("Clear search text and verify no failure toast message display")
    public void verifyNoToastMessageDisplayAfterClearingSearchText() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement inputBox = commonFunction.findElementByDataID("searchValue");
        inputBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        List<String> toastMessages = commonFunction.getListToastMessage();
        Assert.assertTrue("Display unexpected toast message", toastMessages.size() == 0);
    }

    @When("Changing {string} operator on the operator field")
    public void selectingOptionOnTheOperatorField(String newOperator) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.selectDropDownOption(commonFunction.findElementByDataID("operator"), newOperator);
    }

    @Then("Verify Search box is cleared with {string} column, {string} operator")
    public void verifySearchBoxIsClearedWithColumnOperator(String columnName, String operator) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (columnName.equalsIgnoreCase("Name")) {
            String searchText = commonFunction.findElementByDataID("searchValue").getAttribute("value");
            Assert.assertEquals("Search box wasn't cleared after changing new operator on the operator field", searchText, "");
        } else {
            List<WebElement> searchTextLoc = commonFunction.findElementsByXpath(locator.getLocator("datePicker"));
            List<String> searchBoxClearedLst = searchTextLoc.stream().map(item -> item.getAttribute("value")).filter(item -> item.equals("")).collect(Collectors.toList());
            if (operator.equalsIgnoreCase("between")) {
                Assert.assertEquals("Search box wasn't cleared after changing new operator on the operator field", searchBoxClearedLst.size(), 2);
            } else {
                Assert.assertEquals("Search box wasn't cleared after changing new operator on the operator field", searchBoxClearedLst.size(), 1);
            }
        }
    }

    @When("Changing the column name to {string}")
    public void changingTheColumnNameTo(String columnName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.selectDropDownOption(commonFunction.findElementByDataID("columnName"), columnName);
    }

    @Then("Verify that the Operator & Search box field are updated correctly after the colum name is changed to {string}")
    public void verifyThatTheOperatorSearchBoxFieldAreUpdatedCorrectlyAfterTheColumNameIsChangedTo(String columnName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String expOpertor = null;
        List<String> expOpertorLst = null;
        Select select = new Select(commonFunction.findElementByDataID("operator"));
        List<WebElement> listOptions = select.getOptions();
        String firstOption = select.getFirstSelectedOption().getText();
        List<String> actOperatorLst = new ArrayList<>();
        for (WebElement e : listOptions) {
            actOperatorLst.add(e.getText());
        }
        Collections.sort(actOperatorLst);
        log.info("Get list operator on ui: " + actOperatorLst);
        switch (columnName) {
            case "Name":
                Assert.assertTrue(firstOption.equals("="));
                expOpertor = "=,!=,In,Like,Not Like";
                expOpertorLst = Arrays.stream(expOpertor.split(",")).sorted().collect(Collectors.toList());
                Assert.assertTrue("Operator dropdown option is displayed incorrectly", expOpertorLst.equals(actOperatorLst));
                Assert.assertTrue(commonFunction.isDisplayed("searchValue", 5));
                Assert.assertTrue("Title search box is displayed incorrectly.Expected: Title search box is Search", commonFunction.isDisplayedByXpath(locator.getLocator("searchBoxTitle"), 5));
                break;

            case "Last Updated":
                Assert.assertTrue(firstOption.equals("<"));
                expOpertor = "<,>,Between";
                expOpertorLst = Arrays.stream(expOpertor.split(",")).sorted().collect(Collectors.toList());
                Assert.assertTrue("Operator dropdown option is displayed incorrectly", expOpertorLst.equals(actOperatorLst));
                Assert.assertTrue(commonFunction.isDisplayed("datepicker", 5));
                Assert.assertTrue("Title search box is displayed incorrectly. Expected: Title search box is Date", commonFunction.isDisplayedByXpath(locator.locatorFormat("columnFilterDateSection", "Date"), 5));
                break;
        }
    }

    @Then("Verify that toast message {string} will be display")
    public void verifyThatToastMessageWillBeDisplay(String toastMessage) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> toastMessages = commonFunction.getListToastMessage();
        String actualMessage = commonFunction.getToastMessage();
        Assert.assertTrue("Display unexpected toast message", toastMessages.size() == 1);
        log.info("Expected toast message is: " + actualMessage);
        Assert.assertTrue("Toast message not match as expected message", actualMessage.equalsIgnoreCase(toastMessage));
    }


    @Given("user create contact list")
    public void user_create_contact_list() {
        commonFunction.navigateToOutboundPage("contacts");
        log.info("Create contact List");
        log.info("Delete Contact list if available");
        RestMethodsContactListObj.deleteContactList(testData.getData("contactlist", "name"), "");
        ContactListPageObj.createContactList(testData.getData("contactlist"));
        ContactListPageObj.verifyNotification(testData.getData("contactlist"));

    }

    @Then("create contact list page is not closed and Attributes tab is enabled")
    public void create_contact_list_page_is_not_closed_and_attributes_tab_is_enabled() {

        WebElement attributeTab = commonFunction.findElementByDataID("tab-attributes");
        Assert.assertFalse("Attribute tab enable", commonFunction.verifyAttributeAvailable(attributeTab, "disabled"));
    }

    @Then("navigate to attribute tab")
    public void navigate_to_attribute_tab() {
        commonFunction.clickTab("tab-attributes");
    }

    @Then("User add new Contact Attribute")
    public void user_add_new_contact_attribute() {
        ContactListPageObj.addAttribute(testData.getData("contactAttribute"));
        ContactListPageObj.verifyNotification(testData.getData("contactAttribute"));
    }


    @Then("Verify new attributes displayed on list page")
    public void verify_new_attributes_displayed_on_list_page() {
        List<String> listAttributes = commonFunction.getListColumnData("Name");
        Assert.assertEquals("ERR!!!. New Attribure not displayed at top of the list attribute table", testData.getData("contactAttribute", "name"), listAttributes.get(0));
    }

    @Then("Verify new attribute information is correct")
    public void verify_new_attribute_information_is_correct() {
        ContactListPageObj.verifyAttributeTable(testData.getData("contactAttribute"));
    }

    @Then("Clean attribute testcase data")
    public void clean_attribute_testcase_data() {
        RestMethodsContactListObj.deleteAttributeContactList(testData.getStrict("contactlist", "name"), testData.getStrict("contactAttribute", "name"));
        RestMethodsContactListObj.deleteContactList(testData.getStrict("contactlist", "name"), "");
    }

    @Then("Verify cancel form with {string} action")
    public void verifyCancelForm(String action) {
        Map<String, String> attributeMap = testData.getData("contactAttribute");
        commonFunction.verifyCancelForm(attributeMap, action);
    }

    @Then("Verify cancel form when all values are not changed")
    public void verifyCancelFormNotChanged() {
        commonFunction.verifyPageLoaded(testData.getData("contactAttribute", "pageName"));
    }

    @When("Click to {string} Button")
    public void clickAction(String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton(action.toLowerCase());
        commonFunction.waitForPageLoadComplete(10, 2);
    }

    @Then("Verify Add New Attribute button is display")
    public void verify_add_new_attribute_button_is_display() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.isDisplayed("add", 2);
        commonFunction.isEnable("add");
    }

    @Given("User input information to create contact list without clicking save button")
    public void user_input_information_to_create_contact_list_without_clicking_save_button() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> contactlist = testData.getData("contactlist");
        commonFunction.navigateToOutboundPage("contacts");
        commonFunction.clickButton("add");
        commonFunction.sendKeysToTextBox("name", contactlist.get("name"));
        commonFunction.sendKeysToTextBox("description", contactlist.get("description"));
    }

    @Then("Verify Attributes tab is disable")
    public void verifyAttributeTabIsDisable() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement attributeTab = commonFunction.findElementByDataID("tab-attributes");
        Assert.assertTrue("Attribute tab enable", commonFunction.verifyAttributeAvailable(attributeTab, "disabled"));
    }


    @Then("User add new Contact Attribute with invalid value")
    public void userAddNewAttributeWithInvalidValue() {
        ContactListPageObj.addAttribute(testData.getData("contactAttribute"));
        ContactListPageObj.verifyFieldError("name-wrapper", testData.getData("contactAttribute", "NotificationAlert"));
    }

    @Then("Verify new attributes displayed is less than 30 characters")
    public void userAttributeNameIsNotOver30() {
        String nameData = testData.getData("contactAttribute", "name");
        String[] results = nameData.split("(?<=\\G.{30})");
        String name30Chars = results[0];
        Map<String, String> contactAttMap = testData.getData("contactAttribute");
        contactAttMap.put("name", name30Chars);
        List<String> listAttributes = commonFunction.getListColumnData("Name");
        Assert.assertEquals("ERR!!!. New Attribure not displayed at top of the list attribute table", contactAttMap.get("name"), listAttributes.get(0));
        ContactListPageObj.verifyAttributeTable(contactAttMap);
    }

    @Then("Verify the save button is disabled")
    public void verifyTheSaveButtonIsDisabled() {
        commonFunction.isDisplayed("save", 2);
        WebElement saveBtn = commonFunction.findElementByDataID("save");
        Boolean flag = commonFunction.verifyAttributeAvailable(saveBtn, "disabled");
        Assert.assertTrue("ERR!! Save button is not disabled", flag);
    }

    @Then("User input search string in basic search")
    public void user_input_search_string_in_basic_search() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.basicSearchOnTable(testData.getString("contactAttribute", "searchText"));
    }

    @Then("Verify basic search work as expected")
    public void verify_basic_search_work_as_expected() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> contactAttribute = testData.getData("contactAttribute");
        Assert.assertTrue("Basic search doesn't work", ContactListPageObj.verifyAttributeTableBasicSearch(contactAttribute));
    }

    @Then("Click to Clear Search button")
    public void clickToClearSearchButton() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("clearSearch");
    }

    @Then("Verify the search box should be clear and all data back to default")
    @Then("Verify data is refresh and back to default")
    public void verify_working_clear_search_and_refresh() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> currentList = RestMethodsContactListObj.getListAttributeName(testData.getString("contactlist", "name"));
        ContactListPageObj.verifyClearBasicSearchOrRefresh(currentList);
    }

    @When("Create a list of {int} consecutive attribute by name")
    public void createSequenceAttribute(int numAtt) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Map<String, String> data = new HashMap<String, String>();
        data.put("name", testData.getData("contactAttribute", "name"));
        data.put("dataType", testData.getData("contactAttribute", "dataType"));
        data.put("type", testData.getData("contactAttribute", "type"));
        String clsName = testData.getData("contactlist", "name");
        for (int i = 0; i < numAtt; i++) {
            data.put("name", testData.getData("contactAttribute", "name") + "_" + String.valueOf(i));
            RestMethodsContactListObj.createAttribute(data, clsName);
        }
        try {
            WebElement refresh = commonFunction.presentElement("refresh", 10);
            if (refresh != null)
                refresh.click();
        } catch (Exception e) {

        }
    }

    @Then("Verify expected contactLists on Contact List")
    public void verifyExpectedContactListsOnContactList(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyAdvanceSearchResult(data.get("ContactListNames"), true);
    }

    @Then("Verify Not contactLists on Contact List")
    public void verifyNotContactListsOnContactList(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyAdvanceSearchResult(data.get("ContactListNames"), false);

    }

    @When("Create a list of {int} consecutive attribute by data type")
    public void createSequenceAttributeByAttType(int numAtt) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] listDataType = testData.getString("contactAttribute", "dataType").split("\\|");
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", testData.getData("contactAttribute", "name"));
        data.put("dataType", listDataType[0]);
        data.put("type", testData.getData("contactAttribute", "type"));
        String clsName = testData.getData("contactlist", "name");
        for (int i = 0; i < numAtt; i++) {
            data.put("name", testData.getData("contactAttribute", "name") + "_" + String.valueOf(i));
            data.put("dataType", listDataType[i]);
            RestMethodsContactListObj.createAttribute(data, clsName);
        }
        try {
            WebElement refresh = commonFunction.presentElement("refresh", 10);
            if (refresh != null)
                refresh.click();
        } catch (Exception e) {

        }
    }

    @Then("User add new list Contact Attribute")
    public void userAddNewListContactAttributeByUI() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> attribute = new HashMap<>();
        attribute.putAll(testData.getData("contactAttribute"));
        String[] name = testData.getString("contactAttribute", "name").split("\\|");
        String[] dataType = testData.getString("contactAttribute", "dataType").split("\\|");
        for (int i = 0; i < name.length; i++) {
            attribute.put("name", name[i]);
            if (dataType.length > 1) {
                attribute.put("dataType", dataType[i]);
            }
            ContactListPageObj.addAttribute(attribute);
            commonFunction.waitForPageLoadComplete(10, 1);
            Assert.assertEquals("Message isn't displayed", attribute.get("NotificationAlert"), ContactListPageObj.getToastMessage());
        }
    }

    @Then("Clean list attribute testcase data")
    public void clean_list_attribute_testcase_data() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] name = testData.getString("contactAttribute", "name").split("\\|");
        for (int i = 0; i < name.length; i++) {
            log.info("Delete Attributes: " + name[i]);
            RestMethodsContactListObj.deleteAttributeContactList(testData.getData("contactlist", "name"), name[i]);
        }
        RestMethodsContactListObj.deleteContactList(testData.getData("contactlist", "name"), "");
    }

    @Then("User add new Contact Attribute with empty value")
    public void userAddNewContactAttributeWithEmptyValue() {
        ContactListPageObj.addAttribute(testData.getData("contactAttribute"));
        commonFunction.findElementByDataID("name").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        ContactListPageObj.verifyFieldError("name-wrapper", testData.getData("contactAttribute", "NotificationAlert"));
    }

    @Then("User add new Contact Attribute without saving")
    public void userAddNewContactAttributeWithoutSaving() {
        ContactListPageObj.addAttribute(testData.getData("contactAttribute"));
    }

    @Then("User clicks button {string} in dialog")
    public void userClicksButtonInDialog(String btnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButtonInDialog(btnName);
    }

    @Then("Refresh button is available on page")
    public void refreshButtonIsAvailableOnPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Refresh button isn't displayed", commonFunction.isDisplayed("refresh"));
        Assert.assertTrue("Refresh button isn't enable", commonFunction.isEnable("refresh"));

    }

    @Then("Verify page navigation with page size {string}")
    @Then("Verify pagination with page size {string}")
    public void verifyPaginationWithPageSize(String pageSize) {
        log.info("Verify pagination with page size: " + pageSize);
        commonFunction.verifyPageNavigation(pageSize);
    }

    @When("Check if page have {int} attributes")
    public void checkIfPageHaveAttributes(int numAtt) {
        int noOfAttr = RestMethodsContactListObj.getNumberAttContactList(testData.getData("contactlist", "name"));
        if (noOfAttr < numAtt) {
            log.info("Generate new Attribute for testing: " + (numAtt - noOfAttr));
            this.createSequenceAttribute(numAtt - noOfAttr);
        }
        ContactListPageObj.addAttribute(testData.getData("contactAttribute"));
    }

    @Then("Verify new the attributes created will have type {string}")
    public void verifyNewTheAttributesCreatedByUserWillHaveTypeUser(String type) {
        commonFunction.basicSearchOnTable(testData.getData("contactAttribute", "name"));
        Assert.assertEquals("ERR!!! Column Type String is not match", type, commonFunction.getSingleDataFromTable(testData.getData("contactAttribute", "name"), "Type"));
    }

    @Then("Verify dialog {string} is still visible")
    public void verifyDialogIsStillVisible(String dialogName) {
        commonFunction.dialogIsVisible(dialogName, 2);
    }


    @Then("Click to filter button")
    @Then("Click to filter button to disable")
    public void clickToFilterButton() {
        Assert.assertTrue("Filter button is not available", commonFunction.isDisplayed("columnFilters", 10));
        commonFunction.clickButton("columnFilters");
    }

    @Then("Apply Advanced search for field with operator and the value name and verify search result")
    public void applyAdvancedSearchForFieldWithOperatorAndTheValueIs(DataTable table) {
        for (Map<String, String> data : table.asMaps(String.class, String.class)) {
            String fieldName = data.get("Column");
            String operator = data.get("Operator");
            String inputValue = data.get("Value");
            commonFunction.applyAdvanceSearch(fieldName, operator, inputValue);
            verifyAdvanceSearchResult(fieldName);
        }
    }

    @Then("Apply Advanced search for field with operator and the value name and verify empty search result")
    public void applyAdvancedSearchForFieldWithOperatorAndTheValueIsempty(DataTable table) {
        for (Map<String, String> data : table.asMaps(String.class, String.class)) {
            String fieldName = data.get("Column");
            String operator = data.get("Operator");
            String inputValue = data.get("Value");
            commonFunction.applyAdvanceSearch(fieldName, operator, inputValue);
            verifyAdvanceSearchResultIsEmpty(fieldName);
        }
    }

    @Then("Verify Advance search options are not displayed")
    public void verifyAdvanceSearchOptionsAreNotDisplayed() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] sFields = {"columnName", "operator", "searchValue"};
        commonFunction.verifyFieldsNotDisplayedPage(sFields);
    }

    @Then("Verify system defined attributes displayed on list page")
    public void verify_system_defined_attributes_displayed_on_list_page() {
        ArrayList attlist = testData.getData("contactAttribute");
        for (Object attribute : attlist) {
            log.info("Verify System attribute Data -->" + (Map<String, String>) attribute);
            ContactListPageObj.verifyAttributeTable((Map<String, String>) attribute);
        }

    }

    @Then("User try to add new attribute with same name like system attribute with {string} {string} then verify message {string} displayed")
    public void user_try_to_add_new_attribute_with_same_name_like_system_attribute_with_and_verify_results(String name, String dataType, String message) {
        Map<String, String> attdata = new HashMap<String, String>();
        attdata.put("name", name);
        attdata.put("dataType", dataType);
        log.info("--------------------" + attdata);
        ContactListPageObj.addAttribute(attdata);
        ContactListPageObj.verifyFieldError("name-wrapper", message);
    }

    @Then("User try to add new attribute with same name like system attribute with then verify message displayed")
    public void user_try_to_add_new_attribute_with_same_name_like_system_attribute_with_and_verify_results(DataTable table) {

        for (Map<String, String> data : table.asMaps(String.class, String.class)) {

            log.info("--------------------" + data);
            ContactListPageObj.addAttribute(data);
            ContactListPageObj.verifyFieldError("name-wrapper", data.get("Message"));
        }
    }

    @When("Create a contact list and import datasource by API")
    @When("Create a contact list by API")
    public void createAContactListByAPI() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> contactListData = testData.getData("contactlist");
        RestMethodsContactListObj.deleteContactList(contactListData.get("name"), "");
        RestMethodsContactListObj.createContactList(contactListData);
        if (testData.containsKey("contactAttribute")) {
            ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
            String contactlistName = testData.getData("contactlist", "name");
            for (Map<String, String> attribute : attlist) {
                if (!attribute.get("type").contains("PREDEFINED")
                        && !attribute.get("type").equals("SYSTEM")
                        && !attribute.get("dataType").equals("SYSTEM")) {
                    RestMethodsContactListObj.createAttribute(attribute, contactlistName);
                }
            }
        }
        if (testData.containsKey("datasource")) {
            Map<String, String> dataSourceData = testData.getData("datasource");
            if (dataSourceData.get("ftpIPHostName").equalsIgnoreCase("usecommon")) {
                dataSourceData.put("ftpIPHostName", EnvSetup.SFTP_HOSTNAME);
                dataSourceData.put("ftpUserName", EnvSetup.SFTP_USERNAME);
                dataSourceData.put("ftpPassword", EnvSetup.SFTP_PASSWORD);
                dataSourceData.put("ftpRemoteFilePath", EnvSetup.SFTP_REMOTE_FILEPATH + "/" + dataSourceData.get("ftpRemoteFilePath"));
            }
            String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + dataSourceData.get("sourceFile");
            log.info(sourceFile);
            utilityFun.copyFile(dataSourceData.get("ftpIPHostName"), dataSourceData.get("ftpUserName"),
                    dataSourceData.get("ftpPassword"), sourceFile, dataSourceData.get("ftpRemoteFilePath"));
            RestMethodsContactListObj.deleteDataSource(dataSourceData.get("name"), "");
            RestMethodsContactListObj.createDataSource(dataSourceData);
            RestMethodsContactListObj.runDataSourceByAPI(contactListData.get("name"), dataSourceData.get("name"));
            utilityFun.wait(5);
        }
    }

    @When("Create a contact list by UI")
    public void createAContactListByUI() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> contactListData = testData.getData("contactlist");
        RestMethodsContactListObj.deleteContactList(contactListData.get("name"), "");
        ContactListPageObj.createContactList(contactListData.get("name"), contactListData.get("description"));
    }

    @When("Create multiple contact lists by API")
    public void createMultipleContactListsByAPI() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> ctlData = testData.getData("contactlist");
        String originNames = testData.getData("contactlist", "name");
        String originDescriptions = testData.getData("contactlist", "description");
        String[] contactListName = ctlData.get("name").split("\\|");
        String[] contactListDes = ctlData.get("description").split("\\|");
        String desCtl = "";
        if (!testData.containsKey("datasource")) {
            for (int i = 0; i < contactListName.length; i++) {
                ctlData.put("name", contactListName[i]);
                desCtl = contactListDes.length > i ? contactListDes[i] : desCtl;
                ctlData.put("description", desCtl);
                RestMethodsContactListObj.deleteContactList(ctlData.get("name"), "");
                RestMethodsContactListObj.createContactList(ctlData);
            }
        } else {
            Map<String, String> dsData = testData.getData("datasource");
            String dsName = "";
            String sourceFileName = "";
            String[] datasourceName = dsData.get("name").split("\\|");
            String[] sourceName = dsData.get("sourceFile").split("\\|");
            this.createMultipleDatasourcesByAPI();
            for (int i = 0; i < contactListName.length; i++) {
                ctlData.put("name", contactListName[i]);
                desCtl = contactListDes.length > i ? contactListDes[i] : desCtl;
                ctlData.put("description", desCtl);
                RestMethodsContactListObj.deleteContactList(ctlData.get("name"), "");
                RestMethodsContactListObj.createContactList(ctlData);
                //copy file and import ds
                dsName = datasourceName.length > i ? datasourceName[i] : dsName;
                sourceFileName = sourceName.length > i ? sourceName[i] : sourceFileName;
                String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + sourceFileName;
                log.info(sourceFile);
                utilityFun.copyFile(dsData.get("ftpIPHostName"), dsData.get("ftpUserName"),
                        dsData.get("ftpPassword"), sourceFile, dsData.get("ftpRemoteFilePath"));
                RestMethodsContactListObj.runDataSourceByAPI(ctlData.get("name"), dsName);
                utilityFun.wait(1);
            }
        }
        ctlData.put("name", originNames);
        ctlData.put("description", originDescriptions);
    }

    @When("Create a list {int} contact lists by API")
    public void createListContactListByAPI(int numberOfCtlist) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> ctlData = testData.getData("contactlist");
        String clNameDefault = ctlData.get("name");
        for (int i = 1; i <= numberOfCtlist; i++) {
            ctlData.put("name", (clNameDefault + "_" + i));
            RestMethodsContactListObj.deleteContactList(ctlData.get("name"), "");
            RestMethodsContactListObj.createContactList(ctlData);
        }
        if (testData.containsKey("datasource")) {
            Map<String, String> dsData = testData.getData("datasource");
            String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + dsData.get("sourceFile");
            log.info(sourceFile);
            utilityFun.copyFile(dsData.get("ftpIPHostName"), dsData.get("ftpUserName"),
                    dsData.get("ftpPassword"), sourceFile, dsData.get("ftpRemoteFilePath"));
            RestMethodsContactListObj.deleteDataSource(dsData.get("name"), "");
            RestMethodsContactListObj.createDataSource(dsData);
            for (int i = 1; i <= numberOfCtlist; i++) {
                RestMethodsContactListObj.runDataSourceByAPI(clNameDefault + "_" + i, dsData.get("name"));
                utilityFun.wait(1);
            }
        }
        ctlData.put("name", clNameDefault);
    }

    @When("Create a datasource by API")
    public void createADatasourceByAPI() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsContactListObj.deleteDataSource(testData.getData("datasource", "name"), "");
        RestMethodsContactListObj.createDataSource(testData.getData("datasource"));
    }

    @When("Create a datasource by UI")
    public void createADatasourceByUI() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> data = testData.getData("datasource");
        if (data.get("ftpIPHostName").equalsIgnoreCase("usecommon")) {
            data.put("ftpIPHostName", EnvSetup.SFTP_HOSTNAME);
            data.put("ftpUserName", EnvSetup.SFTP_USERNAME);
            data.put("ftpPassword", EnvSetup.SFTP_PASSWORD);
            data.put("ftpRemoteFilePath", EnvSetup.SFTP_REMOTE_FILEPATH + "/" + data.get("ftpRemoteFilePath"));
        }
        RestMethodsContactListObj.deleteDataSource(data.get("name"), "");
        commonFunction.navigateToOutboundPage("datasource");
        DataSourcePageObj.addFTPdataSourceN(data);
        Map<String, String> confirmMessage = new HashMap<>();
        confirmMessage.put("NotificationAlert", data.get("NotificationAlert"));
        commonFunction.verifyNotification(confirmMessage);
    }

    @When("Create multiple datasources by API")
    public void createMultipleDatasourcesByAPI() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> data = testData.getData("datasource");
        String[] nameDsData = data.get("name").split("\\|");
        String[] desDsData = data.get("description").split("\\|");
        String[] fileXpathData = data.get("ftpRemoteFilePath").split("\\|");
        String[] sourceData = data.get("sourceFile").split("\\|");
        String description = "";
        String sourceFileName = "";
        String ftpRemoteFilePath = "";
        for (int i = 0; i < nameDsData.length; i++) {
            data.put("name", nameDsData[i]);
            description = desDsData.length > i ? desDsData[i] : description;
            data.put("description", description);
            sourceFileName = sourceData.length > i ? sourceData[i] : sourceFileName;
            data.put("sourceFile", sourceFileName);
            ftpRemoteFilePath = fileXpathData.length > i ? fileXpathData[i] : ftpRemoteFilePath;
            data.put("ftpRemoteFilePath", ftpRemoteFilePath);
            RestMethodsContactListObj.deleteDataSource(nameDsData[i], "");
            RestMethodsContactListObj.createDataSource(data);
        }
    }

    @When("Create a list {int} datasource by API")
    public void createListDatasourceByAPI(int numberOfDs) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> data = testData.getData("datasource");
        String nameDsDefault = data.get("name");
        for (int i = 1; i <= numberOfDs; i++) {
            data.put("name", (nameDsDefault + "_" + i));
            RestMethodsContactListObj.deleteDataSource(nameDsDefault + "_" + i, "");
            RestMethodsContactListObj.createDataSource(data);
        }
        data.put("name", nameDsDefault);
    }

    @When("Clean up contact list data for testing")
    public void deleteContactListsByAPI() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] contactListNames = testData.getString("contactlist", "name").split("\\|");
        if (testData.containsKey("datasource")) {
            deleteDatasourcesByAPI();
        }
        for (int i = 0; i < contactListNames.length; i++) {
            RestMethodsContactListObj.deleteAllContactList(contactListNames[i]);
        }
    }

    @When("Clean up datasource data for testing")
    public void deleteDatasourcesByAPI() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] datasourceNames = testData.getString("datasource", "name").split("\\|");
        for (int i = 0; i < datasourceNames.length; i++) {
            RestMethodsContactListObj.deleteAllDatasource(datasourceNames[i]);
        }
    }

    @And("Search contact list with name")
    public void searchContactList() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String searchText = testData.getData("contactlist", "searchText");
        commonFunction.basicSearchOnTable(searchText);
    }

    @Then("Verify user is in Attribute langding page of contact list added")
    public void verifyUserIsInAttributeLangdingPage() {
        commonFunction.verifyPageLoaded(testData.getData("contactlist", "name"));
    }


    @Then("Verify advance search result by {string}")
    public void verifyAdvanceSearchResult(String colName) {
        String colNameSearch = new Select(commonFunction.findElementByDataID("columnName")).getFirstSelectedOption().getText();
        Assert.assertEquals("Column name is incorrect", colName, colNameSearch);
        switch (colNameSearch) {
            case "Name":
                ContactListPageObj.verifyAdvSearchResult();
                break;
            case "Last Updated":
                commonFunction.verifyAdvanceSearchForDateTimeCol();
                break;
            default:
                log.info("Field inputted not exist for advance search");
                Assert.fail("Field inputted not exist for advance search");
                break;
        }
    }

    @Then("Verify {string} child attributes displayed on list page")
    public void verify_phone_child_attributes_displayed_on_list_page(String attChild) {
        ArrayList attlist = new ArrayList();
        commonFunction.basicSearchOnTable(testData.getData("contactAttribute", "name"));
        if (attChild.toLowerCase().equals("phone")) {
            attlist = testData.getData("PhoneChildAttribute");
            Assert.assertEquals("ERR!!!. Number of Pre-Defined attribute for Phone is not corrected", attlist.size() + 1, commonFunction.numberRecordOnCurrentPage());
        } else if (attChild.toLowerCase().equals("email")) {
            attlist = testData.getData("EmailChildAttribute");
            Assert.assertEquals("ERR!!!. Number of Pre-Defined attribute for Email is not corrected", attlist.size() + 1, commonFunction.numberRecordOnCurrentPage());
        } else if (attChild.toLowerCase().equals("zip")) {
            attlist = testData.getData("ZipChildAttribute");
            Assert.assertEquals("ERR!!!. Number of Pre-Defined attribute for Zip is not corrected", attlist.size() + 1, commonFunction.numberRecordOnCurrentPage());
        } else {
            Assert.fail("ERR!!!. Please recheck whether attribute type is not supported: " + attChild);
        }
        String attName = testData.getStrict("contactAttribute", "name");
        for (Object attribute : attlist) {
            Map<String, String> childAttData = (Map<String, String>) attribute;
            String tmpattName = attName + childAttData.get("name");
            childAttData.put("name", tmpattName);
            log.info("Verify Predefined attribute Data -->" + childAttData);
            ContactListPageObj.verifyAttributeTable(childAttData);
        }

    }

    @Then("Verify advance search result by {string} with empty result")
    public void verifyAdvanceSearchResultIsEmpty(String colName) {
        String colNameSearch = new Select(commonFunction.findElementByDataID("columnName")).getFirstSelectedOption().getText();
        Assert.assertEquals("Column name is incorrect", colName, colNameSearch);
        switch (colNameSearch) {
            case "Name":
                ContactListPageObj.verifyAdvSearchResultIsEmpty();
                break;
            case "Last Updated":
                commonFunction.verifyAdvanceSearchForDateTimeCol();
                break;
            default:
                log.info("Field inputted not exist for advance search");
                Assert.fail("Field inputted not exist for advance search");
                break;
        }
    }

    @Then("User add new Contact Attribute with Value character")
    public void userAddAttWithSpecialCharacter(DataTable table) {
        Map<String, String> attData = testData.getData("contactAttribute");
        for (Map<String, String> data : table.asMaps(String.class, String.class)) {
            log.info(data);
            String singlechar = data.get("Value");
            attData.put("name", singlechar);
            ContactListPageObj.addAttribute(attData);
            ContactListPageObj.verifyFieldError("name-wrapper", testData.getData("contactAttribute", "NotificationAlert"));
        }
    }


    @When("Search contact list with attribute {string}, operator {string}, type input {string} and value search {string}")
    public void searchContactListWithAttributeOperatorTypeInputAndValueSearch(String field, String operator, String typeInput, String contents) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.advanceSearchFilter(field, operator, typeInput, contents);
    }

    @Then("User try to add new phone attribute however its associate attribute is matching with one existing attribute then verify message displayed")
    public void user_try_to_add_new_phone_attribute_however_its_associate_attribute_is_matching_with_one_existing_attribute_then_verify_message_displayed(DataTable table) {
        for (Map<String, String> data : table.asMaps(String.class, String.class)) {
            log.info(data);
            log.info("Add associate attribute");
            Map<String, String> attdata = new HashMap<String, String>();
            String childAttName = data.get("AttributeName") + data.get("ChildAttributeName");
            attdata.put("name", childAttName);
            attdata.put("dataType", data.get("ChildAttributeDataType"));
            log.info(attdata);
            ContactListPageObj.addAttribute(attdata);
            ContactListPageObj.verifyNotification(testData.getData("contactAttribute"));
            log.info("Add parent attribute");
            attdata.put("name", data.get("AttributeName"));
            attdata.put("dataType", data.get("DataType"));
            log.info(attdata);
            ContactListPageObj.addAttribute(attdata);
            if (data.get("Message").contains("already exists")) {

                ContactListPageObj.verifyFieldError("name-wrapper", data.get("Message"));
            } else {
                ContactListPageObj.verifyNotification(testData.getData("contactAttribute"));
            }
        }
    }

    @Then("User try to add new {string} attribute when predefined attribute is matching with one existing attribute then verify message displayed")
    public void user_try_to_add_new_non_phone_attribute_when_predefined_attribute_is_matching_with_one_existing_attribute_then_verify_message_displayed(String type, DataTable table) {
        user_try_to_add_new_phone_attribute_however_its_associate_attribute_is_matching_with_one_existing_attribute_then_verify_message_displayed(table);
    }

    @Then("Clean consecutive attribute testcase data")
    public void clean_consecutive_attribute_testcase_data() {
        log.info("Delete Contact list and Attributes");
        String ClistName = testData.getStrict("contactlist", "name");
        RestMethodsContactListObj.deleteAttributeContactList(ClistName, testData.getStrict("contactAttribute", "name"));
        List<String> listAttr = RestMethodsContactListObj.getListAttributeName(ClistName);
        for (String element : listAttr) {
            if (element.contains(testData.getStrict("contactAttribute", "name"))) {
                log.info("Delete Attributes: " + element);
                RestMethodsContactListObj.deleteAttributeContactList(ClistName, element);
            }
        }
        RestMethodsContactListObj.deleteContactList(ClistName, "");
    }

    @Given("User add attributes")
    public void userAddAttributes() {
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        for (Map<String, String> attribute : attlist) {
            ContactListPageObj.addAttribute(attribute);
            commonFunction.waitForPageLoadComplete(10, 1);
        }
    }

    @Then("Clean the testcase data")
    public void cleanTheData() {
        log.info("Delete attributes");
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        for (Map<String, String> attrData : attlist) {
            RestMethodsContactListObj.deleteAttributeContactList(testData.getData("contactlist", "name"), attrData.get("name"));
        }
        log.info("Delete contact list");
        RestMethodsContactListObj.deleteContactList(testData.getStrict("contactlist", "name"), "");
    }

    @Then("Required user attributes created in contact list")
    public void createAttributesByAPI() {
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        String contactlistName = testData.getData("contactlist", "name");
        for (Map<String, String> attribute : attlist) {
            if (!attribute.get("dataType").equals("SYSTEM")) {
                RestMethodsContactListObj.createAttribute(attribute, contactlistName);
            }
        }
    }

    @Then("Verify all user/system attributes are displayed")
    public void verifyAllAttributesDisplayed() {
        Map<String, String> contactlist = testData.getData("contactlist");
        ContactListPageObj.actionOnContact(contactlist.get("name"), "View Contacts");
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        for (Map<String, String> attributeE : attlist) {
            ContactListPageObj.verifyHeaderLabelDisplay(attributeE.get("name"));
        }
    }

    @Then("Verify all attributes are sorted by name")
    public void verifyAllAttributesAreSortedByName() {
        Map<String, String> contactlist = testData.getData("contactlist");
        ContactListPageObj.actionOnContact(contactlist.get("name"), "View Contacts");
        ArrayList<String> listEleActual = ContactListPageObj.getAllHeaders();
        ArrayList<String> listEleExpcted = new ArrayList<>();
        String[] listIgnore = testData.getData("contactlist", "AttributesToIgnoreFromVerification").toString().split(",");
        for (String attIgnore : listIgnore) {
            listEleActual.removeIf(att -> att.contentEquals(attIgnore));
        }
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        for (Map<String, String> attributeE : attlist) {
            listEleExpcted.add(attributeE.get("name"));
        }
        Collections.sort(listEleExpcted);
        log.info("listEleActual: " + listEleActual);
        log.info("listEleExpcted: " + listEleExpcted);
        Assert.assertEquals("List size is not same", listEleActual.size(), listEleExpcted.size());
        int i = 0;
        for (String eleExp : listEleExpcted) {
            Assert.assertEquals("Value sorted not match", eleExp, listEleActual.get(i));
            i++;
        }
    }

    @Then("Verify the error icon is displayed with failed reason")
    public void verifyTheErrorIconIsDisplayedWithFailedReason() {
        Map<String, String> contactlist = testData.getData("contactlist");
        ContactListPageObj.navigateToOutboundPage("contacts");
        Assert.assertTrue("Error icon visible", ContactListPageObj.isDisplayedElementByXpath(locator.getLocator("errorIcon")));
        ContactListPageObj.verifytoolTip(contactlist.get("name"), contactlist.get("ImportJobAlert"), true, contactlist.get("VerifyHeader") != null);
    }

    @Then("click on edit contact")
    public void click_on_edit_contact() {
        commonFunction.clickAction(testData.getData("EditContact", "EditContactRecord"), "Edit");

    }

    @Then("edit the contact fields")
    public void edit_the_contact_fields() {
        ContactListPageObj.editcontact(testData);

    }

    @Then("verify contact is edited successfully")
    public void verify_contact_is_edited_successfully() {

        ContactListPageObj.verifyEditedContact(testData);
    }

    @Then("Verify System attributes should not be shown for modification")
    public void verify_system_attributes_should_not_be_shown_for_modification() {
        log.info("Verify System attributes should not be shown for modification");
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        String contactlistName = testData.getData("contactlist", "name");
        for (Map<String, String> attribute : attlist) {
            if (attribute.get("type").equals("SYSTEM")) {
                log.info("Verify system attribute not displayed on UI: " + attribute.get("name"));
                Assert.assertFalse("ERR!!!. System attribute displayed on UI", commonFunction.isDisplayedAriaLabel(attribute.get("name"), 5));
            }
        }
    }

    @Then("Verify edit form should be available to user to modify the contact")
    public void verify_edit_form_should_be_available_to_user_to_modify_the_contact() {
        log.info("Verify page navigate to edit contact page");
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        for (Map<String, String> attribute : attlist) {
            if (!attribute.get("type").equals("SYSTEM")) {
                log.info("Verify attribute field displayed on UI for edit: " + attribute);
                Assert.assertTrue("", commonFunction.isDisplayed(attribute.get("name"), 5));
            }
        }
    }

    @Then("Verify the save and cancel button is available")
    public void verifyTheSaveAndCancelButtonIsAvailable() {
        Assert.assertTrue("Save button is not available", ContactListPageObj.isDisplayed("save", 5));
        Assert.assertTrue("Cancel button is not available", ContactListPageObj.isDisplayed("cancel", 5));
    }

    @Then("Verify cancel form on edit contacts page with {string} action")
    public void verifyCancelFormOnEditContactsPageWithAction(String action) {
        Map<String, String> editContactMap = testData.getData("EditContact");
        commonFunction.verifyCancelForm(editContactMap, action);
    }

    @Then("verify attributes are grouped by user phone email and zip")
    public void verify_attributes_displayed_in_group() {
        ContactListPageObj.verifyContactFieldGrouped(testData);

    }

    @Then("click on edit contact for edited contact")
    public void click_on_edit_contact_verify() {
        commonFunction.clickAction(testData.getData("EditContact", "EditValue"), "Edit");

    }

}


