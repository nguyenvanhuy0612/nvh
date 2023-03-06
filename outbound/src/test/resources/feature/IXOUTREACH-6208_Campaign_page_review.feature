@IXOUTREACH-6208
Feature: Story IXOUTREACH-6208 Campaign page - review

  @Campaign @AC6 @P1 @TC1 @ANIinformation
  Scenario: Verify the save button disable and error hint message when input special character to Sender's display name while create Campaign
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = Campaigns, Data = ANI-Information-01
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    Then verify save button is disable and error message when input special character to "Sender's Display Name"

  @Campaign @AC6 @P1 @TC2 @ANIinformation
  Scenario: Verify the maximum length of Sender's display name is 11 character while create Campaign
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = Campaigns, Data = ANI-Information-02
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And Input "NameMore11Character" to "Sender's Display Name" field
    Then verify the max length of "Sender's Display Name" is 11

  @Campaign @AC6 @P1 @TC3 @ANIinformation
  Scenario: Verify the maximum length of Sender's display name is 11 character while update Campaign
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = Campaigns, Data = ANI-Information-03
    And Clean up all data for testing
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And Input "SenderName" to "Sender's Display Name" field
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign page
    And Input "NameMore11Character" to "Sender's Display Name" field
    Then verify the max length of "Sender's Display Name" is 11

  @Campaign @AC6 @P1 @TC4 @ANIinformation
  Scenario: Verify value of Sender's Display Name keep in campaign Editor page after created campaign successful
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = Campaigns, Data = ANI-Information-04
    And Clean up all data for testing
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And Input value to ANI Configuration
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign page
    Then Verify "Sender's Display Name" - ANI information keep in campaign editor after "created"

  @Campaign @AC6 @P1 @TC5 @ANIinformation
  Scenario: Verify value of Sender's Display Name keep in campaign Editor page after updated campaign successful
    Given The Campaign management URL is hit
    And Load testcase data: DataFile = Campaigns, Data = ANI-Information-05
    And Clean up all data for testing
    And Create contact list using API
    And Create campaign strategies using API to associate to campaign
    When Click Add New campaign button, fill name and description
    And Associate Contact lists to campaign
    And Associate Strategy to campaign
    And Input value to ANI Configuration
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign page
    And Input update value to ANI Configuration
    And User click "Save" button
    And User "clicks a campaign name link" for update campaign page
    Then Verify "Sender's Display Name" - ANI information keep in campaign editor after "updated"

  @Campaign @Point5 @P1 @TC15
  Scenario: Strategy-Verify the Name cannot start with numeric
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC15"
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    Then Verify error message displayed for mandatory "name" field
      | DataFile | CampaignStrategy             |
      | Data     | Naming_Mandatory_Field_Error |

  @Campaign @Point5 @P1 @TC16
  Scenario Outline: Strategy-Verify the Name cannot start with special characters
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "<Data>"
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    Then Verify error message displayed for mandatory "name" field
      | DataFile | CampaignStrategy             |
      | Data     | Naming_Mandatory_Field_Error |
    Examples:
      | Data                   |
      | Point5_TC16_hyphen     |
      | Point5_TC16_undersocre |

  @Campaign @Point5 @P1 @TC17 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Simplified Chinese
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC17"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC17      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC17      |

  @Campaign @Point5 @P1 @TC18 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Japanese
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC18"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC18      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC18      |

  @Campaign @Point5 @P1 @TC19 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Korean
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC19"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC19      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC19      |

  @Campaign @Point5 @P1 @TC20 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - English
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC20"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC20      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC20      |

  @Campaign @Point5 @P1 @TC21 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - French
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC21"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC21      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC21      |

  @Campaign @Point5 @P1 @TC22 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Germany
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC22"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC22      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC22      |

  @Campaign @Point5 @P1 @TC23 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Italian
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC23"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC23      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC23      |

  @Campaign @Point5 @P1 @TC24 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Russian
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC24"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC24      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC24      |

  @Campaign @Point5 @P1 @TC25 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Latin
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC25"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC25      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC25      |

  @Campaign @Point5 @P1 @TC26 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Spanish
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC26"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC26      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC26      |

  @Campaign @Point5 @P1 @TC27 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Brazilian-Portuguese
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC27"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC27      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC27      |

  @Campaign @Point5 @P1 @TC28 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - Hebrew
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC28"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC28      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC28      |

  @Campaign @Point5 @P1 @TC29 @IgnoreInParallel
  Scenario: Strategy-Verify the Name must start with alphabetic character - India-Hindi
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "Point5_TC29"
    And Clean up all data for testing
    And The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then Verify "strategy" after "created" is on top
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC29      |
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy |
      | Data     | Point5_TC29      |

  @Campaign @Point5 @P1 @TC30
  Scenario: Campaign-Verify the Name cannot start with numeric
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC30
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    Then Verify error message displayed for mandatory "name" field
      | DataFile | CampaignStrategy             |
      | Data     | Naming_Mandatory_Field_Error |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC31
  Scenario Outline: Campaign-Verify the Name cannot start with special characters
    * Load test data: DataFile = CampaignTestData, Data = <Data>
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    Then Verify error message displayed for mandatory "name" field
      | DataFile | CampaignStrategy             |
      | Data     | Naming_Mandatory_Field_Error |
    * Clean up campaign and contact list and strategy data added for testing
    Examples:
      | Data                   |
      | Point5_TC31_hyphen     |
      | Point5_TC31_undersocre |

  @Campaign @Point5 @P1 @TC32 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Simplified Chinese
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC32
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC32      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC33 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Japanese
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC33
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC33      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC34 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Korean
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC34
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC34      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC35 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - English
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC35
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC35      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC36 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - French
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC36
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC36      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC37 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Germany
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC37
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC37      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC38 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Italian
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC38
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC38      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC39 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Russian
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC39
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC39      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC40 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Latin
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC40
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC40      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC41 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Spanish
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC41
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC41      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC42 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Brazilian-Portuguese
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC42
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC42      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC43 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - Hebrew
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC43
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC43      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC44 @IgnoreInParallel
  Scenario: Campaign-Verify the Name must start with alphabetic character - India-Hindi
    * Load test data: DataFile = CampaignTestData, Data = Point5_TC44
    And Clean up all data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then User should able to see success message "Campaign added successfully."
    Then Verify "campaign" after "created" is on top
      | DataFile | CampaignTestData |
      | Data     | Point5_TC44      |
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @Point5 @P1 @TC60
  Scenario: DataSource-Verify the Name cannot start with numeric
    Given Admin hit the Data source URL
    When Admin user clicks "New Data Source" button
    And User send test data to field
      | Field Name | Data Source Name |
      | Input Text | 1111111          |
    Then Verify error message displayed for mandatory "name" field
      | DataFile | CampaignStrategy             |
      | Data     | Naming_Mandatory_Field_Error |

  @Campaign @Point5 @P1 @TC61
  Scenario: DataSource-Verify the Name cannot start with hyphen
    Given Admin hit the Data source URL
    When Admin user clicks "New Data Source" button
    And User send test data to field
      | Field Name | Data Source Name |
      | Input Text | -hyphen          |
    Then Verify error message displayed for mandatory "name" field
      | DataFile | CampaignStrategy             |
      | Data     | Naming_Mandatory_Field_Error |

  @Campaign @Point5 @P1 @TC62
  Scenario: DataSource-Verify the Name cannot start with under score
    Given Admin hit the Data source URL
    When Admin user clicks "New Data Source" button
    And User send test data to field
      | Field Name | Data Source Name |
      | Input Text | _UnderScore      |
    Then Verify error message displayed for mandatory "name" field
      | DataFile | CampaignStrategy             |
      | Data     | Naming_Mandatory_Field_Error |

  @Campaign @Point5 @P1 @TC63 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Simplified Chinese
    And Delete data source
      | DataFile | DataSourceTestData                   |
      | Data     | Verify_ds_Name_in_Simplified_Chinese |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData                   |
      | Data     | Verify_ds_Name_in_Simplified_Chinese |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData                   |
      | Data     | Verify_ds_Name_in_Simplified_Chinese |
    And Delete data source
      | DataFile | DataSourceTestData                   |
      | Data     | Verify_ds_Name_in_Simplified_Chinese |

  @Campaign @Point5 @P1 @TC64 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Japanese
    And Delete data source
      | DataFile | DataSourceTestData         |
      | Data     | Verify_ds_Name_in_Japanese |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData         |
      | Data     | Verify_ds_Name_in_Japanese |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData         |
      | Data     | Verify_ds_Name_in_Japanese |
    And Delete data source
      | DataFile | DataSourceTestData         |
      | Data     | Verify_ds_Name_in_Japanese |

  @Campaign @Point5 @P1 @TC65 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Korean
    And Delete data source
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_Korean |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_Korean |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_Korean |
    And Delete data source
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_Korean |

  @Campaign @Point5 @P1 @TC66 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - English
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_English |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_English |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_English |
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_English |

  @Campaign @Point5 @P1 @TC67 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - French
    And Delete data source
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_French |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_French |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_French |
    And Delete data source
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_French |

  @Campaign @Point5 @P1 @TC68 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Germany
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Germany |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Germany |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Germany |
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Germany |

  @Campaign @Point5 @P1 @TC69 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Italian
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Italian |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Italian |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Italian |
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Italian |

  @Campaign @Point5 @P1 @TC70 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Russian
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Russian |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Russian |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Russian |
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Russian |

  @Campaign @Point5 @P1 @TC71 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Latin
    And Delete data source
      | DataFile | DataSourceTestData      |
      | Data     | Verify_ds_Name_in_Latin |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData      |
      | Data     | Verify_ds_Name_in_Latin |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData      |
      | Data     | Verify_ds_Name_in_Latin |
    And Delete data source
      | DataFile | DataSourceTestData      |
      | Data     | Verify_ds_Name_in_Latin |

  @Campaign @Point5 @P1 @TC72 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Spanish
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Spanish |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Spanish |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Spanish |
    And Delete data source
      | DataFile | DataSourceTestData        |
      | Data     | Verify_ds_Name_in_Spanish |

  @Campaign @Point5 @P1 @TC73 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Brazilian_Portuguese
    And Delete data source
      | DataFile | DataSourceTestData                     |
      | Data     | Verify_ds_Name_in_Brazilian_Portuguese |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData                     |
      | Data     | Verify_ds_Name_in_Brazilian_Portuguese |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData                     |
      | Data     | Verify_ds_Name_in_Brazilian_Portuguese |
    And Delete data source
      | DataFile | DataSourceTestData                     |
      | Data     | Verify_ds_Name_in_Brazilian_Portuguese |

  @Campaign @Point5 @P1 @TC74 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - Hebrew
    And Delete data source
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_Hebrew |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_Hebrew |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_Hebrew |
    And Delete data source
      | DataFile | DataSourceTestData       |
      | Data     | Verify_ds_Name_in_Hebrew |

  @Campaign @Point5 @P1 @TC75 @IgnoreInParallel
  Scenario: DataSource-Verify the Name must start with alphabetic character - India_Hindi
    And Delete data source
      | DataFile | DataSourceTestData            |
      | Data     | Verify_ds_Name_in_India_Hindi |
    Given Admin hit the Data source URL
    When Admin user add new Data Source with details configuration
      | DataFile | DataSourceTestData            |
      | Data     | Verify_ds_Name_in_India_Hindi |
    Then User should be redirected to Data Source landing page
      | NotificationAlert | Data Source created successfully. |
    Then Verify "data source" after "creating" is on top
      | DataFile | DataSourceTestData            |
      | Data     | Verify_ds_Name_in_India_Hindi |
    And Delete data source
      | DataFile | DataSourceTestData            |
      | Data     | Verify_ds_Name_in_India_Hindi |

  @Campaign @Point5 @P1 @TC45
  Scenario: ContactList-Verify the Name cannot start with numeric
    * Load test data: DataFile = ContactList, Data = Point5_TC45
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    And Provides name and description in the contact list form
    Then Verify error message displayed for mandatory "name" field
      | DataFile | CampaignStrategy             |
      | Data     | Naming_Mandatory_Field_Error |

  @Campaign @Point5 @P1 @TC46
  Scenario Outline: ContactList-Verify the Name cannot start with special characters
    * Load test data: DataFile = ContactList, Data = <Data>
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    And Provides name and description in the contact list form
    Then Verify error message displayed for mandatory "name" field
      | DataFile | CampaignStrategy             |
      | Data     | Naming_Mandatory_Field_Error |
    And Clean up campaign and contact list and strategy data added for testing
    Examples:
      | Data                   |
      | Point5_TC46_hypen      |
      | Point5_TC46_Underscore |

  @Campaign @Point5 @P1 @TC47 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Simplified Chinese
    * Load test data: DataFile = ContactList, Data = Point5_TC47
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC47 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC48 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Japanese
    * Load test data: DataFile = ContactList, Data = Point5_TC48
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC48 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC49 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Korean
    * Load test data: DataFile = ContactList, Data = Point5_TC49
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC49 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC50 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - English
    * Load test data: DataFile = ContactList, Data = Point5_TC50
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC50 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC51 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - French
    * Load test data: DataFile = ContactList, Data = Point5_TC51
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC51 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC52 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Germany
    * Load test data: DataFile = ContactList, Data = Point5_TC52
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC52 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC53 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Italian
    * Load test data: DataFile = ContactList, Data = Point5_TC53
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC53 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC54 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Russian
    * Load test data: DataFile = ContactList, Data = Point5_TC54
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC54 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC55 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Latin
    * Load test data: DataFile = ContactList, Data = Point5_TC55
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC55 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC56 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Spanish
    * Load test data: DataFile = ContactList, Data = Point5_TC56
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC56 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC57 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Brazilian-Portuguese
    * Load test data: DataFile = ContactList, Data = Point5_TC57
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC57 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC58 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - Hebrew
    * Load test data: DataFile = ContactList, Data = Point5_TC58
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC58 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC59 @IgnoreInParallel
  Scenario: ContactList-Verify the Name must start with alphabetic character - India-Hindi
    * Load test data: DataFile = ContactList, Data = Point5_TC59
    And Clean up all data for testing
    Given The Contact list URL is hit to view the existing contact
    And Click to Create Contact List Button
    When He fills information in the Name and Description fields in the contact list form and click on Save button
    Then Contact list is created successfully
    And Click to Cancel Button
    And Admin should landed to contact list page
    Then Verify "ContactList" after "created" is on top
      | DataFile | ContactList |
      | Data     | Point5_TC59 |
    * Clean up contact list data for testing

  @Campaign @Point5 @P1 @TC76
  Scenario: ContactList-Atrribute-Verify the Name cannot start with numeric
    * Load test data: DataFile = ContactAttributeTestData, Data = Point5_TC76
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute with invalid value
    * Clean attribute testcase data

  @Campaign @Point5 @P1 @TC77
  Scenario Outline: ContactList-Atrribute-Verify the Name cannot start with special characters
    * Load test data: DataFile = ContactAttributeTestData, Data = <Data>
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute with invalid value
    * Clean attribute testcase data
    Examples:
      | Data                   |
      | Point5_TC77_hyphen     |
      | Point5_TC77_undersocre |
