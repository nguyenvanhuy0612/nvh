package com.avaya.outbound.frame;

import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.UtilityFun;
import com.avaya.outbound.lib.support.Locator;
import com.avaya.outbound.lib.support.TestData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.cucumber.java.Scenario;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v108.network.Network;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CommonFunction {
    // Define Logger
    private final Logger log = LogManager.getLogger(CommonFunction.class);
    private final Locator locator = new Locator();
    public UtilityFun utilityFun = new UtilityFun();
    public RestMethods RestMethodsObj = new RestMethods();
    public RestMethods RestMethodsContactListObj = new RestMethods(EnvSetup.OUTBOUND_CONTACTS_API_BASEURL, EnvSetup.DB_USER, EnvSetup.DB_PASSWORD);
    protected WebDriver driver;
    protected JavascriptExecutor js;

    public CommonFunction(WebDriver driver) {
        initCommon(driver);
    }

    public CommonFunction() {
    }

    private void initCommon(WebDriver driver) {
        if (driver != null) {
            this.driver = driver;
            this.js = (JavascriptExecutor) driver;
        }
    }

    public List<String> getValueHeaderFileImport(String pathFile) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> valueHeader = new ArrayList<>();
        File file = new File(pathFile);
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
            valueHeader.add(br.readLine());
        } catch (Exception e) {
        }
        return valueHeader;
    }

    public List<String> getValueColumnOfFile(String pathFile, String nameColumn) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> columnData = new ArrayList<>();
        File file = new File(pathFile);
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
            List<String> headerData = List.of(br.readLine().split(","));
            int index = headerData.indexOf(nameColumn);
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                columnData.add(data[index]);
            }
        } catch (Exception e) {
        }
        return columnData;
    }

    /**
     * Wait by using JAVA Generics (You can use By, WebElement or String as data-testid)
     *
     * @param element : By, WebElement or String
     * @param sec     : time to wait
     * @return WebElement or null
     */
    public <T> WebElement presentElement(T element, double sec) {
        try {
            if (element.getClass().getName().contains("By")) {
                driver.manage().timeouts().implicitlyWait(Duration.ofMillis((long) sec * 1000));
                return driver.findElement((By) element);
            } else if (element.getClass().getName().contains("WebElement")) {
                driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
                return new WebDriverWait(driver, Duration.ofMillis((long) sec * 1000))
                        .until(ExpectedConditions.visibilityOf((WebElement) element));
            } else if (element.getClass().getName().contains("String")) {
                driver.manage().timeouts().implicitlyWait(Duration.ofMillis((long) sec * 1000));
                return driver.findElement(By.xpath("//*[@data-testid='" + element + "']"));
            }
            log.info("The element type is not one of the type: `By`, `WebElement` or (`String` as data-testid)");
        } catch (Exception e) {
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(EnvSetup.ImplicitWait));
        }
        return null;
    }

    /**
     * @param timeoutInSec
     * @param waitInterval
     * @return
     */
    public String waitForPageLoadComplete(int timeoutInSec, double waitInterval) {
        log.info("Wait for page load...");
        String status = "";
        By loading = new ByAll(
                By.xpath("//div[@data-testid='loader'][contains(@class, 'neo-spinner')]"),
                By.xpath("//div[contains(@class, 'neo-widget__body--loading')]"),
                By.xpath("//div[@data-testid='loader']")
        );
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
            status = new WebDriverWait(this.driver, Duration.ofSeconds(timeoutInSec), Duration.ofMillis((long) (waitInterval * 1000)))
                    .ignoreAll(List.of(NoSuchElementException.class, NotFoundException.class, RuntimeException.class,
                            ElementClickInterceptedException.class, ElementNotInteractableException.class))
                    .until(driver -> {
                        if (js.executeScript("return document.readyState;").toString().equalsIgnoreCase("complete"))
                            if (driver.findElements(loading).size() == 0 && driver.findElements(loading).size() == 0) {
                                System.out.println("complete");
                                return "complete";
                            }
                        System.out.println("not complete");
                        return null;
                    });
        } catch (Exception e) {
            System.out.println("timeout");
            status = "timeout";
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(EnvSetup.ImplicitWait));
        }
        log.info("status: " + status);
        return status;
    }

    /**
     * @param sec
     */
    public void sleepInSec(double sec) {
        try {
            Thread.sleep((long) (sec * 1000));
        } catch (Exception ignored) {
        }
    }

    public boolean tryClick(By by) {
        return tryClick(by, 60);
    }

    public boolean tryClickXpath(String xpath, int sec) {
        return tryClick(By.xpath(xpath), sec);
    }

    public boolean tryClick(By by, int sec) {
        // scroll Into View
        JavascriptExecutor jse2 = (JavascriptExecutor) driver;
        try {
            WebElement element = presentElement(by, sec);
            if (element != null) {
                jse2.executeScript("arguments[0].scrollIntoView()", element);
                element.click();
                return true;
            }
        } catch (Exception e) {
            log.info("Exception while clicking to element " + by);
            log.info(e.getMessage());
        }
        try {
            WebElement element = presentElement(by, sec);
            if (element != null) {
                jse2.executeScript("arguments[0].click()", element);
                return true;
            }
        } catch (Exception e) {
            log.info("Exception while clicking to element using JS " + by);
            log.info(e.getMessage());
        }
        return false;
    }

    public boolean tryClick(String dataID, int sec) {
        return tryClick(By.xpath("//*[@data-testid='" + dataID + "']"), sec);
    }

    /**
     * @param element
     * @param sec
     * @return
     */
    public boolean tryClick(WebElement element, int sec) {
        return presentElement(element, sec) != null && tryClick(element);
    }

    public boolean tryClick(WebElement element) {
        // scroll Into View
        JavascriptExecutor jse2 = (JavascriptExecutor) driver;
        jse2.executeScript("arguments[0].scrollIntoView()", element);
        if (!element.isEnabled()) {
            return false;
        }
        try {
            element.click();
            return true;
        } catch (Exception e) {
            log.info("Exception while clicking to element " + element);
            log.info(e.getMessage());
        }
        try {
            jse2.executeScript("arguments[0].click()", element);
            return true;
        } catch (Exception e) {
            log.info("Exception while clicking to element using javascript " + element);
            log.info(e.getMessage());
        }
        return false;
    }

    public void clickUsingActions(WebElement element) {
        new Actions(driver).moveToElement(element).pause(Duration.ofMillis(300)).click().perform();
    }

    public WebElement getStableElement(String xpathLoc) {
        return new WebDriverWait(driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .ignoreAll(List.of(NotFoundException.class, NoSuchElementException.class, StaleElementReferenceException.class))
                .until(d -> {
                    WebElement element = presentElement(By.xpath(xpathLoc), EnvSetup.ImplicitWait);
                    if (element == null) {
                        return null;
                    }
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    }
                    return null;
                });
    }

    public void clickAnElementUsingJavaScript(WebElement webElement) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", webElement);
    }

    public void clickAnElementUsingJavaScript(String dataIDElement, int timeout) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = presentElement(dataIDElement, timeout);
        if (element != null)
            js.executeScript("arguments[0].click();", element);
    }

    public void clickAnElementUsingJavaScript(By xpath, int timeout) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = presentElement(xpath, timeout);
        if (element != null)
            js.executeScript("arguments[0].click();", element);
    }

    public void scrollIntoView(WebElement element) {
        try {
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            ((JavascriptExecutor) this.driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500);
        } catch (Exception e) {
            log.info("Cannot perform action, " + e.getMessage());
        }
    }

    public void scrollIntoView(By locator) {
        scrollIntoView(this.driver.findElement(locator));
    }

    public void scrollIntoViewAndSendKeys(WebElement element, CharSequence... keys) {
        try {
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            scrollIntoView(element);
            element.sendKeys(keys);
        } catch (Exception e) {
            log.info("Cannot perform action, " + e.getMessage());
        }
    }

    public void scrollIntoViewAndClick(WebElement element) {
        try {
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            scrollIntoView(element);
            element.click();
        } catch (Exception e) {
            log.info("Cannot perform action, " + e.getMessage());
        }
    }

    /**
     * @param timeoutInSec
     * @return
     */
    public String waitForPageLoadComplete(int timeoutInSec) {
        return waitForPageLoadComplete(timeoutInSec, 1);
    }

    private String getItemTextInSelect(String sItem, Select selectedOption) {
        for (WebElement optionElement : selectedOption.getOptions()) {
            if (optionElement.getText().equalsIgnoreCase(sItem)) {
                sItem = optionElement.getText();
                log.info(sItem);
                break;
            }
        }
        return sItem;
    }

    /**
     * description: Method to select the option if given text is present partially
     * or fully in the text
     *
     * @param element
     * @param sItem
     */
    public void selectDropDownOption(WebElement element, String sItem) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Select selectedOption = new Select(element);
        sItem = getItemTextInSelect(sItem, selectedOption);
        selectedOption.selectByVisibleText(sItem);
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * description: Method to select drop down option id the given text matches the
     * drop down option
     *
     * @param element
     * @param sItem
     */
    public void selectDropDownOptionEqual(WebElement element, String sItem) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Select selectedOption = new Select(element);
        sItem = getItemTextInSelect(sItem, selectedOption);
        selectedOption.selectByVisibleText(sItem);
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void selectDropDownOptionEqual(String dropdownDataID, String sItem) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement element = findElementByDataID(dropdownDataID);
        Select selectedOption = new Select(element);
        sItem = getItemTextInSelect(sItem, selectedOption);
        selectedOption.selectByVisibleText(sItem);
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * description: Method to select the dropdown option with given element and
     * selection text by index
     *
     * @param element
     * @param sItem
     */
    public void selectDropDownOptionIndex(WebElement element, String sItem) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Select selectedOption = new Select(element);
        int index = 0;
        for (WebElement optionElement : selectedOption.getOptions()) {
            if (optionElement.getText().equalsIgnoreCase(sItem)) {
                sItem = optionElement.getText();
                log.info(sItem + " " + index);
                break;
            }
            index = index + 1;
        }
        selectedOption.selectByIndex(index);
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * description: Method to select option by value
     *
     * @param element
     * @param sItem
     */
    public void selectDropDownOptionByValue(WebElement element, String sItem) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("element: " + element);
        Select selectedOption = new Select(element);
        sItem = getItemTextInSelect(sItem, selectedOption);
        selectedOption.selectByValue(sItem);
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * @param
     */
    public void navigateToOutboundPage(String pagename) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Navigate to page : " + pagename);
        if (EnvSetup.USENGM.equalsIgnoreCase("yes")) {
            log.info("Use UI from NGM");
            // Close all tabs
            waitForPageLoadComplete(20);
            presentElements("//*[@aria-label='Close tab']", 5).forEach(this::clickAnElementUsingJavaScript);
            if (pagename.equalsIgnoreCase("contacts")
                    || pagename.equalsIgnoreCase("datasource")) {
                String menuname = "//li[@data-sidebar-id='contacts']";
                String cname = getStableElement(menuname).getAttribute("class");
                log.info("cname: " + cname);
                if (!cname.contains("expand")) {
                    log.info("Expand all outbound menus");
                    tryClick(By.xpath(menuname), 60);
                    sleepInSec(0.2);
                }
                if (pagename.equalsIgnoreCase("contacts")) {
                    waitForElementClickable(locator.get("contact_list"));
                    sleepInSec(0.2);
                    tryClick(locator.get("contact_list"), 60);
                    presentElement(By.xpath("//a[@role = 'tab'][@aria-label = 'Contact List']"), 30);
                    sleepInSec(0.2);
                } else if (pagename.equalsIgnoreCase("datasource")) {
                    waitForElementClickable(locator.get("datasource"));
                    sleepInSec(0.2);
                    tryClick(locator.get("datasource"), 60);
                    presentElement(By.xpath("//a[@role = 'tab'][@aria-label = 'Data Source']"), 30);
                    sleepInSec(0.2);
                }
            } else if ((pagename.equalsIgnoreCase("campaigns-manager")
                    || pagename.equalsIgnoreCase("campaign-strategy")
                    || pagename.equalsIgnoreCase("completion-codes"))) {
                String menuname = "//li[@data-sidebar-id='campaigns']";
                String cname = getStableElement(menuname).getAttribute("class");
                log.info("cname: " + cname);
                if (!cname.contains("expand")) {
                    log.info("Expand all outbound menus");
                    tryClick(By.xpath(menuname), 60);
                    sleepInSec(0.2);
                }
                if (pagename.equalsIgnoreCase("campaigns-manager")) {
                    waitForElementClickable(locator.get("campaign_manager"));
                    sleepInSec(0.2);
                    tryClick(locator.get("campaign_manager"), 60);
                    presentElement(By.xpath("//a[@role = 'tab'][@aria-label = 'Campaign Manager']"), 30);
                    sleepInSec(0.2);
                } else if (pagename.equalsIgnoreCase("campaign-strategy")) {
                    waitForElementClickable(locator.get("campaign_strategy"));
                    sleepInSec(0.2);
                    tryClick(locator.get("campaign_strategy"), 60);
                    presentElement(By.xpath("//a[@role = 'tab'][@aria-label = 'Campaign Strategy']"), 30);
                    sleepInSec(0.2);
                } else if (pagename.equalsIgnoreCase("completion-codes")) {
                    waitForElementClickable(locator.get("completion_code"));
                    sleepInSec(0.2);
                    tryClick(locator.get("completion_code"), 60);
                    presentElement(By.xpath("//a[@role = 'tab'][@aria-label = 'Completion Code']"), 30);
                    sleepInSec(0.2);
                }
            } else {
                log.info("NGM not support this page: %s, try to using UI".formatted(pagename));
                navigateOutboundWithOutNGM(pagename);
            }
        } else {
            navigateOutboundWithOutNGM(pagename);
        }
        this.waitForPageLoadComplete(30);
        log.info("Successfully navigated to: " + pagename);
        log.info("---------------------------------------------------------------------------------------");
    }

    public void navigateOutboundWithOutNGM(String pagename) {
        log.info("Use UI directly");
        if (pagename.equalsIgnoreCase("contacts") || pagename.equalsIgnoreCase("datasource")) {
            log.info("Navigated to " + EnvSetup.OUTBOUND_CONTACTS_UI_BASEURL + "/" + pagename);
            driver.navigate().to(EnvSetup.OUTBOUND_CONTACTS_UI_BASEURL + "/" + pagename);
        } else {
            log.info("Navigated to " + EnvSetup.OUTBOUND_CAMPAIGNS_UI_BASEURL + "/" + pagename);
            driver.navigate().to(EnvSetup.OUTBOUND_CAMPAIGNS_UI_BASEURL + "/" + pagename);
        }
        waitForPageLoadComplete(60);
    }

    public WebElement findElement(By by) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Finding Element By " + by + " ");
        log.info("---------------------------------------------------------------------------------------");
        return new WebDriverWait(this.driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * Method to search element by xpath
     *
     * @param xpath
     */
    public WebElement findElementByXpath(String xpath) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Finding Element By xpath " + xpath + " ");
        log.info("---------------------------------------------------------------------------------------");
        return new WebDriverWait(this.driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .ignoring(StaleElementReferenceException.class)
                .until(driver -> driver.findElement(By.xpath(xpath)));
    }

    public WebElement findElementByDataID(String dataID) {
        return findElementByXpath("//*[@data-testid='" + dataID + "']");
    }

    /**
     * @param buttonName
     */
    public void clickButton(String buttonName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Clicking on button " + buttonName + " from page:" + pageName);
        scrollIntoView(findElementByXpath("(//*[@data-testid='%s'])".formatted(buttonName)));
        clickAnElementUsingJavaScript(findElementByXpath("(//*[@data-testid='%s'])".formatted(buttonName)));
        log.info("---------------------------------------------------------------------------------------");
    }

    /**
     * description: Method to enter text in any input for New UI by sending label name and input value
     *
     * @param inputBoxLabel,input
     */
    public void sendKeysToTextBox(String inputBoxLabel, String input) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Entering value in " + inputBoxLabel + " from page:" + pageName);
        scrollIntoView(findElementByXpath("//*[@data-testid=\"%s\"]/..//*[local-name()='input' or local-name()='textarea']".formatted(inputBoxLabel)));
        waitForElementVisible("//*[@data-testid=\"%s\"]/..//*[local-name()='input' or local-name()='textarea']".formatted(inputBoxLabel));
        WebElement inputBox = findElementByXpath("//*[@data-testid=\"%s\"]/..//*[local-name()='input' or local-name()='textarea']".formatted(inputBoxLabel));
        //using action CTR+ A, DELETE to clear
        //inputBox.clear();
        if (input != null) {
            inputBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            sleepInSec(0.2);
            inputBox.sendKeys(Keys.DELETE);
            sleepInSec(0.2);
            inputBox.sendKeys(input);
        }
        log.info("---------------------------------------------------------------------------------------");
    }

    /**
     * Common method to get Page name
     *
     * @return
     */
    public String getPageName() {
        return new WebDriverWait(this.driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .ignoring(StaleElementReferenceException.class)
                .until(driver -> driver.findElement(By.xpath("//*[contains(@class,'pageWrapper')]")).getAttribute("data-testid"));
    }

    public Map<String, String> determineCurrentPage() {
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> tables = new HashMap<>();
        tables.put("mode", "");
        tables.put("state", "");
        tables.put("page", "");
        String url = driver.getCurrentUrl();
        if (url.contains(EnvSetup.OUTBOUND_CONTACTS_UI_BASEURL)) {
            tables.put("mode", "non-ngm");
            tables.put("page", "OUTBOUND_CONTACTS");
            log.info(tables);
            return tables;
        }
        if (url.contains(EnvSetup.OUTBOUND_CAMPAIGNS_UI_BASEURL)) {
            tables.put("mode", "non-ngm");
            tables.put("page", "OUTBOUND_CAMPAIGNS");
            log.info(tables);
            return tables;
        }
        if (!url.contains(EnvSetup.NGM_URL)) {
            tables.put("mode", "none");
            log.info(tables);
            return tables;
        }
        tables.put("mode", "ngm");
        if (url.contains("account-resolver")) {
            tables.put("state", "EnterUsername");
            log.info(tables);
            return tables;
        } else if (url.contains("openid-connect/auth?client_id")) {
            tables.put("state", "SignIn");
            log.info(tables);
            return tables;
        }
        Instant start = Clock.systemDefaultZone().instant();
        Instant end = start.plus(Duration.ofSeconds(10));
        while (end.isAfter(Clock.systemDefaultZone().instant())) {
            if (presentElement(By.xpath("//div[@class='uwf-contact-center__welcome']"), 0.2) != null) {
                tables.put("mode", "ngm");
                tables.put("state", "Welcome");
                break;
            }
            if (presentElement(By.xpath("//nav[@class='neo-leftnav'][@aria-label='Secondary']"), 0.2) != null) {
                tables.put("mode", "ngm");
                tables.put("state", "Administration");
                break;
            }
        }
        log.info(tables);
        return tables;
    }

    public void verifyNotification(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify notification alert");
        String[] NotificationAlert = null;
        if (data.containsKey("NotificationAlert") && !data.get("NotificationAlert").isEmpty()) {
            log.info("Notification Alert ->" + data.get("NotificationAlert"));
            NotificationAlert = data.get("NotificationAlert").split("\\|");
        }
        waitForElementVisible(By.xpath("//div[@class='Toastify__toast-body']"));
        List<WebElement> alerts = findElementsByXpath("//div[@class='Toastify__toast-body']");
        Assert.assertEquals("Number of alert are not matched", NotificationAlert.length, alerts.size());
        List<String> alertMessage = new ArrayList<String>();
        for (WebElement alert : alerts) {
            log.info("Alert " + alert.getText());
            alertMessage.add(alert.getText());
            clickAnElementUsingJavaScript(alert);
            //alert.click();
        }
        if (!data.get("NotificationAlert").isEmpty()) {
            int j = 0;
            for (String alert : NotificationAlert) {
                Assert.assertTrue("Actual :- " + alertMessage.get(j) + "  \nExpected :-" + alert, alertMessage.contains(alert));
                j = j + 1;
            }
        }

    }

    public WebElement waitForElementVisible(WebElement element) {
        long startTime = System.currentTimeMillis();
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .withMessage("Waiting for element to be available to click")
                .until(ExpectedConditions.visibilityOf(element));
        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("Time required to search element in milli seconds: " + elapsedTime);
        return webElement;
    }

    public WebElement waitForElementVisible(By by) {
        long startTime = System.currentTimeMillis();
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .withMessage("Waiting for element to be available to click")
                .until(ExpectedConditions.visibilityOfElementLocated(by));
        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("Time required to search element in milli seconds: " + elapsedTime);
        return webElement;
    }

    public WebElement waitForElementVisible(String xpath) {
        return waitForElementVisible(By.xpath(xpath));
    }

    /**
     * Method to wait for element to be clickable
     *
     * @param element
     */
    public WebElement waitForElementClickable(WebElement element) {
        long startTime = System.currentTimeMillis();
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .withMessage("Waiting for element to be available to click")
                .until(ExpectedConditions.elementToBeClickable(element));
        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("Time required to search element in milli seconds: " + elapsedTime);
        return webElement;
    }

    public WebElement waitForElementClickable(By by) {
        long startTime = System.currentTimeMillis();
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .withMessage("Waiting for element to be available to click")
                .until(ExpectedConditions.elementToBeClickable(by));
        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("Time required to search element in milli seconds: " + elapsedTime);
        return webElement;
    }

    public WebElement waitForElementClickable(String xpath) {
        return waitForElementClickable(By.xpath(xpath));
    }

    /**
     * @param radioButtonName
     */
    public void clickRadioButton(String radioButtonName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info(pageName);
        waitForElementClickable(findElementByXpath("//*[@data-testid='%s']/..//label[@for='%s']".formatted(radioButtonName, radioButtonName)));
        log.info("Clicking on radio button " + radioButtonName + " from page:" + pageName);
        findElementByXpath("//*[@data-testid='%s']/..//label[@for='%s']".formatted(radioButtonName, radioButtonName)).click();
        log.info("---------------------------------------------------------------------------------------");
    }

    /**
     * @param toggleButton
     */
    public void clickToggleButton(String toggleButton) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        scrollIntoView(findElementByXpath("//*[@data-testid='%s']/..".formatted(toggleButton)));
        waitForElementClickable(findElementByXpath("//*[@data-testid='%s']/..".formatted(toggleButton)));
        log.info("Clicking on toggle button " + toggleButton + " from page:" + pageName);
        findElementByXpath("//*[@data-testid='%s']/..".formatted(toggleButton)).click();
        log.info("---------------------------------------------------------------------------------------");
    }

    /**
     * Method to Verify element level errors in UI
     *
     * @param fieldId
     * @param fieldErrorMessage
     */
    public void verifyFieldError(String fieldId, String fieldErrorMessage) {
        utilityFun.wait(1); //Sometimes it takes time to show error, and it returns label. hence wait is required.
        WebElement elements;
        String pageName = getPageName();
        if (!fieldId.contains("wrapper"))
            fieldId = fieldId + "-wrapper";
        if (pageName.equalsIgnoreCase("campaignAddEdit")) {
            elements = driver.findElement(By.xpath("//*[@data-testid='%s']//*[@class='neo-input-hint']".formatted(fieldId)));
        } else {
            elements = driver.findElement(By.xpath("//*[@data-testid='%s']//*[@class='neo-input-hint']".formatted(fieldId)));
        }
        String s = elements.getCssValue("color");
        String c = Color.fromString(s).asHex();
        String message = elements.getAttribute("innerText").trim();
        log.info("\n Verify field error: " + fieldId + "\n Expected Error: " + fieldErrorMessage + " \n Actual Error: " + message);
        Assert.assertEquals(message, fieldErrorMessage.trim());
        Assert.assertEquals("#ab2c2c", c);
    }

    /**
     * Click on more actions
     *
     * @param value
     */
    public void clickTakeMoreActionElipse(String value) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String locator = "//span[text()='%s']/ancestor::tr//div[starts-with(@data-testid,'dropdown')]//*[@data-testid='more-actions']".formatted(value);
        tryClick(By.xpath(locator));
    }

    /**
     * Method to click in Actions. three dots in table
     *
     * @param value
     * @param action
     */
    public void clickAction(String value, String action) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        clickTakeMoreActionElipse(value);
        tryClick(By.xpath("//*[@role='menu']//*[text()='%s']".formatted(action)));
    }

    public void takeScreenShotOnFailedScenario(Scenario scenario) {
        try {
            if ((scenario.isFailed())) {
                log.info("Taking snapshot-----------");
                final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
            }
        } catch (Exception e) {
            log.info(e);
        }
    }

    // TABLE HANDLING
    // Duong
    public List<String> getListColumnDataOnCurPage(String columnName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Column name: " + columnName);
        try {
            waitForPageLoadComplete(30, 0.3);
            int nameColIndex = getColumnIndex(columnName) + 1;
            log.info("nameColIndex: " + nameColIndex);
            String valueLoc = "//tbody/tr[@role='row']/td[@role='cell'][" + nameColIndex + "]";
            log.info("valueLoc: " + valueLoc);
            if (presentElement(By.xpath(valueLoc), 5) == null) {
                return new ArrayList<>();
            }
            ArrayList<String> list = new ArrayList<>();
            return findElementsByXpath(valueLoc).stream().map(WebElement::getText).collect(Collectors.toList());
        } catch (Exception e) {
            log.info("Exception when get data from list: ", e);
            return new ArrayList<>();
        }
    }

    public List<String> getListRowDataOnCurPage(int rowIndex) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("rowIndex: " + rowIndex);
        String rowLoc = "//tbody/tr[" + (++rowIndex) + "]/td[position() > 1]//*[@data-testid='ColumnTextLink' or @data-testid='columnText']";
        log.info("rowLoc: " + rowLoc);
        try {
            if (presentElement(By.xpath(rowLoc), 5) == null)
                return new ArrayList<>();
            return findElementsByXpath(rowLoc).stream().map(WebElement::getText).collect(Collectors.toList());
        } catch (Exception e) {
            log.info("Exception when get data from list: ", e);
            return new ArrayList<>();
        }
    }

    public int getColumnIndex(String columnName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("columnName: " + columnName);
        List<WebElement> elements = findElementsByXpath("//thead[@class='tableFixHeader']//th[@role='columnheader']");
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getText().trim().contentEquals(columnName)) {
                return i;
            }
        }
        return -1;
    }
    // END TABLE HANDLING

    public int getRowIndex(String columnName, String columnValue) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("columnName: " + columnName);
        log.info("columnValue: " + columnValue);
        int nameColIndex = getColumnIndex(columnName) + 1;
        log.info("nameColIndex: " + nameColIndex);
        String rowLoc = "//tbody/tr[@role='row'][./td[" + nameColIndex + "]//*[@data-testid='columnText' and text()='"
                + columnValue + "']]";
        log.info("rowLoc: " + rowLoc);
        if (presentElement(By.xpath(rowLoc), 5) == null) {
            return -1;
        }
        try {
            return Integer.parseInt(findElementByXpath(rowLoc).getAttribute("index"));
        } catch (Exception e) {
            log.info("Exception when getRowIndex: ", e);
            return -1;
        }
    }

    public int getRowIndexOfNameColumn(String columnValue) {
        return getRowIndex("Name", columnValue);
    }

    public int numberRecordOnCurrentPage() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        waitForPageLoadComplete(30, 1);
        if (presentElement(locator.get("rowList"), 5) == null) {
            log.info("Number of record in page: 0 record");
            return 0;
        }
        int records = driver.findElements(locator.get("rowList")).size();
        log.info("Number of record in page: " + records + " records");
        return records;
    }

    public List<String> getListToastMessage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String toastLocXpath = locator.getLocator("toastMessage");
        log.info("TOAST_MESSAGE_DISPLAY_XPATH: " + toastLocXpath);
        try {
            return new WebDriverWait(this.driver, Duration.ofSeconds(30))
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(NullPointerException.class)
                    .until(driver ->
                            presentElements(toastLocXpath, 1).stream().map(WebElement::getText).collect(Collectors.toList())
                    );
        } catch (Exception e) {
            log.info("Exception when getListToastMessage: ", e);
            return new ArrayList<>();
        }
    }

    public String getToastMessage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String toastLocXpath = locator.getLocator("toastMessage");
        log.info("TOAST_MESSAGE_DISPLAY_XPATH: " + toastLocXpath);
        try {
            return new WebDriverWait(this.driver, Duration.ofSeconds(15))
                    .ignoring(JavascriptException.class)
                    .ignoring(NullPointerException.class)
                    .until(driver -> {
                                String mes = js.executeScript("return document.querySelector('div.Toastify__toast-body').innerText;").toString();
                                if (mes == null || mes.isEmpty())
                                    return null;
                                return mes;
                            }
                    );
        } catch (Exception e) {
            log.info("Exception when getToastMessage: ", e);
            return null;
        }
    }

    public String getTextField(String fieldId) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        waitForElementClickable(findElementByXpath("//*[@data-testid='%s']".formatted(fieldId)));
        log.info("Get Text on field " + fieldId + " from page:" + pageName);
        log.info("---------------------------------------------------------------------------------------");
        return findElementByXpath("//*[@data-testid='%s']".formatted(fieldId)).getText();
    }

    public String getAttributeField(String fieldId, String Atribute) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Get Text on field " + fieldId);
        log.info("---------------------------------------------------------------------------------------");
        return findElementByXpath("//*[@data-testid='%s']".formatted(fieldId)).getAttribute(Atribute);
    }

    public boolean isDisplayed(String dataID) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("wait for button '" + dataID + "' from page: " + pageName);
        return presentElement(By.xpath("//*[@data-testid='%s']".formatted(dataID)), 60).isDisplayed();//!= null;
    }

    public boolean isDisplayed(String dataID, int waitSec) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        scrollIntoView(By.xpath("//*[@data-testid='%s']".formatted(dataID)));
        log.info("wait for Element with `data-testid` is '" + dataID + "' from page: " + pageName);
        return presentElement(By.xpath("//*[@data-testid='%s']".formatted(dataID)), waitSec).isDisplayed();
    }

    public boolean isPresent(String dataID, int waitSec) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("wait for Element with `data-testid` is '" + dataID + "' from page: " + pageName);
        return presentElement(By.xpath("//*[@data-testid='%s']".formatted(dataID)), waitSec) != null;
    }

    public boolean pageIsVisible(String pageName, int waitSec) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify page " + pageName + " visible");
        return presentElement(By.xpath("//*[@data-testid='page' and text()='" + pageName + "']"), waitSec) != null;
    }

    /**
     * Method to search elements by xpath
     *
     * @param xpath
     */
    public List<WebElement> findElementsByXpath(String xpath) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Finding Elements By xpath " + xpath + " ");
        log.info("---------------------------------------------------------------------------------------");
        return new WebDriverWait(this.driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
    }

    public List<WebElement> presentElements(String xpath, int sec) {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(sec));
            return driver.findElements(By.xpath(xpath));
        } catch (Exception e) {
            return null;
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(EnvSetup.ImplicitWait));
        }
    }

    /**
     * Method to get text from any text box and compare with expected value
     *
     * @param fieldId,value
     */
    public void verifyTextFieldValue(String fieldId, String value) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("wait for Element with `data-testid` is '" + fieldId + "' from page: " + pageName);
        Assert.assertEquals(findElementByXpath("//*[@data-testid=\"%s\"]/..//*[local-name()='input' or local-name()='textarea']".formatted(fieldId)).getAttribute("value"), value);
    }

    /**
     * Method to click anchor link of any list voew page
     *
     * @param value
     */
    public void clickAnchorLinkOfListViewPage(String value) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        waitForElementClickable(findElementByXpath("//a//*[text()='%s']".formatted(value)));
        log.info("Clicking on Link " + value + " from page:" + pageName);
        findElementByXpath("//a//*[text()='%s']".formatted(value)).click();
    }

    /**
     * Method to click Leave This page button
     */
    public void clickLeaveThisPageButton() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        waitForElementClickable(findElementByXpath("//*[@data-testid=\"positive-action\"]"));
        findElementByXpath("//*[@data-testid=\"positive-action\"]").click();
        log.info("---------------------------------------------------------------------------------------");
    }

    /**
     * Method to verify Leave this page? pop up
     *
     * @param popupHeader,popupMessage,stayButton,leaveButton
     */
    public void verifyConfirmLeaveThisPageDialog(String popupHeader, String popupMessage, String stayButton, String leaveButton) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String header = "//*[@role=\"dialog\"]//h4";
        String message = "//*[@role=\"dialog\"]//p";
        String buttonStayOnThisPage = "//button[@data-testid=\"negative-action\"]";
        String buttonLeaveThisPage = "//button[@data-testid=\"positive-action\"]";
        log.info("Verifying pop up " + popupHeader);
        waitForElementClickable(findElementByXpath(header));
        Assert.assertEquals("Pop up " + popupHeader + " not displayed", findElementByXpath(header).getAttribute("innerText"), popupHeader);
        Assert.assertEquals("Pop up " + popupMessage + " not displayed", findElementByXpath(message).getAttribute("innerText"), popupMessage);
        Assert.assertEquals("Pop up  button " + stayButton + " not displayed", findElementByXpath(buttonStayOnThisPage).getText(), stayButton);
        Assert.assertEquals("Pop up button " + leaveButton + " not displayed", findElementByXpath(buttonLeaveThisPage).getText(), leaveButton);
        log.info("---------------------------------------------------------------------------------------");
    }

    /**
     * Method to get pop up with header is present or not
     *
     * @param popupHeader
     */
    public boolean verifyPopUpPresent(String popupHeader) {
        String header = "//*[@role=\"dialog\"]//h4";
        return this.presentElement(header, 10) == null;
    }

    /**
     * Method to bread crumb link on edit page
     *
     * @param breadCrumbLinkName
     */
    public void clickBreadCrumbLink(String breadCrumbLinkName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Clicking on bread crumb link " + breadCrumbLinkName + " on page " + pageName);
        waitForElementClickable(findElementByXpath("//a[@data-testid='page' and text()='%s']".formatted(breadCrumbLinkName)));
        findElementByXpath("//a[@data-testid='page' and text()='%s']".formatted(breadCrumbLinkName)).click();
        log.info("---------------------------------------------------------------------------------------");
    }

    public DevTools blockRequestViaDevToolsAPI(List<String> listBlocked) {
        if (!(driver instanceof ChromeDriver))
            return null;
        ChromeDriver chromeDriver = (ChromeDriver) driver;
        DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.setBlockedURLs(listBlocked));
        return devTools;
    }

    /**
     * Method to search data in table using basic search option
     *
     * @param sItem
     */
    public void basicSearchOnTable(String sItem) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        sendKeysToTextBox("searchTable", sItem);
        log.info("Wait until table is loaded");
        utilityFun.wait(3);
        waitForPageLoadComplete(30, 1);
    }

    /**
     * Method to return single text data for any colunm on list page
     *
     * @param sourceName
     * @param columnName
     * @return column destination value
     */
    public String getSingleDataFromTable(String sourceName, String columnName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Column name: " + columnName);
        try {
            int nameColIndex = getColumnIndex(columnName);
            log.info("nameColIndex: " + nameColIndex);
            waitForElementClickable(findElementByXpath("//table/tbody/tr//*[text()='" + sourceName + "']//ancestor::tr[@role='row']/td[@role='cell'][" + (nameColIndex + 1) + "]"));
            return driver.findElement(By.xpath("//table/tbody/tr//*[text()='" + sourceName + "']//ancestor::tr[@role='row']/td[@role='cell'][" + (nameColIndex + 1) + "]")).getText();
        } catch (Exception e) {
            log.info("ERR!!! .Can't found: " + sourceName + " in list page");
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to check the element is enable or not
     *
     * @param name
     * @return true/fasle
     */
    public boolean isEnable(String name) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return findElementByXpath("(//*[@data-testid='%s'])".formatted(name)).isEnabled();
    }

    public void clickTab(String tabName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Clicking on tab " + tabName + " on Campaign page");
        waitForElementClickable(findElementByDataID(tabName));
        findElementByDataID(tabName).click();
        log.info("---------------------------------------------------------------------------------------");
    }

    public String getTextOptionIsSelected(String dropDownOptions) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Select ctlSelected = new Select(this.findElementByDataID(dropDownOptions));
        String sItem = ctlSelected.getFirstSelectedOption().getText();
        log.info("sItem is selcted: " + sItem);
        return sItem;
    }

    public boolean clickXpath(String xpath) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Clicking xpath " + xpath + " on page " + pageName);
        WebElement element = presentElement(By.xpath(xpath), 60);
        if (element != null) {
            element.click();
            return true;
        }
        log.info("Cannot click to xpath: " + xpath);
        return false;
    }

    public boolean verifyAttributeAvailable(WebElement element, String attribute) {
        Boolean result = false;
        try {
            String value = element.getAttribute(attribute);
            if (value != null) {
                result = true;
            }
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * Method to select Page size
     *
     * @param size
     */
    public void selectPageSize(String size) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Selecting page size " + size + " on page " + pageName);
        scrollIntoView(this.findElementByDataID("pageSizeDropdown"));
        selectDropDownOptionEqual(this.findElementByDataID("pageSizeDropdown"), size);
        this.waitForPageLoadComplete(20, 1);
    }

    /**
     * Method to verify Page size
     *
     * @param size
     */
    public void verifyPageSize(String size) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Verifying selected page size " + size + " on page " + pageName);
        Assert.assertEquals("Expected page size is " + size + " but found" + findElementByXpath("//select[@data-testid=\"pageSizeDropdown\"]").getText(), findElementByXpath("//select[@data-testid=\"pageSizeDropdown\"]").getText(), size);
        log.info("---------------------------------------------------------------------------------------");
    }

    /**
     * Method to navigate page number by seting value in go to page text box*+
     *
     * @param pageNo
     */
    public void navigateToPageByGoToPage(int pageNo) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Navigating to pagination number  " + pageNo + " on page " + pageName);
        sendKeysToTextBox("goToPage", String.valueOf(pageNo));
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Method to return all selection box options
     *
     * @param selectBoxID
     */
    public List<String> getAllSelectDropDownOptions(String selectBoxID) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Getting all selection box values from " + selectBoxID + " on page " + pageName);
        Select selectBox = new Select(driver.findElement(By.xpath("//select[@data-testid=\"%s\"]".formatted(selectBoxID))));
        // getting the list of options in the dropdown with getOptions()
        List<WebElement> options = selectBox.getOptions();
        ArrayList<String> allOptions = new ArrayList<String>();
        int size = options.size();
        for (int i = 0; i < size; i++) {
            allOptions.add(options.get(i).getText().toString());
        }
        log.info("options" + options);
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        return allOptions;
    }

    /**
     * Method to refresh current page and then wait in "sec"
     *
     * @param sec
     * @return complete or other string
     */
    public String refreshPage(int sec) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        driver.navigate().refresh();
        String status = "";
        status = this.waitForPageLoadComplete(sec);
        return status;
    }

    /**
     * Method to get number page of page list
     *
     * @return number page
     */
    public int getNumberPage() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<WebElement> pages = driver.findElements(By.xpath("//button[contains(@data-testid,'page')]"));
        return pages.size();
    }

    /**
     * Method to return current page
     *
     * @param
     * @return number of current page
     * -1 if the page is empty
     */
    public int getCurrentPageNumber() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<WebElement> pages = driver.findElements(By.xpath("//button[contains(@data-testid,'page')]"));
        for (int i = 0; i < pages.size(); i++) {
            if (pages.get(i).getAttribute("class").contains("secondary")) {
                return (i + 1);
            }
        }
        return -1;
    }

    /**
     * Method to return current page size
     *
     * @param
     * @return current page size
     */
    public int getCurrentPageSize() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement pageSizeElement = driver.findElement(By.xpath("//select[@data-testid=\"pageSizeDropdown\"]"));
        String selectedPageSize = pageSizeElement.getAttribute("value");
        log.info("selectedPageSize: " + selectedPageSize);
        return Integer.parseInt(selectedPageSize);
    }

    /**
     * Method to navigate page number by click to page number
     *
     * @param pageNo
     */
    public void navigateToPageByClickPageNumber(int pageNo) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Navigating to pagination number  " + pageNo + " on page " + pageName);
        if (pageNo > 0 && pageNo < (this.getNumberPage() + 1)) {
            driver.findElement(By.xpath("//button[@data-testid=\"page_" + Integer.toString(pageNo) + "\"]")).click();
            this.sleepInSec(1);
        } else {
            log.info("!!PageNo is incorrect!!");
        }
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Method to get all total record on a page based on the page size
     *
     * @return totalRecords
     */
    public int countAllRecord() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (this.findElementByDataID("page").getText().equalsIgnoreCase("Completion Codes")) {
            log.info("----------------Get all record on landing page---------------");
            return this.numberRecordOnCurrentPage();
        }
        int records = this.numberRecordOnCurrentPage();
        if (records == 0) {
            log.info("Number of record: 0 record");
            return 0;
        }
        String numLoc = "//nav[@aria-label='pagination']/ul[@class='neo-pagination__list']/li";
        if (presentElement(By.xpath(numLoc), 1) == null || presentElements(numLoc, 1).size() == 1) {
            log.info("Number of record: " + records + " record");
            return records;
        }
        findElementByXpath(numLoc + "[last()]").click();
        waitForPageLoadComplete(60, 0.5);
        String lastR = findElementByXpath("//*[@id='pageDetails']/p").getText().trim().replaceFirst("\\d+-", "");
        log.info("Number of record: " + lastR + " record");
        tryClick(By.xpath(numLoc), 5);
        return Integer.parseInt(lastR);
    }

    /**
     * Method to get the number of pages base on page size
     * data contains: pageSize, totalRecords
     *
     * @return numberOfPage
     */
    public int numberOfPageWithPageSize(String pageSize, int totalRecords) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int numberOfPage = (int) Math.ceil((double) totalRecords / Integer.parseInt(pageSize));
        log.info("Number of page is: " + numberOfPage + ", with page size: " + pageSize);
        return numberOfPage;
    }

    /**
     * Method to verify value of page range base on page size
     * data contains: pageSize, totalRecords
     */
    public void verifyNumberOfPage(String pageSize, int totalRecords) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int expectedNumber = this.numberOfPageWithPageSize(pageSize, totalRecords);
        log.info("Expected number of page is: " + expectedNumber);
        int countOnUI = this.findElementsByXpath(locator.getLocator("ULListPageNumber")).size();
        WebElement lastPage = this.findElementByXpath((locator.getLocator("ULListPageNumber") + "[" + countOnUI + "]//button"));
        int actualNumber = Integer.parseInt(lastPage.getText());
        log.info("Actual number of page is: " + actualNumber);
        Assert.assertEquals("Number of page is not correct!", expectedNumber, actualNumber);
    }

    /**
     * Method to verify the header name when landing on a page
     *
     * @param label
     */
    public void verifyHeaderLabelDisplay(String label) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify the label name: '" + label + "' is display on the Header");
        String xpath = "//*[@class='tableFixHeader']//span[text()='" + label + "']";
        log.info("xpath: " + xpath);
        Assert.assertNotNull("The label '" + label + "' is not display", this.presentElement(By.xpath(xpath), 10));
    }

    /**
     * Method to verify the error message while no record show on any page
     *
     * @param message
     */
    public void verifyMessageWithNoRecord(String message) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering into method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("message: " + message);
        String xpath = "//*[text()='" + message + "']";
        log.info("xpath: " + xpath);
        Assert.assertNotNull("The message: '" + message + "' is not display", this.presentElement(By.xpath(xpath), 10));
    }

    /**
     * Method to verify the detail of a page base on page size
     * data contains: pageSize, totalRecords
     */
    public void verifyPageDetail(String pageSize, int totalRecords) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String expectedPageDetailValue;
        int countNumberPage = this.numberOfPageWithPageSize(pageSize, totalRecords);
        int pageSizeValue = Integer.parseInt(pageSize);
        for (int i = 1; i <= countNumberPage; i++) {
            WebElement pageSizeElement = driver.findElement(By.xpath("//div[@id='pageDetails']/p"));
            String actualPageDetailValue = pageSizeElement.getText();
            log.info("Actual page detail at page " + i + " is " + actualPageDetailValue);
            if (i < countNumberPage) {
                expectedPageDetailValue = (pageSizeValue * (i - 1) + 1) + "-" + pageSizeValue * i;
                Assert.assertEquals("Page detail at page " + i + " not match as expected", expectedPageDetailValue, actualPageDetailValue);
                log.info("Expected page detail at page " + i + " is " + expectedPageDetailValue);
                this.clickButton("nextPage");
                utilityFun.wait(5);
                this.waitForPageLoadComplete(30, 1);
            } else {
                expectedPageDetailValue = (pageSizeValue * (i - 1) + 1) + "-" + totalRecords;
                Assert.assertEquals("Page detail at page " + i + " not match as expected", expectedPageDetailValue, actualPageDetailValue);
                log.info("Expected page detail at page " + i + " is " + expectedPageDetailValue);
            }
        }
    }

    /**
     * Method to verify the sort function work or not
     * data contains: columnName, pageSizeList
     *
     * @return TRUE --sort function work well
     * @return FAILURE --sort function doest not work as expected
     */
    public boolean verifySortWorkProperlyAcrossAllPages(String columnName, String pageSizeList) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Get total records...");
        List<String> listData;
        List<String> listDataAfterSort;
        String[] sortOrder = {"asc", "des"};
        int totalRecords = this.countAllRecord();
        String[] pageSizeValue = pageSizeList.split(",");
        try {
            for (String order : sortOrder) {
                log.info("Verify sort icon will be shown when first click on Name column");
                WebElement nameColumn = this.findElementByXpath("//th[@role='columnheader']//span[text() ='" + columnName + "']");
                this.tryClick(nameColumn, 1);
                for (String pageSize : pageSizeValue) {
                    log.info("Get total pages corresponding to page size");
                    int totalPages = this.numberOfPageWithPageSize(pageSize, totalRecords);
                    log.info("Select page size as: " + pageSize);
                    this.selectPageSize(pageSize);
                    this.waitForPageLoadComplete(10, 1);
                    if (order.equals("asc")) {
                        WebElement sortingArrowUp = this.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-up')]");
                        Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowUp.isDisplayed());
                    } else {
                        WebElement sortingArrowDown = this.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-down')]");
                        Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowDown.isDisplayed());
                    }
                    log.info("Verify sort function work properly");
                    listData = this.getListColumnData(columnName);
                    log.info("List data get on UI: " + listData);
                    listDataAfterSort = listData;
                    if (order.equals("asc")) {
                        Collections.sort(listDataAfterSort);
                    } else {
                        listDataAfterSort.sort(Comparator.reverseOrder());
                    }
                    log.info("List data after applying sort: " + listDataAfterSort);
                    Assert.assertEquals("Sort function do not work properly", listData, listDataAfterSort);
                    if (totalPages > 1) {
                        for (int i = 1; i < totalPages; i++) {
                            WebElement nextPageButton = this.findElementByXpath("//button[@id='nextPage']");
                            this.tryClick(nextPageButton, 1);
                            this.waitForPageLoadComplete(10, 1);
                            log.info("Verify sort icon will be shown when first click on Name column");
                            if (order.equals("asc")) {
                                WebElement sortingArrowUp = this.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-up')]");
                                Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowUp.isDisplayed());
                            } else {
                                WebElement sortingArrowDown = this.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-down')]");
                                Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowDown.isDisplayed());
                            }
                            log.info("Verify sort function work properly");
                            listData = this.getListColumnData(columnName);
                            log.info("List data get on UI: " + listData);
                            listDataAfterSort = listData;
                            if (order.equals("asc")) {
                                Collections.sort(listDataAfterSort);
                            } else {
                                listDataAfterSort.sort(Comparator.reverseOrder());
                            }
                            log.info("List data after applying sort: " + listDataAfterSort);
                            Assert.assertEquals("Sort function do not work properly", listData, listDataAfterSort);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.info("Sort is not work correctly, Test failed");
            return false;
        }
    }

    /**
     * Method to get data of a column base on page size
     * data contains: columnName
     *
     * @return list of column
     */
    public List<String> getListColumnData(String columnName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Column name: " + columnName);
        if (this.numberRecordOnCurrentPage() == 0) {
            return new ArrayList<>();
        }
        int nameColIndex = getColumnIndex(columnName) + 1;
        log.info("nameColIndex: " + nameColIndex);
        return this.findElementsByXpath("//tbody/tr[@role='row']/td[@role='cell']" + "[" + nameColIndex + "]").stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Method to verify next and previous button
     * data contains: countNumberPage
     */
    public void verifyNextPreviousButton(int countNumberPage) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (countNumberPage == 0) {
            Assert.assertFalse("No records but Next button is still display", this.isDisplayed("nextPage"));
            Assert.assertFalse("No records but Previous button is still display", this.isDisplayed("previousPage"));
        } else if (countNumberPage == 1) {
            Assert.assertFalse("Only one page records but Next button is still enable", this.tryClick(findElementByDataID("nextPage"), 3));
            Assert.assertFalse("Only one page records but Previous button is still enable", this.tryClick(findElementByDataID("previousPage"), 3));
        } else {
            for (int i = 1; i <= countNumberPage; i++) {
                log.info("Go to page " + i);
                Assert.assertEquals("Page " + i + " is not selected", Integer.parseInt(this.getPageSelected()), i);
                if (i == 1) {
                    Assert.assertTrue("Next button is disabled on first page", this.isEnable("nextPage"));
                    Assert.assertFalse("Previous button still enable on first page", this.tryClick(findElementByDataID("previousPage"), 3));
                } else if (i > 1 && i < countNumberPage) {
                    Assert.assertTrue("Next button is disabled", this.isEnable("nextPage"));
                    Assert.assertTrue("Previous button is disabled", this.isEnable("previousPage"));
                } else {
                    Assert.assertFalse("Next button still enable on latest page", this.tryClick(findElementByDataID("nextPage"), 3));
                    Assert.assertTrue("Previous button is disabled on latest page", this.isEnable("previousPage"));
                    break;
                }
                this.clickButton("nextPage");
                this.waitForPageLoadComplete(30, 1);
            }
        }
    }

    /**
     * Method to get value of selected page
     *
     * @return pageSelect
     */
    public String getPageSelected() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement pageSelected = this.findElementByXpath("//button[@class='neo-btn neo-btn-square-secondary neo-btn-square-secondary--info neo-btn-square']");
        Assert.assertTrue("No any number page selected", pageSelected.isDisplayed());
        String pageSelect = pageSelected.getText();
        log.info("Number page is selecting: " + pageSelect);
        return pageSelect;
    }

    public String enterAndVerifyField(String field, CharSequence value, Predicate<String> isTrue) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Enter the character to " + field + " field: " + value);
        getFieldByArialLabelText(field).clear();
        getFieldByArialLabelText(field).sendKeys(value);
        sleepInSec(0.2);
        String text = getFieldByArialLabelText(field).getText();
        log.info("Text: " + text);
        Assert.assertTrue(isTrue.test(text));
        return text;
    }


    public WebElement getFieldByArialLabelText(String field) {
        String fieldBase1 = locator.locatorFormat("inputFieldBase", field, "div//input");
        String fieldBase2 = locator.locatorFormat("inputFieldBase", field, "textarea");
        By by = new ByAll(By.xpath(fieldBase1), By.xpath(fieldBase2));
        return new WebDriverWait(this.driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void enterToTheField(String field, String value) {
        enterToTheField(field, true, value);
    }

    public void enterToTheField(String field, boolean isClear, String value) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("field: " + field + ", value: " + value);
        Objects.requireNonNull(value);
        String fieldBase1 = locator.locatorFormat("inputFieldBase", field, "div//input");
        String fieldBase2 = locator.locatorFormat("inputFieldBase", field, "textarea");
        WebElement inputBox = By.xpath(fieldBase1 + "|" + fieldBase2).findElement(driver);
        if (isClear) {
            inputBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            sleepInSec(0.2);
        }
        inputBox.sendKeys(value);
        WebElement dropdown = presentElement(By.xpath(fieldBase1), 0.5);
        if (dropdown != null
                && dropdown.getAttribute("aria-label") != null
                && dropdown.getAttribute("aria-label").equalsIgnoreCase("search-input")) {
            tryClick(By.xpath(locator.locatorFormat("inputFieldDropDown", field, value)), 5);
        }
    }

    /**
     * Method to get list attribute column on header table
     *
     * @return listColumn
     */
    public List<String> getAllColumnOnHeader() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            List<String> listColumn = this.findElementsByXpath("//th/div[@data-testid='header-cell']/span[1]").stream().map(WebElement::getText).collect(Collectors.toList());
            log.info("List attribute column : " + listColumn);
            return listColumn;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Method use get data of column all page
     *
     * @param columnName: name of column
     * @return List data of column all page
     */
    public List<String> getListColumnDataOnAllPage(String columnName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> listDataColumn = new ArrayList<>();
        try {
            if (this.presentElement("nextPage", 5) == null) {
                return this.getListColumnDataOnCurPage(columnName);
            }
            if (!this.findElementByDataID("nextPage").isEnabled()) {
                return this.getListColumnDataOnCurPage(columnName);
            }
            this.selectDropDownOptionEqual("pageSizeDropdown", "100");
            this.waitForPageLoadComplete(30, 2);
            if (columnName.equalsIgnoreCase("last updated")) {
                this.waitForPageLoadComplete(60, 3);
            }
            listDataColumn.addAll(this.getListColumnDataOnCurPage(columnName));
            while (findElementByDataID("nextPage").isEnabled()) {
                clickAnElementUsingJavaScript("nextPage", 60);
                waitForPageLoadComplete(30, 2);
                listDataColumn.addAll(this.getListColumnDataOnCurPage(columnName));
            }
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
        }
        return listDataColumn;
    }

    /**
     * Method verify basic Search work correctly
     *
     * @param searchText: input text to text box search
     * @param nameColumn: name of column
     * @return True if basic search work correctly
     */
    public boolean verifyBasicSearchWorkCorrect(String searchText, String nameColumn) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Search Text input : " + searchText);
        log.info("Name Column get data : " + nameColumn);
        try {
            log.info("Get list of page campaign name");
            List<String> dataColumnNameDefault = this.getListColumnDataOnAllPage(nameColumn);
            int numberRecordMatchExpected = 0;
            for (String name : dataColumnNameDefault) {
                if (name.toLowerCase().contains(searchText.toLowerCase())) {
                    numberRecordMatchExpected++;
                }
            }
            this.sendKeysToTextBox("searchTable", searchText);
            waitForPageLoadComplete(30, 2);
            List<String> dataResultSearch = this.getListColumnDataOnAllPage(nameColumn);
            int numberRecordMatchActual = 0;
            for (String name : dataResultSearch) {
                if (!name.toLowerCase().contains(searchText.toLowerCase())) {
                    return false;
                } else {
                    numberRecordMatchActual++;
                }
            }
            Assert.assertTrue("Value of result record not match, Test is failed", numberRecordMatchActual >= numberRecordMatchExpected);
            log.info("Verify clear search basic work correctly");
            this.presentElement("clearSearch", 20).click();
            waitForPageLoadComplete(30, 1);
            Assert.assertTrue("Search box not empty", getAttributeField("searchTable", "value").isEmpty());
            int numberRecordActual = this.countAllRecord();
            Assert.assertTrue("Clear search work incorrectly", numberRecordActual >= dataColumnNameDefault.size());
        } catch (Exception e) {
            log.info("-----------------------------End Method-------------------------------------");
            return false;
        }
        return true;
    }


    /**
     * Method to get list data off all columns/column on a page
     * If column = All, pageNumber = pageSize = "": Get all data of all column on pages match to conditional searching
     * column = ""All"", pageNumber and pageSize not empty: Get all data of all column on specific page that match to conditional searching
     *
     * @param column
     * @param pageNumber
     * @param pageSize
     * @return fullResults
     **/
    public Hashtable[] getListColumnDataOnPage(String column, String pageNumber, String pageSize, String sortColumn, String sortOrder) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int totalPages;
        int selectedPage;
        String pageName = getPageName();
        Hashtable[] fullResults = null;
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        List<WebElement> rows = this.findElementsByXpath("//table[@data-testid='table']/tbody/tr[*]");
        try {
            if (rows.size() != 0) {
                driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
                int totalRecords;
                this.selectPageSize("100");
                this.waitForPageLoadComplete(20, 2);
                List<WebElement> pageElements = this.findElementsByXpath("//*[@aria-label='pagination']//li");
                WebElement lastPageEle = this.findElementByXpath(("//*[@aria-label='pagination']//li" + "[" + pageElements.size() + "]//button"));
                int actPageValue = Integer.parseInt(lastPageEle.getText());
                if (actPageValue > 1) {
                    this.navigateToPageByGoToPage(actPageValue);
                    this.waitForPageLoadComplete(20, 2);
                    List<WebElement> rowsOfLastPage = this.findElementsByXpath("//*[@role='table']/tbody/tr");
                    totalRecords = (actPageValue - 1) * 100 + rowsOfLastPage.size();
                    this.navigateToPageByGoToPage(1);
                    this.waitForPageLoadComplete(20, 2);
                } else {
                    List<WebElement> rowsOfLastPage = this.findElementsByXpath("//*[@role='table']/tbody/tr");
                    totalRecords = rowsOfLastPage.size();
                }
                int j = 0;
                int listIndex;
                int columnIndex;
                int count = 0;
                log.info("Get all column name");
                ArrayList<String> listColumns = new ArrayList<>();
                List<WebElement> headerElements = this.findElementsByXpath("//*[@role='columnheader']");
                for (WebElement headerEle : headerElements) {
                    if (j != 0) {
                        listColumns.add(headerEle.getText());
                    }
                    j++;
                }
                if (!sortColumn.isEmpty() && !sortOrder.isEmpty()) {
                    int idx = 0;
                    boolean existCol = false;
                    while (idx <= listColumns.size()) {
                        for (String col : listColumns) {
                            if (col.equalsIgnoreCase(sortColumn)) {
                                if (sortOrder.equalsIgnoreCase("Asc")) {
                                    log.info("Selecting the sort order as ascending...on column name: " + sortColumn);
                                    WebElement nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + sortColumn + "']"));
                                    this.tryClick(nameColumn, 1);
                                    this.waitForPageLoadComplete(10, 1);
                                    WebElement sortingArrowUp = this.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-up')]");
                                    Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowUp.isDisplayed());
                                } else {
                                    log.info("Selecting the sort order as descending...on column name: " + sortColumn);
                                    WebElement nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + sortColumn + "']"));
                                    this.tryClick(nameColumn, 1);
                                    this.tryClick(nameColumn, 1);
                                    this.waitForPageLoadComplete(10, 1);
                                    WebElement sortingArrowDown = this.findElementByXpath("//span[starts-with(@class,'neo-icon-arrow-down')]");
                                    Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowDown.isDisplayed());
                                }
                                existCol = true;
                                break;
                            }
                        }
                        if (existCol) {
                            break;
                        }
                        idx++;
                    }
                    if (!existCol) {
                        log.info("ERROR: This column name does not exist!!!");
                        return null;
                    }
                } else {
                    log.info("Selecting sort order as default");
                }
                if (column.equals("All")) {
                    log.info("Starting get data of all columns...");
                    if (pageSize.isEmpty() && pageNumber.isEmpty()) {
                        pageNumber = "1";
                        totalPages = this.numberOfPageWithPageSize("100", totalRecords);
                        fullResults = new Hashtable[totalRecords];
                        this.selectPageSize("100");
                    } else {
                        totalPages = this.numberOfPageWithPageSize(pageSize, totalRecords);
                        this.selectPageSize(pageSize);
                    }
                    this.waitForPageLoadComplete(20, 2);
                    selectedPage = Integer.parseInt(pageNumber);
                    if (selectedPage > totalPages) {
                        log.info("This page do not exist on system with page size as " + pageSize);
                        return null;
                    }
                    while (selectedPage <= totalPages) {
                        if (totalPages != 1) {
                            this.navigateToPageByGoToPage(selectedPage);
                            this.waitForPageLoadComplete(20, 2);
                        }
                        WebElement table = this.findElementByXpath("//*[@role='table']");
                        List<WebElement> totalRowsElements = table.findElements(By.xpath("//*[@role='table']/tbody/tr"));
                        log.info("Number of rows in the table: " + totalRowsElements.size());
                        if (!pageSize.isEmpty() && !pageNumber.isEmpty()) {
                            fullResults = new Hashtable[totalRowsElements.size()];
                        }
                        for (WebElement rowElement : totalRowsElements) {
                            Hashtable<String, String> result = new Hashtable<>();
                            List<WebElement> totalColumnEles = rowElement.findElements(By.xpath("./td"));
                            columnIndex = 0;
                            listIndex = 0;
                            for (WebElement columnEle : totalColumnEles) {
                                if (columnIndex == 0) {
                                    // skip empty column
                                } else {
                                    // This is the old coding, Need keeping it for any change in the future
                                    /*if (pageName.equalsIgnoreCase("strategy-list-page")) {
                                        if (!(listColumns.get(listIndex).equals("Type"))) {
                                            result.put(listColumns.get(listIndex), columnEle.getText());
                                        } else {
                                            result.put(listColumns.get(listIndex), columnEle.findElement(By.xpath(".//button")).getAttribute("aria-label"));
                                        }
                                    }*/
                                    if (pageName.equalsIgnoreCase("strategy-list-page") || pageName.equalsIgnoreCase("campaign-list-page") || pageName.equalsIgnoreCase("contact-list-page") || pageName.equalsIgnoreCase("completion-code-list-page") || pageName.equalsIgnoreCase("data-source-page")) {
                                        result.put(listColumns.get(listIndex), columnEle.getText());
                                    }
                                    listIndex = listIndex + 1;
                                }
                                columnIndex = columnIndex + 1;
                            }
                            fullResults[count] = result;
                            count++;
                        }
                        if (!pageSize.isEmpty() && !pageNumber.isEmpty()) {
                            return fullResults;
                        }
                        if (selectedPage == totalPages) {
                            break;
                        }
                        selectedPage++;
                    }
                }
            } else {
                log.info("No record exist on this system!!!");
                return null;
            }
            return fullResults;
        } catch (Exception e) {
            log.info("FAILURE! Can not get detail data of record");
            return fullResults;
        }
    }


    /**
     * Method to verify items listed in dropdown
     *
     * @param sdropdown
     * @param sItems
     * @return pageSelect
     */
    public boolean verifyAllItemTextInSelect(String sdropdown, String sItems) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> myItems = getAllSelectDropDownOptions(sdropdown);
        log.info(sdropdown + " has values <" + myItems.toString() + "> checking for <" + sItems + ">");
        String sResults = "";
        for (String myItem : myItems) {
            if (sResults.isEmpty() || sResults.isBlank()) {
                sResults = sResults + myItem;
            } else {
                sResults = sResults + "|" + myItem;
            }
        }
        return sItems.toString().replace("\"", "").equals(sResults.toString());
    }

    /**
     * Method to verify items listed in dropdown
     *
     * @param sFields
     * @return
     */
    public void verifyFieldsNotDisplayedPage(String[] sFields) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (String sField : sFields) {
            Assert.assertFalse(sField + " is not displayed", this.isDisplayed(sField));
        }
    }

    /**
     * Method to Apply Advance search criteria
     *
     * @param columnName
     * @param operation
     * @param searchText
     * @return
     */
    public void applyAdvanceSearch(String columnName, String operation, String searchText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Applied advance search: " + columnName + " - " + operation + " - " + searchText);
        waitForPageLoadComplete(60, 1);
        WebElement btnAdvSearch = getStableElement("//*[@data-testid='columnFilters']");
        if (!btnAdvSearch.getAttribute("class").contains("neo-btn-square neo-icon-filter-filled")) {
            this.clickButton("columnFilters");
            utilityFun.wait(1);
        }
        this.selectDropDownOption(this.findElementByDataID("columnName"), columnName);
        this.selectDropDownOption(this.findElementByDataID("operator"), operation);
        String searchType = this.findElementByXpath("//label[text()='Search'] //..//*[@data-testid]").getAttribute("data-testid");
        if (searchType.equalsIgnoreCase("searchValue")) {
            this.sendKeysToTextBox("searchValue", searchText);
        } else {
            this.selectDropDownOptionEqual("dropdown", searchText);
        }
        this.waitForPageLoadComplete(30, 3);
    }

    /**
     * Method to Enable/Disable Advance Search
     *
     * @param sEnableDisable
     * @return
     */
    public void advanceSearchenableDisable(String sEnableDisable) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement btnFilterStrategy = this.findElementByDataID("columnFilters");
        if (sEnableDisable.equalsIgnoreCase("enable")) {
            if (!btnFilterStrategy.getAttribute("class").contains("neo-btn-square neo-icon-filter-filled")) {
                this.clickButton("columnFilters");
                utilityFun.wait(1);
            }
        } else {
            if (btnFilterStrategy.getAttribute("class").contains("neo-btn-square neo-icon-filter-filled")) {
                this.clickButton("columnFilters");
                utilityFun.wait(1);
            }
        }
    }

    /**
     * Method to verify items selected in dropdown
     *
     * @param sdropdown
     * @param sItems
     * @return pageSelect
     */
    public boolean verifySelectedItemTextInSelect(String sdropdown, String sItems) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Select select = new Select(this.findElementByDataID(sdropdown));
        WebElement option = select.getFirstSelectedOption();
        String defaultItem = option.getText();
        log.info(sdropdown + " has value <" + defaultItem + "> selected");
        return sItems.toString().equals(defaultItem.toString());
    }

    /**
     * Method to clear data of multi select search input field
     *
     * @param name name of search field
     * @return
     */
    public void clearSearchInput(String name) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String xpath = "//div[contains(@class, 'neo-multiselect') and (@name='%s')]//input[@id='search-input']".formatted(name);
        WebElement element = findElementByXpath(xpath);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        waitForPageLoadComplete(60, 1);
    }

    /**
     * Method to verify column header name on page
     * data format: "headerCells": "Name|Type|Contact List|Campaign Strategy"
     *
     * @return boolean
     */
    public boolean verifyColumnHeader(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String xpath = "//div[@data-testid='header-cell']";
        String[] dataHeaderCells = data.get("headerCells").split("\\|");
        List<WebElement> headerCells = driver.findElements(By.xpath(xpath));
        String[] actHeaderCells = new String[headerCells.size()];
        int i = 0;
        for (WebElement headerCell : headerCells) {
            String headerCellTxt = headerCell.getText();
            actHeaderCells[i] = headerCellTxt;
            i++;
        }
        log.info("Verify header column name" + data.get("headerCells"));
        if (dataHeaderCells.length != actHeaderCells.length) {
            log.info("the number of column names is not as expected");
            log.info("Expected: " + dataHeaderCells.length + "  || Actual: " + actHeaderCells.length);
            return false;
        } else {
            for (i = 0; i < dataHeaderCells.length; i++) {
                for (int j = 0; j < actHeaderCells.length; j++) {
                    if (dataHeaderCells[i].equals(actHeaderCells[j])) {
                        break;
                    }
                    if ((j == actHeaderCells.length) && !dataHeaderCells[i].equals(actHeaderCells[j])) {
                        log.info("The column name expected:" + dataHeaderCells[i] + "is not show on UI");
                        return false;
                    }
                }
            }
        }
        return true;

    }

    public String windowExec(String... commands) {
        try {
            Process p = new ProcessBuilder(commands).start();
            return IOUtils.toString(p.getInputStream(), Charset.defaultCharset());
        } catch (Exception e) {
            log.info(e);
            return null;
        }
    }

    public void clickRefreshButton() {
        try {
            By by = new ByAll(By.id("refreshTable"),
                    By.xpath("//*[@data-testid='refreshTable']"),
                    By.xpath("//*[@aria-label='Refresh']"));
            new WebDriverWait(driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                    .until(ExpectedConditions.elementToBeClickable(by)).click();
            waitForPageLoadComplete(30, 1);
        } catch (Exception e) {
            log.info("Cannot click refresh button");
        }
    }

    /**
     * Method drop-down result is disappeared when the user selected strategy/contact list
     *
     * @param dropdownName
     * @return return true/false
     */
    public boolean verifyDropdownDisappearedWhenValueSeletced(String dropdownName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String dropdownShow = this.findElementByXpath("//div[@name='" + dropdownName + "']").getAttribute("aria-expanded");
        if (dropdownShow.equals("true")) {
            log.info("Drop-down result should be disappeared when the user selected a" + dropdownName);
            return false;
        } else {
            log.info("Drop-down result is disappeared when the user selected a" + dropdownName);
            return true;
        }
    }

    /**
     * Method Response time to display list should not exceed {int} seconds
     *
     * @param dropdownName,responseTime
     * @return return true/false
     */
    public boolean responseTimeToDisplayExceedSec(String dropdownName, int responseTime) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String xpath = "//div[@name='" + dropdownName + "']//ul[@role='listbox']/li";
        if (this.presentElement(By.xpath(xpath), responseTime) != null) {
            log.info("The dropdown menu not display Exceed " + responseTime + " sec(s)");
            return true;
        } else {
            log.info("The dropdown menu not display within " + responseTime + " sec(s)");
            return false;
        }
    }

    /**
     * Method to verify the number max of records in dropdown list
     *
     * @param dropdownName,numberMax
     * @return return true/false
     */
    public boolean dropdownListDisplayMaxValue(String dropdownName, int numberMax) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String xpath = "//div[@name='" + dropdownName + "']//ul[@role='listbox']/li";
        int numberValueDropdown = this.findElementsByXpath(xpath).size();
        if (numberValueDropdown > numberMax) {
            log.info("The list shows more than the limited number");
            return false;
        } else {
            log.info("listings show less than limited amount");
            return true;
        }
    }

    /**
     * Method to verify the dropdown is show
     *
     * @param dropdownName
     * @return return true/false
     */
    public boolean dropdownListDisplay(String dropdownName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String xpath = "//div[@name='" + dropdownName + "']";
        if (this.presentElement(By.xpath(xpath), 10).isDisplayed()) {
            log.info("Dropdown " + dropdownName + " shown on campaign create/edit page");
            return true;
        } else {
            log.info("Dropdown " + dropdownName + " not shown on campaign create/edit page");
            return false;
        }
    }

    /**
     * Method to open the dropdown list
     *
     * @param dropdownListName
     */
    public void openDropdownList(String dropdownListName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String xpath = "//div[contains(@class, 'neo-multiselect') and (@name='" + dropdownListName + "')]//input[@id='search-input']";
        this.findElementByXpath(xpath).click();
    }

    /**
     * Method to verify the dropdown input is empty
     *
     * @param field
     * @return return true/false
     */
    public boolean selectedFieldListIsEmpty(String field) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String xpath = "//div[@name='" + field + "']//input[@data-testid='" + field + "']";
        if (this.findElementByXpath(xpath).getAttribute("value").isEmpty()) {
            log.info("Selected" + field + " is empty");
            return true;
        } else {
            log.info("Seletcted" + field + "is not empty");
            return false;
        }
    }

    /**
     * Method to get cell for all table
     *
     * @return String
     */
    private String getCell(String cellLoc) {
        String cellData = null;
        WebElement cellE1 = this.presentElement(By.xpath(cellLoc + "//span[@class='cellEllipsis']"), 0.1);
        if (cellE1 != null && !cellE1.getText().isEmpty()) {
            // not found cellE1
            return cellE1.getText();
        }
        if (cellE1 != null && cellE1.getText().isEmpty()) {
            // found empty text cellE1
            cellData = "";
        }
        WebElement cellE2 = this.presentElement(By.xpath(cellLoc + "/label/input"), 0.1);
        if (cellE2 != null && cellE2.getAttribute("aria-checked") != null) {
            // found Attribute cellE2
            return cellE2.getAttribute("aria-checked");
        }
        WebElement cellE3 = this.presentElement(By.xpath(cellLoc + "/div"), 0.1);
        if (cellE3 != null && !cellE3.getText().isEmpty()) {
            // found Text cellE3
            return cellE3.getText();
        }
        return cellData;
    }

    /**
     * Method to get table data
     */
    public List<Map<String, String>> getTable() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<Map<String, String>> tables = new ArrayList<>();
        String headerLoc = "//div[@data-testid='header-cell']/span[1]";
        if (presentElement(By.xpath(headerLoc), 5) == null) {
            return tables;
        }
        List<String> headers = new WebDriverWait(driver, Duration.ofSeconds(EnvSetup.ImplicitWait), Duration.ofMillis(500))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .until(driver -> presentElement(By.xpath(headerLoc), 0.5) == null ?
                        null : driver.findElements(By.xpath(headerLoc)).stream().map(WebElement::getText).collect(Collectors.toList())
                );
        List<List<String>> rowData = new ArrayList<>();
        String rowbase = "//tbody/tr";
        if (headers.isEmpty() || getListColumnDataOnCurPage(headers.get(0)).isEmpty() || presentElement(By.xpath(rowbase), 5) == null) {
            return tables;
        }
        int rowNum = driver.findElements(By.xpath(rowbase)).size();
        for (int i = 1; i <= rowNum; i++) {
            List<String> curRow = new ArrayList<>();
            String cellbase = rowbase + "[" + i + "]/td[.//span[@class='cellEllipsis'] or ./label[@class] or ./div[contains(@class,'neo-chip')]]";
            int cellNum = driver.findElements(By.xpath(cellbase)).size();
            for (int j = 1; j <= cellNum; j++) {
                String currCellLoc = "(" + cellbase + ")[" + j + "]";
                curRow.add(getCell(currCellLoc));
            }
            rowData.add(curRow);
        }
        rowData.forEach(r -> {
            Map<String, String> row = new HashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                row.put(headers.get(i), r.get(i));
            }
            tables.add(row);
        });
        return tables;
    }

    /**
     * Method to verify sorting on Current Page
     */
    public void verifySortingCurrentPage(String columName, List<String> listOfColumnToVerify) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<Map<String, String>> def = this.getTable().stream().peek(m -> m.keySet().retainAll(listOfColumnToVerify)).toList();

        log.info("Verify that ascending sorting on '%s' column as default".formatted(columName));
        List<Map<String, String>> ascDef = def.stream().map(HashMap::new)
                .sorted(Comparator.comparing(m -> m.get(columName)))
                .collect(Collectors.toList());
        clickSort(columName, 1);
        List<Map<String, String>> ascTables = this.getTable().stream().peek(m -> m.keySet().retainAll(listOfColumnToVerify)).toList();
        log.info("Expected Ascending: " + ascDef);
        log.info("Actual Ascending: " + ascTables);
        Assert.assertEquals(ascDef, ascTables);

        log.info(String.format("Verify that descending sorting on '%s' column", columName));
        List<Map<String, String>> descDef = def.stream().map(HashMap::new)
                .sorted(Collections.reverseOrder(Comparator.comparing(m -> m.get(columName))))
                .collect(Collectors.toList());
        clickSort(columName, 1);
        List<Map<String, String>> descTables = this.getTable().stream().peek(m -> m.keySet().retainAll(listOfColumnToVerify)).toList();
        log.info("Expected Descending: " + descDef);
        log.info("Actual Descending: " + descTables);
        Assert.assertEquals(descTables, descDef);

        log.info(String.format("Verify sorting on %s column restore to default after 3rd clicks", columName));
        clickSort(columName, 1);
        List<Map<String, String>> defTable = this.getTable().stream().peek(m -> m.keySet().retainAll(listOfColumnToVerify)).toList();
        log.info("Expected Default: " + def);
        log.info("Actual Default: " + defTable);
        Assert.assertEquals(defTable, def);
    }


    /**
     * Method to verify sorting on Current Page
     */
    public void verifySortingCurrentPage(String columName) {
        verifySortingCurrentPage(columName, getAllHeaders());
    }

    public void verifySortStaticData_RandomlyDisplayOnTable(String columName) {
        verifySortingCurrentPage(columName, List.of(columName));
    }

    public void verifySortStaticData_RandomlyDisplayOnTable(String columName, String sortBy) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        refreshPage(30);
        List<String> def = this.getListColumnDataOnCurPage(columName);
        switch (sortBy.toLowerCase()) {
            case "ascending" -> {
                log.info(String.format("Verify that ascending sorting on %s column", columName));
                List<String> ascDef = def.stream().sorted().toList();
                clickSort(columName, 1);
                List<String> ascTables = this.getListColumnDataOnCurPage(columName);
                log.info("Expected Ascending: " + ascDef);
                log.info("Actual Ascending: " + ascTables);
                Assert.assertEquals(ascDef, ascTables);
            }
            case "descending" -> {
                log.info(String.format("Verify that descending sorting on %s column", columName));
                List<String> descDef = def.stream().sorted(Comparator.reverseOrder()).toList();
                clickSort(columName, 2);
                List<String> descTables = this.getListColumnDataOnCurPage(columName);
                log.info("Expected Descending: " + descDef);
                log.info("Actual Descending: " + descTables);
                Assert.assertEquals(descTables, descDef);
            }
            default -> {
                log.info(String.format("Verify sorting on %s column restore to default after 3rd clicks", columName));
                clickSort(columName, 3);
                List<String> defTable = this.getListColumnDataOnCurPage(columName);
                log.info("Expected Default: " + def);
                log.info("Actual Default: " + defTable);
                Assert.assertEquals(defTable, def);
            }
        }
    }

    /**
     * Method to verify sorting on Current Page
     */
    public void verifySortingCurrentPage(String columName, String sort) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verifing <"+columName+"> sort order <"+sort+">");
        this.waitForPageLoadComplete(10, 1);
        this.sleepInSec(2);
        List<Map<String, String>> def = this.getTable();
        log.info("default executed: " + def);
        switch (sort.toLowerCase()) {
            case "ascending" -> {
                log.info("Verify that ascending sorting on %s column".formatted(columName));
                List<Map<String, String>> ascDef = def.stream().map(HashMap::new)
                        .sorted(Comparator.comparing(m -> m.get(columName)))
                        .collect(Collectors.toList());
                clickSort(columName, 1);
                this.sleepInSec(1);
                List<Map<String, String>> ascTables = this.getTable();
                log.info("Expected Ascending: " + ascDef);
                log.info("Actual Ascending: " + ascTables);
                Assert.assertEquals(ascDef, ascTables);
            }
            case "descending" -> {
                log.info("Verify that descending sorting on %s column".formatted(columName));
                List<Map<String, String>> descDef = def.stream().map(HashMap::new)
                        .sorted(Collections.reverseOrder(Comparator.comparing(m -> m.get(columName))))
                        .collect(Collectors.toList());
                clickSort(columName, 2);
                this.sleepInSec(1);
                List<Map<String, String>> descTables = this.getTable();
                log.info("Expected Descending: " + descDef);
                log.info("Actual Descending: " + descTables);
                Assert.assertEquals(descTables, descDef);
            }
            default -> {
                log.info("Verify sorting on %s column restore to default after 3rd clicks".formatted(columName));
                clickSort(columName, 3);
                this.sleepInSec(1);
                List<Map<String, String>> defTable = this.getTable();
                log.info("Expected Default: " + def);
                log.info("Actual Default: " + defTable);
                Assert.assertEquals(defTable, def);
            }
        }
    }

    /**
     * Method to get row data on table
     */
    public Map getTableRowData(List<Map<String, String>> htData, String columName, String value) {
        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            int i = 0;
            for (Map<String, String> hTemp : htData) {
                if (hTemp.get(columName).equals(value)) {
                    break;
                }
                i++;
            }
            return htData.get(i);
        } catch (Exception e) {
            log.info("Failed by: ", e);
            return null;
        }
    }

    /**
     * method use click name Column Header on table
     *
     * @param nameColumn: nameColumn of table
     */
    public void clickHeaderColumn(String nameColumn) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.tryClick(By.xpath("//div[@data-testid='header-cell']//span[contains(text(),'%s')]".formatted(nameColumn)), 3);
    }

    /**
     * method use to verify locator is displayed By xpath
     *
     * @param xpath
     * @param waitSec
     * @return boolean
     */
    public boolean isDisplayedByXpath(String xpath, int waitSec) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("wait for Element with `xpath` is '" + xpath + "' from page: " + pageName);
        return presentElement(By.xpath(xpath), waitSec) != null;
    }


    /**
     * The method used to verify the sort order icons on a separate column
     *
     * @return boolean
     */
    public boolean verifySortOrderIconsOnColumn(String sortIcon, String columnName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement colNameEle = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + columnName + "']"));
        try {
            switch (sortIcon) {
                case "all" -> {
                    log.info("Click on column and verify the sorting order icon will be show as Ascending");
                    tryClick(colNameEle, 1);
                    waitForPageLoadComplete(10, 1);
                    Assert.assertTrue("ERROR: Sorting icon does not show as ASC", driver.findElement(locator.get("sortIconArrowUp")).isEnabled());
                    log.info("Re-click on column and verify the sorting order icon will be show as Descending");
                    tryClick(colNameEle, 1);
                    waitForPageLoadComplete(10, 1);
                    Assert.assertTrue("ERROR: Sorting icon does not show as DSC", driver.findElement(locator.get("sortIconArrowDown")).isEnabled());
                    log.info("Re-click on column and verify the sorting order icon will disappear");
                    tryClick(colNameEle, 1);
                    waitForPageLoadComplete(10, 1);
                    String locatorXpath = "//div[@data-testid='header-cell' and ./span[text()='" + columnName + "'] and ./span[starts-with(@class, 'neo-icon-arrow')]]";
                    Assert.assertNull("ERROR: Sorting icon does not show as DEFAULT", presentElement(By.xpath(locatorXpath), 5));
                }
                case "asc" -> {
                    log.info("Verify the sorting order icon will be show as Ascending");
                    tryClick(colNameEle, 1);
                    waitForPageLoadComplete(10, 1);
                    Assert.assertTrue("ERROR: Sorting icon does not show as ASC", driver.findElement(locator.get("sortIconArrowUp")).isEnabled());
                }
                case "dsc" -> {
                    tryClick(colNameEle, 1);
                    tryClick(colNameEle, 1);
                    waitForPageLoadComplete(10, 1);
                    Assert.assertTrue("ERROR: Sorting icon does not show as DSC", driver.findElement(locator.get("sortIconArrowDown")).isEnabled());
                }
                case "default" -> {
                    log.info("Verify default sort icon");
                    String locatorXpath = "//div[@data-testid='header-cell' and ./span[text()='" + columnName + "'] and ./span[starts-with(@class, 'neo-icon-arrow')]]";
                    Assert.assertNull("ERROR: Sorting icon does not show as DEFAULT", presentElement(By.xpath(locatorXpath), 5));
                }
            }
        } catch (Exception e) {
            log.info("FAILURE: Sorting icons do not show as expected");
            driver.manage().timeouts().implicitlyWait(EnvSetup.ImplicitWait, TimeUnit.SECONDS);
            return false;
        }
        return true;
    }


    /**
     * method use clean up data before run
     */
    public void cleanUpCampaignAndContactListAndStrategyBefore(TestData<String, String> testData) {
        RestMethodsObj.deleteAllCampaign(testData.getString("campaign", "name"));
        RestMethodsContactListObj.deleteAllContactList(testData.getString("contactlist", "name"));
        RestMethodsObj.deleteAllStrategies(testData.getString("strategy", "name"));
    }

    /**
     * method use input search with advanced search
     *
     * @param field     : attribute of column name search
     * @param operator  : operation search
     * @param inputType : input text or select calendar
     * @param contents: search text or select
     */
    public void advanceSearchFilter(String field, String operator, String inputType, String contents) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (field) {
            case "Name":
                this.applyAdvanceSearch(field, operator, contents);
                break;
            case "Last Updated", "Last Executed", "Start Time":
                Assert.assertTrue("Input advance search error with " + field,
                        this.applyFilterCalendar(field, operator, inputType, contents));
                break;
            default:
                log.info("Attribute input is not display on column");
                Assert.fail("Attribute input is not display on column");
                break;
        }
    }

    /**
     * @param contents: search tex - time handle on last update
     * @return list value local date follow time format "yyyy/MM/dd hh:mm:ss"
     */
    public List<LocalDateTime> extractContentToDateTime(String contents) {
        return extractContentToDateTime(LocalDateTime.now(), contents);
    }

    /**
     * @param baseTime search tex - time handle on last update
     * @param contents time want update duration
     * @return list localDate
     */

    public List<LocalDateTime> extractContentToDateTime(LocalDateTime baseTime, String contents) {
        Objects.requireNonNull(contents);
        List<LocalDateTime> results = new ArrayList<>();
        for (String content : contents.split(",")) {
            content = content.trim();
            String originText = new String(content);
            if (content.toLowerCase().startsWith("offset:")) {
                content = content.toLowerCase().replaceAll("offset:|\\s", "");
                int timeInMinutes = 0;
                int timeInSeconds = 0;
                try {
                    timeInMinutes = content.matches("^([+-]?\\d{1,8})m.*") ?
                            Integer.parseInt(content.replaceAll("^([+-]?\\d{1,8})m.*", "$1")) : 0;
                } catch (Exception e) {
                }
                try {
                    timeInSeconds = content.matches("^([+-]?\\d{1,8})s.*") ?
                            Integer.parseInt(content.replaceAll("^([+-]?\\d{1,8})s.*", "$1")) : 0;
                } catch (Exception e) {
                }
                LocalDateTime time = baseTime.plusMinutes(timeInMinutes).plusSeconds(timeInSeconds);
                String timeString = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                timeString = timeString.substring(0, timeString.indexOf(".")).replaceAll("T", " ");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                LocalDateTime updatedOnTime = LocalDateTime.parse(timeString, dateTimeFormatter);
                results.add(updatedOnTime);
            } else if (content.toLowerCase().startsWith("contactlist:")) {
                results.add(RestMethodsContactListObj.getContactListLastUpdate(originText.split("\\:")[1].trim()));
            } else if (content.toLowerCase().startsWith("campaign:")) {
                results.add(RestMethodsObj.getLastExecutedCampaign(originText.split("\\:")[1].trim()));
            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
                    LocalDateTime time = LocalDateTime.parse(content, formatter);
                    results.add(time);
                } catch (Exception e) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a");
                    LocalDateTime time = LocalDateTime.parse(content, formatter);
                    results.add(time);
                }
            }
        }
        return results;
    }

    /**
     * @param field      : attribute of column name search
     * @param operator   : operator search
     * @param inputTypes : input text or select calendar
     * @param contents:  search text or select
     * @return true if input successful and else
     */
    public boolean applyFilterCalendar(String field, String operator, String inputTypes, String contents) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("field: " + field);
        log.info("operator: " + operator);
        try {
            WebElement btnAdvSearch = this.findElementByDataID("columnFilters");
            if (!btnAdvSearch.getAttribute("class").contains("neo-btn-square neo-icon-filter-filled")) {
                this.clickButton("columnFilters");
                utilityFun.wait(1);
            }
            log.info("Select column name: " + field);
            this.selectDropDownOption(this.findElementByDataID("columnName"), field);
            log.info("Select operator: " + operator);
            this.selectDropDownOption(this.findElementByDataID("operator"), operator);
            List<WebElement> contentsElement = driver.findElements(By.xpath("//input[@data-testid='datepicker']"));
            List<LocalDateTime> contentsList = this.extractContentToDateTime(contents);
            List<String> inputTypeList = Arrays.stream(inputTypes.split(",")).map(String::trim).collect(Collectors.toList());
            String inputType = "";
            for (int i = 0; i < contentsElement.size(); i++) {
                inputType = i < inputTypeList.size() ? inputTypeList.get(i) : inputType;
                fillDateTimeField(inputType, contentsElement.get(i), contentsList.get(i));
            }
        } catch (Exception e) {
            log.info("Exception ", e);
            return false;
        }
        return true;
    }


    public void fillDateTimeField(String inputType, WebElement field, LocalDateTime text) {
        switch (inputType) {
            case "Input":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
                field.sendKeys(formatter.format(text));
                field.sendKeys(Keys.ENTER);
                break;
            case "Select":
                field.click();
                this.selectCalendar(text, "", "MMM yyyy dd", "h:mm a");
                break;
            default:
                Assert.fail("Input Type is not format correctly");
        }
    }

    /**
     * @Method to verify advance search result for contact list page
     */
    public void verifyAdvanceSearchResult() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String colNameSearch = new Select(this.findElementByXpath(locator.getLocator("columnNameFieldAdvanceSearch"))).getFirstSelectedOption().getText();
        switch (colNameSearch) {
            case "Name":
                this.verifyAdvanceSearchForNameCol();
                break;
            case "Last Updated", "Start Time":
                this.verifyAdvanceSearchForDateTimeCol();
                break;
            case "Status":
                this.verifyAdvanceSearchForStatusCol();
                break;
            default:
                log.info("Field inputted not exist for advance search");
                Assert.fail("Field inputted not exist for advance search");
                break;
        }
    }


    /**
     * @param : name of time column for advance search
     */
    public void verifyAdvanceSearchForDateTimeCol() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String colNameSearch = new Select(this.findElementByXpath(locator.getLocator("columnNameFieldAdvanceSearch"))).getFirstSelectedOption().getText();
        String operator = new Select(this.findElementByXpath(locator.getLocator("operatorFieldAdvanceSearch"))).getFirstSelectedOption().getText();
        String searchText = null;
        String searchText2 = null;
        LocalDateTime time = null;
        LocalDateTime time2 = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a");
        if (operator.equalsIgnoreCase("between")) {
            searchText = this.findElementByXpath(locator.getLocator("startTimeCalenderAdvanceSearch")).getAttribute("value");
            String[] arr = searchText.trim().split(" ");
            searchText = arr[0] + " " + arr[1] + ":00 " + arr[2];
            time = LocalDateTime.parse(searchText, formatter);
            searchText2 = this.findElementByXpath(locator.getLocator("endTimeCalenderAdvanceSearch")).getAttribute("value");
            String[] arr1 = searchText2.trim().split(" ");
            searchText2 = arr1[0] + " " + arr1[1] + ":00 " + arr1[2];
            time2 = LocalDateTime.parse(searchText2, formatter);
        } else {
            searchText = this.findElementByXpath(locator.getLocator("dateTextAdvanceSearch")).getAttribute("value");
            String[] arr = searchText.trim().split(" ");
            searchText = arr[0] + " " + arr[1] + ":00 " + arr[2];
            time = LocalDateTime.parse(searchText, formatter);
        }
        waitForPageLoadComplete(60, 2);
        List<String> listResult = this.getListColumnDataOnAllPage(colNameSearch);
        listResult.removeIf(e -> e.contentEquals("In Progress"));
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");
        List<LocalDateTime> listTimeResult = listResult.stream().map(s -> LocalDateTime.parse(s, formatter1)).collect(Collectors.toList());
        log.info("Search text : " + searchText + " - " + searchText2);
        log.info("Count records match with this advance search: " + listResult.size());
        log.info("List result: " + listResult);
        if (listResult.size() > 0) {
            switch (operator) {
                case "<": {
                    for (LocalDateTime result : listTimeResult) {
                        Assert.assertTrue("Result of search not match for " + operator, result.isBefore(time));
                    }
                    break;
                }
                case ">": {
                    for (LocalDateTime result : listTimeResult) {
                        Assert.assertTrue("Result of search not match for " + operator, result.isAfter(time) || result.isEqual(time));
                    }
                    break;
                }
                case "Between": {
                    for (LocalDateTime result : listTimeResult) {
                        Assert.assertTrue("Result of search not match for " + operator + " - " + result, (result.isAfter(time) || result.isEqual(time)) && result.isBefore(time2));
                    }
                    break;
                }
                default: {
                    log.info("Operator isn't exist for column name: " + colNameSearch);
                    Assert.fail("Operator isn't exist for column name: " + colNameSearch);
                    break;
                }
            }
        } else {
            log.info("No result match for search '" + colNameSearch + "' with '" + searchText + "' by operator '" + operator + "' ");
        }
    }


    /**
     * @method to verify advance search result for Name column
     */
    public void verifyAdvanceSearchForNameCol() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String colNameSearch = new Select(this.findElementByXpath(locator.getLocator("columnNameFieldAdvanceSearch"))).getFirstSelectedOption().getText();
        String operator = new Select(this.findElementByXpath(locator.getLocator("operatorFieldAdvanceSearch"))).getFirstSelectedOption().getText();
        String searchText = this.findElementByXpath(locator.getLocator("searchTextAdvanceSearch")).getAttribute("value");
        List<String> listResult = this.getListColumnDataOnAllPage(colNameSearch);
        log.info("Search text : " + searchText);
        log.info("Count records match with this advance search: " + listResult.size());
        log.info("List result: " + listResult);
        if (listResult.size() > 0) {
            switch (operator) {
                case "=": {
                    Assert.assertTrue("Result of advance search by operator '=' more than 1", listResult.size() == 1);
                    Assert.assertTrue("Result of advance search not match for: " + operator, listResult.get(0).equalsIgnoreCase(searchText));
                    break;
                }
                case "!=": {
                    for (String result : listResult) {
                        Assert.assertTrue("Advance search result not match for: " + operator, !result.equalsIgnoreCase(searchText));
                    }
                    break;
                }
                case "In": {
                    List<String> searchKey = Arrays.stream(searchText.toLowerCase().split(",")).map(String::trim).collect(Collectors.toList());
                    for (String result : listResult) {
                        Assert.assertTrue("Advance search result not match for: " + operator, searchKey.contains(result.toLowerCase()));
                    }
                    break;
                }
                case "Like": {
                    for (String result : listResult) {
                        Assert.assertTrue("Advance search result not match for: " + operator, result.toLowerCase().contains(searchText.toLowerCase()));
                    }
                    break;
                }
                case "Not Like": {
                    for (String result : listResult) {
                        Assert.assertTrue("Advance search result not match for: " + operator, !result.toLowerCase().contains(searchText.toLowerCase()));
                    }
                    break;
                }
                default: {
                    log.info("Operator isn't exist for column name: " + colNameSearch);
                    Assert.fail("Operator isn't exist for column name: " + colNameSearch);
                    break;
                }
            }
        } else {
            log.info("No result match for search '" + colNameSearch + "' with '" + searchText + "' by operator '" + operator + "'");
        }
    }

    /**
     * @method this method to selecting a calendar
     */
    public void selectCalendar(LocalDateTime timeDateSelect, String time, String dateFormat, String timeformat) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (timeDateSelect != null) {
            //Navigate to Month
            String uiDateStr = this.findElementByXpath("//div[contains(@class,'current-month')]").getText().trim();
            log.info(uiDateStr);
            uiDateStr = uiDateStr.split(" ")[0].substring(0, 3) + " " + uiDateStr.split(" ")[1] + " 01";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate dateUI = LocalDate.parse(uiDateStr, dateFormatter);
            Period diffrent = Period.between(dateUI, LocalDate.from(timeDateSelect));
            int diffrentMonths = diffrent.getMonths();
            log.info("diffrentMonths : " + diffrentMonths);
            String btnXpath = "";
            if (diffrentMonths < 0) {
                btnXpath = "//button[@aria-label='Previous Month']";
            }
            if (diffrentMonths > 0) {
                btnXpath = "//button[@aria-label='Next Month']";
            }
            for (int i = 0; i < Math.abs(diffrentMonths); i++) {
                this.findElementByXpath(btnXpath).click();
                sleepInSec(0.3);
            }

            //select date
            WebElement dateSelectEle = this.findElementByXpath(locator.locatorFormat("filterSelectingDate", String.valueOf(timeDateSelect.getDayOfMonth())));
            tryClick(dateSelectEle, 3);
        }
        //select time
        if (timeformat != null) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timeformat);
            String timeSlect = time;
            if (timeDateSelect != null) {
                timeSlect = timeDateSelect.format(timeFormatter);
            }
            log.info("Selecting :" + timeSlect);
            log.info(timeDateSelect);
            WebElement timeSelectEle = this.findElementByXpath("//li[text()='%s']".formatted(timeSlect));
            tryClick(timeSelectEle, 3);
        }
    }

    public void verifyAllOptionSearchAreDisplayed() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Basic search is not display", isDisplayed("searchTable", 60));
        Assert.assertTrue("Advance search is not display", isDisplayed("columnFilters", 10));
    }

    public void verifyFilterComponentsDisplay() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Name Field is not display", isDisplayed("columnName", 60));
        Assert.assertTrue("Operator Field is not display", isDisplayed("operator", 60));
        Assert.assertTrue("Search Field is not display", isDisplayed("searchValue", 60));
    }

    public void clickFilterButton() {
        clickAnElementUsingJavaScript("columnFilters", 60);
    }

    /*
     * Method to wait until loader is visible
     */
    public void waitForLoader() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        long startTime = System.currentTimeMillis();
        //log.info(locator.get("coloumnloader"));
        WebElement element = presentElement(locator.get("coloumnloader"), 2);
        long elapsedTime = 0;
        while (element != null) {
            element = presentElement(locator.get("coloumnloader"), 2);
            elapsedTime = (new Date()).getTime() - startTime;
            if (elapsedTime > 70000) {
                log.info("Time out..waiting for loader to disappear");
                break;
            }
            utilityFun.wait(1);
        }
        log.info("Loader disappear in " + elapsedTime);
    }

    /**
     * Method to return selected option in dropdown
     *
     * @param selectBoxID
     * @return Selected string
     */
    public String getSelectedOptionInDropDown(String selectBoxID) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Getting selected option in dropdown box " + selectBoxID + " on page " + pageName);
        Select selectBox = new Select(driver.findElement(By.xpath("//select[@data-testid=\"%s\"]".formatted(selectBoxID))));
        WebElement option = selectBox.getFirstSelectedOption();
        log.info("Selected value: " + option.getText());
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        return option.getText();
    }


    /**
     * method verify value back to default of advance search
     */
    public void verifyAdvancedSearchFieldBackToDefaultAfterClickRefreshButton() {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Column Name field is not back to default when click refresh button", getTextOptionIsSelected("columnName").equalsIgnoreCase("Name"));
        Assert.assertTrue("Operator field is not back to default when click refresh button", getTextOptionIsSelected("operator").equalsIgnoreCase("="));
        Assert.assertTrue("Search Text is not back to default when click refresh button", isDisplayed("searchValue", 5));
    }


    /**
     * method to get list data of Last updated column for operator and search inputted
     */
    public List<String> getExpectedListLastUpdatedByOperator(String operator, String searchText) {
        List<LocalDateTime> listLastUpdated = RestMethodsContactListObj.getListDataLastUpdatedByAPI();
        log.info("listLastUpdated : " + listLastUpdated);
        List<LocalDateTime> advanceSearchvalue = this.extractContentToDateTime(searchText);
        log.info("advanceSearchvalue : " + advanceSearchvalue);
        List<LocalDateTime> expectData = new ArrayList<>();
        switch (operator) {
            case "<": {
                expectData = listLastUpdated.stream().filter(time -> time.isBefore(advanceSearchvalue.get(0))).collect(Collectors.toList());
                break;
            }
            case ">": {
                expectData = listLastUpdated.stream().filter(time -> time.isAfter(advanceSearchvalue.get(0)) || time.isEqual(advanceSearchvalue.get(0))).collect(Collectors.toList());
                break;
            }
            case "Between": {
                expectData = listLastUpdated.stream().filter(time -> (time.isAfter(advanceSearchvalue.get(0)) || time.isEqual(advanceSearchvalue.get(0))) && time.isBefore(advanceSearchvalue.get(1))).collect(Collectors.toList());
                break;
            }
            default: {
                log.info("Operator isn't exist for this column name");
                Assert.fail("Operator isn't exist for this column name: ");
                break;
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a");
        List<String> expectDataStr = expectData.stream().map(time -> time.format(formatter)).collect(Collectors.toList());
        return expectDataStr;
    }


    public List<String> getExpectedListNameByOperator(String operator, String searchText) {
        List<String> listNameOrigin = getListColumnDataOnAllPage("Name");
        log.info("List Name origin on UI: " + listNameOrigin);
        List<String> expectData = new ArrayList<>();
        switch (operator) {
            case "=": {
                expectData = listNameOrigin.stream().filter(result -> result.equalsIgnoreCase(searchText)).collect(Collectors.toList());
                Assert.assertTrue("Advance search for name should be equal 0 or 1", expectData.size() <= 1);
                break;
            }
            case "!=": {
                expectData = listNameOrigin.stream().filter(result -> !result.equalsIgnoreCase(searchText)).collect(Collectors.toList());
                break;
            }
            case "Like": {
                expectData = listNameOrigin.stream().filter(result -> result.toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
                break;
            }
            case "Not Like": {
                expectData = listNameOrigin.stream().filter(result -> !result.toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
                break;
            }
            case "In": {
                List<String> searchKey = Arrays.stream(searchText.toLowerCase().split("\\,")).map(String::trim).collect(Collectors.toList());
                expectData = listNameOrigin.stream().filter(result -> searchKey.contains(result.toLowerCase())).collect(Collectors.toList());
                break;
            }
            default: {
                log.info("Operator isn't exist for this column name");
                Assert.fail("Operator isn't exist for this column name: ");
                break;
            }
        }
        return expectData;
    }


    /**
     * Method to Verify error hint shown on field
     *
     * @param fieldId
     * @return error message hint
     */
    public String getErrorHintField(String fieldId) {
        WebElement elements = this.presentElement(By.xpath("//*[@data-testid='%s']/../../..//*[@class='neo-input-hint']".formatted(fieldId)), 5);
        if (elements != null) {
            return elements.getText();
        } else {
            return null;
        }
    }

    /**
     * Method use to check the name of Strategy/campaign/contactlist should be at first row after edited/created successful
     *
     * @param name - name of strategy/contactlist/campaign
     * @return boolean
     * true if name in first row of list
     * false if name is not in first row of list
     */
    public boolean newlyCreatedOrEditedInFirstRow(String name) {
        String nameInFirstRowAct = this.getListColumnDataOnCurPage("Name").get(0);
        log.info("name in first row Actual: " + nameInFirstRowAct);
        log.info("name expected: " + name);
        if (nameInFirstRowAct.equals(name)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to verify cancel form when user click cancel button
     * condition is user change any values in page
     *
     * @param action
     */
    public void verifyCancelForm(Map<String, String> testData, String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        presentElement(locator.getLocator("dialogCancel"), 5);
        log.info("Dialog is displayed");
        Assert.assertEquals("Header Popup text not match", "Leave this page?", findElementByXpath(locator.getLocator("cancelHeader")).getText());
        log.info("Header verified");
        Assert.assertEquals("Message Popup text not match", "The changes will be lost if you navigate away from this page.", findElementByXpath(locator.getLocator("cancelMessage")).getText());
        log.info("Notification verified");
        Assert.assertEquals("Stay page option text not match", "Stay on this page", getTextField("stay-page"));
        log.info("Button stay on page verified");
        Assert.assertEquals("Leave page option text not match", "Leave this page", getTextField("leave-page"));
        log.info("Button leave this page verified");
        switch (action.toLowerCase()) {
            case "stay on this page":
                clickButton("stay-page");
                break;
            case "leave this page":
                clickButton("leave-page");
                break;
        }
        WebElement pageName = findElementByXpath(locator.getLocator("pageLoaded"));
        Assert.assertNotNull(presentElement(pageName, 5));
        Assert.assertEquals(testData.get("pageName"), pageName.getText());
        log.info("Page " + pageName.getText() + " is loaded");
    }

    /**
     * Method to verify page loaded
     * condition is user do not change any values in  current page
     */
    public void verifyPageLoaded(String pageNameExp) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement pageName = findElementByXpath(locator.getLocator("pageLoaded"));
        Assert.assertNotNull(presentElement(pageName, 5));
        Assert.assertEquals(pageNameExp, pageName.getText());
        log.info("Page " + pageName.getText() + " is loaded");
    }

    public boolean isDisplayedAriaLabel(String ariaLabel, int waitSec) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            String pageName = getPageName();
            log.info("wait for button '" + ariaLabel + "' from page: " + pageName);
            return presentElement(By.xpath("//*[@aria-label='%s']".formatted(ariaLabel)), waitSec).isDisplayed();//!= null;
        } catch (Exception e) {
            return false;
        }
    }

    public void verifyOperatorDisplayCorrectWithAttribute(String attribute, String operators) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement btnAdvSearch = this.findElementByDataID("columnFilters");
        if (!btnAdvSearch.getAttribute("class").contains("neo-btn-square neo-icon-filter-filled")) {
            this.clickButton("columnFilters");
            utilityFun.wait(1);
        }
        utilityFun.wait(1);
        this.selectDropDownOptionEqual("columnName", attribute);
        String[] arrOperator = operators.split(",");
        ArrayList<String> expLst = new ArrayList<String>(Arrays.asList(arrOperator));
        Collections.sort(expLst);
        List<String> actLst = this.getAllSelectDropDownOptions("operator");
        Collections.sort(actLst);
        log.info("Expected list: " + expLst);
        log.info("Actual list: " + actLst);
        Assert.assertTrue("List operator on dropdown option displayed incorrectly ", expLst.equals(actLst));
    }

    /**
     * Method use to verify a link start with https protocol
     *
     * @param listUrl This is list of URLs to check
     * @return boolean: true if url start with https
     * false if url none start with https
     */
    public boolean isHttpsUrl(ArrayList<String> listUrl) {
        String urlPattern = "^(https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        boolean httpsYes = false;
        for (String url : listUrl) {
            Matcher matcher = pattern.matcher(url);
            httpsYes = matcher.matches();
            if (!httpsYes) {
                log.info("Failed URL: " + url);
                return false;
            }
        }
        return httpsYes;
    }

    /**
     * method using to get all entries api log in Network in current page
     *
     * @param
     * @return List entries apis
     */
    public List<LogEntry> getAPIEntries() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
        return entries;
    }

    /**
     * method using to get all urls in Network in current page
     *
     * @param
     * @return List urls
     */
    public ArrayList<String> getURLs() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<LogEntry> entries = this.getAPIEntries();
        String camapaignPort = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL.split(":")[2];
        String contactPort = EnvSetup.OUTBOUND_CONTACTS_API_BASEURL.split(":")[2];
        String urlPattern = "(?i)(https*)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*:(" + camapaignPort + "|" + contactPort +
                ")/(strategies|campaigns|completion-codes|contact-lists|data-sources)[-a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*";
        ArrayList<String> result = new ArrayList<String>();
        for (LogEntry entry : entries) {
            Pattern pattern = Pattern.compile(urlPattern);
            Matcher matcher = pattern.matcher(entry.getMessage());
            while (matcher.find()) {
                result.add(matcher.group());
            }
        }
        return result;
    }


    /**
     * Method use to verify status code
     *
     * @param listStatusCode This is the value of status code
     * @return boolean: true if value of status code as 200 or 201
     * false if value of status code not as 200 and 201
     */
    public boolean verifyStatusCode(ArrayList<String> listStatusCode) {
        boolean result = false;
        for (String statusCode : listStatusCode) {
            if (statusCode.equalsIgnoreCase("200") || statusCode.equalsIgnoreCase("201")) {
                result = true;
            } else {
                return false;
            }
        }
        return result;
    }

    /**
     * Description: Method to return total number of pages is displayed with total records and current selected page size
     *
     * @param pageSize
     * @param totalRecords
     * @return
     */
    public int numberPageWithPageSize(String pageSize, int totalRecords) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int numberOfPage = 1;
        int size = Integer.parseInt(pageSize.trim());
        if (totalRecords == 0) {
            numberOfPage = 0;
        } else if (totalRecords <= size) {
            numberOfPage = 1;
        } else {
            int temp = totalRecords % size;
            if (temp == 0) {
                numberOfPage = totalRecords / size;
            } else {
                numberOfPage = totalRecords / size + 1;
            }
        }
        log.info("Number of page is: " + numberOfPage + ", with page size: " + pageSize);
        return numberOfPage;
    }

    /**
     * Description: Method to verify Page Navigation element on list page
     * Verify Next/Previous/Goto page button
     *
     * @param pageSize
     */
    public void verifyPageNavigation(String pageSize) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int totalRecords = countAllRecord();
        log.info("Total records: " + totalRecords);
        selectPageSize(pageSize);
        int countNumberPage = this.numberPageWithPageSize(pageSize, totalRecords);
        if (countNumberPage == 0) {
            Assert.assertNull("No records but Next button is still display", presentElement("nextPage", 5));
            Assert.assertNull("No records but Previous button is still display", presentElement("previousPage", 5));
            Assert.assertNull("No records but GotoPage box is still display", presentElement("goToPage", 5));
        } else if (countNumberPage == 1) {
            Assert.assertFalse("Only one page records but Next button is still enable", isEnable("nextPage"));
            Assert.assertFalse("Only one page records but Previous button is still enable", isEnable("previousPage"));
            Assert.assertNull("No records but GotoPage button is still display", presentElement("goToPage", 5));
        } else {
            Assert.assertTrue("GotoPage button is not display", isDisplayed("goToPage", 10));
            int middlePage = this.numberPageWithPageSize("2", countNumberPage);
            int rangePrevPage = middlePage - 1;
            int rangeNextPage = countNumberPage - middlePage;
            log.info("-----------------------------------------------------------------------------------------------------");
            log.info("Verify Goto page working correctly");
            log.info("Go to the Middle page " + middlePage);
            sendKeysToTextBox("goToPage", String.valueOf(middlePage));
            waitForPageLoadComplete(30, 1);
            verifyPageDisplay(pageSize, middlePage, countNumberPage);
            log.info("-----------------------------------------------------------------------------------------------------");
            log.info("Verify Previous page button working correctly");
            for (int i = rangePrevPage; i >= 1; i--) {
                clickButton("previousPage");
                waitForPageLoadComplete(10, 1);
                this.verifyPageDisplay(pageSize, i, countNumberPage);
            }
            Assert.assertFalse("Stay on first page but Previous button is still enable", isEnable("previousPage"));
            log.info("-----------------------------------------------------------------------------------------------------");
            log.info("Verify Next page button working correctly");
            sendKeysToTextBox("goToPage", String.valueOf(middlePage));
            waitForPageLoadComplete(30, 1);
            for (int i = 1; i <= rangeNextPage; i++) {
                clickButton("nextPage");
                waitForPageLoadComplete(10, 1);
                this.verifyPageDisplay(pageSize, middlePage + i, countNumberPage);
            }
            Assert.assertFalse("Stay on last page but Next button is still enable", isEnable("nextPage"));
        }
    }

    /**
     * Description: Method to verify page detail information is corrected
     *
     * @param pageSize
     * @param currentPage
     * @param lastPage
     */
    public void verifyPageDisplay(String pageSize, int currentPage, int lastPage) {
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int pageSizeValue = Integer.parseInt(pageSize);
        int totalRecordOnPage = this.numberRecordOnCurrentPage();
        log.info("totalRecordOnPage " + totalRecordOnPage);
        String expectedPageDetailValue = (pageSizeValue * (currentPage - 1) + 1) + "-" + (pageSizeValue * (currentPage - 1) + totalRecordOnPage);
        Assert.assertEquals("Page detail at page " + currentPage + " not match as expected", expectedPageDetailValue, getTextField("pageDetails"));
        if (currentPage != lastPage)
            Assert.assertEquals("Total records on page " + currentPage + " not match as page size", pageSizeValue, totalRecordOnPage);
    }

    /**
     * Description: Method to verify list page table items displayed
     *
     * @param sExpectedList
     * @param bTrue
     */
    public void verifyAdvanceSearchResult(String sExpectedList, boolean bTrue) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify " + sExpectedList + " are listed " + bTrue);
        Hashtable[] totalRecords = getListColumnDataOnPage("All", "", "", "", "");
        boolean bresult = false;
        if (totalRecords == null) {
        } else {
            String columnName = "Name";
            String[] allItems = sExpectedList.split("\\|");
            for (int i = 0; i < allItems.length; i++) {
                outerloop:
                for (Hashtable record : totalRecords) {
                    Set keys = record.keySet();
                    for (Object key : keys) {
                        String str = (String) key;
                        if (str.equals(columnName) && record.get(str).equals(allItems[i])) {
                            log.info("Matching Table Records found key <" + str + "><" + record.get(str) + ">");
                            bresult = true;
                            break outerloop;
                        }
                    }
                }
            }
        }
        Assert.assertEquals(" Failed to verify listed Strategies", bTrue, bresult);
    }

    /**
     * method to send check ssl, cipher command to server
     *
     * @param tlsVer, cipher, server
     * @return
     */
    public String sendCMDToServer(String tlsVer, String cipher, String server) throws IOException {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            String output = "";
            String line;
            String cmd = "cmd /c \"java -jar " + EnvSetup.UTILITIES_PATH + "ssltest.jar -sslprotocol " + tlsVer + " -ciphers " + cipher + " " + server + "\"";
            log.info("command: " + cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));
            while ((line = bri.readLine()) != null) {
                output = output + "\n" + line;
            }
            bri.close();
            while ((line = bre.readLine()) != null) {
                output = output + "\n" + line;
            }
            bre.close();
            p.waitFor();
            System.out.println("response: ");
            System.out.println(output);
            return output;
        } catch (Exception err) {
            err.printStackTrace();
            return "";
        }
    }

    /**
     * method to check supported ssl, cipher command to server
     *
     * @param tlsVer, cipher, result response
     * @return True: if support
     * False: if not support
     */
    public boolean checkSupportedSSLCipher(String tlsVer, String cipher, String result) throws IOException {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("tlsVer: " + tlsVer);
        log.info("Cipher suite: " + cipher);
        String regex_Pattern = "(?i)Accepted *" + tlsVer + " *" + cipher;
        log.info("pattern: " + regex_Pattern);
        Pattern pattern = Pattern.compile(regex_Pattern);
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            log.info(matcher.group());
            return true;
        }
        return false;
    }


    public void search(String text) {
        sendKeysToTextBox("searchTable", text);
        waitForPageLoadComplete(60);
    }


    /**
     * This function to click specific button on dialog
     *
     * @param buttonName
     */
    public void clickButtonInDialog(String buttonName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String pageName = getPageName();
        log.info("Clicking on button " + buttonName + " from page:" + pageName);
        waitForElementClickable(findElementByXpath("(//*[@role='dialog']//*[@data-testid='%s'][last()])".formatted(buttonName)));
        findElementByXpath("(//*[@role='dialog']//*[@data-testid='%s'][last()])".formatted(buttonName)).click();
        log.info("---------------------------------------------------------------------------------------");
    }


    /**
     * This function to check a dialog is still visible
     *
     * @param pageName
     */
    public boolean dialogIsVisible(String pageName, int waitSec) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify dialog " + pageName + " is still visible");
        return presentElement(By.xpath("//*[@role='dialog']//*[text()=" + pageName + "']"), waitSec) != null;
    }

    /**
     * @param characters: Special Character
     * @param typeInput:  Basic search or advanced search
     *                    method verify Failed Toast Message Is Not Display When Input Special Character
     */
    public void verifyFailedToastMessageIsNotDisplayWhenInputSpecialCharacter(String characters, String typeInput) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("List of invalid characters: " + characters);
        SoftAssert sortAssert = new SoftAssert();
        for (char input : characters.toCharArray()) {
            if (typeInput.equalsIgnoreCase("basic")) {
                sendKeysToTextBox("searchTable", String.valueOf(input));
                waitForPageLoadComplete(10, 0.4);
            } else {
                applyAdvanceSearch("Name", "=", String.valueOf(input));
                waitForPageLoadComplete(10, 0.4);
            }
            List<WebElement> toastMessages = presentElements(locator.getLocator("toastMessage"), 3);
            if (toastMessages.size() > 0) {
                log.info("Failure toast message displayed when user input text: " + String.valueOf(input));
                utilityFun.wait(5);
            }
            sortAssert.assertEquals(toastMessages.size(), 0, "Failure toast message displayed when user input text: " + String.valueOf(input));
        }
        sortAssert.assertAll();
    }


    /**
     * Method to verify Name record is on top in current page
     *
     * @param name: record name
     * @return true: record name is on top
     * false: record name is not on top
     */
    public boolean verifyRecordNameShowOnTopCurrentPage(String name) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> listStrategy = getListColumnDataOnCurPage("Name");
        if (listStrategy.size() != 0) {
            if (listStrategy.get(0).equals(name)) {
                return true;
            }
            log.info("The Record: " + name + " is not on top in current page list");
            return false;
        } else {
            log.info("!!ERR: the page is empty!!");
            return false;
        }
    }

    /**
     * @param campaignName : Name of campaign that user want to check status
     * @param interval:    interval time between 2 check
     * @param timeout:     time max check status
     *                     This method to wait Campaign completed within specific time period
     */
    public void waitRunCampaignCompleted(String campaignName, int interval, int timeout) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        search(campaignName);
        WebElement ele = this.presentElement("inProgressChip", 1);
        Instant end = Clock.systemDefaultZone().instant().plus(Duration.ofSeconds(timeout));
        while (ele != null && end.isAfter(Clock.systemDefaultZone().instant())) {
            this.sleepInSec(interval);
            clickRefreshButton();
            search(campaignName);
            ele = this.presentElement("inProgressChip", 2);
        }
        if (ele != null) {
            log.info("Campaign " + campaignName + " haven't completed after " + timeout + " sec");
        }
    }

    /**
     * description: Method to compare to 2 dateTime
     * date1 < date2, return value < 0
     * date1 > date2, return value > 0
     * date1 = date2, return value = 0
     */
    public int compareDatetime(String dateTime1, String dateTime2) {
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy H:mm:ss a");//MM-dd-yyyy H:mm:ss a
        Date date1 = null, date2 = null;
        try {
            date1 = sdf.parse(dateTime1);
            date2 = sdf.parse(dateTime2);
        } catch (Exception e) {
            log.error("Maybe The DateTime is incorrectly format");
        }
        return date1.compareTo(date2);
    }

    /**
     * description: Method to verify sort by datetime correctly
     */
    public boolean verifySortByDatetime(List<String> dateTime, String sortBy) {
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean flag = true;
        int count;
        if (dateTime.size() > 10) {
            count = 10;
        } else {
            count = dateTime.size();
        }
        switch (sortBy.toLowerCase()) {
            case "ascending":
                for (int i = 0; i < count - 1; i++) {
                    int num = compareDatetime(dateTime.get(i), dateTime.get(i + 1));
                    if (num > 0) {
                        log.error("Failed: " + dateTime.get(i));
                        log.error("Failed: " + dateTime.get(i + 1));
                        log.error("Failed: ascending sort incorrectly");
                        flag = false;
                        break;
                    }
                }
                break;
            case "descending":
                for (int i = 0; i < count - 1; i++) {
                    int num = compareDatetime(dateTime.get(i), dateTime.get(i + 1));
                    if (num < 0) {
                        log.error("Failed: " + dateTime.get(i));
                        log.error("Failed: " + dateTime.get(i + 1));
                        log.error("Failed: descending sort incorrectly");
                        flag = false;
                        break;
                    }
                }
                break;
        }
        if (flag) {
            log.info("Sort correctly");
        }
        return flag;
    }

    public void monitorUICampaignTill(String expectedStatus, String campName, int findCampTime, int timeout, double interval) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Maximum wait time: " + timeout);
        Instant start = Clock.systemDefaultZone().instant();
        Instant end = start.plus(Duration.ofSeconds(timeout));
        String status;
        By statusLoc = By.xpath("//table[@data-testid='table']/tbody/tr[.//span[text()='%s']]/td[3]/div".formatted(campName));
        while (true) {
            WebElement element = presentElement(statusLoc, findCampTime);
            if (element == null) {
                log.info("Not found campaign: " + campName);
                break;
            }
            status = element.getText();
            if (status == null) {
                log.info("Status is null");
                break;
            }
            if (expectedStatus.equalsIgnoreCase(status)) {
                log.info("Status is: " + expectedStatus);
                log.info("Success monitor campaign: %s with status: %s".formatted(campName, status));
                break;
            }
            if (end.isBefore(Clock.systemDefaultZone().instant())) {
                log.info("Timeout !!!");
                break;
            }
            sleepInSec(interval);
        }
        log.info("Total monitor time is " + Duration.between(start, Clock.systemDefaultZone().instant()).toSeconds() + " second(s)");
    }

    public void monitorCampaignTill(String expectedStatus, String campName, int timeout, double interval) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Maximum wait time: " + timeout);
        Instant start = Clock.systemDefaultZone().instant();
        Instant end = start.plus(Duration.ofSeconds(timeout));
        String status;
        while (true) {
            status = RestMethodsObj.getAttributeValueOfJobCampaign(campName, "status");
            if (status == null) {
                log.info("Status is null");
                break;
            }
            if (status.equalsIgnoreCase(expectedStatus)) {
                log.info("Status is: " + expectedStatus);
                log.info("Success monitor campaign: %s with status: %s".formatted(campName, status));
                break;
            }
            if (end.isBefore(Clock.systemDefaultZone().instant())) {
                log.info("Timeout !!!");
                break;
            }
            sleepInSec(interval);
        }
        log.info("Total monitor time is " + Duration.between(start, Clock.systemDefaultZone().instant()).toSeconds() + " second(s)");
    }

    public void monitorCampaignTillCompleted(String campName, int timeout) {
        monitorCampaignTill("COMPLETED", campName, timeout, 3);
    }

    public void monitorCampaignTillPaused(String campName, int timeout) {
        monitorCampaignTill("PAUSED", campName, timeout, 3);
    }

    public void monitorImportContactListImportCompleted(String listName, String id, int timeout) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Maximum wait time: " + timeout);
        if (id == null || id.isEmpty()) {
            id = RestMethodsContactListObj.getContactListID(listName);
        }
        Instant start = Clock.systemDefaultZone().instant();
        Instant end = start.plus(Duration.ofSeconds(timeout));
        try {
            while (true) {
                JsonObject job = RestMethodsContactListObj.getCListJobLatest(listName, id);
                JsonElement element = job.get("status");
                if (element == null) {
                    break;
                }
                if (element.getAsString().contentEquals("COMPLETED")) {
                    break;
                }
                if (end.isBefore(Clock.systemDefaultZone().instant())) {
                    log.info("Timeout !!!");
                    break;
                }
                sleepInSec(5);
            }
        } catch (Exception e) {
        }
        log.info("Total monitor time is " + Duration.between(start, Clock.systemDefaultZone().instant()).toSeconds() + " second(s)");
    }

    public void verifySMSCompletionCode(String phoneNumber, String smsCompletionCode) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int last2Digits = Integer.parseInt(phoneNumber, phoneNumber.length() - 2, phoneNumber.length(), 10);
        Map<Integer, String> tables = new HashMap<>();
        tables.put(0, "SMS_Queued");
        tables.put(1, "SMS_Sent");
        tables.put(2, "SMS_Delivered");
        tables.put(3, "SMS_Rejected");
        tables.put(4, "SMS_Couldnot_Send");
        tables.put(5, "SMS_Couldnot_Deliver");
        tables.put(6, "SMS_Queued");
        IntStream.range(7, 100).boxed().forEach(i -> tables.put(i, "SMS_Failed"));
        Assert.assertEquals(tables.get(last2Digits), smsCompletionCode);
    }

    /**
     * This function to check Error message when input special character to field
     *
     * @param field: Using field name show on UI - ex: Sender's Display Name
     * @param data:  special character need to check: !@#$%^&*()
     * @param Error: error message expected should be shown
     * @return true/false
     */
    public boolean verifyInputSpecialCharacters(String field, String data, String Error) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info(field + " Should be not accepted special characters");
        for (String specialCharacter : data.split("")) {
            this.inputValueToField(field, specialCharacter);
            WebElement hintMessage = this.findElementByXpath("//*[@aria-label=\"" + field + "\"]//parent::div/following-sibling::div[@class='neo-input-hint']");
            String actHintError = hintMessage.getText();
            log.info("put special character: " + specialCharacter);
            log.info("Error message Actual: " + actHintError);
            log.info("Error message Expected: " + Error);
            if (!actHintError.equals(Error)) {
                return false;
            }
            if (isEnable("save")) {
                return false;
            }
        }
        return true;
    }

    /**
     * This function to input value to field - Using field name show on UI to easily describe steps
     *
     * @param fieldName: Using field name show on UI - ex: Sender's Display Name
     * @param value:     special character need to check: !@#$%^&*()
     */
    public void inputValueToField(String fieldName, String value) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Entering value in " + fieldName + " from page:" + value);
        WebElement inputBox = findElementByXpath("//*[@aria-label=\"%s\"]//following-sibling::div//input".formatted(fieldName));
        if (value != null) {
            inputBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            sleepInSec(0.2);
            inputBox.sendKeys(value);
        }
        log.info("---------------------------------------------------------------------------------------");
    }

    /**
     * This function to verify limit of field - Using field name show on UI to easily describe steps
     *
     * @param fieldName: Using field name show on UI - ex: Sender's Display Name
     * @param max:       maximum length of field
     */
    public boolean maxLengthOfField(String fieldName, int max) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement inputBox = findElementByXpath("//*[@aria-label=\"%s\"]//following-sibling::div//input".formatted(fieldName));
        log.info("max length attribute: " + Integer.parseInt(inputBox.getAttribute("maxlength")));
        if (Integer.parseInt(inputBox.getAttribute("maxlength")) != max) {
            return false;
        } else {
            String s = inputBox.getAttribute("value");
            log.info("Actual: " + inputBox.getAttribute("value"));
            if (s.length() != max) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to get all headers on table in page
     */
    public ArrayList<String> getAllHeaders() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<WebElement> listEleHeader = findElementsByXpath(locator.getLocator("listHeaderOnTable"));
        ArrayList<String> listHeaderName = new ArrayList<>();
        if (listEleHeader.size() != 0) {
            for (WebElement ele : listEleHeader) {
                listHeaderName.add(ele.getText());
            }
        }
        return listHeaderName;
    }

    /**
     * Method to action with specific contact on contact list page
     *
     * @param listName
     * @param action
     */
    public void actionOnContact(String listName, String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        navigateToOutboundPage("contacts");
        basicSearchOnTable(listName);
        clickAction(listName, action);
        waitForPageLoadComplete(10, 1);
    }

    /**
     * method to get tooltip
     *
     * @param ctlname
     * @param expValue
     * @param verify
     * @return
     */
    public String verifytoolTip(String ctlname, String expValue, boolean verify, boolean verifyHeader) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        basicSearchOnTable(ctlname);
        waitForLoader();
        String actValue;
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(findElementByXpath("//span[text()='" + ctlname + "']/../..//*[contains(@class,'neo-icon-error error-icon')]")).click().perform();
            WebElement ctlName = findElementByXpath("//span[text()='" + ctlname + "']/../..//*[contains(@data-testid,'tooltip-wrapper')]");
            actValue = ctlName.getAttribute("aria-label");
            log.info(actValue);
            if (verifyHeader) {
                List<String> attrList = Arrays.asList(actValue.substring(actValue.indexOf("[") + 1, actValue.indexOf("]"))
                        .replaceAll("\s", "").split(","));
                Collections.sort(attrList);
                System.out.println(attrList);
                actValue = actValue.substring(0, actValue.indexOf("[") - 1) + " "
                        + attrList.toString() + actValue.substring(actValue.indexOf("]") + 1);
            }
        } catch (StaleElementReferenceException e) {
            log.info("Stale element exception. trying again");
            Actions actions = new Actions(driver);
            actions.moveToElement(findElementByXpath("//span[text()='" + ctlname + "']/../..//*[contains(@class,'neo-icon-error error-icon')]")).click().perform();
            WebElement ctlName = findElementByXpath("//span[text()='" + ctlname + "']/../..//*[contains(@data-testid,'tooltip-wrapper')]");
            actValue = ctlName.getAttribute("aria-label");
            if (verifyHeader) {
                List<String> attrList = Arrays.asList(actValue.substring(actValue.indexOf("[") + 1, actValue.indexOf("]"))
                        .replaceAll("\s", "").split(","));
                Collections.sort(attrList);
                System.out.println(attrList);
                actValue = actValue.substring(0, actValue.indexOf("[") - 1) + " "
                        + attrList.toString() + actValue.substring(actValue.indexOf("]") + 1);
            }
        }
        log.info("Actual value: " + actValue);
        if (verify) {
            log.info("Actual Value : " + actValue);
            log.info("Expected Value :" + expValue);
            Assert.assertEquals("Value of ToolTip is '" + actValue + "', not match with expected value '" + expValue + "'", expValue, actValue);
        }
        return actValue.trim();
    }

    /**
     * Method to verify element is display by xpath
     * return value is true/false
     *
     * @param xpath
     */
    public boolean isDisplayedElementByXpath(String xpath) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Boolean flag = false;
        List<WebElement> listElement = findElementsByXpath(xpath);
        if (listElement.size() > 0) {
            flag = true;
        }
        return findElementByXpath(xpath).isDisplayed();
    }

    /**
     * @param colName:        Column name want click
     * @param numberOfCLicks: number of times want to click
     */
    public void clickSort(String colName, int numberOfCLicks) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Click to column name %s with number of clicks %d".formatted(colName, numberOfCLicks));
        for (int i = 1; i <= numberOfCLicks; i++) {
            By xpath = By.xpath("//th[@class='noWrap'][@role='columnheader']//span[text() ='" + colName + "']");
            tryClick(xpath, 1);
            waitForPageLoadComplete(10, 2);
        }
        //try to get current status for debugs
        try {
            String statusLoc = "//th[@class='noWrap'][@role='columnheader']/div[./span[text()='%s']]/*".formatted(colName);
            String sortOrderStatus = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .ignoreAll(List.of(NotFoundException.class, NoSuchElementException.class, StaleElementReferenceException.class))
                    .until(d -> {
                        List<WebElement> elements = presentElements(statusLoc, 10);
                        if (elements == null) {
                            return null;
                        }
                        if (elements.size() == 1) {
                            return "SortOrder is default";
                        }
                        String attribute = elements.get(1).getAttribute("class");
                        if (attribute.contains("arrow-up")) {
                            return "SortOrder is Ascending";
                        }
                        if (attribute.contains("arrow-down")) {
                            return "SortOrder is Descending";
                        }
                        return null;
                    });
            log.info("sortOrderStatus: " + sortOrderStatus);
        } catch (Exception e) {
            log.info("Timeout to get sortStatus", e);
        }
    }


    /**
     * Method to parse datetime using provided formatter
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public LocalDateTime parseDateTimeusingDateFormatter(String dateFormat, String date) {
        log.info("---------------------------------------------------------------------------------------");
        try {
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDateTime time = LocalDateTime.parse(date, formatter);
            return time;
        } catch (Exception e) {
            try {
                SimpleDateFormat dateformatter = new SimpleDateFormat(dateFormat);
                Date date1 = dateformatter.parse(date);
                Instant current = date1.toInstant();
                LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());
                return ldt;
            } catch (Exception ee) {
                return null;
            }
        }
    }

    public void verifyAdvanceSearchForStatusCol() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String colNameSearch = new Select(this.findElementByXpath(locator.getLocator("columnNameFieldAdvanceSearch"))).getFirstSelectedOption().getText();
        String operator = new Select(this.findElementByXpath(locator.getLocator("operatorFieldAdvanceSearch"))).getFirstSelectedOption().getText();
        String searchText = new Select(this.findElementByXpath(locator.getLocator("searchTextDropdownAdvanceSearch"))).getFirstSelectedOption().getAttribute("value");
        List<String> listResult = this.getListColumnDataOnAllPage(colNameSearch);
        log.info("Search text : " + searchText);
        log.info("Count records match with this advance search: " + listResult.size());
        log.info("List result: " + listResult);
        listResult = listResult.stream().distinct().toList();
        if (listResult.size() > 0) {
            switch (operator) {
                case "=": {
                    Assert.assertTrue("Result of advance search not match for: " + operator, listResult.size() == 1);
                    Assert.assertTrue("Result of advance search not match for: " + operator, listResult.get(0).equalsIgnoreCase(searchText));
                    break;
                }
                case "!=": {
                    for (String result : listResult) {
                        Assert.assertTrue("Advance search result not match for: " + operator, !result.equalsIgnoreCase(searchText));
                    }
                    break;
                }
                default: {
                    log.info("Operator isn't exist for column name: " + colNameSearch);
                    Assert.fail("Operator isn't exist for column name: " + colNameSearch);
                    break;
                }
            }
        } else {
            log.info("No result match for search '" + colNameSearch + "' with '" + searchText + "' by operator '" + operator + "'");
        }
    }

    public void updatedAdvanceSearch(String columName, String operator, String newSearchText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (columName != null) {
            this.selectDropDownOption(this.findElementByDataID("columnName"), columName);
        }
        if (operator != null) {
            this.selectDropDownOption(this.findElementByDataID("operator"), operator);
        }
        columName = new Select(this.findElementByXpath(locator.getLocator("columnNameFieldAdvanceSearch"))).getFirstSelectedOption().getText();
        if (columName.equalsIgnoreCase("name")) {
            this.sendKeysToTextBox("searchValue", newSearchText);
            this.waitForPageLoadComplete(10, 1);
        } else {
            List<WebElement> contentsElement = this.findElementsByXpath(locator.getLocator("dateTextAdvanceSearch"));
            List<LocalDateTime> contentsList = this.extractContentToDateTime(newSearchText);
            for (int i = 0; i < contentsElement.size(); i++) {
                contentsElement.get(i).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                this.fillDateTimeField("Input", contentsElement.get(i), contentsList.get(i));
            }
        }
    }

    /**
     * Method to verify sort of date time column on current page
     *
     * @return
     */
    public void verifySortDateTimeColumn(String columnName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.selectDropDownOption(this.findElementByDataID("pageSizeDropdown"), "100");
        //ApplyAscendingSort
        this.clickSort(columnName, 1);
        List<String> datadLst = this.getListColumnDataOnCurPage(columnName);
        List<String> filterList = datadLst.stream().filter(s -> !s.equals("-") && !s.equals("In Progress")).collect(Collectors.toList());
        Assert.assertTrue(this.verifySortByDatetime(filterList, "ascending"));
        //ApplyDescendingSort
        this.clickSort(columnName, 1);
        datadLst = this.getListColumnDataOnCurPage(columnName);
        filterList = datadLst.stream().filter(s -> !s.equals("-") && !s.equals("In Progress")).collect(Collectors.toList());
        Assert.assertTrue(this.verifySortByDatetime(filterList, "descending"));
    }

    /**
     * Method to verify sort of date time column across all page
     *
     * @return
     */
    public void verifySortWorkProperlyAcrossAllPageOfDateTimeCoumn(String columnName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("Verify Sort icon should display Ascending when first click on name column");
            //ApplyAscSort
            this.clickSort(columnName, 1);
            List<String> listSortAcs = this.getListColumnDataOnCurPage(columnName);
            Assert.assertTrue(this.verifySortByDatetime(listSortAcs, "ascending"));

            while (this.findElementByDataID("nextPage").isEnabled()) {
                this.clickButton("nextPage");
                this.waitForPageLoadComplete(10, 1);
                listSortAcs = this.getListColumnDataOnCurPage(columnName);
                Assert.assertTrue(this.verifySortByDatetime(listSortAcs, "ascending"));
            }
            this.navigateToPageByGoToPage(1);
            this.waitForPageLoadComplete(10, 1);
            //ApplyDesSort
            this.clickSort(columnName, 1);
            List<String> listSortDes = this.getListColumnDataOnCurPage(columnName);
            Assert.assertTrue(this.verifySortByDatetime(listSortDes, "descending"));
            while (this.findElementByDataID("nextPage").isEnabled()) {
                this.clickButton("nextPage");
                this.waitForPageLoadComplete(10, 1);
                listSortDes = this.getListColumnDataOnCurPage(columnName);
                Assert.assertTrue(this.verifySortByDatetime(listSortDes, "descending"));
            }
        } catch (Exception e) {
            log.info("Sort is not work correctly, Test failed");
        }
    }

    public Map<String, String> getFieldAriaDisable() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, String> results = new HashMap<>();
        String loc = "//div[contains(@class, 'neo-dropdown--active')]//div[starts-with(@data-testid, 'action_')]";
        if (presentElement(By.xpath(loc), 60) == null)
            return results;
        Map<String, String> status = findElementsByXpath(loc).stream().collect(Collectors.toMap(
                e -> e.findElement(By.xpath("div")).getText(),
                e -> e.getAttribute("aria-disabled")
        ));
        log.info(status);
        return status;
    }

    public String getXpathText(String xpath) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return new WebDriverWait(driver, Duration.ofSeconds(EnvSetup.ImplicitWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
    }
}
