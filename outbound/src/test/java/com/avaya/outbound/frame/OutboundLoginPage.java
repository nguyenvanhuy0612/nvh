package com.avaya.outbound.frame;

import com.avaya.outbound.lib.support.Locator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class OutboundLoginPage extends CommonFunction {
    private final Logger log = LogManager.getLogger(OutboundLoginPage.class);
    public Locator locator = new Locator(this);

    public OutboundLoginPage(WebDriver driver) {
        super(driver);
    }

    public void appState() {
    }

}
