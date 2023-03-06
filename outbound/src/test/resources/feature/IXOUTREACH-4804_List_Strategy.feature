@IXOUTREACH-4804
Feature: IXOUTREACH-4804 List Strategies
  This feature for ID IXOUTREACH-4804 List Strategies
##############################################################################
##############################################################################
##############################################################################
  @ListStrategy @Campaign @AC1 @TC1
  Scenario: Verify that the strategy page display correctly the all columns name when the user access this page
    Given Load test data: DataFile = CampaignStrategy, Data = listStrategyAC1TC1
    And The Campaign Strategy URL is hit
    When He is on the "Campaign Strategy" page
    Then All column names are shown correctly

  @ListStrategy @Campaign @AC1 @TC2
  Scenario: Verify that the strategy page display correctly the all columns name when the user access then refesh page several times
    Given Load test data: DataFile = CampaignStrategy, Data = listStrategyAC1TC2
    And The Campaign Strategy URL is hit
    And He is on the "Campaign Strategy" page
    When User refresh page 5 times
    Then All column names are shown correctly

  @ListStrategy @Campaign @AC1 @TC3
  Scenario: Verify that the strategy page display correctly the columns name when the user access the add strategy page then back to strategy page with cancel
    Given Load test data: DataFile = CampaignStrategy, Data = listStrategyAC1TC3
    And The Campaign Strategy URL is hit
    And clicks on new campaign strategy
    When Click Cancel Strategy
    Then All column names are shown correctly

  @ListStrategy @Campaign @AC1 @TC4
  Scenario: Verify that the strategy page display correctly the columns name when the user access the add strategy page then back to strategy page with save
    Given Load test data: DataFile = CampaignStrategy, Data = listStrategyAC1TC4
    And The Campaign Strategy URL is hit
    And clicks on new campaign strategy
    And Input value Strategy field
    When Save Strategy
    Then All column names are shown correctly

  @ListStrategy @Campaign @AC1 @TC5
  Scenario: Verify that the strategy page display correctly the columns name when the user access the strategy editor page then back to strategy page with cancel
    Given Load test data: DataFile = CampaignStrategy, Data = listStrategyAC1TC5
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    And Open strategy editor using "clicks on triple dot then select edit strategy"
    When Click Cancel Strategy
    Then All column names are shown correctly

  @ListStrategy @Campaign @AC1 @TC6
  Scenario: Verify that the strategy page display correctly the columns name when the user access the strategy editor page then back to strategy page with save
    Given Load test data: DataFile = CampaignStrategy, Data = listStrategyAC1TC6
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy         |
      | Data     | listStrategyAC1TC6Update |
    Then All column names are shown correctly

  @ListStrategy @Campaign @AC3 @TC11
  Scenario Outline: Verify that the user is still on the strategy page and All column names do not change when clicking any column heading in the strategy list
    Given Load test data: DataFile = CampaignStrategy, Data = listStrategyAC3TC11
    And The Campaign Strategy URL is hit
    When The user click on column heading in the strategy list - column name: "<Data>"
    Then He is on the "Campaign Strategy" page
    And Page do not show any error message
    And All column names are shown correctly
    Examples:
      | Data |
      | Name |
      | Type |

  @ListStrategy @Campaign @AC3 @TC12
  Scenario: Verify sort strategies list should be displayed correctly when click sort option with name column
    Given The Campaign Strategy URL is hit
    And Load test data: DataFile = CampaignStrategy, Data = listStrategyAC3TC12
    When Create 5 campaign strategies using API
    And User refresh page 1 times
    Then The "Name" column of strategy list should be sorted correctly as per the admin's selection
    And Delete campaign strategies was created - 5

  @ListStrategy @Campaign @AC3 @TC13
  Scenario: Verify order strategy list should display correctly after toggle Asc and Desc orders
    Given The Campaign Strategy URL is hit
    And Load test data: DataFile = CampaignStrategy, Data = listStrategyAC3TC13
    When Create 5 campaign strategies using API
    And User refresh page 1 times
    Then Default order strategy list of "Name" column displayed correctly after toggle Asc and Desc orders and go back to the default
    And Delete campaign strategies was created - 5

  @ListStrategy @Campaign @AC3 @TC14
  Scenario: Verify Sort icon should be toggle between Up and Down when user click colum Name on Strategy List
    Given The Campaign Strategy URL is hit
    And Load test data: DataFile = CampaignStrategy, Data = listStrategyAC3TC13
    When Create 5 campaign strategies using API
    And User refresh page 1 times
    Then Verify Sort icon should be toggle between Up and Down when user click column "Name"
    And Delete campaign strategies was created - 5


  @ListStrategy @TC01 @Campaign @AC2
  Scenario: Verify that the new strategy will on top after created on List strategy page
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC2_TC01
    And The Campaign Strategy URL is hit
    When Create strategy by "ui"
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy      |
      | Data     | ListStrategy_AC2_TC01 |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy      |
      | Data     | ListStrategy_AC2_TC01 |

  @ListStrategy @TC02 @Campaign @AC2 @IgnoreInParallel
  Scenario: Verify that the new strategy will on top after edited on List strategy page
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC2_TC02
    When Create 16 campaign strategies using API
    And The Campaign Strategy URL is hit
    And Select page size "10"
    And Navigate to page "2"
    When Edit description field
      | DataFile | CampaignStrategy                          |
      | Data     | ListStrategy_AC2_TC02_modifiedDescription |
    And Verify current page number is 1
    And Verify current page size is 10
    Then Verify "strategy" after "edited" is on top
      | DataFile | CampaignStrategy                          |
      | Data     | ListStrategy_AC2_TC02_modifiedDescription |
    And Delete campaign strategies was created - 16

  @ListStrategy @TC03 @Campaign @AC2
  Scenario: Verify that the default Sort order is on Last modified On in descending order with page size 10
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC2_TC03
    When Create 16 campaign strategies using API
    And The Campaign Strategy URL is hit
    Then The default Sort order is on Last modified On in descending order with pageSize "10"
    And Delete campaign strategies was created - 16

  @ListStrategy @TC04 @Campaign @AC2
  Scenario: Verify that the default Sort order is on Last modified On in descending order with page size 20
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC2_TC04
    When Create 15 campaign strategies using API
    And The Campaign Strategy URL is hit
    Then The default Sort order is on Last modified On in descending order with pageSize "20"
    And Delete campaign strategies was created - 15

  @ListStrategy @TC05 @Campaign @AC2
  Scenario: Verify that the default Sort order is on Last modified On in descending order with page size 50
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC2_TC05
    When Create 16 campaign strategies using API
    And The Campaign Strategy URL is hit
    Then The default Sort order is on Last modified On in descending order with pageSize "50"
    And Delete campaign strategies was created - 16

  @ListStrategy @TC06 @Campaign @AC2
  Scenario: Verify that the default Sort order is on Last modified On in descending order with page size 100
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC2_TC06
    When Create 16 campaign strategies using API
    And The Campaign Strategy URL is hit
    Then The default Sort order is on Last modified On in descending order with pageSize "100"
    And Delete campaign strategies was created - 16

  @ListStrategy @TC07 @Campaign @AC2
  Scenario: Verify that the default Sort order is on Last modified On in descending order after reload List strategy page 5 times
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC2_TC07
    When Create 5 campaign strategies using API
    And The Campaign Strategy URL is hit
    When Refresh page 5 times
    Then The default Sort order is on Last modified On in descending order with pageSize "20"
    And Delete campaign strategies was created - 5

  @ListStrategy @TC01 @Campaign @AC5
  Scenario: Verify that the Refresh button is enable in strategy list page
    Given The Campaign Strategy URL is hit
    Then The Refresh button is enable in strategy list page

  @ListStrategy @TC02 @Campaign @AC5
  Scenario: Verify that the Refresh button is enable in strategy list page after reload multi times
    Given The Campaign Strategy URL is hit
    When Refresh page 5 times
    Then The Refresh button is enable in strategy list page

  @ListStrategy @TC03 @Campaign @AC5 @IgnoreInParallel
  Scenario: Verify that the Refresh button should be get refreshed and displayed the latest information with default sort option
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC5_TC03
    And The Campaign Strategy URL is hit
    And Create multi strategies by "API"
    When Clicking refresh button
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy      |
      | Data     | ListStrategy_AC5_TC03 |
    And Verify current page number is 1
    And Verify current page size is 10
    And The default Sort order is on Last modified On in descending order with pageSize "20"
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy      |
      | Data     | ListStrategy_AC5_TC03 |

  @ListStrategy @TC04 @Campaign @AC5 @IgnoreInParallel
  Scenario: Verify that page list goes back to the first page with default sort option if user is in middle page with other sort after clicked refresh button
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC5_TC04
    And Create multi strategies by "API"
    And The Campaign Strategy URL is hit
    And Select page size "10"
    And Navigate to page "2"
    When Clicking refresh button
    And Verify current page number is 1
    And Verify current page size is 10
    And The default Sort order is on Last modified On in descending order with pageSize "20"
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy      |
      | Data     | ListStrategy_AC5_TC04 |

  @ListStrategy @TC05 @Campaign @AC5 @IgnoreInParallel
  Scenario: Verify that page list goes back to the first page with default sort option if user is in last page with other sort after clicked refresh button
    Given Load test data: DataFile = CampaignStrategy, Data = ListStrategy_AC5_TC05
    And Create multi strategies by "API"
    And The Campaign Strategy URL is hit
    And Select page size "10"
    And Navigate to page "lasted"
    When Clicking refresh button
    And Verify current page number is 1
    And Verify current page size is 10
    And The default Sort order is on Last modified On in descending order with pageSize "20"
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy      |
      | Data     | ListStrategy_AC5_TC05 |

  @ListStrategy @Campaign @AC4 @TC01
  Scenario: Verify number record, page number, previous and next page button, and page size options will be displayed correctly
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC01
    And Create strategy by API
    When The Campaign Strategy URL is hit
    Then Verify "page size options" should be displayed
    And Verify "page number" should be displayed
    And Verify "previous and next button" should be displayed
    And Verify "page detail" should be displayed

  @ListStrategy @Campaign @AC4 @TC02
  Scenario: Verify default page size value is 10
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC02
    And Create strategy by API
    When The Campaign Strategy URL is hit
    Then Verify default page size as "10"

  @ListStrategy @Campaign @AC4 @TC03
  Scenario: Verify user should able to change page size at Campaign Strategy page
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC03
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    Then Verify user be able to change page size to "10,20,50,100"
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC04 @IgnoreInParallel
  Scenario: Verify each page should list the number of records correctly with selected page size
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC04
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    Then Change the page size to "10,20,50,100" and verify number of records on per page
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC05 @IgnoreInParallel
  Scenario: Verify page number display correctly with selected page size
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC05
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    Then Change the page size to "10,20,50,100" and verify number of page
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC06
  Scenario: Verify that if we have only 1 record then Next Page, Previous Page, and Go To Page should not be clickable/displayed
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC06
    And Create strategy by API
    When The Campaign Strategy URL is hit
    Then Verify "Next page" should not be clickable or displayed
    And Verify "Previous page" should not be clickable or displayed
    And Verify "Go To Page" should not be clickable or displayed

  @ListStrategy @Campaign @AC4 @TC07 @IgnoreInParallel
  Scenario: Verify user should able to go to previous and next page
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC07
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Change the page size to "10,20,50,100" then verify next and previous page button work fine
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC08
  Scenario: Verify use will be redirected to the first page if applying sort action on another page
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC08
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Click on the next page button
    And Use click on "Name,Type" field to select sort order as "ascending"
    Then Verify user stay on page "1"
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC09 @IgnoreInParallel
  Scenario: Verify error message "Please enter valid page name" will be displayed when input invalid page number into Go To Page field
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC09
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    Then Send invalid page number and verify error message with page size as "10,20,50,100"
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC10 @IgnoreInParallel
  Scenario: Verify the sort should happen across all the pages
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC10
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    Then Verify the sort should happen across all the pages with field as "Name" and page size as "10,20,50,100"
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC11
  Scenario: Verify user will be placed on the first page if value of Go To Page as an empty
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC11
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Verify user be able to change page size to "10"
    Then Verify user stay on page "1"
    And Verify value of Go To Page box
      | DataFile | CampaignStrategy     |
      | Data     | ListStrategyAC4_TC11 |
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC12 @IgnoreInParallel
  Scenario: Verify user can use the "step up" option and all record show correctly
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC12
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Verify user be able to change page size to "10"
    Then Verify step "up" option work well with page size as "10"
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC13 @IgnoreInParallel
  Scenario:  Verify user can use the "step down" option and all record show correctly
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC13
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Verify user be able to change page size to "10"
    Then Verify step "down" option work well with page size as "10"
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC4 @TC14 @IgnoreInParallel
  Scenario:  Verify Use can click on the "step up" option too many time and an error will be shown
    Given Load testcase data: DataFile = CampaignStrategy, Data = ListStrategyAC4_TC14
    And Create 21 campaign strategies using API
    When The Campaign Strategy URL is hit
    And Verify user be able to change page size to "10"
    Then Verify an error will shown if user click too many times on the step "up" option with page as "10,20,50,100"
    And Delete campaign strategies was created - 21

  @ListStrategy @Campaign @AC7 @TC1 @IgnoreInParallel
  Scenario:  Verify that table headers and an generic error message will be shown if there is no data in the system
    Given Cleanup all strategies data
    When The Campaign Strategy URL is hit
    Then The Refresh button is enable in strategy list page
    And Verify header field "Name,Type" is display
    And Verify a message "Record list is empty" is displayed

  @ListStrategy @Campaign @AC7 @TC02 @IgnoreInParallel
  Scenario:  Verify after clicking the refresh button the table headers and an generic error message will be shown if there is no data in the system
    Given Cleanup all strategies data
    When The Campaign Strategy URL is hit
    And Clicking refresh button
    Then The Refresh button is enable in strategy list page
    And Verify header field "Name,Type" is display
    And Verify a message "Record list is empty" is displayed

  @ListStrategy @Campaign @AC7 @TC03 @IgnoreInParallel
  Scenario:  Verify after refreshing current page the table headers and an generic error message will be shown if there is no data in the system
    Given Cleanup all strategies data
    When The Campaign Strategy URL is hit
    And Refresh page 5 times
    Then The Refresh button is enable in strategy list page
    And Verify header field "Name,Type" is display
    And Verify a message "Record list is empty" is displayed

  @IXOUTREACH-6458
  Scenario: Verify failure toast message should not display when input special character on text box search
    Given The Campaign Strategy URL is hit
    Then Failure toast message is not display when user try entering the characters "`~!@#$%^&*()-_+=[]\\{}|;':\",./<>? " in text box "basic" search
    And Failure toast message is not display when user try entering the characters "`~!@#$%^&*()-_+=[]\\{}|;':\",./<>? " in text box "advanced" search
