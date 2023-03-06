package com.avaya.outbound.frame;

import com.avaya.outbound.lib.UtilityFun;
import com.avaya.outbound.lib.support.Locator;
import com.avaya.outbound.lib.support.TestData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ContactListPage extends CommonFunction {
    private final Logger log = LogManager.getLogger(ContactListPage.class);
    public Locator locator = new Locator(this);
    public WebElement nameFieldWrapper;
    public WebElement popUpHeader;
    public WebElement popupMessage;
    public WebElement pageSizeOptions;
    public WebElement pageSize;
    public WebElement goToPage;
    public WebElement goNextPage;
    public WebElement pageDetail;
    public WebElement numberPageSelected;
    public WebElement sortingArrowUp;
    public WebElement sortingArrowDown;
    public WebElement btnNextPage;

    public ContactListPage(WebDriver driver) {
        super(driver);
    }

    public void createContactList(String nameContact, String description) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Name Contact List: " + nameContact);
        log.info("Description: " + description);
        clickButton("add");
        waitForPageLoadComplete(30, 1);
        sendKeysToTextBox("name", nameContact);
        sleepInSec(0.3);
        sendKeysToTextBox("description", description);
        sleepInSec(0.3);
        clickButton("save");
        waitForPageLoadComplete(30, 0.5);
        tryClick("cancel", 5);
        waitForPageLoadComplete(30, 1);
    }

    public void inputNameAndDescriptionThenClickSave(String nameContact, String description, boolean insensitiveFlag) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (insensitiveFlag) {
            nameContact = nameContact.toUpperCase();
        }
        log.info("Name Contact List: " + nameContact);
        log.info("Description: " + description);
        sendKeysToTextBox("name", nameContact);
        utilityFun.wait(1);
        sendKeysToTextBox("description", description);
        utilityFun.wait(1);
        clickButton("save");
        waitForPageLoadComplete(30, 1);
    }

    public String getHintMessageNameField() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Get Message display On ContactList Landing Page");
        utilityFun.wait(1);
        String hintMessage = driver.findElement(locator.get("hintMessageName")).getText();
        log.info("Hint Message on the Name field: " + hintMessage);
        return hintMessage;
    }

    public String getValueAfterInputName(String inpText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Get value after input text '" + inpText + "' to Name field");
        findElementByDataID("name").clear();
        sleepInSec(0.5);
        sendKeysToTextBox("name", inpText);
        utilityFun.wait(1);
        String value = getAttributeField("name", "value");
        log.info("value: " + value);
        return value;
    }

    public String getTextAfterInputDescription(String inpText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Get text after input text '" + inpText + "' to Description field");
        findElementByDataID("description").clear();
        sleepInSec(0.5);
        sendKeysToTextBox("description", inpText);
        utilityFun.wait(1);
        String description = getTextField("description");
        log.info("Description Text: " + description);
        return description;
    }

    public void verifyHeaderLabelDisplay(String label) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify the label name: '" + label + "' is display on the Header");
        String xpath = "//*[@class='tableFixHeader']//span[text()='" + label + "']";
        log.info("xpath: " + xpath);
        scrollIntoView(By.xpath(xpath));
        Assert.assertNotNull("The label '" + label + "' is not display", presentElement(By.xpath(xpath), 10));
    }

    public void selectAction(String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering into method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Select " + action + " button on Cancellation confirmation box");
        switch (action) {
            case "Leave This Page": {
                clickButton("leave-page");
                waitForPageLoadComplete(5, 1);
                break;
            }
            case "Stay On This Page": {
                clickButton("stay-page");
                waitForPageLoadComplete(5, 1);
                break;
            }
        }
    }

    public boolean verifySortIconDisplayInColumn(String columnName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("Verify Sort icon should display Ascending when first click on name column");
            WebElement nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + columnName + "']"));
            tryClick(nameColumn, 1);
            waitForPageLoadComplete(10, 1);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowUp.isDisplayed());
            log.info("Verify Sort icon should display Descending when second click on name column");
            tryClick(nameColumn, 1);
            waitForPageLoadComplete(10, 1);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowDown.isDisplayed());
            tryClick(nameColumn, 1);
            waitForPageLoadComplete(10, 1);
            log.info("Verify Sort icon should not display when 3rd click time on " + columnName + " column");
            String iconLoc = "//div[@data-testid='header-cell' and ./span[text()='" + columnName + "'] and ./span[starts-with(@class, 'neo-icon-arrow')]]";
            log.info("iconLoc: " + iconLoc);
            WebElement sortIconElem = presentElement(By.xpath(iconLoc), 10);
            Assert.assertNull("Sort is Display on campaign landing page", sortIconElem);
            return true;
        } catch (Exception e) {
            log.info("Sort work is not correctly, Test failed");
        }
        return false;
    }

    public boolean verifySortWorkProperly(String nameColumnSort) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        utilityFun.wait(3);
        try {
            log.info(nameColumnSort);
            List<String> listDefault = this.getListColumnDataOnCurPage(nameColumnSort);
            Collections.sort(listDefault);
            log.info("Verify sort Ascending should work properly");
            WebElement nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + nameColumnSort + "']"));
            tryClick(nameColumn, 1);
            waitForPageLoadComplete(10, 1);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowUp.isDisplayed());
            List<String> listAfterSortAcs = this.getListColumnDataOnCurPage(nameColumnSort);
            Assert.assertEquals("Sort work is not properly", listAfterSortAcs, listDefault);
            log.info("Verify sort Descending  should work properly");
            tryClick(nameColumn, 1);
            waitForPageLoadComplete(10, 1);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowDown.isDisplayed());
            List<String> listAfterSortDes = this.getListColumnDataOnCurPage(nameColumnSort);
            listDefault.sort(Comparator.reverseOrder());
            Assert.assertEquals("Sort work is not properly", listAfterSortDes, listDefault);
            return true;
        } catch (Exception e) {
            log.info(e);
            e.printStackTrace();
            log.info("Sort work not properly, Test is Failed");
        }
        return false;
    }


    public boolean verifySortWorkProperlyWithDifferentPage(String columnName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        tryClick(btnNextPage, 1);
        waitForPageLoadComplete(10, 1);
        List<String> listDefault = this.getListColumnDataOnCurPage(columnName);
        try {
            log.info("Verify Sort icon should display Ascending when first click on name column");
            WebElement nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + columnName + "']"));
            tryClick(nameColumn, 1);
            tryClick(btnNextPage, 1);
            waitForPageLoadComplete(10, 1);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowUp.isDisplayed());
            log.info("Verify Sort work properly with Different page");
            List<String> listSortAcs = this.getListColumnDataOnCurPage(columnName);
            List<String> listManualSortAcs = this.getListColumnDataOnCurPage(columnName);
            Collections.sort(listManualSortAcs);
            Assert.assertEquals("Sort work is not properly", listSortAcs, listManualSortAcs);
            log.info("Verify sort Descending  should work properly");
            log.info("Verify Sort icon should display Descending when second click on name column");
            tryClick(nameColumn, 1);
            tryClick(btnNextPage, 1);
            waitForPageLoadComplete(10, 1);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowDown.isDisplayed());
            log.info("Verify Sort work properly with different page");
            List<String> listSortDes = this.getListColumnDataOnCurPage(columnName);
            List<String> listManualSortDes = this.getListColumnDataOnCurPage(columnName);
            listManualSortDes.sort(Comparator.reverseOrder());
            Assert.assertEquals("Sort work is not properly", listSortDes, listManualSortDes);
            log.info("Verify Sort icon should display correctly when 3rd click time on different page");
            tryClick(nameColumn, 1);
            tryClick(btnNextPage, 1);
            waitForPageLoadComplete(10, 1);
            String iconLoc = "//div[@data-testid='header-cell' and ./span[text()='" + columnName + "'] and ./span[starts-with(@class, 'neo-icon-arrow')]]";
            log.info("iconLoc: " + iconLoc);
            WebElement sortIconElem = presentElement(By.xpath(iconLoc), 10);
            Assert.assertNull("Sort is Display on contact list landing page", sortIconElem);
            List<String> listDefaultAfterSort = this.getListColumnDataOnCurPage(columnName);
            return utilityFun.compareResults(listDefault, listDefaultAfterSort);
        } catch (Exception e) {
            log.info("Sort is not work correctly, Test failed");
        }
        return false;
    }

    public void verifyMessageWithNoRecord(String message) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering into method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("message: " + message);
        String xpath = "//*[text()='" + message + "']";
        log.info("xpath: " + xpath);
        Assert.assertNotNull("The message: '" + message + "' is not display", presentElement(By.xpath(xpath), 10));
    }

    public String getFirstRecordName() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering into method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return findElementByXpath("(//span[@data-testid='columnText'])[1]").getText();
    }

    public void selectPageSize(String pageSize) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        selectDropDownOption(pageSizeOptions, pageSize);
        log.info("Select page size: " + pageSize);
        waitForPageLoadComplete(30, 1);
    }

    public void verifyNumberOfRecordsEachPage(int totalRecords, int pageSize) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int curRecordsFirstPage = findElementsByXpath(locator.getLocator("rowList")).size();
        try {
            int temp = 0;
            if (!this.goNextPage.isEnabled()) {
                Assert.assertEquals("Number of records not match with page size selected", curRecordsFirstPage, totalRecords);
                log.info("Page size is " + pageSize + ", only one page exist. With total records is: " + curRecordsFirstPage);
            } else {
                Assert.assertEquals("Number of records on first page not match with page size selected", curRecordsFirstPage, pageSize);
                temp += curRecordsFirstPage;
                while (this.goNextPage.isEnabled()) {
                    clickButton("nextPage");
                    waitForPageLoadComplete(30, 1);
                    int curRecords = findElementsByXpath(locator.getLocator("rowList")).size();
                    log.info("Total records until current page is " + temp);
                    if (this.goNextPage.isEnabled()) {
                        Assert.assertEquals("Records not match with page size selected", curRecords, pageSize);
                    } else {
                        Assert.assertEquals("Records not match with page size selected", curRecords, totalRecords - temp);
                    }
                    temp += curRecords;
                }
            }
        } catch (Exception e) {
            log.info("Can not count number of records on landing page");
        }
    }

    public void verifyNumberOfPage(String pageSize, int totalRecords) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int expectedNumber = this.numberPageWithPageSize(pageSize, totalRecords);
        log.info("Expected number of page is: " + expectedNumber);
        int countOnUI = findElementsByXpath(locator.getLocator("ULListPageNumber")).size();
        WebElement lastPage = findElementByXpath((locator.getLocator("ULListPageNumber") + "[" + countOnUI + "]/button"));
        int actualNumber = Integer.parseInt(lastPage.getText());
        log.info("Actual number of page is: " + actualNumber);
        Assert.assertEquals("Number of page is not correct!", expectedNumber, actualNumber);
    }

    public void verifyPageDetail(String pageSize, int totalRecords) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String expectedPageDetailValue;
        int countNumberPage = this.numberPageWithPageSize(pageSize, totalRecords);
        int pageSizeValue = Integer.parseInt(pageSize);
        for (int i = 1; i <= countNumberPage; i++) {
            String actualPageDetailValue = this.pageDetail.getText();
            log.info("Actual page detail at page " + i + " is " + actualPageDetailValue);
            if (i < countNumberPage) {
                expectedPageDetailValue = (pageSizeValue * (i - 1) + 1) + "-" + pageSizeValue * i;
                Assert.assertEquals("Page detail at page " + i + " not match as expected", expectedPageDetailValue, actualPageDetailValue);
                log.info("Expected page detail at page " + i + " is " + expectedPageDetailValue);
                clickButton("nextPage");
                waitForPageLoadComplete(30, 1);
            } else {
                expectedPageDetailValue = (pageSizeValue * (i - 1) + 1) + "-" + totalRecords;
                Assert.assertEquals("Page detail at page " + i + " not match as expected", expectedPageDetailValue, actualPageDetailValue);
                log.info("Expected page detail at page " + i + " is " + expectedPageDetailValue);
            }
        }
    }

    public void verifyNextAndPreviousButton(int countNumberPage) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement btnNextPage = findElementByDataID("nextPage");
        WebElement btnPreviousPage = findElementByDataID("previousPage");
        if (countNumberPage == 0) {
            Assert.assertTrue("No records but Next button is still display", presentElement(btnNextPage, 3) == null);
            Assert.assertTrue("No records but Previous button is still display", presentElement(btnPreviousPage, 3) == null);
        } else if (countNumberPage == 1) {
            Assert.assertFalse("Only one page records but Next button is still enable", btnNextPage.isEnabled());
            Assert.assertFalse("Only one page records but Previous button is still enable", btnPreviousPage.isEnabled());
        } else {
            for (int i = 1; i <= countNumberPage; i++) {
                btnNextPage = findElementByDataID("nextPage");
                btnPreviousPage = findElementByDataID("previousPage");
                log.info("Go to page " + i);
                Assert.assertEquals("Page " + i + " is not selected", Integer.parseInt(this.getNumberPageIsSelected()), i);
                if (i == 1) {
                    Assert.assertTrue("Next button is disabled on first page", btnNextPage.isEnabled());
                    Assert.assertFalse("Previous button still enable on first page", btnPreviousPage.isEnabled());
                } else if (i > 1 && i < countNumberPage) {
                    Assert.assertTrue("Next button is disabled", btnNextPage.isEnabled());
                    Assert.assertTrue("Previous button is disabled", btnPreviousPage.isEnabled());
                } else {
                    Assert.assertFalse("Next button still enable on latest page", btnNextPage.isEnabled());
                    Assert.assertTrue("Previous button is disabled on latest page", btnPreviousPage.isEnabled());
                    break;
                }
                clickButton("nextPage");
                waitForPageLoadComplete(30, 1);
            }
        }
    }

    public String getNumberPageIsSelected() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("No any number page selected", numberPageSelected.isDisplayed());
        String pageSelected = numberPageSelected.getText();
        log.info("Number page is selecting: " + pageSelected);
        return pageSelected;
    }

    public String verifyContactListInformationByColumn(String columnName, String expValue, String ctlname, boolean verify) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        basicSearchOnTable(ctlname);
        waitForLoader();
        String actValue;
        try {
            int index = getColumnIndex(columnName) + 1;
            WebElement ctlName = findElementByXpath("//span[text()='" + ctlname + "']/../../td[" + index + "]//span");
            actValue = ctlName.getText();
        } catch (StaleElementReferenceException e) {
            log.info("Stale element exception. trying again");
            int index = getColumnIndex(columnName) + 1;
            WebElement ctlName = findElementByXpath("//span[text()='" + ctlname + "']/../../td[" + index + "]//span");
            actValue = ctlName.getText();
        }
        log.info("Actual value: " + actValue);
        if (verify) {
            Assert.assertEquals("Value of column '" + columnName + "' is '" + actValue + "', not match with expected value '" + expValue + "'", expValue, actValue);
        }
        return actValue.trim();
    }

    /**
     * Method to create contact list
     *
     * @param testData
     */
    public void createContactList(Map<String, String> testData) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        clickButton("add");
        sendKeysToTextBox("name", testData.get("name"));
        sendKeysToTextBox("description", testData.get("description"));
        clickButton("save");
    }

    /**
     * Method to write contact data to csv file
     *
     * @param contacts
     * @param outputFile
     * @param ignoreAttributes
     * @param overwrite
     */
    public void writeContactDatatoOutFile(JsonArray contacts, String outputFile, String[] ignoreAttributes, String sortBy, boolean overwrite) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean alreadyExists = new File(outputFile).exists();
        if (alreadyExists && overwrite) {
            try {
                FileUtils.forceDelete(new File(outputFile));
            } catch (IOException e) {
                log.info("Exception in Delete file : Unable to Delete file " + e.getMessage());
            }
        }
        contacts = utilityFun.JsonObjectSort(contacts, sortBy);
        for (int i = 0; i < contacts.size(); i++) {
            JsonObject contact = contacts.get(i).getAsJsonObject();
            for (String attname : ignoreAttributes) {
                contact.remove(attname);
            }
            List temp = new ArrayList();
            SortedSet<String> sortedKeys = new TreeSet<String>();
            Set<String> keys = contact.keySet();
            Iterator<String> itr = keys.iterator();
            while (itr.hasNext()) {
                sortedKeys.add(itr.next());
            }
            Iterator<String> keysitr = sortedKeys.iterator();
            if (i == 0) {
                log.info("adding headers");
                while (keysitr.hasNext()) {
                    String key = keysitr.next();
                    log.info("adding " + key);
                    temp.add(key.replaceAll("\"", ""));
                }
                utilityFun.writecsv(outputFile, temp);
            }

            temp = new ArrayList();
            itr = sortedKeys.iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                // log.info(contact.get(key));
                temp.add(contact.get(key).toString().replaceAll("\"", ""));
            }

            utilityFun.writecsv(outputFile, temp);
        }
    }


    public void verifyTotalContactsAndLastUpdateVisible() {
        int totalCol = getColumnIndex("Total Contacts");
        Assert.assertEquals("Total contacts column not exist", totalCol, 2);
        log.info("Total contacts column is displayed");
        int lastUpCol = getColumnIndex("Last Updated");
        Assert.assertEquals("Last updated column not exist", lastUpCol, 5);
        log.info("Last updated column is displayed");
    }

    public void verifyImportDSVisible() {
        isDisplayed("action_importContacts", 5);
        log.info("Import contact option is displayed");
    }

    public void verifyPageVisible(String pageName) {
        pageIsVisible(pageName, 5);
    }

    /**
     * Method to check the get total contact with 1 contact list
     * after filter on Contact list page
     *
     * @param name, total
     */
    public void verifyTotalContacts(String name, int total) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        basicSearchOnTable(name);
        String ExpRecords = Integer.toString(total);
        String ActRecords = getSingleDataFromTable(name, "Total Contacts").trim();
        Assert.assertEquals("Total contacts not matching", ExpRecords, ActRecords);
        log.info("Total contacts is correctly");
    }

    /**
     * Method to Run data source
     *
     * @param listName
     * @param dsName
     */
    public void runDataSource(String listName, String dsName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        navigateToOutboundPage("contacts");
        basicSearchOnTable(listName);
        clickAction(listName, "Import Contacts");
        selectDropDownOptionEqual("dataSource", dsName);
        clickButton("run");
    }

    /**
     * Method to monitor import progress status
     *
     * @param listName
     * @param timeout
     */
    public void monitorImportProgress(String listName, int timeout) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String columnName = "Last Updated";
        Date startDate = new Date();
        String progressStatus;
        WebElement ctlName;
        int index = getColumnIndex(columnName) + 1;
        try {
            basicSearchOnTable(listName);
            waitForLoader();
            ctlName = findElementByXpath("//span[text()='" + listName + "']/../../td[" + index + "]/child::*");
            progressStatus = ctlName.getText();
        } catch (StaleElementReferenceException e) {
            log.info("Stale Element exception..trying again");
            ctlName = findElementByXpath("//span[text()='" + listName + "']/../../td[" + index + "]/child::*");
            progressStatus = ctlName.getText();
        }
        log.info("Current Import progress status : " + progressStatus);
        int waitTime = 0;
        while (progressStatus.toLowerCase().contains("in progress") || progressStatus.isEmpty()) {
            try {
                ctlName = findElementByXpath("//span[text()='" + listName + "']/../../td[" + index + "]/child::*");
                progressStatus = ctlName.getText();

                log.info(progressStatus);
                Date endDate = new Date();
                long difference = (endDate.getTime() - startDate.getTime()) / 1000;
                log.info("difference: " + difference);
                if (difference >= timeout) {
                    log.info("Contact Import is in progress for more than" + timeout + " seconds");
                    break;
                }
                waitTime = waitTime + 5;
                utilityFun.wait(waitTime);
                tryClick("refresh", 3);
                basicSearchOnTable(listName);
                waitForLoader();
            } catch (Exception e) {
                log.info("Exception in checking import progress status", e);
            }
        }
        log.info("---------------------------------------------------------------------------------------------------------------------------------------");
        Date endDate = new Date();
        long difference = (endDate.getTime() - startDate.getTime()) / 1000;
        log.info("Time taken to Import contact to: " + listName + " : " + difference + " Seconds");
    }

    public void searchAndClickThreeDotsContactList(String nameContactList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            Actions actions = new Actions(driver);
            sendKeysToTextBox("searchTable", nameContactList);
            utilityFun.wait(2);
            this.clickThreeDotsBesideContactList(nameContactList);
        } catch (Exception e) {
            Assert.fail("No find contact list on contact list landing page");
        }
    }

    public boolean verifyHyperLink(String contactListName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String hyperLinkXpath = locator.locatorFormat("hyperlinkTotalContact", contactListName);
        sendKeysToTextBox("searchTable", contactListName);
        waitForPageLoadComplete(10, 1);
        List<WebElement> hyperLinkElement = presentElements(hyperLinkXpath, 30);
        if (hyperLinkElement.size() != 0) {
            return true;
        }
        return false;
    }

    public String coverHeaderName(String nameColumn) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (nameColumn) {
            case "firstname":
                nameColumn = "First Name";
                break;
            case "lastname":
                nameColumn = "Last Name";
                break;
            case "phone1":
                nameColumn = "Phone Number";
                break;
            case "email":
                nameColumn = "Email";
                break;
            default:
                System.out.println("Not find name header" + nameColumn);
                break;
        }
        return nameColumn;
    }

    public void verifySortFunctionOnEveryColumn(String columnName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            List<String> defaultList = this.getListColumnDataOnCurPage(columnName);
            log.info("defaultList: " + defaultList);
            List<String> list = this.getListColumnDataOnCurPage(columnName);
            this.clickSort(columnName, 1);
            Collections.sort(list);
            Assert.assertTrue("Sort icon is not display on contact landing page", sortingArrowUp.isDisplayed());
            log.info("List after sorting 1 time: " + this.getListColumnDataOnCurPage(columnName));
            Assert.assertTrue("Sort ascending of " + columnName + " is not working", this.getListColumnDataOnCurPage(columnName).equals(list));
            this.clickSort(columnName, 1);
            list.sort(Comparator.reverseOrder());
            Assert.assertTrue("Sort icon is not display on contact landing page", sortingArrowDown.isDisplayed());
            log.info("List after sorting 2 time: " + this.getListColumnDataOnCurPage(columnName));
            Assert.assertTrue("Sort descending of " + columnName + " is not working", this.getListColumnDataOnCurPage(columnName).equals(list));
            this.clickSort(columnName, 1);
            Assert.assertNull("Sort arrow up icon is still display on contact landing page", presentElement(locator.get("sortIconArrowUp"), 2));
            Assert.assertNull("Sort arrow down icon is still display on contact landing page", presentElement(locator.get("sortIconArrowUp"), 2));
            log.info("List after sorting 3 time: " + this.getListColumnDataOnCurPage(columnName));
            Assert.assertTrue("Sort three time of " + columnName + " is not working", this.getListColumnDataOnCurPage(columnName).equals(defaultList));
        } catch (Exception e) {
            log.info("Verify sort - Failed, " + e);
        }
    }

    public List<String> getAllAttributeOnHeaderContact() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> list = new ArrayList<>();
        int size = findElementsByXpath("//*[@class='tableFixHeader']//div[@data-testid='header-cell']").size();
        try {
            for (int i = 1; i <= size; i++) {
                String att = findElementByXpath("//*[@class='tableFixHeader']//th[" + (i + 1) + "]").getText().trim();
                list.add(att);
            }
            log.info("List attribute on header: " + list);
        } catch (Exception e) {
            log.info("Get list attribute on header - failed, " + e);
        }
        return list;
    }

    public void verifyTimestampRecordsByRestAPI(JsonArray contacts) {
        for (int i = 0; i < contacts.size(); i++) {
            String jsArr = String.valueOf(contacts.get(i).getAsJsonObject().get("CreatedOn"));
            jsArr = jsArr.substring(1, jsArr.length() - 1);
            String[] dateNTime = jsArr.split("T");
            String[] dateStr = dateNTime[0].split("-");
            String[] timeStr = dateNTime[1].split(":");

            log.info(dateStr.toString());
            log.info(timeStr.toString());

            String year = String.valueOf(dateStr[0]);
            String month = String.valueOf(dateStr[1]);
            String day = String.valueOf(dateStr[2]);
            String hours = String.valueOf(timeStr[0]);
            String minutes = String.valueOf(timeStr[1]);
            String seconds = String.valueOf(timeStr[2]);

            String dateTimeStr = month + "/" + day + "/" + year + " " + hours + ":" + minutes + ":" + seconds;
            log.info(dateTimeStr);
            UtilityFun.compareTimeBasicFomatter(dateTimeStr);
        }
    }

    /**
     * This method is used to create contactlist runtime for testing 750k import list
     *
     * @param csvfile
     * @param numberofRec
     */
    public void createImportCSV(String csvfile, int numberofRec, boolean validations) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        String attributes = "firstname,lastname,phone1,email";
        List<String> headers = new ArrayList<String>(Arrays.asList(attributes.split(",")));
        boolean alreadyExists = new File(csvfile).exists();
        if (alreadyExists) {
            try {
                FileUtils.forceDelete(new File(csvfile));
            } catch (IOException e) {
                log.info("Exception in Delete file : Unable to Delete file " + e.getMessage());
            }
        }
        utilityFun.writecsv(csvfile, headers);
        log.info("creating CSV file with " + numberofRec + " records");
        try {
            FileOutputStream fos = new FileOutputStream(csvfile, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            CSVWriter csvOutput = new CSVWriter(osw);
            for (int i = 0; i < numberofRec; i++) {
                List<String> temp = new ArrayList();
                if (validations && (i % 7 == 0)) {
                    temp.add("\"FirstName,  Avaya %s\"".formatted(i));
                    temp.add("LastName" + i);
                    temp.add("212 ABC 121" + i);
                    if (i % 8 == 0) {
                        temp.add("testEmail" + i + "@testcom");
                    } else {
                        temp.add("testEmail" + i + "@test.com");
                    }
                } else {
                    temp.add("FirstName" + i);
                    temp.add("LastName" + i);
                    temp.add("212121" + i);
                    temp.add("testEmail" + i + "@test.com");
                }
                csvOutput.writeNext(temp.toArray(String[]::new));
            }
            csvOutput.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("exception while closing file-" + e.getMessage());
        }

        log.info("File created successfully");

    }

    public void verifyTimeZoneForLastUpdate(List<String> timezone) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (!SystemUtils.IS_OS_WINDOWS)
            return;
        String currentTimeZone = windowExec("tzutil", "/g");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a");
            Supplier<LocalDateTime> getWindowTime = () -> {
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy H:mm:ss");
                String[] getCurTimeCmd = {"cmd.exe", "/C", "echo %DATE% %TIME%"};
                // Fri 11/18/2022 15:42:12.71\r\n
                // Fri 11/18/2022  3:42:12.71\r\n
                String textTime = windowExec(getCurTimeCmd);
                textTime = textTime.trim().replaceFirst("\\.\\d+\\s*", "");
                textTime = textTime.replaceFirst("\\s{2,}", " ");
                return LocalDateTime.parse(textTime, formatter1);
            };
            for (String tz : timezone) {
                log.info("The time zone: " + tz);
                // get first contact data for LastUpdate column the parse to Date type
                LocalDateTime lastUpdated = LocalDateTime.parse(getListColumnDataOnCurPage("Last Updated").get(0), formatter);
                LocalDateTime localTime = getWindowTime.get();
                long durationSec = ChronoUnit.SECONDS.between(localTime, lastUpdated);
                log.info("durationSec : " + durationSec);

                windowExec("tzutil", "/s", "\"" + tz + "_dstoff\"");
                utilityFun.wait(1);
                clickRefreshButton();
                utilityFun.wait(1);

                LocalDateTime lastUpdated2 = LocalDateTime.parse(getListColumnDataOnCurPage("Last Updated").get(0), formatter);
                LocalDateTime localTime2 = getWindowTime.get();
                long durationSec2 = ChronoUnit.SECONDS.between(localTime2, lastUpdated2);
                log.info("durationSec2 : " + durationSec2);
                log.info("durationSec2 - durationSec : " + (durationSec2 - durationSec));

                Assert.assertTrue(Math.abs(durationSec2 - durationSec) < 60);
            }
        } catch (Exception e) {
            log.info("Exception", e);
        } finally {
            if (currentTimeZone != null) {
                windowExec("tzutil", "/s", "\"" + currentTimeZone + "\"");
            }
        }
    }

    public void clickThreeDotsBesideContactList(String nameContactList) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Actions actions = new Actions(driver);
        int indexRow = getRowIndex("Name", nameContactList);
        actions.moveToElement(presentElement(By.xpath("//button[@id='dropdown_" + indexRow + "']"), 10)).click().perform();
    }

    public void importContactToList(Map<String, String> data, String contactListName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        tryClick("action_importContacts", 10);
        selectDropDownOptionEqual("dataSource", data.get("name"));
        clickButton("run");
        String toastMessage = this.getToastMessage();
        Assert.assertEquals("Message display is incorrect", "Contact import started successfully for the contact list " + contactListName, toastMessage);
        log.info("Monitor import process status");
        this.monitorImportProgress(contactListName, 60);
    }

    /**
     * Method to add new attribute in list
     *
     * @param data
     */
    public void addAttribute(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        clickButton("add");
        if (!data.get("name").isEmpty()) {
            sendKeysToTextBox("name", data.get("name"));
        }
        if (!data.get("dataType").isEmpty()) {
            selectDropDownOptionEqual("dataType", data.get("dataType"));
        }
        if (data.containsKey("Save") && data.get("Save").equalsIgnoreCase("no")) {
            log.info("do not click on Save");
        } else {
            log.info("click on save");
            clickButton("save");

        }

    }

    /**
     * Method to verify clear search button on basic search and refresh button
     *
     * @param currentList
     */
    public void verifyClearBasicSearchOrRefresh(List<String> currentList) {
        log.info("-------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.assertTrue("Search box not empty", getAttributeField("searchTable", "value").isEmpty());
        List<String> clearList = getListColumnDataOnAllPage("Name");
        Assert.assertEquals("Search list doesn't return default", currentList, clearList);
    }

    /**
     * Method to verify working of basic search
     * Get current list attribute by API
     *
     * @param data
     */
    public boolean verifyAttributeTableBasicSearch(Map<String, String> data) {
        log.info("-------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean found = false;
        List<String> expList = new ArrayList<>();
        if (!data.get("expectedSearchList").equals("")) {
            expList = Arrays.asList(data.get("expectedSearchList").split("\\|"));
        }
        String searchInput = data.get("searchText");
        List<String> searchList = getListColumnDataOnAllPage("Name");
        log.info("Search List: " + searchList);
        log.info("Expected List: " + expList);
        if (searchList.size() == expList.size()) {
            searchList.equals(expList);
            found = true;
        }
        if (searchList.size() > expList.size()) {
            for (String name : searchList) {
                if (!name.toLowerCase().contains(searchInput.toLowerCase())) {
                    found = false;
                    break;
                } else {
                    found = true;
                }
            }
        }
        return found;
    }

    /**
     * Method to verify a new attribute is created with correct information
     *
     * @param data
     */
    public void verifyAttributeTable(Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            basicSearchOnTable(data.get("name"));
            Assert.assertEquals("ERR!!! Column Name String is not match", data.get("name"), getSingleDataFromTable(data.get("name"), "Name"));
            Assert.assertEquals("ERR!!! Column Data Type String is not match", data.get("dataType").toUpperCase(), getSingleDataFromTable(data.get("dataType").toUpperCase(), "Data Type"));
            Assert.assertEquals("ERR!!! Column Type String is not match", data.get("type").toUpperCase(), getSingleDataFromTable(data.get("type").toUpperCase(), "Type").toUpperCase());
        } catch (Exception e) {
            log.info("ERR!!!. Can not verify for new attribute: " + data.get("name"));
            throw new RuntimeException(e);
        }
    }

    /**
     * @method to verify advance search result for Name column
     */
    public void verifyAdvSearchResult() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String colNameSearch = new Select(findElementByDataID("columnName")).getFirstSelectedOption().getText();
        String operator = new Select(findElementByDataID("operator")).getFirstSelectedOption().getText();
        String searchText = findElementByDataID("searchValue").getAttribute("value");
        List<String> listResult = new ArrayList<>();
        List<WebElement> checkEmpty = presentElements(locator.getLocator("emptyListMessage"), 5);
        Boolean emptyList = true;
        if (checkEmpty.size() > 0) {
            log.info("List result is empty");
        } else {
            emptyList = false;
            listResult = getListColumnDataOnAllPage(colNameSearch);
        }
        log.info("Search text : " + searchText);
        log.info("Count records match with this advance search: " + listResult.size());
        log.info("List result: " + listResult);

        switch (operator) {
            case "=": {
                Assert.assertTrue("Result of advance search by operator '=' more than 1", listResult.size() == 1);
                Assert.assertTrue("Result of advance search not match for: " + operator, listResult.get(0).equals(searchText));
                break;
            }
            case "!=": {
                Assert.assertFalse("ERR! List should be had values", emptyList);
                for (String result : listResult) {
                    Assert.assertTrue("Advance search result not match for: " + operator, !result.equalsIgnoreCase(searchText));
                }
                break;
            }
            case "In": {
                Assert.assertFalse("ERR! List should be had values", emptyList);
                List<String> searchKey = Arrays.stream(searchText.toLowerCase().split(",")).map(String::trim).collect(Collectors.toList());
                for (String result : listResult) {
                    Assert.assertTrue("Advance search result not match for: " + operator, searchKey.contains(result.toLowerCase()));
                }
                break;
            }
            case "Like": {
                Assert.assertFalse("ERR! List should be had values", emptyList);
                for (String result : listResult) {
                    Assert.assertTrue("Advance search result not match for: " + operator, result.toLowerCase().contains(searchText.toLowerCase()));
                }
                break;
            }
            case "Not Like": {
                Assert.assertFalse("ERR! List should be had values", emptyList);
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
    }

    /**
     * @method to verify advance search result for Name column
     * with result is empty
     */
    public void verifyAdvSearchResultIsEmpty() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<WebElement> checkEmpty = presentElements(locator.getLocator("emptyListMessage"), 5);
        Boolean emptyList = true;
        if (checkEmpty.size() > 0) {
            log.info("List result is empty");
        } else {
            emptyList = false;
        }
        Assert.assertTrue("List result is not empty", emptyList);
    }

    /**
     * Method to Edit contact with Dynamic attributes
     *
     * @param testData
     */
    public void editcontact(TestData<String, String> testData) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        for (Map<String, String> attribute : attlist) {
            if (attribute.containsKey("EditValue")) {
                //log.info(attribute);
                switch (attribute.get("dataType").toLowerCase()) {
                    case "phone":
                    case "character":
                    case "zip":
                    case "float":
                    case "email":
                    case "string":
                    case "integer": {
                        log.info("---------------------------------------------------------------------");
                        log.info("Edit field : %s to value : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                        sendKeysToTextBox(attribute.get("name"), attribute.get("EditValue"));
                        if (attribute.get("ExpectedError") != null) {
                            log.info("For Data Type %s : Verify Error is displayed for invalid value in Field %s".formatted(attribute.get("dataType"), attribute.get("name")));
                            verifyFieldError(attribute.get("name"), attribute.get("ExpectedError"));

                        }
                        break;
                    }
                    case "date": {
                        log.info("---------------------------------------------------------------------");
                        log.info("Edit Date field : %s to value : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                        if (testData.getData("EditContact", "userDatePicker") != null && testData.getData("EditContact", "userDatePicker").equals("yes")) {
                            log.info("Use date picker for date");
                            sendKeysToTextBox(attribute.get("name"), attribute.get("EditValue"));
                            findElementByDataID(attribute.get("name")).sendKeys(Keys.ESCAPE);
                            utilityFun.wait(1);
                            LocalDateTime datelist = parseDateTimeusingDateFormatter(testData.getData("EditContact", "dateformat"), attribute.get("EditValue"));
                            tryClick(attribute.get("name"), 2);
                            selectCalendar(datelist, "", "MMM yyyy dd", testData.getData("EditContact", ""));

                        } else {
                            sendKeysToTextBox(attribute.get("name"), attribute.get("EditValue"));
                            findElementByDataID(attribute.get("name")).sendKeys(Keys.ESCAPE);

                        }
                        if (attribute.get("ExpectedError") != null) {
                            log.info("For Data Type %s : Verify Error is displayed for invalid value in Field %s".formatted(attribute.get("dataType"), attribute.get("name")));
                            verifyFieldError(attribute.get("name"), attribute.get("ExpectedError"));

                        }
                        break;
                    }
                    case "time": {
                        log.info("---------------------------------------------------------------------");
                        if (testData.getData("EditContact", "userDatePicker") != null && testData.getData("EditContact", "userDatePicker").equals("yes")) {
                            log.info("Use date picker for time");
                            tryClick(attribute.get("name"), 2);
                            selectCalendar(null, attribute.get("EditValue"), "", testData.getData("EditContact", "timeformat"));
                        } else {
                            log.info("Edit Time field %s to value : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                            sendKeysToTextBox(attribute.get("name"), attribute.get("EditValue"));
                        }
                        if (attribute.get("ExpectedError") != null) {
                            log.info("For Data Type %s : Verify Error is displayed for invalid value in Field %s".formatted(attribute.get("dataType"), attribute.get("name")));
                            verifyFieldError(attribute.get("name"), attribute.get("ExpectedError"));

                        }
                        break;
                    }
                    case "datetime": {
                        log.info("---------------------------------------------------------------------");
                        if (testData.getData("EditContact", "userDatePicker") != null && testData.getData("EditContact", "userDatePicker").equals("yes")) {
                            log.info("Use date picker for datetime");
                            sendKeysToTextBox(attribute.get("name"), attribute.get("EditValue"));
                            findElementByDataID(attribute.get("name")).sendKeys(Keys.ESCAPE);
                            utilityFun.wait(1);
                            LocalDateTime datelist = parseDateTimeusingDateFormatter(testData.getData("EditContact", "datetimeformat"), attribute.get("EditValue"));
                            tryClick(attribute.get("name"), 2);
                            selectCalendar(datelist, "", "MMM yyyy dd", testData.getData("EditContact", "timeformat"));
                        } else {
                            log.info("Use sendkeys");
                            log.info("Edit datetime filed %s to value : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                            sendKeysToTextBox(attribute.get("name"), attribute.get("EditValue"));
                            findElementByDataID(attribute.get("name")).sendKeys(Keys.ESCAPE);
                        }
                        if (attribute.get("ExpectedError") != null) {
                            log.info("For Data Type %s : Verify Error is displayed for invalid value in Field %s".formatted(attribute.get("dataType"), attribute.get("name")));
                            verifyFieldError(attribute.get("name"), attribute.get("ExpectedError"));

                        }

                        break;
                    }
                    case "boolean": {
                        log.info("---------------------------------------------------------------------");
                        log.info("Edit boolean field %s to value : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                        String checked = getAttributeField(attribute.get("name"), "aria-checked");
                        log.info("Current value of boolen field : " + checked);
                        log.info("New value of boolen field : " + attribute.get("EditValue"));
                        log.info(checked.contentEquals("true"));
                        log.info(checked.contentEquals("false"));
                        if (checked.contentEquals("false") && (attribute.get("EditValue").toLowerCase().contentEquals("true") || attribute.get("EditValue").toLowerCase().contentEquals("1"))) {
                            log.info("Select boolean attribute to true");
                            clickToggleButton(attribute.get("name"));

                        } else if (checked.contentEquals("true") && (attribute.get("EditValue").toLowerCase().contentEquals("false") || attribute.get("EditValue").toLowerCase().contentEquals("0"))) {
                            log.info("Select boolean attribute to false");
                            clickToggleButton(attribute.get("name"));
                        }
                        break;
                    }

                }
            }
        }

        if (testData.getData("EditContact", "save") != null && testData.getData("EditContact", "save").equals("no")) {
            log.info("No need to click on save");
        } else {

            if (testData.getData("EditContact", "verifyError") != null && testData.getData("EditContact", "verifyError").equals("yes")) {
                log.info("Verify save button is disabled");
                Assert.assertFalse("Save button is enabled", isEnable("save"));
                tryClick("cancel", 10);
                clickButton("leave-page");
            } else {
                log.info("Click on Save contact");
                tryClick("save", 10);

                if (testData.getData("EditContact", "NotificationAlert") != null) {
                    Map<String, String> confirmMessage = new HashMap<>();
                    confirmMessage.put("NotificationAlert", testData.getData("EditContact", "NotificationAlert"));
                    verifyNotification(confirmMessage);
                    //tryClick("cancel", 10);
                    // clickButton("leave-page");
                } else if (testData.getData("EditContact", "fielderror") != null) {
                    verifyFieldError(testData.getData("EditContact", "errorFeildname"), testData.getData("EditContact", "fielderror"));

                }
                waitForPageLoadComplete(30, 0.5);

            }
        }

    }

    /**
     * Method to verify edited contact information
     *
     * @param testData
     */
    public void verifyEditedContact(TestData<String, String> testData) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        for (Map<String, String> attribute : attlist) {
            if (attribute.containsKey("EditValue")) {
                //log.info(attribute);
                switch (attribute.get("dataType").toLowerCase()) {
                    case "phone":
                    case "character":
                    case "zip":
                    case "float":
                    case "email":
                    case "string":
                    case "integer": {
                        log.info("---------------------------------------------------------------------");
                        log.info("Verify field : %s value is changed to : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                        String attvalue = getAttributeField(attribute.get("name"), "value");
                        log.info(attvalue);
                        Assert.assertEquals("Field : %s value is not changed to : %s".formatted(attribute.get("name"), attribute.get("EditValue")), attribute.get("EditValue"), attvalue);
                        break;
                    }
                    case "date": {
                        log.info("---------------------------------------------------------------------");
                        log.info("Verify date field : %s value is changed to : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                        String attvalue = getAttributeField(attribute.get("name"), "value");
                        log.info(attvalue);
                        Assert.assertEquals("Field : %s value is not changed to : %s".formatted(attribute.get("name"), attribute.get("EditValue")), attribute.get("EditValue"), attvalue);
                        break;
                    }
                    case "time": {
                        log.info("---------------------------------------------------------------------");
                        log.info("Verify time field : %s value is changed to : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                        String attvalue = getAttributeField(attribute.get("name"), "value");
                        log.info(attvalue);
                        Assert.assertEquals("Field : %s value is not changed to : %s".formatted(attribute.get("name"), attribute.get("EditValue")), attribute.get("EditValue"), attvalue);
                        break;
                    }
                    case "datetime": {
                        log.info("---------------------------------------------------------------------");
                        log.info("Verify datetime field : %s value is changed to : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                        String attvalue = getAttributeField(attribute.get("name"), "value");
                        log.info(attvalue);
                        Assert.assertEquals("Field : %s value is not changed to : %s".formatted(attribute.get("name"), attribute.get("EditValue")), attribute.get("EditValue"), attvalue);
                        break;
                    }
                    case "boolean": {
                        log.info("---------------------------------------------------------------------");
                        log.info("Verify boolean field : %s value is changed to : %s".formatted(attribute.get("name"), attribute.get("EditValue")));
                        String checked = getAttributeField(attribute.get("name"), "aria-checked");
                        log.info(checked);

                        if (attribute.get("EditValue").toLowerCase().contentEquals("true") || attribute.get("EditValue").toLowerCase().contentEquals("1")) {
                            Assert.assertEquals("Boolean field : %s value is not changed to : true".formatted(attribute.get("name")), "true", checked);

                        } else if (attribute.get("EditValue").toLowerCase().contentEquals("false") || attribute.get("EditValue").toLowerCase().contentEquals("0")) {
                            Assert.assertEquals("Boolean field : %s value is not changed to : false".formatted(attribute.get("name")), "false", checked);
                        }
                        break;
                    }

                }
            }
        }
        log.info("Click on cancel contact");
        tryClick("cancel", 10);

        waitForPageLoadComplete(30, 0.5);

    }

    /**
     * Method to verify Edit contact show attributes grouped by Type USER/PHONE/EMAIL/ZIP
     *
     * @param testData
     */
    public void verifyContactFieldGrouped(TestData<String, String> testData) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ArrayList<Map<String, String>> attlist = testData.getData("contactAttribute");
        ArrayList<Map<String, String>> attlist1 = testData.getData("contactAttribute");
        for (Map<String, String> attribute : attlist) {
            switch (attribute.get("type").toLowerCase()) {
                case "user": {
                    log.info("--------------------------------------USER-------------------------------" + attribute.get("dataType"));
                    if (!attribute.get("dataType").toLowerCase().contentEquals("phone") && !attribute.get("dataType").toLowerCase().contentEquals("email") && !attribute.get("dataType").toLowerCase().contentEquals("zip") && !attribute.get("dataType").toLowerCase().contentEquals("predefined")) {
                        log.info("Verify USER Type attribute %s displayed in USER Attributes section".formatted(attribute.get("name")));
                        String xpath = "//*[@data-testid='USER']//*[@data-testid='%s']".formatted(attribute.get("name"));
                        log.info(xpath);
                        WebElement ispresent = presentElement(By.xpath(xpath), 5);
                        Assert.assertNotNull("USER Type attribute %s is not displayed in USER Attributes section".formatted(attribute.get("name")), ispresent);
                    } else if (attribute.get("dataType").toLowerCase().contentEquals("phone")) {
                        log.info("Verify %s USER Type attribute %s displayed in %s Attributes section".formatted(attribute.get("dataType"), attribute.get("name"), attribute.get("dataType")));
                        String xpath = "//*[@data-testid='PHONE']//*[@data-testid='%s']".formatted(attribute.get("name"));
                        log.info(xpath);
                        WebElement ispresent = presentElement(By.xpath(xpath), 5);
                        Assert.assertNotNull("Verify %s USER Type attribute %s is not displayed in %s Attributes section".formatted(attribute.get("dataType"), attribute.get("name"), attribute.get("dataType")), ispresent);

                    } else if (attribute.get("dataType").toLowerCase().contentEquals("email")) {
                        log.info("Verify %s USER Type attribute %s displayed in %s Attributes section".formatted(attribute.get("dataType"), attribute.get("name"), attribute.get("dataType")));
                        String xpath = "//*[@data-testid='EMAIL']//*[@data-testid='%s']".formatted(attribute.get("name"));
                        log.info(xpath);
                        WebElement ispresent = presentElement(By.xpath(xpath), 5);
                        log.info("Attribute exist " + ispresent);
                        Assert.assertNotNull("Verify %s USER Type attribute %s is not displayed in %s Attributes section".formatted(attribute.get("dataType"), attribute.get("name"), attribute.get("dataType")), ispresent);

                    } else if (attribute.get("dataType").toLowerCase().contentEquals("zip")) {
                        log.info("Verify %s USER Type attribute %s displayed in %s Attributes section".formatted(attribute.get("dataType"), attribute.get("name"), attribute.get("dataType")));
                        String xpath = "//*[@data-testid='ZIP']//*[@data-testid='%s']".formatted(attribute.get("name"));
                        log.info(xpath);
                        WebElement ispresent = presentElement(By.xpath(xpath), 5);
                        Assert.assertNotNull("Verify %s USER Type attribute %s is not displayed in %s Attributes section".formatted(attribute.get("dataType"), attribute.get("name"), attribute.get("dataType")), ispresent);

                    }
                    break;
                }
                case "predefined": {
                    log.info("--------------------------predefined-------------------------------------------");
                    String attributeName = attribute.get("name");
                    log.info(attributeName);
                    for (Map<String, String> innerattribute : attlist1) {
                        //find parent attribute for predefined
                        if (attributeName.contains(innerattribute.get("name")) && !innerattribute.get("name").equalsIgnoreCase(attributeName)) {
                            log.info(attributeName + " " + innerattribute.get("dataType") + " " + innerattribute.get("name"));
                            if (innerattribute.get("dataType").equalsIgnoreCase("phone")) {
                                log.info("Verify Predefined %s Type attribute %s displayed in PHONE Attributes section".formatted(attribute.get("dataType"), attribute.get("name")));
                                String xpath = "//*[@data-testid='PHONE']//*[@data-testid='%s']".formatted(attribute.get("name"));
                                log.info(xpath);
                                WebElement ispresent = presentElement(By.xpath(xpath), 5);
                                Assert.assertTrue("%s predefined Type attribute %s is not displayed in %s Attributes section".formatted(attribute.get("dataType"), attribute.get("name"), attribute.get("dataType")), ispresent != null);

                            } else if (innerattribute.get("dataType").equalsIgnoreCase("email")) {
                                log.info("Verify Predefined %s Type attribute %s displayed in EMAIL Attributes section".formatted(attribute.get("dataType"), attribute.get("name")));
                                String xpath = "//*[@data-testid='EMAIL']//*[@data-testid='%s']".formatted(attribute.get("name"));
                                log.info(xpath);
                                WebElement ispresent = presentElement(By.xpath(xpath), 5);
                                Assert.assertTrue("%s predefined Type attribute %s is not displayed in %s Attributes section".formatted(attribute.get("dataType"), attribute.get("name"), attribute.get("dataType")), ispresent != null);

                            }
                            if (innerattribute.get("dataType").equalsIgnoreCase("zip")) {
                                log.info("Verify Predefined %s Type attribute %s displayed in ZIP Attributes section".formatted(attribute.get("dataType"), attribute.get("name")));
                                String xpath = "//*[@data-testid='ZIP']//*[@data-testid='%s']".formatted(attribute.get("name"));
                                log.info(xpath);
                                WebElement ispresent = presentElement(By.xpath(xpath), 5);
                                Assert.assertTrue("%s predefined Type attribute %s is not displayed in %s Attributes section".formatted(attribute.get("dataType"), attribute.get("name"), attribute.get("dataType")), ispresent != null);

                            }
                        }

                    }

                    break;
                }

            }
        }
    }
}
