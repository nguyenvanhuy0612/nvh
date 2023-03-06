@IXOUTREACH-4860
@ListContacts @ContactList @P1
Feature: Story IXOUTREACH-4860 List Contacts
  The set of test cases to verification the list contacts in the Browser contact page

  @Contact @AC1 @TC1
  Scenario: Verify User should be landed to Contact browser when click View contact option
    * Load test data: DataFile = DataSourceTestData, Data = datasource_AC1_TC1
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    Then View contact option display on three dot dropdown of contact list
    Then Contact page display after click View contact option
    * Clean up contact list data for testing

  @Contact @AC1 @TC2
  Scenario: Verify contact table display correctly with no record configured
    * Load test data: DataFile = DataSourceTestData, Data = datasource_AC1_TC2
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    When Click View contact option on three dot dropdown of contact list
    Then Message "Record list is empty" is displayed in place of row on contact landing page
    And Refresh button will be displayed
    * Clean up contact list data for testing

  @Contact @AC1 @TC3
  Scenario: Verify the column headers should be as per the business attributes associated with that list when click View Contact Option
    * Load test data: DataFile = DataSourceTestData, Data = datasource_AC1_TC3
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    When Click View contact option on three dot dropdown of contact list
    Then The column headers display correctly as per the business attributes associated with that list
    * Clean up contact list data for testing

  @Contact @AC1 @TC4
  Scenario: Verify information contact display correctly with business attributes associated with that list when click View Contact Option
    * Load test data: DataFile = DataSourceTestData, Data = datasource_AC1_TC4
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    When Click View contact option on three dot dropdown of contact list
    Then Information contact displayed correctly with business attributes associated
    * Clean up contact list data for testing

  @Contact @AC2 @TC1
  Scenario: Verify Hyper link should not create if there are 0 contact in contact list
    * Load test data: DataFile = DataSourceTestData, Data = datasource_AC2_TC1
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    Then Hyper link is not create on total contact field of empty contact list
    * Clean up contact list data for testing

  @Contact @AC2 @TC2
  Scenario: Verify Hyper link should be created if there are more than 0 contact in contact list
    * Load test data: DataFile = DataSourceTestData, Data = datasource_AC2_TC2
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    Then Hyper link created on total contact field of contact list contain records
    * Clean up contact list data for testing

  @Contact @AC2 @TC3
  Scenario: Verify the column headers should be as per the business attributes associated with that list when click Hyper link on total contact
    * Load test data: DataFile = DataSourceTestData, Data = datasource_AC2_TC3
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    And Click Hyper link on total contact with contact list
    Then Contact page display after click Hyper link
    And The column headers display correctly as per the business attributes associated with that list
    * Clean up contact list data for testing

  @Contact @AC2 @TC4
  Scenario: Verify information contact display correctly with business attributes associated with that list when Hyper link on total contact
    * Load test data: DataFile = DataSourceTestData, Data = datasource_AC2_TC4
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    And Click Hyper link on total contact with contact list
    Then Information contact displayed correctly with business attributes associated
    * Clean up contact list data for testing

  @Contact @AC2 @TC5
  Scenario: Verify number contact should display on contact page same with total contact on contact list page
    * Load test data: DataFile = DataSourceTestData, Data = datasource_AC2_TC5
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    And Get value total contact of contact list
    And Click Hyper link on total contact with contact list
    Then Number contact displayed on contact page same with total contact on contact list page
    * Clean up contact list data for testing

  @Contact @AC3 @TC1
  Scenario: Verify default page size value in contact browser page is 10
    * Load test data: DataFile = ListContacts, Data = AC31
    Given Create a contact list and import datasource by API
    And The Contact list URL is hit to view the existing contact
    When Click Hyper link on total contact with contact list
    Then The default page size value is 10
    * Clean up contact list data for testing

  @Contact @AC3 @TC2
  Scenario: Verify that user should able to change page size to 10, 20, 50, 100
    * Load test data: DataFile = ListContacts, Data = AC32
    Given Create a contact list and import datasource by API
    And The Contact list URL is hit to view the existing contact
    When Click Hyper link on total contact with contact list
    Then Change page size to 10, 20, 50, 100 and verify its working correctly
    * Clean up contact list data for testing

  @Contact @AC3 @TC3
  Scenario: Verify the number of displayed pages matches the selected page size and number of records
    * Load test data: DataFile = ListContacts, Data = AC33
    Given Create a contact list and import datasource by API
    And The Contact list URL is hit to view the existing contact
    When Click Hyper link on total contact with contact list
    Then Change page size to 10, 20, 50, 100 and verify the number of page display correctly
    * Clean up contact list data for testing

  @Contact @AC3 @TC4
  Scenario: Verify each page will list the correct number of records with the selected page size
    * Load test data: DataFile = ListContacts, Data = AC34
    Given Create a contact list and import datasource by API
    And The Contact list URL is hit to view the existing contact
    When Click Hyper link on total contact with contact list
    Then Change page size to 10, 20, 50, 100 and verify each page will display no more than the number of configuration records in page size
    * Clean up contact list data for testing

  @Contact @AC3 @TC5
  Scenario: Verify record name will display correctly on contact browser
    * Load test data: DataFile = ListContacts, Data = AC35
    Given Create a contact list and import datasource by API
    And The Contact list URL is hit to view the existing contact
    When Click Hyper link on total contact with contact list
    Then Verify record name will display correctly on contact browser

  @Contact @AC3 @TC6
  Scenario: Verify record number, page number, previous, next page buttons, page size options will be shown when there is at least one record
    * Load test data: DataFile = ListContacts, Data = AC36
    Given Create a contact list and import datasource by API
    And The Contact list URL is hit to view the existing contact
    And Click Hyper link on total contact with contact list
    Then Verify the record number, page number, previous, next page buttons, page size options display correctly
    * Clean up contact list data for testing

  @Contact @AC3 @TC7
  Scenario: Verify record number, page number, previous, next page buttons, page size options will be not shown if there are no records
    * Load test data: DataFile = ListContacts, Data = AC37
    Given Create a contact list by API
    And The Contact list URL is hit to view the existing contact
    And Click View contact option on three dot dropdown of contact list
    When The record number, page number, previous, next page buttons, page size options will not be displayed
    * Clean up contact list data for testing

  @Contact @AC3 @TC8
  Scenario: Verify user should able to go to previous and next page if total contact list more than current page size
    * Load test data: DataFile = ListContacts, Data = AC38
    Given Create a contact list and import datasource by API
    And The Contact list URL is hit to view the existing contact
    When Click Hyper link on total contact with contact list
    Then Verify user should able to go to previous and next page
    * Clean up contact list data for testing

  @Contact @AC3 @TC9
  Scenario: The message displayed on the pagination is "Record list is empty" if no records are configured
    * Load test data: DataFile = ListContacts, Data = AC39
    * Create a contact list by API
    Given The Contact list URL is hit to view the existing contact
    When Click View contact option on three dot dropdown of contact list
    Then The message Record list is empty displayed on the pagination because there are no records configured
    * Clean up contact list data for testing

  @Contact @AC4 @TC1
  Scenario: The default sort for contact browser page is by createdOn/updatedOn and in descending order
    * Load test data: DataFile = ListContacts, Data = AC41
    Given Create a contact list and import datasource by API
    And The Contact list URL is hit to view the existing contact
    When Click Hyper link on total contact with contact list
    Then Verify records name is sort by createdOn (updatedOn) and in descending order
    * Clean up contact list data for testing

  @Contact @AC5 @TC1
  Scenario: Verify that user can sort any column with sort symbol display on header table
    * Load test data: DataFile = ListContacts, Data = AC51
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Given Go to Contact browser page of contact list
    Then Sort all column on header and verify sort function is working
    * Clean up contact list data for testing

  @Contact @AC5 @TC2
  Scenario: Verify that sort function will be applied on all page contact
    * Load test data: DataFile = ListContacts, Data = AC52
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Given Go to Contact browser page of contact list
    Then Sort by ascending order and verify that all page are applied follow this sort
    And Sort by descending order and verify that all page are applied follow this sort
    * Clean up contact list data for testing

  @Contact @AC6 @TC1
  Scenario: Verify that refresh button display on contact landing page
    * Load test data: DataFile = ListContacts, Data = AC61
    * Create a contact list by API
    * The Contact list URL is hit to view the existing contact
    Given Go to Contact browser page of contact list
    Then Refresh button will be displayed
    * Clean up contact list data for testing

  @Contact @AC6 @TC2
  Scenario: Verify that user will be back to the first page after clicking refresh button
    * Load test data: DataFile = ListContacts, Data = AC62
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Given Go to Contact browser page of contact list
    When Go to page "2" and click refresh button
    Then User should be backed to first page
    * Clean up contact list data for testing

  @Contact @AC6 @TC3
  Scenario: Verify that user will be back to default sort option after clicking refresh button
    * Load test data: DataFile = ListContacts, Data = AC63
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Given Go to Contact browser page of contact list
    When Click sort on column "First Name" and click refresh button
    Then User should be backed to default sort option of column "First Name"
    * Clean up contact list data for testing

  @Contact @AC6 @TC4
  Scenario: Verify that user will be back to default page size after clicking refresh button
    * Load test data: DataFile = ListContacts, Data = AC64
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Given Go to Contact browser page of contact list
    Then Change page size to "20,50,100" values one by one and click refresh button then verify user is backed to default page size
    * Clean up contact list data for testing

  @Contact @AC7 @TC1 @IgnoreInDailyRun
  Scenario: When the user tries to click the sort button but the browser disconnects from the server, system should display the one generic error message
    * Load test data: DataFile = ListContacts, Data = AC71
    * Create a contact list and import datasource by API
    Given The Contact list URL is hit to view the existing contact
    And Click Hyper link on total contact with contact list
    When There is a problem that cannot connect to the backend
    And Click to firstName column
    Then An unexpected error occurred with the message: "The application has encountered an unknown error. Please try again later!"
    * Clean up contact list data for testing

  @Contact @AC7 @TC2 @IgnoreInDailyRun
  Scenario: When the user tries to click the refresh button but the browser disconnects from the server, system should display the one generic error message
    * Load test data: DataFile = ListContacts, Data = AC72
    Given The Contact list URL is hit to view the existing contact
    And Create a contact list and import datasource by API
    And Click View contact option on three dot dropdown of contact list
    When There is a problem that cannot connect to the backend
    And Click to refresh button
    Then An unexpected error occurred with the message: "The application has encountered an unknown error. Please try again later!"
    * Clean up contact list data for testing

  @Contact @AC7 @TC3 @IgnoreInDailyRun
  Scenario: When the user tries to click the pagination option button but the browser disconnects from the server, system should display the one generic error message
    * Load test data: DataFile = ListContacts, Data = AC73
    * Create a contact list and import datasource by API
    Given The Contact list URL is hit to view the existing contact
    And Click Hyper link on total contact with contact list
    When There is a problem that cannot connect to the backend
    And Change the page size to 50
    Then An unexpected error occurred with the message: "The application has encountered an unknown error. Please try again later!"
    * Clean up contact list data for testing
