package com.avaya.outbound.frame;

import com.avaya.outbound.lib.support.Locator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.util.*;

public class MonitorPage extends CommonFunction {
    public final Logger log = LogManager.getLogger(MonitorPage.class);
    public Locator locator = new Locator(MonitorPage.class);

    /**
     * method get status of campaign on monitor page
     * @return: Map summary status with key is total, running and other
     */
    public Map<String, Integer> activeCampaigns() {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, Integer> results = new HashMap<>();
        List<String> statusJob = this.getListColumnDataOnAllPage("Status");
        results.put("total", statusJob.size());
        int numberRunning = 0;
        int numberPaused = 0;
        int numberOther = 0;
        for (String status : statusJob) {
            if (status.equalsIgnoreCase("Running")) {
                ++numberRunning;
            } else if (status.equalsIgnoreCase("Paused")) {
                ++numberPaused;
            } else {
                ++numberOther;
            }
        }
        results.put("running", numberRunning);
        results.put("paused", numberPaused);
        results.put("other", numberOther);
        return results;
    }

    public boolean verifyExistingCampaign(String campaignName) {
        log.info("----------------------------------------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        this.clickRefreshButton();
        return this.getListColumnDataOnCurPage("Name").contains(campaignName);
    }

    public void takeActionOnActiveCampaignUI(String campaignName, String action, String confirm) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            this.search(campaignName);
            this.clickAction(campaignName, action);
            this.waitForPageLoadComplete(60, 0.5);
            if (confirm.equalsIgnoreCase("Cancel")) {
                this.tryClick(By.xpath("//*[@data-testid='actionConfirmationModal']//button[@data-testid='cancel-action']"), 10);
            } else {
                this.tryClick(By.xpath("//*[@data-testid='actionConfirmationModal']//button[@data-testid='done-action']"), 10);
            }
            this.waitForPageLoadComplete(10, 2);
        } catch (Exception e) {
            log.info(e);
            Assert.fail("Cannot click action " + action);
        }
    }
}