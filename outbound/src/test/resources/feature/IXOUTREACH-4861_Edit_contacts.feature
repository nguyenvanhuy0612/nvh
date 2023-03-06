@IXOUTREACH-4861
Feature: Story IXOUTREACH-4861 Edit contacts
##############################################################################
##############################################################################
##############################################################################



  @Contact @EditContact @P1
  Scenario: Verify edi contact for all data type fields
    * Load test data: DataFile = EditContactTestData, Data = EditContactUI02
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then edit the contact fields
    And Change the page size to 100
    Then click on edit contact for edited contact
    Then verify contact is edited successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1
  Scenario: Verify edit contact page for predefined editable attributes
    * Load test data: DataFile = EditContactTestData, Data = EditContactPredeinfed
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then edit the contact fields
    And Change the page size to 100
    Then click on edit contact
    Then verify contact is edited successfully
    Then clean the files on SFTP server
    Then clean the contact list


  @Contact @EditContact @P1 @AC1 @AC2
  Scenario: Verify edit contact page for system type attribute and not allowed predefined attributes
    * Load test data: DataFile = EditContactTestData, Data = EditContact02
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then Verify edit form should be available to user to modify the contact
    Then Verify System attributes should not be shown for modification
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1 @AC6
  Scenario: Verify save and cancel button is displayed on edit contacts page
    * Load test data: DataFile = EditContactTestData, Data = saveAndCancelBtnValidate
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then Verify the save and cancel button is available
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1 @AC7 @AC8
  Scenario: Verify cancel form when user click leave this page
    * Load test data: DataFile = EditContactTestData, Data = cancelLeave
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then edit the contact fields
    Then Click to "cancel" Button
    Then Verify cancel form on edit contacts page with "Leave this page" action
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1 @AC7 @AC9
  Scenario: Verify cancel form when user click stay on this page
    * Load test data: DataFile = EditContactTestData, Data = cancelStay
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    When once import is completed last import status should be updated
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then edit the contact fields
    Then Click to "cancel" Button
    Then Verify cancel form on edit contacts page with "Stay on this page" action
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1 @AC7 @AC9
  Scenario: Verify Attributes are grouped by USER type PHONE type EMAIL type and ZIP type
    * Load test data: DataFile = EditContactTestData, Data = EditContactgroupedatt
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then verify attributes are grouped by user phone email and zip
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1
  Scenario: Verify Edit with invalid values for each data type float Integer String Boolean Time Date DateTime ZIP Phone Email
    * Load test data: DataFile = EditContactTestData, Data = EditContactValidations01
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then edit the contact fields
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1
  Scenario: Verify Edit with all data type having large data in email
    * Load test data: DataFile = EditContactTestData, Data = EditContactLargeEmail
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then edit the contact fields
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1
  Scenario: Verify Edit with all data type having large data in integer
    * Load test data: DataFile = EditContactTestData, Data = EditContactLargeInteger
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then edit the contact fields
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1
  Scenario: Verify Edit with all data type having large data in float
    * Load test data: DataFile = EditContactTestData, Data = EditContactLargeFloat
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then edit the contact fields
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @EditContact @P1
  Scenario: Verify edi contact for all data type fields empty
    * Load test data: DataFile = EditContactTestData, Data = EditContactUIAllEmpty
    * Create a contact list and import datasource by API
    * The Contact list URL is hit to view the existing contact
    Then Go to Contact browser page of contact list
    And Change the page size to 100
    Then click on edit contact
    Then edit the contact fields
    Then click on edit contact for edited contact
    Then verify contact is edited successfully
    Then clean the files on SFTP server
    Then clean the contact list
