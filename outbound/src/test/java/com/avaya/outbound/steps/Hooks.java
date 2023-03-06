package com.avaya.outbound.steps;

import com.avaya.outbound.frame.CommonFunction;
import com.avaya.outbound.frame.NGMPortal;
import com.avaya.outbound.lib.EnvSetup;
import com.avaya.outbound.lib.Log4J2;
import com.avaya.outbound.lib.UtilityFun;
import io.cucumber.java.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;


public class Hooks {
    private static final Logger log = LogManager.getLogger(Hooks.class);
    private final Context context;

    public Hooks(Context context) {
        this.context = context;
    }

    @BeforeAll(order = 10)
    public static void loadEnv() {
        new EnvSetup();
        Log4J2.init();
        UtilityFun.cleanUpDataForTesting();
    }

    @Before(order = 10)
    public void appState(Scenario scenario) {
        System.out.println("************************************************************************************");
        System.out.println("Entering method " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String feature = scenario.getUri().toString();
        String name = scenario.getName();
        // Init log file
        String sName = name.replaceAll("\\W", "_");
        ThreadContext.put("log_filename", sName);
        log.info("Scenario: " + name);
        log.info("Feature: " + feature);
        log.info("log_filename: " + sName + ".log");

        // Setup webdriver for Thread - Scenario
        context.driver = EnvSetup.setupDriverInstance();
        if (EnvSetup.USENGM.equalsIgnoreCase("yes")) {
            new NGMPortal(context.driver).loginWorkspaces(null);
            context.utilityFun.updateSSOToken(context);
        }
        context.commonFunction = new CommonFunction(context.driver);
        // Init custom Selenium page object for all the context fieldList
        context.initContextPageObject();
        context.utilityFun.tryToLoadData(context.testData, scenario);
    }

    @After(order = 10)
    public void tearDown(Scenario scenario) {
        log.info("Entering method " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String name = scenario.getName();
        log.info("Scenario: " + name);
        log.info("=====================================================================================================");
        log.info("Test Status: " + scenario.getStatus());
        log.info("=====================================================================================================");
        if (context.commonFunction != null) {
            // Snapshot
            context.commonFunction.takeScreenShotOnFailedScenario(scenario);
        }
        // Update status to jira
        context.utilityFun.updateJIRAResults(scenario);
        UtilityFun.writeTestResult(scenario);
        context.utilityFun.quitBrowser(context.driver, scenario);
    }

    @AfterAll(order = 10)
    public static void cleanUpLab() {
        EnvSetup.driverMaps.forEach((t, d) -> {
            try {
                d.close();
                d.quit();
            } catch (Exception e) {
            }
        });
    }
}
