@IXOUTREACH-3969 @IXOUTREACH-3970 @Sanity @P1
Feature: Story IXOUTREACH-3970 Create campaign - Single Tenant
##############################################################################
##############################################################################
##############################################################################

  @Campaign
  Scenario: Verify campaign URL is hit with empty record on landing page
    Given The Campaign management URL is hit
    Given No record is displayed on landing page
    Then Verify Add Campaign button is displayed on landing page

  @Campaign
  Scenario: Verify campaign URL is hit with several record on landing page
    * Load test data: DataFile = CampaignTestData, Data = TestCampaign01
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    Given Several records are displayed on landing page
    Then Verify Add Campaign button is displayed on landing page
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign
  Scenario: Verify create new campaign
    * Load test data: DataFile = CampaignTestData, Data = TestCampaign03
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    Then Verify "TestCampaign03" campaign is added to landing page
    And User should be redirected to campaign landing page
    And Verify new campaign information are correct on landing page
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @IXOUTREACH-4801
  Scenario: Verify create new campaign with same name
    * Load test data: DataFile = CampaignTestData, Data = create_camp1
    * Clean up campaign and contact list and strategy data for testing
    * Create Contact List and Strategy for campaign
    Given The Campaign management URL is hit
    And Click Add New Campaign button, fill the name "camp1" with strategy "strategy1" and contact list "ctlist1"
    And User click "Save" button
    And Click Add New Campaign button, fill the name "camp1" with strategy "strategy1" and contact list "ctlist1"
    And User click "Save" button
    Then Verify campaign name showing hint error message "Campaign with given name already exists."
    Then Verify user should remain on campaign creation page
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign
  Scenario: Verify active tab "Details" include session "Campaign Information Name" and "Description" field
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Details"
    Then Verify active "Details" tab include field "Campaign Information"
    Then Verify active "Details" tab include field "Name"
    Then Verify active "Details" tab include field "Description"
    And Verify active "Details" tab include description "Enter a unique name for the Campaign here with maximum length of 40 characters. Allowed characters are alphanumeric,_(underscore),-(hyphen)"

  @Campaign
  Scenario: Verify warning message when user do not input field Name
    * Load test data: DataFile = CampaignTestData, Data = TestCampaign02
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    When User click "Save" button
    Then Verify failure "This is a required field." is displayed
    And Verify user should remain on campaign creation page
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign @P3 @IXOUTREACH-6208
  Scenario: Verify default value is empty and max length campaign name is 40
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Details"
    Then Verify field "Name" is "empty"
    When User send "81 characters" to field "Name"
      | Field Name       | Name                                                                              |
      | No of Characters | 81                                                                                |
      | Name             | TestCampaignNameWith81characters12345678901234567TestCampaignNameWith81characters |
    Then Verify maximum length of field "Name" is "40"

  @Campaign @P3 @IXOUTREACH-6208
  Scenario: Verify default value is empty and max length campaign description is 128 characters
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Details"
    Then Verify field "Description" is "empty"
    When User send "256 characters" to field "Description"
      | Field Name       | Name                                                                                                                                                                                                                                                             |
      | No of Characters | 256                                                                                                                                                                                                                                                              |
      | Name             | TestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDesc |
    Then Verify maximum length of field "Description" is "128"

  @Campaign
  Scenario: Verify active tab Campaign include session Campaign Type Configuration and ANI Configuration
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Campaign"
    Then Verify active "Campaign" tab include field "Campaign Type Configuration"
    Then Verify active "Campaign" tab include field "ANI Configuration"

  @Campaign
  Scenario: Verify the default type option is Finite
    * Load test data: DataFile = CampaignTestData, Data = TestCampaign04
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    Then Verify default value of "Campaign Type" is "Finite"
    When All the valid and mandatory values are provided
    And User click "Save" button
    And Verify "TestCampaign04" campaign is added to landing page
    Then User should be redirected to campaign landing page
    And Verify new campaign information are correct on landing page
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign
  Scenario: Verify there are 2 options to select are Finite and Infinite
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Campaign"
    Then Verify support option are Finite and Infinite

  @Campaign
  Scenario: Verify session heading ANI Configuration include Sender's Display Name and Sender's Display Name
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Campaign"
    Then Verify active "Campaign" tab include field "Sender's Display Name"
    Then Verify active "Campaign" tab include field "Sender's Address"
    And Verify active "Campaign" tab include description "Provide Sender's Display Name which will be used for this campaign"
    And Verify active "Campaign" tab include description "Provide Sender's Address which will be used for this campaign"

  @Campaign @P3 @IXOUTREACH-6208
  Scenario: Verify default value is empty and max length Sender's Display Name is 11 characters
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Campaign"
    Then Verify field "Sender's Display Name" is "empty"
    When User send "256 characters" to field "Sender's Display Name"
      | Field Name       | Sender's Display Name                                                                                                                                                                                                                                            |
      | No of Characters | 256                                                                                                                                                                                                                                                              |
      | Name             | TestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDesc |
    Then Verify maximum length of field "Sender's Display Name" is "11"

  @Campaign
  Scenario: Verify default value is empty and max length Sender's Address is 255 characters
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Campaign"
    Then Verify field "Sender's Address" is "empty"
    When User send "256 characters" to field "Sender's Address"
      | Field Name       | Sender's Address                                                                                                                                                                                                                                                 |
      | No of Characters | 256                                                                                                                                                                                                                                                              |
      | Name             | TestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDescriptionWith256charactersTestCampaignDesc |
    Then Verify maximum length of field "Sender's Address" is "255"

  @Campaign
  Scenario: Verify active tab Contacts include session Dialing Order Configuration
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Contacts"
    Then Verify active "Contacts" tab include field "Dialing Order Configuration"

  @Campaign
  Scenario: Verify default value of Dialing Order is Priority then Retry then Regular
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Contacts"
    Then Verify default value is Priority then Retry then Regular

  @Campaign
  Scenario: Verify active tab Completion Processing include session Time Based Finish Criteria
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Completion Processing"
    Then Verify active "Completion Processing" tab include field "Time Based Finish Criteria"
    And Verify active "Completion Processing" tab include field "Finish After (Hours : Minute)"
    And Verify active "Completion Processing" tab include field "Finish At (Date : Time)"
    And Verify active "Completion Processing" tab include field "Check Time Based Finish Criteria For Paused Campaign"

  @CampaignCompletionProcessing
  Scenario: Verify invalid value of Finish After Hour_Minute
    * Load test data: DataFile = CampaignTestData, Data = TestCampaign05
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Completion Processing"
    Then Verify field "Finish After (Hours)" is "0"
    Then Verify field "Finish After (Minutes)" is "0"
    When User send invalid data to field
    When User click "Save" button
    Then Verify failure "Entered value should not be greater than 1000." is displayed
    Then Verify failure "Entered value should not be greater than 59." is displayed
    And Verify user should remain on campaign creation page
    * Clean up campaign and contact list and strategy data added for testing

  @CampaignCompletionProcessing
  Scenario: Verify default value of Finish After Date_Time
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Completion Processing"
    When Admin user click on Finish At Date Time Button
    Then Verify field "Finish At (Date)" is "empty"
    Then Verify field "Finish At (Time)" is "empty"

  @Campaign
  Scenario: Verify option Check Time Based Finish Criteria For Paused Campaign
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Completion Processing"
    Then Verify active "Completion Processing" tab include field "Check Time Based Finish Criteria For Paused Campaign"
    Then Verify default value of "Check Time Based Finish Criteria For Paused Campaign" is "False"

  @Campaign
  Scenario: Verify session Time Based Finish Criteria is only visible on Finite Campaign
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    And User select "Campaign Type" option "Infinite"
    Then Verify session Time Based Finish Criteria is only visible on Finite Campaign

  @Campaign
  Scenario: Verify create new campaign with default value and all mandatory fields
    * Load test data: DataFile = CampaignTestData, Data = TestCampaign06
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    And Verify "TestCampaign06" campaign is added to landing page
    Then User should be redirected to campaign landing page
    And Verify new campaign information are correct on landing page
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign
  Scenario: Verify create new infinite campaign
    * Load test data: DataFile = CampaignTestData, Data = TestCampaign07
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    And Verify "TestCampaign07" campaign is added to landing page
    Then User should be redirected to campaign landing page
    And Verify new campaign information are correct on landing page
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign
  Scenario: Verify create new campaign long name
    * Load test data: DataFile = CampaignTestData, Data = TestCampaign08
    * Clean up campaign and contact list and strategy data for testing
    Given The Campaign management URL is hit
    And Create Contact List and Strategy for campaign
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When All the valid and mandatory values are provided
    And User click "Save" button
    And Verify "TestCampaignNameWith80characters12345678" campaign is added to landing page
    Then User should be redirected to campaign landing page
    And Verify new campaign information are correct on landing page
    * Clean up campaign and contact list and strategy data added for testing

  @Campaign
  Scenario: Verify require field for campaign creation detail page
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    Then Page navigate to Campaign detail configuration
    When Admin user click on tab "Details"
    Then Verify active "Details" tab include field "Name"
    Then Verify active "Details" tab include field "Description"
    When Admin user click on tab "Campaign"
    Then Verify active "Campaign" tab include field "Campaign Type"
    Then Verify active "Campaign" tab include field "Sender's Display Name"
    Then Verify active "Campaign" tab include field "Sender's Address"
    When Admin user click on tab "Contacts"
    Then Verify active "Contacts" tab include field "Dialing Order"
    When Admin user click on tab "Completion Processing"
    Then Verify active "Completion Processing" tab include field "Time Based Finish Criteria"

  @Campaign @SearchingStrategy @AC1 @TC1
  Scenario: Verify strategy dropdown search should accept up to 40 characters while creating new campaign
    Given The Campaign management URL is hit to view the existing campaigns
    And User click "New Campaign" button
    When Admin user click on tab "Campaign"
    And Quick search strategy
      | StrategyName | C123limitlength45678901234567891234567890123456789_1asdf |
    Then Verify searched strategy length and value
      | StrategyName       | C123limitlength45678901234567891234567890123456789_1asdf |
      | SearchStringLength | 40                                                       |

  @Campaign @SearchingContactList @AC2 @TC2
  Scenario: Verify contact list dropdown search should accept up to 40 characters while creating new campaign
    Given The Campaign management URL is hit to view the existing campaigns
    And User click "New Campaign" button
    When Admin user click on tab "Contacts"
    And Quick search contact list
      | ContactListName | C1234567890123gfdessdf4567891234567890123456789_1asdf |
    Then Verify searched contact list length and value
      | ContactListName    | C1234567890123gfdessdf4567891234567890123456789_1asdf |
      | SearchStringLength | 40                                                    |

  @Campaign @IXOUTREACH-4801
  Scenario: Verify finishTime attribute in campaign with locale support
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    When Page navigate to Campaign detail configuration
    And Admin user click on tab "Completion Processing"
    Then Verify the Finish At (Date : Time) changes when the time zone changes accordingly

  @Campaign @IXOUTREACH-6031 @Searching @TC1
  Scenario: Campaign Creation - Verify all special characters will be allowed when searching Strategy
    Given The Campaign management URL is hit to view the existing campaigns
    When User click "New Campaign" button
    And Admin user click on tab "Campaign"
    And Quick search strategy
      | StrategyName | ~`!@#$%^&*()-_+={}[]\|\/:;"'<>,.?abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ |
    Then Verify searched strategy length and value
      | StrategyName | ~`!@#$%^&*()-_+={}[]\|\/:;"'<>,.?abcdefgh |
    And Verify an error toast message will "not be" shown
    And Verify "Strategy" combo-box size will be shown as "0"
    And verify error message displayed for mandatory fields
      | DataFile | Campaigns                          |
      | Data     | Select_Strategy_Is_Mandatory_Field |

  @Campaign @IXOUTREACH-6031 @Searching @TC2
  Scenario: Campaign Creation - Verify all special characters will be allowed when searching Contact List
    Given The Campaign management URL is hit to view the existing campaigns
    When User click "New Campaign" button
    And Admin user click on tab "Contacts"
    And Quick search contact list
      | ContactListName | ~`!@#$%^&*()-_+={}[]\|\/:;"'<>,.?abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ |
    Then Verify searched contact list length and value
      | ContactListName | ~`!@#$%^&*()-_+={}[]\|\/:;"'<>,.?abcdefgh |
    And Verify an error toast message will "not be" shown
    And Verify "Contact List" combo-box size will be shown as "0"
    And verify error message displayed for mandatory fields
      | DataFile | Campaigns                             |
      | Data     | Select_ContactList_Is_Mandatory_Field |

  @IXOUTREACH-6459
  @Campaign
  Scenario: User is not able to input special characters using the Num Lock external keyboard for Time Based Finish Criteria field (numeric field)
    Given The Campaign management URL is hit
    And User click "New Campaign" button
    When Enter the invalid character using the Num Lock external keyboard for Time Based Finish Criteria field

  @Campaign @IXOUTREACH-6542
  Scenario: Do not allow duplicate campaign name - handle create case - insensitive
    * Load test data: DataFile = CampaignTestData, Data = Handle_Create_Case_Insensitive
    * Create Contact List and Strategy for campaign
    Given The Campaign management URL is hit
    When Click Add New Campaign button, fill the name "insensitive-campaign-name" with strategy "insensitive-strategy-name" and contact list "insensitive-contact-list-name"
    And User click "Save" button
    And Click Add New Campaign button, fill the name "INSENSITIVE-CAMPAIGN-NAME" with strategy "insensitive-strategy-name" and contact list "insensitive-contact-list-name"
    And User click "Save" button
    Then Verify campaign name showing hint error message "Campaign with given name already exists."
    And Verify user should remain on campaign creation page
    * Clean up campaign and contact list and strategy data added for testing
