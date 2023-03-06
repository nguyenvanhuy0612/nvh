@IXOUTREACH-4072 @IXOUTREACH-6477 @P1 @Monitor
@DataFile=MonitorActiveCampaign
Feature: IXOUTREACH-6477 Monitor_List_Search_and_Sort_in_Active_Campaign_view


  @AC5 @TC1 @Data=MonitorListCamp_AC5_TC1
  Scenario: Verify that list campaigns should be refreshed every 5 sec automatically in active campaign page
    Given Create 5 campaign using API
    And Run 4 campaigns by campaign name using API
    And The Active Campaign monitor URL is hit
    When Run 1 campaign more by campaign name using API
    Then List campaign is updated every 5 seconds automatically

  @AC5 @TC2 @Data=MonitorListCamp_AC5_TC2
  Scenario: Verify that list campaigns should be updated every 5 sec automatically during basic search implementation
    Given Create 4 campaign using API
    And Run 2 campaigns by campaign name using API
    And The Active Campaign monitor URL is hit
    When Apply basic search for name column
    And Run 2 campaign more by campaign name using API
    Then List campaign is updated every 5 seconds automatically

  @AC5 @TC3 @Data=MonitorListCamp_AC5_TC3
  Scenario: Verify that list campaigns should be updated every 5 sec automatically during advance search implementation
    Given Create 3 campaign using API
    And Run 2 campaigns by campaign name using API
    And The Active Campaign monitor URL is hit
    When Apply advance search for campaign name with operator
      | columnName  | Name                      |
      | operator    | =                         |
      | searchValue | MonitorListCamp_AC5_TC3_3 |
    And Run 1 campaign more by campaign name using API
    Then List campaign is updated every 5 seconds automatically

  @AC5 @TC4 @Data=MonitorListCamp_AC5_TC4
  Scenario: Verify that list campaigns should be updated every 5 sec automatically during sorting implementation
    Given Create 3 campaign using API
    And Run 2 campaigns by campaign name using API
    And The Active Campaign monitor URL is hit
    When Apply sorting ascending on Name column
    And Run 1 campaign more by campaign name using API
    Then List campaign is updated every 5 seconds automatically

  @AC5 @TC5 @Data=MonitorListCamp_AC5_TC5
  Scenario: Verify that list campaigns should be updated every 5 sec automatically during  pagination changing implementation
    Given Create 3 campaign using API
    And Run 2 campaigns by campaign name using API
    And The Active Campaign monitor URL is hit
    When Change page size to 100 in active campaign page
    And Run 1 campaign more by campaign name using API
    Then List campaign is updated every 5 seconds automatically

  @AC7 @TC1 @Data=SORT_AC7_TC1
  Scenario: Verify the sort icon displays correctly on the header panel when clicking on the Status column with campaign running
    * Create and run 1 campaign using API
    Given The Active Campaign monitor URL is hit
    Then User clicks on header column "Status", the sorting icons change with each click

  @AC7 @TC2 @Data=SORT_AC7_TC2
  Scenario: Verify sort function work correctly with Status column
    * Create and run 6 campaign using API
    Given The Active Campaign monitor URL is hit
    Then The sort function works correctly when the user tries to click the "Status" column a few times

  @AC7 @TC3 @Data=SORT_AC7_TC3
  Scenario: Verify that the sort function for the Status column works correctly across pages
    * Create and run 32 campaign using API
    Given The Active Campaign monitor URL is hit
    Then The user is implementing sorting with the "Status" Column, going to each page and verifying that the sort still works for each page

  @AC8 @TC1 @Data=ADV_SEARCH_AC8_TC1
  Scenario: Verify that advance search function is work correctly for Name column with each operator
    * Create and run 12 campaign using API
    Given The Active Campaign monitor URL is hit
    Then Apply advance search for Name column with each operator and verify function works correctly
      | Column Name | Operator | Value                                     |
      | Name        | Like     | ADV_SEARCH_AC8_TC1                        |
      | Name        | In       | ADV_SEARCH_AC8_TC1_2,ADV_SEARCH_AC8_TC1_1 |
      | Name        | =        | ADV_SEARCH_AC8_TC1_11                     |
      | Name        | !=       | ADV_SEARCH_AC8_TC1_3                      |
      | Name        | Not Like | ADV_SEARCH_AC8_TC1_5                      |

  @AC8 @TC9 @Data=ADV_SEARCH_AC8_TC9
  Scenario: Verify that the Operator and Search box field are updated correctly after the column name is changed
    Given Create and run 12 campaign using API
    When The Active Campaign monitor URL is hit
    Then Operator and Search box field are updated correctly after the column name is changed
      | Column Name | Operator | Value                                     |
      | Name        | Like     | ADV_SEARCH_AC8_TC9_2                      |
      | Name        | In       | ADV_SEARCH_AC8_TC9_7,ADV_SEARCH_AC8_TC9_1 |
      | Name        | =        | ADV_SEARCH_AC8_TC9_5                      |
      | Name        | !=       | ADV_SEARCH_AC8_TC9_3                      |
      | Name        | Not Like | ADV_SEARCH_AC8_TC9_8                      |

  @AC8 @TC10 @Data=ADV_SEARCH_AC8_TC10
  Scenario: Verify that Search text is not cleared after changing operator on the operator field
    When The Active Campaign monitor URL is hit
    Then Verify that Search text is cleared after changing operator on "Name" column

  @AC8 @TC2 @Data=ADV_SEARCH_AC8_TC2
  Scenario: Verify that advance search function is work correctly for Start time column with each operator
    * Create 10 campaign using API
    Given The Active Campaign monitor URL is hit
    * Run 10 campaign using API
    Then Apply advance search for Start time column with each operator and verify it it work correctly
      | Column Name | Input type    | Operator | Value                                                        |
      | Start time  | Input         | >        | campaign:ADV_SEARCH_AC8_TC2_1                                |
      | Start time  | Select        | >        | campaign:ADV_SEARCH_AC8_TC2_1                                |
      | Start time  | Input         | <        | campaign:ADV_SEARCH_AC8_TC2_10                               |
      | Start time  | Select        | <        | campaign:ADV_SEARCH_AC8_TC2_10                               |
      | Start time  | Select,Select | Between  | campaign:ADV_SEARCH_AC8_TC2_1,campaign:ADV_SEARCH_AC8_TC2_10 |
      | Start time  | Input,Input   | Between  | campaign:ADV_SEARCH_AC8_TC2_2,campaign:ADV_SEARCH_AC8_TC2_8  |
      | Start time  | Select,Input  | Between  | campaign:ADV_SEARCH_AC8_TC2_1,campaign:ADV_SEARCH_AC8_TC2_10 |
      | Start time  | Input,Select  | Between  | campaign:ADV_SEARCH_AC8_TC2_2,campaign:ADV_SEARCH_AC8_TC2_8  |

  @AC8 @TC3 @Data=ADV_SEARCH_AC8_TC3
  Scenario: Verify that advance search function is work correctly for Status column with each operator
    * Create 10 campaign using API
    Given The Active Campaign monitor URL is hit
    * Run 10 campaign using API
    When Pause 6 campaign using API and waiting campaigns are paused successfully
    Then Apply advance search for Status column with each operator and verify it it work correctly
      | Column Name | Operator | Value   |
      | Status      | !=       | Paused  |
      | Status      | =        | Running |
      | Status      | =        | Paused  |
      | Status      | !=       | Running |

  @AC8 @TC4
  Scenario: Verify Name and Start time, Status display on dropdown of Column Name on active campaign monitor page
    Given The Active Campaign monitor URL is hit
    When Click filter button
    Then Name and Start time, Status display on dropdown of Column Name on active campaign monitor page
      | Name | Start Time | Status |

  @AC8 @TC5
  Scenario Outline: Verify list operator display correctly for each advance search attribute
    Given The Active Campaign monitor URL is hit
    When Click filter button
    Then list "<Operator>" display correctly for each advance search "<Attribute>"
    Examples:
      | Attribute  | Operator              |
      | Name       | =,!=,In,Like,Not Like |
      | Start time | <,>,Between           |
      | Status     | =,!=                  |

  @AC8 @TC6 @Data=ADV_SEARCH_AC8_TC6
  Scenario: Verify that user can stop campaign successful after using advance search
    * Create 1 campaign using API
    Given The Active Campaign monitor URL is hit
    * Run 1 campaign using API
    When Apply advance search for field "Name" by "=" with "ADV_SEARCH_AC8_TC6"
    Then User can stop campaign successful with result search

  @AC8 @TC7 @Data=ADV_SEARCH_AC8_TC7
  Scenario: Verify that advance search result should updated when updating input value for Name column
    * Create 5 campaign using API
    Given The Active Campaign monitor URL is hit
    * Run 5 campaign using API
    Then Verify advance search result correctly after updating input value
      | Column Name | Operator | Value                                     | New value            | Input type |
      | Name        | Like     | ADV_SEARCH_AC8_TC7_1                      | ADV_SEARCH_AC8_TC7_2 | Input      |
      | Name        | In       | ADV_SEARCH_AC8_TC7_1,ADV_SEARCH_AC8_TC7_2 | ADV_SEARCH_AC8_TC7_3 | Input      |
      | Name        | =        | ADV_SEARCH_AC8_TC7_1                      | ADV_SEARCH_AC8_TC7_2 | Input      |
      | Name        | !=       | ADV_SEARCH_AC8_TC7_1                      | ADV_SEARCH_AC8_TC7_2 | Input      |
      | Name        | Not Like | ADV_SEARCH_AC8_TC7_1                      | ADV_SEARCH_AC8_TC7_2 | Input      |

  @AC8 @TC8 @Data=ADV_SEARCH_AC8_TC8
  Scenario: Verify that advance search result should updated when updating input value for Start time
    * Create 10 campaign using API
    Given The Active Campaign monitor URL is hit
    * Run 10 campaign using API
    Then Verify advance search result correctly after updating input value
      | Column Name | Operator | Value                                                       | New value                                                    | Input type  |
      | Start Time  | >        | campaign:ADV_SEARCH_AC8_TC8_10                              | campaign:ADV_SEARCH_AC8_TC8_5                                | Select      |
      | Start Time  | <        | campaign:ADV_SEARCH_AC8_TC8_1                               | campaign:ADV_SEARCH_AC8_TC8_5                                | Input       |
      | Start Time  | Between  | campaign:ADV_SEARCH_AC8_TC8_1,campaign:ADV_SEARCH_AC8_TC8_6 | campaign:ADV_SEARCH_AC8_TC8_5,campaign:ADV_SEARCH_AC8_TC8_10 | Input,Input |

  @AC4 @TC1 @Data=RefreshMonitor_AC4_TC1 @Monitor
  Scenario: Verify List & summary should be refreshed when user refreshes the page
    Given The Active Campaign monitor URL is hit
    When Search campaign "RefreshMonitor_AC4_TC1"
    Then User should  able to see 0 campaign
    When Create 12 campaign using API
    When Run 12 campaign using API
    When Clicking refresh button
    When Search campaign "RefreshMonitor_AC4_TC1"
    Then User should  able to see 12 campaign
    When User click on column name "Name" to change sorting order
    When User clicks the refresh button
    Then The campaign is sorted base on Start Time
    When User click on column name "Status" to change sorting order
    When User clicks the refresh button
    Then The campaign is sorted base on Start Time
    When User click on column name "Start Time" to change sorting order
    When User clicks the refresh button
    Then The campaign is sorted base on Start Time


  @AC4 @TC2 @Data=RefreshMonitor_AC4_TC2 @Monitor
  Scenario Outline: Verify List & summary should be refreshed when and advanced search on page
    Given The Active Campaign monitor URL is hit
    When Search Active Campaign list with field "<Column Name>", operator "<Operator>", type "<Input type>" value "<Value>" search
    Then User should  able to see 0 campaign
    When Create 6 campaign using API
    When Run 6 campaign using API
    When Clicking refresh button
    Then The campaign is sorted base on Start Time with advance search
    When Search Active Campaign list with field "<Column Name>", operator "<Operator>", type "<Input type>" value "<Value>" search
    Then User should  able to see <Output> campaign
    Then The campaign is sorted base on Start Time with advance search
    When Clicking refresh button
    Then The campaign is sorted base on Start Time with advance search

    @Campaign @AC4 @TC2
    Examples:
      | Column Name | Operator | Value                                                                                                                                                 | Input type | Output |
      | Name        | Like     | RefreshMonitor_AC4_TC2                                                                                                                                | Input      | 6      |
      | Name        | In       | RefreshMonitor_AC4_TC2_1,RefreshMonitor_AC4_TC2_2,RefreshMonitor_AC4_TC2_3,RefreshMonitor_AC4_TC2_4,RefreshMonitor_AC4_TC2_5,RefreshMonitor_AC4_TC2_6 | Input      | 6      |
      | Name        | =        | RefreshMonitor_AC4_TC2_1                                                                                                                              | Input      | 1      |


