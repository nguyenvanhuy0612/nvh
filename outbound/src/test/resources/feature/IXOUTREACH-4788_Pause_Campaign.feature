@IXOUTREACH-4072 @IXOUTREACH-4788 @P1 @Monitor
@DataFile=MonitorActiveCampaign
Feature: IXOUTREACH-4788 Pause_campaign

  @AC1 @TC1 @Data=PauseCamp_AC1_TC1
  Scenario: Verify that Pause action is displayed for each campaign option menu
    Given Create and run 1 campaign using API
    And The Active Campaign monitor URL is hit
    Then Verify the status of campaign is "Running"
    And User search and click on "Pause" option of campaign then click "Pause" button on confirmation box
    Then Verify the status of campaigns and buttons change correctly
      | CampaignStatus | ButtonDisable | ButtonEnable |
      | Pausing        | Pause         |              |
    Then Verify the status of campaigns and buttons change correctly
      | CampaignStatus | ButtonDisable | ButtonEnable |
      | Paused         | Pause         | Resume       |
    And User search and click on "Resume" option of campaign then click "Resume" button on confirmation box
    Then Verify the status of campaigns and buttons change correctly
      | CampaignStatus | ButtonDisable | ButtonEnable |
      | Running        | Resume        | Pause        |

  @AC2 @TC1 @Data=PauseCamp_AC2_TC1
  Scenario: Verify that information on pause confirmation display correctly after clicking pause option at running campaign
    Given Create 1 campaign using API
    And Run 1 campaign using API
    And The Active Campaign monitor URL is hit
    And Select pause option on option menu dropdown
    Then "Pause" confirmation box displays include two options Pause and Cancel

  @AC3 @TC1 @Data=PauseCamp_AC3_TC1
  Scenario: Verify that campaign state is change from Running to Pausing after clicking Pause in pause confirmation
    Given Create 1 campaign using API
    And Run 1 campaign using API
    And The Active Campaign monitor URL is hit
    When Select pause option on option menu dropdown and click "Pause" button
    Then Campaign state is changed from Running to Pausing

  @AC3 @TC2 @Data=PauseCamp_AC3_TC2
  Scenario: Verify that toast message would be display once the campaign is paused
    Given Create 1 campaign using API
    And Run 1 campaign using API
    And The Active Campaign monitor URL is hit
    When Select pause option on option menu dropdown and click "Pause" button
    Then Pause campaign toast message displays in monitor page

  @AC4 @TC1 @Data=PauseCampaign_AC4_TC1
  Scenario: Verify that campaign state is kept Running after clicking Cancel in pause confirmation
    * Create and run 1 campaign using API
    Given The Active Campaign monitor URL is hit
    When Select "Pause" option on option menu dropdown and Click "Cancel" button on pause confirmation box
    Then Campaign status "Running" on Active Campaigns page
    Then Stay in Active campaign view and confirmation box is not display in active campaign view

  @AC4 @TC2 @Data=PauseCampaign_AC4_TC2
  Scenario: Verify that user pause campaign successfully which is selected Cancel in Pause confirmation before
    * Create and run 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Select "Pause" option on option menu dropdown and Click "Cancel" button on pause confirmation box
    When Select "Pause" option on option menu dropdown and Click "Pause" button on pause confirmation box
    Then Campaign status "Paused" on Active Campaigns page
    Then Stay in Active campaign view and confirmation box is not display in active campaign view

  @AC6 @TC1 @Data=PauseCampaign_AC6_TC1
  Scenario: Verify that campaign state stay Pausing until it finishes the outstanding
    Given Create and run 1 campaign using API
    When The Active Campaign monitor URL is hit
    Then Campaign state stay Pausing until it finishes the outstanding

  @AC6 @TC2 @Data=PauseCampaign_AC6_TC2
  Scenario: Verify that no new SMS be placed once entering to Pausing state
    Given Create and run 1 campaign using API
    When The Active Campaign monitor URL is hit
    Then No new SMS be placed once entering to Pausing state

  @AC7 @TC1 @Data=PauseCamp_AC7_TC1
  Scenario: Verify that campaign state is changed to Pausing, Paused what should be displayed in Monitor
    Given Create 1 campaign using API
    And Run 1 campaign using API
    And The Active Campaign monitor URL is hit
    When Select pause option on option menu dropdown and click "Pause" button
    Then Pausing,Paused status campaign should be display in Monitor

  @AC7 @TC2 @Data=PauseCamp_AC7_TC2 @IgnoreInParallel
  Scenario: Verify that Other state count should be decremented and Paused state count should be incremented when campaign state is transition to Paused
    Given Create 1 campaign using API
    And Run 1 campaign using API
    And The Active Campaign monitor URL is hit
    Then Other state count decrement and Paused state count increment when campaign state is transition to Paused

  @AC8 @TC1 @Data=PauseCampaign_AC8_TC1
  Scenario: Verify that user can stop campaign successfully while it is in Paused state
    * Create and run 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Select "Pause" option on option menu dropdown and Click "Pause" button on pause confirmation box
    When User click option stop campaign and click "Stop" button
    Then Campaign is stopped successfully

  @AC8 @TC2 @Data=PauseCampaign_AC8_TC2 @IgnoreInParallel
  Scenario: Verify that Paused state count should be decremented when campaign state is changed from Paused to Stopped
    * Create and run 1 campaign using API
    Given The Active Campaign monitor URL is hit
    When Select "Pause" option on option menu dropdown and Click "Pause" button on pause confirmation box
    Then Paused state count is decremented when campaign state is changed from Paused to Stopped