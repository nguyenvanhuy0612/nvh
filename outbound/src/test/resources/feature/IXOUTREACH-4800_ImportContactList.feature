@IXOUTREACH-3983 @IXOUTREACH-4800
Feature: Story IXOUTREACH-4800 Import Contacts from SFTP Data Source with Basic Validations
##############################################################################
##############################################################################
##############################################################################

  @Contact @ContactListImport @P1
  Scenario: Verify we can import list using FTP data source
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList01
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC1 @TC1 @P1
  Scenario: Verify the Import contacts option is displayed when user click option button on one contact list
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC1
    Given The Contact list URL is hit to view the existing contact
    Given The Contact list is created
    And Required user attributes created in contact list
    When user search contact list
    When user click more actions
    Then Verify the "Import Contacts" is displayed
    Then clean the contact list

  @Contact @ContactListImport @AC3 @TC6 @P1
  Scenario: Verify the contact list page GUI is displayed two columns named Last Updated and Total Contacts
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC6
    Given The Contact list URL is hit to view the existing contact
    Given The Contact list is created
    And Required user attributes created in contact list
    When User clicks the refresh button on contact list page
    Then Verify two columns named Last Updated and Total Contacts are displayed
    Then clean the contact list

  @Contact @ContactListImport @AC2 @TC2 @P2
  Scenario: Verify list of datasource should displayed when user click on Import Contacts option
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactListTest
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    When User select contact list and clicks import contact
    Then Verify Import Contacts page is displayed
    Then clean the contact list


  @Contact @ContactListImport @AC3 @TC5 @P1
  Scenario: Verify user can import contact list successfully
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC5
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    Then Verify User should be redirected to "Contact List" landing page
    Then contact list page should show import in progress status
    And user search contact list
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list


  @Contact @ContactListImport @AC3 @TC7 @P1
  Scenario: Verify the Last Update the status of contact list during importing is In-progress
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC7
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    Then Verify User should be redirected to "Contact list" landing page
    And user search contact list
    And contact list page should show import in progress status
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC2 @TC3 @P3
  Scenario: Verify user can able to select one data source to Import Contacts
    * Load test data: DataFile = ContactListImportTestData, Data = SelectDataSource
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    When User select contact list and clicks import contact
    Then Users able to select one data source from dropdown list
    Then clean the contact list


  @Contact @ContactListImport @AC3 @TC8 @P1
  Scenario: Verify the Last Update the status of contact list after importing is Timestamp
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC8
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    Then Verify User should be redirected to "Contact list" landing page
    And contact list page should show import in progress status
    And user search contact list
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC3 @TC9 @P1
  Scenario: Verify the Total Contacts the status of contact list during importing is number of contacts
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC9
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    Then Verify User should be redirected to "Contact list" landing page
    And contact list page should show import in progress status
    And user search contact list
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC3 @TC10 @P1
  Scenario: Verify Columns Last Updated when import datasource failed
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC10
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    And The Contact list URL is hit to view the existing contact
    Then Verify User should be redirected to "Contact List" landing page
    And user search contact list
    And contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list


  @Contact @ContactListImport @AC3 @TC11 @P1
  Scenario: Verify Last Updated column when import datasource failed with second import
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC11
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    And contact list page should show import in progress status
    And user search contact list
    Then once import is completed last import status should be updated
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC11_Failed
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    And The Contact list URL is hit to view the existing contact
    Then Verify User should be redirected to "Contact List" landing page
    And user search contact list
    And contact list page should show import in progress status
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC11
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list
    Then clean the files on SFTP server
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC11_Failed
    Then clean the contact list
    Then clean the files on SFTP server

  @Contact @ContactListImport @AC4 @TC12 @P1
  Scenario: Verify import datasource with invalid Phone number include non-numeric characters
    * Load test data: DataFile = ContactListImportTestData, Data = DsWithInvalidCharacter
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When User select contact list and clicks import contact
    Then Users able to select one data source from dropdown list
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P2
  Scenario: Verify import datasource with invalid email in few records
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactListInvalidEmail_01
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P2
  Scenario: Verify import datasource with invalid phone in few records
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactListInvalidPhone_01
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1
  Scenario: Verify import datasource with invalid phone and email in few records
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_PhoneEmailValidation01
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC5 @TC13 @P3
  Scenario: Verify user can import datasource with all valid Default Business attributes
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_Valid_Default_Attribute
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC4 @TC18 @P1
  Scenario: Verify import datasource with invalid field separator
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_Invalid_Field_Separator
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC5 @TC25 @P3
  Scenario: Verify import datasource with all valid Default Business attributes and non Default attributes
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_IncludeNonDefault_ValidAttribute
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC5 @TC26 @P3
  Scenario: Verify import datasource with CSV file lack of Field First Name
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_LackOf_Field_Firstname
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC5 @TC27 @P3
  Scenario: Verify import datasource with CSV file lack of Field Last Name
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_LackOf_Field_Lastname
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC5 @TC28 @P2
  Scenario: Verify import datasource with CSV file lack of Field Phone 1
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_LackOf_Field_Phone_1
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC5 @TC28 @P3
  Scenario: Verify import datasource with CSV file lack of Field Email
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_LackOf_Field_Email
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC5 @TC28 @P3
  Scenario: Verify import datasource with CSV file have several empty fields
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_With_Empty_Field
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC5 @TC23 @P3
  Scenario: Verify the the Create_on is automated creation to show timestamp at which contact on list
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC23
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    Then contact list page should show import in progress status
    And user search contact list
    Then once import is completed last import status should be updated
    Then Verify records show timestamp on Create on column
    Then clean the files on SFTP server
    Then clean the contact list


  @Contact @ContactListImport @AC6 @TC30 @P1
  Scenario: Verify user can import datasource on existing contact list
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC30
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    And contact list page should show import in progress status
    Then once import is completed last import status should be updated
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC30_exist
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    And contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1
  Scenario: Verify system clean existing contacts from list when new import is run
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactListReimport_01
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    And new updated contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify existing data is deleted and new list is imported
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1
  Scenario: Verify id is unique in imported contact list
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactListuniqueid
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify id is unique in imported list
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @AC5 @TC28 @P3
  Scenario: Verify import datasource with CSV file with missing headers
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_Missing_Header
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1
  Scenario: Verify in contact import if field separator in inside data is wrapped with double quote then record is imported successfully
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactListquotes
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1
  Scenario: Verify import contact with field Name in difference language
    * Load test data: DataFile = ContactListImportTestData, Data = ImportListForeign
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P2
  Scenario: Verify import 2 contact list with same datasource file
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList01
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList02
    When user create contact list
    And Required user attributes created in contact list
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully and all contacts are correct
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList01
    Then clean the files on SFTP server
    Then clean the contact list
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList02
    Then clean the contact list


  @Contact @ContactListImport @AC6 @TC32 @P3
  Scenario: Verify user can not import the records with invalid email on existing contact list
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC32
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    And contact list page should show import in progress status
    And user search contact list
    Then once import is completed last import status should be updated
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC32_invalid_email
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    And contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list


  @Contact @ContactListImport @AC6 @TC33 @P3
  Scenario: Verify user can import datasource on existing contact list with phone cleaned up
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC33
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    And contact list page should show import in progress status
    Then once import is completed last import status should be updated
    * Load test data: DataFile = ContactListImportTestData, Data = VerifyImportContactOption_TC33_Phone_CleanUp
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    And contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list


  @Contact @ContactListImport @P2
  Scenario: Verify import contact with different header reorder
    * Load test data: DataFile = ContactListImportTestData, Data = ImportListReorderHeader
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P2
  Scenario: Verify import contact no csv file on sftp server
    * Load test data: DataFile = ContactListImportTestData, Data = ImportListNoSourceFile
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    When user run data source
    Then clean the contact list

  @Contact @ContactListImport @P2
  Scenario: Verify import contact with datasource remote path has not define csv file
    * Load test data: DataFile = ContactListImportTestData, Data = ImportListNoDefineCSVFile
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P2
  Scenario: Verify import contact with datasource have no configure remote path
    * Load test data: DataFile = ContactListImportTestData, Data = ImportListNoRemotePath
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P2
  Scenario: Verify import datasource with CSV file with empty file
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_Empty_File
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list


  @Contact @ContactListImport @P1
  Scenario: Verify import contact list with 10k contacts in csv
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList10k
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully and all contacts are correct
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1 @IXOUTREACH-4801
  Scenario: Show "count of current contacts" instead of "In progress" for running import contacts
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList10k
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then Show "count of current contacts" instead of "In progress" for running import contacts in Total Contacts column
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1 @IXOUTREACH-6357
  Scenario: Verify user can import contact list with all data attribute type
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList_AllDataAttType
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully and all contacts are correct
    Then clean the files on SFTP server
    Then Clean the testcase data

  @Contact @ContactListImport @P1 @IXOUTREACH-6357
  Scenario: Verify user can import contact list with hyphen attribute name
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList_HyphenAttName
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully and all contacts are correct
    Then clean the files on SFTP server
    Then Clean the testcase data

  @Contact @ContactListImport @P1 @IXOUTREACH-6357
  Scenario: Verify user can import contact list with G-14 languages
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList_G14Language
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully and all contacts are correct
    Then clean the files on SFTP server
    Then Clean the testcase data

  @Contact @ContactListImport @P1 @IXOUTREACH-6357
  Scenario: Verify user can import contact list with child attribute values
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList_ChildAttValue
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully and all contacts are correct
    Then clean the files on SFTP server
    Then Clean the testcase data

  @Contact @ContactListImport @P1 @IXOUTREACH-6357 @AC6
  Scenario: Verify all system attributes are displayed on contact list view page
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList_sysHeader
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify all system attributes are displayed
    * clean the files on SFTP server
    * Clean the testcase data

  @Contact @ContactListImport @P1 @IXOUTREACH-6357 @AC6
  Scenario: Verify all user attributes are displayed on contact list view page
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList_userHeader
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify all user attributes are displayed
    * clean the files on SFTP server
    * Clean the testcase data

  @Contact @ContactListImport @P1 @IXOUTREACH-6357 @AC6
  Scenario: Verify all user attributes are sorted by name
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList_sortHeader
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify all attributes are sorted by name
    * clean the files on SFTP server
    * Clean the testcase data

  @Contact @ContactListImport @P1 @IXOUTREACH-6357
  Scenario: Verify import with few invalid values for each data type
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList_InvalidAtt
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully and all contacts are correct
    Then clean the files on SFTP server
    Then Clean the testcase data

  @Contact @ContactListImport @P1 @IXOUTREACH-6357
  Scenario: Verify import with all data type having large data in each field
    * Load test data: DataFile = ContactListImportTestData, Data = ImportContactList_LargeAtt
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully and all contacts are correct
    Then clean the files on SFTP server
    Then Clean the testcase data

  @Contact @Validation @P1 @IXOUTREACH-6795 @AC3
  Scenario: Verify user import contact list include system attribute
    * Load test data: DataFile = ContactListImportTestData, Data = Validation_sysFailed
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify the error icon is displayed with failed reason
    * clean the files on SFTP server
    * Clean the testcase data

  @Contact @Validation @P1 @IXOUTREACH-6795 @AC3
  Scenario: Verify user import allowed predefine attributes successfully
    * Load test data: DataFile = ContactListImportTestData, Data = Validation_allowedPredefine
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully and all contacts are correct
    * clean the files on SFTP server
    * Clean the testcase data

  @Contact @Validation @P1 @IXOUTREACH-6795 @AC3
  Scenario: Verify contact list import not allowed for predefine attributes not allowed to import
    * Load test data: DataFile = ContactListImportTestData, Data = Validation_notAllowedPredefine
    Given user create contact list
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify the error icon is displayed with failed reason
    * clean the files on SFTP server
    * Clean the testcase data

  @Contact @ContactListImport @P1 @IXOUTREACH-6795 @AC2
  Scenario: Verify import with attributes in csv are not present in contact List
    * Load test data: DataFile = ContactListImportTestData, Data = ClistHaveNoAttrs
    Given The Contact list is created
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1 @IXOUTREACH-6795 @AC2
  Scenario: Verify import contact list CSV with predefined as well as user defined contact attributes
    * Load test data: DataFile = ContactListImportTestData, Data = ImportCSV_AllPredefine_Attr
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1 @IXOUTREACH-6795 @AC2
  Scenario: Verify if record in csv does not have all fileds then record should be rejected
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_wRecords_Missing_Column
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1 @IXOUTREACH-6795 @AC2
  Scenario: Verify if csv file have empty line
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_CSVHaveEmptyLine
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1 @IXOUTREACH-6795 @AC2
  Scenario: Verify import with attribute name in csv is in different case sensitive
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_Case_Sensitive
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then Verify list is imported successfully
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1 @IXOUTREACH-6795 @AC2
  Scenario: Verify import with few attribute not exit in csv and few not exist in contact list
    * Load test data: DataFile = ContactListImportTestData, Data = Import_CSV_MissMapping_Attrs
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P2
  Scenario: Verify import datasource with CSV file with all records rejected
    * Load test data: DataFile = ContactListImportTestData, Data = ImportList_allrecordsRejected
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When user run data source
    And The Contact list URL is hit to view the existing contact
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list
