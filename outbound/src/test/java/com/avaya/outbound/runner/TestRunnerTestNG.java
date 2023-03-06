package com.avaya.outbound.runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = {"src/test/resources/feature"},
        glue = {"com.avaya.outbound.steps"},
        tags = "not (@ignore or @IgnoreInDailyRun or @IgnoreInParallel)",
        snippets = CucumberOptions.SnippetType.UNDERSCORE,
        plugin = {"pretty",
                "html:target/cucumber/reports/Cucumber.html",
                "json:target/cucumber/reports/Cucumber.json",
                "junit:target/cucumber/reports/Cucumber.xml",
                "rerun:target/cucumber/reports/rerun.txt"
        })
public class TestRunnerTestNG extends AbstractTestNGCucumberTests {
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
