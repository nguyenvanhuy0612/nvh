@IXOUTREACH-5338
Feature: IXOUTREACH-5338 Advance search option in campaign strategy
##############################################################################
##############################################################################
##############################################################################
  @CampaignStrategy @AdvanceSearching @Campaign @AC1 @TC1 @P5 @IgnoreInParallel
  Scenario: Verify search box and clear search option will be displayed when no strategy exists on the landing page
    Given The Campaign Strategy URL is hit
    And Cleanup all strategies data
    When Verify that there is no data in table
    Then Verify "Search box" should be displayed
    And Verify "Clear search option" should be displayed

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P5 @IgnoreInParallel
  Scenario: Verify filter button will be displayed when no strategy exists on the landing page
    Given The Campaign Strategy URL is hit
    And Cleanup all strategies data
    Then Verify that there is no data in table
    And Verify Advance Search option is displayed on campaign strategy

  @CampaignStrategy @AdvanceSearching @Campaign @AC1 @TC2
  Scenario: Verify search box and clear search option will be displayed when one or more strategies are present on the landing page
    Given Load testcase data: DataFile = CampaignStrategy, Data = AdvanceSearchingAC1_TC02
    And Create 5 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Clicking refresh button
    Then Verify "Search box" should be displayed
    And Verify "Clear search option" should be displayed
    And  Delete campaign strategies was created - 5

  @CampaignStrategy @AdvanceSearching @Campaign @AC1 @TC3
  Scenario: Verify search box work correctly
    Given Load testcase data: DataFile = CampaignStrategy, Data = AdvanceSearchingAC1_TC03
    And Create 25 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Verify basic search with "All" column as "strategy"
      | DataFile | CampaignStrategy         |
      | Data     | AdvanceSearchingAC1_TC03 |
    And  Delete campaign strategies was created - 25


    #To verify this scenario we get strategy list from API and UI during Runtime. During parallel execution list will be different due to other tests data manipulations
  @CampaignStrategy @AdvanceSearching @Campaign @AC1 @TC4 @IgnoreInParallel
  Scenario: Verify clear search option work correctly
    Given Load testcase data: DataFile = CampaignStrategy, Data = AdvanceSearchingAC1_TC04
    And Create 25 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Verify basic search with "All" column as "PUNETMA-cleared"
      | DataFile | CampaignStrategy         |
      | Data     | AdvanceSearchingAC1_TC04 |
    When User clear search string
    Then Verify basic search with "All" column as ""
      | DataFile | CampaignStrategy         |
      | Data     | AdvanceSearchingAC1_TC04 |
    And  Delete campaign strategies was created - 25

  @CampaignStrategy @AdvanceSearching @Campaign @AC1 @TC5 @IgnoreInParallel
  Scenario: Verify user can edit and save any strategies with the search box
    Given Load testcase data: DataFile = CampaignStrategy, Data = AdvanceSearchingAC1_TC05
    And Create 25 campaign strategies using API
    When The Campaign Strategy URL is hit
    And User search string as "Verify-strategy-is-updated05_21"
    And Edit description field
      | DataFile | CampaignStrategy    |
      | Data     | EditAndSaveStrategy |
    Then Verify edited records data
      | DataFile | CampaignStrategy    |
      | Data     | EditAndSaveStrategy |
    And  Delete campaign strategies was created - 25

  @CampaignStrategy @AdvanceSearching @Campaign @AC1 @TC6
  Scenario: Verify user can re-input to the search box
    Given Load testcase data: DataFile = CampaignStrategy, Data = AdvanceSearchingAC1_TC06
    And Create 25 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Verify basic search with "All" column as "input"
      | DataFile | CampaignStrategy         |
      | Data     | AdvanceSearchingAC1_TC06 |
    Then Verify value of Search Box as "input"
    When Verify basic search with "All" column as "str"
      | DataFile | CampaignStrategy         |
      | Data     | AdvanceSearchingAC1_TC06 |
    Then Verify value of Search Box as "str"
    And  Delete campaign strategies was created - 25

  @CampaignStrategy @AdvanceSearching @Campaign @AC1 @TC7
  Scenario: Verify nothing will be shown if user tries to search a strategy doesn't exist on the system
    Given Load testcase data: DataFile = CampaignStrategy, Data = AdvanceSearchingAC1_TC07
    When Create 25 campaign strategies using API
    And The Campaign Strategy URL is hit
    Then Verify basic search with "All" column as "doNot-*#Exist_on-SystemOBaaS-TMA"
      | DataFile | CampaignStrategy         |
      | Data     | AdvanceSearchingAC1_TC07 |
    And  Delete campaign strategies was created - 25

  @CampaignStrategy @AdvanceSearching @Campaign @AC1 @TC8
  Scenario: Verify a set of options will be set as default after clicking on the refresh button
    Given Load testcase data: DataFile = CampaignStrategy, Data = AdvanceSearchingAC1_TC08
    When Create 125 campaign strategies using API
    And The Campaign Strategy URL is hit
    Then Verify basic search with "All" column as "12"
      | DataFile | CampaignStrategy         |
      | Data     | AdvanceSearchingAC1_TC08 |
    When Verify default options after clicking on Refresh button with "Basic Search"
    And  Delete campaign strategies was created - 125


  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P3
  Scenario:Verify filter button will be displayed when one or more strategies are present on the landing page
    Given The Campaign Strategy URL is hit
    And Load test data: DataFile = CampaignStrategy, Data = AdvanceSearchStrategyAC2TC2
    When Create 1 campaign strategies using API
    And Clicking refresh button
    And Verify Advance Search option is displayed on campaign strategy
    And Delete campaign strategies was created - 1

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P4
  Scenario: Verify Filter button is disappeared when clicking Filter button again
    Given The Campaign Strategy URL is hit
    And Enable Advance search on campaign strategy
    And Disable Advance search on campaign strategy
    Then Verify Advance search options are not displayed on campaign strategy

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P3
  Scenario: Verify Column Name, Search and Operator will be shown all the search options as defined
    Given The Campaign Strategy URL is hit
    And Enable Advance search on campaign strategy
    Then Verify columnName is displayed on campaign strategy with values
      | columnName | Name |
    Then Verify columnName is displayed on campaign strategy without values
      | columnName | Last Executed |
    Then Verify operator is displayed on campaign strategy with values
      | operator | =\|!=\|In\|Like\|Not Like |
    Then Verify searchValue is displayed on campaign strategy with values
      | searchvalue |  |


  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P3
  Scenario: Verify Column Name, Search and Operator will be displayed with default selectionwhen selecting Filter
    Given The Campaign Strategy URL is hit
    And Enable Advance search on campaign strategy
    Then Verify columnName default selected on campaign strategy
      | columnName | Name |
    Then Verify operator default selected on campaign strategy
      | operator | = |
    Then Verify searchValue is displayed on campaign strategy with values
      | searchvalue |  |

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P3
  Scenario: Verify search options will be set as default after clicking on the refresh button
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name |
      | operator    | =    |
      | searchValue | demo |
    And Clicking refresh button
    Then Verify columnName default selected on campaign strategy
      | columnName | Name |
    Then Verify operator default selected on campaign strategy
      | operator | = |
    Then Verify searchValue is displayed on campaign strategy with values
      | searchvalue |  |

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P3 @IgnoreInParallel
  Scenario: Verify user can edit and save any strategies with the filter
    Given Load testcase data: DataFile = CampaignStrategy, Data = AdvanceSearchStrategy1AC2TC2
    And Create 1 campaign strategies using API
    And The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name                           |
      | operator    | =                              |
      | searchValue | AdvanceSearchStrategy1AC2TC2_0 |
    And Edit description field
      | DataFile | CampaignStrategy               |
      | Data     | AdvanceSearchStrategy1AC2TC2_0 |
    Then Verify edited records data
      | DataFile | CampaignStrategy               |
      | Data     | AdvanceSearchStrategy1AC2TC2_0 |
    And  Delete campaign strategies was created - 1

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P1
  Scenario: Verify Filter work correctly with operator as Equal
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC2AdvanceEqualSearch
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name                    |
      | operator    | =                       |
      | searchValue | AC2AdvanceEqualSearch_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC2AdvanceEqualSearch_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC2AdvanceEqualSearch_0\|AC2AdvanceEqualSearch_1 |
    And  Delete campaign strategies was created - 3

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P2
  Scenario: Verify Filter work correctly with operator as Not Equal
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC2AdvanceNotEquSear
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name                   |
      | operator    | !=                     |
      | searchValue | AC2AdvanceNotEquSear_1 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC2AdvanceNotEquSear_0\|AC2AdvanceNotEquSear_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC2AdvanceNotEquSear_1 |
    And  Delete campaign strategies was created - 3

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P2
  Scenario: Verify Filter work correctly with operator as In
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC2AdvanceInSearch
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name                                      |
      | operator    | In                                        |
      | searchValue | AC2AdvanceInSearch_0,AC2AdvanceInSearch_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC2AdvanceInSearch_0\|AC2AdvanceInSearch_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC2AdvanceInSearch_1 |
    And  Delete campaign strategies was created - 3


  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P2
  Scenario: Verify Filter work correctly with operator as Like
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC2AdvanceLikeSearch
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name     |
      | operator    | Like     |
      | searchValue | Search_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC2AdvanceLikeSearch_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC2AdvanceLikeSearch_0\|AC2AdvanceLikeSearch_1 |
    And  Delete campaign strategies was created - 3

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P2
  Scenario: Verify Filter work correctly with operator as Not Like
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC21AdvanceNotLikeSer
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name         |
      | operator    | Not Like     |
      | searchValue | NotLikeSer_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC21AdvanceNotLikeSer_0\|AC21AdvanceNotLikeSer_1 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC21AdvanceNotLikeSer_2 |
    And  Delete campaign strategies was created - 3

  @CampaignStrategy @Searching @Campaign @AC#1 @P4 @IXOUTREACH-6020
  Scenario: Verify Quick search on campaign strategy allows 40 charaters
    Given The Campaign Strategy URL is hit
    And Quick search strategy on campaign strategy
      | StrategyNames | A12345678901234567891234567890123456789_1 |
    Then Verify searched strategy length
      | SearchStringLength | 40 |

  @CampaignStrategy @Searching @Campaign @AC#1 @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case Strategy name is listed using Lowercase and Uppercase Quick Search
    Given Load testcase data: DataFile = CampaignStrategy, Data = QuickSrchStrategyCaseIn_0
    And  Delete campaign strategies was created - 1
    And Create 1 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Quick search strategy on campaign strategy
      | StrategyNames | QuickSrchStrategyCaseIn_0 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | QuickSrchStrategyCaseIn_0 |
    And Clicking refresh button
    And Quick search strategy on campaign strategy
      | StrategyNames | quicksrchstrategycasein_0 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | QuickSrchStrategyCaseIn_0 |
    And Clicking refresh button
    And Quick search strategy on campaign strategy
      | StrategyNames | QUICKSRCHSTRATEGYCASEIN_0 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | QuickSrchStrategyCaseIn_0 |
    And  Delete campaign strategies was created - 1

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case Strategy name is listed using Lowercase and Uppercase Advance Search equal operator
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC21AdvanceEqualSearch
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name                     |
      | operator    | =                        |
      | searchValue | ac21advanceequalsearch_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC21AdvanceEqualSearch_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC21AdvanceEqualSearch_0\|AC21AdvanceEqualSearch_1 |
    And Clicking refresh button
    And Apply Advance search on campaign strategy
      | columnName  | Name                     |
      | operator    | =                        |
      | searchValue | AC21ADVANCEEQUALSEARCH_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC21AdvanceEqualSearch_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC21AdvanceEqualSearch_0\|AC21AdvanceEqualSearch_1 |
    And  Delete campaign strategies was created - 3

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case Strategy name is listed using Lowercase and Uppercase Advance Search NOT equal operator
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC21AdvanceNotEquSear
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name                    |
      | operator    | !=                      |
      | searchValue | ac21advancenotequsear_1 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC2AdvanceNotEquSear_0\|AC21AdvanceNotEquSear_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC21AdvanceNotEquSear_1 |
    And Clicking refresh button
    And Apply Advance search on campaign strategy
      | columnName  | Name                    |
      | operator    | !=                      |
      | searchValue | AC21ADVANCENOTEQUSEAR_1 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC21AdvanceNotEquSear_0\|AC21AdvanceNotEquSear_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC21AdvanceNotEquSear_1 |
    And  Delete campaign strategies was created - 3

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case Strategy name is listed using Lowercase and Uppercase Advance Search In operator
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC21AdvanceInSearch
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name                                        |
      | operator    | In                                          |
      | searchValue | ac21advanceinsearch_0,ac21advanceinsearch_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC21AdvanceInSearch_0\|AC21AdvanceInSearch_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC21AdvanceInSearch_1 |
    And Clicking refresh button
    And Apply Advance search on campaign strategy
      | columnName  | Name                                        |
      | operator    | In                                          |
      | searchValue | AC21ADVANCEINSEARCH_0,AC21ADVANCEINSEARCH_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC21AdvanceInSearch_0\|AC21AdvanceInSearch_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC21AdvanceInSearch_1 |
    And  Delete campaign strategies was created - 3


  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case Strategy name is listed using Lowercase and Uppercase Advance Search Like operator
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC21AdvanceLikeSearch
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name     |
      | operator    | Like     |
      | searchValue | search_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC21AdvanceLikeSearch_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC21AdvanceLikeSearch_0\|AC21AdvanceLikeSearch_1 |
    And Clicking refresh button
    And Apply Advance search on campaign strategy
      | columnName  | Name     |
      | operator    | Like     |
      | searchValue | SEARCH_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC21AdvanceLikeSearch_2 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC21AdvanceLikeSearch_0\|AC21AdvanceLikeSearch_1 |
    And  Delete campaign strategies was created - 3

  @CampaignStrategy @AdvanceSearching @Campaign @AC#2 @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case Strategy name is listed using Lowercase and Uppercase Advance Search NOT Like operator
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC22AdvanceNotLikeSer
    And Create 3 campaign strategies using API
    Given The Campaign Strategy URL is hit
    And Apply Advance search on campaign strategy
      | columnName  | Name         |
      | operator    | Not Like     |
      | searchValue | notlikeser_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC22AdvanceNotLikeSer_0\|AC22AdvanceNotLikeSer_1 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC22AdvanceNotLikeSer_2 |
    And Clicking refresh button
    And Apply Advance search on campaign strategy
      | columnName  | Name         |
      | operator    | Not Like     |
      | searchValue | NOTLIKESER_2 |
    Then Verify expected strategies on campaign strategy
      | StrategyNames | AC22AdvanceNotLikeSer_0\|AC22AdvanceNotLikeSer_1 |
    Then Verify Not strategies expected on campaign strategy
      | StrategyNames | AC22AdvanceNotLikeSer_2 |
    And  Delete campaign strategies was created - 3
