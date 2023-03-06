@IXOUTREACH-3983 @IXOUTREACH-4057 @Sanity @P1
Feature: Story IXOUTREACH-4057 Create contact data source - SFTP

##############################################################################
##############################################################################
##############################################################################

  @Contact @DataSource @TC1 @AC1
  Scenario: Verify DataSource URL is hit then DataSource page is loaded
    Given Admin hit the Data source URL
    Then Verify Data source page is loaded
    Then Verify New data source button is displayed on landing page

  @Contact @DataSource @TC2 @AC2
  Scenario: Verify the require options are available on Data Source configuration
    Given Admin hit the Data source URL
    When Admin user clicks "New Data Source" button
    Then Verify require fields for Data Source page are displayed
      | Field Name       | Field Description | Default Value |
      | DataSourceName   | Name              | empty         |
      | DataSourceDesc   | Description       | empty         |
      | DataSourceType   | Data Source Type  | N/A           |
      | ServerHostName   | SFTP Host         | empty         |
      | ServerUserName   | Username          | empty         |
      | ServerPassword   | Password          | empty         |
      | ServerRemotePath | Remote Path       | empty         |
      | FieldDelimiter   | Comma (,)         | N/A           |
      | TestConnection   | Test Connection   | N/A           |

  @Contact @DataSource @TC3 @AC2
  Scenario: Verify Data Source Name max length are 40 characters
    Given Admin hit the Data source URL
    When Admin user clicks "New Data Source" button
    When User send test data to field
      | Field Name       | Data Source Name                              |
      | No of Characters | 45                                            |
      | Input Text       | TestDataLengthTestDataLengthTestDataLengthT45 |
    Then Verify max length of field "Data Source Name" is "40"

  @Contact @DataSource @TC4 @AC2
  Scenario: Verify Data Source Description max length are 128 characters
    Given Admin hit the Data source URL
    When Admin user clicks "New Data Source" button
    When User send test data to field
      | Field Name       | Data Source Description                                                                                                           |
      | No of Characters | 129                                                                                                                               |
      | Input Text       | TestDataLengthTestDataLengthTestDataLengthTestDataLengthTestDataLengthTestDataLengthTestDataLengthTestDataLengthTestDataLength129 |
    Then Verify max length of field "Data Source Description" is "128"

  @Contact @DataSource @TC5 @AC2
  Scenario: Verify create Data Source Name with hyphen and underscore characters
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData |
      | Data     | Test_DS_02         |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify new Data Source information are correct on landing page
    * User clean datasource after completing TC

  @Contact @DataSource @TC6 @AC4
  Scenario: Verify create Data Source Name with special characters is not allowed
    Given Admin hit the Data source URL
    When Admin user clicks "New Data Source" button
    When User send test data to field
      | Field Name       | Data Source Name |
      | No of Characters | 129              |
      | Input Text       | TestDS$%&*       |
    Then Verify data source name field does not contain special characters
    And Hint message display "Name should start with alphabet and allowed characters are alphanumeric, hyphen and underscore." is displayed for "name"

  @Contact @DataSource
  Scenario: Verify configure SFTP Host by FQDN address
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData   |
      | Data     | Test_FQDN_Datasource |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify new Data Source information are correct on landing page
    * User clean datasource after completing TC


  @Contact @DataSource @TC7 @AC2
  Scenario: Verify user can not save the datasource when missing datasource name
    Given Admin hit the Data source URL
    Then Admin user add new Data Source when missing some fields
      | DataFile | DataSourceTestData   |
      | Data     | Test_DS_Missing_name |
    And Verify the datasource can not save and the error message is displayed

  @Contact @DataSource @TC8 @AC2
  Scenario: Verify user can not save the datasource when missing FTP/SFTP host name
    Given Admin hit the Data source URL
    Then Admin user add new Data Source when missing some fields
      | DataFile | DataSourceTestData   |
      | Data     | Test_DS_Missing_Host |
    And Verify the datasource can not save and the error message is displayed

  @Contact @DataSource @TC9 @AC2
  Scenario: Verify user can not save the datasource when missing User name of destination
    Given Admin hit the Data source URL
    Then Admin user add new Data Source when missing some fields
      | DataFile | DataSourceTestData      |
      | Data     | Test_DS_Missing_Usename |
    And Verify the datasource can not save and the error message is displayed

  @Contact @DataSource @TC10 @AC2
  Scenario: Verify user can not save the datasource when missing Password of destination
    Given Admin hit the Data source URL
    Then Admin user add new Data Source when missing some fields
      | DataFile | DataSourceTestData |
      | Data     | Test_DS_Missing_Pw |
    And Verify the datasource can not save and the error message is displayed

  @Contact @DataSource @TC11 @AC2
  Scenario: Verify user can not save the datasource when missing Remote path
    Given Admin hit the Data source URL
    Then Admin user add new Data Source when missing some fields
      | DataFile | DataSourceTestData   |
      | Data     | Test_DS_Missing_Path |
    And Verify the datasource can not save and the error message is displayed

  @Contact @DataSource @TC12 @AC2
  Scenario: Verify password field must not be visible in the UI
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData  |
      | Data     | Test_Password_field |
    Then Verify password field must not be visible in the UI

  @Contact @DataSource @TC13 @AC5
  Scenario: Verify user can add new datasource with input mandatory fields only
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData   |
      | Data     | Test_Mandatory_field |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify new Data Source information are correct on landing page
    * User clean datasource after completing TC

  @Contact @DataSource @TC14 @AC5
  Scenario: Verify the DS should be displayed as list item after creating DS successfully and redirecting to landing page
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData |
      | Data     | Test_List_DS1      |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify new Data Source information are correct on landing page
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData |
      | Data     | Test_List_DS2      |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify new Data Source information are correct on landing page
    And Verify two new data source are displayed on list page
      | Data_1 | Test_List_DS1 |
      | Data_2 | Test_List_DS2 |
    * User clean datasource after completing TC

  @Contact @DataSource @TC15 @AC2
  Scenario: Verify configure SFTP Host by IP Address
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData |
      | Data     | Test_Host_IP       |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify new Data Source information are correct on landing page
    * User clean datasource after completing TC

  @Contact @DataSource @TC16 @AC6
  Scenario: Verify user can not save the data source when entering the invalid hostname
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData     |
      | Data     | ContactDataSourceSFTP3 |
    Then Verify the datasource can not save


  @Contact @DataSource @TC17 @AC6
  Scenario: Verify user can save new data source by try add invalid information then correct again
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData     |
      | Data     | ContactDataSourceSFTP4 |
    Then Verify the datasource can not save
    When User update valid information
    When Admin user clicks "Save" button
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify new Data Source information are correct on landing page
    * User clean datasource after completing TC

  @Contact @DataSource @TC18 @AC6
  Scenario: Verify user can not add new datasource with invalid information
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData           |
      | Data     | ContactDataSourceSFTP_NoSave |
    Then Verify the datasource can not save
    Then Admin user clicks "Cancel" button
    And Admin user clicks "Leave this page" button
    Then Verify New data source button is displayed on landing page

  @Contact @DataSource @TC19 @AC6
  Scenario: Verify User cannot add datasource with the same name
    Given Admin hit the Data source URL
    Then User create new Data Source with the same name
      | DataFile | DataSourceTestData |
      | Data     | Test_DS_same_name  |
    And Verify the datasource can not save and the error message is displayed
    * User clean datasource after completing TC

  @Contact @DataSource @TC20 @AC6
  Scenario: Verify User cannot add datasource with special character
    Given Admin hit the Data source URL
    Then Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData        |
      | Data     | Test_DS_Special_character |
    And Verify the datasource can not save and the error message is displayed

  @Contact @DataSource
  Scenario:  Verify user can not save the datasource when remote path is not entered
    Given Admin hit the Data source URL
    Then Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData |
      | Data     | Test_DS_04         |
    Then Hint message display "Remote Path should not be null or blank" is displayed for "ftpRemoteFilePath"

  @Contact @DataSource
  Scenario: Verify create Data Source Host with invalid format is not allowed
    Given Admin hit the Data source URL
    When Admin user clicks "New Data Source" button
    When User send test data to field
      | Field Name       | Data Source Host |
      | No of Characters | 129              |
      | Input Text       | 10.133.84.666    |
    Then Hint message display "Invalid Host for SFTP Server." is displayed for "ftpIPHostName"

  @Contact @DataSource @P1
  Scenario: Verify test connection on FTP type data source
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration and click on test connection
      | DataFile | DataSourceTestData    |
      | Data     | Test_DS_01_connection |
    Then User test connection successful message should be displayed
      | DataFile | DataSourceTestData    |
      | Data     | Test_DS_01_connection |

  @Contact @DataSource @P2
  Scenario: Verify test connection on FTP type data source with invalid input
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration and click on test connection
      | DataFile | DataSourceTestData    |
      | Data     | Test_DS_02_connection |
    Then User test connection failed message should be displayed
      | DataFile | DataSourceTestData    |
      | Data     | Test_DS_02_connection |

  @Contact @DataSource @P2
  Scenario: Verify mandatory filed errors for add data source page
    Given Admin hit the Data source URL
    When Admin user add new Data Source with empty data in mandatory fields
      | DataFile | DataSourceTestData          |
      | Data     | Test_DS_03_Mandatory_fields |
    Then verify error message displayed for mandatory fields
      | DataFile | DataSourceTestData          |
      | Data     | Test_DS_03_Mandatory_fields |

  @Contact @DataSource @TC21 @AC5
  Scenario: Verify the DS is created and listed on the landing page
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData |
      | Data     | Test_DS_034        |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify new Data Source is created on landing page
    * User clean datasource after completing TC

  @Contact @DataSource @TC21 @AC6
  Scenario Outline: Verify error message displayed when user add name include special character
    Given Admin hit the Data source URL
    Then Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData       |
      | Data     | Test_DS_check_field_name |
    And User input name with "<data>" include special character
    When Admin user clicks "Save" button
    And Verify the datasource can not save and the error message is displayed

    Examples:
      | data        |
      | !           |
      | @           |
      | #           |
      | $           |
      | %           |
      | ^           |
      | &           |
      | *           |
      | (           |
      | )           |
      | '           |
      | \"          |
      | :           |
      | ?           |
      | ~           |
      | +           |
      | .           |
      | <           |
      | >           |
      | =           |
      | space space |

  @Contact @DataSource @Story4057 @TC22
  Scenario: Verify disable Test connection button till response received
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration and click on test connection
      | DataFile | DataSourceTestData       |
      | Data     | Test_DS_connection_Valid |
    Then User test connection successful message should be displayed
      | DataFile | DataSourceTestData       |
      | Data     | Test_DS_connection_Valid |

  @Contact @DataSource @IXOUTREACH-6542
  Scenario: Do not allow duplicate data source name - handle create case - insensitive
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData             |
      | Data     | Handle_Create_Case_Insensitive |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData             |
      | Data     | HANDLE_CREATE_CASE_INSENSITIVE |
    And Verify the datasource can not save and the error message is displayed
    And Delete data source
      | DataFile | DataSourceTestData             |
      | Data     | Handle_Create_Case_Insensitive |
