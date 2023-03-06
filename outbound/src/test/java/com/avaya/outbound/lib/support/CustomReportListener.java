package com.avaya.outbound.lib.support;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;

public class CustomReportListener implements EventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        /*
         * :: is method reference , so this::collectTag means collectTags method in
         * 'this' instance. Here we say runStarted method accepts or listens to
         * TestRunStarted event type
         */
        publisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        publisher.registerHandlerFor(TestSourceRead.class, this::featureRead);
        publisher.registerHandlerFor(TestCaseStarted.class, this::ScenarioStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
    }

    /*
     * Here we set argument type as TestRunStarted if you set anything else then the
     * corresponding register shows error as it doesn't have a listner method that
     * accepts the type specified in TestRunStarted.class
     */


    // Here we create the reporter
    private void runStarted(TestRunStarted event) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + event.getInstant());
    }

    private void runFinished(TestRunFinished event) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + event.getResult());
    }

    private void featureRead(TestSourceRead event) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + event.getSource());
    }

    // This event is triggered when Test Case is started
    // here we create the scenario node
    private void ScenarioStarted(TestCaseStarted event) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + event.getTestCase());
    }

    // step started event
    // here we creates the test node
    private void stepStarted(TestStepStarted event) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + event.getTestCase());
    }

    // This is triggered when TestStep is finished
    private void stepFinished(TestStepFinished event) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + event.getResult());
    }
}