package com.avaya.outbound.frame;

import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.support.Locator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CampaignStrategyPage extends CommonFunction {
    private final Logger log = LogManager.getLogger(CampaignStrategyPage.class);
    public Locator locator = new Locator(this);

    public WebElement homeStrategyPage;
    public WebElement createStrategyButton;
    public WebElement btnAddNewStrategy;
    public WebElement btnSaveStrategy;
    public WebElement txtBoxStrategyName;
    public WebElement txtBoxStrategyDescription;
    public WebElement txtBoxStrategySMSText;
    public WebElement hintMessageName;
    public WebElement errMsgStrategySMSTextInput;
    public WebElement confirmLeaveStrategy;
    public WebElement confirmStayStrategy;
    public WebElement btnCancelStrategy;
    public WebElement createStrategyPage;
    public WebElement notificationInform;
    public List<WebElement> strategyNameCells;
    public WebElement sortingArrowUp;
    public WebElement sortingArrowDown;

    public CampaignStrategyPage(WebDriver driver) {
        super(driver);
    }

    public boolean createStrategyAPI(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        JsonObject result = RestMethodsObj.createStrategy(data);
        if (result == null) {
            log.info("Cannot create Strategy by API");
            return false;
        } else {
            String nameStrategy = data.get("name");;
            String descriptionStrategy = data.get("description");
            String smsText = data.get("smsText");
            String smsPace = data.get("smsPace");
            String smsPacingTimeUnit = data.get("smsPacingTimeUnit").toUpperCase();
            if (data.containsKey("strategyType") == true ? data.get("strategyType").equalsIgnoreCase("sms") : data.get("type").equalsIgnoreCase("sms")) {
                try {
                    if (StringUtils.containsIgnoreCase(result.toString(), "errorcode") || StringUtils.containsIgnoreCase(result.toString(), "errormessage")) {
                        log.info("!ERR: response of API contains error!!!");
                        return false;
                    } else if (result.get("name").toString().replace("\"", "").equals(nameStrategy) &&
                            result.get("description").toString().replace("\"", "").equals(descriptionStrategy) &&
                            result.get("smsText").toString().replace("\"", "").equals(smsText)) {
                        log.info("Create SMS strategy successfully");
                        return true;
                    } else {
                        log.info("Create SMS strategy unsuccessfully: name or description or sms text is incorrect!!!");
                        return false;
                    }
                } catch (Exception e) {
                    log.info("Cannot create Strategy by API");
                    log.info(e);
                    return false;
                }
            } else {
                log.info("currently only support create sms strategy");
                return false;
            }
        }
    }

    public void valueSpecialCharacters(String field, String data, String Error) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (field.toLowerCase()) {
            case ("name"):
                log.info("Strategy Name value contain special characters");
                btnSaveStrategy.click();
                utilityFun.wait(1);
                for (String specialCharacter : data.split("")) {
                    txtBoxStrategyName.clear();
                    utilityFun.wait(1);
                    txtBoxStrategyName.sendKeys(specialCharacter);
                    utilityFun.wait(1);
                    String errMsg = hintMessageName.getText();
                    log.info("Error message Actual: " + errMsg);
                    log.info("Error message Expected: " + Error);
                    Assert.assertEquals("Strategy name can contain special characters - failed", Error, errMsg);
                }
                utilityFun.wait(1);
                txtBoxStrategyName.clear();
                break;
        }
    }

    public void createStrategy(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if(data.containsKey("strategyType") == true ? data.get("strategyType").equalsIgnoreCase("sms") : data.get("type").equalsIgnoreCase("sms")) {
            this.clickButton("add");
            this.waitForPageLoadComplete(10, 1);
            if(data.containsKey("name")) {
                this.sendKeysToTextBox("name",data.get("name"));
            }else{
                log.info("!!!The Name strategy is null!!!");
            }
            this.waitForPageLoadComplete(10, 1);
            if(data.containsKey("description")) {
                this.sendKeysToTextBox("description",data.get("description"));
            }else{
                log.info("!!!The Description strategy is null!!!");
            }
            this.waitForPageLoadComplete(10, 1);
            if(data.containsKey("smsText")) {
                this.sendKeysToTextBox("smsText",data.get("smsText"));
            }else{
                log.info("!!!The SMSText strategy is null!!!");
            }
            this.waitForPageLoadComplete(10, 1);
            if(data.containsKey("smsPacingTimeUnit")) {
                this.selectDropDownOptionEqual("smsPacingTimeUnit",data.get("smsPacingTimeUnit"));
            }else{
                log.info("!!!The smsPacingTimeUnit is missed, using default is SECONDS!!!");
            }
            this.waitForPageLoadComplete(10, 1);
            if(data.containsKey("smsPace")) {
                this.sendKeysToTextBox("smsPace", data.get("smsPace"));
            }else{
                log.info("!!!The smsPace is missed, using default is 10!!!");
                this.sendKeysToTextBox("smsPace", "10");
            }
            this.waitForPageLoadComplete(10, 1);
            if(data.containsKey("smsPacingTimeUnit")) {
                this.selectDropDownOptionEqual("smsPacingTimeUnit",data.get("smsPacingTimeUnit"));
            }else{
                log.info("!!!The smsPacingTimeUnit is missed, using default is SECONDS!!!");
            }
            if(this.isEnable("save")){
            this.clickButton("save");}
            //this.waitForPageLoadComplete(10, 1);
        }else{
            log.info("!!!Currently only support SMS strategy type!!!");
        }
    }

    public void enterStrategyFormDetails(Map<String, String> data) {
        if(data.containsKey("name")){
            if (!data.get("name").equals("")) {
                sendKeysToTextBox("name", data.get("name"));
            }
        }
        if(data.containsKey("description")){
            if (!data.get("description").equals("")) {
                sendKeysToTextBox("description", data.get("description"));
            }
        }
        if(data.containsKey("smsText")){
            if (!data.get("smsText").equals("")) {
                if(!data.get("smsText").equals("blank")){
                    sendKeysToTextBox("smsText", data.get("smsText"));
                }else {
                    sendKeysToTextBox("smsText", "");
                }
            }
        }
        if(data.containsKey("smsPace")){
            if (!data.get("smsPace").equals("")) {
                sendKeysToTextBox("smsPace", data.get("smsPace"));
            }
        }
    }

    public void verifyStrategyPageDetails(Map<String, String> data) {
        if (data.get("name") != null) {
        }
        if (data.get("description") != null) {
        }
        if (data.get("smsText") != null) {
        }
    }

    public boolean StrategyExisted(String Name,String Type){
        try {
            basicSearchOnTable(Name);
            List<WebElement> strategyRows = driver.findElements(By.xpath("//tbody/tr[@role='row']"));
            for (WebElement strategyRow : strategyRows) {
                List<WebElement> strategyCells = strategyRow.findElements(By.xpath("//td[@role='cell']"));
                for(int i = 0; i < strategyCells.size(); i++){
                    if(strategyCells.get(i).getText().equals(Name)){
                        log.info("!!!Strategy: " + Name + "is existed");
                        log.info("Get Strategy Type");
                        String typeStrategy = getAttributeField("columnTypeImage", "class");
                        log.info("!!!Strategy: " + Name + "and Strategy Type: " + typeStrategy + "is existed");
                        if(typeStrategy.contains(Type.toLowerCase())) {
                            return true;
                        }else{
                            log.info("Strategy Type is not correct");
                            return false;
                        }
                    }
                }
            }
            log.info("!!!Strategy: " + Name + "is not existed");
            return false;
        }catch (Exception e){
            log.info("!!!Something went wrong when find Strategy Name on Strategy page");
            log.info(e);
            return false;
        }
    }

    public boolean dialogConfirmIsDisplay(){
        try {
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            List<WebElement> dialogloc = driver.findElements(By.xpath("//*[@aria-label='dialog']"));
            return dialogloc.size() != 0;
        }finally {
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        }
    }

    public boolean verifyNotificationSuccessfully(String message){
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try{
            if (notificationInform.isDisplayed() && notificationInform.getText().equalsIgnoreCase(message)){
                return true;
            }else{
                log.info("The message is not correct: " + notificationInform.getText());
                return false;
            }
        }catch (Exception e){
            log.info("The notification successfully is not displayed");
            log.info(e);
            return false;
        }
    }

    public boolean verifyAddStrategySuccessfullyUI(String Name, String message, String des){
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return this.verifyNotificationSuccessfully(message) && this.StrategyExisted(Name, des);
    }

    public void inputTextBox(WebElement e, String text){
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        e.clear();
        utilityFun.wait(1);
        e.sendKeys(text);
        utilityFun.wait(1);
    }

    /*
     * Method to verify user is on edit strategy page
     * para: nameStrategy
     * return:
     * @true: user is on edit strategy page
     * @false: user is not on edit strategy page
     * */
    public boolean verifyUserOnEditStrategyPage(String nameStrategy){
        String nameStrategyEdit = "//ol/li[@aria-label='"+ nameStrategy +"']";
        String namePageStrategy = "//ol/li[@aria-label='Campaign Strategy']";
        try {
            WebElement nameStrategyEditElement = findElementByXpath(nameStrategyEdit);
            WebElement namePageStrategyElement = findElementByXpath(namePageStrategy);
            return nameStrategyEditElement.isDisplayed() && namePageStrategyElement.isDisplayed();
        }catch (Exception e){
            log.info(e);
            return false;
        }
    }

    /*
     * Method for each action of specific strategy
     * para: nameStrategy
     * return:
     * @true: take action successfully
     * @false: take action unsuccessfully
     * */
    public boolean actionOnSpecificStrategy(Map<String, String> data){
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info(data);
        String action = data.get("action");
        try {
            switch (action) {
                case "Edit":
                    try {
                        clickAction(data.get("name"), action);
                        utilityFun.wait(1);
                        //verify user is on edit page
                        if (this.verifyUserOnEditStrategyPage(data.get("name"))) {
                            data.replace("name","");//do not edit name
                            this.enterStrategyFormDetails(data);
                            utilityFun.wait(1);
                            clickButton("save");
                            utilityFun.wait(1);
                            return true;
                        }else{
                            // user is not on edit page
                            log.info("!!ERR: user is not on Edit strategy page!!!");
                            return false;
                        }
                    }catch(Exception e){
                        log.info("!!Exception: Something went wrong during edit Strategy!!");
                        log.info(e);
                        return false;
                    }
                case "Delete":
                    log.info("!!!Not support this action yet!!!");
                    return false;
                default:
                    log.info("has no action");
                    return false;
            }
        }catch (Exception e){
            log.info("!!Exception: Something went wrong during click action of Strategy");
            log.info(e);
            return false;
        }
    }

    public void createListStrategiesPage(int numberStrategies,Map<String, String> data) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("Number Campaign Strategies need create : " + numberStrategies);
        log.info("Name campaign strategy start with : " + data.get("name"));
        log.info("Description: " + data.get("description"));
        log.info("SMS Text:" + data.get("smsText"));
        log.info("strategy Type" + data.get("type"));
        String nameStrategyInt = data.get("name");
        for (int i = 0; i < numberStrategies; i++) {
            String nameStrategy = data.get("name") + "_" + i;
            data.put("name", nameStrategy);
            boolean flag = this.createStrategyAPI(data);
            data.put("name", nameStrategyInt);
        }
    }

    public boolean verifySortWorkProperly(String nameColumnSort) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        utilityFun.wait(3);
        try {
            List<String> listDefault = this.getListColumnDataOnCurPage(nameColumnSort);
            Collections.sort(listDefault);
            log.info("Verify sort Ascending should work properly");
            log.info("List sort expected:");
            log.info(listDefault);
            WebElement nameColumn = driver.findElement(By.xpath("//th[@role='columnheader']//span[text() ='" + nameColumnSort + "']"));
            tryClick(nameColumn, 1);
            waitForPageLoadComplete(10, 1);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowUp.isDisplayed());
            List<String> listAfterSortAcs = this.getListColumnDataOnCurPage(nameColumnSort);
            log.info("List sort Actual:");
            log.info(listAfterSortAcs);
            Assert.assertTrue("Sort work is not properly", listAfterSortAcs.equals(listDefault));
            log.info("Verify sort Descending  should work properly");
            tryClick(nameColumn, 1);
            waitForPageLoadComplete(10, 1);
            Assert.assertTrue("Sort icon is not display on campaign landing page", sortingArrowDown.isDisplayed());
            List<String> listAfterSortDes = this.getListColumnDataOnCurPage(nameColumnSort);
            listDefault.sort(Comparator.reverseOrder());
            log.info("List sort expected:");
            log.info(listDefault);
            log.info("List sort Actual:");
            log.info(listAfterSortDes);
            Assert.assertTrue("Sort work is not properly", listAfterSortDes.equals(listDefault));
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Sort work not properly, Test is Failed");
        }
        return false;
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
            WebElement sortIconElem = presentElement(By.xpath(locator.locatorFormat("iconSortingArrow", columnName)), 10);
            Assert.assertNull("Sort is Display on campaign landing page", sortIconElem);
            return true;
        } catch (Exception e) {
            log.info("Sort work is not correctly, Test failed");
        }
        return false;
    }

    /**
     * Method to get list all strategies and sort order is on Last modified On in descending order from API
     *
     * @param
     * @return
     * List<String>  all strategies and sort order is on Last modified On in descending
     */
    public List<String> getListStrategiesDefaultSort(Map<String, String> data) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        //get all strategies from API
        JsonArray res = RestMethodsObj.getStrategyList(data);
        //convert strategies json result to map <String, String> list
        HashMap<String, String> listStrategies = new HashMap<String, String>();
        try {
            for (int i = 0; i < res.size(); i++) {
                JsonObject resResult = (JsonObject) res.get(Integer.parseInt(String.valueOf(i)));
                listStrategies.put(resResult.get("name").getAsString(), resResult.get("updatedOn").getAsString());
            }
        }catch (Exception e){
            log.info("!!Exception during get all strategies form API!!");
            log.info(e);
            return null;
        }
        //get list time UpdateOn
        ArrayList<String> listTimeUpdateOn = new ArrayList<>();
        for (Map.Entry<String, String> entry : listStrategies.entrySet()) {
            listTimeUpdateOn.add(entry.getValue());
        }
        //sort descending on time UpdateOn
        String temp = null;
        for (int i = 0; i < listTimeUpdateOn.size(); i ++ ){
            for (int j = 0; j < listTimeUpdateOn.size() -1; j++){
                LocalDateTime aLDT1 = LocalDateTime.parse(listTimeUpdateOn.get(j).replace("Z", ""));
                LocalDateTime aLDT2 = LocalDateTime.parse(listTimeUpdateOn.get(j+1).replace("Z", ""));
                if(aLDT1.compareTo(aLDT2) < 0){// aLDT2 is after aLDT1
                    temp = listTimeUpdateOn.get(j);
                    listTimeUpdateOn.set(j,listTimeUpdateOn.get(j+1));
                    listTimeUpdateOn.set(j+1,temp);
                }
            }
        }
        //sort list Hashmap strategies by last time update on descending
        List<String> listStrategiesSortedDefault = new ArrayList<String>();
        for (String UpdateOn : listTimeUpdateOn) {
            for (Map.Entry<String, String> entry : listStrategies.entrySet()) {
                if (entry.getValue().equals(UpdateOn)) {
                    listStrategiesSortedDefault.add(entry.getKey());
                    break;
                }
            }
        }
        return listStrategiesSortedDefault;
    }
    /**
     * Method to verify The default Sort order is on Last modified On in descending order on current page
     *
     * @param
     * defaultSortExpected: the list strategies that Sort order is on Last modified On in descending order
     * index: start verify sort order from "index" in defaultSortExpected list
     * @return
     * true: default Sort order is on Last modified On in descending order
     * false: default Sort is incorrect
     */
    public boolean verifyDefaultSortCurrentPage(List<String> defaultSortExpected, int index) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        //get current list strategies
        List<String> strategiesActual = getListColumnDataOnCurPage("Name");
        log.info("Expected order: " + defaultSortExpected);
        log.info("Actual order: " + strategiesActual);
        log.info("!!!Start verify default sort order...");
        for (int i = index; i < strategiesActual.size() ; i++){
            if (!strategiesActual.get(i).equals(defaultSortExpected.get(i))){
                log.info("!!ERR: incorrect default sort!!");
                log.info("!!Actual: " + strategiesActual.get(i));
                log.info("!!Expected: " + defaultSortExpected.get(i));
                return false;
            }
        }
        return true;
    }

    /**
     * Method to verify the refresh button is enable on strategy page list
     *
     * @param
     * @return
     * true: refresh button is enable
     * false: refresh button is not enable
     */
    public boolean verifyRefreshButtonEnable() {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return isEnable("refreshTable");
    }
    /**
     * Method to get Strategy ID
     *
     * @param strategyName
     *
     * @return
     * null
     * String ID
     */
    public String getStrategyID(String strategyName, String strategyType) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("Strategy Name: " + strategyName);
            Map<String, String > data =new HashMap<>();
            data.put("strategyType",strategyType);
            data.put("name",strategyName);
            data.put("pageNumber","0");
            data.put("pageSize","1000");
            JsonArray res = RestMethodsObj.getStrategyList(data);
            log.info(res);
            for (int i = 0; i < res.size(); i++) {
                JsonObject strategyJsonObj = (JsonObject) res.get(Integer.parseInt(String.valueOf(i)));
                if(strategyJsonObj.get("name").getAsString().equals(data.get("name"))){
                    return strategyJsonObj.get("id").getAsString();
                }
            }
        } catch (Exception e) {
            log.info("!!!ERR: something went wrong when get ID of strategy!!!");
            log.info(e.getMessage());
        }
        return null;
    }
    /**
     * Method to delete Strategy by ID or Name
     *
     * @param strategyName, id
     * @return
     * True: delete successfully
     * False: delete unsuccessfully
     */
    public boolean deleteStrategy(String strategyType, String strategyName, String id) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            log.info("Strategy Name: " + strategyName + "\nid: " + id);
            if (id == null || id.isEmpty()) {
                id = this.getStrategyID(strategyName, strategyType);
            }
            if (id == null || id.isEmpty()) {
                log.info("!!!Not found strategy!!!");
                return false;
            }
            String url = EnvSetup.OUTBOUND_CAMPAIGNS_API_BASEURL + "/strategies/" + id;
            log.info("url: " + url);
            JsonObject result = RestMethodsObj.rest(RestMethods.Method.DELETE, url, "");
            //handle result to make sure the result is correct: todo later
            log.info(result);
            return true;
        } catch (Exception e) {
            log.info("!!!ERR: something went wrong when deleting strategy!!!");
            log.info(e.getMessage());
            return false;
        }
    }
    /**
     * Method to delete List Strategy by Name
     *
     * @param data
     * data contains: name, strategyType
     * @return
     * True: delete successfully
     * False: delete unsuccessfully
     */
    public boolean deleteMultiStrategies(Map<String, String> data) {
        try {
            log.info("---------------------------------------------------------------------------------------");
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            List<String> strategies = Arrays.asList(data.get("name").split("\\|"));
            for (int i = 0 ;i < strategies.size(); i++){
                if(!this.deleteStrategy(data.containsKey("strategyType") == true ? data.get("strategyType") : data.get("type"), strategies.get(i),null)){
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    /**
     * method using to verify the result of basic search
     * @param columnName
     * @param searchText
     * @return true if the result match to expected value
     * @return false if no record is returned or result do not match with expected value
     */
    @SuppressWarnings("rawtypes")
    public boolean verifyBasicSearch(String columnName, String searchText, Map<String, String> data) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("SearchText: " + searchText);
        log.info("Column Name: " + columnName);
        log.info("Start searching...");
        String specialCharacters = "~`!@#$%^&*()+={}[]\\|\\/:;\"'<>,.?";
        boolean exitFlag = false;
        log.info("Verifying if the name contains special character...");
        for (int i = 0; i < specialCharacters.length(); i++) {
            char letter = specialCharacters.charAt(i);
            log.info("Letter: " + letter);
            if (searchText.contains(String.valueOf(letter))) {
                exitFlag = true;
                break;
            }
        }
        ArrayList <String> strategyName = new ArrayList<>();
        sendKeysToTextBox("searchTable", searchText);
        waitForPageLoadComplete(20, 2);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        if (!exitFlag) {
            List<WebElement> rows = findElementsByXpath("//table[@data-testid='table']/tbody/tr[*]");
            if (rows.size() != 0) {
                driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
                Hashtable[] totalRecords = getListColumnDataOnPage(columnName, "", "", "", "");
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
                    for (String name : strategyName) {
                        if (!(name.toLowerCase().contains(searchText.toLowerCase()))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        log.info("No record match to the conditional searching");
        return false;
    }

    public void createMultipleStrategies(String sStrategyNames,String sStrategyType) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String [] allStrategy = sStrategyNames.split("\\|");
        String [] allStrategyType = sStrategyType.split("\\|");
        for (int i = 0; i < allStrategy.length; i++) {
            Map<String, String> mydata = new HashMap<>();
            mydata.put("name", allStrategy[i]);
            mydata.put("description", allStrategy[i]);
            mydata.put("sms", allStrategyType[i]);
            mydata.put("strategyType", allStrategyType[i]);
            mydata.put("smsText", allStrategy[i]);
            mydata.put("smsPace", "20");
            mydata.put("smsPacingTimeUnit", "Second");
            log.info(i+"****"+mydata.toString());
            boolean flag = this.createStrategyAPI(mydata);
        }
        log.info("Successfully created strategies "+sStrategyNames);
    }
    public void deleteMultipleStrategies(String sStrategyNames,String sStrategyType ) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String [] allStrategy = sStrategyNames.split("\\|");
        String [] allStrategyType = sStrategyType.split("\\|");
        for (int i = 0; i < allStrategy.length; i++) {
            deleteStrategy(allStrategyType[i], allStrategy[i], "");
        }
        log.info("Successfully deleted strategies "+sStrategyNames);
    }



    public void compareAllDataOfRecordsWithSort(Hashtable [] noneSort, Hashtable [] noneDefaultSort) {
        log.info("---------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String recordName = null;
        for (Hashtable record : noneSort) {
            HashSet<String> defaultSort = null;
            HashSet<String> afterSorted = null;
            Set keys = record.keySet();
            for (Object key : keys) {
                String str = (String) key;
                if (str.equals("Name")) {
                    recordName = record.get(str).toString();
                    defaultSort = new HashSet<>(record.values());
                    break;
                }
            }
            for (Hashtable rec : noneDefaultSort) {
                boolean existFlag = false;
                Set key1 = rec.keySet();
                for (Object key2 : key1) {
                    String str1 = (String) key2;
                    if (str1.equals("Name")) {
                        String recName = rec.get(str1).toString();
                        if (recordName.equals(recName)) {
                            afterSorted = new HashSet<>(rec.values());
                            existFlag = true;
                            break;
                        }
                    }
                }
                if (existFlag) {
                    break;
                }
            }
            log.info("Data of record --------- None Sort: " + defaultSort);
            log.info("Data of record -- After Sorting as: " + afterSorted);
            Assert.assertEquals("All value do not match", defaultSort, afterSorted);
        }
    }


    /**
     * method using to verify the field should be modified or not
     * @param field
     * @return true if modified expected
     * @return false if modified not expected
     */
    public boolean verifyFieldOfStrategyCanModify(String field){
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        switch (field.toLowerCase()){
            case "strategy name":
                log.info("Expected: User should not be able to modify the Name field - Name field grey out");
                if (this.isEnable("name")) {
                    log.info("Actual: Field Name are enable - failed");
                    return false;
                } else {
                    log.info("Actual: Field Name are disable - passed");
                    return true;
                }
            case "description":
                log.info("Expected: User should be able to modify the description field");
                return this.isEnable("description");
            case "sms text":
                log.info("Expected: User should be able to modify the sms text field");
                //return this.isEnable("smstext");
                return this.isEnable("smsText");
            case "pace":
                log.info("Expected: User should be able to modify the pace field");
                return this.isEnable("smsPace");
            case "timeunit":
                log.info("Expected: User should be able to modify the time unit field");
                return this.isEnable("smsPacingTimeUnit");
        }
        log.info("Invalid field name");
        return false;
    }

    /**
     * method using to verify the strategy save success
     * @param data
     * @return true if save success
     * @return false if save unsucces
     */
    public boolean verifyStrategySaveSuccessful(Map<String, String> data) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("User should be redirected to campaign strategy landing page");
        if(getTextField("add").equals("Add New Strategy")){
            log.info("should be have notification - Strategy added successfully");
            if(!data.containsKey("NotificationAlert")){
                data.put("NotificationAlert", "Strategy added successfully.");
            }
            this.verifyNotification(data);
            if(data.containsKey("checkOnTop")){
                if (data.get("checkOnTop").equals("yes")){
                    log.info("Newly Strategy should be at top row in strategies list");
                    return newlyCreatedOrEditedInFirstRow(data.get("name"));
                }else return true;
            } else return true;
        }else return false;
    }
}