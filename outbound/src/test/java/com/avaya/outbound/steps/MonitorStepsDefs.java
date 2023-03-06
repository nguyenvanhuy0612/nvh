package com.avaya.outbound.steps;

import com.avaya.outbound.frame.MonitorPage;
import com.avaya.outbound.frame.RestMethods;
import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
import com.google.gson.JsonObject;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MonitorStepsDefs extends StepContext {
    public Locator locator = new Locator(MonitorPage.class);

    public MonitorStepsDefs(Context context) {
        context.init(this);
    }

    @After(order = 9001, value = "@Monitor")
    public void afterSupervisorDashboard(Scenario scenario) {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
        RestMethodsObj.deleteAllCampaign(testData.getString("campaign", "name"));
        RestMethodsObj.deleteAllStrategies(testData.getString("campaign", "strategy"));
        RestMethodsContactListObj.deleteContactList(testData.getString("campaign", "contactList"), "");
        RestMethodsContactListObj.deleteDataSource(testData.getString("datasource", "name"), "");
        RestMethodsObj.deleteAllStrategies(testData.getString("strategy", "name"));
        RestMethodsContactListObj.deleteContactList(testData.getString("contactlist", "name"), "");
    }

    @Given("The Active Campaign monitor URL is hit")
    public void theActiveCampaignMonitorURLIsHit() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("The Active Campaign monitor URL is hit");
        commonFunction.navigateToOutboundPage("active-campaigns");
        Assert.assertEquals("ERR!!!. URL is not correct for Outbound Admin page", "Active Campaigns", commonFunction.findElementByDataID("page").getText());
    }

    @Then("Basic search display on Monitor active Campaign page")
    public void basicSearchDisplayOnMonitorActiveCampaignPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Basic search is not display on Active Campaign page", commonFunction.isDisplayed("searchTable"));
    }

    @And("Basic search works properly on Monitor active Campaign page")
    public void basicSearchWorksProperlyOnMonitorActiveCampaignPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Basic search works incorrectly on active Campaign page", commonFunction.verifyBasicSearchWorkCorrect(
                testData.getData("campaign", "name") + "_1", "Name"));
    }

    @Given("Stop {int} campaign campaign using API")
    public void stopnumberCampaign(int numberCam) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameCam = testData.getString("campaign", "name");
        if (numberCam == 1) {
            RestMethodsObj.stopCampaignByAPI(nameCam, "");
            String status = RestMethodsObj.getAttributeValueOfJobCampaign(nameCam, "status");
            boolean state = List.of("completed", "stopping").contains(status.toLowerCase());
            Assert.assertTrue("Campaign can not stop when user using API", state);
        } else {
            for (int i = 1; i <= numberCam; i++) {
                String status = RestMethodsObj.getAttributeValueOfJobCampaign(nameCam + "_" + i, "status");
                if (status.equalsIgnoreCase("stopping") || status.equalsIgnoreCase("completed")) {
                    continue;
                }
                RestMethodsObj.stopCampaignByAPI(nameCam + "_" + i, "");
            }
            commonFunction.sleepInSec(0.5);
            for (int i = 1; i <= numberCam; i++) {
                String status = RestMethodsObj.getAttributeValueOfJobCampaign(nameCam + "_" + i, "status");
                boolean state = List.of("completed", "stopping").contains(status.toLowerCase());
                Assert.assertTrue("Campaign can not stop when user using API", state);
            }
        }
    }

    @And("Stop all campaigns for testing using API")
    public void stopAllCampaignsUsingAPI() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsObj.stopAllCampaigns(testData.getString("campaign", "name"));
    }

    @Given("Stop {int} campaign campaign using API and waiting campaigns stop succesful")
    public void stopNumberCampaignAndWaitCompletedStopping(int numberCam) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameCam = testData.getString("campaign", "name");
        for (int i = 1; i <= numberCam; i++) {
            String status = RestMethodsObj.getAttributeValueOfJobCampaign(nameCam + "_" + i, "status");
            if (status.equalsIgnoreCase("stopping") || status.equalsIgnoreCase("completed")) {
                continue;
            }
            RestMethodsObj.stopCampaignByAPI(nameCam + "_" + i, "");
        }
        commonFunction.sleepInSec(0.5);
        for (int i = 1; i <= numberCam; i++) {
            String status = RestMethodsObj.getAttributeValueOfJobCampaign(nameCam + "_" + i, "status");
            boolean state = List.of("completed", "stopping").contains(status.toLowerCase());
            Assert.assertTrue("Campaign can not stop when user using API", state);
            commonFunction.monitorCampaignTillCompleted(nameCam + "_" + i, 100);
        }
    }

    @Given("Stop {int} campaign and {string} monitor stop successful")
    public void stopMultiCampaign(int numberCam, String confirm) {
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
        Assert.assertTrue("Number campaign want to stop larger than current campaign on Page", numberCam <= campaignIDs.size());
        for (int i = 0; i < numberCam; i++) {
            String status = RestMethodsObj.getAttributeValueOfJobCampaign(campaignNames.get(i), "status");
            if (status.equalsIgnoreCase("stopping") || status.equalsIgnoreCase("completed")) {
                continue;
            }
            RestMethodsObj.stopCampaignByAPI(campaignNames.get(i), campaignIDs.get(i));
            commonFunction.sleepInSec(0.5);
            status = RestMethodsObj.getAttributeValueOfJobCampaign(campaignNames.get(i), "status");
            boolean state = List.of("completed", "stopping").contains(status.toLowerCase());
            Assert.assertTrue("Campaign can not stop when user using API", state);
            if (confirm.equalsIgnoreCase("yes")) {
                commonFunction.monitorCampaignTillCompleted(campaignNames.get(i), 100);
            }
        }
    }

    @Then("Active Campaign statistics display correctly with status campaign running, other and total")
    public void verifyActiveCampaignDisplayCorrectly() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.refreshPage(10);
        Map<String, Integer> activeCampaigns = MonitorPageObj.activeCampaigns();
        String statisticsExpected = "Total:" + activeCampaigns.get("total") + "Running:" + activeCampaigns.get("running") +
                "Paused:" + activeCampaigns.get("paused") + "Others:" + activeCampaigns.get("other");
        String statisticsActual = commonFunction.presentElement(By.xpath(locator.getLocator("campaignSummary")), 20).getText().replace("\n", "");
        log.info("Actual result: " + statisticsActual);
        log.info("Expected result: " + statisticsExpected);
        Assert.assertTrue("Active campaign static is not display correctly with actual: " + statisticsActual + "but " +
                "expected: " + statisticsExpected, statisticsExpected.contentEquals(statisticsActual));
    }

    @When("The campaign is sorted base on Start Time")
    public void theCampaignIsSortedBaseOnStartTime() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/dd/yyyy h:mm:ss a");
        commonFunction.search(testData.getData("campaign", "name"));
        List<String> listStringTime = commonFunction.getListColumnDataOnAllPage("Start Time");
        Assert.assertTrue("While sorting column user is not having more than 1 campaign on page", listStringTime.size() > 1);
        Assert.assertTrue("Default sort display is not correctly on active campaign page", commonFunction.verifySortByDatetime(listStringTime, "descending"));
    }

    @Then("User clicks on header column {string}, the sorting icons change with each click")
    public void userClicksOnHeaderColumnAndSortIconsShowCorrectlyWithEachClick(String column) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Column user want click is: " + column);
        // verify without any click, default is not any icon display
        Assert.assertNull("Sort arrow up icon is still display on landing page",
                commonFunction.presentElement(locator.get("sortIconArrowUp"), 1));
        Assert.assertNull("Sort arrow down icon is still display on landing page",
                commonFunction.presentElement(locator.get("sortIconArrowDown"), 1));
        // verify with single click, ArrowUp icon should be displayed, ArrowDown is not
        commonFunction.clickSort(column, 1);
        Assert.assertNotNull("Sort arrow up icon is not display on landing page",
                commonFunction.presentElement(locator.get("sortIconArrowUp"), 2));
        Assert.assertNull("Sort arrow down icon is display on landing page, but expected after 1 click to show only ArrowUp icon",
                commonFunction.presentElement(locator.get("sortIconArrowDown"), 2));
        // verify with single next click, ArrowUp icon should be not displayed, ArrowDown is display
        commonFunction.clickSort(column, 1);
        Assert.assertNull("Sort icon up arrow is still displayed on landing page, but expected after next click to show only Arrow Down icon",
                commonFunction.presentElement(locator.get("sortIconArrowUp"), 2));
        Assert.assertNotNull("Sort icon down arrow is not display on landing page",
                commonFunction.presentElement(locator.get("sortIconArrowDown"), 2));
        // verify with 3rd click, restore back to default
        commonFunction.clickSort(column, 1);
        Assert.assertNull("Sort arrow up icon is still display on landing page",
                commonFunction.presentElement(locator.get("sortIconArrowUp"), 1));
        Assert.assertNull("Sort arrow down icon is still display on landing page",
                commonFunction.presentElement(locator.get("sortIconArrowDown"), 1));
    }

    @Then("The sort function works correctly when the user tries to click the {string} column a few times")
    public void theSortFunctionWorksCorrectlyWhenTheUserTriesToClickTheColumnAFewTimes(String column) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (column.equalsIgnoreCase("Start Time")) {
            commonFunction.verifySortDateTimeColumn(column);
        } else if (column.equalsIgnoreCase("Status")) {
            commonFunction.search(testData.getString("campaign", "name"));
            commonFunction.verifySortingCurrentPage("Status", List.of("Status", "Name"));
        } else {
            commonFunction.search(testData.getString("campaign", "name"));
            commonFunction.verifySortingCurrentPage(column, "Ascending");
            commonFunction.search(testData.getString("campaign", "name"));
            commonFunction.verifySortingCurrentPage(column, "Descending");
            commonFunction.search(testData.getString("campaign", "name"));
            commonFunction.verifySortingCurrentPage(column, "Default");
        }
    }

    @Then("The user is implementing sorting with the {string} Column, going to each page and verifying that the sort still works for each page")
    public void theUserIsImplementingSortingWithTheColumnGoingToEachPageAndVerifyingThatTheSortStillWorksForEachPage(String column) {
        if (column.equalsIgnoreCase("Start Time")) {
            commonFunction.verifySortWorkProperlyAcrossAllPageOfDateTimeCoumn(column);
        } else {
            commonFunction.basicSearchOnTable(testData.getString("campaign", "name"));
            boolean result = ContactListPageObj.verifySortWorkProperlyWithDifferentPage(column);
            Assert.assertTrue("Sort is not work correctly with different page", result);
        }
    }

    @Then("The campaign is displayed on active campaign page when the campaign is {string}")
    public void verifyActiveCampaignDisplayOnActiveCampaignPage(String status) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameCamp = testData.getData("campaign", "name");
        commonFunction.search(nameCamp);
        Assert.assertTrue("Status display incorrectly with expected", RestMethodsObj.getAttributeValueOfJobCampaign(nameCamp,
                "status").equalsIgnoreCase(status));
        List<String> campaignNames = commonFunction.getListColumnDataOnAllPage("Name");
        Assert.assertTrue("The Active campaign page is not display campaign with campaign " + status, campaignNames.contains(nameCamp));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");
        String timeString = commonFunction.getSingleDataFromTable(nameCamp, "Start Time");
        LocalDateTime startCampaignTimeActual = LocalDateTime.parse(timeString.replace("\u202F", " "), dateTimeFormatter);
        LocalDateTime startCampaignTimeExpected = RestMethodsObj.getTimeForRunCampaign(nameCamp, "startedOn");
        Duration duration = Duration.between(startCampaignTimeExpected, startCampaignTimeActual);
        log.info("Actual start campaign time: " + startCampaignTimeActual);
        log.info("Expected start campaign time: " + startCampaignTimeExpected);
        Assert.assertTrue("Start campaign display incorrectly on active campaign page", Math.abs(duration.toSeconds()) <= 1);
    }

    @Then("Active campaigns view title is displayed correctly")
    public void activeCampaignsViewTitleIsDisplayedCorrectly() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Active campaigns view is not display correctly", commonFunction.isDisplayedAriaLabel("Active Campaigns", 10));
    }

    @And("There is a empty message {string}")
    public void thereIsAEmptyMessage(String emptyMessage) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyMessageWithNoRecord(emptyMessage);
    }

    @And("The Active Campaign page displays correctly with all header table")
    public void theActiveCampaignPageDisplaysATableWithHeaderInclude() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String tableHeader = testData.getString("headerTable");
        String[] headers = tableHeader.split(",");
        for (String header : headers) {
            commonFunction.verifyHeaderLabelDisplay(header.trim());
        }
    }

    @And("The Active Campaign page displays basic search field, Refresh icon, Filter icon, summary section")
    public void theActiveCampaignPageDisplaysBasicSearchFieldRefreshIconFilterIcon() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Basic search field is not displayed without data", commonFunction.isPresent("searchTable", 3));
        Assert.assertTrue("Refresh icon is not displayed without data", commonFunction.isPresent("refreshTable", 3));
        Assert.assertTrue("Filter icon is not displayed without data", commonFunction.isPresent("columnFilters", 3));
        Assert.assertTrue("summary section is not displayed without data", commonFunction.presentElement(By.xpath(locator.getLocator("campaignSummary")), 10).isDisplayed());
    }

    @And("The Active Campaign page not displays pagination")
    public void theActiveCampaignPageNotDisplaysPagination() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertFalse("Pagination is still displayed without data", commonFunction.isPresent("paginationDiv", 3));
    }

    @When("There are no campaigns display on active campaigns page")
    public void thereAreNoCampaignsDisplayOnActiveCampaignsPage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.search("Invalid Name Campaign");
        Assert.assertEquals("More than 0 campagin display on campaign landing page", 0, commonFunction.numberRecordOnCurrentPage());
    }

    @Then("{string} which should not be display on active campaign page")
    public void theNoActiveCampaignShouldNotBeDisplayOnActiveCampaignPage(String campState) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> listNoActiveCamp = new ArrayList<>();
        if (campState.contains("completed")) {
            RestMethodsObj.getAllFilterNameLike(RestMethods.Page.campaign, testData.getData("campaign", "name")).forEach(obj -> listNoActiveCamp.add(obj.getAsJsonObject().get("name").getAsString()));
        } else {
            RestMethodsObj.getAllFilterNameLike(RestMethods.Page.campaign, testData.getData("campaign", "name")).forEach(obj -> {
                if (obj.getAsJsonObject().get("lastExecutedOn").isJsonNull()) {
                    listNoActiveCamp.add(obj.getAsJsonObject().get("name").getAsString());
                }
            });
        }
        List<String> listActiveCamp = commonFunction.getListColumnDataOnAllPage("Campaign Name");
        for (String noActiveCamp : listNoActiveCamp) {
            Assert.assertFalse("Completed, non-active campaign still display on active campaign page", listActiveCamp.contains(noActiveCamp));
        }
    }

    @Then("Apply sort for each column and click refresh button then make sure the sort restore back to default")
    public void verifyUserIsBackedToDefaultSortAfterApplyingSortAndClickRefresh() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] ArrayCol = {"Name", "Start Time"};
        for (String column : ArrayCol) {
            for (int i = 1; i <= 3; i++) {
                context.ContactListPageObj.clickSort(column, i);
                commonFunction.clickButton("refreshTable");
                commonFunction.waitForPageLoadComplete(10, 1);
                Assert.assertNull("Sort arrow up icon is still display in active campaign page", commonFunction.presentElement(By.xpath(locator.getLocator("sortIconArrowUp")), 2));
                Assert.assertNull("Sort arrow down icon is not display on Start time column", commonFunction.presentElement(By.xpath(String.format(locator.getLocator("iconSortArrowDown"), "Start Time")), 2));
            }
        }
    }

    @Then("Verify that the Stop option {string} on Supervisor Dashboard")
    public void verifyThatTheStopOptionEnabled(String status) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getStrict("campaign", "name");
        commonFunction.clickTakeMoreActionElipse(campaignName);
        if (status.equalsIgnoreCase("disabled")) {
            Assert.assertNotNull("Stop option not be disabled while campaign is not run",
                    commonFunction.presentElement(By.xpath(locator.locatorFormat("stopCampaignOptionFormat", "true")), 60));
        } else {
            Assert.assertNotNull("Stop option not be enabled while campaign is running",
                    commonFunction.presentElement(By.xpath(locator.locatorFormat("stopCampaignOptionFormat", "false")), 60));
        }
    }

    @Then("Apply advance search for Name column with each operator and verify function works correctly")
    public void apply_advance_search_for_name_column_with_each_operator_and_verify_function_works_correctly(List<Map<String, String>> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (Map<String, String> testcase : data) {
            verifyAdvanceSearch(testcase);
        }
    }

    private void verifyAdvanceSearch(Map<String, String> testcase) {
        commonFunction.applyAdvanceSearch(testcase.get("Column Name"), testcase.get("Operator"), testcase.get("Value"));
        List<Map<String, String>> tables = commonFunction.getTable();
        List<String> names = tables.stream().map(m -> m.get("Name")).toList();
        if (names.size() == 0) {
            return;
        }
        switch (testcase.get("Operator")) {
            case "Like" -> Assert.assertTrue(names.stream().allMatch(name -> name.contains(testcase.get("Value"))));
            case "Not Like" ->
                    Assert.assertFalse(names.stream().allMatch(name -> name.contains(testcase.get("Value"))));
            case "=" -> {
                Assert.assertTrue(names.size() == 1 && names.get(0).contentEquals(testcase.get("Value")));
            }
            case "!=" -> Assert.assertFalse(names.contains(testcase.get("Value")));
            case "In" -> Assert.assertTrue(List.of(testcase.get("Value").split(",")).containsAll(names));
        }
    }

    @Then("Stop all campaigns and wait for the campaign to stop completely")
    public void stop_all_campaigns_and_wait_for_the_campaign_to_stop_completely() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsObj.stopAllCampaigns(testData.getString("campaign", "name"), 60);
    }

    @When("User stop campaign with stop campaign option")
    public void userStopCampaignWithStopCampaignOption() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.tryClick("clearSearch", 10);
        commonFunction.search(testData.getStrict("campaign", "name"));
        commonFunction.clickAction(testData.getStrict("campaign", "name"), "Stop");
        commonFunction.waitForPageLoadComplete(60, 0.5);
    }

    @And("Verify the completed campaign not visible in active campaign view")
    public void verifyTheCompletedCampaignNotVisibleInActiveCampaignView() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String completedCampaign = testData.getString("campaign", "name");
        Assert.assertFalse(MonitorPageObj.verifyExistingCampaign(completedCampaign));
    }

    @And("Campaign status {string} on Active Campaigns page")
    public void campaignStatusOnActiveCampaignsPage(String expStatus) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getString("campaign", "name");
        Assert.assertNotNull(commonFunction.presentElement(By.xpath(locator.locatorFormat("statusCampaignFormat", campaignName, expStatus)), 60));
    }

    @Then("Campaign completed is not display on active campaign when user using basic search")
    public void verifyStatusCampaignDisplayActiveCampaign() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.search(testData.getData("campaign", "name") + "_1");
        List<String> statusDisplay = commonFunction.getListColumnDataOnAllPage("Status");
        for (String status : statusDisplay) {
            boolean state = List.of("running", "stopping", "paused", "pausing", "resuming", "starting").contains(status.toLowerCase());
            Assert.assertTrue("Status is not display correctly on active campagin page with status is: " + status, state);
        }
    }

    @Then("Total number of records display correctly on active campaign page")
    public void totalNumberOfContactsRecordsDisplayCorrectlyOnActiveCampaignPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String numRecordExpected = testData.getString("contactlist", "totalContact");
        String numRecordActual = commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Total Contacts");
        Assert.assertEquals("Total number of record displayed incorrectly on active campaign page", numRecordExpected, numRecordActual);
    }

    @And("Total number of attempts displayed correctly on active campaign page")
    public void totalNumberOfAttemptsDisplayedCorrectlyOnActiveCampaignPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<JsonObject> numbContactCompletedExpected = RestMethodsObj.getAttempts(testData.getString("campaign", "name"));
        int numAttemptActual = Integer.parseInt(commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Total Attempts"));
        Assert.assertEquals("Total number of attempts displayed incorrectly on active campaign page", numbContactCompletedExpected.size(), numAttemptActual);
    }

    @And("Total number of attempts still pending to be attempted displayed correctly on active campaign page")
    public void totalNumberOfAttemptsStillPendingToBeAttemptedDisplayedCorrectlyOnActiveCampaignPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int numNonAttemptActual = Integer.parseInt(commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Total Un-attempted Contacts"));
        List<JsonObject> numbRecordCompleted = RestMethodsObj.getAttempts(testData.getString("campaign", "name"));
        int totalContact = Integer.parseInt(testData.getString("contactlist", "totalContact"));
        int numNonAttemptExpected = totalContact - numbRecordCompleted.size();
        Assert.assertEquals("Total number number of attempts still pending displayed incorrectly on active campaign page", numNonAttemptExpected, numNonAttemptActual);
    }

    @And("Total Completed Contacts displayed correctly on active campaign page")
    public void totalCompletedContactsDisplayedCorrectlyOnActiveCampaignPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int numbContactCompletedActual = Integer.parseInt(commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Total Completed Contacts"));
        List<JsonObject> numbContactCompletedExpected = RestMethodsObj.getAttempts(testData.getString("campaign", "name"));
        Assert.assertEquals("Total Completed Contacts displayed incorrectly on active campaign page", numbContactCompletedActual, numbContactCompletedExpected.size());
    }

    @And("Percent Completed display correctly follow record sent")
    public void percentCompletedDisplayCorrectlyFollowRecordSent() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int totalRecord = Integer.parseInt(testData.getString("contactlist", "totalContact"));
        int numberContactCompleted = Integer.parseInt(commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Total Completed Contacts"));
        double numPercentExpected = Math.round((double) numberContactCompleted / totalRecord * 100 * 100) / 100.0;
        double numPercentActual = Double.parseDouble(commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Percent Completed"));
        Assert.assertTrue("Percent Completed display incorrectly follow record sent on active campaign page", numPercentExpected == numPercentActual);
//        Assert.assertTrue("Percent Completed display incorrectly follow record sent on active campaign page", Math.abs(numPercentExpected - numPercentActual) <= 0.01);
    }

    @Then("List campaign is updated every {int} seconds automatically")
    public void listCampaignIsUpdatedEverySecondsAutomatically(int timeReloadPage) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        List<String> newCampName = testDataObject.getData("newActiveCampaign");
        for (int i = 0; i < newCampName.size(); i++) {
            Assert.assertNotNull("The just running campaign is not updated within " + timeReloadPage + " seconds.",
                    commonFunction.presentElement(By.xpath(String.format(locator.getLocator("campaignNameOnTable"),
                            newCampName.get(i))), timeReloadPage));
        }
        softAssert.assertAll();
    }

    @When("Apply basic search for name column")
    public void applyBasicSearchForNameColumn() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.basicSearchOnTable(testData.getString("campaign", "name"));
    }

    @When("Apply advance search for campaign name with operator")
    public void applyAdvanceSearchForCampaignNameAndOperatorIsLike(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.applyAdvanceSearch(data.get("columnName"), data.get("operator"), data.get("searchValue"));
    }

    @When("Apply sorting ascending on Name column")
    public void applySortingAscendingOnNameColumn() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickSort("Name", 1);
    }

    @When("Change page size to {int} in active campaign page")
    public void changePageSizeToInActiveCampaignPage(int pageSize) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.selectDropDownOptionEqual("pageSizeDropdown", String.valueOf(pageSize));
        commonFunction.waitForPageLoadComplete(30, 2);
    }

    @Then("Apply advance search for Start time column with each operator and verify it it work correctly")
    public void applyAdvanceSearchForStartTimeColumnWithEachOperatorAndVerifyItItWorkFine(List<Map<String, String>> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (Map<String, String> testcase : data) {
            commonFunction.applyFilterCalendar(testcase.get("Column Name"), testcase.get("Operator"), testcase.get("Input type"), testcase.get("Value"));
            commonFunction.verifyAdvanceSearchForDateTimeCol();
        }
    }

    @Then("Apply advance search for Status column with each operator and verify it it work correctly")
    public void applyAdvanceSearchForStatusColumnWithEachOperatorAndVerifyItItWorkCorrectly(List<Map<String, String>> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (Map<String, String> testcase : data) {
            commonFunction.applyAdvanceSearch(testcase.get("Column Name"), testcase.get("Operator"), testcase.get("Value"));
            commonFunction.verifyAdvanceSearchResult();
        }
    }

    @When("Click filter button")
    public void clickFilterButton() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("columnFilters");
    }

    @Then("Name and Start time, Status display on dropdown of Column Name on active campaign monitor page")
    public void nameAndStartTimeStatusDisplayOnDropdownOfColumnNameOnActiveCampaignMonitorPage(List<List<String>> columnToAdvanceSearch) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Select select = new Select(commonFunction.findElementByDataID("columnName"));
        List<WebElement> listOptions = select.getOptions();
        List<String> actualResult = new ArrayList<>();
        for (WebElement e : listOptions) {
            actualResult.add(e.getText());
        }
        log.info("Expected list: " + columnToAdvanceSearch.get(0));
        log.info("Actual list: " + actualResult);
        Assert.assertEquals("List column names are supported to advance search on contact list page", columnToAdvanceSearch.get(0), actualResult);
    }

    @Then("list {string} display correctly for each advance search {string}")
    public void listDisplayCorrectlyForEachAdvanceSearch(String allOperator, String colAdvanceSearch) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyOperatorDisplayCorrectWithAttribute(colAdvanceSearch, allOperator);
    }

    @When("Apply advance search for field {string} by {string} with {string}")
    public void applyAdvanceSearchForFieldByWith(String columnName, String operator, String searchText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.applyAdvanceSearch(columnName, operator, searchText);
    }

    @Then("User can stop campaign successful with result search")
    public void userCanStopCampaignSuccessfulWithResultSearch() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campaignName = testData.getString("campaign", "name");
        commonFunction.clickAction(campaignName, "Stop");
        commonFunction.tryClick("done-action", 10);
        commonFunction.monitorCampaignTillCompleted(campaignName, 30);
        Assert.assertFalse(context.MonitorPageObj.verifyExistingCampaign(campaignName));
    }

    @Then("Verify advance search result correctly after updating input value")
    public void verifyAdvanceSearchResultCorrectlyAfterUpdatingInputValue(List<Map<String, String>> mapList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (Map<String, String> data : mapList) {
            commonFunction.advanceSearchFilter(data.get("Column Name"), data.get("Operator"), data.get("Input type"), data.get("Value"));
            commonFunction.verifyAdvanceSearchResult();
            commonFunction.updatedAdvanceSearch(null, null, data.get("New value"));
            commonFunction.verifyAdvanceSearchResult();
        }
    }

    @Then("User should  able to see {int} campaign")
    public void userShouldNotAbleToSeeAnyCampaign(int count) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> listName = commonFunction.getListColumnDataOnAllPage("Name");
        Assert.assertTrue("User is not able to see expected campaign list count", listName.size() == count);
    }

    @Then("User click on column name {string} to change sorting order")
    public void userClickOnColumnNameToChangeColumnSortingOrder(String column) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickHeaderColumn(column);
    }

    @When("Search Active Campaign list with field {string}, operator {string}, type {string} value {string} search")
    public void searchActiveCampaignListWithFieldOperatorTypeValueSearch(String field, String operator, String typeInput, String contents) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.advanceSearchFilter(field, operator, typeInput, contents);
    }

    @When("The campaign is sorted base on Start Time with advance search")
    public void theCampaignIsSortedBaseOnStartTimeWithoutBasicSearch() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/dd/yyyy h:mm:ss a");
        List<String> listStringTime = commonFunction.getListColumnDataOnAllPage("Start Time");
        Assert.assertTrue("Default sort display is not correctly on active campaign page", commonFunction.verifySortByDatetime(listStringTime, "descending"));
    }

    @And("Select {string} option on option menu dropdown and Click {string} button on pause confirmation box")
    public void selectOptionOnOptionMenuDropdownAndClickButtonOnPauseConfirmationBox(String action, String confirm) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        MonitorPageObj.takeActionOnActiveCampaignUI(testData.getString("campaign", "name"), action, confirm);
        if (confirm.equalsIgnoreCase("stop")) {
            commonFunction.monitorCampaignTillCompleted(testData.getString("campaign", "name"), 60);
        } else if (confirm.equalsIgnoreCase("pause")) {
            commonFunction.monitorCampaignTillPaused(testData.getString("campaign", "name"), 60);
        }
    }

    @Then("Stay in Active campaign view and confirmation box is not display in active campaign view")
    public void stayInActiveCampaignViewAndConfirmationBoxIsNotDisplayInActiveCampaignView() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = commonFunction.getPageName();
        Assert.assertEquals("Error!! Not stay in Active campaign view as expected", pageName, "active-campaigns-page");
        Assert.assertFalse("The confirmation box is still display in active campaign view after clicking cancel", commonFunction.isDisplayedAriaLabel("dialog", 2));
    }

    @When("Campaign is stopped successfully")
    public void campaignIsStoppedSuccessfully() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.monitorCampaignTillCompleted(testData.getData("campaign", "name"), 60);
        String status = RestMethodsObj.getAttributeValueOfJobCampaign(testData.getData("campaign", "name"), "status");
        Assert.assertTrue("Current status is " + status + " , expected status is Completed", status.equalsIgnoreCase("completed"));
        String completedCampaign = testData.getString("campaign", "name");
        Assert.assertFalse("Campaign %s is still displayed on active campaign view after campaign is paused successfully".formatted(completedCampaign), MonitorPageObj.verifyExistingCampaign(completedCampaign));
    }

    @Then("Paused state count is decremented when campaign state is changed from Paused to Stopped")
    public void pausedStateCountShouldBeDecrementedWhenCampaignStateIsChangedFromPausedToStopped() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sleepInSec(5);
        int pausedNumberBefore = Integer.parseInt(commonFunction.getXpathText(this.locator.getLocator("summaryNumber").formatted("Paused")));
        MonitorPageObj.takeActionOnActiveCampaignUI(testData.getString("campaign", "name"), "Stop", "stop");
        String locPausedExp = this.locator.getLocator("numberInMonitorFormat").formatted("Paused", pausedNumberBefore - 1);
        int pausedNumberAfter = Integer.parseInt(commonFunction.getXpathText(this.locator.getLocator("summaryNumber").formatted("Paused")));
        Assert.assertNotNull("Paused state count not be decremented after campaign is paused successfully. Expect: %d. But actual: %d".formatted(pausedNumberBefore - 1, pausedNumberAfter), commonFunction.presentElement(By.xpath(locPausedExp), 60));
    }

    @And("Select pause option on option menu dropdown")
    public void selectPauseOptionOnOptionMenuDropdown() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.advanceSearchFilter("Name", "=", "Input", testData.getString("campaign", "name"));
        commonFunction.clickAction(testData.getString("campaign", "name"), "Pause");
    }

    @Then("{string} confirmation box displays include two options Stop and Cancel")
    @Then("{string} confirmation box displays include two options Pause and Cancel")
    public void confirmationBoxDisplayWithTwoButton(String button) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Confirmation box is not displays", commonFunction.isDisplayedAriaLabel("dialog", 2));
        Assert.assertEquals("Confirmation box header text is incorrect", testData.getString("campaign", "confirmationHeader"),
                commonFunction.findElementByXpath(locator.getLocator("confirmationBoxHeader")).getText());
        Assert.assertEquals("Confirmation box body text is incorrect", String.format(testData.getString("campaign", "confirmationBodyText"),
                testData.getString("campaign", "name")), commonFunction.findElementByXpath(locator.getLocator("confirmationBoxBody")).getText());
        Assert.assertTrue("Cancel button is not displays on confirmation box", commonFunction.isPresent("cancel-action", 2));
        Assert.assertTrue(button + " button is not displays on confirmation box", commonFunction.isPresent("done-action", 2));
    }

    @When("Select pause option on option menu dropdown and click {string} button")
    public void selectPauseOptionOnOptionMenuDropdownAndClickPauseButton(String button) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.advanceSearchFilter("Name", "=", "Input", testData.getString("campaign", "name"));
        commonFunction.clickAction(testData.getString("campaign", "name"), "Pause");
        if (button.equalsIgnoreCase("pause")) {
            commonFunction.clickButton("done-action");
        } else {
            commonFunction.clickButton("cancel-action");
        }
        commonFunction.waitForPageLoadComplete(10, 1);
    }

    @Then("Campaign state is changed from Running to Pausing")
    public void campaignStateIsChangedFromRunningToPausing() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String currentStatusCamp = RestMethodsObj.getAttributeValueOfJobCampaign(testData.getString("campaign", "name"), "status");
        Assert.assertEquals("Current status of campaign is not Pausing", "PAUSING", currentStatusCamp);
    }

    @Then("Pause campaign toast message displays in monitor page")
    public void pauseCampaignToastMessageDisplaysInMonitorPage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("No only one pause campaign toast message displays", commonFunction.getListToastMessage().size() == 1);
        Assert.assertEquals("Fail to pause campaign!", testData.getString("campaign", "pauseCampaignToastMessage")
                .formatted(testData.getString("campaign", "name")), commonFunction.getToastMessage());
    }

    @Then("Pausing,Paused status campaign should be display in Monitor")
    public void pausingPausedStatusCampaignShouldBeDisplayInMonitor() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> campStatus = commonFunction.getListColumnDataOnAllPage("Status");
        boolean state = List.of("pausing", "paused").contains(campStatus.get(0).toLowerCase());
        Assert.assertTrue("Can't pause campaign : " + testData.getString("campaign", "name"), state);
    }

    @Then("Other state count decrement and Paused state count increment when campaign state is transition to Paused")
    public void otherStateCountDecrementAndPausedStateCountIncrementWhenCampaignStateIsTransitionToPaused() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.sleepInSec(2);
        int numberOtherBefore = Integer.parseInt(commonFunction.getXpathText(this.locator.getLocator("summaryNumber").formatted("Others")));
        int numberPausedBefore = Integer.parseInt(commonFunction.getXpathText(this.locator.getLocator("summaryNumber").formatted("Paused")));
        RestMethodsObj.pauseCampaignByAPI(testData.getString("campaign", "name"), "");
        String locOtherExp = this.locator.getLocator("numberInMonitorFormat").formatted("Others", numberOtherBefore + 1);
        String locPausedExp = this.locator.getLocator("numberInMonitorFormat").formatted("Paused", numberPausedBefore + 1);
        log.info("locOtherExp: " + locOtherExp);
        log.info("locPausedExp: " + locPausedExp);
        Assert.assertNotNull(commonFunction.presentElement(By.xpath(locOtherExp), 60));
        Assert.assertNotNull(commonFunction.presentElement(By.xpath(locPausedExp), 60));
    }

    @Given("Pause {int} campaign using API")
    public void pauseNumberCampaign(int numberCam) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameCam = testData.getString("campaign", "name");
        for (int i = 1; i <= numberCam; i++) {
            String status = RestMethodsObj.getAttributeValueOfJobCampaign(nameCam + "_" + i, "status");
            if (status.equalsIgnoreCase("running")) {
                RestMethodsObj.pauseCampaignByAPI(nameCam + "_" + i, "");
            }
        }
        commonFunction.sleepInSec(0.5);
        for (int i = 1; i <= numberCam; i++) {
            String status = RestMethodsObj.getAttributeValueOfJobCampaign(nameCam + "_" + i, "status");
            boolean state = List.of("paused", "pausing").contains(status.toLowerCase());
            Assert.assertTrue("Campaign can not pause when user using API", state);
        }
    }

    @Given("Pause {int} campaign using API and waiting campaigns are paused successfully")
    public void pauseNumberCampaignAndWaitToTillPaused(int numberCam) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameCam = testData.getString("campaign", "name");
        if (numberCam == 1) {
            RestMethodsObj.pauseCampaignByAPI(nameCam, "");
            commonFunction.monitorCampaignTillPaused(nameCam, 100);
        } else {
            for (int i = 1; i <= numberCam; i++) {
                String status = RestMethodsObj.getAttributeValueOfJobCampaign(nameCam + "_" + i, "status");
                if (status.equalsIgnoreCase("running")) {
                    RestMethodsObj.pauseCampaignByAPI(nameCam + "_" + i, "");
                }
            }
            commonFunction.sleepInSec(0.5);
            for (int i = 1; i <= numberCam; i++) {
                String status = RestMethodsObj.getAttributeValueOfJobCampaign(nameCam + "_" + i, "status");
                boolean state = List.of("paused", "pausing").contains(status.toLowerCase());
                Assert.assertTrue("Campaign can not pause when user using API", state);
                commonFunction.monitorCampaignTillPaused(nameCam + "_" + i, 100);
            }
        }
        commonFunction.sleepInSec(5);
    }

    @Then("Operator and Search box field are updated correctly after the column name is changed")
    public void operatorAndSearchBoxFieldAreUpdatedCorrectlyAfterTheColumnNameIsChanged(List<Map<String, String>> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (Map<String, String> testcase : data) {
            testcase = new HashMap<>(testcase);
            String curOperator = testcase.get("Operator");
            List<String> restOfOperators = data.stream().map(m -> m.get("Operator")).filter(o -> !o.contentEquals(curOperator)).toList();
            for (String operator : restOfOperators) {
                testcase.put("Operator", operator);
                verifyAdvanceSearch(testcase);
            }
        }
    }

    @Then("Verify that Search text is cleared after changing operator on {string} column")
    public void verifyThatSearchTextIsClearedAfterChangingOperatorOnTheOperatorField(String column) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (column) {
            case "Name" -> {
                List<String> operators = List.of("!=", "Like", "Not Like");
                String searchText = testData.getOrDefault("Value", "Test");
                commonFunction.applyAdvanceSearch("Name", "=", searchText);
                for (String operator : operators) {
                    commonFunction.selectDropDownOptionEqual("operator", operator);
                    commonFunction.waitForPageLoadComplete(1);
                    String contents = commonFunction.getFieldByArialLabelText("Search").getAttribute("value");
                    log.info("Operator: %s , contents: %s".formatted(operator, contents));
                    Assert.assertTrue("Search text is clear after change operator", contents.contentEquals(searchText));
                    commonFunction.enterToTheField("Search", searchText);
                }
            }
        }
    }

    @Then("Waiting Campaign status is {string} and click to {string} option on Campaign")
    public void click_to_option_on_campaign(String status, String option) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int waitTime = 0;
        while (waitTime < 60) {
            waitTime = waitTime + 2;
            utilityFun.wait(2);
            if (RestMethodsObj.getAttributeValueOfJobCampaign(testData.getData("campaign", "name"), "status").equalsIgnoreCase(status)) {
                commonFunction.tryClick("clearSearch", 10);
                commonFunction.search(testData.getStrict("campaign", "name"));
                commonFunction.clickAction(testData.getStrict("campaign", "name"), option);
                commonFunction.waitForPageLoadComplete(60, 0.5);
                break;
            }
            if (waitTime >= 60) {
                Assert.assertFalse("timeout", true);
            }
        }
    }

    @Then("Verify information in confirmation box is correct when clicking {string} button")
    public void verify_information_in_confirmation_box(String button) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String confirmation = String.format(testData.getString("activeCampaign", "ConfirmationResume"), testData.getString("campaign", "name"));
        commonFunction.isDisplayedAriaLabel("dialog", 2);
        Assert.assertEquals("Confirmation not correct!!!!!", commonFunction.findElementByXpath(locator.getLocator("confirmationBox")).getText(), confirmation);
        Assert.assertTrue("Cancel button doesn't display!!!", commonFunction.isPresent("cancel-action", 2));
        Assert.assertTrue(button + " doesn't display!!!", commonFunction.findElementByXpath(String.format(locator.getLocator("resumeConfirmationButton"), button)).isDisplayed());
    }

    @When("User click option {string} campaign and click {string} button on confirmation box")
    public void resumeCampaign(String button, String confirm) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            commonFunction.tryClick("clearSearch", 10);
            commonFunction.search(testData.getStrict("campaign", "name"));
            commonFunction.clickAction(testData.getStrict("campaign", "name"), button);
            commonFunction.waitForPageLoadComplete(60, 0.2);
            if (button.equalsIgnoreCase(confirm)) {
                commonFunction.tryClick("done-action", 10);
                commonFunction.waitForPageLoadComplete(10, 2);
                Map<String, String> confirmMessage = new HashMap<>();
                String confirmation = String.format(testData.getString("activeCampaign", "NotificationAlert"), button, testData.getString("campaign", "name"));
                confirmMessage.put("NotificationAlert", confirmation);
                commonFunction.verifyNotification(confirmMessage);
            } else {
                commonFunction.tryClick("cancel-action", 10);
            }
            commonFunction.waitForPageLoadComplete(10, 1.5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @And("Verify the status of campaign is {string}")
    public void verifyTheStatusOfCampaign(String statusExp) {
        commonFunction.basicSearchOnTable(testData.getString("campaign", "name"));
        int nameColIndex = commonFunction.getColumnIndex("Status");
        commonFunction.waitForElementVisible(commonFunction.findElementByXpath("//table/tbody/tr//*[text()='" +
                statusExp + "']//ancestor::tr[@role='row']/td[@role='cell'][" + (nameColIndex + 1) + "]"));
        String statusAct = commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Status");
        int count = 0;
        while (!statusAct.equalsIgnoreCase(statusExp)) {
            statusAct = commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Status");
            utilityFun.wait(3);
            count = count + 1;
            if (count == 10) {
                break;
            }
        }
        Assert.assertEquals("The status is incorrect", statusExp, statusAct);
    }

    @Then("Verify {string} option is disabled")
    public void verifyOptionIsDisabled(String option) {
        commonFunction.clickTakeMoreActionElipse(testData.getString("campaign", "name"));
        String dataID = switch (option) {
            case "Resume" -> "action_resumeCampaign";
            case "Pause" -> "action_pauseCampaign";
            case "Stop" -> "action_stopCampaign";
            default -> "";
        };
        String valueAct = commonFunction.getAttributeField(dataID, "aria-disabled");
        Assert.assertTrue("Value is not disable", Boolean.parseBoolean(valueAct));
    }

    @And("Click {string} button on confirmation form")
    public void clickButtonOnConfirmationForm(String btn) {
        commonFunction.tryClick("cancel-action", 5);
    }

    @And("User click option {string} for {int} campaign and verify status of them is {string}")
    public void verifyStatus(String button, int numberCampaign, String status) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> campaign = testData.getData("campaign");
        String defCamp = testData.getStrict("campaign", "name");
        int waitTime = 0;
        for (int i = 1; i <= numberCampaign; i++) {
            campaign.put("name", (defCamp + "_" + i));
            this.resumeCampaign(button, button);
            while (waitTime < 60) {
                waitTime = waitTime + 2;
                utilityFun.wait(2);
                if (RestMethodsObj.getAttributeValueOfJobCampaign(testData.getData("campaign", "name"), "status").equalsIgnoreCase(status)) {
                    break;
                }
                if (waitTime >= 60) {
                    Assert.assertFalse("timeout", true);
                }
            }
            campaign.put("name", defCamp);
        }
    }

    @Then("Get status for all campaign after {string}")
    public void getStatus(String state) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> activeCampaign = testData.getData("activeCampaign");
        List<WebElement> runningEle = commonFunction.findElementsByXpath(locator.getLocator("stateCountRunning"));
        String runningStatus = runningEle.get(0).getText();
        List<WebElement> pauseEle = commonFunction.findElementsByXpath(locator.getLocator("stateCountPause"));
        String pauseStatus = pauseEle.get(0).getText();
        if (state.equalsIgnoreCase("pause")) {
            activeCampaign.put("PauseBeforeResume", pauseStatus);
            activeCampaign.put("RunningBeforeResume", runningStatus);
        }
        if (state.equalsIgnoreCase("resume")) {
            activeCampaign.put("PauseAfterResume", pauseStatus);
            activeCampaign.put("RunningAfterResume", runningStatus);
        }
    }

    @Then("Running state count should be incremented and Pause state count should be decremented")
    public void verifyStateCount() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Running state ins't incremented!!!!",
                Integer.parseInt(testData.getStrict("activeCampaign", "RunningBeforeResume")) < Integer.parseInt(testData.getStrict("activeCampaign", "RunningAfterResume")));
        Assert.assertTrue("Pause state ins't decremented!!!!",
                Integer.parseInt(testData.getStrict("activeCampaign", "PauseBeforeResume")) > Integer.parseInt(testData.getStrict("activeCampaign", "PauseAfterResume")));
    }

    @Then("Verify {string} option is enable")
    public void verifyOptionIsEnable(String option) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickTakeMoreActionElipse(testData.getString("campaign", "name"));
        String dataID = switch (option) {
            case "Resume" -> "action_resumeCampaign";
            case "Pause" -> "action_pauseCampaign";
            case "Stop" -> "action_stopCampaign";
            default -> "";
        };
        String valueAct = commonFunction.getAttributeField(dataID, "aria-disabled");
        Assert.assertFalse("Value is not enable", Boolean.parseBoolean(valueAct));
    }

    @Then("Campaign state stay Pausing until it finishes the outstanding")
    public void campaignStateStayPausingUntilItFinishesTheOutstanding() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campName = testData.getString("campaign", "name");
        commonFunction.search(campName);
        RestMethodsObj.pauseCampaignByAPI(campName, "");
        commonFunction.sleepInSec(0.5);
        String curTotalAttempts = commonFunction.getSingleDataFromTable(campName, "Total Attempts");
        log.info("curTotalAttempts: " + curTotalAttempts);
        commonFunction.monitorCampaignTillPaused(campName, 60);
        commonFunction.sleepInSec(5);
        String pausedTotalAttempts = commonFunction.getSingleDataFromTable(campName, "Total Attempts");
        log.info("pausedTotalAttempts: " + pausedTotalAttempts);
        long curTotalAttemptsLong = Long.parseLong(curTotalAttempts);
        long pausedTotalAttemptsLong = Long.parseLong(pausedTotalAttempts);
        Assert.assertEquals(curTotalAttemptsLong + 1, pausedTotalAttemptsLong);
    }

    @Then("No new SMS be placed once entering to Pausing state")
    public void noNewSMSBePlacedOnceEnteringToPausingState() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String campName = testData.getString("campaign", "name");
        RestMethodsObj.pauseCampaignByAPI(campName, "");
        commonFunction.search(campName);
        commonFunction.monitorCampaignTillPaused(campName, 60);
        commonFunction.sleepInSec(5);
        String curTotalAttempts = commonFunction.getSingleDataFromTable(campName, "Total Attempts");
        log.info("curTotalAttempts: " + curTotalAttempts);
        commonFunction.sleepInSec(5); // sleep 5 sec to wait for new sms is sent or not
        String pausedTotalAttempts = commonFunction.getSingleDataFromTable(campName, "Total Attempts");
        log.info("pausedTotalAttempts: " + pausedTotalAttempts);
        long curTotalAttemptsLong = Long.parseLong(curTotalAttempts);
        long pausedTotalAttemptsLong = Long.parseLong(pausedTotalAttempts);
        Assert.assertEquals(curTotalAttemptsLong, pausedTotalAttemptsLong);
    }

    @Then("Verify the status of campaigns and buttons change correctly")
    public void verifyTheStatusOfCampaignsAndButtonsChangeCorrectly(List<Map<String, String>> dataLists) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> data = dataLists.get(0);
        log.info(data);
        String status = data.get("CampaignStatus");
        int statusIndex = commonFunction.getColumnIndex("Status");
        WebElement elementStatus = commonFunction.presentElement(By.xpath("//tbody//td[%d]/div[text()='%s']".formatted(statusIndex + 1, status)), 60);
        Assert.assertNotNull(elementStatus);

        String disableBtn = data.get("ButtonDisable") == null ? "" : data.get("ButtonDisable");
        String enableBtn = data.get("ButtonEnable") == null ? "" : data.get("ButtonEnable");
        String campName = testData.getString("campaign", "name");
        // verify button status
        if (!disableBtn.isEmpty() || !enableBtn.isEmpty()) {
            commonFunction.clickTakeMoreActionElipse(campName);
            Map<String, String> menuStatus = commonFunction.getFieldAriaDisable();
            if (!disableBtn.isEmpty())
                Assert.assertTrue("Value is not disable", menuStatus.get(disableBtn).contentEquals("true"));
            if (!enableBtn.isEmpty())
                Assert.assertTrue("Value is not enable", menuStatus.get(enableBtn).contentEquals("false"));
            commonFunction.clickButton("page_1");
        }
    }

    @And("User search and click on {string} option of campaign then click {string} button on confirmation box")
    public void userSearchAndClickOnOptionOfCampaignThenClickButtonOnConfirmationBox(String option, String confirmBtn) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.tryClick("clearSearch", 10);
        commonFunction.search(testData.getStrict("campaign", "name"));
        commonFunction.clickAction(testData.getStrict("campaign", "name"), option);
        commonFunction.waitForPageLoadComplete(60, 0.2);
        String loc = "//button[@aria-label='%s']".formatted(confirmBtn);
        commonFunction.waitForElementClickable(By.xpath(loc));
        commonFunction.tryClick(By.xpath(loc), 60);
    }

    @And("Verify campaign status after paused")
    public void verifyCampaignStatusAfterPaused() {
        log.info("Verify campaign resume the remain records after click resume button");
        String campName = testData.getString("campaign", "name");
        commonFunction.monitorCampaignTillPaused(campName, 30);
        log.info("Get the Total curent attempts when status is Paused");
        commonFunction.basicSearchOnTable(testData.getString("campaign", "name"));
        int completedContact = Integer.parseInt(commonFunction.getSingleDataFromTable(campName, "Total Completed Contacts"));
        int totalContact = Integer.parseInt(commonFunction.getSingleDataFromTable(campName, "Total Contacts"));
        int pendingContact = Integer.parseInt(commonFunction.getSingleDataFromTable(campName, "Total Un-attempted Contacts"));
        Assert.assertEquals("!!!ERR. The pending contact not match with expected.", (totalContact - completedContact), pendingContact);
    }

    @When("Campaign already finish at least {int} records")
    public void campaignAlreadyFinishAtLeastRecords(int number) {
        log.info("Campaign already finish at least " + number + " records");
        commonFunction.basicSearchOnTable(testData.getString("campaign", "name"));
        int totalAttempt = Integer.parseInt(commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Total Attempts"));
        for (int i = 0; i < number; i++) {
            totalAttempt = Integer.parseInt(commonFunction.getSingleDataFromTable(testData.getString("campaign", "name"), "Total Attempts"));
            if (totalAttempt >= number) {
                break;
            }
            utilityFun.wait(3);
        }
        Assert.assertNotEquals("ERR!!!. The Total Attempts seem not update when campaign running", 0, totalAttempt);
    }

    @Then("Verify campaign process all contacts of contact list")
    public void verifyCampaignProcessAllContactsOfContactList() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.monitorCampaignTillCompleted(testData.getData("campaign", "name"), 300);
        String sourceFile = EnvSetup.TEST_DATA_PATH + "datafiles/" + testData.getData("datasource", "sourceFile");
        List<String> phoneDefault = commonFunction.getValueColumnOfFile(sourceFile, "phoneNumber");
        Collections.sort(phoneDefault);
        Map<String, List<String>> attempts = RestMethodsObj.getAttempts(testData.getString("campaign", "name"), "address");
        List<String> address = attempts.get("address");
        Collections.sort(address);
        Assert.assertTrue("Campaign is not send SMS to default address list in the contact", phoneDefault.equals(address));
    }

    @Then("Verify after resume campaign do not dial already dialed records")
    public void verifyAfterResumeCampaignDoNotDialAlreadyDialedRecords() {
        log.info("Verify no duplicate records are found after campaign finish");
        Map<String, List<String>> attempts = RestMethodsObj.getAttempts(testData.getString("campaign", "name"), "address");
        List<String> address = attempts.get("address");
        Collections.sort(address);
        for (int i = 60001; i <= 60100; i++) {
            Assert.assertEquals("ERR!!!. The contact number is not match with the contact list", Integer.parseInt(address.get(i - 60001)), i);
        }
    }

    @And("User able to see {int} campaign")
    public void userAbleToSeeCampaign(int numberCampaign) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.search(testData.getData("campaign", "name"));
        commonFunction.sleepInSec(0.5);
        this.userShouldNotAbleToSeeAnyCampaign(numberCampaign);
        commonFunction.clickRefreshButton();
        commonFunction.sleepInSec(0.5);
    }
}
