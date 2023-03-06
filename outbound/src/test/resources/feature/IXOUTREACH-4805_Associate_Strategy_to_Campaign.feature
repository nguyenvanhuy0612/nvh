@IXOUTREACH-4805
Feature: This feature for ID IXOUTREACH-4805 Associate strategy to campaign
##############################################################################
##############################################################################
  @Campaign @AC1 @TC01 @IgnoreInDailyRun
  Scenario: Verify associate strategy field shown on create campaign page and dropdown shown maximum 100 value
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp01
    And Create contact list using API
    And Create 124 campaign strategies using API to check associate to campaign
    When User click "New Campaign" button
    Then Associate strategy field display on create campaign page
    Then Dropdown strategy list displayed on strategy field
    And Dropdown strategy list displayed max 100 value
    And Delete campaign strategies was created - 124 to check associtate to campaign
    And Delete contact list was created

  @Campaign  @AC1 @TC02
  Scenario: Verify user cannot save the campaign without providing strategy
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp02
    And Create contact list using API
    When Click Add New campaign button, fill name and description
    And Associate Contact list to campaign
    And User click "Save" button
    Then User can not create campaign without associate strategy
    And Delete contact list was created

  @Campaign @AC1 @TC03 @IgnoreInDailyRun
  Scenario: Verify The response shouldn't exceed more than 2 seconds while populating the data (2000 Strategies) in dropdown
    Given Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp03
    And The Campaign management URL is hit
    And Create 2000 campaign strategies using API to check associate to campaign
    When Click Add New campaign button, fill name and description
    And In the Strategy Configuration, click select box to open the dropdown menu
    Then Response time to display Strategy list should not exceed 2 seconds
    And Delete campaign strategies was created - 2000 to check associtate to campaign

  @Campaign @AC1 @TC04
  Scenario: Verify that strategy list drop-down result is disappeared when the user selected a stratregy
    Given Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp04
    And The Campaign management URL is hit
    And Create 5 campaign strategies using API to check associate to campaign
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Strategy to campaign
    Then Verify drop-down result is disappeared when the user selected a stratregy
    And Delete campaign strategies was created - 5 to check associtate to campaign
    And Delete campaign strategies was created - 1 to check associtate to campaign

  @Campaign @AC4 @TC14
  Scenario:Verify campaign created successful displaying the success toast message when created campaign successful with associate strategy
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp14
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And User click "Save" button
    Then Verify toast message create campaign successfully
    And Clean up data create campaign

  @Campaign @AC4 @TC19
  Scenario:Verify user redirected to campaign landing page and Strategy name displayed as part of campaign list after created campaign successfully
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp19
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And User click "Save" button
    Then User should be redirected to campaign landing page
    And Strategy name displayed as part of campaign list
    And Clean up data create campaign

  @Campaign @AC4 @TC20
  Scenario:Verify newly campaign should be at top of the list after created campaign successfully
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp20
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And User click "Save" button
    Then User should be redirected to campaign landing page
    And Newly created campaign should be displayed at top of the lists
    And Clean up data create campaign

  @Campaign @AC5 @TC15
  Scenario:Verify campaign update successful displaying the success toast message when update campaign with associate strategy
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp15
    And Create contact list using API
    And Create multiple campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign Strategy
    And Update Associate Strategy to campaign
    And User click "Save" button
    Then Verify toast message updated campaign successfully
    And Clean up data create campaign

  @Campaign @AC5 @TC16
  Scenario:Verify user redirected to campaign landing page and Strategy name displayed as part of campaign list after update campaign successfully
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp16
    And Create contact list using API
    And Create multiple campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign Strategy
    And Update Associate Strategy to campaign
    And User click "Save" button
    Then User should be redirected to campaign landing page
    And Strategy name displayed as part of campaign list
    And Clean up data create campaign

  @Campaign @AC5 @TC22 @IgnoreInParallel
  Scenario:Verify newly campaign should be at top of the list after updated campaign successfully
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp22
    And Create contact list using API
    And Create multiple campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign Strategy
    And Update Associate Strategy to campaign
    And User click "Save" button
    Then User should be redirected to campaign landing page
    And Newly created campaign should be displayed at top of the lists
    And Clean up data create campaign

  @Campaign @AC5 @TC23
  Scenario:Verify user cannot update campaign without associate strategy
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp23
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign Strategy
    And Clear Associate Strategy to campaign
    Then User click "Save" button
    Then User can not update campaign without associate strategy
    And Clean up data create campaign

  @Campaign @AssociateStrategyToCmp @AC2 @TC07
  Scenario: Verify the drop-down box to select strategy is empty in the 1st time load create strategy page
    Given Load testcase data: DataFile = CampaignStrategy, Data = AssociateStrategyToCmp_AC2_TC01_PrepareData
    And Create multiple campaign strategies using API to associate to campaign
    And The Campaign management URL is hit
    When User click "New Campaign" button
    Then Verify The Select Campaign Strategy field is empty
    And Delete Strategies after tested

  @Campaign @AssociateStrategyToCmp @AC2 @TC09
  Scenario: Verify the drop-down box to select strategy is displayed
    Given Load testcase data: DataFile = CampaignStrategy, Data = AssociateStrategyToCmp_AC2_TC03_PrepareData
    And Create multiple campaign strategies using API to associate to campaign
    When The Campaign management URL is hit
    And User click "New Campaign" button
    And In the Strategy Configuration, click select box to open the dropdown menu
    Then Verify the dropdown menu to select strategy is displayed
    And Delete Strategies after tested

  @Campaign @AssociateStrategyToCmp @AC2 @TC10 @IgnoreInParallel
  Scenario: Verify the strategies are sorted by strategy name ascending order in the drop-down box
    Given Load testcase data: DataFile = CampaignStrategy, Data = AssociateStrategyToCmp_AC2_TC04_PrepareData
    And Create multiple campaign strategies using API to associate to campaign
    And The Campaign management URL is hit
    When User click "New Campaign" button
    And In the Strategy Configuration, click select box to open the dropdown menu
    Then Verify the dropdown menu to select strategy is displayed
    And The strategies is showed fully in the dropdown
    And Strategies display correctly in ascending order name in dropdown
    And Delete Strategies after tested

  @Campaign @AssociateStrategyToCmp @AC2 @TC17 @IgnoreInParallel
  Scenario: Verify the drop-down box to select strategy shows fully strategies in DB
    Given Load testcase data: DataFile = CampaignStrategy, Data = AssociateStrategyToCmp_AC2_TC05_PrepareData
    And Create multiple campaign strategies using API to associate to campaign
    And The Campaign management URL is hit
    When User click "New Campaign" button
    And In the Strategy Configuration, click select box to open the dropdown menu
    Then Verify the dropdown menu to select strategy is displayed
    And The strategies is showed fully in the dropdown
    And Delete Strategies after tested

  @Campaign @AssociateStrategyToCmp @AC3 @TC11
  Scenario: Verify user can search the strategy by the strategy name in drop-down box
    Given Load testcase data: DataFile = CampaignStrategy, Data = AssociateStrategyToCmp_AC3_TC01_PrepareData
    And Create multiple campaign strategies using API to associate to campaign
    And The Campaign management URL is hit
    When User click "New Campaign" button
    And In the Strategy Configuration, click select box to open the dropdown menu
    And Search strategy in Select Campaign Strategy field
      | DataFile | CampaignStrategy                |
      | Data     | AssociateStrategyToCmp_AC3_TC01 |
    Then Verify the result searching in dropdown box is correct
      | DataFile | CampaignStrategy                |
      | Data     | AssociateStrategyToCmp_AC3_TC01 |
    And Delete Strategies after tested

  @Campaign @AssociateStrategyToCmp @AC3 @TC12
  Scenario: Verify the dropdown box to select strategy is null if searching incorrect strategy name
    Given Load testcase data: DataFile = CampaignStrategy, Data = AssociateStrategyToCmp_AC3_TC02_PrepareData
    And Create multiple campaign strategies using API to associate to campaign
    And The Campaign management URL is hit
    When User click "New Campaign" button
    And In the Strategy Configuration, click select box to open the dropdown menu
    And Search strategy in Select Campaign Strategy field
      | DataFile | CampaignStrategy                |
      | Data     | AssociateStrategyToCmp_AC3_TC02 |
    Then Verify the result searching in dropdown box is correct
      | DataFile | CampaignStrategy                |
      | Data     | AssociateStrategyToCmp_AC3_TC02 |
    And Delete Strategies after tested

  @Campaign @AssociateStrategyToCmp @AC3 @TC13
  Scenario: Verify user can search the strategy by name multi times in the drop-down box
    Given Load testcase data: DataFile = CampaignStrategy, Data = AssociateStrategyToCmp_AC3_TC03_PrepareData
    And Create multiple campaign strategies using API to associate to campaign
    And The Campaign management URL is hit
    When User click "New Campaign" button
    And In the Strategy Configuration, click select box to open the dropdown menu
    And Search strategy in Select Campaign Strategy field
      | DataFile | CampaignStrategy                  |
      | Data     | AssociateStrategyToCmp_AC3_TC03_1 |
    Then Verify the result searching in dropdown box is correct
      | DataFile | CampaignStrategy                  |
      | Data     | AssociateStrategyToCmp_AC3_TC03_1 |
    When Clear the search box of "Select Campaign Strategy" field
    When Search strategy in Select Campaign Strategy field
      | DataFile | CampaignStrategy                  |
      | Data     | AssociateStrategyToCmp_AC3_TC03_2 |
    Then Verify the result searching in dropdown box is correct
      | DataFile | CampaignStrategy                  |
      | Data     | AssociateStrategyToCmp_AC3_TC03_2 |
    When Clear the search box of "Select Campaign Strategy" field
    When Search strategy in Select Campaign Strategy field
      | DataFile | CampaignStrategy                  |
      | Data     | AssociateStrategyToCmp_AC3_TC03_3 |
    Then Verify the result searching in dropdown box is correct
      | DataFile | CampaignStrategy                  |
      | Data     | AssociateStrategyToCmp_AC3_TC03_3 |
    And Delete Strategies after tested

  @Campaign @AssociateStrategyToCmp @AC3 @TC23 @IgnoreInParallel
  Scenario: Verify the dropdown box show fully strategies when user clear the searchbox
    Given Load testcase data: DataFile = CampaignStrategy, Data = AssociateStrategyToCmp_AC3_TC05_PrepareData
    And Create multiple campaign strategies using API to associate to campaign
    And The Campaign management URL is hit
    When User click "New Campaign" button
    And In the Strategy Configuration, click select box to open the dropdown menu
    And Search strategy in Select Campaign Strategy field
      | DataFile | CampaignStrategy                |
      | Data     | AssociateStrategyToCmp_AC3_TC05 |
    Then Verify the result searching in dropdown box is correct
      | DataFile | CampaignStrategy                |
      | Data     | AssociateStrategyToCmp_AC3_TC05 |
    When Clear the search box of "Select Campaign Strategy" field
    Then The strategies is showed fully in the dropdown
    And Strategies display correctly in ascending order name in dropdown
    And Delete Strategies after tested

  @Campaign @AC4 @TC26
  Scenario:Verify campaign strategy associated keep in campaign editor after user save campaign success then edit it again
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp26
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    And Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And User click "Save" button
    When User "clicks a campaign name link" for update campaign Strategy
    Then Verify strategy name keep in select campaign strategy after "saved"
    And Clean up data create campaign

  @Campaign @AC4 @TC27
  Scenario:Verify campaign strategy associated keep in campaign editor after user updated campaign success then edit it again
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = CampaignStrategy, Data = associtateStrategytoCmp27
    And Create contact list using API
    And Create multiple campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign Strategy
    And Update Associate Strategy to campaign
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign Strategy
    Then Verify strategy name keep in select campaign strategy after "updated"
    And Clean up data create campaign

