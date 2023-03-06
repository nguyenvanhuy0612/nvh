package com.avaya.outbound.steps;


import com.avaya.outbound.frame.CampaignStrategyPage;
import com.avaya.outbound.frame.RestMethods;
import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
import com.avaya.outbound.lib.support.TestData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CampaignStrategyDefs extends StepContext {
    public Locator locator = new Locator(CampaignStrategyPage.class);


    public CampaignStrategyDefs(Context context) {
        context.init(this);
    }

    @Before(order = 9000, value = "@CampaignStrategy or @UpdateStrategy or @ListStrategy or @AdvanceSearching or @UpdateCampaign")
    public void initPageObject() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @After(order = 9000, value = "@CampaignStrategy or @UpdateStrategy or @ListStrategy or @AdvanceSearching or @UpdateCampaign")
    public void afterCamp(Scenario scenario) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
        try {
            this.cleanUpDataCreateStrategy();
        } catch (Exception e) {
            log.info(e);
        }

    }

    @And("(?i)^Load testcase data:\\s*DataFile\\s*=\\s*([\\w\\.\\-\\+]+)\\s*,\\s*Data\\s*=\\s*([\\w\\.\\-\\+]+)\\s*$")
    @Given("Load testcase data: DataFile = {string}, Data = {string}")
    public void loadDataForTest(String dataFile, String data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        testData.load(dataFile, data);
        log.info("testData: " + testData);
    }

    @Given("The Campaign Strategy URL is hit")
    public void the_Campaign_Strategy_URL_is_hit() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Verify the Campaign Strategy URL is hit");
        commonFunction.navigateToOutboundPage("campaign-strategy");
        Assert.assertEquals("ERR!!!. URL is not correct for Outbound Admin page", "Campaign Strategy", CampaignStrategyPageObj.homeStrategyPage.getText());
    }

    @Then("The campaign strategy should have option to create new strategy")
    public void verify_option_to_create_new_strategy() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Verify the button create strategy is displayed");
        Assert.assertTrue("ERR!!!. The Create Strategy button is not displayed", CampaignStrategyPageObj.createStrategyButton.isDisplayed());
    }

    @And("Create strategy by API")
    public void create_Strategy_By_API() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Create Strategy by API");
        RestMethodsObj.deleteStrategy(testData.getStrict("strategy", "name"), "");
        boolean flag = CampaignStrategyPageObj.createStrategyAPI(testData.getData("strategy"));
        Assert.assertTrue("ERR!!!. The Create Strategy by API unsuccessfully", flag);
    }

    @When("Reload Campaign strategy url multi times")
    public void reload_Page() {
        int times = 1;
        try {
            times = Integer.parseInt(testData.getString("strategy", "Reload Times"));
        } catch (Exception e) {
            log.info("The TC data has no \"Reload Times\"!! Using default reload page is 1");
        }
        log.info("---------------------------------------------------------------------------------------");
        log.info("Reload browser " + testData.getString("strategy", "Reload Times") + " times.");
        for (int i = 0; i < times; i++) {
            log.info("Reload browser " + times + " times.");
            driver.navigate().refresh();
            utilityFun.wait(5);
            this.verify_option_to_create_new_strategy();
        }
    }


    @When("clicks on new campaign strategy")
    public void clickaddStrategy() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if(!testData.isEmpty()){
            RestMethodsObj.deleteStrategy(testData.getStrict("strategy", "name"), "");
        }
        commonFunction.clickButton("add");
        utilityFun.wait(1);
    }

    @Then("should have option to create simple SMS strategy")
    public void simpleSMSstrategyExist() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            List<WebElement> SimpleSMStrategy = driver.findElements(By.xpath("//*[text()='SMS Text']"));
            if (SimpleSMStrategy.size() != 0) {
                log.info("Simple SMS Strategy Exits - passed");
            } else {
                Assert.fail("Simple SMS Strategy not exits - failed");
            }
        } finally {
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            List<WebElement> SimpleSMStrategy = driver.findElements(By.xpath("//*[text()='SMS Text']"));
            if (SimpleSMStrategy.size() != 0) {
                log.info("Simple SMS Strategy Exits - passed");
            } else {
                Assert.fail("Simple SMS Strategy not exits - failed");
            }
        }
    }

    @When("^Input value Strategy field.*$")
    public void inputStrategyField() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        commonFunction.sendKeysToTextBox("name", strategiesData.get("name"));
        commonFunction.sendKeysToTextBox("description", strategiesData.get("description"));
        commonFunction.sendKeysToTextBox("smsText", strategiesData.get("smsText"));
        if (strategiesData.containsKey("smsPacingTimeUnit")) {
            commonFunction.selectDropDownOptionEqual("smsPacingTimeUnit", strategiesData.get("smsPacingTimeUnit"));
        } else {
            log.info("!!!The smsPacingTimeUnit is missed, using default is SECONDS!!!");
        }
        commonFunction.sendKeysToTextBox("smsPace", strategiesData.get("smsPace"));
        commonFunction.waitForPageLoadComplete(10, 1);
    }

    @Then("Verify cannot enter more {int} character in Strategy {string}")
    public void verifyTheNumberofCharacter(int maxLenght, String field) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (field) {
            case "Name":
                String maxlengthNameUI = commonFunction.getAttributeField("name", "maxlength");
                log.info("max length name UI: " + maxlengthNameUI + "---------------- Expected: " + maxLenght);
                Assert.assertEquals("Maximum allowed length of the strategy name not match", Integer.toString(maxLenght), maxlengthNameUI);
                String StrategyName = commonFunction.getAttributeField("name", "value");
                if (StrategyName.length() > maxLenght) {
                    Assert.fail("Maximum allowed length of the strategy " + field + " is not " + maxLenght + " - failed");
                } else {
                    log.info("max length of strategy " + field + " is " + maxLenght + " - passed");
                }
                break;
            case "Description":
                // CampaignStrategyPageObj.txtBoxStrategyDescription.getAttribute("maxlength").toString();
                String maxlengthDesUI = commonFunction.getAttributeField("description", "maxlength");
                log.info("max length Des UI: " + maxlengthDesUI + "---------------- Expected: " + maxLenght);
                Assert.assertEquals("Maximum allowed length of the strategy name not match", Integer.toString(maxLenght), maxlengthDesUI);
                log.info("-------------------------------------------------------------------------");
                String StrategyDes = commonFunction.getTextField("description");
                log.info("Strategy Description: " + StrategyDes + "/n ------- lenght:" + StrategyDes.length());
                if (StrategyDes.length() > maxLenght) {
                    Assert.fail("Maximum allowed length of the strategy " + field + " is not " + maxLenght + " - failed");
                } else {
                    log.info("max length of strategy " + field + " is " + maxLenght + " - passed");
                }
                break;
        }
        utilityFun.wait(5);
    }


    @When("Save Strategy")
    public void saveStrategy() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("save");
        //commonFunction.waitForPageLoadComplete(60, 1);
    }

    @Then("Error message is shown")
    public void errorMessageIsShown() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<WebElement> Errormsgs = driver.findElements(By.xpath("//*[@class='neo-input-hint']"));
        if (Errormsgs.size() != 0) {
            log.info("Error message is shown - passed");
        } else {
            Assert.fail("Dont have error message - failed");
        }
    }

    @And("Validation the error message required field {string}")
    public void errMsgRequiredField(String field) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (field) {
            case "Name":
                commonFunction.verifyFieldError("name", testData.getData("strategy", "errorMessage"));
                break;
            case "SMS Text":
                commonFunction.verifyFieldError("smsText", testData.getData("strategy", "errorMessage"));
                break;
        }

    }

    @And("Verify user still stay on {string} page")
    public boolean verifyUserStillStayOnSpecificPage(String pageName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("Verify that user is on " + pageName + " page");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            commonFunction.waitForElementClickable(commonFunction.findElementByXpath("(//*[@data-testid='%s'])".formatted("strategyAdd")));
            log.info("Check Cancel button is enabled");
            Assert.assertTrue(
                    "ERR! Cancel button is not enable",
                    commonFunction.findElementByDataID("cancel").isEnabled());
            return true;
        } catch (Exception e) {
            Assert.fail("ERR! Cancel button is not displayed");
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        }
    }

    @When("Entered special character to in the Strategy name")
    public void specialChartoName() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @And("Validation the error message special character field")
    public void verifyErrMsgSpecialChar() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        CampaignStrategyPageObj.valueSpecialCharacters("name", strategiesData.get("name"), strategiesData.get("errorMessage"));
    }

    @Given("^Create Strategy.*$")
    public void createStrategy() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        RestMethodsObj.deleteStrategy(testData.getStrict("strategy", "name"), "");
        CampaignStrategyPageObj.createStrategy(strategiesData);
        commonFunction.waitForPageLoadComplete(60, 1);
    }

    @And("Cleanup and create strategy name {string}, sms {string} by API")
    public void cleanUpAndCreateStrategy(String strategyName, String smsText) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("strategyName: " + strategyName);
        log.info("smsText: " + smsText);
        Map<String, String> strategyData = new HashMap<>();
        strategyData.put("type", "sms");
        strategyData.put("name", strategyName);
        strategyData.put("description", "description");
        strategyData.put("smsText", smsText);
        strategyData.put("smsPace", "100");
        strategyData.put("smsPacingTimeUnit", "second");
        RestMethodsObj.deleteStrategy(strategyName, "");
        RestMethodsObj.createStrategy(strategyData);
    }

    @Given("Verify Strategy save successful")
    public void verifyStrategySaveSuccessful() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        Assert.assertTrue("campaign strategy save UNSUCCESS", CampaignStrategyPageObj.verifyStrategySaveSuccessful(strategiesData));
        Assert.assertTrue("campaign strategy save failed", CampaignStrategyPageObj.StrategyExisted(strategiesData.get("name"), strategiesData.get("type")));
    }

    @Then("Verify strategy name showing hint error message {string}")
    public void verifyHintError(String expMgs) {
        log.info("Expected message: " + expMgs);
        utilityFun.wait(1);
        String actualMsg = commonFunction.findElementByXpath("//div[@data-testid='name-wrapper']//div[@class='neo-input-hint']").getText();
        Assert.assertEquals("The message not match", expMgs, actualMsg);
    }

    @Then("UI displays correct the {string} Error for {string} field")
    public void checkErrorFieldStrategy(String errorType, String field) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Check error msg in " + field + " strategy");
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        if (field.equalsIgnoreCase("name")) {
            if (errorType.equalsIgnoreCase("popup")) {
                try {
                    String errorActual = CampaignStrategyPageObj.notificationInform.getText();
                    Assert.assertTrue("ERR! The error message is in correct: " + errorActual, errorActual.equalsIgnoreCase(strategiesData.get("errorMessage")));
                } catch (Exception e) {
                    Assert.fail("ERR! The error message is in not displayed");
                    log.info(e);
                }
            } else if (errorType.equalsIgnoreCase("hint")) {
                try {
                    Assert.assertTrue("ERR! The error message is in correct: " + CampaignStrategyPageObj.hintMessageName.getText(),
                            CampaignStrategyPageObj.hintMessageName.getText().equalsIgnoreCase(strategiesData.get("errorMessage")));
                } catch (Exception e) {
                    Assert.fail("ERR! The error message is in not displayed");
                    log.info(e);
                }
            }
        }
        if (field.equalsIgnoreCase("smstext")) {
            if (errorType.equalsIgnoreCase("hint")) {
                try {
                    Assert.assertTrue("ERR! The error message is in correct: " + CampaignStrategyPageObj.errMsgStrategySMSTextInput.getText(), CampaignStrategyPageObj.errMsgStrategySMSTextInput.getText().equalsIgnoreCase(strategiesData.get("errorMessage")));
                } catch (Exception e) {
                    Assert.fail("ERR! The error message is in not displayed");
                    log.info(e);
                }
            }
        }
    }

    @Then("UI stays on The Create Strategy page")
    public void checkStayOnCreateStrategyPageAfterSaveInvalidValue() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Check user stay on create strategy page");
        try {
            log.info("Check create strategy page");
            Assert.assertTrue(
                    "ERR! The User does not stay on create strategy page (\"Add New Strategy\" is not displayed): "
                            + CampaignStrategyPageObj.createStrategyPage.getText(),
                    CampaignStrategyPageObj.createStrategyPage.getText().equalsIgnoreCase("Add New Strategy"));
        } catch (Exception e) {
            Assert.fail("ERR!create strategy page is not existed");
            log.info(e);
        }
        try {
            log.info("Check Cancel button is enabled");
            Assert.assertTrue(
                    "ERR! Cancel button is not enable",
                    CampaignStrategyPageObj.btnCancelStrategy.isEnabled());
        } catch (Exception e) {
            Assert.fail("ERR! Cancel button is not displayed");
            log.info(e);
        }
    }

    @When("^Re input valid strategy name.*")
    public void reInputStrategyName() {
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        CampaignStrategyPageObj.inputTextBox(CampaignStrategyPageObj.txtBoxStrategyName, strategiesData.get("nameEdit"));
        CampaignStrategyPageObj.btnSaveStrategy.click();
    }

    @When("^Re input valid sms text.*")
    public void reInputSmsText() {
        CampaignStrategyPageObj.inputTextBox(CampaignStrategyPageObj.txtBoxStrategySMSText, testData.getString("strategy", "smsTextEdit"));
        CampaignStrategyPageObj.btnSaveStrategy.click();
    }

    @Then("^.*Save strategy successfully.*valid.*input")
    public void checkStrategySaveSuccessfully() {
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        if (strategiesData.containsKey("nameEdit")) {
            CampaignStrategyPageObj.verifyAddStrategySuccessfullyUI(strategiesData.get("nameEdit"), strategiesData.get("notificationMessage"), strategiesData.get("description"));
        } else {
            CampaignStrategyPageObj.verifyAddStrategySuccessfullyUI(strategiesData.get("name"), strategiesData.get("notificationMessage"), strategiesData.get("description"));
        }
    }

    @And("User navigate to {string} page")
    public void userNavigateToPage(String pagename) {
        if (EnvSetup.USENGM.equalsIgnoreCase("yes")) {
            log.info("Use UI directly");
            if (pagename.equalsIgnoreCase("contacts") || pagename.equalsIgnoreCase("datasource")) {
                log.info("Navigated to " + EnvSetup.OUTBOUND_CONTACTS_UI_BASEURL + "/" + pagename);
                driver.navigate().to(EnvSetup.OUTBOUND_CONTACTS_UI_BASEURL + "/" + pagename);
            } else {
                log.info("Navigated to " + EnvSetup.OUTBOUND_CAMPAIGNS_UI_BASEURL + "/" + pagename);
                driver.navigate().to(EnvSetup.OUTBOUND_CAMPAIGNS_UI_BASEURL + "/" + pagename);
            }
            commonFunction.waitForPageLoadComplete(30, 1);
        } else {
            commonFunction.navigateToOutboundPage(pagename);
        }
    }


    @And("Click Add New Strategy button")
    public void clickAddNewStrategyButton() {
        RestMethodsObj.deleteStrategy(testData.getString("strategy", "name"), "");
        commonFunction.clickButton("add");
    }

    @And("Create strategy by {string}")
    public void createStrategy(String method) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Create Strategy by " + method);
        RestMethodsObj.deleteStrategy(testData.getStrict("strategy", "name"), "");
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        if (method.equalsIgnoreCase("api")) {
            boolean flag = CampaignStrategyPageObj.createStrategyAPI(strategiesData);
            Assert.assertTrue("ERR!!!. The Create Strategy by API unsuccessfully", flag);
            utilityFun.wait(1);
        } else if (method.equalsIgnoreCase("ui")) {
            CampaignStrategyPageObj.createStrategy(strategiesData);
        }
    }

    @And("^Enter valid strategy details$")
    public void enterValidStrategyDetails(Map<String, String> data) {
        TestData<String, String> strategyEdit = new TestData<>();
        strategyEdit.load(data.get("DataFile"), data.get("Data"));
        CampaignStrategyPageObj.enterStrategyFormDetails(strategyEdit.getData("strategy"));
    }

    @Then("User should able to see success message {string}")
    public void userShouldAbleToSeeSuccessMessage(String message) {
        Map<String, String> strategiesData = testData.getData("strategy");
        strategiesData.put("NotificationAlert", message);
        commonFunction.verifyNotification(strategiesData);
    }

    @Then("^User should able to see strategy on to of strategy management screen$")
    public void userShouldAbleToSeeStrategyOnToOfStrategyManagementScreen(Map<String, String> data) {
        testData.load(data.get("DataFile"), data.get("Data"));
        commonFunction.waitForElementClickable(commonFunction.findElementByXpath(locator.getLocator("tableListStrategy")));
        List<WebElement> list = commonFunction.findElementsByXpath(locator.getLocator("tableListStrategy"));
        Assert.assertEquals(testData.getString("strategy", "name"), list.get(1).getAttribute("innerText"));
    }

    @Then("User should not able to see success message {string}")
    public void userShouldNotAbleToSeeSuccessMessage(String message) {
        List<WebElement> alerts2 = commonFunction.presentElements(locator.getLocator("notificationMessage"), 5);
        List<String> alertMessage = new ArrayList<String>();
        if (alerts2 != null) {
            for (WebElement alert : alerts2) {
                log.info("Alert " + alert.getText());
                alertMessage.add(alert.getText());
            }
            Assert.assertFalse("User is able to see success message even though user has not filed mandatory fields", alertMessage.contains(message));
        } else {
            log.info("don't any toast message");
        }
    }

    @Then("^User should able to see Edit strategy page for strategy$")
    public void userShouldAbleToSeeEditStrategyPageForStrategy(Map<String, String> data) {
        testData.load(data.get("DataFile"), data.get("Data"));
        CampaignStrategyPageObj.verifyStrategyPageDetails(testData.getData("strategy"));
    }


    @When("Click Cancel Strategy")
    public void clickCancelStrategy() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignStrategyPageObj.btnCancelStrategy.click();

    }

    @Then("Cancel confirm form is displayed - {string}")
    public void verifyCancelConfirmFormDisplayed(String Display) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (Display.equals("yes")) {
            log.info("Expected: Dialog confirm leave show");
            if (!CampaignStrategyPageObj.dialogConfirmIsDisplay()) {
                log.info("Actual: Dialog confirm leave not show - Failed ");
                Assert.fail("Expected: Dialog confirm leave show /nActual: Dialog confirm leave is not show");
            } else {
                log.info("Dialog confirm leave is show - PASSED ");
            }
        } else {
            log.info("Expected: Dialog confirm leave not show");
            if (!CampaignStrategyPageObj.dialogConfirmIsDisplay()) {
                log.info("Actual: Dialog confirm leave not show - Passed ");
            } else {
                log.info("Dialog confirm leave show - Failed ");
                Assert.fail("Expected: Dialog confirm leave not show /nActual: Dialog confirm leave show");
            }
        }
    }

    @When("Select Cancel confirm form option - {string}")
    public void selectCancelConfirmFormOption(String Option) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (Option) {
            case "Stay on this page":
                CampaignStrategyPageObj.confirmStayStrategy.click();
                break;
            case "Leave this page":
                CampaignStrategyPageObj.confirmLeaveStrategy.click();
                break;
        }
    }

    @Then("Verify page handler after confirm cancel form - {string}")
    public void pageHandlerAfterConfirmCancelForm(String Expected) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Expected: " + Expected);
        String pagename = driver.findElement(By.xpath("//div[@id='pageLayout']")).getAttribute("data-testid");
        if (Expected.equals("Stay on Create strategy page")) {
            log.info("Expected: UI are staying ---- strategyAdd");
            log.info("Actual: UI are staying ---- " + pagename);
            if (!pagename.equals("strategyAdd")) {
                Assert.fail("Page not stay on Create Strategy");
            } else {
                log.info("UI are stay on Create strategy page");
            }
        } else if (Expected.equals("Strategy landing page")) {
            log.info("Expected: UI are staying ---- strategy");
            log.info("Actual: UI are staying ---- " + pagename);
            if (!pagename.equals("strategy-list-page")) {
                Assert.fail("Page not leave to Strategy landing page");
            } else {
                log.info("UI leaved to Strategy landing page");
            }
        }
    }

    @Then("Verify Strategy do not exist")
    public void strateyNotExits() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        if (CampaignStrategyPageObj.StrategyExisted(strategiesData.get("name"), strategiesData.get("type"))) {
            log.info("Strategy in the list even if the user confirm cancel during its creation - FAILED");
            Assert.fail("Strategy in the list even if the user confirm cancel during its creation");
        } else {
            log.info("Strategy do not exist in the list when the user confirm cancel during its creation - PASSED");
        }
    }

    @Then("Verify message on cancel confirm form")
    public void verifyMessageCancelConfirmForm() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        log.info("Message header expected:" + strategiesData.get("msgCancelConfirmHeader"));
        log.info("Message body expected:" + strategiesData.get("msgCancelConfirmBody"));
        String msgCancelConfirmHeader = driver.findElement(By.xpath("//*[@aria-label='dialog']/*[contains(@class,'header')]")).getText();
        String msgCancelConfirmBody = driver.findElement(By.xpath("//*[@aria-label='dialog']//*[contains(@class,'message')]")).getText();
        log.info("Message header actual:" + msgCancelConfirmHeader);
        log.info("Message body actual:" + msgCancelConfirmBody);
        Assert.assertEquals("Message header not match", strategiesData.get("msgCancelConfirmHeader"), msgCancelConfirmHeader);
        Assert.assertEquals("Message body not match", strategiesData.get("msgCancelConfirmBody"), msgCancelConfirmBody);
    }

    @And("User click on Edit option of strategy")
    public void userClickOnEditOptionOfStrategy(Map<String, String> data) {
        testData.load(data.get("DataFile"), data.get("Data"));
        commonFunction.clickTakeMoreActionElipse(testData.getString("strategy", "name"));
        commonFunction.clickAction(testData.getString("strategy", "name"), "Edit");
    }

    @And("^User click on Strategy name link")
    public void userClickOnStrategyNameLink(Map<String, String> data) {
        testData.load(data.get("DataFile"), data.get("Data"));
        commonFunction.clickAnchorLinkOfListViewPage(testData.getString("strategy", "name"));
    }

    @When("User clicks button {string}")
    public void clickButton(String Action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton(Action);
    }


    @And("User clicks Leave this page button")
    public void userClicksButton() {
        commonFunction.clickLeaveThisPageButton();
    }

    @Then("^User should able to see leave this page pop up$")
    public void userShouldAbleToSeeLeaveThisPagePopUp(Map<String, String> data) {
        testData.load(data.get("DataFile"), data.get("Data"));
        String[] popUpDetails = testData.getString("strategy", "leavePageConfirmationDialog").split("\\|");
        commonFunction.verifyConfirmLeaveThisPageDialog(popUpDetails[0], popUpDetails[1], popUpDetails[2], popUpDetails[3]);
    }

    @And("User clicks button Stay on this page")
    public void userClicksButtonStayOnThisPage() {
        commonFunction.clickButton("negative-action");
    }

    @When("User click on navigation link {string}")
    public void userClickOnNavigationLink(String linkName) {
        commonFunction.clickBreadCrumbLink(linkName);
    }

    @When("Open strategy editor using {string}")
    public void openStrategyEditor(String method) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("open strategy editor by " + method);
        String strategyName = testData.getString("strategy", "name");
        if (method.contains("link")) {
            commonFunction.clickAnchorLinkOfListViewPage(strategyName);
            commonFunction.waitForPageLoadComplete(20, 1);
        } else if (method.contains("triple dot")) {
            commonFunction.clickAction(strategyName, "Edit");
            commonFunction.waitForPageLoadComplete(20, 1);
        }
    }

    @Then("Strategy editor should come up")
    public void strategyEditorPageload() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            List<WebElement> namePages = driver.findElements(By.xpath("//a[@data-testid='page']"));
            String pageName = namePages.get(1).getText();
            log.info("Page editor load: " + pageName);
            if (pageName.equals(testData.getString("strategy", "name"))) {
                log.info("Strategy editor load successful");

            } else {
                log.info("Strategy editor cannot load");
                Assert.fail("Strategy editor cannot load");
            }

        } finally {
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        }
    }

    @And("Verify {string} field can modify")
    @And("Verify {string} field cannot modify")
    public void verifyFieldCanModify(String field) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Field should be mofified not expected", CampaignStrategyPageObj.verifyFieldOfStrategyCanModify(field));
    }

    @And("Strategy editor show old data correctly")
    public void verifyOldDataStrategyEditor() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        String nameCurrent = commonFunction.getAttributeField("name", "value");
        String desCurrent = commonFunction.getTextField("description");
        String smsCurent = commonFunction.getTextField("smsText");
        log.info("Verify Name field show correctly");
        log.info("name field -- Expected: " + strategiesData.get("name") + " Actual: " + nameCurrent);
        Assert.assertEquals("name field show not match", strategiesData.get("name"), nameCurrent);
        log.info("Verify description field show correctly");
        log.info("description field -- Expected: " + strategiesData.get("description") + " Actual: " + desCurrent);
        Assert.assertEquals("description field show not match", strategiesData.get("description"), desCurrent);
        log.info("Verify smsText field show correctly");
        log.info("smsText field -- Expected: " + strategiesData.get("smsText") + " Actual: " + smsCurent);
        Assert.assertEquals("smsText field show not match", strategiesData.get("smsText"), smsCurent);
    }


    @When("^Edit .* field$")
    public void editFiledStrategy(Map<String, String> data) {
        TestData<String, String> strategyEdit = new TestData<>();
        strategyEdit.load(data.get("DataFile"), data.get("Data"));
        Assert.assertTrue("ERR!!!. Edit unsuccessfully", CampaignStrategyPageObj.actionOnSpecificStrategy(strategyEdit.getData("strategy")));
        utilityFun.wait(5);
    }

    @Then("Verify notification is displayed")
    public void checkNotification() {
        String acMessage = CampaignManagerPageObj.getToastMessage();
        String exMessage = testData.getString("strategy", "NotificationAlert");
        Assert.assertEquals("Incorrect message!!!", exMessage, acMessage);
    }

    @Then("Verify Strategy is listed")
    public void checkStrategyListed(Map<String, String> data) {
        TestData<String, String> strategyEdit = new TestData<>();
        strategyEdit.load(data.get("DataFile"), data.get("Data"));
        Map<String, String> strategiesDataEdit = (Map) strategyEdit.getData("strategy");
        Assert.assertTrue("ERR!: Strategy is not listed!!!", CampaignStrategyPageObj.StrategyExisted(testData.getData("strategy", "name"), testData.getData("strategy", "type")));
    }

    @Then("User should not able to see {string} pop up")
    public void userShouldNotAbleToSeePopUp(String header) {
        Assert.assertTrue("User should not able to see " + header + " pop up", commonFunction.verifyPopUpPresent(testData.getStrict("strategy", "leavePageConfirmationDialog").split("\\|")[0]));
    }

    @Then("Verify info is correct after edit")
    public void verifyInfoAfterEdit(Map<String, String> data) {
        TestData<String, String> strategyEdit = new TestData<>();
        strategyEdit.load(data.get("DataFile"), data.get("Data"));
        Map<String, String> strategiesDataEdit = (Map) strategyEdit.getData("strategy");
        //go to edit page
        this.openStrategyEditor("triple dot");
        //check user on strategy page
        CampaignStrategyPageObj.verifyUserOnEditStrategyPage(testData.getString("strategy", "name"));
        //verify data in edit page are changed.
        if (strategiesDataEdit.get("description").equals("")) {
            strategiesDataEdit.replace("description", testData.getString("strategy", "description"));
        }
        if (strategiesDataEdit.get("smsText").equals("")) {
            strategiesDataEdit.replace("smsText", testData.getString("strategy", "smsText"));
        }
        this.verifyOldDataStrategyEditorAfterUpdate(strategiesDataEdit);
    }

    @And("^Enter invalid strategy details$")
    public void enterInvalidStrategyDetails(Map<String, String> data) {
        TestData<String, String> strategyEdit = new TestData<>();
        strategyEdit.load(data.get("DataFile"), data.get("Data"));
        Map<String, String> strategiesDataEdit = (Map) strategyEdit.getData("strategy");
        utilityFun.wait(1);
        CampaignStrategyPageObj.enterStrategyFormDetails(strategiesDataEdit);
    }

    @And("^Verify maximum allowed length of the .* field$")
    public void verifyMaximumLengthOfSpecificField(Map<String, String> data) {
        TestData<String, String> strategyEdit = new TestData<>();
        strategyEdit.load(data.get("DataFile"), data.get("Data"));
        Map<String, String> strategiesData = (Map) strategyEdit.getData("strategy");
        String fieldIdName = strategiesData.get("fieldId");
        int maxLength = 0;
        String inputEditValue = null;
        String realValue = null;
        JsonArray result = RestMethodsObj.getStrategyList(strategiesData);
        for (int i = 0; i < result.size(); i++) {
            JsonObject resResult = (JsonObject) result.get(Integer.parseInt(String.valueOf(i)));
            String nameStrategy = resResult.get("name").getAsString();
            if (nameStrategy.equals(strategiesData.get("name"))) {
                if (fieldIdName.equals("name")) {
                    realValue = resResult.get("name").getAsString();
                    maxLength = 40;
                    inputEditValue = strategiesData.get("name");
                }
                if (fieldIdName.equals("description")) {
                    realValue = resResult.get("description").getAsString();
                    maxLength = 128;
                    inputEditValue = strategiesData.get("description");
                }
                if (fieldIdName.equals("smsText")) {
                    realValue = resResult.get("smsText").getAsString();
                    maxLength = 256;
                    inputEditValue = strategiesData.get("smsText");
                }
            }
        }
        while (inputEditValue != null) {
            log.info("Verify maximum allowed length of the " + fieldIdName + " field as expected");
            String expectedValue = inputEditValue.substring(0, maxLength);
            log.info("Expected value: " + expectedValue);
            log.info("Real value: " + realValue);
            Assert.assertEquals("Value of " + fieldIdName + " field does not match", expectedValue, realValue);
            break;
        }
    }

    @And("He is on the {string} page")
    public void verifyStayonPage(String pageName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String xpath = "//li[contains(@class,'link--current')]";
        String linkCurrent = commonFunction.findElementByXpath(xpath).getAttribute("aria-label");
        log.info("User are stay on page:");
        log.info("Actual:" + linkCurrent);
        log.info("Expected:" + pageName);

        Assert.assertEquals("ERR!!!. User are not stay on page:" + linkCurrent, pageName, linkCurrent);
    }

    @Then("All column names are shown correctly")
    public void verifyAllColumnNames() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String xpath = "//div[@data-testid='header-cell']";
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        String[] dataHeaderCells = strategiesData.get("headerCells").split(";");
        List<WebElement> headerCells = driver.findElements(By.xpath(xpath));
        String[] actHeaderCells = new String[headerCells.size()];
        int i = 0;
        for (WebElement headerCell : headerCells) {
            String headerCellTxt = headerCell.getText();
            actHeaderCells[i] = headerCellTxt;
            i++;
        }
        log.info("Verify header column name" + strategiesData.get("headerCells"));
        if (dataHeaderCells.length != actHeaderCells.length) {
            log.info("the number of column names is not as expected");
            log.info("Expected: " + dataHeaderCells.length + "  || Actual: " + actHeaderCells.length);
            Assert.fail("the number of column names is not as expected");

        } else {
            for (i = 0; i < dataHeaderCells.length; i++) {
                for (int j = 0; j < actHeaderCells.length; j++) {
                    if (dataHeaderCells[i].equals(actHeaderCells[j])) {
                        break;
                    }

                    if ((j == actHeaderCells.length) && !dataHeaderCells[i].equals(actHeaderCells[j])) {
                        log.info("The column name expected:" + dataHeaderCells[i] + "is not show on UI");
                        Assert.fail("The column name expected:" + dataHeaderCells[i] + "is not show on UI");
                    }
                }
            }
        }
    }

    @When("User refresh page {int} times")
    public void refresh_Page(int times) {
        log.info("---------------------------------------------------------------------------------------");
        for (int i = 0; i < times; i++) {
            log.info("Reload browser " + times + " times.");
            driver.navigate().refresh();
            utilityFun.wait(1);
        }
    }

    @When("The user click on column heading in the strategy list - column name: {string}")
    public void clickOnColumnHeader(String headerCell) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Click on header cell - " + headerCell);
        String xpath = "//div[@data-testid='header-cell']//span[text()='" + headerCell + "']";
        commonFunction.findElementByXpath(xpath).click();
    }

    @And("Page do not show any error message")
    public void verifyDontAnyAlert() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            List<WebElement> alerts = commonFunction.findElementsByXpath("//div[@class='Toastify__toast-body']");
            if (alerts.size() > 0) {
                for (WebElement alert : alerts) {
                    log.info("the alert is shown - is not expected");
                    log.info(alert.getText());
                }
                Assert.fail("the alert is shown - is not expected");
            } else {
                log.info("Dont have any alerts are shown");
            }
        } catch (Exception e) { //ignore
        }
    }

    @And("Create {int} campaign strategies using API")
    public void createSomeStrategies(int count) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        CampaignStrategyPageObj.createListStrategiesPage(count, strategiesData);
    }

    @Then("Verify Sort icon should be toggle between Up and Down when user click column {string}")
    public void verifySortIconToggleUpDown(String columnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify sort work properly with " + columnName + " column");
        boolean expected = CampaignStrategyPageObj.verifySortIconDisplayInColumn(columnName);
        Assert.assertTrue("Sort icon is not displayed correctly when use click " + columnName
                + " column on contact list landing page", expected);
        log.info("Sort icon displayed correctly when use click " + columnName + " column on contact list landing page");
    }

    @Then("The {string} column of strategy list should be sorted correctly as per the admin's selection")
    public void verifySortedInDesiredOrderAsPerTheAdminSSelection(String columnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.basicSearchOnTable(testData.getStrict("strategy", "name"));
        log.info("Verify sort work properly with " + columnName + " column");
        boolean expected = CampaignStrategyPageObj.verifySortWorkProperly(columnName);
        Assert.assertTrue("Sort can work on contact list landing page", expected);
        log.info(columnName + " sorted in desired order as per the admin's selection.");
    }

    @Then("Default order strategy list of {string} column displayed correctly after toggle Asc and Desc orders and go back to the default")
    public void defaultOrderContactListDisplayedCorrectlyAfterToggleAscAndDescOrdersAndGoBackToTheDefault(String columnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.basicSearchOnTable(testData.getStrict("strategy", "name"));
        List<String> listDefaultBeforeSort = CampaignStrategyPageObj.getListColumnDataOnCurPage(columnName);
        boolean expected = CampaignStrategyPageObj.verifySortIconDisplayInColumn(columnName);
        Assert.assertTrue("Sort icon is not displayed correctly when use click " + columnName
                + " column on contact list landing page", expected);
        List<String> listDefaultAfterSort = CampaignStrategyPageObj.getListColumnDataOnCurPage(columnName);
        boolean result = listDefaultBeforeSort.equals(listDefaultAfterSort);
        Assert.assertTrue("Default order is not display correctly when toggle sort", result);
        log.info("Default order contact list of " + columnName + " column displayed correctly after toggle Asc and Desc orders and go back to the default");
    }

    @And("Delete campaign strategies was created - {int}")
    public void deleteStrategyCreated(int number) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        if (number == 1) {
            CampaignStrategyPageObj.deleteStrategy(strategiesData.get("type"), strategiesData.get("name"), "");
        } else {
            for (int i = 0; i < number; i++) {
                String nameStrategy = strategiesData.get("name") + "_" + i;

                CampaignStrategyPageObj.deleteStrategy(strategiesData.get("type"), nameStrategy, "");
            }
        }
    }

    @And("Create multi strategies by {string}")
    public void createStrategies(String method) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("---------------------------------------------------------------------------------------");
        log.info("Create Strategy by " + method);
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        if (strategiesData.get("type").equalsIgnoreCase("sms")) {
            String[] nameArray = strategiesData.get("name").split("\\|");
            String[] desArray = strategiesData.get("description").split("\\|");
            String[] smsTextArray = strategiesData.get("smsText").split("\\|");
            String[] smsPaceArray = strategiesData.get("smsPace").split("\\|");
            String[] smsPacingTimeUnitArray = strategiesData.get("smsPacingTimeUnit").split("\\|");
            HashMap<String, String> testData1 = new HashMap<String, String>();
            testData1.put("type", "sms");
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
                if (method.equalsIgnoreCase("api")) {
                    boolean flag = CampaignStrategyPageObj.createStrategyAPI(testData1);
                    Assert.assertTrue("ERR!!!. The Create Strategy by API unsuccessfully", flag);
                } else if (method.equalsIgnoreCase("ui")) {
                    Map<String, String> strategyParams = new HashMap<>();
                    strategyParams.put("name", strategiesData.get("name"));
                    strategyParams.put("description", strategiesData.get("description"));
                    strategyParams.put("smsText", strategiesData.get("smsText"));
                    strategyParams.put("smsPace", strategiesData.get("smsPace"));
                    strategyParams.put("smsPacingTimeUnit", strategiesData.get("smsPacingTimeUnit"));
                    CampaignStrategyPageObj.createStrategy(strategyParams);
                    utilityFun.wait(1);
                }
            }

        } else {
            Assert.fail("!!ERR!!: currently ony support SMS type strategy");
        }
    }

    @Then("The default Sort order is on Last modified On in descending order with pageSize {string}")
    public void verifyDefaultSortOrder(String pageSize) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("---------------------------------------------------------------------------------------");

        log.info("Verify Default sort");
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        //select page size
        commonFunction.selectPageSize(pageSize);
        //get number page with pageSize
        int numberPage = commonFunction.getNumberPage();
        //get List strategies that sorted by default on time UpdateOn
        List<String> listStrategiesSortedDefault = CampaignStrategyPageObj.getListStrategiesDefaultSort(strategiesData);
        //verify default sort on all pages
        int index = 0;
        for (int i = 0; i < numberPage; i++) {
            //verify default sort on current page
            log.info("verify default sort on current page " + (i + 1));
            Assert.assertTrue("!!!The default sort is correct!!!", CampaignStrategyPageObj.verifyDefaultSortCurrentPage(listStrategiesSortedDefault, index));
            index = index + Integer.parseInt(String.valueOf(pageSize));
            commonFunction.navigateToPageByClickPageNumber(i + 2);
        }
    }

    @Then("Verify {string} after {string} is on top")
    public void verifyRecordNameShowOnTopCurrentPage(String recordName, String action, Map<String, String> data) {
//        HashMap<String, String> testData1 = utilityFun.readJsonToHashMap(data.get("DataFile"), data.get("Data"));
        TestData<String, String> testData1 = new TestData<>();
        testData1.load(data.get("DataFile"), data.get("Data"));
        log.info(testData1);
        log.info("---------------------------------------------------------------------------------------");
        log.info("Verify " + recordName + " will be shown on top afer " + action);
        if (recordName.equalsIgnoreCase("datasource")) {
            Assert.assertTrue("!!ERR: The strategy " + testData1.getData("datasource", "name") + "is not on top!!!", commonFunction.verifyRecordNameShowOnTopCurrentPage(testData1.getData("datasource", "name")));
        } else if (recordName.equalsIgnoreCase("campaign") || recordName.equalsIgnoreCase("contactlist") || recordName.equalsIgnoreCase("strategy")) {
            Assert.assertTrue("!!ERR: The strategy " + testData1.getData(recordName.toLowerCase(), "name") + "is not on top!!!", commonFunction.verifyRecordNameShowOnTopCurrentPage(testData1.getData(recordName.toLowerCase(), "name")));
        }
    }

    @When("Refresh page {int} times")
    public void refreshPage(int times) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Step : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("---------------------------------------------------------------------------------------");
        log.info("Refresh page: " + times + "times");
        for (int i = 1; i <= times; i++) {
            log.info("Refresh page: " + i + " times");
            String status = commonFunction.refreshPage(60);
            if (!(status.contains("complete") || status.contains("ready"))) {
                Assert.fail("!!ERR: Page strategy list load take long time");
                break;
            }
        }
    }

    @When("The Refresh button is enable in strategy list page")
    public void verifyRefreshButtonEnable() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Verify the refresh button is enable");
        Assert.assertTrue("!!ERR: Verify the refresh button is not enable", CampaignStrategyPageObj.verifyRefreshButtonEnable());
    }

    @And("Clicking refresh button")
    public void clickToRefreshButton() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("refreshTable");
        utilityFun.wait(1);
    }

    @And("Verify current page size is {int}")
    public void verifyCurrentPageSize(int size) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Expected page size: " + size);
        log.info("Actual page size: " + commonFunction.getCurrentPageSize());
        Assert.assertEquals("!!ERR: current page size is incorrect", commonFunction.getCurrentPageSize(), size);
    }

    @And("Verify current page number is {int}")
    public void verifyCurrentPageNum(int pageNum) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Expected page number: " + pageNum);
        log.info("Actual page number: " + commonFunction.getCurrentPageNumber());
        Assert.assertEquals("!!ERR: current page number is incorrect", commonFunction.getCurrentPageNumber(), pageNum);
    }

    @And("Navigate to page {string}")
    public void clickPageNum(String pageNum) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int pageNumber = 1;
        if (pageNum.equalsIgnoreCase("lasted")) {
            pageNumber = commonFunction.getNumberPage() - 1;
        } else {
            pageNumber = Integer.parseInt(pageNum);
        }
        commonFunction.navigateToPageByClickPageNumber(pageNumber);
        utilityFun.wait(2);
    }

    @And("Select page size {string}")
    public void selectPageSize(String size) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.selectPageSize(size);
        utilityFun.wait(2);
    }

    @And("Clean up Strategies after tested")
    public void cleanUpStrategies(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        TestData<String, String> testData1 = new TestData<>();
        testData1.load(data.get("DataFile"), data.get("Data"));
        Assert.assertTrue("!!ERR: Something went wrong during deleted strategies!!!", CampaignStrategyPageObj.deleteMultiStrategies(testData1.getData("strategy")));
    }

    @Then("Verify {string} should be displayed")
    public void verifyDisplayedOptions(String option) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (option) {
            case "page size options": {
                Assert.assertTrue("Page size options not display", commonFunction.isDisplayed("pageSizeDropdown"));
            }
            break;
            case "page number": {
                Assert.assertTrue("List page number not display", commonFunction.isDisplayed("page_1"));
            }
            break;
            case "previous and next button": {
                Assert.assertTrue("Next page button not display", commonFunction.isDisplayed("nextPage"));
                Assert.assertTrue("Previous page button not display", commonFunction.isDisplayed("previousPage"));
            }
            break;
            case "page detail": {
                Assert.assertTrue("Next page button not display", commonFunction.isDisplayed("pageDetails"));
            }
            break;
            case "Search box": {
                Assert.assertTrue("Search box does not display", commonFunction.isDisplayed("searchTable"));
            }
            break;
            case "Clear search option": {
                Assert.assertTrue("Search box does not display", commonFunction.isDisplayed("clearSearch"));
            }
        }
    }

    @And("Cleanup all strategies data")
    public void cleanupAllStrategiesData() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RestMethodsObj.deleteAllStrategies();
    }

    @And("Verify a message {string} is displayed")
    public void verifyErrorMessageIsDisplayed(String errorMessage) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyMessageWithNoRecord(errorMessage);
    }

    @And("Verify header field {string} is display")
    public void verifyHeaderFieldIsDisplay(String headerName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] listHeaderName = headerName.split(",");
        for (String name : listHeaderName) {
            commonFunction.verifyHeaderLabelDisplay(name);
        }
    }


    @Then("Verify default page size as {string}")
    public void getDefaultPageSize(String pageSize) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int actPageSize = commonFunction.getCurrentPageSize();
        Assert.assertEquals("Currently default page size is " + actPageSize + ", not as expected " + pageSize, actPageSize, Integer.parseInt(pageSize));
    }

    @Then("Verify user be able to change page size to {string}")
    public void verifyUserBeAbleToChangePageSize(String pageSizeList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] pageSizeValue = pageSizeList.split(",");
        for (String select : pageSizeValue) {
            commonFunction.selectPageSize(select);
            int currentSize = commonFunction.getCurrentPageSize();
            Assert.assertEquals("Page size is not displayed as expected", Integer.parseInt(select), currentSize);
        }
    }

    @Then("Verify user stay on page {string}")
    public void verifyUserStayOnSelectedPage(String pageNumber) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.isDisplayed("page_" + pageNumber);
        utilityFun.wait(1);
        boolean pageElement = commonFunction.findElementByXpath("//button[@data-testid='page_" + pageNumber + "']").getAttribute("class").contains("secondary");
        Assert.assertTrue("Use does not stay on selected page", pageElement);
    }

    @Then("Change the page size to {string} and verify number of records on per page")
    public void changeThePageSizeAndVerifyNumberOfRecordsOnPerPage(String pageSizeList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int totalRecords = commonFunction.countAllRecord();
        String[] pageSizes = pageSizeList.split(",");
        for (String select : pageSizes) {
            commonFunction.selectPageSize(select);
            utilityFun.wait(2);
            commonFunction.verifyPageDetail(select, totalRecords);
        }
    }


    @Then("Change the page size to {string} and verify number of page")
    public void changeThePageSizeToAndVerifyNumberOfPage(String pageSizeList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int totalRecords = commonFunction.countAllRecord();
        String[] pageSizeValue = pageSizeList.split(",");
        for (String pageSize : pageSizeValue) {
            commonFunction.selectPageSize(pageSize);
            utilityFun.wait(1);
            commonFunction.verifyNumberOfPage(pageSize, totalRecords);
        }
    }

    @Then("Verify an error will shown if user click too many times on the step {string} option with page as {string}")
    public void clickManyTimesOnStepUpOptionAndVerifyErrorMessage(String stepOption, String pageSizeList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement element = commonFunction.findElementByDataID("goToPage");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        log.info("Get total records");
        int totalRecords = commonFunction.countAllRecord();
        String[] pageSizeValue = pageSizeList.split(",");
        for (String pageSize : pageSizeValue) {
            commonFunction.selectPageSize(pageSize);
            if (commonFunction.presentElement(locator.get("goToPage"), 3) != null) {

                log.info("Get total pages corresponding to page size");
                int totalPages = commonFunction.numberOfPageWithPageSize(pageSize, totalRecords);
                int currentSize = commonFunction.getCurrentPageSize();
                Assert.assertEquals("Page size is not displayed as expected", Integer.parseInt(pageSize), currentSize);
                for (int i = 1; i <= totalPages + 1; i++) {
                    log.info("Click many times on the step " + stepOption + " option and verify the error message will be shown with page size" + pageSize);
                    js.executeScript("document.getElementById(\"goToPage\").stepUp()");
                    WebElement goToPageEle = commonFunction.findElementByXpath(locator.getLocator("goToPage"));
                    goToPageEle.sendKeys(Keys.ENTER);
                    commonFunction.waitForPageLoadComplete(30, 3);
                    if (i == (totalPages + 1)) {
                        List<String> toastMessage = commonFunction.getListToastMessage();
                        log.info("toastMessage: " + toastMessage);
                        for (String msg : toastMessage) {
                            Assert.assertEquals("Toast message display incorrectly", "Please enter valid page number", msg);
                        }
                        utilityFun.wait(1);
                    }
                }
                commonFunction.clickButton("refreshTable");
            }
        }
    }

    @And("Verify value of Go To Page box")
    public void verifyValueOfGoToPageBox(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        testData.load(data.get("DataFile"), data.get("Data"));
        WebElement element = commonFunction.findElementByDataID("goToPage");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String currentValue = (String) js.executeScript("return arguments[0].value", element);
        log.info("Value :" + currentValue);
        String expectedValue = testData.getString("strategy", "goToPageValue");
        Assert.assertEquals("Value do not show as expected", expectedValue, currentValue);
    }

    @And("Change the page size to {string} then verify next and previous page button work fine")
    public void changeThePageSizeThenVerifyNextAndPreviousPageButtonWorkFine(String pageSizeList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int totalRecords = commonFunction.countAllRecord();
        String[] pageSizeValue = pageSizeList.split(",");
        for (String pageSize : pageSizeValue) {
            commonFunction.selectPageSize(pageSize);
            int countNumberPage = commonFunction.numberOfPageWithPageSize(pageSize, totalRecords);
            commonFunction.verifyNextPreviousButton(countNumberPage);
        }
    }

    @Then("Verify the sort should happen across all the pages with field as {string} and page size as {string}")
    public void verifySortConsistentAcrossAllPage(String fieldName, String pageSizeList) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean result = commonFunction.verifySortWorkProperlyAcrossAllPages(fieldName, pageSizeList);
        Assert.assertTrue("Sort is not work correctly with different page", result);
    }

    @Then("Verify step {string} option work well with page size as {string}")
    public void verifyStepOptionWorkCorrectly(String stepOption, String pageSize) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement element = commonFunction.findElementByXpath(locator.getLocator("goToPage"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        log.info("Get total records");
        int totalRecords = commonFunction.countAllRecord();
        int expectedTotalRecords = 0;
        log.info("Get total pages corresponding to page size");
        int totalPages = commonFunction.numberOfPageWithPageSize(pageSize, totalRecords);

        switch (stepOption) {
            case "up": {
                commonFunction.selectPageSize(pageSize);
                int currentSize = commonFunction.getCurrentPageSize();
                Assert.assertEquals("Page size is not displayed as expected", Integer.parseInt(pageSize), currentSize);
                for (int i = 1; i <= totalPages; i++) {
                    WebElement goToPageEle = commonFunction.findElementByXpath(locator.getLocator("goToPage"));
                    js.executeScript("document.getElementById(\"goToPage\").stepUp()");
                    goToPageEle.sendKeys(Keys.ENTER);
                    commonFunction.waitForPageLoadComplete(30, 3);
                    WebElement ele = commonFunction.findElementByXpath(locator.getLocator("goToPage"));
                    String pageValue = (String) js.executeScript("return arguments[0].value", ele);
                    log.info("Page value :" + pageValue);
                    Assert.assertEquals("Value do not show as expected", Integer.parseInt(pageValue), i);
                    commonFunction.isDisplayed("page_" + i);
                    utilityFun.wait(3);
                    boolean pageElement = commonFunction.findElementByXpath("//button[@data-testid='page_" + i + "']").getAttribute("class").contains("secondary");
                    Assert.assertTrue("Use does not stay on selected page", pageElement);
                    int recordsOnPerPage = commonFunction.numberRecordOnCurrentPage();
                    expectedTotalRecords += recordsOnPerPage;
                }
                Assert.assertEquals("Total records do not match", totalRecords, expectedTotalRecords);
                break;
            }
            case "down": {
                commonFunction.selectPageSize(pageSize);
                int currentSize = commonFunction.getCurrentPageSize();
                Assert.assertEquals("Page size is not displayed as expected", Integer.parseInt(pageSize), currentSize);
                commonFunction.sendKeysToTextBox("goToPage", String.valueOf(totalPages));
                utilityFun.wait(1);
                commonFunction.isDisplayed("page_" + totalPages);
                utilityFun.wait(1);
                boolean pageElement = commonFunction.findElementByXpath("//button[@data-testid='page_" + totalPages + "']").getAttribute("class").contains("secondary");
                Assert.assertTrue("Use does not stay on selected page", pageElement);
                WebElement ele = commonFunction.findElementByXpath(locator.getLocator("goToPage"));
                String pageValue = (String) js.executeScript("return arguments[0].value", ele);
                log.info("Page value :" + pageValue);
                Assert.assertEquals("Value do not show as expected", Integer.parseInt(pageValue), totalPages);
                int recordsOnPerPage = commonFunction.numberRecordOnCurrentPage();
                expectedTotalRecords += recordsOnPerPage;
                for (int i = totalPages; i > 1; i--) {
                    WebElement goToPageEle = commonFunction.findElementByXpath(locator.getLocator("goToPage"));
                    js.executeScript("document.getElementById(\"goToPage\").stepDown()");
                    goToPageEle.sendKeys(Keys.ENTER);
                    commonFunction.waitForPageLoadComplete(30, 3);
                    ele = commonFunction.findElementByXpath(locator.getLocator("goToPage"));
                    pageValue = (String) js.executeScript("return arguments[0].value", ele);
                    log.info("Page value :" + pageValue);
                    Assert.assertEquals("Value do not show as expected", Integer.parseInt(pageValue), i - 1);
                    commonFunction.isDisplayed("page_" + i);
                    utilityFun.wait(1);
                    pageElement = commonFunction.findElementByXpath("//button[@data-testid='page_" + (i - 1) + "']").getAttribute("class").contains("secondary");
                    Assert.assertTrue("Use does not stay on selected page", pageElement);
                    recordsOnPerPage = commonFunction.numberRecordOnCurrentPage();
                    expectedTotalRecords += recordsOnPerPage;
                }
                Assert.assertEquals("Total records do not match", totalRecords, expectedTotalRecords);
                break;
            }
        }
    }


    @Then("Verify {string} should not be clickable or displayed")
    public void verifyOptionShouldNotBeVisible(String option) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.basicSearchOnTable(testData.getStrict("strategy", "name"));
        switch (option) {
            case "Next page": {
                Assert.assertFalse("Next page option still can click", commonFunction.tryClick(commonFunction.findElementByDataID("nextPage"), 3));
            }
            break;
            case "Previous page": {
                Assert.assertFalse("Next page option still can click", commonFunction.tryClick(commonFunction.findElementByDataID("previousPage"), 3));
            }
            break;
            case "Go To Page": {
                Assert.assertFalse("Go To Page option still displayed", commonFunction.isPresent("goToPage", 3));
            }
            break;
        }
    }

    @And("Click on the next page button")
    public void clickOnNextPageButton() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("nextPage");
    }

    @Then("Send invalid page number and verify error message with page size as {string}")
    public void sendInvalidCharacterAndVerifyErrorMessage(String pageSizeList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Get the maximum value of pages based on the corresponding page size");
        int totalRecords = commonFunction.countAllRecord();
        String[] pageSizeValue = pageSizeList.split(",");
        for (String pageSize : pageSizeValue) {
            commonFunction.selectPageSize(pageSize);
            if (commonFunction.presentElement(locator.get("goToPage"), 3) != null) {
                int totalPages = commonFunction.numberOfPageWithPageSize(pageSize, totalRecords);
                log.info("Total pages: " + totalPages);
                ArrayList<Integer> inputList = new ArrayList<>();
                inputList.add(0);
                inputList.add(totalPages + 1);
                for (Integer pageNumber : inputList) {
                    commonFunction.sendKeysToTextBox("goToPage", String.valueOf(pageNumber));
                    commonFunction.waitForPageLoadComplete(30, 1);
                    List<String> toastMessage = commonFunction.getListToastMessage();
                    log.info("toastMessage: " + toastMessage);
                    for (String msg : toastMessage) {
                        Assert.assertEquals("Toast message display incorrectly", "Please enter valid page number", msg);
                    }
                    utilityFun.wait(3);
                }
            }
        }
    }

    @And("Use click on {string} field to select sort order as {string}")
    public void clickOnSpecificFieldToSelectSortOrder(String fieldNameList, String sortOrder) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement nameColumn = null;
        String[] fieldName = fieldNameList.split(",");
        for (String field : fieldName) {
            switch (sortOrder) {
                case "ascending": {
                    log.info("Verify arrow up icon will be shown");
                    nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + field + "']"));
                    commonFunction.tryClick(nameColumn, 1);
                    commonFunction.waitForPageLoadComplete(10, 1);
                    WebElement sortingArrowUp = commonFunction.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-up')]");
                    Assert.assertTrue("Sort icon is not display correctly on campaign landing page", sortingArrowUp.isDisplayed());
                    break;
                }
                case "descending": {
                    log.info("Verify arrow down icon will be shown");
                    nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + field + "']"));
                    commonFunction.tryClick(nameColumn, 1);
                    commonFunction.waitForPageLoadComplete(10, 1);
                    WebElement sortingArrowDown = commonFunction.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-down')]");
                    Assert.assertTrue("Sort icon is not display correctly on campaign landing page", sortingArrowDown.isDisplayed());
                    break;
                }
                case "default": {
                    log.info("Verify arrow up/arrow down icon will not be shown");
                    nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + field + "']"));
                    commonFunction.tryClick(nameColumn, 1);
                    commonFunction.waitForPageLoadComplete(10, 1);
                    WebElement sortingArrowUp = commonFunction.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-up')]");
                    Assert.assertTrue("Sort icon is not display correctly on campaign landing page", sortingArrowUp.isDisplayed());
                    WebElement sortingArrowDown = commonFunction.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-down')]");
                    Assert.assertTrue("Sort icon is not display correctly on campaign landing page", sortingArrowDown.isDisplayed());
                    break;
                }
            }
            commonFunction.clickButton("refreshTable");
        }
    }

    @When("Click Add New Strategy button, fill the name {string}, Description {string}, SMS Text {string} and click save button")
    public void clickAddNewStrategyButtonFillTheNameDescriptionSMSText(String name, String description, String smsText) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Name: " + name);
        log.info("description: " + description);
        log.info("smsText: " + smsText);
        commonFunction.clickButton("add");
        commonFunction.enterToTheField("Name", name);
        commonFunction.enterToTheField("Description", description);
        commonFunction.enterToTheField("SMS Text", smsText);
        commonFunction.sendKeysToTextBox("smsPace", "20");
        commonFunction.selectDropDownOptionEqual("smsPacingTimeUnit", "Second");
        commonFunction.clickButton("save");
    }

    @SuppressWarnings({"rawtypes", "rawtypes"})
    @And("Verify basic search with {string} column as {string}")
    public void verifyBasicSearch(String columnName, String searchText, Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        testData.load(data.get("DataFile"), data.get("Data"));
        log.info("Get strategy by API");
        ArrayList<String> listStrategy = new ArrayList<>();
        boolean existFlag = false;
        JsonArray result = RestMethodsObj.getStrategyList(testData.getData("strategy"));
        for (int i = 0; i < result.size(); i++) {
            JsonObject resResult = (JsonObject) result.get(Integer.parseInt(String.valueOf(i)));
            listStrategy.add(resResult.get("name").getAsString());
        }
        if (searchText.isEmpty()) {
            boolean existStrFlag = false;
            log.info("SearchText: " + searchText);
            log.info("Column Name: " + columnName);
            log.info("Start searching...");
            ArrayList<String> strategyName = new ArrayList<>();
            commonFunction.sendKeysToTextBox("searchTable", searchText);
            Assert.assertEquals("Search box still exits value", commonFunction.findElementByDataID("searchTable").getAttribute("value"), "");
            commonFunction.waitForPageLoadComplete(20, 2);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            List<WebElement> rows = commonFunction.findElementsByXpath("//table[@data-testid='table']/tbody/tr[*]");
            if (rows.size() != 0) {
                driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                Hashtable[] totalRecords = commonFunction.getListColumnDataOnPage(columnName, "", "", "", "");
                if (columnName.equals("All")) {
                    columnName = "Name";
                }
                for (Hashtable record : totalRecords) {
                    Set keys = record.keySet();
                    for (Object key : keys) {
                        String str = (String) key;
                        if (str.equals(columnName)) {
                            System.out.println("Key: " + str + " & Value: " + record.get(str));
                            strategyName.add(record.get(str).toString());
                        }
                    }
                }
                if (strategyName.size() != 0) {
                    for (String strName : listStrategy) {
                        for (String name : strategyName) {
                            if (name.equals(strName)) {
                                existStrFlag = true;
                                break;
                            }
                        }
                        Assert.assertTrue(strName + " strategy does not exist on the system", existStrFlag);
                        existStrFlag = false;
                    }
                }
            }
        } else {
            for (String strategy : listStrategy) {
                if (strategy.contains(searchText)) {
                    existFlag = true;
                    break;
                }
            }
            if (existFlag) {
                log.info("Verify strategies do exist on system");
                Assert.assertTrue("Verifying the basic search FAILURE!!!", CampaignStrategyPageObj.verifyBasicSearch(columnName, searchText, testData.getData("strategy")));
            } else {
                log.info("Verify strategy does not exist on system");
                Assert.assertFalse("Verifying the basic search FAILURE!!!", CampaignStrategyPageObj.verifyBasicSearch(columnName, searchText, testData.getData("strategy")));
            }
        }
    }


    @SuppressWarnings({"rawtypes", "rawtypes"})
    @Then("Verify edited records data")
    public void verifyUpdatedRecordData(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        testData.load(data.get("DataFile"), data.get("Data"));
        String columnName = testData.getData("strategy", "columnName");
        ArrayList<String> listStrategies = new ArrayList<>();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        List<WebElement> rows = commonFunction.findElementsByXpath("//table[@data-testid='table']/tbody/tr[*]");
        if (rows.size() != 0) {
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            Hashtable[] totalRecords = commonFunction.getListColumnDataOnPage(columnName, "", "", "", "");
            for (Hashtable record : totalRecords) {
                Set keys = record.keySet();
                for (Object key : keys) {
                    String str = (String) key;
                    listStrategies.add(record.get(str).toString());
                }
            }
            if (listStrategies.size() != 0) {
                log.info("Verify the " + listStrategies.get(0) + " strategy will be shown on the top of first page");
                Assert.assertEquals("ERROR!!!The updated strategy was not show on the top of the fist page", listStrategies.get(0), testData.getData("strategy", "name"));
            }
            log.info("Verify content of strategy update as expected");
            JsonArray result = RestMethodsObj.getStrategyList(testData.getData("strategy"));
            for (int i = 0; i < result.size(); i++) {
                JsonObject resResult = (JsonObject) result.get(Integer.parseInt(String.valueOf(i)));
                String nameStrategy = resResult.get("name").getAsString();
                if (nameStrategy.equals(listStrategies.get(0))) {
                    String updatedValue = resResult.get("smsText").getAsString();
                    Assert.assertEquals("Updated value do not match", updatedValue, testData.getData("strategy", "smsText"));
                    break;
                }
            }
        }
    }


    @When("Verify that there is no data in table")
    public void verifyNoRecordOnSystem() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("refreshTable");
        log.info("Get number of record on system");
        int numberOfRecord;
        numberOfRecord = commonFunction.numberRecordOnCurrentPage();
        if (numberOfRecord == 0) {
            log.info("There is no record on the system");
        } else {
            log.info("Starting delete record...");
            RestMethodsObj.deleteAllStrategies();
            numberOfRecord = commonFunction.numberRecordOnCurrentPage();
            Assert.assertEquals("There are some records on system, please checking log", 0, numberOfRecord);
        }
    }

    @When("User clear search string")
    public void clearSearchString() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.clickButton("clearSearch");
        Assert.assertEquals("Search box still exits value", commonFunction.findElementByDataID("searchTable").getAttribute("value"), "");
    }

    @And("User search string as {string}")
    public void setSearchString(String searchText) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Start searching...");
        commonFunction.clickButton("refreshTable");
        commonFunction.sendKeysToTextBox("searchTable", searchText);
        commonFunction.waitForPageLoadComplete(20, 2);
    }

    @When("Verify default options after clicking on Refresh button with {string}")
    public void clickOnRefreshButtonAndVerifyDefaultOptions(String searchType) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        List<WebElement> rows = commonFunction.findElementsByXpath("//table[@data-testid='table']/tbody/tr[*]");
        if (rows.size() != 0) {
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            log.info("Clicking on the Refresh button");
            commonFunction.clickButton("refreshTable");
            commonFunction.waitForPageLoadComplete(20, 2);
            if (searchType.equals("Basic search")) {
                log.info("Verify default value of page size as 20");
                Assert.assertEquals("!!ERR: current page size is incorrect", commonFunction.getCurrentPageSize(), 20);
                log.info("Verify value of the search box will be clear");
                Assert.assertEquals("Search box still exits value", commonFunction.findElementByDataID("searchTable").getAttribute("value"), "");
                log.info("Verify user will be stayed on the first page");
                Assert.assertEquals("!!ERR: current page number is incorrect", 1, commonFunction.getCurrentPageNumber());
            }
        }
    }


    @And("Verify Advance Search option is displayed on campaign strategy")
    public void Verify_Advance_Search_option_is_displayed_on_campaign_strategy() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue(" Advance Search not displayed", commonFunction.isDisplayed("columnFilters"));
    }

    @And("Enable Advance search on campaign strategy")
    public void Enable_Advance_search_on_campaign_strategy() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.advanceSearchenableDisable("Enable");
    }

    @And("Disable Advance search on campaign strategy")
    public void Disable_Advance_search_on_campaign_strategy() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.advanceSearchenableDisable("Disable");
    }

    @Then("Verify columnName is displayed on campaign strategy with values")
    public void verifycolumnNameIsDisplayedOnCampaignStrategyWithValues(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue(commonFunction.verifyAllItemTextInSelect("columnName", data.get("columnName")));
    }

    @Then("Verify columnName is displayed on campaign strategy without values")
    public void verifycolumnNameIsDisplayedOnCampaignStrategyWithoutValues(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertFalse(commonFunction.verifyAllItemTextInSelect("columnName", data.get("columnName")));
    }

    @Then("Verify operator is displayed on campaign strategy with values")
    public void verifyoperatorIsDisplayedOnCampaignStrategyWithValues(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue(commonFunction.verifyAllItemTextInSelect("operator", data.get("operator")));
    }

    @Then("Verify searchValue is displayed on campaign strategy with values")
    public void verifysearchValueIsDisplayedOnCampaignStrategyWithValues(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String sExpValue = data.get("searchvalue") == null ? "" : data.get("searchvalue");
        Assert.assertEquals("Expected value is not present", sExpValue, commonFunction.getTextField("searchValue"));
    }

    @Then("Verify Advance search options are not displayed on campaign strategy")
    public void Verify_Advance_search_options_are_not_displayed_on_campaign_strategy() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String[] sFields = {"columnName", "operator", "searchValue"};
        commonFunction.verifyFieldsNotDisplayedPage(sFields);
    }

    @And("Create campaign strategy using API")
    public void createCampaignStrategyUsingAPI() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategyData = testData.getData("strategy");
        RestMethodsObj.createStrategy(strategyData);
    }

    @Then("Verify value of Search Box as {string}")
    public void getSearchString(String searchText) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Start fetching value...");
        Assert.assertEquals("Searching text do not match", commonFunction.findElementByDataID("searchTable").getAttribute("value"), searchText);
    }


    @Then("Verify columnName default selected on campaign strategy")
    public void verifyColumnNameDefaultSelectedOnCampaignStrategy(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue(commonFunction.verifySelectedItemTextInSelect("columnName", data.get("columnName")));
    }

    @Then("Verify operator default selected on campaign strategy")
    public void verifyOperatorDefaultSelectedOnCampaignStrategy(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue(commonFunction.verifySelectedItemTextInSelect("operator", data.get("operator")));
    }

    @And("Apply Advance search on campaign strategy")
    public void applyAdvanceSearchOnCampaignStrategy(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.applyAdvanceSearch(data.get("columnName"), data.get("operator"), data.get("searchValue"));
        utilityFun.wait(10);
    }

    @And("Create campaign strategies using API")
    public void createCampaignStrategiesUsingAPI(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignStrategyPageObj.createMultipleStrategies(data.get("name"), data.get("type"));
    }

    @And("Clean campaign strategies was created")
    public void CleanCampaignStrategiesWasCreated(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignStrategyPageObj.deleteMultipleStrategies(data.get("StrategyNames"), data.get("StrategyType"));
    }

    @Then("Verify expected strategies on campaign strategy")
    public void verifyExpectedStrategiesOnCampaignStrategy(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyAdvanceSearchResult(data.get("StrategyNames"), true);
    }

    @Then("Verify Not strategies expected on campaign strategy")
    public void verifyNotStrategiesExpectedOnCampaignStrategy(Map<String, String> data1) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        commonFunction.verifyAdvanceSearchResult(data1.get("StrategyNames"), false);
    }

    @And("Quick search strategy on campaign strategy")
    public void quickSearchStrategyOnCampaignStrategy(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Quick search strategy <" + data.get("StrategyNames") + ">");
        commonFunction.sendKeysToTextBox("searchTable", data.get("StrategyNames"));
    }

    @Then("Verify searched strategy length")
    public void verifySearchedStrategyLength(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int iSearchedTextLength = commonFunction.getAttributeField("searchTable", "value").length();
        log.info("Expected Searched Text String<" + data.get("SearchStringLength") + ">");
        log.info("Actual Searched Text String length <" + iSearchedTextLength + ">");
        Assert.assertEquals(data.get("SearchStringLength").toString(), Integer.toString(iSearchedTextLength));
    }

    @Then("Verify {string} field is displayed while {string} strategy")
    public void verifyFiledPaceAndTimeUnit(String field, String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("In page: " + action + " strategy");
        log.info("Verify " + field + " is existed");
        if (field.equalsIgnoreCase("Pace")) {
            Assert.assertNotNull("!!!ERR: " + field + " is not existed!!!", commonFunction.presentElement("smsPace", 2));
        }
        if (field.equalsIgnoreCase("timeunit")) {
            Assert.assertNotNull("!!!ERR: " + field + " is not existed!!!", commonFunction.presentElement("smsPacingTimeUnit", 2));
        }
    }

    @Then("Verify {string} value is displayed in TimeUnit while {string} strategy")
    public void verifyValueTimeUnit(String Value, String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("In page: " + action + " strategy");
        List<String> options = commonFunction.getAllSelectDropDownOptions("smsPacingTimeUnit");
        Assert.assertTrue("!!!ERR: " + Value + " is not displayed in dropdown!!!", options.contains(Value));
    }

    @Then("Verify The default value of TimeUnit is {string}")
    public void verifyDefaultValueTimeUnit(String Value) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String selectedValue = commonFunction.getSelectedOptionInDropDown("smsPacingTimeUnit");
        Assert.assertEquals("!!ERR: default value of TimeUnit is incorrect!!!", selectedValue, Value);
    }

    @And("Verify error {string} when input invalid values of Pace field while {string} strategy")
    public void verifyInvalidValuePaceField(String fieldErrorMessage, String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Error message expected: " + fieldErrorMessage);
        log.info("Action: " + action);
        if (commonFunction.isEnable("save")) {
            commonFunction.clickButton("save");
        }
        Assert.assertFalse("Save button should be disabled when input invalid value of pace field", commonFunction.isEnable("save"));
        commonFunction.verifyFieldError("smsPace", fieldErrorMessage);
    }

    @And("Verify error does not show when input valid values of Pace field while {string} strategy")
    public void verifyValidValuePacefield(String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Action: " + action);
        Assert.assertTrue("!!!ERR: error message shown when input valid value of pace field while!!", commonFunction.getErrorHintField("pacing") == null);
    }

    @And("Verify User can save strategy with valid value of Pace field while {string} strategy")
    public void verifyCanSaveStrategySuccessfull(String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Action: " + action + " strategy");
        commonFunction.verifyNotification(testData.getData("strategy"));
    }

    @And("Update timeunit to {string}")
    public void updateTimeunitStrategy(String unit) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Updating timeunit to " + unit);
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        commonFunction.selectDropDownOptionEqual("smsPacingTimeUnit", unit);
        commonFunction.waitForPageLoadComplete(10, 1);
        if (strategiesData.containsKey("smsPaceUpdate")) {
            commonFunction.sendKeysToTextBox("smsPace", strategiesData.get("smsPaceUpdate"));
        } else {
            commonFunction.sendKeysToTextBox("smsPace", strategiesData.get("smsPace"));
        }
        commonFunction.waitForPageLoadComplete(10, 1);

    }

    @And("verify the pace sms maximum is {int} per {string}")
    public void verifyPaceSMSMaximum(int maxValue, String timeUnit) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Updating timeunit to " + timeUnit);
        commonFunction.selectDropDownOptionEqual("smsPacingTimeUnit", timeUnit);
        log.info("Verify have error hint when user input value greater" + maxValue);
        commonFunction.sendKeysToTextBox("smsPace", Integer.toString(maxValue + 5));
        if (commonFunction.getErrorHintField("smsPace") != null) {
            log.info("Error hint show when user input value greater max value - error hint: \n" + commonFunction.getErrorHintField("smsPace"));
            Assert.assertFalse("button save not disable", commonFunction.isEnable("save"));
        } else {
            Assert.fail("Error hint not show when input value greater max value -- FAILED");
        }
        log.info("Verify have error hint not when user input value smaller" + maxValue);
        commonFunction.sendKeysToTextBox("smsPace", Integer.toString(maxValue - 5));
        if (commonFunction.getErrorHintField("smsPace") != null) {
            Assert.fail("Error hint show when user input value smaller max value");
        } else {
            log.info("Verify have error hint not when user input value smaller - PASSED");
            Assert.assertTrue("button save are disable when input value smaller max value", commonFunction.isEnable("save"));
        }
    }

    @Then("Verify Non Numeric characters are not allowed in Pace filed while {string} strategy")
    public void verifyNonNumbericCharsNotAllowed(String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("In page: " + action + " strategy");
        if (action.equalsIgnoreCase("creating")) {
            String paceFiledValue = commonFunction.getTextField("smsPace");
            Assert.assertTrue("!!ERR: Pace field is not null: " + paceFiledValue, paceFiledValue.isEmpty());
        } else if (action.equalsIgnoreCase("updating")) {
            commonFunction.sendKeysToTextBox("smsPace", testData.getString("strategy", "smsPaceUpdate"));
            String paceFiledValue = commonFunction.getTextField("smsPace");
            Assert.assertTrue("!!ERR: Pace field is not null: " + paceFiledValue, paceFiledValue.isEmpty());
        } else {
            log.info("Invalid parameter in Steps - input 'creating' or 'updating'");
            Assert.fail();
        }
    }

    @And("update pace field and timeunit")
    public void updatePaceAndTimeUnit() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        if (strategiesData.containsKey("smsPacingTimeUnitUpdate")) {
            commonFunction.selectDropDownOptionEqual("smsPacingTimeUnit", testData.getString("strategy", "smsPacingTimeUnitUpdate"));
            commonFunction.waitForPageLoadComplete(10, 1);
        }
        commonFunction.waitForPageLoadComplete(10, 1);
        commonFunction.sendKeysToTextBox("smsPace", testData.getString("strategy", "smsPaceUpdate"));
        commonFunction.waitForPageLoadComplete(10, 1);

    }

    @Then("Verify pace field and timeunit field keep in strategy editor")
    public void verifyPaceAndTimeunitKeepInStrategyEditor() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering Step: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> strategiesData = (Map) testData.getData("strategy");
        if (strategiesData.containsKey("smsPaceUpdate")) {
            String paceFiledValueAct = commonFunction.getAttributeField("smsPace", "value");
            String timeUnitAct = commonFunction.getSelectedOptionInDropDown("smsPacingTimeUnit");
            log.info("Verify Pace field value +++ Expected: " + strategiesData.get("smsPaceUpdate") + " Actual: " + paceFiledValueAct);
            Assert.assertEquals("pace field not keep in strategy editor", paceFiledValueAct, strategiesData.get("smsPaceUpdate"));
            log.info("Verify Pace time unit value +++ Expected: " + strategiesData.get("smsPacingTimeUnitUpdate") + " Actual: " + timeUnitAct);
            Assert.assertEquals("Timeunit field not keep in strategy editor", timeUnitAct, strategiesData.get("smsPacingTimeUnitUpdate"));
        } else {
            String paceFiledValueAct = commonFunction.getAttributeField("smsPace", "value");
            String timeUnitAct = commonFunction.getSelectedOptionInDropDown("smsPacingTimeUnit");
            log.info("Verify Pace field value +++ Expected: " + strategiesData.get("smsPace") + " Actual: " + paceFiledValueAct);
            Assert.assertEquals("pace field not keep in strategy editor", paceFiledValueAct, strategiesData.get("smsPace"));
            log.info("Verify Pace time unit value +++ Expected: " + strategiesData.get("smsPacingTimeUnit") + " Actual: " + timeUnitAct);
            Assert.assertEquals("Timeunit field not keep in strategy editor", timeUnitAct, strategiesData.get("smsPacingTimeUnit"));
        }
    }

    @And("Create strategy by API new Json")
    public void create_Strategy_By_API_newJson() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Create Strategy by API");
        Map<String, String> strategyData = testData.getData("strategy");
        log.info(strategyData);
        boolean flag = CampaignStrategyPageObj.createStrategyAPI(strategyData);
        Assert.assertTrue("ERR!!!. The Create Strategy by API unsuccessfully", flag);
    }

    @When("Enter the invalid character using the Num Lock external keyboard for Pace field")
    public void enterTheInvalidCharacterUsingTheNumLockExternalKeyboardForPaceField() {
        commonFunction.enterAndVerifyField("Pace", Keys.SUBTRACT, String::isEmpty);
        commonFunction.enterAndVerifyField("Pace", "-", String::isEmpty);
    }

    @Then("Verify error message displayed for mandatory {string} field")
    public void verifyErrorMsgField(String field, Map<String, String> data) {
        HashMap<String, String> testData1 = utilityFun.readJsonToHashMap(data.get("DataFile"), data.get("Data"));
        testData1.keySet().forEach((key) -> {
            commonFunction.verifyFieldError(key, testData1.get(key));
        });
    }

    @Then("Clean up data create strategy")
    public void cleanUpDataCreateStrategy() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (testData != null) {
            String[] strategyNameList = testData.getString("strategy", "name").split("\\|");
            for (String strategyName : strategyNameList) {
                RestMethodsObj.deleteStrategy(strategyName, "");
            }
        }
    }

    @And("Strategy editor show old data correctly after update")
    public void verifyOldDataStrategyEditorAfterUpdate(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String nameCurrent = commonFunction.getAttributeField("name", "value");
        String desCurrent = commonFunction.getTextField("description");
        String smsCurent = commonFunction.getTextField("smsText");
        log.info("Verify Name field show correctly");
        log.info("name field -- Expected: " + data.get("name") + " Actual: " + nameCurrent);
        Assert.assertEquals("name field show not match", data.get("name"), nameCurrent);
        log.info("Verify description field show correctly");
        log.info("description field -- Expected: " + data.get("description") + " Actual: " + desCurrent);
        Assert.assertEquals("description field show not match", data.get("description"), desCurrent);
        log.info("Verify smsText field show correctly");
        log.info("smsText field -- Expected: " + data.get("smsText") + " Actual: " + smsCurent);
        Assert.assertEquals("smsText field show not match", data.get("smsText"), smsCurent);
    }

    @And("Verify do not any error message when edit Pace field with sequentially each value {string} to Pace field")
    public void verifyValidPaceFieldEdit(String value) {
        String[] valueCheck = value.split("\\|");
        for (int i = 0; i < valueCheck.length; i++) {
            log.info("verify input value " + valueCheck[i] + " to Pace field then save");
            commonFunction.sendKeysToTextBox("smsPace", valueCheck[i]);
            log.info("verify don't any error message hint when input value " + valueCheck[i]);
            log.info("Error message hint: " + commonFunction.getErrorHintField("smsPace"));
            Assert.assertNull(commonFunction.getErrorHintField("smsPace"));
        }
    }

    @Then("Verify Pace value and no error message with using arrow key to change Pace field at Min and Max value - {string} unit")
    public void verifyArrowKeytoChangePaceField(String timeUnit) {
        String maxValue = null;
        String minValue = "1";
        WebElement paceField = commonFunction.findElementByDataID("smsPace");
        String paceValue = "";
        switch (timeUnit) {
            case "Second" -> maxValue = "40";
            case "Minute" -> maxValue = "2400";
            case "Hour" -> maxValue = "144000";
        }
        log.info("Check enter arrow up to pace field - with min Value");
        commonFunction.sendKeysToTextBox("smsPace", minValue);
        paceField.sendKeys(Keys.ARROW_UP);
        paceValue = paceField.getAttribute("value");
        log.info("Pace vale expected: " + String.valueOf(Integer.valueOf(minValue) + 1) + " --- Acutal: " + paceValue);
        Assert.assertEquals(paceValue, String.valueOf(Integer.valueOf(minValue) + 1));
        log.info("Check error hint - should be null");
        log.info("Error message hint: " + commonFunction.getErrorHintField("smsPace"));
        Assert.assertNull(commonFunction.getErrorHintField("smsPace"));

        log.info("Check enter arrow down to pace field - with min Value");
        commonFunction.sendKeysToTextBox("smsPace", minValue);
        paceField.sendKeys(Keys.ARROW_DOWN);
        paceValue = paceField.getAttribute("value");
        log.info("Pace vale expected: " + minValue + " --- Acutal: " + paceValue);
        Assert.assertEquals(paceValue, minValue);
        log.info("Check error hint - should be null");
        log.info("Error message hint: " + commonFunction.getErrorHintField("smsPace"));
        Assert.assertNull(commonFunction.getErrorHintField("smsPace"));

        log.info("Check enter arrow up to pace field - with max Value");
        commonFunction.sendKeysToTextBox("smsPace", maxValue);
        paceField.sendKeys(Keys.ARROW_UP);
        paceValue = paceField.getAttribute("value");
        log.info("Pace vale expected: " + maxValue + " --- Acutal: " + paceValue);
        Assert.assertEquals(paceValue, maxValue);
        log.info("Check error hint - should be null");
        log.info("Error message hint: " + commonFunction.getErrorHintField("smsPace"));
        Assert.assertNull(commonFunction.getErrorHintField("smsPace"));

        log.info("Check enter arrow down to pace field - with max Value");
        commonFunction.sendKeysToTextBox("smsPace", maxValue);
        paceField.sendKeys(Keys.ARROW_DOWN);
        paceValue = paceField.getAttribute("value");
        log.info("Pace vale expected: " + String.valueOf(Integer.valueOf(maxValue) - 1) + " --- Acutal: " + paceValue);
        Assert.assertEquals(paceValue, String.valueOf(Integer.valueOf(maxValue) - 1));
        log.info("Check error hint - should be null");
        log.info("Error message hint: " + commonFunction.getErrorHintField("smsPace"));
        Assert.assertNull(commonFunction.getErrorHintField("smsPace"));
    }

    @And("Delete campaign strategy was created")
    public void deleteCampaignStrategyWasCreated() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        CampaignStrategyPageObj.deleteStrategy(testData.getData("strategy", "type"), testData.getData("strategy", "name"), "");
    }
}
