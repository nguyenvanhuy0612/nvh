@IXOUTREACH-6653
Feature: Story IXOUTREACH-6653 Tenant aware Contact Service

  @AC1 @P1 @TC1
  Scenario: API-Verify error message response when user send request GET list contact list with wrong account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC01
    Then Verify error message and error code when send request "GET" API with wrong account ID to "contact-lists" - "contacts" service

  @AC1 @P1 @TC2
  Scenario: API-Verify error message response when user send request GET list data-source with wrong account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC02
    Then Verify error message and error code when send request "GET" API with wrong account ID to "data-sources" - "contacts" service

  @AC1 @P1 @TC3
  Scenario: API-Verify error message response when user send request POST to create contact list with wrong account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC03
    Then Verify error message and error code when send request "POST" API with wrong account ID to "contact-lists" - "contacts" service

  @AC1 @P1 @TC4
  Scenario: API-Verify error message response when user send request POST to create data source with wrong account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC04
    Then Verify error message and error code when send request "POST" API with wrong account ID to "data-sources" - "contacts" service

  @AC1 @P1 @TC5
  Scenario: API-Verify error message response  when user send request POST to run data source with wrong account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC05
    Then Verify error message and error code when send request "RUN" API with wrong account ID to "data-sources" - "contacts" service

  @AC1 @P1 @TC6
  Scenario: API-Verify API execute with default account when user send request GET list contact list without account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC01
    Then Verify response API when send request "GET" API without account ID to "contact-lists" - "contacts" service

  @AC1 @P1 @TC7
  Scenario: API-Verify API execute with default account  when user send request GET list data source without account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC02
    Then Verify response API when send request "GET" API without account ID to "data-sources" - "contacts" service

  @AC1 @P1 @TC8
  Scenario: API-Verify API execute with default account when user send request POST to create contact list without account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC03
    Then Verify response API when send request "POST" API without account ID to "contact-lists" - "contacts" service

  @AC1 @P1 @TC9
  Scenario: API-Verify API execute with default account  when user send request POST to create data source without account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC04
    Then Verify response API when send request "POST" API without account ID to "data-sources" - "contacts" service

  @AC1 @P1 @TC10
  Scenario: API-Verify API execute with default account  when user send request POST to run data source without account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantContact-TC06
    And contactlist csv file available on SFTP server
    And The SFTP data source is created
    When Verify response API when send request "RUN" API without account ID to "data-sources" - "contacts" service
    Then clean the files on SFTP server
    Then clean the contact list