@IXOUTREACH-6095 @DataFile=Campaigns @P1
@Campaign
Feature: IXOUTREACH-6095_Start_Campaign_With_Pacing

  @AC2 @TC1 @P1 @Data=CPPacingAC2_TC1
  Scenario: Verify campaign should work correctly follow Hour Pacing as defined in strategy
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Campaign works correctly follow "Hour" Pacing Parameters defined in its strategy
    And Clean up all data for testing

  @AC2 @TC2 @Data=CPPacingAC2_TC2
  Scenario: Verify campaign should work correctly follow Minute Pacing as defined in strategy
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Campaign works correctly follow "Minute" Pacing Parameters defined in its strategy
    And Clean up all data for testing

  @AC2 @TC3 @Data=CPPacingAC2_TC3
  Scenario: Verify campaign should work correctly follow Second Pacing as defined in strategy
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Campaign works correctly follow "Second" Pacing Parameters defined in its strategy
    And Clean up all data for testing

  @AC2 @TC4 @Data=CPPacingAC2_TC4
  Scenario: Verify campaign should be pick up the contacts and send SMS to default address list in the contact
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Campaign sent SMS to default address list in the contact
    And Clean up all data for testing

  @AC2 @TC5 @IgnoreInDailyRun @Data=CPPacingAC2_TC5
  Scenario: Verify that starting campaign successfully with contact list with 10k record
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Run a campaigns using API
    Then Campaign is stated successfully and there are 10000 SMS that is sent
    And Clean up all data for testing

  @AC4 @Data=IXOUTREACH-6095_TC1
  Scenario: Verify sms status when sending correctly
    *   The Campaign management URL is hit to view the existing campaigns
    Given Create 1 campaign using API
    When Run a campaigns using API
    And Monitor campaign status running completed
    Then Verify the SMS status
      | SMS_Queued | SMS_Sent | SMS_Delivered | SMS_Failed | SMS_Rejected | SMS_Couldnot_Send | SMS_Couldnot_Deliver |
    And Clean up all data for testing

  @AC4 @Data=IXOUTREACH-6095_TC2
  Scenario: Verify that result ratio run SMS campaign should be display correctly on attempt output
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Result ratio run SMS campaign should be display correctly on attempt output
    And Clean up all data for testing

  @AC5 @TC1 @Data=campaignPacing_AC5_TC1
  Scenario: Verify the sort icon should be displayed after clicking sort on the Last Executed column on the Campaign Manager landing page
    Given The Campaign management URL is hit
    And There are at least 5 campaign was added and run completed
    Then  User Sorting and the sort icon should be displayed on the Last Executed column
    And Clean up all data for testing

  @AC5 @TC2 @Data=campaignPacing_AC5_TC2 @IgnoreInParallel
  Scenario: Verify that sort function should be work fine on the Last Executed column on the Campaign Manager landing page
    Given The Campaign management URL is hit
    And There are at least 5 campaign was added and run completed
    Then  User should be able to sorting on the Last Executed column
    And Clean up all data for testing

  @AC5 @TC3
  Scenario: Verify list operator display correctly for Last Executed attribute
    Given The Campaign management URL is hit
    Then Verify list Operators on dropdown option displayed correctly
      | Attribute | Last Executed |
      | Operator  | <,>,Between   |

  @AC5 @TC4-TC12 @Data=campaignPacing_AC5 @IgnoreInParallel
  Scenario: Verify that user can search Campaign by Last Executed time with operators
    Given The Campaign management URL is hit
    And There are at least 5 campaign was added and run completed
    Then Advance search on campaign manager page and verify advance search result
      | Column Name   | Input type    | Operator | Value                                                       |
      | Last Executed | Input         | >        | campaign:pacingCampaign_AC5_3                               |
      | Last Executed | Select        | >        | campaign:pacingCampaign_AC5_3                               |
      | Last Executed | Input         | <        | campaign:pacingCampaign_AC5_3                               |
      | Last Executed | Select        | <        | campaign:pacingCampaign_AC5_3                               |
      | Last Executed | Select,Select | Between  | campaign:pacingCampaign_AC5_1,campaign:pacingCampaign_AC5_5 |
      | Last Executed | Input,Input   | Between  | campaign:pacingCampaign_AC5_1,campaign:pacingCampaign_AC5_5 |
      | Last Executed | Select,Input  | Between  | campaign:pacingCampaign_AC5_1,campaign:pacingCampaign_AC5_5 |
      | Last Executed | Input,Select  | Between  | campaign:pacingCampaign_AC5_1,campaign:pacingCampaign_AC5_5 |
    And Clean up all data for testing


  @AC5 @TC5 @Data=campaignPacing_AC5_TC5 @IgnoreInParallel
  Scenario Outline: Verify that a error message will be display when input end time less than start time
    Given The Campaign management URL is hit
    And There are at least 2 campaign was added and run completed
    When Advance search for field "<Column Name>" by "<Operator>" with "<Input type>" text "<Value>" on contact list page
    Then Verify that a error message "Invalid dates provided. From date should be less than to date." will be display
    And Clean up all data for testing

    @AC5 @TC5
    Examples:
      | Column Name   | Operator | Value                                                               | Input type    |
      | Last Executed | Between  | campaign:campaignPacing_AC5_TC5_2,campaign:campaignPacing_AC5_TC5_1 | Select,Select |


  @AC3 @TC1-TC11 @Data=startCampAC3 @IgnoreInParallel
  Scenario: Verify that a event is generated on attempts node
    Given The Campaign management URL is hit
    And There are at least 1 campaign was added and run completed
    Then The event is generated on attempts node
      | Event                |
      | attemptId            |
      | jobId                |
      | contactList          |
      | campaign             |
      | address              |
      | contactId            |
      | attemptTime          |
      | channelType          |
      | completionCode       |
      | systemCompletionCode |
      | lastDispositionTime  |
    And Clean up all data for testing
