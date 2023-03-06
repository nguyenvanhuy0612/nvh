@IXOUTREACH-5180
Feature: IXOUTREACH-5180_Import750KContactList

  @Contact @ContactListImport @P1 @IgnoreInDailyRun
  Scenario: Verify we can import 750k list using FTP data source
    * Load test data: DataFile = ContactListImportTestData, Data = Import750kContactList01
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv having 750k records available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1 @IgnoreInDailyRun
  Scenario: Verify we can import 750k list using FTP data source with phone and email validations
    * Load test data: DataFile = ContactListImportTestData, Data = Import750kContactList02
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv having 750k records available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list

  @Contact @ContactListImport @P1 @IgnoreInDailyRun
  Scenario: Verify we can reimport 750k list using FTP data source
    * Load test data: DataFile = ContactListImportTestData, Data = ReImport750kContactList02
    Given The Contact list is created
    And Required user attributes created in contact list
    And The SFTP data source is created
    And contactlist csv having 750k records available on SFTP server
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    When user run data source
    Then contact list page should show import in progress status
    Then once import is completed last import status should be updated
    Then clean the files on SFTP server
    Then clean the contact list
