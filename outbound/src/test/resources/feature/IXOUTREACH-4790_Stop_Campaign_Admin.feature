@IXOUTREACH-4027 @IXOUTREACH-4790 @Campaign @P1
@DataFile=Campaigns
Feature: Story IXOUTREACH-4790 - Stop campaign - Admin

  @Campaign @AC1 @TC1
  Scenario: Verify that The Stop option should be enabled when The campaign is running
    * Load test data: DataFile = Campaigns, Data = StopCampaignAC1_TC1
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Verify that the Stop option "enabled"
    * Clean up all data for testing

  @Campaign @AC1 @TC2
  Scenario: Verify that The Stop option should be disabled when The campaign is not running
    * Load test data: DataFile = Campaigns, Data = StopCampaignAC1_TC2
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then Verify that the Stop option "disabled"
    * Clean up all data for testing

  @Campaign @AC1 @TC3
  Scenario: Verify that The Start option should be disabled when The campaign is stopping
    * Load test data: DataFile = Campaigns, Data = StopCampaignAC1_TC3
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    And User click option stop campaign and click "Stop" button
    Then Verify that the Start option is disabled
    * Clean up all data for testing

  @Campaign @AC1 @TC4
  Scenario: Verify that a toast message is displayed when the campaign is stopping and click stop button one more time.
    * Load test data: DataFile = Campaigns, Data = StopCampaignAC1_TC4
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    And The campaign is stopping and click stop button one more time
    Then Verify that a failure toast message is displayed
    * Clean up all data for testing

  @Campaign @AC1 @TC5
  Scenario: Verify that The Stop option should be displayed in order Edit-Start-Stop
    * Load test data: DataFile = Campaigns, Data = StopCampaignAC1_TC5
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When User selects one of the campaigns
    Then Verify that the Stop option displayed in order Edit-Start-Stop
    * Clean up all data for testing

  @Campaign @AC2 @TC1
  Scenario: Verify that confirmation message displays correctly when user selects Stop campaign option
    * Load test data: DataFile = Campaigns, Data = StopCampAC2_TC1
    * Create 1 campaign using API
    Given The Campaign management URL is hit
    And Start campaign with start campaign option
    When Stop campaign with stop campaign option
    Then "Stop" confirmation box displays include two options Stop and Cancel
    * Clean up all data for testing

  @Campaign @AC2 @TC2
  Scenario: Verify that campaign will be stopped when user click Stop option on confirmation message box
    * Load test data: DataFile = Campaigns, Data = StopCampAC2_TC2
    * Create 1 campaign using API
    Given The Campaign management URL is hit
    And Start campaign with start campaign option
    When User click option stop campaign and click "Stop" button
    Then Campaign status is "Stopping"
    * Clean up all data for testing

  @Campaign @AC2 @TC3
  Scenario: Verify that campaign will be remained Running when user click Cancel option on confirmation message box
    * Load test data: DataFile = Campaigns, Data = StopCampAC2_TC3
    * Create 1 campaign using API
    Given The Campaign management URL is hit
    And Start campaign with start campaign option
    When User click option stop campaign and click "Cancel" button
    Then Campaign status is "Running"
    * Clean up all data for testing

  @Campaign @AC3 @TC1
  Scenario: Verify that campaign status get transition from Running to Stopping after user select Stop campaign option till the campaign is Completed
    * Load test data: DataFile = Campaigns, Data = StopCampAC3_TC1
    * Create 1 campaign using API
    Given The Campaign management URL is hit
    And Start campaign with start campaign option
    Then Action menu option "Start" will be disappeared after user select action
    And Campaign status is "Running"
    When User click option stop campaign and click "Stop" button
    Then Action menu option "Stop" will be disappeared after user select action
    When The Active Campaign monitor URL is hit
    And Search campaign "StopCampAC3_TC1"
    Then Campaign status get transition to "Stopping" till campaign is completed
    * Clean up all data for testing

  @Campaign @AC3 @TC2
  Scenario: Verify that In-Progress message continue to be displayed in Last Executed column after user select Stop campaign option till the campaign is Completed
    * Load test data: DataFile = Campaigns, Data = StopCampAC3_TC2
    * Create 1 campaign using API
    Given The Campaign management URL is hit
    And Start campaign with start campaign option
    When User click option stop campaign and click "Stop" button
    Then In-progress status continue to be displayed in Last Executed column till campaign is completed
    And Clean up all data for testing

  @Campaign @AC4 @TC1 @Data=STOPCAMPAIGN_AC4_TC1
  Scenario: Verify toast message should display "Stop request accepted for campaign <Campaign Name>" when user confirm stop campaign
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Run a campaigns using API
    When User click option stop campaign and click "Stop" button
    Then Verify the toast message is displayed correctly
    * Clean up all data for testing

  @Campaign @AC4 @TC2 @Data=STOPCAMPAIGN_AC4_TC2
  Scenario: Verify campaign job status should display "Stopping" until it finishes the outstanding SMS
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Run a campaigns using API
    When User click option stop campaign and click "Stop" button
    Then Verify campaign job status should display "Stopping" until it finishes the outstanding SMS
    * Clean up all data for testing

  @Campaign @AC4 @TC3 @Data=STOPCAMPAIGN_AC4_TC3
  Scenario: Verify no new SMS should be placed once it enters to "Stopping" state
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Run a campaigns using API
    When User click option stop campaign and click "Stop" button
    Then Verify no new SMS should be placed once campaign enters to "Stopping" state
    * Clean up all data for testing

  @Campaign @AC5 @TC1 @Data=STOPCAMPAIGN_AC5_TC1
  Scenario: Verify campaign job state should change from “Stopping” state to “Completed” state when user stop campaign successful
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Run a campaigns using API
    When User click option stop campaign and click "Stop" button
    Then Campaign job state should change from “Stopping” state to “Completed” state
    * Clean up all data for testing

  @Campaign @AC5 @TC2 @Data=STOPCAMPAIGN_AC5_TC2
  Scenario: Verify Completion code should display correctly with some SMS sent
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Run a campaigns using API
    When User click option stop campaign and click "Stop" button
    And Campaign job state should change from “Stopping” state to “Completed” state
    Then Verify the SMS status
      | SMS_Queued | SMS_Sent | SMS_Delivered | SMS_Failed | SMS_Rejected | SMS_Couldnot_Send | SMS_Couldnot_Deliver |
    * Clean up all data for testing

  @Campaign @AC5 @TC3 @Data=STOPCAMPAIGN_AC5_TC3
  Scenario: Verify User can start campaign  again when campaign stopped
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Run a campaigns using API
    When User click option stop campaign and click "Stop" button
    Then User can start campaign again
    * Clean up all data for testing

  @Campaign @AC5 @TC4 @Data=STOPCAMPAIGN_AC5_TC4
  Scenario: Verify  User can edit and save successful when campaign stopped
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Run a campaigns using API
    When User click option stop campaign and click "Stop" button
    Then User can edit and save campaign successful when campaign stopped
    * Clean up all data for testing
