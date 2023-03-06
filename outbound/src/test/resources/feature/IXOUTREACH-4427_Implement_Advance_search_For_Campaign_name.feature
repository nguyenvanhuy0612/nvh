@IXOUTREACH-3969 @IXOUTREACH-4427 @P1
Feature: Story IXOUTREACH-4427 Advance search for Campaigns

##############################################################################
##############################################################################
##############################################################################

  @TC1
  @Campaign @ListCampaign
  Scenario: Verify provision Filter button should also be present on the landing page with no campaigns display on campaign landing page
    Given The Campaign management URL is hit to view the existing campaigns
    When There are no campaigns display on campaign landing page
    Then The filter campaign button should display

  @TC2
  @Campaign @ListCampaign
  Scenario: Verify provision Filter button should also be present on the landing page with 1 campaign configured
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-TC2
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then The filter campaign button should display
    And Clean up all data for testing

  @TC3
    @Campaign @ListCampaign
  Scenario Outline: Verify The filter should start displaying the matched campaigns details based on the filter criteria applied on the search text when user click
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-TC3
    * Create 5 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "<Field>" "<Operator>" "<Content>" and verify results
    And Clean up all data for testing
    @TC3
    @Campaign @ListCampaign
    Examples:
      | Field | Operator | Content              |
      | Name  | =        | camp                 |
      | Name  | !=       | camp                 |
      | Name  | In       | camp_1,camp_2,camp_3 |
      | Name  | Like     | camp                 |
      | Name  | Not Like | camp                 |


  @TC4
  @Campaign @ListCampaign
  Scenario: Verify The search results should keep getting updated as the characters are entered by the user in the filter search box
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-TC4
    * Create 5 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "=" "camp_TC4" and verify results
    Then The search results should keep getting updated as the characters are entered by the user in the filter search box
    And Clean up all data for testing


  @TC5
  @Campaign @ListCampaign
  Scenario: Verify User should be able to select desired campaign(s) from the displayed campaigns at any point of time without the need to click on any other button (Search button)
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-TC5
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "Like" "camp_TC5" and verify results
    Then User should be able to select desired campaign from the displayed campaigns at any point of time without the need to click on any other button
    And Clean up all data for testing

  @TC6
  @Campaign @ListCampaign
  Scenario: Verify the user can View, Edit, Delete etc. campaigns on search results when search with filter
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-TC6
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "Like" "camp_AS_TC6" and verify results
    And  User Clicks a campaign name link
    And User should be able to initiate "Edit"
    Then The new values are saved successfully for the selected campaign
    And Clean up all data for testing

  @TC7
  @Campaign @ListCampaign
  Scenario: Verify column names and operators should be present for the filter
    Given Load test data: DataFile = Campaigns, Data = Advanced-Search-TC7
    Given The Campaign management URL is hit to view the existing campaigns
    When Check option column and operation
    And Clean up all data for testing

  @TC8
  @Campaign @ListCampaign
  Scenario: Verify User should be able to clear the filter by clicking the filter button again.
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-TC8
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "Like" "camp_AS_TC8" and verify results
    Then User should be able to clear the filter by clicking the filter button again
    And The filter should start displaying the matched campaigns details based on the filter criteria applied on the search text "camp_AS_TC8"
    And Clean up all data for testing

  @Campaign @ListCampaign @Searching @AC#1 @P4 @IXOUTREACH-6020 @IXOUTREACH-6208
  Scenario: Verify Quick search on campaign allows 40 characters
    Given The Campaign management URL is hit to view the existing campaigns
    And Quick search campaign
      | CampaignNames | C123456789012345678901234567890123456789012345678901234567890123456789012345678_1 |
    Then Verify searched campaign length
      | SearchStringLength | 40 |

  @Campaign @ListCampaign @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case campaign name is listed using Lowercase and Uppercase Quick Search
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-Case
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Quick search campaign
      | CampaignNames | advanced-search-case |
    Then Verify expected campaign on campaign List page
      | CampaignNames | Advanced-Search-Case |
    And Quick search campaign
      | CampaignNames | ADVANCED-SEARCH-CASE |
    Then Verify expected campaign on campaign List page
      | CampaignNames | Advanced-Search-Case |
    And Clean up all data for testing

  @Campaign @ListCampaign @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case campaign name is listed using Lowercase and Uppercase Advance Search equal operator
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-Case01
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "=" "advanced-search-case01" and verify results
    And User should be able to clear the filter by clicking the filter button again
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "=" "ADVANCED-SEARCH-CASE01" and verify results
    And Clean up all data for testing

  @Campaign @ListCampaign @P2 @IXOUTREACH-6181 @IgnoreInParallel
  Scenario: Verify mixed Case campaign name is listed using Lowercase and Uppercase Advance Search NOT equal operator
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-Case02
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "!=" "advanced-search-case02" and verify results
    And User should be able to clear the filter by clicking the filter button again
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "!=" "ADVANCED-SEARCH-CASE02" and verify results
    And Clean up all data for testing

  @Campaign @ListCampaign @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case campaign name is listed using Lowercase and Uppercase Advance Search In operator
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-Case03
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "In" "advanced-search-case03" and verify results
    And User should be able to clear the filter by clicking the filter button again
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "In" "ADVANCED-SEARCH-CASE03" and verify results
    And Clean up all data for testing

  @Campaign @ListCampaign @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case campaign name is listed using Lowercase and Uppercase Advance Search Like operator
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-Case04
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "Like" "advanced-search-case04" and verify results
    And User should be able to clear the filter by clicking the filter button again
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "Like" "ADVANCED-SEARCH-CASE04" and verify results
    And Clean up all data for testing

  @Campaign @ListCampaign @P2 @IXOUTREACH-6181 @IgnoreInParallel
  Scenario: Verify mixed Case campaign name is listed using Lowercase and Uppercase Advance Search NOT Like operator
    * Load test data: DataFile = Campaigns, Data = Advanced-Search-Case05
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "Not Like" "advanced-search-case05" and verify results
    And User should be able to clear the filter by clicking the filter button again
    Then User clicks the filter button, selects the filter criteria and types in the filter search box with "Name" "Not Like" "ADVANCED-SEARCH-CASE05" and verify results
    And Clean up all data for testing
