package com.avaya.outbound.lib.support;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.TreeMap;

public class TestResultCollection implements ConcurrentEventListener {

    Map<String, String> testResults = new TreeMap<>();

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestCaseStarted.class, testCaseStarted);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, testCaseFinished);
    }

    private final EventHandler<TestCaseFinished> testCaseFinished = event -> {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + event.getResult());
        testResults.put(TestResultCollection.getTagDetails(event), event.getResult().getStatus().name());
    };

    private final EventHandler<TestCaseStarted> testCaseStarted = event -> {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + event.getTestCase());
        testResults.put(TestResultCollection.getTagDetails(event), "STARTED");
    };

    public static String getTagDetails(TestCaseEvent event) {
        return event.getTestCase().getTags().toString();
    }
}