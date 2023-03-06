@IXOUTREACH-4785 @IXOUTREACH-4027 @Campaign @P1
Feature: Story IXOUTREACH-4785 - Start campaign without pacing
  This feature for JIRA ID IXOUTREACH-4785 - Start campaign without pacing

  @Campaign @AC1 @TC1 @P1
  Scenario: Verify start campaign option display on option menu dropdown
    * Load test data: DataFile = Campaigns, Data = DataAC1_TC1
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then Start campaign option display on option menu dropdown
    And Clean up all data for testing

  @Campaign @AC2 @TC1 @P1
  Scenario: Verify "Last Executed" column should display on header campaign landing page
    Given The Campaign management URL is hit to view the existing campaigns
    Then "Last Executed" column displayed on header of campaign landing page

  @Campaign @AC2 @TC2 @P1
  Scenario: Verify that status In Progress should be display in "Last Executed" Column if campaign is started successfully
    * Load test data: DataFile = Campaigns, Data = DataAC2_TC2
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Status campaign display "In Progress" in "Last Executed" column campaign landing page
    And Clean up all data for testing

  @Campaign @AC2 @TC3 @P1
  Scenario: Verify start campaign option disable when campaign running
    * Load test data: DataFile = Campaigns, Data = DataAC2_TC3
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Start campaign option disable when campaign running
    And Clean up all data for testing

  @Campaign @AC2 @TC4 @P1
  Scenario: Verify that toast message 'Campaign name is started with Job ID' should be displayed when campaign is started successfully
    * Load test data: DataFile = Campaigns, Data = DataAC2_TC4
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Toast message "Campaign name is started successfully." displayed correctly when run campaign
    And Clean up all data for testing

  @Campaign @AC2 @TC5 @P1
  Scenario: Verify that user can started successfully with type infinite
    * Load test data: DataFile = Campaigns, Data = DataAC2_TC5
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Toast message "Campaign name is started successfully." displayed correctly when run campaign
    And Status campaign display "In Progress" in "Last Executed" column campaign landing page
    And Clean up all data for testing

  @Campaign @AC2 @TC6 @P1
  Scenario: Verify start campaign option enable when campaign running completed
    * Load test data: DataFile = Campaigns, Data = DataAC2_TC6
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Start campaign option enable when campaign running completed
    And Clean up all data for testing

  @Campaign @AC3 @TC1 @P1
  Scenario: Verify failure toast message should display when start campaign and associated contact list doesn't exist
    * Load test data: DataFile = Campaigns, Data = DataAC3_TC1
    Given Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Given Delete contact list that associated with campaign
    When User click start campaign option
    Then Failure toast message "Failed to start campaign, associated contact list does not exist." displayed correctly when run campaign
    And Clean up all data for testing

  @Campaign @AC3 @TC2 @P1
  Scenario: Verify failure toast message should display when start campaign and associated strategy doesn't exist
    * Load test data: DataFile = Campaigns, Data = DataAC3_TC2
    Given Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Delete campaign strategy that associated with campaign
    When User click start campaign option
    Then Failure toast message "Failed to start campaign, associated strategy does not exist." displayed correctly when run campaign
    And Clean up all data for testing

  @Campaign @AC3 @TC3 @P1
  Scenario: Verify that status in "Last Executed" Column should change instantly when running a campaign with 0 contact
    * Load test data: DataFile = Campaigns, Data = DataAC3_TC3
    Given Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Toast message "Campaign name is started successfully." displayed correctly when run campaign
    And Status in "Last Executed" column change instantly when run campaign successful
    And Clean up all data for testing
