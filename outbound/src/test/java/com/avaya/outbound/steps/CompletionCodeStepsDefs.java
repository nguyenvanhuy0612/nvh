package com.avaya.outbound.steps;


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
import org.openqa.selenium.Keys;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CompletionCodeStepsDefs extends StepContext {
    public Locator locator = new Locator();

    public CompletionCodeStepsDefs(Context context) {
        context.init(this);
    }

    @Before(order = 9000, value = "@CompletionCode")
    public void initPageObject() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @After(order = 9000, value = "@CompletionCode")
    public void after(Scenario scenario) {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
    }

    @Given("The Completion code URL is hit to view the existing complete code")
    public void openURL() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.navigateToOutboundPage("completion-codes");
        Assert.assertEquals("ERR!!!. URL is not correct for Outbound Admin page", "Completion Codes",
                commonFunction.findElementByDataID("page").getText());
        commonFunction.waitForPageLoadComplete(60, 2);
    }


    @Then("All column as {string} display on header table")
    public void verifyAllColumnOnHeaderTable(String header) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> expectList = new ArrayList<>();
        Collections.addAll(expectList, header.split(","));
        log.info("expList : " + expectList);
        List<String> actualList = commonFunction.getAllColumnOnHeader();
        log.info("actList : " + actualList);
        Assert.assertEquals("List column on header table on completion code landing page display in-correct! ", expectList, actualList);
    }


    @Then("Data of system completion code display correctly on landing cc page")
    public void verifySystemCCTableData(List<Map<String, String>> listData) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<Map<String, String>> listMap = commonFunction.getTable();
        String[] key = {"Code", "Description", "Type", "RPC", "Success", "Closure"};
        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < listData.size(); i++) {
            Map<String, String> expData = listData.get(i);
            log.info("exp Data: " + expData);
            String value = expData.get("Code");
            Map<String, String> actualData = commonFunction.getTableRowData(listMap, "Code", value);
            Assert.assertNotNull(actualData);
            log.info("actual data:  " + actualData);
            for (int j = 0; j < key.length; j++) {
                softAssert.assertTrue(actualData.get(key[j]).equals(expData.get(key[j])), "Value " + key[j] + " not match!!");
            }
        }
        softAssert.assertAll();
    }


    @Then("Verify that system completion code is defined as System type")
    public void verifyCCType(List<Map<String, String>> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("data: " + data);
        List<Map<String, String>> listMap = commonFunction.getTable();
        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> expData = data.get(i);
            log.info("exp Data: " + expData);
            String value = expData.get("Code");
            Map<String, String> actualData = commonFunction.getTableRowData(listMap, "Code", value);
            Assert.assertNotNull(actualData);
            log.info("actual data:  " + actualData);
            softAssert.assertTrue(actualData.get("Type").equals(expData.get("Type")), "Type not match with " + value);
        }
        softAssert.assertAll();
    }


    @Then("Verify that each system completion code has following three properties")
    public void verifyThatEachSystemCompletionCodeHasFollowingThreeProperties(List<Map<String, String>> listData) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("data: " + listData);
        List<Map<String, String>> listMap = commonFunction.getTable();
        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < listData.size(); i++) {
            Map<String, String> expData = listData.get(i);
            log.info("exp Data: " + expData);
            String value = expData.get("Code");
            Map<String, String> actualData = commonFunction.getTableRowData(listMap, "Code", value);
            Assert.assertNotNull(actualData);
            log.info("actual data:  " + actualData);
            softAssert.assertTrue(actualData.get("RPC").equalsIgnoreCase(expData.get("RPC")), "Properties RPC not match");
            softAssert.assertTrue(actualData.get("Success").equalsIgnoreCase(expData.get("Success")), "Properties Success not match");
            softAssert.assertTrue(actualData.get("Closure").equalsIgnoreCase(expData.get("Closure")), "Properties Closure not match");
        }
        softAssert.assertAll();
    }

    @Then("User should be able to sort the {string} colum")
    public void heShouldBeAbleToSortTheColum(String columName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.refreshPage(30);
        commonFunction.verifySortingCurrentPage(columName, "Ascending");
        commonFunction.refreshPage(30);
        commonFunction.verifySortingCurrentPage(columName, "Descending");
        commonFunction.refreshPage(30);
        commonFunction.verifySortingCurrentPage(columName, "Default");
    }

    @Then("Sorting each colum and the sort icon should be displayed")
    public void sortingEachColumAndTheSortIconShouldBeDisplayed(List<List<String>> lstStr) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> columNames = lstStr.get(0);
        for (String columName : columNames) {
            commonFunction.clickHeaderColumn(columName);
            Assert.assertTrue(commonFunction.isDisplayedByXpath(locator.getLocator("sortIconArrowUp"), 30));
            commonFunction.clickHeaderColumn(columName);
            Assert.assertTrue(commonFunction.isDisplayedByXpath(locator.getLocator("sortIconArrowDown"), 30));
        }
    }


    @Then("search text box displayed on completion code page")
    public void verifySearchTextBoxIsDisplayed() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Search text box is not display on completion code page", commonFunction.isDisplayed("searchTable", 30));
    }


    @When("User input {string} to search text box and {string} click Enter")
    public void userInputValidSearchToSearchTextBox(String valueSearch, String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.presentElement("searchTable", 30).clear();
        commonFunction.sendKeysToTextBox("searchTable", valueSearch);
        if (action.equalsIgnoreCase("yes")) {
            commonFunction.presentElement("searchTable", 30).sendKeys(Keys.ENTER);
        } else {
            log.info("Search and no click enter");
        }
        utilityFun.wait(2);
    }


    @Then("Search results displayed correctly by using like operator when user search with Name is {string}")
    public void searchResultsDisplayedCorrectlyByNameUsingLikeOperator(String searchText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Search results is not work properly", commonFunction.verifyBasicSearchWorkCorrect(searchText, "Code"));
    }


    @Then("Search results displayed empty on completion code page")
    public void searchResultsDisplayedEmptyOnCompletionCodePage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify no result display on completion code page");
        Assert.assertEquals("Search results displayed empty on completion code page", 0, commonFunction.numberRecordOnCurrentPage());
    }


    @And("Message {string} is displayed in place of row on completion code landing page")
    public void verifyMessageDisplayWhenNoResultDisplay(String message) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyMessageWithNoRecord(message);
    }


    @And("Clear search display on completion code page")
    public void clearSearchDisplayOnCompletionCodePage() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Search text box is not display on completion code page", commonFunction.isDisplayed("clearSearch", 30));
    }

    @Then("Search results displayed correctly by using like operator when user re-input with Name is {string}")
    public void searchResultsDisplayedCorrectlyByUsingLikeOperatorWhenUserReInputWithNameIs(String searchText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.searchResultsDisplayedCorrectlyByNameUsingLikeOperator(searchText);
    }

    @And("Quick search code on Completion code")
    public void quickSearchcodeOnCompletionCode(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Quick search Completion Code <"+data.get("CompletioncodeNames")+">");
        commonFunction.sendKeysToTextBox("searchTable",data.get("CompletioncodeNames"));
    }

    @Then("Verify searched Completion code length")
    public void verifySearchedCompletioncodeLength(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int iSearchedTextLength=commonFunction.getAttributeField("searchTable","value").length();
        log.info("Expected Searched Text String<"+data.get("SearchStringLength")+">");
        log.info("Actual Searched Text String length <"+iSearchedTextLength+">");
        Assert.assertEquals(data.get("SearchStringLength").toString(),Integer.toString(iSearchedTextLength));
    }
}

