@IXOUTREACH-5181 @IXOUTREACH-6180 @Campaign @P1 @Monitor
@DataFile=Campaigns
Feature: Story IXOUTREACH-6180 - Stop campaign - Monitor

  @Monitor @AC1 @TC1
  Scenario: Verify that The Stop option should be enabled/disabled when The campaign is running/stopped
    * Load test data: DataFile = Campaigns, Data = StopCampaignAC1_TC1
    * Create 1 campaign using API
    * Run a campaigns using API
    Given The Active Campaign monitor URL is hit
    Then Verify that the Stop option "enabled"
    When User click option stop campaign and click "Stop" button
    Then Verify that the Stop option "disabled" on Supervisor Dashboard

  @Monitor @AC2 @TC1
  Scenario: Verify confirmation message box should be displayed when The clicking stop option
    * Load test data: DataFile = MonitorActiveCampaign, Data = StopCampaignAC2_TC1
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    When User stop campaign with stop campaign option
    Then "Stop" confirmation box displays include two options Stop and Cancel

  @Monitor @AC3 @TC1
  Scenario: Verify the change in state from running to stopping
    * Load test data: DataFile = MonitorActiveCampaign, Data = StopCampaignAC3_TC1
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    When User click option stop campaign and click "Stop" button
    Then Campaign status "Stopping" on Active Campaigns page
    Then Stay in Active campaign view and confirmation box is not display in active campaign view

  @Monitor @AC5 @TC1
  Scenario: Verify that campaign will continue to display in the monitor with Running state when Cancel the confirmation
    * Load test data: DataFile = MonitorActiveCampaign, Data = StopCampaignAC5_TC1
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    When User click option stop campaign and click "Cancel" button
    Then Campaign status "Running" on Active Campaigns page
    Then Stay in Active campaign view and confirmation box is not display in active campaign view

  @Monitor @AC6 @TC1
  Scenario: Verify the change in state from stopping to completed
    * Load test data: DataFile = MonitorActiveCampaign, Data = StopCampaignAC6_TC1
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    When User click option stop campaign and click "Stop" button
    Then Campaign be kept status "Stopping" till campaign is completed
    And Campaign status is "Completed"

  @Monitor @AC6 @TC2
  Scenario: Verify the completed campaign not visible in active campaign view
    * Load test data: DataFile = MonitorActiveCampaign, Data = StopCampaignAC6_TC2
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    And User click option stop campaign and click "Stop" button
    When Campaign be kept status "Stopping" till campaign is completed
    Then Verify the completed campaign not visible in active campaign view