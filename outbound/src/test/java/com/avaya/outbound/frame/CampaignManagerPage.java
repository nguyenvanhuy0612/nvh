package com.avaya.outbound.frame;

import com.avaya.outbound.lib.support.Locator;
import com.avaya.outbound.lib.support.TestData;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class CampaignManagerPage extends CommonFunction {
    private final Logger log = LogManager.getLogger(CampaignManagerPage.class);
    public Locator locator = new Locator(this);

    public WebElement closeButton;
    public WebElement campaignSummaryMenu;
    public WebElement textBoxUserName;
    public WebElement btnAddNewCampaign;
    public WebElement btnSaveCampaign;

    //Campaign Configuration detail tab
    public WebElement tabDetails;
    public WebElement tabCampaign;
    public WebElement tabCompletionProcessing;
    public WebElement txtBoxCampaignName;
    public WebElement txtBoxDescription;
    public WebElement selectTypeInfinite;
    public WebElement selectTypeFinite;
    public WebElement btnCheckAll;
    public WebElement btnFilterCampaign;
    public WebElement btnRefresh;
    public List<WebElement> rowsList;
    public WebElement btnSelectColumn;
    public WebElement pageSizeDropdown;
    public WebElement searchCampaign;
    public WebElement btnPreviousPage;
    public WebElement btnNextPage;
    public WebElement table;
    public WebElement sessionDetails;
    public WebElement sessionCampaign;
    public WebElement selectNoAssociation;
    public WebElement txtBoxSenderDisplayName;
    public WebElement txtBoxSenderAddress;
    public WebElement selectFinishAfter;
    public WebElement selectFinishAt;
    public WebElement txtBoxFinishAfterHrs;
    public WebElement txtBoxFinishAfterMins;
    public WebElement txtBoxFinishAtDate;
    public WebElement txtBoxFinishAtTime;
    public WebElement selectCheckTimeBasedFinish;
    public WebElement sortingArrowUp;
    public WebElement sortingArrowDown;
    public WebElement selectTypeColumn;
    public WebElement refreshButton;
    public WebElement finishAtDateTimeButton;
    public WebElement txtBoxGoToPage;
    public WebElement txtBoxNavigatePage;
    public WebElement clearSearchCampaign;
    public WebElement columnName;
    public WebElement operator;
    public WebElement searchText;
    public WebElement sessionContact;
    public WebElement sessionCompCodeProcessing;
    public WebElement homePage;
    public WebElement btnDeleteCampaign;
    public WebElement btnButtonDelete;
    public WebElement fromDisplayName;
    public WebElement fromAddress;
    public WebElement finishAfterHours;
    public WebElement finishAfterMinutes;
    public List<WebElement> finishAtDate;
    public WebElement editCampaign;
    public WebElement BtnFinishAfter;
    public WebElement toggleTimeBasedFinishCriteriaForPausedCampaign;
    public WebElement radioFinishAt;
    public List<WebElement> radioFinishAtDateTime;
    public WebElement btnCancelCampaign;
    public WebElement btnLeaveThisPage;
    public WebElement btnStayOnThisPage;
    public WebElement txtCampaignName;
    public WebElement ErrorMessage;
    public List<WebElement> DialingOrderTitle;
    public WebElement ThreeDots;
    public WebElement notification;

    public CampaignManagerPage(WebDriver driver) {
        super(driver);
    }

    public int getRowIndexWithName(String campName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String rowLoc = "//tr[@role='row'][@index][./td[2]//span[@data-testid='ColumnTextLink'][text()='" + campName + "']]";
        log.info("rowLoc: " + rowLoc);
        WebElement rowNumEle = presentElement(By.xpath(rowLoc), 60);
        if (rowNumEle == null)
            return -1;
        return Integer.parseInt(rowNumEle.getAttribute("index"));
    }

    public void searchCampaign(String campaignName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("campaignName: " + campaignName);
        WebElement searchElem = driver.findElement(locator.get("searchBox"));
        searchElem.sendKeys(Keys.chord(campaignName, Keys.ENTER));
        waitForPageLoadComplete(10, 1);
    }

    public void searchAndClickThreeDot(String campaignName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        clearSearchCampaign.click();
        searchCampaign(campaignName);
        clickThreeDot(campaignName);
    }

    public void clickThreeDot(String campaignName) {
        log.info("-----------------------------------------------------------------------------------------------------");
        Actions actions = new Actions(driver);
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement threeDots = driver.findElement(locator.get("firstThreeDotBtn"));
        actions.moveToElement(threeDots).pause(500).click().perform();
        waitForPageLoadComplete(30, 2);
    }

    public String getHeaderPage() {
        log.info("-------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement headerElement = presentElement(locator.get("pageHeader"), 5);
        return headerElement == null ? "" : headerElement.getText();
    }

    public String getValueOfFieldOnCampaignSummary(String field) {
        log.info("-------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Get Value of '" + field + "' field");
        return switch (field.toLowerCase()) {
            case "name" -> getTextField("name");
            case "description" -> getTextField("description");
            case "campaign strategy" -> getTextField("strategy");
            case "campaign type", "campaigntype" -> getTextField("campaignType");
            case "dialing order", "dialingorder" -> getTextField("dialingOrder");
            case "contact list" -> getTextField("contactList");
            default -> null;
        };
    }

    public boolean verifyCampaignPage() {
        log.info("-------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Campaign Manager landing page should be displayed back");
        return btnAddNewCampaign.isDisplayed();
    }

    public int getNumberIconEditOnCampaignSummaryPage() {
        log.info("-------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (presentElement(locator.get("editIcon"), 5) == null)
            return 0;
        return driver.findElements(locator.get("editIcon")).size();
    }

    public int getNumberCloseBtn() {
        log.info("-------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (presentElement(locator.get("btnClose"), 5) == null)
            return 0;
        return driver.findElements(locator.get("btnClose")).size();
    }

    public void addCampaignDetail(TestData<String, String> testData) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("TestCase Data: " + testData);
        TestData<String, String> campaign = testData.getData("campaign");
        TestData<String, String> strategy = testData.getData("strategy");
        TestData<String, String> contactList = testData.getData("contactlist");
        String campaignName = campaign.get("name");
        String description = campaign.get("description");
        String campaignType = campaign.get("type");
        String campaignFinishCriteria = campaign.get("finishType");
        log.info("Enter Campaign Name: " + campaignName);
        enterToTheField("Name", campaignName);
        log.info("Enter Description: " + description);
        enterToTheField("Description", description);
        log.info("Enter Campaign Type: " + campaignType);
        if (campaignType.equals("Finite")) {
            log.info("Finite is a default option");
        } else {
            log.info("Select type Infinite campaign");
            clickAnElementUsingJavaScript(findElementByDataID("INFINITE"));
        }
        log.info("Enter Sender Display Name: " + campaign.get("senderDisplayName"));
        enterToTheField("Sender's Display Name", campaign.get("senderDisplayName"));
        log.info("Enter Senders Address: " + campaign.get("senderAddress"));
        enterToTheField("Sender's Address", campaign.get("senderAddress"));
        log.info("Select Time Based Finish Criteria: " + campaignFinishCriteria);
        if (campaignType.equals("Finite")) {
            if (campaignFinishCriteria.equalsIgnoreCase("Finish At")) {
                clickAnElementUsingJavaScript(findElementByDataID("FINISH_AT"));
                waitForPageLoadComplete(10, 0.5);
                log.info("Select Finish Campaign At Date/Time: " + campaign.get("finishAtDate") + "/" + campaign.get("finishAtTime"));
                findElementByDataID("finishAtDate").clear();
                sendKeysToTextBox("finishAtDate", campaign.get("finishAtDate"));
                clickButton("campaign");
                findElementByDataID("finishAtTime").clear();
                sendKeysToTextBox("finishAtTime", campaign.get("finishAtTime"));
            } else {
                clickAnElementUsingJavaScript(findElementByDataID("FINISH_AFTER"));
                waitForPageLoadComplete(10, 0.5);
                log.info("Select Finish Campaign After Hours/Minutes: " + campaign.get("finishAfterHrs") + "/" + campaign.get("finishAfterMins"));
                findElementByDataID("finishAfterHrs").clear();
                sendKeysToTextBox("finishAfterHrs", campaign.get("finishAfterHrs"));
                findElementByDataID("finishAfterMins").clear();
                sendKeysToTextBox("finishAfterMins", campaign.get("finishAfterMins"));
            }
        }
        enterToTheField("Contact List", contactList.get("name"));
        enterToTheField("Select Campaign Strategy", strategy.get("name"));
    }

    public String getMessageNoRecord() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement message = driver.findElement(locator.get("msg_NoRecord"));
        return message.getText();
    }

    /**
     * method get value of column on campaign landing page
     *
     * @param nameColumn: header of column on campaign landing page
     * @return
     */
    public String getValueOfColumn(String nameColumn) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        int index = getColumnIndex(nameColumn);
        String xpath = "//table[@data-testid='table']/tbody/tr[1]/td[" + index + "]";
        WebElement valueOfCol = driver.findElement(By.xpath(xpath));
        return valueOfCol.getText();
    }

    /**
     * Method get value size of record on campaign landing page
     *
     * @return int numberRecordActual
     */
    public int getValueOfPageDetails() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement valuePageDetails = waitForElementVisible(locator.get("pageDetails"));
        String getValue = valuePageDetails.getText().replaceAll("\\d+-(\\d+)", "$1");
        return Integer.parseInt(getValue);
    }

    public int checkPageDisplay() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<WebElement> numberPage = driver.findElements(locator.get("txtBoxNavigatePage"));
        return numberPage.size();
    }

    /**
     * @param nameCampaign: name campaign that use want verify check box display
     * @return true if check box displayed
     */
    public boolean verifyCheckBoxSelectEachCamp(String nameCampaign) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        searchCampaign.sendKeys(nameCampaign);
        utilityFun.wait(2);
        WebElement btnSelect = driver.findElement(By.id("checkbox_0"));
        return btnSelect.isDisplayed();
    }

    public boolean isCampaignExist(String searchName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Search campaign on UI: " + searchName);
        findElementByDataID("searchTable").clear();
        sendKeysToTextBox("searchTable", searchName);
        String found = (String.format(locator.getLocator("linkTextCampaign"), searchName));
        return isDisplayedByXpath(found, 3);
    }

    public void isTextDisplayed(String searchString) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String errMsg = locator.locatorFormat("message", searchString);
        Assert.assertNotNull("ERR!!!. Message is not displayed", presentElement(By.xpath(errMsg), 3));
    }

    public boolean verifyActiveTab(String tabName, String fieldName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Check on active tab: " + tabName + " should include field/field description: " + fieldName);
        String xpathField = locator.getLocator("fieldName").replaceAll("%s", fieldName);
        boolean found = false;
        try {
            switch (tabName) {
                case "Details": {
                    found = findElementByXpath(xpathField).isDisplayed();
                    break;
                }
                case "Campaign": {
                    found = findElementByXpath(xpathField).isDisplayed();
                    break;
                }
                case "Contacts": {
                    found = findElementByXpath(xpathField).isDisplayed();
                    break;
                }
                case "Completion Processing": {
                    found = findElementByXpath(xpathField).isDisplayed();
                    break;
                }
            }
        } catch (Exception e) {
            log.info("ERR!!! .Cannot found field: " + fieldName + " on tab: " + tabName);
            log.info(e.getMessage());
            Assert.fail();
        }
        if (found) {
            log.info("Found Hierachy tab: " + tabName + " -> " + fieldName);
        } else {
            log.info("ERR!!! .Cannot found field: " + fieldName + " on tab: " + tabName);
            Assert.fail();
        }
        return found;
    }

    /**
     * method using get value of page size on campaign landing page
     *
     * @return
     */
    public int getValueDefaultPageSizeOnCampaignLanding() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement PageDefault = driver.findElement(locator.get("pageSizeDropdown_option1"));
        return Integer.parseInt(PageDefault.getText());
    }

    /**
     * Method using to verify number page showing correctly
     *
     * @param numberPageDisplay: number Page showing on campaign landing
     * @return
     */
    public boolean checkPageDisplayOnCampaignLanding(String numberPageDisplay) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        scrollIntoViewAndSendKeys(txtBoxGoToPage, numberPageDisplay);
        waitForPageLoadComplete(30, 2);
        String xpathPage = "//button[@id='" + numberPageDisplay + "']";
        WebElement pageDisplayed = driver.findElement(By.xpath(xpathPage));
        String pageDisplay = pageDisplayed.getAttribute("class");
        return pageDisplay.contains("secondary");
    }

    /**
     * method using verify record display correctly with number page size
     *
     * @param numberPageSize: number page size that user want to record display on campaign landing.
     */
    public void verifyRecordDisplayCorrectlyWithNumberPage(String numberPageSize) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        selectDropDownOptionEqual(pageSizeDropdown, numberPageSize);
        utilityFun.wait(3);
        int getNumberRecord = numberRecordOnCurrentPage();
        Assert.assertTrue(getNumberRecord <= Integer.parseInt(numberPageSize));
        Assert.assertEquals(this.getValueOfPageDetails(), getNumberRecord);
        log.info("Number Record display correctly with number page is " + numberPageSize);
        Assert.assertFalse("landing page is not return page default",isEnable("previousPage"));
        log.info("Campaign landing page take user to first page  page size is " + numberPageSize);
    }

    /**
     * method using verify number record display correctly when change page size
     *
     * @return
     */
    public boolean verifyRecordDisplayCorrectly(List<String> listPageSize) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean result = false;
        try {
            for (String pageSize : listPageSize) {
                log.info("Verify number record display correctly when change the page size is " + pageSize);
                verifyRecordDisplayCorrectlyWithNumberPage(pageSize);
                utilityFun.wait(1);
            }
            result = true;
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * Method using verify Search basic work correctly
     *
     * @param campaignNameExpected: name campaign that user want search
     * @return
     */
    public boolean verifyQuickSearchWorkCorrect(String campaignNameExpected) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("campaignNameExpected: " + campaignNameExpected);
        try {
            log.info("Get list of current campaign name");
            List<String> nameList = this.getListColumnDataOnCurPage("Name");
            log.info(nameList);
            if (nameList.size() < 2)
                return false;
            for (String name : nameList) {
                if (!name.contains(campaignNameExpected)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.info("-----------------------------End Method-------------------------------------");
        }
        return true;
    }

    /**
     * method using verify result search update when user update the campaign name on search box.
     *
     * @param characterUpdateSearchBox: character that user want update on Search box
     * @return
     */
    public boolean updateCharacterOnSearchTextBox(String characterUpdateSearchBox) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean result = false;
        try {
            log.info("Get value of search campaign before Update new character");
            int numberRecordBeforeUpdate = this.numberRecordOnCurrentPage();
            log.info("Update new character on search box");
            findElementByDataID("searchTable").sendKeys(characterUpdateSearchBox);
            utilityFun.wait(3);
            int numberRecordAfterUpdate = this.numberRecordOnCurrentPage();
            Assert.assertTrue("List number Record haven't update when input new character", numberRecordBeforeUpdate != numberRecordAfterUpdate);
            log.info("List result update campaign landing page");
            log.info("Click clear character on search box");
            findElementByDataID("clearSearch").click();
            numberRecordAfterUpdate = this.numberRecordOnCurrentPage();
            Assert.assertTrue("List number Record haven't update when clear character", numberRecordBeforeUpdate <= numberRecordAfterUpdate);
            log.info("Result search updated when update character");
            result = true;
        } catch (Exception e) {
            log.info("Result search can not update when update character");
        }
        return result;
    }

    /**
     * method using to select menu on any campaign
     *
     * @param indexRow:  Row that user want click three dots
     * @param xpathMenu: locater of menu that user want click
     */
    public void clickMenuOnThreeDotOfCampaign(int indexRow, String xpathMenu) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Actions actions = new Actions(driver);
        String xpathThreeDots = "//button[@id='dropdown_" + indexRow + "']";
        log.info(xpathThreeDots);
        WebElement threeDots = driver.findElement(By.xpath(xpathThreeDots));
        log.info("Click Three Dots of Campaign");
        actions.moveToElement(threeDots).pause(300).click().perform();
        utilityFun.wait(2);
        log.info("Click Menu on Dropdown of Three Dots");
        driver.findElement(By.xpath(xpathMenu)).click();
        waitForPageLoadComplete(30, 2);
    }

    public boolean verifyUserCanClickEditCampaign() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Click Edit any campaign on list result search");
        Random generator = new Random();
        int records = this.numberRecordOnCurrentPage();
        int indexCampaignWantEdit = generator.nextInt(records - 2) + 1;
        clickMenuOnThreeDotOfCampaign(indexCampaignWantEdit, locator.getLocator("action_editCampaign"));
        boolean result = driver.findElement(locator.get("scrollableTab")).isDisplayed();
        btnCancelCampaign.click();
        waitForPageLoadComplete(30, 2);
        return result;
    }

    /**
     * method using get name campaign first row on campaign landing page
     *
     * @return
     */
    public String getNameCampaignFirstRow() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return getListColumnDataOnCurPage("Name").get(0);
    }

    /**
     * method using to get name campaign that user click edit
     *
     * @param descriptionEdit: information description that user will edit on edit campaign page.
     * @return
     */
    public String getNameCampaignJustEdit(String descriptionEdit) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Edit any campaign on list");
        Random generator = new Random();
        int indexCampaignWantEdit = generator.nextInt(this.numberRecordOnCurrentPage() - 2) + 1;
        clickMenuOnThreeDotOfCampaign(indexCampaignWantEdit, locator.getLocator("action_editCampaign"));
        String nameCampaignEdit = waitForElementVisible(locator.getLocator("txtBoxCampaignName")).getAttribute("value");
        this.txtBoxDescription.sendKeys(descriptionEdit);
        utilityFun.wait(1);
        this.btnSaveCampaign.click();
        utilityFun.wait(2);
        return nameCampaignEdit;
    }

    /**
     * method using to verify sort work properly with column when user click header
     *
     * @param nameColumnSort: name column that user want sort
     * @return
     */
    public boolean verifySortWorkProperly(String nameColumnSort) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        selectDropDownOptionEqual(this.pageSizeDropdown, "100");
        utilityFun.wait(3);
        try {
            List<String> listDefault = this.getListColumnDataOnCurPage(nameColumnSort);
            Collections.sort(listDefault);
            log.info("Verify sort Ascending should work properly");
            WebElement nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + nameColumnSort + "']"));
            nameColumn.click();
            utilityFun.wait(2);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowUp.isDisplayed());
            List<String> listAfterSortAcs = this.getListColumnDataOnCurPage(nameColumnSort);
            Assert.assertEquals("Sort work is not properly", listAfterSortAcs, listDefault);

            log.info("Verify sort Descending  should work properly");
            nameColumn.click();
            utilityFun.wait(2);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowDown.isDisplayed());
            List<String> listAfterSortDes = this.getListColumnDataOnCurPage(nameColumnSort);
            listDefault.sort(Comparator.reverseOrder());
            Assert.assertEquals("Sort work is not properly", listAfterSortDes, listDefault);

            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Sort work not properly, Test is Failed");
        }
        return false;
    }

    /**
     * method using to verify sort icon display correctly when user click name column
     *
     * @param columnName: name column that user click on campaign landing page
     * @return
     */
    public boolean verifySortIconDisplayInColumn(String columnName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("Verify Sort icon should display Ascending when first click on name column");
            WebElement nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + columnName + "']"));
            nameColumn.click();
            utilityFun.wait(2);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowUp.isDisplayed());
            log.info("Verify Sort icon should display Descending when second click on name column");
            nameColumn.click();
            utilityFun.wait(2);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowDown.isDisplayed());
            nameColumn.click();
            utilityFun.wait(2);
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

    /**
     * method using to filter Campaign
     */
    public boolean filterCampaign(String columnName, String operation, String searchText) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Filter Campaign----------------------------");
        log.info("columnName: " + columnName);
        log.info("operation: " + columnName);
        log.info("searchText: " + searchText);
        selectDropDownOption(this.pageSizeDropdown, "100");
        waitForPageLoadComplete(30, 1);
        if (!btnFilterCampaign.getAttribute("class").contains("neo-btn-square neo-icon-filter-filled")) {
            this.btnFilterCampaign.click();
            utilityFun.wait(1);
        }
        log.info("Select column name: " + columnName);
        selectDropDownOption(this.columnName, columnName);
        log.info("Select operation: " + operation);
        selectDropDownOption(this.operator, operation);
        this.searchText.sendKeys(searchText);
        waitForPageLoadComplete(30, 3);
        List<String> nameList = this.getListColumnDataOnCurPage("Name");
        log.info("nameList: " + nameList);
        switch (operation.toLowerCase()) {
            case "=":
                if (nameList.size() != 1) {
                    log.info("Number of campaign not equal to 1");
                    return false;
                }
                return nameList.get(0).contentEquals(searchText);
            case "!=":
                for (String name : nameList) {
                    if (name.contentEquals(searchText)) {
                        return false;
                    }
                }
                return true;
            case "in":
                List<String> results = Arrays.stream(searchText.split(",")).map(String::trim).collect(Collectors.toList());
                for (String name : nameList) {
                    if (!results.contains(name)) {
                        return false;
                    }
                }
                return true;
            case "like":
                for (String name : nameList) {
                    if (!name.contains(searchText)) {
                        return false;
                    }
                }
                return true;
            case "not like":
                for (String name : nameList) {
                    if (name.contains(searchText)) {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }

    /**
     * method using Check list campaign after filter
     */
    public void checkListCampaign(String listCampaign) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Check list Campaign----------------------------");
        String[] nameCampaign = listCampaign.split("\\|");
        List<String> campaignList = new ArrayList<>();
        selectDropDownOption(this.pageSizeDropdown, "100");
        utilityFun.wait(1);
        while (true) {
            campaignList.addAll(this.getListColumnDataOnCurPage("Name"));
            if (!this.btnNextPage.isEnabled()) {
                break;
            } else {
                this.btnNextPage.click();
            }
        }
        if (campaignList.size() == nameCampaign.length) {
            for (String test : nameCampaign) {
                Assert.assertTrue(campaignList.contains(test));
            }
        } else {
            Assert.fail("Fail list Campaign not match");
        }
    }

    public void campaignView() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Check Campaign can view after filter----------------------------");
        WebElement Details = driver.findElement(locator.get("campaignDetailTab"));
        Assert.assertTrue("can not view campaign detail", Details.getText().contains("Details"));
        utilityFun.wait(1);
        WebElement Campaign = driver.findElement(locator.get("campaignTab"));
        Assert.assertTrue("can not view campaign ", Campaign.getText().contains("Campaign"));
        utilityFun.wait(1);
        WebElement contacts = driver.findElement(locator.get("recordTab"));
        Assert.assertTrue("can not view contact", contacts.getText().contains("Contacts"));
        utilityFun.wait(1);
        WebElement CompletionCodes = driver.findElement(locator.get("associationTab"));
        Assert.assertTrue("can not view completion code", CompletionCodes.getText().contains("Completion Codes"));
        utilityFun.wait(1);
        WebElement CompletionProcessing = driver.findElement(locator.get("completionprocessingTab"));
        Assert.assertTrue("can not view completion processing", CompletionProcessing.getText().contains("Completion Processing"));
        utilityFun.wait(1);
        WebElement media = driver.findElement(locator.get("mediaTab"));
        Assert.assertTrue("can not view media", media.getText().contains("Media"));
        utilityFun.wait(1);
        WebElement AdditionalParameter = driver.findElement(locator.get("additionalparameterTab"));
        Assert.assertTrue("can not view Additional Parameter", AdditionalParameter.getText().contains("Additional Parameter"));
    }


    public void editDesCamp(String description) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Check Campaign can edit ( description) after filter----------------------------");
        tryClick("action_editCampaign", 10);
        waitForPageLoadComplete(30,2);
        findElementByDataID("description").clear();
        sendKeysToTextBox("description", description);
        tryClick("save",10);
        utilityFun.wait(2);
    }


    public void deleteFirstCamp() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Check Campaign can delete after filter----------------------------");
        Actions builder = new Actions(driver);
        builder.moveToElement(this.ThreeDots).perform();
        this.ThreeDots.click();
        utilityFun.wait(5);
        log.info("Click Delete campaign on DropDown");
        this.btnDeleteCampaign.click();
        utilityFun.wait(3);
        log.info("Deleting");
        this.btnButtonDelete.click();
    }

    public void verifyCampaign(Map<String, String> testData) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Search Campaign----------------------------");
        sendKeysToTextBox("searchTable", testData.get("name"));
        clickAction(testData.get("name"), "Edit");
        waitForPageLoadComplete(30,3);
        if (testData.containsKey("EditDescription")) {
            log.info("Check value description");
            String valueDescription = findElementByDataID("description").getText();
            log.info("Description of Update : " + valueDescription);
            Assert.assertTrue(valueDescription.contentEquals(testData.get("EditDescription")));
        }
        if (testData.containsKey("Edit Sender's Display Name")) {
            WebElement test = driver.findElement(By.xpath("//*[@id='tab-basicparameter']"));
            test.click();
            log.info("Check value sender's display name");
            log.info(this.fromDisplayName.getAttribute("value"));
            log.info(testData.get("Edit Sender's Display Name"));
            String valueDisplayName = this.fromDisplayName.getAttribute("value");
            Assert.assertTrue(valueDisplayName.contentEquals(testData.get("Edit Sender's Display Name")));
        }
        if (testData.containsKey("Edit Sender's Address")) {
            WebElement test = driver.findElement(By.xpath("//*[@id='tab-basicparameter']"));
            test.click();
            log.info("Check value sender's address");
            log.info(this.fromAddress.getAttribute("value"));
            log.info(testData.get("Edit Sender's Address"));
            String valueAddress = this.fromAddress.getAttribute("value");
            Assert.assertTrue(valueAddress.contentEquals(testData.get("Edit Sender's Address")));
        }
        if (testData.containsKey("Edit Dialing Order")) {
            log.info("Check value dialing order");
            ArrayList<String> expectedOrder = new ArrayList<>();
            Collections.addAll(expectedOrder, testData.get("Edit Dialing Order").split("\\|"));
            for (int i = 0; i <= 2; i++) {
                utilityFun.wait(3);
                List<WebElement> array = this.DialingOrderTitle;
                ArrayList<String> actualOrder = new ArrayList<>();
                for (WebElement element : this.DialingOrderTitle)
                    actualOrder.add(element.getAttribute("innerText"));
                if (i != actualOrder.indexOf(expectedOrder.get(i))) {
                    Assert.fail();
                }
            }
        }
        if (testData.containsKey("finishTimeAfter")) {
            log.info("Check value Time Based Finish After");
            String[] TimeBasedFinishCriteria = testData.get("finishTimeAfter").split("\\|");
            Assert.assertEquals(TimeBasedFinishCriteria[0], this.finishAfterHours.getAttribute("value"));
            Assert.assertEquals(TimeBasedFinishCriteria[1], this.finishAfterMinutes.getAttribute("value"));
        }
        if (testData.containsKey("finishTimeAt")) {
            log.info("Check value Time Based Finish Criteria");
            String[] TimeBasedFinishCriteria = testData.get("finishTimeAt").split("\\|");
            int i = 0;
            for (WebElement ValueTime : this.finishAtDate) {
                Assert.assertEquals(TimeBasedFinishCriteria[i], ValueTime.getAttribute("value"));
                i++;
            }
        }
        WebElement cancelBtnEle = presentElement(locator.get("btnCancelCampaign"), 5);
        if (cancelBtnEle != null) {
            cancelBtnEle.click();
        }
        WebElement leaveBtnEle = presentElement(locator.get("leaveThisPage"), 5);
        if (leaveBtnEle != null) {
            leaveBtnEle.click();
        }
    }

    public void clickToCampaign(String campaignName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        waitForElementClickable(By.xpath(locator.locatorFormat("campaignNameFormat", campaignName))).click();
        waitForPageLoadComplete(30, 1);
        presentElement(By.xpath(locator.getLocator("campaignPageVerify")), 30);
    }

    public void updateCampaign(String action, String cpName) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (action.toLowerCase().contains("clicks a campaign name link")) {
            log.info("--------------------------Search Campaign----------------------------");
            findElementByXpath(locator.getLocator("searchCampaign")).sendKeys(cpName);
            waitForPageLoadComplete(10,5);
            log.info("Click  campaign link");
            WebElement CampaignName = findElementByXpath("//*[text()='" + cpName + "']");
            CampaignName.click();
            waitForPageLoadComplete(10,2);
        }
        if (action.toLowerCase().contains("clicks the triple dots then selects edit menu")) {
            log.info("--------------------------Search Campaign----------------------------");
            findElementByDataID("searchTable").sendKeys(cpName);
            waitForPageLoadComplete(10,3);
            Actions builder = new Actions(driver);
            builder.moveToElement(this.ThreeDots).perform();
            clickButton("more-actions");
            waitForPageLoadComplete(10,2);
            log.info("Click Edit campaign on DropDown");
            clickButton("action_editCampaign");
            waitForPageLoadComplete(10,2);
        }
    }

    public void editCampaignValue(Map<String, String> testData) {
        for (String editValue : testData.keySet()) {
            log.info(editValue);
            switch (editValue) {
                case ("description"):
                    findElementByXpath(locator.getLocator("txtBoxDescription")).clear();
                    waitForPageLoadComplete(10,2);
                    findElementByXpath(locator.getLocator("txtBoxDescription")).sendKeys(Keys.CONTROL + "a");
                    waitForPageLoadComplete(10,2);
                    findElementByXpath(locator.getLocator("txtBoxDescription")).sendKeys(Keys.DELETE);
                    waitForPageLoadComplete(10,2);
                    log.info(testData.get(editValue));
                    findElementByXpath(locator.getLocator("txtBoxDescription")).sendKeys(testData.get(editValue));
                    break;

                case ("senderDisplayName"):
                    findElementByXpath(locator.getLocator("txtBoxSenderDisplayName")).clear();
                    waitForPageLoadComplete(10,2);
                    log.info(testData.get(editValue));
                    findElementByXpath(locator.getLocator("txtBoxSenderDisplayName")).sendKeys(testData.get(editValue));
                    break;
                case ("senderAddress"):
                    findElementByXpath(locator.getLocator("txtBoxSenderAddress")).clear();
                    waitForPageLoadComplete(10,2);
                    log.info(testData.get(editValue));
                    findElementByXpath(locator.getLocator("txtBoxSenderAddress")).sendKeys(testData.get(editValue));
                    break;
                case ("dialingOrder"):
                    ArrayList<String> expectedOrder = new ArrayList<String>();
                    Collections.addAll(expectedOrder, testData.get("dialingOrder").split("\\|"));
                    for (int i = 0; i <= 2; i++) {
                        waitForPageLoadComplete(10,3);
                        List<WebElement> array =  findElementsByXpath(locator.getLocator("DialingOrderTitle"));
                        ArrayList<String> actualOrder = new ArrayList<String>();
                        for (WebElement element : array)
                        {
                            if (!element.getText().contentEquals("Dialing Order")) {
                                actualOrder.add(element.getText().trim());
                            }
                        }
                        if (i == actualOrder.indexOf(expectedOrder.get(i))) {
                            continue;
                        } else {
                            Actions builder = new Actions(driver);
                            Action dragAndDrop = builder.moveToElement(array.get(i)).release(array.get(i)).build();
                            dragAndDrop.perform();
                            WebElement element = findElementByXpath("//span[text()='%s']/..".formatted(                                    actualOrder.get(actualOrder.indexOf(expectedOrder.get(i)))));
                            JavascriptExecutor _js = (JavascriptExecutor) driver;
                            _js.executeScript("function createEvent(typeOfEvent) {\n"
                                    + "var event =document.createEvent(\"CustomEvent\");\n"
                                    + "event.initCustomEvent(typeOfEvent,true, true, null);\n"
                                    + "event.dataTransfer = {\n" + "data: {},\n"
                                    + "setData: function (key, value) {\n" + "this.data[key] = value;\n" + "},\n"
                                    + "getData: function (key) {\n" + "return this.data[key];\n" + "}\n" + "};\n"
                                    + "return event;\n" + "}\n" + "\n"
                                    + "function dispatchEvent(element, event,transferData) {\n"
                                    + "if (transferData !== undefined) {\n" + "event.dataTransfer = transferData;\n"
                                    + "}\n" + "if (element.dispatchEvent) {\n" + "element.dispatchEvent(event);\n"
                                    + "} else if (element.fireEvent) {\n"
                                    + "element.fireEvent(\"on\" + event.type, event);\n" + "}\n" + "}\n" + "\n"
                                    + "function simulateHTML5DragAndDrop(element, destination) {\n"
                                    + "var dragStartEvent =createEvent('dragstart');\n"
                                    + "dispatchEvent(element, dragStartEvent);\n"
                                    + "var dropEvent = createEvent('drop');\n"
                                    + "dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);\n"
                                    + "var dragEndEvent = createEvent('dragend');\n"
                                    + "dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);\n" + "}\n" + "\n"
                                    + "var source = arguments[0];\n" + "var destination = arguments[1];\n"
                                    + "simulateHTML5DragAndDrop(source,destination);", element, array.get(i));
                        }
                    }
                    break;
                case ("finishTimeAfter"):
                    log.info("Set time based Finish Criteria ->" + testData.get("finishTimeAfter"));
                    if (!testData.get("finishTimeAfter").isEmpty()) {
                        String[] TimeBasedFinishCriteria = testData.get("finishTimeAfter").split("\\|");
                        findElementByXpath(locator.getLocator("BtnFinishAfter")).click();
                        findElementByXpath(locator.getLocator("finishAfterHours")).clear();
                        findElementByXpath(locator.getLocator("finishAfterHours")).sendKeys(TimeBasedFinishCriteria[0]);
                        findElementByXpath(locator.getLocator("finishAfterMinutes")).clear();
                        findElementByXpath(locator.getLocator("finishAfterMinutes")).sendKeys(TimeBasedFinishCriteria[1]);
                        if (testData.containsKey("CheckTimeBasedFinishCriteriaForPausedCampaign")
                                && !testData.get("CheckTimeBasedFinishCriteriaForPausedCampaign").isEmpty()) {
                            if (testData.get("CheckTimeBasedFinishCriteriaForPausedCampaign")
                                    .equalsIgnoreCase("yes"))
                                findElementByXpath(locator.getLocator("toggleTimeBasedFinishCriteriaForPausedCampaign")).click();
                        }
                    }
                    break;
                case ("finishTimeAt"):
                    log.info("Set time based Finish Criteria ->" + testData.get("finishTimeAt"));
                    if (!testData.get("finishTimeAt").isEmpty()) {
                        String[] TimeBasedFinishCriteria = testData.get("finishTimeAt").split("\\|");
                        findElementByXpath(locator.getLocator("radioFinishAt")).click();
                        int i = 0;
                        List<WebElement> listE = findElementsByXpath(locator.getLocator("radioFinishAtDateTime"));
                        for (WebElement SetTime : listE) {
                            SetTime.clear();
                            SetTime.sendKeys(TimeBasedFinishCriteria[i]);
                            i++;
                        }
                        findElementByDataID("tab-details").click();
                        if (testData.containsKey("CheckTimeBasedFinishCriteriaForPausedCampaign")
                                && !testData.get("CheckTimeBasedFinishCriteriaForPausedCampaign").isEmpty()) {
                            if (testData.get("CheckTimeBasedFinishCriteriaForPausedCampaign").isEmpty()) {
                                if (testData.get("CheckTimeBasedFinishCriteriaForPausedCampaign")
                                        .equalsIgnoreCase("yes"))
                                    findElementByXpath(locator.getLocator("toggleTimeBasedFinishCriteriaForPausedCampaign")).click();
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void SaveOrCancelCampaignEdit(String action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Click " + action.toLowerCase());
        if (action.toLowerCase().contains("save")) {
            tryClick("save",5);
        }
        if (action.toLowerCase().contains("cancel")) {
            tryClick("cancel",5);
        }
        waitForPageLoadComplete(10,1);
    }

    public void checkValueSave(Map<String, String> testData) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (testData.containsKey("description")) {
            log.info("Check value description");
            log.info(findElementByXpath(locator.getLocator("txtBoxDescription")).getText());
            log.info(testData.get("description"));
            String valueDescription = findElementByXpath(locator.getLocator("txtBoxDescription")).getText();
            Assert.assertTrue(valueDescription.contentEquals(testData.get("description")));
        }
        if (testData.containsKey("senderDisplayName")) {
            log.info("Check value sender's display name");
            log.info(findElementByXpath(locator.getLocator("txtBoxSenderDisplayName")).getAttribute("value"));
            log.info(testData.get("senderDisplayName"));
            String valueDisplayName = findElementByXpath(locator.getLocator("txtBoxSenderDisplayName")).getAttribute("value");
            Assert.assertTrue(valueDisplayName.contentEquals(testData.get("senderDisplayName")));
        }
        if (testData.containsKey("senderAddress")) {
            log.info("Check value sender's address");
            log.info(findElementByXpath(locator.getLocator("txtBoxSenderAddress")).getAttribute("value"));
            log.info(testData.get("senderAddress"));
            String valueAddress = findElementByXpath(locator.getLocator("txtBoxSenderAddress")).getAttribute("value");
            Assert.assertTrue(valueAddress.contentEquals(testData.get("senderAddress")));
        }
        if (testData.containsKey("dialingOrder")) {
            log.info("Check value dialing order");
            ArrayList<String> expectedOrder = new ArrayList<String>();
            Collections.addAll(expectedOrder, testData.get("dialingOrder").split("\\|"));
            for (int i = 0; i <= 2; i++) {
                utilityFun.wait(3);
                List<WebElement> arrayE = findElementsByXpath(locator.getLocator("DialingOrderTitle"));
                ArrayList<String> actualOrder = new ArrayList<String>();
                for (WebElement element : arrayE){
                    if (!element.getText().contentEquals("Dialing Order")) {
                        actualOrder.add(element.getText().trim());
                    }
                }
                log.info(actualOrder.get(i).toString());
                log.info(expectedOrder.get(i).toString());
                if (i != actualOrder.indexOf(expectedOrder.get(i))) {
                    Assert.fail();
                }
            }
        }
        if (testData.containsKey("finishTimeAfter")) {
            log.info("Check value Time Based Finish After");
            String[] TimeBasedFinishCriteria = testData.get("finishTimeAfter").split("\\|");
            Assert.assertEquals(TimeBasedFinishCriteria[0], findElementByXpath(locator.getLocator("finishAfterHours")).getAttribute("value"));
            Assert.assertEquals(TimeBasedFinishCriteria[1], findElementByXpath(locator.getLocator("finishAfterMinutes")).getAttribute("value"));
        }
        if (testData.containsKey("finishTimeAt")) {
            log.info("Check value Time Based Finish Criteria");
            String[] TimeBasedFinishCriteria = testData.get("finishTimeAt").split("\\|");
            int i = 0;
            List<WebElement> listE = findElementsByXpath(locator.getLocator("radioFinishAtDateTime"));
            for (WebElement ValueTime : listE) {
                Assert.assertEquals(TimeBasedFinishCriteria[i], ValueTime.getAttribute("value"));
                i++;
            }
        }
    }

    public void CheckCharacterLength(String value) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (value.toLowerCase()) {
            case "description": {
                Assert.assertEquals("The new variable description has a length greater than 128 characters ","128", String.valueOf(findElementByDataID("description").getText().length()));
                break;
            }
            case "sender's display name": {
                Assert.assertEquals("The new variable sender's display name has a length greater than 11 characters ","11", String.valueOf(findElementByDataID("senderDisplayName").getAttribute("value").length()));
                break;
            }
            case "sender's address": {
                Assert.assertEquals("The new variable sender's address has a length greater than 255 characters ", String.valueOf(findElementByXpath(locator.getLocator("txtBoxSenderAddress")).getAttribute("value").length()), "255");
                break;
            }
        }
    }

    public void ValueSpecialCharacters(String value, String Error, Map<String, String> testData) {
        String specialCharacterListForDescription = "^~`!;$#\\<>&\"";
        String specialCharacterListForSenderDisplay = "`'<>\"";
        switch (value.toLowerCase()) {
            case ("description"):
                log.info("Edit description value contain special characters");
                for (String specialCharacter : specialCharacterListForDescription.split("")) {
                    this.txtBoxDescription.clear();
                    utilityFun.wait(2);
                    this.txtBoxDescription.sendKeys(specialCharacter);
                    utilityFun.wait(1);
                    this.btnSaveCampaign.click();
                    utilityFun.wait(2);
                    String notificationMessage = this.notification.getText();
                    log.info("Error message: " + notificationMessage);
                    Assert.assertTrue("description can contain special characters (" + specialCharacter + ") fail", notificationMessage.contains(Error));
                }
                utilityFun.wait(2);
                this.txtBoxDescription.clear();
                break;

            case ("sender's display name"):
                log.info("Edit sender's display name value contain special characters");
                for (String specialCharacter : specialCharacterListForSenderDisplay.split("")) {
                    this.txtBoxSenderDisplayName.clear();
                    utilityFun.wait(2);
                    this.txtBoxSenderDisplayName.sendKeys(specialCharacter);
                    utilityFun.wait(1);
                    this.btnSaveCampaign.click();
                    utilityFun.wait(1);
                    String notificationMessage = this.notification.getText();
                    log.info("Error message " + notificationMessage);
                    Assert.assertTrue("sender's display name can contain  special characters (" + specialCharacter + ") fail", notificationMessage.contains(Error));
                }
                this.txtBoxSenderDisplayName.clear();
                break;
            case ("sender's address"):
                log.info("Edit sender's display name value can contain special characters");
                for (String specialCharacter : specialCharacterListForSenderDisplay.split("")) {
                    this.txtBoxSenderAddress.clear();
                    utilityFun.wait(2);
                    this.txtBoxSenderAddress.sendKeys(testData.get(specialCharacter));
                    utilityFun.wait(1);
                    this.btnSaveCampaign.click();
                    utilityFun.wait(1);
                    String notificationMessage = this.notification.getText();
                    log.info("Error message " + notificationMessage);
                    Assert.assertTrue("sender's address contain  special characters (" + specialCharacter + ") fail", notificationMessage.contains(Error));
                }
                this.txtBoxSenderAddress.clear();
                break;
        }
    }

    public void leaveThisPageOrStayThisPage(String Action) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (Action.toLowerCase().contains("leaver this page")) {
            log.info(findElementByDataID("positive-action").getText());
            clickButton("positive-action");
        }
        if (Action.toLowerCase().contains("stay on this page")) {
            log.info(findElementByDataID("negative-action").getText());
            clickButton("negative-action");
        }
    }

    public void CheckValueAfterCancel(Map<String, String> testData) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Check Value after cancel stay on this page----------------------------");
        if (testData.containsKey("description")) {
            log.info("Check value description");
            log.info( findElementByXpath(locator.getLocator("txtBoxDescription")).getText());
            log.info(testData.get("description"));
            String valueDescription =  findElementByXpath(locator.getLocator("txtBoxDescription")).getText();
            Assert.assertTrue(valueDescription.contentEquals(testData.get("description")));
        }
        if (testData.containsKey("Edit Sender's Display Name")) {
            log.info("Check value sender's display name");
            log.info( findElementByXpath(locator.getLocator("txtBoxSenderDisplayName")).getAttribute("value"));
            log.info(testData.get("senderDisplayName"));
            String valueDisplayName =  findElementByXpath(locator.getLocator("txtBoxSenderDisplayName")).getAttribute("value");
            Assert.assertTrue(valueDisplayName.contentEquals(testData.get("senderDisplayName")));
        }
        if (testData.containsKey("Edit Sender's Address")) {
            log.info("Check value sender's address");
            log.info( findElementByXpath(locator.getLocator("txtBoxSenderAddress")).getAttribute("value"));
            log.info(testData.get("senderAddress"));
            String valueAddress =  findElementByXpath(locator.getLocator("txtBoxSenderAddress")).getAttribute("value");
            Assert.assertTrue(valueAddress.contentEquals(testData.get("senderAddress")));
        }
        if (testData.containsKey("Edit Dialing Order")) {
            log.info("Check value dialing order");
            ArrayList<String> expectedOrder = new ArrayList<String>();
            Collections.addAll(expectedOrder, testData.get("dialingOrder").split("\\|"));
            for (int i = 0; i <= 2; i++) {
                utilityFun.wait(3);
                List<WebElement> array =  findElementsByXpath(locator.getLocator("DialingOrderTitle"));
                ArrayList<String> actualOrder = new ArrayList<String>();
                for (WebElement element : array)
                    actualOrder.add(element.getAttribute("innerText"));
                if (i != actualOrder.indexOf(expectedOrder.get(i))) {
                    Assert.fail();
                }
            }
        }
        if (testData.containsKey("Time Based Finish Criteria")) {
            log.info("Check value Time Based Finish Criteria");
            String[] TimeBasedFinishCriteria = testData.get("finishTime").split("\\|");
            Assert.assertEquals(TimeBasedFinishCriteria[0], findElementByXpath(locator.getLocator("finishAfterHours")).getText());
            Assert.assertEquals(TimeBasedFinishCriteria[1], findElementByXpath(locator.getLocator("finishAfterMinutes")).getText());
        }
        if (testData.containsKey("finishTimeAt")) {
            log.info("Check value Time Based Finish Criteria");
            String[] TimeBasedFinishCriteria = testData.get("finishTimeAt").split("\\|");
            int i = 0;
            List<WebElement> ListE = findElementsByXpath(locator.getLocator("radioFinishAtDateTime"));
            for (WebElement ValueTime : ListE) {
                Assert.assertEquals(TimeBasedFinishCriteria[i], ValueTime.getAttribute("value"));
                i++;
                if(i >= 1){
                    break;
                }
            }
        }
    }

    public void CheckOldValueAfterCancel(String cancel, String leaverThisPage, Map<String, String> testData) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Hashtable<String, String> oldValue = this.OldValueCampaign();
        editCampaignValue(testData);
        waitForPageLoadComplete(10,2);
        SaveOrCancelCampaignEdit(cancel);
        waitForPageLoadComplete(10,3);
        leaveThisPageOrStayThisPage(leaverThisPage);
        checkOldValue(oldValue, testData);
    }

    public Hashtable<String, String> OldValueCampaign() {
        Hashtable<String, String> oldValue = new Hashtable<String, String>();
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("old value description");
        utilityFun.wait(2);
        log.info(this.txtBoxDescription.getText());
        oldValue.put("old Value Description", this.txtBoxDescription.getText());
        log.info("old value sender's display name");
        log.info(this.txtBoxSenderDisplayName.getAttribute("value"));
        oldValue.put("old value sender's display name", this.txtBoxSenderDisplayName.getAttribute("value"));
        log.info("old value sender's address");
        log.info(this.txtBoxSenderAddress.getAttribute("value"));
        oldValue.put("old value sender's address", this.txtBoxSenderAddress.getAttribute("value"));
        log.info("old value Dialing Order");
        String actualOrder = "";
        int i = 0;
        for (WebElement element : this.DialingOrderTitle) {
            if (i == 0) {
                actualOrder = element.getAttribute("innerText");
            } else {
                actualOrder = actualOrder + "|" + element.getAttribute("innerText");
            }
            i++;
        }
        oldValue.put("old value Dialing Order", actualOrder);
        String TimeBasedFinishCriteria = "";
        WebElement typeTimeBase = presentElement(this.finishAfterHours, 20);
        if (typeTimeBase == null) {
            TimeBasedFinishCriteria = "FinishAfter" + "|" + this.finishAfterHours.getAttribute("value") + "|" + this.finishAfterMinutes.getAttribute("value");
        } else {
            int j = 0;
            TimeBasedFinishCriteria = "FinishAt";
            for (WebElement SetTime : this.radioFinishAtDateTime) {
                TimeBasedFinishCriteria = TimeBasedFinishCriteria + "|" + SetTime.getAttribute("value");
            }
        }
        oldValue.put("old value Time Based Finish Criteria", TimeBasedFinishCriteria);
        return oldValue;
    }

    public void checkOldValue(Hashtable<String, String> OldValue, Map<String, String> testData) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Search Campaign----------------------------");
        findElementByXpath(locator.getLocator("searchCampaign")).sendKeys(testData.get("name"));
        utilityFun.wait(2);
        WebElement CampaignName = findElementByXpath("//span[text()='" + testData.get("name") + "']");
        CampaignName.click();
        utilityFun.wait(5);
        log.info("the old value of the appropriate description");
        Assert.assertTrue("The value description is modified ", OldValue.get("old Value Description").contentEquals(findElementByXpath(locator.getLocator("txtBoxDescription")).getText()));
        log.info("Match");
        log.info("Check old value Sender's Display Name");
        Assert.assertTrue("The value Sender's Display Name is modified", OldValue.get("old value sender's display name").contentEquals(findElementByXpath(locator.getLocator("txtBoxSenderDisplayName")).getAttribute("value")));
        log.info("Match");
        log.info("Check old values Sender's Address");
        Assert.assertTrue("The value Sender's Address is modified", OldValue.get("old value sender's address").contentEquals(findElementByXpath(locator.getLocator("txtBoxSenderAddress")).getAttribute("value")));
        log.info("Match");
        log.info("Check old values Dialing Order");
        String[] DialingOrderOldValue = OldValue.get("old value Dialing Order").split("\\|");
        ArrayList<String> actualOrder = new ArrayList<String>();
        for (WebElement element : DialingOrderTitle)
            actualOrder.add(element.getAttribute("innerText"));
        for (int i = 0; i <= 2; i++) {
            if (i == actualOrder.indexOf(DialingOrderOldValue[i])) {
                continue;
            } else {
                Assert.fail("The value Dialing Order is modified");
            }
        }
        log.info("Match");
        log.info("Check old values Time Based Finish Criteria");
        String[] OldValueTimeBasedFinishCriteria = OldValue.get("old value Time Based Finish Criteria").split("\\|");
        ArrayList<String> TimeBasedFinishCriteria = new ArrayList<>();
        WebElement typeTimeBasedFinishCriteria = presentElement(finishAfterHours, 20);
        if (typeTimeBasedFinishCriteria == null) {
            TimeBasedFinishCriteria.add("FinishAfter");
            TimeBasedFinishCriteria.add(finishAfterHours.getAttribute("value"));
            TimeBasedFinishCriteria.add(finishAfterMinutes.getAttribute("value"));
        } else {
            int j = 0;
            TimeBasedFinishCriteria.add("FinishAt");
            List<WebElement> listE = findElementsByXpath(locator.getLocator("radioFinishAtDateTime"));
            for (WebElement SetTime : listE) {
                TimeBasedFinishCriteria.add(SetTime.getAttribute("value"));
            }
        }
        for (int j = 0; j <= 2; j++) {
            log.info(TimeBasedFinishCriteria.get(j));
            log.info(OldValueTimeBasedFinishCriteria[j]);
            log.info(TimeBasedFinishCriteria.indexOf(OldValueTimeBasedFinishCriteria[j]));
            if (j == TimeBasedFinishCriteria.indexOf(OldValueTimeBasedFinishCriteria[j])) {
                continue;
            } else {
                Assert.fail("The value Dialing Order is modified");
            }
        }
    }

    public void UnknowError() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Check Unknow Error is display----------------------------");
        WebElement Error = presentElement(locator.getLocator("ErrorMessage"), 20);
        if (Error == null) {
            log.info("No error message");
        } else {
            String MessageError = Error.getText();
            Assert.fail("Unknow Error: " + MessageError);
        }
    }

    public void SaveWithErrorMessage() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<WebElement> errorMessage = driver.findElements(By.xpath("//*[contains(text(),'This is a required field.')]"));
        if (errorMessage.size() < 0) {
            log.info("The error message: This is a required field. is not display");
            Assert.fail();
        } else {
            log.info("The expected error message display: This is a required field.");
        }
    }

    public void SearchCampaignName(String CampaignName, String action) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("--------------------------Search Campaign----------------------------");
        this.searchCampaign.sendKeys(CampaignName);
        utilityFun.wait(10);
        selectDropDownOption(this.pageSizeDropdown, "50");
        utilityFun.wait(1);
        WebElement WebTable = driver.findElement(By.xpath("//table[@role='table']/tbody"));
        List<WebElement> TotalRowCount = WebTable.findElements(By.xpath("tr"));
        for (WebElement rowElement : TotalRowCount) {
            WebElement colElement = rowElement.findElement(By.xpath("td[2]"));
            if (colElement.getText().contentEquals(CampaignName)) {
                if (action.contentEquals("ThreeDots")) {
                    WebElement ThreeDots = rowElement.findElement(By.xpath("td[1]//button"));
                    ThreeDots.click();
                    utilityFun.wait(5);
                    log.info("Click Edit campaign on DropDown");
                    this.editCampaign.click();
                    utilityFun.wait(5);
                    break;
                } else {
                    WebElement CampaignLink = rowElement.findElement(By.xpath("td[2]//span[@data-testid='ColumnTextLink']"));
                    CampaignLink.click();
                    break;
                }
            }
        }
    }

    public void EditMandatoryField(String cmpName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement webCampaignName = driver.findElement(By.xpath("//*[@id='campaignName']"));
        if (!webCampaignName.getAttribute("value").contentEquals(cmpName)) {
            Assert.fail("Campaign is not matching");
        } else {
            log.info("Campaign Name is matching and not empty");
        }
        WebElement selectInfinite = driver.findElement(By.xpath("//div[@class='neo-input-group--inline']/label[@for ='infinite']"));
        log.info("Select Infinite option");
        selectInfinite.click();
        utilityFun.wait(1);
        WebElement noAssociate = driver.findElement(By.xpath("//*[@id='noAssociation']"));
        if (noAssociate.getAttribute("aria-checked").contentEquals("false")) {
            log.info("Select Do not associate any contact list at start option");
            noAssociate.click();
            utilityFun.wait(1);
        }
    }

    public void addSimpleCampaign(String CampaignName, String description, String CampaignType, String strategyName, String DisplayName, String Address, String clsName, String timeBasedFinishCriteria) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        clickButton("addNewCampaign");
        waitForPageLoadComplete(10,1);
        log.info("Add campaign name: " + CampaignName);
        sendKeysToTextBox("name", CampaignName );
        waitForPageLoadComplete(10,1);
        log.info("Add description: " + description);
        findElementByXpath(locator.getLocator("txtBoxDescription")).sendKeys(description);
        waitForPageLoadComplete(10,1);
        log.info("Select campaign type: " + CampaignType);
        if (CampaignType.contentEquals("infinite")) {
            clickButton("infiniteType");
        }
        enterToTheField("Select Campaign Strategy", strategyName);
        waitForPageLoadComplete(10,1);
        log.info("Add sender's display name: " + DisplayName);
        sendKeysToTextBox("senderDisplayName", DisplayName);
        waitForPageLoadComplete(10,1);
        log.info("Add sender's address: " + Address);
        sendKeysToTextBox("senderAddress", Address);
        waitForPageLoadComplete(10,1);
        enterToTheField("Contact List", clsName);
        waitForPageLoadComplete(10,1);
        String[] typeTimeBased = timeBasedFinishCriteria.split("\\|");
        if (typeTimeBased[0].contentEquals("FinishAfter")) {
            findElementByXpath(locator.getLocator("BtnFinishAfter")).click();
            waitForPageLoadComplete(10,1);
            findElementByXpath(locator.getLocator("finishAfterHours")).clear();
            waitForPageLoadComplete(10,1);
            findElementByXpath(locator.getLocator("finishAfterHours")).sendKeys(typeTimeBased[1]);
            waitForPageLoadComplete(10,1);
            findElementByXpath(locator.getLocator("finishAfterMinutes")).clear();
            waitForPageLoadComplete(10,1);
            findElementByXpath(locator.getLocator("finishAfterMinutes")).sendKeys(typeTimeBased[2]);
        } else {
            findElementByXpath(locator.getLocator("radioFinishAt")).click();
            int i = 1;
            List<WebElement> listElement = findElementsByXpath(locator.getLocator("radioFinishAtDateTime"));
            for (WebElement SetTime : listElement) {
                SetTime.sendKeys(typeTimeBased[i]);
                i++;
            }
        }
        clickButton("save");
    }

    public void associateContactListToCamp(String ctlName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        clickTab("tab-contacts");
        sleepInSec(0.2);
        tryClick("contactList", 3);
        sleepInSec(0.5);
        findElementByDataID("contactList").clear();
        waitForPageLoadComplete(10, 1);
        enterToTheField("Contact List", ctlName);
        sleepInSec(0.3);
    }

    public void clickAddNewCampaignButtonFillTheNameAndDescription(String name, String description) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        findElementByDataID("addNewCampaign").click();
        sendKeysToTextBox("name", name);
        findElementByXpath("//*[@id='description']").sendKeys(description);
    }

    public List<String> newCampaignPage_getContactListInDropDown() {
        try {
            String paginationOfDropDownLoc = "//*[@name='contactList']//ul[@role='listbox']/li";
            log.info("paginationOfDropDownLoc: " + paginationOfDropDownLoc);
            if (presentElement(By.xpath(paginationOfDropDownLoc), 5) == null) {
                return new ArrayList<>();
            }
            return findElementsByXpath(paginationOfDropDownLoc).stream().map(WebElement::getText).collect(Collectors.toList());
        } catch (Exception e) {
            log.info("Exception when get list " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void newCampaignPage_searchContactList(String clName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Contact list Name : " + clName);
        clickXpath("//li[@aria-label='Contacts']");
        utilityFun.wait(0.5f);
        findElementByXpath("//*[@name='contactList']//input[@id='search-input']").click();
        utilityFun.wait(0.5f);
        findElementByXpath("//*[@name='contactList']//input[@id='search-input']").sendKeys(clName);
    }

    public void addNewCampaign(Map<String, String> testData) {
        btnAddNewCampaign.click();
        enterToTheField("Name", testData.get("CampaignName"));
        enterToTheField("Description", testData.get("Description"));
        enterToTheField("Select Campaign Strategy", testData.get("Strategy"));
        enterToTheField("Contact List", testData.get("ContactListName"));
        btnSaveCampaign.click();
    }

    public void associateStrategyToCamp(String strName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        clickTab("tab-campaign");
        sleepInSec(0.5);
        tryClick("strategy", 3);
        sleepInSec(0.5);
        clearAssociateStrategyToCamp();
        waitForPageLoadComplete(10, 1);
        enterToTheField("Select Campaign Strategy", strName);
        sleepInSec(0.3);
    }

    public void clearAssociateStrategyToCamp() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        clickTab("tab-campaign");
        waitForPageLoadComplete(30, 1);
        WebElement contactListWrapper = findElementByDataID("strategy-wrapper");
        contactListWrapper.findElement(By.xpath(".//input[@id='search-input']")).sendKeys(Keys.CONTROL, "a");
        contactListWrapper.findElement(By.xpath(".//input[@id='search-input']")).sendKeys(Keys.DELETE);
    }


    public boolean campaignInFirstRow(Map<String, String> data) {
        String cmpInFristRowActual = this.getNameCampaignFirstRow();
        log.info("Campaign in first row: " + cmpInFristRowActual);
        String cmpInFirstRowExpected = data.get("name");
        log.info("Expected name: " + cmpInFirstRowExpected);
        if (cmpInFristRowActual.equals(cmpInFirstRowExpected)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to verify campaign data after created or updated
     *
     * @param data data field: campaign name, campaign type, contact list, strategy
     * @return True: if data campaign created show exactly
     * False: if data campaign created show not excactly
     */
    public boolean verifyCampaignAfterCreated(Map<String, String> data) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.searchCampaign.sendKeys(data.get("Name"));
        sleepInSec(2);
        List<WebElement> rowCells = driver.findElements(By.xpath("//tbody//*[contains(@data-testid,'columnText')]"));
        List<WebElement> headerCell = driver.findElements(By.xpath("//div[@data-testid = 'header-cell']/span"));
        Map<String, String> cmpDataAct = new HashMap<String, String>();
        for (int i = 0; i < headerCell.size(); i++) {
            if (i == 0) {
                cmpDataAct.put(headerCell.get(i).getText(), rowCells.get(i).findElement(By.xpath("./span")).getText());
            } else {
                cmpDataAct.put(headerCell.get(i).getText(), rowCells.get(i).getText());
            }
        }
        log.info("data actual:\n" + cmpDataAct);
        log.info("data expected: \n" + data);
        return cmpDataAct.get("Name").equals(data.get("Name")) && cmpDataAct.get("Type").equals(cmpDataAct.get("Type")) && cmpDataAct.get("Contact List").equals(data.get("Contact List")) && cmpDataAct.get("Campaign Strategy").equals(data.get("Campaign Strategy"));
    }

    /**
     * Method to get Strategy in list dropdown in campaign page
     *
     * @param
     * @return Strategy list in dropdown
     */
    public List<String> getStrategyInListDropDownInCampaignPage() {
        List<String> listStrategy = new ArrayList<String>();
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            listStrategy = findElementsByXpath("//div[contains(@class, 'neo-multiselect') and (@name='strategy')]//ul[@role='listbox']/li").stream().map(WebElement::getText).collect(Collectors.toList());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return listStrategy;
    }

    /**
     * Method to input search strategy in drop down
     *
     * @param
     * @return return true/false
     */
    public boolean inputSearchStrategyToDropDownInCampaignPage(Map<String, String> data) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            findElementByXpath("//div[contains(@class, 'neo-multiselect') and (@name='strategy')]//input[@id='search-input']").sendKeys(data.get("name"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Method to check strategy name keep in campaign editor after save when create/edit campaign
     *
     * @param name
     * @return return true/false
     */
    public boolean verifyStrategyNamekeepinCampaignEditorAfterSave(String name) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Verify Stratgey " + name + " keep in Campaign editor");
        clickTab("tab-campaign");
        WebElement strategyWraperloc = findElementByXpath("//div[@data-testid='strategy-wrapper']");
        String strategyNameAct = strategyWraperloc.findElement(By.xpath(".//input[@data-testid='strategy']")).getAttribute("value");
        log.info("strategy name actual: " + strategyNameAct);
        if (strategyNameAct.equals(name)) {
            log.info("Strategy name keep in campaign editor");
            return true;
        } else {
            log.info("Strategy name not keep in campaign editor");
            return false;
        }
    }


    public void searchStrategyOnCampaignManagerPage(String strategyName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        sendKeysToTextBox("strategy", strategyName);
        waitForPageLoadComplete(10, 1);
    }


    public void searchContactListOnCampaignManagerPage(String contactListName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        sendKeysToTextBox("contactList", contactListName);
        waitForPageLoadComplete(10, 1);
    }

    public String getValueOfSearchedStrategyOnCampaignManagerPage() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement strategySearchBox = driver.findElement(locator.get("searchStrategyBox"));
        return strategySearchBox.getAttribute("value");
    }


    public String getValueOfSearchedContactListOnCampaignManagerPage() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        WebElement contactListSearchBox = driver.findElement(locator.get("searchContactListBox"));
        return contactListSearchBox.getAttribute("value");
    }

    public void verifyTheFinishAtDateTimeChangesWhenTheTimeZoneChangesAccordingly() {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (!SystemUtils.IS_OS_WINDOWS)
            return;
        String currentTimeZone = windowExec("tzutil", "/g");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            LocalTime time = LocalTime.of(13, 0);
            locator.get("btnFinishAt").findElement(driver).click();
            sleepInSec(0.5);
            enterToTheField("Finish At (Time)", "1:00 PM");
            ZoneOffset offset = OffsetDateTime.now().getOffset();
            windowExec("tzutil", "/s", "\"UTC\"");
            sleepInSec(0.3);
            locator.get("btnFinishAfter").findElement(driver).click();
            sleepInSec(0.5);
            locator.get("btnFinishAt").findElement(driver).click();
            sleepInSec(0.5);
            String valueTime = locator.get("finishAtTime").findElement(driver).getAttribute("value");
            LocalTime newTime = LocalTime.parse(valueTime, formatter);
            long deltaSec = ChronoUnit.SECONDS.between(newTime, time);
            long zoneSec = offset.getTotalSeconds();
            Assert.assertEquals(deltaSec, zoneSec);
        } catch (Exception e) {
            log.info("Exception: ", e);
        } finally {
            if (currentTimeZone != null) {
                windowExec("tzutil", "/s", "\"" + currentTimeZone + "\"");
            }
        }
    }

    public int coverPacingToTime (String timeUnit,String valuePacing, String numberRecord) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Time unit input on strategy " + timeUnit);
        log.info("Value pacing follow "+ timeUnit + ": " + valuePacing);
        log.info("number contact in contact list: " + numberRecord);
        int timeExpected = 0;
        switch (timeUnit){
            case "Hour":
                timeExpected = Integer.parseInt(numberRecord)/(Integer.parseInt(valuePacing)/3600);
                break;
            case "Minute":
                timeExpected = Integer.parseInt(numberRecord)/(Integer.parseInt(valuePacing)/60);
                break;
            case "Second":
                timeExpected = Integer.parseInt(numberRecord)/Integer.parseInt(valuePacing);
                break;
            default:
                Assert.fail(timeUnit + " input incorrectly");
                break;
        }
        return timeExpected;
    }

    public void verifyStatusCampaignDisplayCorrectly(String campaignName, String status){
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Campaign Name: " + campaignName);
        log.info("Status expected: " + status);
        try{
            String actualStatus = RestMethodsObj.getAttributeValueOfJobCampaign(campaignName, "status");
            sleepInSec(0.5);
            Assert.assertTrue("Status of Campaign is not display correctly with expected: " + status + " but actual find: " + actualStatus,
                    actualStatus.equalsIgnoreCase(status));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void verifyMoreOptionDisplayOrder( List<String> expectedList){
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        List<WebElement> optionEles = this.findElementsByXpath("//div[@role='menu']/div");
        List<String> actList = new ArrayList<String>();
        for (WebElement ele : optionEles){
            if (expectedList.contains(ele.getText())){
                actList.add(ele.getText());
            }
        }
        log.info("Actual: "+actList);
        log.info("Expected: "+expectedList);
        Assert.assertEquals("The Stop option not displayed in order Edit-Start-Stop",expectedList,actList);
    }

}
