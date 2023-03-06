package com.avaya.outbound.steps;

import com.avaya.outbound.frame.*;
import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.UtilityFun;
import com.avaya.outbound.lib.support.TestData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.List;

public abstract class StepContext {
    public Logger log = LogManager.getLogger(this);
    public Context context;
    public List<Object> listShared;
    public List<String> listStringShared;
    public List<WebDriver> listDriver;
    public TestData<String, String> testData;
    public TestData<String, Object> testDataObject;

    public WebDriver driver;
    public CommonFunction commonFunction;
    public UtilityFun utilityFun;

    public CampaignManagerPage CampaignManagerPageObj;
    public CampaignStrategyPage CampaignStrategyPageObj;
    public MonitorPage MonitorPageObj;
    public ContactListPage ContactListPageObj;

    public NGMPortal NGMPortalObj;
    public DataSourcePage DataSourcePageObj;
    public SecureAPICommunicationMethods SecureAPICommunicationMethodsObj;
    public RestMethods RestMethodsObj = new RestMethods();
    public RestMethods RestMethodsContactListObj = new RestMethods(EnvSetup.OUTBOUND_CONTACTS_API_BASEURL, EnvSetup.DB_USER, EnvSetup.DB_PASSWORD);
}
