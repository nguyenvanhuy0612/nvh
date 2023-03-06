@IXOUTREACH-3983 @IXOUTREACH-4770 @P1
@ContactList @Sanity
Feature: Story IXOUTREACH-4770 List contact lists
  The set of test cases to verification the list contact list


  @Contact @AC1 @TC1
  Scenario: Verify the Name label will be displayed on the header Table
    Given The Contact list URL is hit to view the existing contact
    Then  Name displayed on header Table

  @Contact @AC1 @TC2
  Scenario:  Verify contact table displays correctly without contact list display on landing page
    Given The Contact list URL is hit to view the existing contact
    When There are no contact list display on contact list landing page
    Then Message "Record list is empty" is displayed in place of row on contact list landing page

  @Contact @AC2_TC1
  Scenario: Verify The default display order of contact list should be based on update time of the contact list
    * Load test data: DataFile = ContactList, Data = ContactList_AC2_TC1
    Given The Contact list URL is hit to view the existing contact
    When Create a list 4 contact lists by API
    Then The default display order of contact list displayed based on update time of the contact list
    * Clean up contact list data for testing

  @Contact @AC3_TC1
  Scenario: Verify fields Name should toggle between Asc and Desc orders with UI indication transitioning from one order to another when clicked.
    * Load test data: DataFile = ContactList, Data = ContactList_AC3_TC1
    Given The Contact list URL is hit to view the existing contact
    When Create a list 4 contact lists by API
    And Search contact list with name
    Then The "Name" column toggle between Asc and Desc orders with UI indication transitioning from one order to another when clicked
    * Clean up contact list data for testing

  @Contact @AC3_TC2
  Scenario: Verify order contact list should display correctly when click sort option with Name column
    * Load test data: DataFile = ContactList, Data = ContactList_AC3_TC2
    Given The Contact list URL is hit to view the existing contact
    When Create a list 4 contact lists by API
    And Search contact list with name
    Then "Name" column should be sorted in desired order as per the admin's selection.
    * Clean up contact list data for testing

  @Contact @AC3_TC3
  Scenario: Verify order contact list should display correctly after toggle Asc and Desc orders
    * Load test data: DataFile = ContactList, Data = ContactList_AC3_TC3
    Given The Contact list URL is hit to view the existing contact
    When Create a list 4 contact lists by API
    And Search contact list with name
    Then Default order contact list of "Name" column displayed correctly after toggle Asc and Desc orders and go back to the default
    * Clean up contact list data for testing

  @Contact @AC3_TC4
  Scenario: Verify sort should happen across all the pages
    * Load test data: DataFile = ContactList, Data = ContactList_AC3_TC4
    Given The Contact list URL is hit to view the existing contact
    When Create a list 42 contact lists by API
    And Search contact list with name
    Then The Sort work correctly with different page and "Name" header
    * Clean up contact list data for testing

  @Contact @AC4_TC1
  Scenario: Verify name contact list should display correctly on contact table
    * Load test data: DataFile = ContactList, Data = ContactList_AC4_TC1
    Given The Contact list URL is hit to view the existing contact
    When Create a contact list by API
    Then "Name" of contact list displayed correctly with information contact list created
    * Clean up contact list data for testing

  @Contact @AC4_TC2
  Scenario: Verify number record, page number, previous and next page button, page size options be displayed correctly
    * Load test data: DataFile = ContactList, Data = ContactList_AC4_TC2
    Given The Contact list URL is hit to view the existing contact
    When Create a contact list by API
    Then Page size options should be displayed
    And Page number should be displayed
    And Previous and next button should be displayed
    And Page detail should be displayed
    * Clean up contact list data for testing

  @Contact @AC4_TC3
  Scenario: Verify default page size value in contact list page is 10
    * Load test data: DataFile = ContactList, Data = ContactList_AC4_TC3
    Given Create a contact list by API
    When The Contact list URL is hit to view the existing contact
    Then Page size value displays "10" by default
    * Clean up contact list data for testing

  @Contact @AC4_TC4
  Scenario: Verify that user should able to change page size at contact list page
    * Load test data: DataFile = ContactList, Data = ContactList_AC4_TC4
    * Create a list 21 contact lists by API
    Given The Contact list URL is hit to view the existing contact
    Then Change the page size "10,20,50,100" values one by one then page size value display correctly
    * Clean up contact list data for testing

  @Contact @AC4_TC5
  Scenario: Verify each page should list the number of records correctly with page size selected
    * Load test data: DataFile = ContactList, Data = ContactList_AC4_TC5
    * Create a list 21 contact lists by API
    Given The Contact list URL is hit to view the existing contact
    Then Change the page size "10,20,50,100" values one by one then verify number of records each page display correctly
    * Clean up contact list data for testing

  @Contact @AC4_TC6
  Scenario: Verify page number display correclty with page size selected
    * Load test data: DataFile = ContactList, Data = ContactList_AC4_TC6
    * Create a list 21 contact lists by API
    Given The Contact list URL is hit to view the existing contact
    Then Change the page size "10,20,50,100" values one by one then verify number of page display correctly
    * Clean up contact list data for testing

  @Contact @AC4_TC7
  Scenario: Verify user should able to go to previous and next page if total contact list more than current page size
    * Load test data: DataFile = ContactList, Data = ContactList_AC4_TC7
    * Create a list 21 contact lists by API
    Given The Contact list URL is hit to view the existing contact
    Then Change the page size "10" values one by one then next and previous page button work fine
    * Clean up contact list data for testing

  @Contact @AC4_TC8
  Scenario: Verify error message "Please enter valid page name" will be displayed when input invalid page number into go to page field
    * Load test data: DataFile = ContactList, Data = ContactList_AC4_TC8
    * Create a list 21 contact lists by API
    Given The Contact list URL is hit to view the existing contact
    Then Input page number value "0,99999" which is not exist on landing page into Go to page field and verify message display
    * Clean up contact list data for testing

  @Contact @AC5 @TC1
  Scenario: Verify refresh button will be displayed on contact list page
    Given The Contact list URL is hit to view the existing contact
    Then Refresh button will be displayed

  @Contact @AC5 @TC2
  Scenario: Verify The default Sort order is on Last modified On in descending order after clicking refresh button
    * Load test data: DataFile = ContactList, Data = ContactList_AC5_TC2
    Given The Contact list URL is hit to view the existing contact
    When Create a contact list by API
    And Admin should landed to contact list page
    And Click to refresh button
    Then Contacts added will show up at top of table
    * Clean up contact list data for testing

  @Contact @AC5 @TC3
  Scenario: Verify user be backed to the first page with default sort option when clicking refresh button
    * Load test data: DataFile = ContactList, Data = ContactList_AC5_TC3
    * Create a list 25 contact lists by API
    Given The Contact list URL is hit to view the existing contact
    And Click to next page button
    And Click to refresh button
    Then User be back to first page with default sort option
    * Clean up contact list data for testing

  @Contact @AC5 @TC4
  Scenario: Verify default page size is restored to default of 10 when refresh button is clicked
    * Load test data: DataFile = ContactList, Data = ContactList_AC5_TC4
    * Create a list 25 contact lists by API
    Given The Contact list URL is hit to view the existing contact
    Then Change the page size to "10,20,50,100" values one by one then click refresh button then make sure the page size restore back to 10 as default
    * Clean up contact list data for testing

  @Contact @AC6 @TC1 @IgnoreInDailyRun
  Scenario: Verify unexpected message displays in unexpected scenario when sorting
    Given The Contact list URL is hit to view the existing contact
    And There is a problem that cannot connect to the backend
    When Click to Name column
    Then An unexpected error occurred with the message: "The application has encountered an unknown error. Please try again later!"

  @Contact @AC6 @TC2 @IgnoreInDailyRun
  Scenario: Verify unexpected message displays in unexpected scenario when clicking refresh button
    Given The Contact list URL is hit to view the existing contact
    And There is a problem that cannot connect to the backend
    When Click to refresh button
    Then An unexpected error occurred with the message: "The application has encountered an unknown error. Please try again later!"

  @Contact @AC6 @TC3 @IgnoreInDailyRun
  Scenario: Verify unexpected message displays in unexpected scenario when selecting pagination option button
    * Load test data: DataFile = ContactList, Data = ContactList_AC6_TC3
    * Create a list 4 contact lists by API
    Given The Contact list URL is hit to view the existing contact
    And There is a problem that cannot connect to the backend
    When Change the page size to 50
    Then An unexpected error occurred with the message: "The application has encountered an unknown error. Please try again later!"
    * Clean up contact list data for testing

  @Contact @AC7 @TC1
  Scenario:Verify header, pagination, refresh button should display with no contact list display on the contact list landing page
    Given The Contact list URL is hit to view the existing contact
    When There are no contact list display on contact list landing page
    Then Name displayed on header Table
    And Message "Record list is empty" is displayed in place of row on contact list landing page
    And Refresh button will be displayed

  @Contact @IXOUTREACH-4801 @P3
  Scenario: Date for last updated on column should be locale specific
    * Load test data: DataFile = ContactList, Data = contact_list1
    * Create a contact list by API
    Given The Contact list URL is hit to view the existing contact
    Then Change the time zone and verify the time in the Last Updated column is changed respectively
      | India Standard Time | Fiji Standard Time | SE Asia Standard Time | UTC | Central Europe Standard Time | Central Brazilian Standard Time |
    * Clean up contact list data for testing

  @Contact @IXOUTREACH-6020 @P4
  Scenario: Verify Quick search on contact list allows 40 characters
    * Load test data: DataFile = ContactList, Data = contact_list2
    Given The Contact list URL is hit to view the existing contact
    And Search contact list with name
    Then Verify searched contact list length
      | SearchStringLength | 40 |

  @Contact @IXOUTREACH-6458 @IgnoreInDailyRun
  Scenario: Verify failure toast message should not display when input special character on text box search
    Given The Contact list URL is hit to view the existing contact
    Then Failure toast message is not display when user try entering the characters "`~!@#$%^&*()-_+=[]\\{}|;':\",./<>? " in text box "basic" search
    And Failure toast message is not display when user try entering the characters "`~!@#$%^&*()-_+=[]\\{}|;':\",./<>? " in text box "advanced" search
