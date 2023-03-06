@IXOUTREACH-4072 @IXOUTREACH-6103 @IXOUTREACH-6477 @Campaign @P1
@DataFile=MonitorActiveCampaign
Feature: IXOUTREACH-6103 - Monitor Active Campaign - manual refresh


  @Monitor @AC1 @TC1 @Data=MONITORACTIVE_AC1_TC1
  Scenario: Verify active campaigns page get displayed without data
    Given The Active Campaign monitor URL is hit
    When There are no campaigns display on active campaigns page
    Then Active campaigns view title is displayed correctly
    And There is a empty message "Record list is empty"
    And The Active Campaign page displays correctly with all header table
    And The Active Campaign page displays basic search field, Refresh icon, Filter icon, summary section
    And The Active Campaign page not displays pagination

  @Monitor @AC2 @TC1 @Data=MONITORACTIVE_AC2_TC1
  @IgnoreInParallel
  Scenario: Verify Active Campaign statistics display correctly on active campaign monitor page summary
    * Create 12 campaign using API
    * Run 12 campaign using API
    Given The Active Campaign monitor URL is hit
    Then Active Campaign statistics display correctly with status campaign running, other and total
    And Pause 6 campaign using API and waiting campaigns are paused successfully
    Then Active Campaign statistics display correctly with status campaign running, other and total
    And Stop 4 campaign campaign using API
    Then Active Campaign statistics display correctly with status campaign running, other and total
    * Stop all campaigns for testing using API

  @Monitor @AC3 @TC1 @Data=ActiveCampaignAC3_TC1
  Scenario: Verify that the campaign should be displayed on active campaign page when the campaign is running
    * Create 1 campaign using API
    * Run 1 campaign using API
    Given The Active Campaign monitor URL is hit
    When The campaign is displayed on active campaign page when the campaign is "Running"
    And Pause 1 campaign using API and waiting campaigns are paused successfully
    Then Total number of records display correctly on active campaign page
    And Total number of attempts displayed correctly on active campaign page
    And Total number of attempts still pending to be attempted displayed correctly on active campaign page
    And Total Completed Contacts displayed correctly on active campaign page
    And Percent Completed display correctly follow record sent
    * Stop all campaigns for testing using API

  @Monitor @AC3 @TC2 @Data=ActiveCampaignAC3_TC2
  Scenario: Verify that the campaign should be displayed on active campaign page when the campaign is stopping
    * Create 1 campaign using API
    * Run 1 campaign using API
    Given The Active Campaign monitor URL is hit
    And  Stop 1 campaign campaign using API
    Then The campaign is displayed on active campaign page when the campaign is "Stopping"

  @Monitor @AC4 @TC1 @Data=MonitorCamp_AC4_TC1
  Scenario: Verify that the campaign should not be displayed on active campaign page when the campaign run completed
    * Create 3 campaign using API
    Given Run 3 campaign using API
    And The Active Campaign monitor URL is hit
    When Stop all campaigns and wait for the campaign to stop completely
    Then "The completed campaigns" which should not be display on active campaign page

  @Monitor @AC4 @TC2 @Data=MonitorCamp_AC4_TC2
  Scenario: Verify that user should not see campaigns which are not active
    * Create 3 campaign using API
    Given The Active Campaign monitor URL is hit
    Then "Campaign is not started" which should not be display on active campaign page

  @Monitor @AC5 @TC1 @Data=MonitorCamp_AC5_TC1
  @IgnoreInParallel
  Scenario: Verify that pagination work fine in active campaign page
    * Create 65 campaign using API
    * Run 65 campaign using API
    Given The Active Campaign monitor URL is hit
    And User able to see 65 campaign
    Then Page size value displays "10" by default
    Then Verify pagination with page size "10"
    Then Verify pagination with page size "20"
    Then Verify pagination with page size "50"
    Then Verify pagination with page size "100"
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC2 @Data=MonitorCamp_AC5_TC2
  Scenario: Verify that user will be back to default page size after clicking Refresh button
    * Create 12 campaign using API
    Given Run 12 campaign using API
    And The Active Campaign monitor URL is hit
    Then Change the page size to "20,50,100" values one by one then click refresh button then make sure the page size restore back to 10 as default
    * Stop all campaigns for testing using API


  @Monitor @AC5 @TC3 @Data=MonitorCamp_AC5_TC3
  Scenario: Verify that user will be back to default sort after clicking Refresh button
    * Create 12 campaign using API
    Given Run 12 campaign using API
    And The Active Campaign monitor URL is hit
    When Apply sort for each column and click refresh button then make sure the sort restore back to default
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC4
  Scenario: Verify failure toast message should not display when input special character on text box search
    Given The Active Campaign monitor URL is hit
    Then Failure toast message is not display when user try entering the characters "`~!@#$%^&*()-_+=[]\\{}|;':\",./<>? " in text box "basic" search
    And Failure toast message is not display when user try entering the characters "`~!@#$%^&*()-_+=[]\\{}|;':\",./<>? " in text box "advanced" search

  @Monitor @AC5 @TC5 @Data=MONITORACTIVE_AC5_TC5
  Scenario: Verify campaign completed should not display on active campaign when user using basic search
    * Create 12 campaign using API
    * Run 12 campaign using API
    Given The Active Campaign monitor URL is hit
    Then Campaign completed is not display on active campaign when user using basic search
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC6 @Data=MONITORACTIVE_AC5_TC6
  @IgnoreInParallel
  Scenario: Verify Basic search display correctly and works properly on Monitor active Campaign page
    * Create 12 campaign using API
    * Run 12 campaign using API
    Given The Active Campaign monitor URL is hit
    Then Basic search display on Monitor active Campaign page
    And Basic search works properly on Monitor active Campaign page
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC7 @Data=Monitor_AC5_TC7
  Scenario: The default sort for contact browser page is by Start Time and in descending order
    * Create 8 campaign using API
    Given The Active Campaign monitor URL is hit
    And Run 8 campaign using API
    When The campaign is sorted base on Start Time
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC8
  Scenario: Verify the sort icon displays correctly on the header panel when clicking on the Name column
    Given The Active Campaign monitor URL is hit
    When There are no campaigns display on active campaigns page
    Then User clicks on header column "Name", the sorting icons change with each click

  @Monitor @AC5 @TC9
  Scenario: Verify the sort icon displays correctly on the header panel when clicking on the Start Time column without data
    Given The Active Campaign monitor URL is hit
    When There are no campaigns display on active campaigns page
    Then User clicks on header column "Start Time", the sorting icons change with each click

  @Monitor @AC5 @TC10 @Data=Monitor_AC5_TC10
  Scenario: Verify the sort icon displays correctly on the header panel when clicking on the Name column with campaign running
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    When Run a campaigns using API
    Then User clicks on header column "Name", the sorting icons change with each click
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC11 @Data=Monitor_AC5_TC11
  Scenario: Verify the sort icon displays correctly on the header panel when clicking on the Start Time column with campaign running
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    When Run a campaigns using API
    Then User clicks on header column "Start Time", the sorting icons change with each click
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC12 @Data=Monitor_AC5_TC12
  @IgnoreInParallel
  Scenario: Verify sort function work correctly with Name column
    * Create 6 campaign using API
    * Run 6 campaign using API
    Given The Active Campaign monitor URL is hit
    When Pause 6 campaign using API and waiting campaigns are paused successfully
    Then The sort function works correctly when the user tries to click the "Name" column a few times
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC13 @Data=Monitor_AC5_TC13
  Scenario: Verify sort function work correctly with Start Time column
    * Create 10 campaign using API
    * Run 10 campaign using API
    Given The Active Campaign monitor URL is hit
    Then The sort function works correctly when the user tries to click the "Start Time" column a few times
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC14 @Data=Monitor_AC5_TC14
  Scenario: Verify that the sort function for the Name column works correctly across pages.
    * Create 24 campaign using API
    * Run 24 campaign using API
    Given The Active Campaign monitor URL is hit
    When Pause 6 campaign using API and waiting campaigns are paused successfully
    Then The user is implementing sorting with the "Name" Column, going to each page and verifying that the sort still works for each page
    * Stop all campaigns for testing using API

  @Monitor @AC5 @TC15 @Data=Monitor_AC5_TC15
  Scenario: Verify that the sort function for the Start Time column works correctly across pages.
    * Create 24 campaign using API
    * Run 24 campaign using API
    Given The Active Campaign monitor URL is hit
    Then The user is implementing sorting with the "Start Time" Column, going to each page and verifying that the sort still works for each page
    * Stop all campaigns for testing using API