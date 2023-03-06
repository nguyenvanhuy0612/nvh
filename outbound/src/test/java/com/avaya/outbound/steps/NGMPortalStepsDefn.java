package com.avaya.outbound.steps;

import com.avaya.outbound.frame.CampaignManagerPage;
import com.avaya.outbound.frame.NGMPortal;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class NGMPortalStepsDefn extends StepContext {
    public Locator locator = new Locator(NGMPortal.class);

    public NGMPortalStepsDefn(Context context) {
        context.init(this);
    }

    @Before(order = 9000, value = "@ListCampaign or @Campaign or @CampaignSummaryView or @UpdateCampaign or @CampaignCompletionProcessing or @Workspaces")
    public void initPageObject() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @After(order = 9000, value = "@ListCampaign or @Campaign or @CampaignSummaryView or @UpdateCampaign or @CampaignCompletionProcessing or @Workspaces")
    public void afterWorkspace(Scenario scenario) {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());
    }

    @After(order = 9001, value = "")
    public void afterWorkSpacePage(Scenario scenario) {
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Test Status: " + scenario.getStatus());

    }

    @Given("Login to workspaces")
    public void loginWorkSpaces() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        NGMPortalObj.loginWorkspaces(testData);

    }

    @Then("User expands {string} menu then open {string} submenu on NGM")
    public void userExpandsMenuThenOpenSubmenuOnNGM(String menu, String submenu) {
        log.info("User open expand menu: " + menu);
        String menuName = String.format(locator.getLocator("menuName"), menu.toLowerCase());
        String cname = commonFunction.findElementByXpath(menuName).getAttribute("class");
        if (!cname.contains("expand")) {
            commonFunction.findElementByXpath(menuName).click();
        }
        log.info("-----------------------------------------------------------------------------------------------------");
        if (submenu.equalsIgnoreCase("data source")) {
            commonFunction.findElementByXpath(locator.getLocator("datasource")).click();
        } else {
            String page = submenu.toLowerCase().replace(" ", "-");
            commonFunction.findElementByXpath(String.format(locator.getLocator("menuName"), page)).click();
        }
        commonFunction.waitForPageLoadComplete(20);
        log.info("Verify tab is opened: " + submenu);
        List<WebElement> tabList = commonFunction.findElementsByXpath("//*[@role='tab']");
        String tabName = tabList.get(tabList.size() - 1).getText();
        Assert.assertEquals("ERR!!!. New tab was not opened within right panel", submenu, tabName);
    }

    @When("User close all current sub tabs and UI show empty workspace")
    public void userCloseAllCurrentSubTabs() {
        log.info("User close all current sub tabs");
        List<WebElement> closebtn = driver.findElements(By.xpath("//*[@aria-label='Close tab']"));
        for (WebElement close : closebtn) {
            log.info("close existing tab");
            commonFunction.clickAnElementUsingJavaScript(close);
        }
        commonFunction.waitForPageLoadComplete(10);
        commonFunction.verifyMessageWithNoRecord("Open a link to create a tab in your workspace");
    }

    @Given("User is in NGM workspace")
    public void userIsInWorkspace() {
        log.info("Verify user is in workspace");
        if (EnvSetup.USENGM.equalsIgnoreCase("no")) {
            NGMPortalObj.loginWorkspaces(testData);
        }
        commonFunction.verifyMessageWithNoRecord("Administration");
        Assert.assertNull("ERR!!!. Outbound Admin header is displayed on NGM", commonFunction.presentElement(By.xpath("//h4[text()='Outbound Admin']"), 5));
    }

    @Then("Verify help icon not visible on NGM")
    public void verifyHelpIconNotVisibleOnNGM() {
        log.info("Verify help icon not visible on NGM");
        Assert.assertNull("ERR!!!. Help icon is displayed on NGM", commonFunction.presentElement(By.xpath(locator.getLocator("helpIcon")), 3));
    }

    @Then("Verify help icon visible on OBaaS")
    public void verifyHelpIconVisibleOnOBaaS() {
        log.info("Verify help icon visible on OBaaS");
        Assert.assertNotNull("ERR!!!. Help icon is not displayed on OBaaS", commonFunction.presentElement(By.xpath(locator.getLocator("helpIcon")), 3));
    }
}

