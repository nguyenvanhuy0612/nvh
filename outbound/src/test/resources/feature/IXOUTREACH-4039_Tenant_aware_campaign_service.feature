@IXOUTREACH-4039
Feature: Story IXOUTREACH-4039 Tenant aware Contact Service

  @AC1 @P1 @TC1
  Scenario: API-Verify error message response when user send request GET list campaign with wrong account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC01
    Then Verify error message and error code when send request "GET" API with wrong account ID to "campaigns" - "campaigns" service

  @AC1 @P1 @TC2
  Scenario: API-Verify error message response when user send request GET list campaign strategy with wrong account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC02
    Then Verify error message and error code when send request "GET" API with wrong account ID to "strategies" - "campaigns" service

  @AC1 @P1 @TC3
  Scenario: API-Verify error message response when user send request GET list completion code with wrong account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC03
    Then Verify error message and error code when send request "GET" API with wrong account ID to "completion-codes" - "campaigns" service

  @AC1 @P1 @TC4
  Scenario: API-Verify error message response when user send request POST create campaign with wrong tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC04
    Then Verify error message and error code when send request "POST" API with wrong account ID to "campaigns" - "campaigns" service

  @AC1 @P1 @TC5
  Scenario: API-Verify error message response when user send request POST start campaign with wrong tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC05
    Then Verify error message and error code when send request "RUN" API with wrong account ID to "campaigns" - "campaigns" service

  @AC1 @P1 @TC6
  Scenario: API-Verify error message response when user send request POST stop campaign with wrong tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC06
    Then Verify error message and error code when send request "STOP" API with wrong account ID to "campaigns" - "campaigns" service

  @AC1 @P1 @TC7
  Scenario: API-Verify error message response when user send request POST to create campaign strategy with wrong tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC07
    Then Verify error message and error code when send request "POST" API with wrong account ID to "strategies" - "campaigns" service

  @AC1 @P1 @TC8
  Scenario: API-Verify error message response when user send request PUT to edit campaign strategy with wrong tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC08
    Then Verify error message and error code when send request "PUT" API with wrong account ID to "strategies" - "campaigns" service

  @AC1 @P1 @TC9
  Scenario: API-Verify error message response when user send request PUT to edit campaign with wrong tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC09
    Then Verify error message and error code when send request "PUT" API with wrong account ID to "campaigns" - "campaigns" service


  @AC1 @P1 @TC10
  Scenario: API-Verify API execute with default tenant when user send request GET list campaign without tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC01
    Then Verify response API when send request "GET" API without account ID to "campaigns" - "campaigns" service


  @AC1 @P1 @TC11
  Scenario: API-Verify API execute with default tenant when user send request GET list campaign strategy without tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC02
    Then Verify response API when send request "GET" API without account ID to "strategies" - "campaigns" service

  @AC1 @P1 @TC12
  Scenario: API-Verify API execute with default tenant when user send request GET list completion code without tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC03
    Then Verify response API when send request "GET" API without account ID to "completion-codes" - "campaigns" service

  @AC1 @P1 @TC13
  Scenario: API-Verify API execute with default tenant when user send request POST create campaign without tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC10
    And Delete Strategies after tested
    And Create strategy by API
    And Delete contact list was created
    And Create contact list using API
    Then Verify response API when send request "POST" API without account ID to "campaigns" - "campaigns" service

  @AC1 @P1 @TC14
  Scenario: API-Verify API execute with default tenant when user send request POST to create campaign strategy without tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC07
    Then Verify response API when send request "POST" API without account ID to "strategies" - "campaigns" service

  @AC1 @P1 @TC15
  Scenario: API-Verify API execute with default tenant when user send request POST start campaign without account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC12
    And Delete Strategies after tested
    And Create strategy by API
    And Delete contact list was created
    And Create contact list using API
    Then Verify response API when send request "START" API without account ID to "campaigns" - "campaigns" service

  @AC1 @P1 @TC16
  Scenario: API-Verify API execute with default tenant when user send request POST stop campaign without account ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC12
    And Delete Strategies after tested
    And Create strategy by API
    And Delete contact list was created
    And Create contact list using API
    Then Verify response API when send request "STOP" API without account ID to "campaigns" - "campaigns" service

  @AC1 @P1 @TC17
  Scenario: API-Verify API execute with default tenant  when user send request PUT to edit campaign without tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC12
    And Delete Strategies after tested
    And Create strategy by API
    And Delete contact list was created
    And Create contact list using API
    Then Verify response API when send request "PUT" API without account ID to "campaigns" - "campaigns" service

  @AC1 @P1 @TC18
  Scenario: API-Verify API execute with default tenant  when user send request PUT to edit strategy without tennant ID
    Given Load testcase data: DataFile = TenantAware, Data = TenantCampaign-TC11
    Then Verify response API when send request "PUT" API without account ID to "strategies" - "campaigns" service


