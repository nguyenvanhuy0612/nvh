@IXOUTREACH-3969 @IXOUTREACH-3972 @P1
Feature: Story IXOUTREACH-3972 List campaigns
  This feature for JIRA ID IXOUTREACH-3972 list campaigns

  @TC1
  @Campaign @ListCampaign
  Scenario: Verify campaign table display correctly with no campaigns display on campaign landing page
    Given The Campaign management URL is hit to view the existing campaigns
    Given There are no campaigns display on campaign landing page
    Then Message "Record list is empty" is displayed in place of row on campaign manager landing page

  @TC2
  @Campaign @ListCampaign
  Scenario: Verify provision to Search box and Refresh button should also be present on the landing page with no campaigns display on campaign landing page
    Given The Campaign management URL is hit to view the existing campaigns
    Given There are no campaigns display on campaign landing page
    Then The search box should display
    And The refresh button should display

  @TC3
  @Campaign @ListCampaign
  Scenario: Verify the campaign information should be displayed correct base name column
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC3
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then "Name" column displays correctly with individual campaigns
    And "Type" column displays correctly with individual campaigns
    And Clean up all data for testing

  @TC4
  @Campaign @ListCampaign
  Scenario: Verify a triple dot should display each row on campaign manager page
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC4
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then Triple dot should display each row on campaign manager page
    And Clean up all data for testing

  @TC5
  @Campaign @ListCampaign
  Scenario: Verify number record, page number, previous and next button page, change record option should be displayed correctly
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC5
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Search campaign "list_camp_TC5"
    Then Number record display correctly in the left bottom page
    And Previous and next button page having provision and disable to click in the page
    And The number page display is 1 page
    And Change number page record option is display
    And Clean up all data for testing

  @TC6
  @Campaign @ListCampaign
  Scenario: Verify provision Search box and Refresh button should also be present on the landing page with 1 campaign configured
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC6
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then The search box should display
    And The refresh button should display
    And Clean up all data for testing

  @TC7
  @Campaign @ListCampaign
  Scenario: Verify the page information should be present with a provision to navigate and jump to any other page forward and backward
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC7
    * Create 44 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then Default page size display at 10
    And Pagination work correctly on campaign landing page
    And Clean up all data for testing

  @TC8
  @Campaign @ListCampaign
  Scenario: Verify each page should list the number of records based on the page size
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC8
    * Create 24 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And User input text to field search
    Then Number record page display correctly when change the page size to "10,20,50,100"
    And Clean up all data for testing

  @TC9
  @Campaign @ListCampaign
  Scenario: Verify User should be able to change the page size "10,20,50,100", record should reloaded on page size change and should take user to first page
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC9
    * Create 74 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And User input text to field search
    Then User able to change the page size "10,20,50,100", record should reloaded on page size changed and should take user to first page
    And Clean up all data for testing

  @TC10
  @Campaign @ListCampaign
  Scenario: The default display order of records should be based on update time of the record
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC10
    * Create 4 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    Then The default display order of records should be based on update time of the record
    And Clean up all data for testing

  @TC11
  @Campaign @ListCampaign
  Scenario: Verify the quick search should work properly when input text on field search
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC11
    * Create 14 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And User input text to field search
    Then The quick search should start displaying the matched campaigns details if the search text entered is present anywhere in the campaign name
    And Clean up all data for testing

  @TC12
  @Campaign @ListCampaign
  Scenario: Verify the search results should keep getting updated as the campaign name characters are entered by the user in the search box
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC12
    * Create 12 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And User input text to field search
    Then The search results should keep getting updated as the campaign name characters are entered by the user in the search box
    And Clean up all data for testing

  @TC13
  @Campaign @ListCampaign
  Scenario: Verify the user can edit any campaigns from the displayed campaigns at any point of time without the need to click on any other button
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC13
    * Create 5 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    And User input text to field search
    Then User should be able to View and Edit operations for the searched campaign(s)
    And Clean up all data for testing

  @TC14
  @Campaign @ListCampaign
  Scenario: Verify List of campaigns table should display with "Record list is empty" message in place of rows when user Search campaign with invalid information
    Given The Campaign management URL is hit to view the existing campaigns
    When There are no campaigns display on campaign landing page
    Then List of campaigns table is displayed with "Record list is empty" message in place of rows

  @TC15
  @Campaign @ListCampaign
  Scenario: Verify Search box, Filter button & Refresh button should also be present on the landing page when the user searches for a campaign with invalid information
    Given The Campaign management URL is hit to view the existing campaigns
    When There are no campaigns display on campaign landing page
    Then List of campaigns table is displayed with "Record list is empty" message in place of rows
    And The search box should display
    And The refresh button should display

  @TC16
  @Campaign @ListCampaign
  Scenario: Verify Page information should not be present on the landing page when the user searches for a campaign with invalid information
    Given The Campaign management URL is hit to view the existing campaigns
    When There are no campaigns display on campaign landing page
    Then Previous page, next page, number page and change number page record option is not display on campaign landing page

  @TC17
  @Campaign @ListCampaign
  Scenario: Verify entire listed content should be sorted in desired order as per the admin's selection
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC17
    * Create 5 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When User input text to field search
    Then The "Name" column should be sorted in desired order as per the admin's selection.
    Then The "Type" column should be sorted in desired order as per the admin's selection.
    And Clean up all data for testing

  @TC18
  @Campaign @ListCampaign
  Scenario: Verify Default sorting order would be - Campaign update time (Ascending order).
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC18
    * Create 5 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When User input text to field search
    When User clicks on any one of these table column - Name, Type
    Then Default sorting order would be - Campaign update time Ascending order
    And Clean up all data for testing

  @TC19
  @Campaign @ListCampaign
  Scenario: verify Each of the 'n' fields (Name, Type) should toggle between Asc and Desc orders with UI indication transitioning from one order to another when clicked.
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC19
    * Create 5 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When User input text to field search
    Then The "Name" column toggle between Asc and Desc orders with UI indication transitioning from one order to another when clicked.
    Then The "Type" column toggle between Asc and Desc orders with UI indication transitioning from one order to another when clicked.
    And Clean up all data for testing

  @TC20
  @Campaign @ListCampaign
  Scenario: Verify All the search, filter, sort, pagination should be set to default and according campaign rows should be displayed from page 1 when user click button
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC20
    * Create 5 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When User input text to field search
    And User clicks the refresh button
    Then The search box should display
    And  The filter campaign button should display
    And  Click on type column and verify its sort
    And  Verify pagination reset to default values
    And Clean up all data for testing

  @TC21
  @Campaign @ListCampaign @IXOUTREACH-6090
  Scenario: Verify Campaign Manager Page supports sorting for Campaign Strategy
    Given The Campaign management URL is hit to view the existing campaigns
    Then Verify "all" sort icons on "Campaign Strategy" column

  @TC22
  @Campaign @ListCampaign @IXOUTREACH-6090
  Scenario: Verify sort order will be set as default after clicking on Refresh button
    Given The Campaign management URL is hit to view the existing campaigns
    Then Verify "asc" sort icons on "Campaign Strategy" column
    When User clicks the refresh button
    Then Verify "default" sort icons on "Campaign Strategy" column

  @TC23
  @Campaign @ListCampaign @IXOUTREACH-6090
  Scenario: Verify ascending sort work properly for Campaign Strategy on the Campaign Manager Page
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC23
    And Create contact list using API
    And Create 11 campaign strategies using API to check associate to campaign
    And Create 11 campaigns
    When The Campaign management URL is hit to view the existing campaigns
    Then Verify the sorting as "Asc" on "Campaign Strategy" column work properly
    And Clean up all data for testing

  @TC24
  @Campaign @ListCampaign @IXOUTREACH-6090
  Scenario: Verify descending sort work properly for Campaign Strategy on the Campaign Manager Page
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC24
    And Create contact list using API
    And Create 11 campaign strategies using API to check associate to campaign
    When The Campaign management URL is hit to view the existing campaigns
    And Create 11 campaigns
    Then Verify the sorting as "Des" on "Campaign Strategy" column work properly
    And Clean up all data for testing

  @TC25
  @Campaign @ListCampaign @IXOUTREACH-6090
  Scenario: Verify the sorting on Campaign Strategy column should happen across all the pages
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC25
    And Create contact list using API
    And Create 15 campaign strategies using API to check associate to campaign
    And Create 15 campaigns
    When The Campaign management URL is hit to view the existing campaigns
    Then Verify the sort should happen across all the pages with field as "Campaign Strategy" and page size as "10"
    And Clean up all data for testing

  @TC26
  @Campaign @ListCampaign @IXOUTREACH-6090
  Scenario: Verify user will be redirected to first page if user try applying sort for Campaign Strategy column on another page
    * Load test data: DataFile = Campaigns, Data = ListCampaign_TC26
    And Create contact list using API
    And Create 11 campaign strategies using API to check associate to campaign
    And Create 11 campaigns
    When The Campaign management URL is hit to view the existing campaigns
    And Click on the next page button
    And Use click on "Campaign Strategy" field to select sort order as "ascending"
    Then Verify user stay on page "1"
    And Clean up all data for testing

  @Campaign @IXOUTREACH-6458
  Scenario: Verify failure toast message should not display when input special character on text box search
    Given The Campaign management URL is hit to view the existing campaigns
    Then Failure toast message is not display when user try entering the characters "`~!@#$%^&*()-_+=[]\\{}|;':\",./<>? " in text box "basic" search
    And Failure toast message is not display when user try entering the characters "`~!@#$%^&*()-_+=[]\\{}|;':\",./<>? " in text box "advanced" search
