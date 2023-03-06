package com.avaya.outbound.frame;

import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
import com.avaya.outbound.lib.support.TestData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;

import java.util.Map;


public class NGMPortal extends CommonFunction {
    private final Logger log = LogManager.getLogger(NGMPortal.class);
    public Locator locator = new Locator(this);


    public NGMPortal(WebDriver driver) {
        super(driver);
    }

    /**
     * Method to login work spaces
     *
     * @param testData
     */
    public void loginWorkspaces(TestData testData) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        waitForPageLoadComplete(20);
        Map<String, String> tables = determineCurrentPage();
        String state = tables.getOrDefault("state", "");
        tryLoginFromState(state);
    }

    public void tryLoginFromState(String state) {
        if (state.isEmpty()) {
            String workspaceURL = "https://%s/services/ApplicationCenter".formatted(EnvSetup.NGM_URL);
            driver.navigate().to(workspaceURL);
            waitForPageLoadComplete(60);
            String state1 = determineCurrentPage().getOrDefault("state", "");
            if (state1.isEmpty()) {
                Assert.fail("Current browser does not navigate to NGM");
            } else {
                state = state1;
            }
        }
        if (state.equalsIgnoreCase("EnterUsername")) {
            WebElement userName = findElement(locator.get("userName"));
            sleepInSec(0.2);
            userName.clear();
            sleepInSec(0.2);
            userName.sendKeys(EnvSetup.NGM_USER);
            sleepInSec(0.2);
            locator.get("nextBtn").findElement(driver).submit();
            sleepInSec(0.5);
            waitForPageLoadComplete(20, 1);
            if (presentElement(locator.get("nextBtn"), 1) != null)
                locator.get("nextBtn").findElement(driver).submit();
            state = "SignIn";
        }
        if (state.equalsIgnoreCase("SignIn")) {
            WebElement userName = presentElement(locator.get("userName"), 30);
            if (userName != null) {
                userName.clear();
                sleepInSec(0.2);
                userName.sendKeys(EnvSetup.NGM_USER);
            }
            WebElement password = findElement(locator.get("password"));
            password.clear();
            sleepInSec(0.2);
            password.sendKeys(EnvSetup.NGM_PASSWORD);
            sleepInSec(0.1);
            tryClick(locator.get("signIn"), 60);
            waitForPageLoadComplete(60);
            state = "Welcome";
        }
        if (state.equalsIgnoreCase("Welcome")) {
            tryClick(locator.get("administration"), 60);
            presentElement(By.xpath("//nav[@class='neo-leftnav'][@aria-label='Secondary']"), 60);
            state = "Administration";
        }
        if (state.equalsIgnoreCase("Administration")) {
            log.info("User is already logged in to NGM");
        }
    }
}





