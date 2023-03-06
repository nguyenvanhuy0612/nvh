@IXOUTREACH-3983 @IXOUTREACH-4766 @P1
@ContactList @Sanity
Feature: IXOUTREACH-4766_Create contact_list

  @Contact @AC1_TC1 @IXOUTREACH-4801
  Scenario: Verify button Create Contact List display on landing page
    Given The Contact list URL is hit to view the existing contact
    Then  Verify button Add New Contact List display

  @Contact @AC1_TC2
  Scenario: Verify the new page to create contact list should be displayed
    Given The Contact list URL is hit to view the existing contact
    When Click to Create Contact List Button
    Then Should get one new page to create the contact list

  @Contact @AC1_TC3
  Scenario: Verify Hint message display when click Save button with contact list have special character.
    Given The Contact list URL is hit to view the existing contact
    When Click to Create Contact List Button
    Then Try entering the characters "`~!@#$%^&*()+=[]\\{}|;':\",./<>? " one by one in the Name field, then verify the hint error message appears and the contact list cannot be created with these characters

  @Contact @AC1_TC4
  Scenario: Verify the Name field should be displayed with highlight as mandatory field, allowing up to 40 characters including underscores, dashes, alphanumerics
    Given The Contact list URL is hit to view the existing contact
    When Click to Create Contact List Button
    Then The Name field should be displayed with highlight as mandatory field
    And Verify Name field allowing up to 40 characters including underscores, dashes, alphanumerics

  @Contact @AC1_TC5
  Scenario: Verify the Description field should be displayed with not highlight as optional field, allowing up to 128 characters including blank
    Given The Contact list URL is hit to view the existing contact
    When Click to Create Contact List Button
    Then The Description field should be displayed
    And Verify Description field allowing up to 128 characters

  @Contact @AC2_TC1
  Scenario: Verify a contact list is created successfully after click Save button
    * Load test data: DataFile = ContactList, Data = Create_CTL_AC2_TC1
    * Clean up contact list data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Verify user is in Attribute langding page of contact list added
    * Clean up contact list data for testing

  @Contact @AC2_TC2
  Scenario: Verify admin can create a contact list with Name only, Description is empty
    * Load test data: DataFile = ContactList, Data = Create_CTL_AC2_TC2
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name field and leaves the Description field blank in the contact list form and click on Save button
    Then Message should be displayed that - contact list is created successfully
    * Clean up contact list data for testing

  @Contact @AC2_TC3
  Scenario: Verify admin can create a contact list with Name and Description
    * Load test data: DataFile = ContactList, Data = Create_CTL_AC2_TC3
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Message should be displayed that - contact list is created successfully
    * Clean up contact list data for testing

  @Contact @AC3_TC1
  Scenario: Verify that Cancellation confirmation box will be display correctly when click Cancel button on Add New Contact List page
    * Load test data: DataFile = ContactList, Data = Create_CTL_AC3_TC1
    Given The Contact list URL is hit to view the existing contact
    When Click to Create Contact List Button
    And Provides name and description in the contact list form
    And Click to Cancel Button
    Then Cancellation confirmation box displays

  @Contact @AC3_TC2
  Scenario: Verify information on Cancellation confirmation box display correctly
    * Load test data: DataFile = ContactList, Data = Create_CTL_AC3_TC2
    Given The Contact list URL is hit to view the existing contact
    When Click to Create Contact List Button
    And Provides name and description in the contact list form
    And Click to Cancel Button
    Then Information on Cancellation confirmation box display correctly

  @Contact @AC4_TC1
  Scenario: Verify that user will lanb back to the Contact list page when click OK on Cancellation confirmation box
    * Load test data: DataFile = ContactList, Data = Create_CTL_AC4_TC1
    Given The Contact list URL is hit to view the existing contact
    When Click to Create Contact List Button
    And Provides name and description in the contact list form
    And Click to Cancel Button
    And Click "Leave This Page" on Cancellation confirmation box
    Then User is backed to the contact list page

  @Contact @AC5_TC1
  Scenario: Verify that admin still stay on Add New Contact List page when click Cancel on Cancellation confirmation box
    * Load test data: DataFile = ContactList, Data = Create_CTL_AC5_TC1
    Given The Contact list URL is hit to view the existing contact
    When Click to Create Contact List Button
    And Provides name and description in the contact list form
    And Click to Cancel Button
    And Click "Stay On This Page" on Cancellation confirmation box
    Then User still stay on Add New Contact List page

  @Contact @AC7_TC1
  Scenario: Verify Hint message display when click Save button with contact list name already exists.
    * Load test data: DataFile = ContactList, Data = Create_CTL_AC7_TC1
    * Clean up contact list data for testing
    Given The Contact list URL is hit to view the existing contact
    When Create Contact List with information Name and Description
    And Create Contact List with existed name "none insensitive case"
    Then Hint message display "Contact List with given name already exists." under name field
    * Clean up contact list data for testing

  @IXOUTREACH-6542 @Contact
  Scenario: Do not allow duplicate contact list name - handle create case - insensitive
    * Load test data: DataFile = ContactList, Data = ContactList-Handle-Insensitive
    * Clean up contact list data for testing
    Given The Contact list URL is hit to view the existing contact
    When Create a contact list by API
    And Create Contact List with existed name "insensitive case"
    Then Hint message display "Contact List with given name already exists." under name field
    * Clean up contact list data for testing
