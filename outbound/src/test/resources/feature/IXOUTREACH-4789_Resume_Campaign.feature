@IXOUTREACH-7238 @IXOUTREACH-4789
Feature: Story IXOUTREACH-4789 Resume Campaign

##############################################################################
##############################################################################
##############################################################################

  @Monitor @P1
  Scenario: Verify that the confirmation message should be displayed when user select resume option
    * Load test data: DataFile = MonitorActiveCampaign, Data = ResumeCampaign_ConfirmMessage
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    And User click option "Pause" campaign and click "Pause" button on confirmation box
    Then Waiting Campaign status is "Paused" and click to "Resume" option on Campaign
    Then Verify information in confirmation box is correct when clicking "Resume" button
    * Clean up all data for testing

  @Monitor @P1
  Scenario: Verify that the Resume button should be disabled when campaign is in running state
    * Load test data: DataFile = MonitorActiveCampaign, Data = resume_verifyBtn
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    And Verify the status of campaign is "Running"
    Then Verify "Resume" option is disabled
    * Clean up all data for testing

  @Monitor @P1
  Scenario: Verify campaign will remain in Paused state and continue to be displayed in Monitor page when click Cancel in confirmation option
    * Load test data: DataFile = MonitorActiveCampaign, Data = resume_verifyCancel
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    And Verify the status of campaign is "Running"
    When User click option "Pause" campaign and click "Pause" button on confirmation box
    Then Verify the status of campaign is "Paused"
    When User click option "Resume" campaign and click "Cancel" button on confirmation box
    Then Verify the status of campaign is "Paused"
    * Clean up all data for testing

  @Monitor @P1
  Scenario: Verify that the campaign should be changed to Running status when user click option Resume on confirmation message
    * Load test data: DataFile = MonitorActiveCampaign, Data = ResumeCampaign_ConfirmMessage_02
    * Create 5 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 5 campaign using API
    And User click option "Pause" for 2 campaign and verify status of them is "Paused"
    Then Get status for all campaign after "pause"
    And User click option "Resume" for 2 campaign and verify status of them is "Running"
    Then Get status for all campaign after "resume"
    Then Running state count should be incremented and Pause state count should be decremented
    * Clean up all data for testing

  @Monitor @P1
  Scenario: Verify campaign status should get transition correctly from each state
    * Load test data: DataFile = MonitorActiveCampaign, Data = Campaign_State_Transition
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    When Campaign already finish at least 25 records
    Then User click option "Pause" campaign and click "Pause" button on confirmation box
    And Verify the status of campaign is "Paused"
    And User click option "Resume" campaign and click "Resume" button on confirmation box
    And Verify the status of campaign is "Running"
    Then Campaign is stated successfully and there are 100 SMS that is sent
    And Monitor campaign status running completed
    * Clean up all data for testing


  @Monitor @P1
  Scenario: Verify after resume campaign is not dialing already dialed records
    * Load test data: DataFile = MonitorActiveCampaign, Data = Campaign_Dialed_Records
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 1 campaign using API
    When Campaign already finish at least 20 records
    Then Verify the status of campaign is "Running"
    When User click option "Pause" campaign and click "Pause" button on confirmation box
    Then Action menu option "Pause" will be disappeared after user select action
    Then Verify the status of campaign is "Paused"
    Then Verify campaign status after paused
    When User click option "Resume" campaign and click "Resume" button on confirmation box
    Then Action menu option "Resume" will be disappeared after user select action
    Then Verify the status of campaign is "Running"
    Then Verify campaign process all contacts of contact list
    Then Verify after resume campaign do not dial already dialed records
    * Clean up all data for testing
