@IXOUTREACH-4031 @IXOUTREACH-5478
Feature: Support secure API communication
##############################################################################
##############################################################################
##############################################################################
  @SecureAPI @AC1 @TC01 @P1 @IgnoreInDailyRun
  Scenario: Verify connection to OutBound services must run through https protocol
    Given Verify user can "connect to outbound services" through https protocol
      | PageName         | Method | Protocol | Status |
      | strategies       | GET    | https    | 200    |
      | campaigns        | GET    | https    | 200    |
      | completion-codes | GET    | https    | 200    |
      | contact-lists    | GET    | https    | 200    |
      | data-sources     | GET    | https    | 200    |

  @SecureAPI @AC1 @TC02 @P1 @IgnoreInDailyRun
  Scenario: Verify user can add new campaign strategy through https protocol
    Given Load testcase data: DataFile = SecureAPICommunication, Data = VerifySecureCommunication
    Then Verify user can "add new campaign strategy" through https protocol
      | PageName   | Method | Protocol | Status |
      | strategies | POST   | https    | 200    |

  @SecureAPI @AC1 @TC03 @P1 @IgnoreInDailyRun
  Scenario: Verify user can add new contact list through https protocol
    Given Load testcase data: DataFile = SecureAPICommunication, Data = VerifySecureCommunication
    Then Verify user can "add new contact list" through https protocol
      | PageName      | Method | Protocol | Status |
      | contact-lists | POST   | https    | 201    |

  @SecureAPI @AC1 @TC04 @P1 @IgnoreInDailyRun
  Scenario: Verify user can add new campaign through https protocol
    Given Load testcase data: DataFile = SecureAPICommunication, Data = VerifySecureCommunication
    Then Verify user can "add new campaign" through https protocol
      | PageName  | Method | Protocol | Status |
      | campaigns | POST   | https    | 200    |

  @SecureAPI @AC1 @TC05 @P1 @IgnoreInDailyRun
  Scenario: Verify user can add and import new data source through https protocol
    Given Load testcase data: DataFile = SecureAPICommunication, Data = VerifySecureCommunication
    Then Verify user can "add new data source" through https protocol
      | PageName     | Method | Protocol | Status |
      | data-sources | POST   | https    | 200    |

  @SecureAPI @AC1 @TC06 @P1 @IgnoreInDailyRun
  Scenario: Verify user can update campaign strategy through https protocol
    Given Load testcase data: DataFile = SecureAPICommunication, Data = VerifySecureCommunication_Update
    Then Verify user can "update campaign strategy" through https protocol
      | PageName   | Method | Protocol | Status |
      | strategies | PUT    | https    | 200    |

  @SecureAPI @AC1 @TC07 @P1 @IgnoreInDailyRun
  Scenario: Verify user can update and start campaign through https protocol
    Given Load testcase data: DataFile = SecureAPICommunication, Data = VerifySecureCommunication_Update
    Then Verify user can "update campaign" through https protocol
      | PageName  | Method | Protocol | Status |
      | campaigns | PUT    | https    | 200    |

  @SecureAPI @AC1 @TC08 @P1 @IgnoreInDailyRun
  Scenario: Verify Swagger links for both Campaigns and Contacts API are running in Secure mode
    Then Verify user can "connect to Swagger links" through https protocol
      | PageName          | Method | Protocol | Status |
      | Swagger-campaigns | GET    | https    | 200    |
      | Swagger-contacts  | GET    | https    | 200    |

  @SecureAPI @AC1 @TC09 @P1 @IgnoreInDailyRun
  Scenario: Verify user can connect to health URLs in Secure mode
    Then Verify user can "connect to health URLs" through https protocol
      | PageName         | Method | Protocol | Status |
      | health-campaigns | GET    | https    | 200    |
      | health-contacts  | GET    | https    | 200    |

  @SecureAPI @AC2 @TC12 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify user can establish the connection to the Campaign page over HTTPS protocol in backend
    Given The Campaign management URL is hit
    When Refresh page 1 times
    Then Verify all urls start with https after navigating to the Campaign page

  @SecureAPI @AC2 @TC13 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify user can establish the connection to the Campaign Strategy page over HTTPS protocol in backend
    Given The Campaign Strategy URL is hit
    When Refresh page 1 times
    Then Verify all urls start with https after navigating to the Campaign Strategy page

  @SecureAPI @AC2 @TC14 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify user can establish the connection to the Contact List page over HTTPS protocol in backend
    Given The Contact list URL is hit to view the existing contact
    When Refresh page 1 times
    Then Verify all urls start with https after navigating to Contact List page

  @SecureAPI @AC2 @TC15 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify user can establish the connection to the Completion Code page over HTTPS protocol in backend
    Given The Completion code URL is hit to view the existing complete code
    When Refresh page 1 times
    Then Verify all urls start with https after navigating to the Completion Code page

  @SecureAPI @AC2 @TC16 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify user can establish the connection to the Data Source page over HTTPS protocol in backend
    Given Admin hit the Data source URL
    When Refresh page 1 times
    Then Verify all urls start with https after navigating to Data Source page

  @SecureAPI @AC2 @TC17 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while creating Campaign in backend
    * Load test data: DataFile = SecureAPICommunication, Data = SecureAPIAC2TC23
    Given The Campaign management URL is hit
    When Refresh page 1 times
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should be redirected to campaign landing page
    And Verify "SecureAPIAC2TC23" campaign is added to landing page
    And Verify new campaign information are correct on landing page
    Then Verify all urls start with https after creating Campaign
    * Clean up campaign and contact list and strategy data added for testing

  @SecureAPI @AC2 @TC18 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while creating Campaign Strategy in backend
    Given Load test data: DataFile = SecureAPICommunication, Data = SecureAPIAC2TC24
    And The Campaign Strategy URL is hit
    When Refresh page 1 times
    When Create Strategy
    And Verify User can save strategy with valid value of Pace field while "creating" strategy
    Then Verify all urls start with https after creating Campaign Strategy
    And Clean up Strategies after tested
      | DataFile | SecureAPICommunication |
      | Data     | SecureAPIAC2TC24       |

  @SecureAPI @AC2 @TC19 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while creating Contact List in backend
    Given The Contact list URL is hit to view the existing contact
    When Refresh page 1 times
    When Create Contact List with Name "SecureAPIAC2TC25CL25" and Description "SecureAPIAC2TC25CL25"
    Then Verify all urls start with https after creating Contact List
    * Delete all contact list with Name contains "SecureAPIAC2TC25CL25"

  @SecureAPI @AC2 @TC20 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while creating Data Source in backend
    Given Admin hit the Data source URL
    When Refresh page 1 times
    When Admin user add new Data Source with details configuration
      | DataFile | SecureAPICommunication |
      | Data     | SecureAPIAC2TC26       |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify new Data Source information are correct on landing page
    Then Verify all urls start with https after creating Data Source
    * User clean datasource after completing TC

  @SecureAPI @AC2 @TC21 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while updating Campaign in backend
    * Load testcase data: DataFile = SecureAPICommunication, Data = SecureAPIAC2TC27
    Given The Campaign management URL is hit
    When Refresh page 1 times
    Given Create contact list with name "SecureAPIAC2TC27CLS" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When User "clicks a campaign name link" for update campaign
    And Edit value for campaign "with value valid"
    And User clicks "save"
    And User "clicks a campaign name link" for update campaign
    And The new values are saved successfully for the selected campaign.
    Then Verify all urls start with https after updating Campaign
    * Clean up campaign and contact list and strategy

  @SecureAPI @AC2 @TC22 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while updating Campaign Strategy in backend
    Given Load testcase data: DataFile = "SecureAPICommunication", Data = "SecureAPIAC2TC28"
    Given The Campaign Strategy URL is hit
    And Create strategy by "API"
    When Refresh page 1 times
    When Edit smsText field
      | DataFile | SecureAPICommunication          |
      | Data     | UpdateStrategy_SecureAPIAC2TC28 |
    Then Verify all urls start with https after updating Campaign
    And Clean up Strategies after tested
      | DataFile | SecureAPICommunication |
      | Data     | SecureAPIAC2TC28       |

  @SecureAPI @AC2 @TC23 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while listing all Campaigns in backend
    * Load test data: DataFile = SecureAPICommunication, Data = SecureAPIAC2TC30
    Given The Campaign management URL is hit
    When Refresh page 1 times
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should be redirected to campaign landing page
    And Verify "SecureAPIAC2TC30" campaign is added to landing page
    And Verify new campaign information are correct on landing page
    When User clicks the refresh button
    Then Verify all urls start with https after listing all Campaigns
    * Clean up campaign and contact list and strategy data added for testing

  @SecureAPI @AC2 @TC24 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while listing all Campaign Strategies in backend
    Given Load test data: DataFile = SecureAPICommunication, Data = SecureAPIAC2TC31
    And The Campaign Strategy URL is hit
    When Refresh page 1 times
    When Create Strategy
    And Verify User can save strategy with valid value of Pace field while "creating" strategy
    And Clicking refresh button
    Then Verify all urls start with https after listing all Campaign Strategies
    And Clean up Strategies after tested
      | DataFile | SecureAPICommunication |
      | Data     | SecureAPIAC2TC31       |

  @SecureAPI @AC2 @TC25 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while listing all Contact Lists in backend
    Given The Contact list URL is hit to view the existing contact
    When Refresh page 1 times
    When Create Contact List with Name "SecureAPIAC2TC32CL32" and Description "SecureAPIAC2TC32CL32"
    And Click to "Cancel" Button
    And Click to refresh button
    Then Verify all urls start with https after listing all Contact Lists
    * Delete all contact list with Name contains "SecureAPIAC2TC32CL32"

  @SecureAPI @AC2 @TC26 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while listing all Completion Codes in backend
    Given The Completion code URL is hit to view the existing complete code
    When Refresh page 2 times
    Then Verify all urls start with https after listing all Completion Codes

  @SecureAPI @AC2 @TC27 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while listing all Data Sources in backend
    Given Admin hit the Data source URL
    When Refresh page 2 times
    Then Verify all urls start with https after navigating to listing all Data Sources

  @SecureAPI @AC3 @TC35 @P1 @IgnoreInDailyRun
  Scenario: API-Verify all connections cannot run over HTTP protocol while creating strategies
    Given Load testcase data: DataFile = SecureAPICommunication, Data = DisableHTTP
    When Verify cannot send API create "strategies" via HTTP protocol

  @SecureAPI @AC3 @TC36 @P1 @IgnoreInDailyRun
  Scenario: API-Verify all connections cannot run over HTTP protocol while creating Contact List
    Given Load testcase data: DataFile = SecureAPICommunication, Data = DisableHTTP
    When Verify cannot send API create "contact-lists" via HTTP protocol

  @SecureAPI @AC3 @TC37 @P1 @IgnoreInDailyRun
  Scenario: API-Verify all connections cannot run over HTTP protocol while creating campaigns
    Given Load testcase data: DataFile = SecureAPICommunication, Data = DisableHTTP
    When Verify cannot send API create "campaigns" via HTTP protocol

  @SecureAPI @AC3 @TC38 @P1 @IgnoreInDailyRun
  Scenario: API-Verify all connections cannot run over HTTP protocol while creating data-sources
    Given Load testcase data: DataFile = SecureAPICommunication, Data = DisableHTTP
    When Verify cannot send API create "data-sources" via HTTP protocol

  @SecureAPI @AC3 @TC39 @P1 @IgnoreInDailyRun
  Scenario Outline: API-Verify all connections cannot run over HTTP protocol while Get PAGE <pageName>
    When Verify cannot send API to get list "<pageName>" via HTTP protocol
    Examples:
      | pageName        |
      | campaigns       |
      | strategies      |
      | contact-lists   |
      | data-sources    |
      | completion-codes|
      | swagger links for campaigns    |
      | swagger links for contact-list |
      | health for campaigns           |
      | health for contact-list        |

  @SecureAPI @AC3 @TC40 @P1 @IgnoreInDailyRun
  Scenario: API-Verify all connections cannot run over HTTP protocol while update strategies
    Given Load testcase data: DataFile = SecureAPICommunication, Data = DisableHTTP
    When Create "strategies" by API via HTTPS protocol
    Then Verify cannot send API update "strategies" via HTTP protocol

  @SecureAPI @AC3 @TC41 @P1 @IgnoreInDailyRun
  Scenario: API-Verify all connections cannot run over HTTP protocol while update campaigns
    Given Load testcase data: DataFile = SecureAPICommunication, Data = DisableHTTP
    When Create "campaigns" by API via HTTPS protocol
    Then Verify cannot send API update "campaigns" via HTTP protocol

  @SecureAPI @AC3 @TC42 @P1 @IgnoreInDailyRun
  Scenario: API-Verify all connections cannot run over HTTP protocol while update contact-lists
    Given Load testcase data: DataFile = SecureAPICommunication, Data = DisableHTTP
    When Create "contact-lists" by API via HTTPS protocol
    Then Verify cannot send API update "contact-lists" via HTTP protocol

  @SecureAPI @AC3 @TC43 @P1 @IgnoreInDailyRun
  Scenario: API-Verify all connections cannot run over HTTP protocol while update data-sources
    Given Load testcase data: DataFile = SecureAPICommunication, Data = DisableHTTP
    When Create "data-sources" by API via HTTPS protocol
    Then Verify cannot send API update "data-sources" via HTTP protocol

  @SecureAPI @AC3 @TC44 @P1 @IgnoreInDailyRun
  Scenario: API-Verify all connections cannot run over HTTP protocol while start campaigns
    Given Load testcase data: DataFile = SecureAPICommunication, Data = DisableHTTP
    When Create "campaigns" by API via HTTPS protocol
    Then Verify cannot send API execute "campaigns" via HTTP protocol

  @SecureAPI @AC2 @TC28 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while importing Data Sources in backend
    * Load test data: DataFile = SecureAPICommunication, Data = SecureAPIAC28ImportContactOption
    Given The Contact list is created
    And The SFTP data source is created
    And contactlist csv file available on SFTP server
    When Users select one data source to import
    Then Verify User should be redirected to "Contact List" landing page
    Then contact list page should show import in progress status
    And user search contact list
    Then once import is completed last import status should be updated
    Then Verify all urls start with https after importing Data Sources
    Then clean the files on SFTP server
    Then clean the contact list

  @SecureAPI @AC2 @TC29 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify user can establish the connection to the Campaign health page over HTTPS protocol
    Then The user navigate to "Campaign" health link

  @SecureAPI @AC2 @TC30 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify user can establish the connection to the contacts health page over HTTPS protocol
    Then The user navigate to "Contact" health link

  @SecureAPI @AC2 @TC31 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify user can establish the connection to the Campaign swagger page over HTTPS protocol
    Then The user navigate to "Campaign" swagger link

  @SecureAPI @AC2 @TC32 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify user can establish the connection to the contacts swagger page over HTTPS protocol
    Then The user navigate to "Contact" swagger link

  @SecureAPI @AC2 @TC85 @P1 @IgnoreInDailyRun
  Scenario: UI-Verify all connections must run over HTTPS protocol while starting campaign
    * Load test data: DataFile = SecureAPICommunication, Data = SecureAC2_TC2
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Start campaign with start campaign option
    Then Status campaign display "In Progress" in "Last Executed" column campaign landing page
    Then Verify all urls start with https after starting campaign
    And Clean up all data for testing

  @SecureAPI @AC4 @AC5 @TC46 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system supports TLSv1.2 and cipher TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
    Then Verify the system supports "TLSv1.2" and cipher "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC47 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.1 and cipher TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC48 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.1 and cipher TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
    Then Verify the system does not supports "TLSv1.3" and cipher "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC49 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system supports TLSv1.3 and cipher TLS_AES_256_GCM_SHA384
    Then Verify the system supports "TLSv1.3" and cipher "TLS_AES_256_GCM_SHA384" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC50 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.2 and cipher TLS_AES_256_GCM_SHA384
    Then Verify the system does not supports "TLSv1.2" and cipher "TLS_AES_256_GCM_SHA384" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC51 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.1 and cipher TLS_AES_256_GCM_SHA384
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_AES_256_GCM_SHA384" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC52 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system supports TLSv1.3 and cipher TLS_CHACHA20_POLY1305_SHA256
    Then Verify the system supports "TLSv1.3" and cipher "TLS_CHACHA20_POLY1305_SHA256" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC53 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.2 and cipher TLS_CHACHA20_POLY1305_SHA256
    Then Verify the system does not supports "TLSv1.2" and cipher "TLS_CHACHA20_POLY1305_SHA256" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC54 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.1 and cipher TLS_CHACHA20_POLY1305_SHA256
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_CHACHA20_POLY1305_SHA256" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC55 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system supports TLSv1.2 and cipher TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
    Then Verify the system supports "TLSv1.2" and cipher "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC56 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.3 and cipher TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
    Then Verify the system does not supports "TLSv1.3" and cipher "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC57 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.1 and cipher TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC58 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system supports TLSv1.3 and cipher TLS_AES_128_GCM_SHA256
    Then Verify the system supports "TLSv1.3" and cipher "TLS_AES_128_GCM_SHA256" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC59 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.1 and cipher TLS_AES_128_GCM_SHA256
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_AES_128_GCM_SHA256" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC60 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot supports TLSv1.2 and cipher TLS_AES_128_GCM_SHA256
    Then Verify the system does not supports "TLSv1.2" and cipher "TLS_AES_128_GCM_SHA256" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC61 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot support invalid TLS version
    Then Verify the system does not supports "TLSv.invalid" and cipher "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC62 @P1 @IgnoreInDailyRun
  Scenario: CampaignService-Verify the system doesnot support invalid cipher suite
    Then Verify the system does not supports "TLSv1.2" and cipher "TLS_ECDHE_RSA_WITH_invalid" with "CampaignService"

  @SecureAPI @AC4 @AC5 @TC63 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot support invalid TLS version
    Then Verify the system does not supports "TLSv.invalid" and cipher "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC64 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot support invalid cipher suite
    Then Verify the system does not supports "TLSv1.2" and cipher "TLS_ECDHE_RSA_WITH_invalid" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC65 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system supports TLSv1.2 and cipher TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
    Then Verify the system supports "TLSv1.2" and cipher "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC66 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.1 and cipher TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC67 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.3 and cipher TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
    Then Verify the system does not supports "TLSv1.3" and cipher "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC68 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system supports TLSv1.3 and cipher TLS_AES_256_GCM_SHA384
    Then Verify the system supports "TLSv1.3" and cipher "TLS_AES_256_GCM_SHA384" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC69 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.2 and cipher TLS_AES_256_GCM_SHA384
    Then Verify the system does not supports "TLSv1.2" and cipher "TLS_AES_256_GCM_SHA384" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC70 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.1 and cipher TLS_AES_256_GCM_SHA384
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_AES_256_GCM_SHA384" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC71 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system supports TLSv1.3 and cipher TLS_CHACHA20_POLY1305_SHA256
    Then Verify the system supports "TLSv1.3" and cipher "TLS_CHACHA20_POLY1305_SHA256" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC72 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.2 and cipher TLS_CHACHA20_POLY1305_SHA256
    Then Verify the system does not supports "TLSv1.2" and cipher "TLS_CHACHA20_POLY1305_SHA256" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC73 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.1 and cipher TLS_CHACHA20_POLY1305_SHA256
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_CHACHA20_POLY1305_SHA256" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC74 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system supports TLSv1.2 and cipher TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
    Then Verify the system supports "TLSv1.2" and cipher "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC75 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.3 and cipher TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
    Then Verify the system does not supports "TLSv1.3" and cipher "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC76 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.1 and cipher TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC77 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system supports TLSv1.3 and cipher TLS_AES_128_GCM_SHA256
    Then Verify the system supports "TLSv1.3" and cipher "TLS_AES_128_GCM_SHA256" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC78 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.2 and cipher TLS_AES_128_GCM_SHA256
    Then Verify the system does not supports "TLSv1.2" and cipher "TLS_AES_128_GCM_SHA256" with "ContactService"

  @SecureAPI @AC4 @AC5 @TC79 @P1 @IgnoreInDailyRun
  Scenario: ContactService-Verify the system doesnot supports TLSv1.1 and cipher TLS_AES_128_GCM_SHA256
    Then Verify the system does not supports "TLSv1.1" and cipher "TLS_AES_128_GCM_SHA256" with "ContactService"