@IXOUTREACH-3983 @IXOUTREACH-4776 @P1
@Campaign @Sanity
Feature: IXOUTREACH-4776 Associate contact list to campaign
  The set of test cases to verification associate contact list to campaign

  @AC1 @TC1 @Campaign
  Scenario: Verify associate contact list field, dropdown should display on create contact list page and dropdown shown maximum 100 value
    * Load test data: DataFile = ContactListAssociateCampaign, Data = AC1_TC01
    Given Create a list 124 contact lists by API
    And The Campaign management URL is hit
    When User click "New Campaign" button
    Then Associate contact list field display on create campaign page
    Then Dropdown contact list displayed on contact list field
    And Dropdown contact list displayed max 100 value
    * Clean up contact list data for testing

  @AC1 @TC2 @Campaign
  Scenario: Verify user can not create campaign without associate contact list
    * Load testcase data: DataFile = ContactListAssociateCampaign, Data = AC1_TC02
    * Clean up campaign and contact list and strategy data for testing
    And Create campaign strategy using API
    Given The Campaign management URL is hit
    When User click "New Campaign" button
    And Input campaign name and campaign strategy after click save campaign button
    Then user can not create campaign without associate contact list
    * Clean up campaign and contact list and strategy data added for testing

  @AC1 @TC3 @IgnoreInDailyRun @Campaign
  Scenario: Verify The response shouldn't exceed more than 2 seconds while populating the data (2000 lists) in dropdown
    * Load testcase data: DataFile = ContactListAssociateCampaign, Data = AC1_TC03
    * Create a list 2000 contact lists by API
    Given The Campaign management URL is hit to view the existing campaigns
    When User click "New Campaign" button
    And In the Contact List Configuration, click select box to open the dropdown menu
    Then Response time to display contact list should not exceed 2 seconds
    * Clean up contact list data for testing

  @AC2 @TC1 @Campaign
  Scenario: Verify contact list should display correctly in alphabetical sort order in dropdown
    * Load testcase data: DataFile = ContactListAssociateCampaign, Data = AC2_TC01
    * Create a list 24 contact lists by API
    Given The Campaign management URL is hit to view the existing campaigns
    When User click "New Campaign" button
    And In the Contact List Configuration, click select box to open the dropdown menu
    Then Contact list display correctly in alphabetical sort order in dropdown
    * Clean up contact list data for testing

  @AC3 @TC1 @Campaign
  Scenario: Verify can search and select contact list when input valid characters to search box
    * Load testcase data: DataFile = ContactListAssociateCampaign, Data = AC3_TC01
    * Clean up campaign and contact list and strategy data for testing
    * Create campaign strategy using API
    * Create a contact list by API
    Given The Campaign management URL is hit to view the existing campaigns
    When Click Add New Campaign button, fill the name and description then associate strategy to campaign
    And Search contact list added and assign to campaign
    Then Click save button, the campaign created with this contact list
    * Clean up campaign and contact list and strategy data added for testing

  @AC3 @TC2 @Campaign
  Scenario: Verify no results when user input invalid/unmatch to search box
    * Load testcase data: DataFile = ContactListAssociateCampaign, Data = AC3_TC02
    * Clean up campaign and contact list and strategy data for testing
    * Create a contact list by API
    Given The Campaign management URL is hit to view the existing campaigns
    When User click "New Campaign" button
    And Enter the invalid or unmatch contact list name to search box
    Then Verify the list contact list in the search results is empty
    * Clean up campaign and contact list and strategy data added for testing

  @AC3 @TC3 @Campaign
  Scenario: Verify the contact list will sort by name when searching
    * Load testcase data: DataFile = ContactListAssociateCampaign, Data = AC3_TC03
    * Clean up campaign and contact list and strategy data for testing
    * Create campaign strategy using API
    * Create a contact list by API
    Given The Campaign management URL is hit to view the existing campaigns
    When Click Add New Campaign button, fill the name and description then associate strategy to campaign
    Then Search contact list added then verify the list contact list in the search results is sort by name
    And Click save button, the campaign created with this contact list
    * Clean up campaign and contact list and strategy data added for testing

  @AC4 @TC1 @Campaign
  Scenario: Verify the Contacts displayed property on the Header
    Given The Campaign management URL is hit to view the existing campaigns
    Then The Contact List attribute will display on the Header

  @AC4 @TC2 @Campaign
  Scenario: Campaign should be created with success toast message by associated contact list
    * Load test data: DataFile = ContactListAssociateCampaign, Data = Campaigns_AC4_TC2
    * Clean up campaign and contact list and strategy data for testing
    * Create campaign strategy using API
    * Create a contact list by API
    Given The Campaign management URL is hit to view the existing campaigns
    When Create a campaign with contact list
    And Create campaign message successfully display
    Then User should be redirected to campaign landing page
    * Clean up campaign and contact list and strategy data added for testing

  @AC4 @TC4 @Campaign
  Scenario: Verify information campaign should display correctly on campaign landing page with associate 1 contact list
    * Load test data: DataFile = ContactListAssociateCampaign, Data = Campaigns_AC4_TC4
    * Clean up campaign and contact list and strategy data for testing
    * Create campaign strategy using API
    * Create a contact list by API
    Given The Campaign management URL is hit to view the existing campaigns
    When Click Add New Campaign button, fill the name and description then associate strategy to campaign
    And Associate contact list to campaign and click save button
    Then Information of campaign display correctly on campaign landing page with contact list associated
    * Clean up campaign and contact list and strategy data added for testing

  @AC5 @TC1 @Campaign
  Scenario: Verify user will be back to landing page and get toast message display after save the contact list modification associated to campaign
    * Load test data: DataFile = ContactListAssociateCampaign, Data = Campaigns_AC5_TC1
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit to view the existing campaigns
    And Create 2 contact lists 1 strategy and 1 campaigns using the API
    And Search and click to go to edit campaign
    And Update contact list for campaign
    And Click to "save" button
    Then Create campaign message successfully display
    And User should be redirected to campaign landing page
    * Clean up campaign and contact list and strategy data added for testing

  @AC5 @TC2 @Campaign
  Scenario: Verify information contact list of campaign at landing page display correct with modification
    * Load test data: DataFile = ContactListAssociateCampaign, Data = Campaigns_AC5_TC2
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit to view the existing campaigns
    And Create 2 contact lists 1 strategy and 1 campaigns using the API
    And Search and click to go to edit campaign
    And Update contact list for campaign
    And Click to "save" button
    Then Verify contact list associated information display correctly on campaign landing page
    * Clean up campaign and contact list and strategy data added for testing

  @AC5 @TC3 @Campaign
  Scenario: Verify user cannot update campaign without associate contact list
    * Load test data: DataFile = ContactListAssociateCampaign, Data = Campaigns_AC5_TC3
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And Search and click to go to edit campaign
    Then Un-associate contact lists existing to campaign and verify save button is disable
    * Clean up campaign and contact list and strategy data added for testing

  @AC5 @TC4 @Campaign
  Scenario: Verify name of contact list associated to campaign should be display correctly on add new campaign page
    * Load test data: DataFile = ContactListAssociateCampaign, Data = Campaigns_AC5_TC4
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit to view the existing campaigns
    And Create 2 contact lists 1 strategy and 1 campaigns using the API
    And Search and click to go to edit campaign
    And Update contact list for campaign
    And Click to "save" button
    And Search and click to go to edit campaign
    Then Name of contact list associated to campaign should be display correctly on add new campaign page
    * Clean up campaign and contact list and strategy data added for testing
