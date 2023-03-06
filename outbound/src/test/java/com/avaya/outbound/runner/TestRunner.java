package com.avaya.outbound.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.BeforeClass;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        objectFactory = com.avaya.outbound.lib.support.CustomPicoFactory.class,
        features = {"src/test/resources/feature"},
        glue = {"com.avaya.outbound.steps"},
        monochrome = true,
        //tags = "not (@ignore or @IgnoreInDailyRun or @IgnoreInParallel)",
        tags = "@IXOUTREACH-4788",
        publish = false,
        snippets = CucumberOptions.SnippetType.UNDERSCORE,
        plugin = {"pretty",
                "usage:target/cucumber/reports/Cucumberusage.txt",
                "html:target/cucumber/reports/Cucumber.html",
                "json:target/cucumber/reports/Cucumber.json",
                "junit:target/cucumber/reports/Cucumber.xml",
                "rerun:target/cucumber/reports/rerun.txt",
                "timeline:target/cucumber/reports/timeline",
                "message:target/cucumber/reports/message.json",
                "com.avaya.outbound.lib.support.CustomReportListener",
                "com.avaya.outbound.lib.support.TestResultCollection"
        }
)
public class TestRunner {
    /**
     @BeforeClass public static void loadEnvData() {
     //Move to io.cucumber.java.BeforeAll to support Cucumber CLI
     //All junit implement will be handled by Cucumber Hooks
     }

     public static void main(String[] args) {
     JUnitCore.runClasses(TestRunner.class);
     }
     */
}
