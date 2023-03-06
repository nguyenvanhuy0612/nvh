@IXOUTREACH-4083
Feature: IXOUTREACH-4803 Create Strategy
  This feature for ID IXOUTREACH-4803 Create Strategy
##############################################################################
##############################################################################
##############################################################################
  @Campaign @AC1_TC01
  @CampaignStrategy
  Scenario: Verify  Campaign Strategy page has option to create new strategy
    Given The Campaign Strategy URL is hit
    Then The campaign strategy should have option to create new strategy

  @Campaign @AC1_TC02
  @CampaignStrategy
  Scenario: Verify  Campaign strategy page has option to create new strategy after created new some strategies
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC1_TC02
    Given The Campaign Strategy URL is hit
    And Create strategy by API
    And Reload Campaign strategy url multi times
    Then The campaign strategy should have option to create new strategy

  @Campaign @AC1_TC03
  @CampaignStrategy
  Scenario: Verify  Campaign strategy page has option to create new strategy after reload page multi times
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC1_TC03
    Given The Campaign Strategy URL is hit
    Then The campaign strategy should have option to create new strategy
    When Reload Campaign strategy url multi times
    Then The campaign strategy should have option to create new strategy

  @Campaign @AC2
  @CampaignStrategy
  Scenario:Verify Simple SMS Strategy should be shown when user clicks on new strategy
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    Then should have option to create simple SMS strategy

  @Campaign @AC2
  @CampaignStrategy
  Scenario:Verify that the maximum allowed length of the strategy name is 40
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-MaxLenght-Name
    And clicks on new campaign strategy
    When Input value Strategy field to check maximum lenght of Strategy field: Name
    Then Verify cannot enter more 40 character in Strategy "name"

  @Campaign @AC2
  @CampaignStrategy
  Scenario:Verify that the strategy name is mandatory field
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-Name-Blank
    And clicks on new campaign strategy
    When Save Strategy
    And Validation the error message required field "Name"

  @Campaign @AC2
  @CampaignStrategy
  Scenario: Verify that Strategy name can only contain alphanumeric - Hyphen and underscore
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-special-character
    And clicks on new campaign strategy
    When Entered special character to in the Strategy name
    Then Validation the error message special character field

  @Campaign @AC2
  @CampaignStrategy
  Scenario: Verify that Strategy Description is optional
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-description-empty
    And Create Strategy - with Strategy Description is blank
    When Verify Strategy save successful

  @Campaign @AC2
  @CampaignStrategy
  Scenario:Verify that the maximum allowed length of the strategy description is 128
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-MaxLenght-Des
    And clicks on new campaign strategy
    When Input value Strategy field to check maximum lenght of Strategy field: Description
    Then Verify cannot enter more 128 character in Strategy "Description"

  @Campaign @AC2
  @CampaignStrategy
  Scenario:Verify that SMS TEXT is mandatory field
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-Blank
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is blank
    When Save Strategy
    And Validation the error message required field "SMS Text"

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - Simplified Chinese
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-Simplified-Chinese
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is Simplified Chinese
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - Japanese
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-Japanese
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is Japanese
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - Korean
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-Korean
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is Korean
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - English
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-English
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is English
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - French
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-French
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is French
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - German
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-German
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is German
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - Italian
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-Italian
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is Italian
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - Russian
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-Russian
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is Russian
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - Latin-Spanish
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-Latin-Spanish
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is Latin-Spanish
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - Brazilian-Portuguese
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-Brazilian-Portuguese
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is Brazilian-Portuguese
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC2
  @CampaignStrategy
  @G-14
  Scenario:Verify strategy can be created when input SMS TEXT with G-14 languages - Hebrew
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Strategy-SMS-Hebrew
    And clicks on new campaign strategy
    And Input value Strategy field - SMS text is Hebrew
    When Save Strategy
    Then Verify Strategy save successful
    And  Delete campaign strategies was created - 1

  @Campaign @AC4-CancelForm
  @CampaignStrategy
  Scenario:Verify that cancel form display when user input some field of strategy then click cancel
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = dialog-Confirm-Cancel-Show
    And clicks on new campaign strategy
    And Input value Strategy field
    When Click Cancel Strategy
    Then Cancel confirm form is displayed - "yes"

  @Campaign @AC4-CancelForm
  @CampaignStrategy
  Scenario: Verify that Cancel Form not show when user dont input to any field of Strategy
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = dialog-Confirm-Cancel-not-Show
    And clicks on new campaign strategy
    And Input value Strategy field
    When Click Cancel Strategy
    Then Cancel confirm form is displayed - "no"

  @Campaign @AC4-CancelForm
  @CampaignStrategy
  Scenario: Verify that User stay on that create strategy page when click option Stay on this page on Cancel form
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = dialog-Confirm-Cancel-Option-No
    And clicks on new campaign strategy
    And Input value Strategy field
    And Click Cancel Strategy
    And Cancel confirm form is displayed - "yes"
    When Select Cancel confirm form option - "Stay on this page"
    Then Verify page handler after confirm cancel form - "Stay on Create strategy page"

  @Campaign @AC4-CancelForm
  @CampaignStrategy
  Scenario: Verify that User landed to strategy landing page when click option Leave this page on Cancel form
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = dialog-Confirm-Cancel-Option-Yes
    And clicks on new campaign strategy
    And Input value Strategy field
    And Click Cancel Strategy
    And Cancel confirm form is displayed - "yes"
    When Select Cancel confirm form option - "Leave this page"
    Then Verify page handler after confirm cancel form - "Strategy landing page"

  @Campaign @AC4-CancelForm
  @CampaignStrategy
  Scenario: Verify that strategy do not exist in List all strategy when user cancel create
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = dialog-Confirm-Cancel-Option-Yes
    And clicks on new campaign strategy
    And Input value Strategy field
    And Click Cancel Strategy
    And Cancel confirm form is displayed - "yes"
    When Select Cancel confirm form option - "Leave this page"
    Then Verify page handler after confirm cancel form - "Strategy landing page"
    And Verify Strategy do not exist

  @Campaign @AC4-CancelForm
  @CampaignStrategy
  Scenario: Verify message on cancel dialog of strategy page
    Given The Campaign Strategy URL is hit
    Given Load test data: DataFile = CampaignStrategy, Data = Verify-Message-dialog-confirm
    And clicks on new campaign strategy
    And Input value Strategy field
    And Click Cancel Strategy
    When Cancel confirm form is displayed - "yes"
    Then Verify message on cancel confirm form

  @Campaign @AC4_TC01
  @CampaignStrategy @IXOUTREACH-4801
  Scenario: Verify that Strategy could not create with duplicate name
    * Cleanup and create strategy name "strategy1", sms "test" by API
    Given The Campaign Strategy URL is hit
    When Click Add New Strategy button, fill the name "strategy1", Description "", SMS Text "test" and click save button
    When Click Add New Strategy button, fill the name "strategy1", Description "", SMS Text "test" and click save button
    Then Verify strategy name showing hint error message "Strategy with given name already exists."
    And UI stays on The Create Strategy page

  @Campaign @AC4_TC02 @CampaignStrategy
  Scenario Outline: Verify that Strategy could not create with special character in name field
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "<Data>"
    And The Campaign Strategy URL is hit
    When Create Strategy with special character name by UI
    Then UI displays correct the "hint" Error for "name" field
    Then UI stays on The Create Strategy page
    Examples:
      | Data                      |
      | AC4_TC02_1_!              |
      | AC4_TC02_2_@              |
      | AC4_TC02_3_#              |
      | AC4_TC02_4_$              |
      | AC4_TC02_5_%              |
      | AC4_TC02_6_^              |
      | AC4_TC02_7_&              |
      | AC4_TC02_8_*              |
      | AC4_TC02_9_(              |
      | AC4_TC02_10_)             |
      | AC4_TC02_11_double_quotes |
      | AC4_TC02_12_:             |
      | AC4_TC02_13_?             |
      | AC4_TC02_14_~             |
      | AC4_TC02_15_+             |
      | AC4_TC02_16_<             |
      | AC4_TC02_17_.             |
      | AC4_TC02_18_/             |
      | AC4_TC02_19_'             |
      | AC4_TC02_20_backslash     |
      | AC4_TC02_21_space         |
      | AC4_TC02_22_>             |
      | AC4_TC02_23_=             |
      | AC4_TC02_24_combine       |

  @Campaign @AC4_TC03
  @CampaignStrategy
  Scenario: Verify that Strategy could not create with null name field
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC4_TC03
    And The Campaign Strategy URL is hit
    When Create Strategy with null name by UI
    Then UI displays correct the "hint" Error for "name" field
    Then UI stays on The Create Strategy page

  @Campaign @AC4_TC04
  @CampaignStrategy
  Scenario: Verify that Strategy could not create with null Sms Text field
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC4_TC04
    And The Campaign Strategy URL is hit
    When Create Strategy with null SMS text field by UI
    Then UI displays correct the "hint" Error for "smstext" field
    Then UI stays on The Create Strategy page

  @Campaign @AC4_TC05
  @CampaignStrategy
  Scenario: Verify that User can edit Strategy Name after input invalid value
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC4_TC05
    And The Campaign Strategy URL is hit
    When Create Strategy with special character name by UI
    Then UI displays correct the "hint" Error for "name" field
    Then UI stays on The Create Strategy page
    When Re input valid strategy name and save
    Then Save strategy successfully with new valid name input

  @Campaign @AC4_TC06
  @CampaignStrategy
  Scenario: Verify that User can edit Sms Text field after input invalid value
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC4_TC06
    And The Campaign Strategy URL is hit
    When Create Strategy with special character name by UI
    Then UI displays correct the "hint" Error for "smstext" field
    Then UI stays on The Create Strategy page
    When Re input valid sms text and save
    Then Save strategy successfully with new valid name input

  @Campaign @AC3_TC01
  @CampaignStrategy @IgnoreInParallel
  Scenario: Verify Campaign strategy user should able to save strategy with valid input
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC3_TC01
    And The Campaign Strategy URL is hit
    When Click Add New Strategy button
    And Enter valid strategy details
      | DataFile | CampaignStrategy |
      | Data     | AC3_TC01         |
    And Save Strategy
    Then User should able to see success message "Strategy added successfully."
    Then User should able to see strategy on to of strategy management screen
      | DataFile | CampaignStrategy |
      | Data     | AC3_TC01         |


  @Campaign @AC3_TC02
  @CampaignStrategy
  Scenario: Verify Campaign strategy user should not able to save strategy with null values
    Given Load testcase data: DataFile = CampaignStrategy, Data = AC3_TC02
    And The Campaign Strategy URL is hit
    When Click Add New Strategy button
    And Enter valid strategy details
      | DataFile | CampaignStrategy |
      | Data     | AC3_TC02         |
    And Save Strategy
    Then User should not able to see success message "Strategy added successfully."

  @IXOUTREACH-6459 @Campaign
  @CampaignStrategy
  Scenario: User is not able to input special characters using the Num Lock external keyboard for Pace field (numeric field)
    Given The Campaign Strategy URL is hit
    When Click Add New Strategy button
    When Enter the invalid character using the Num Lock external keyboard for Pace field

  @IXOUTREACH-6542 @Campaign
  @CampaignStrategy
  Scenario: Do not allow duplicate strategy name - handle create case - insensitive
    Given Load testcase data: DataFile = CampaignTestData, Data = Handle_Create_Case_Insensitive
    And The Campaign Strategy URL is hit
    When Click Add New Strategy button, fill the name "insensitive-strategy-name", Description "", SMS Text "test" and click save button
    And Click Add New Strategy button, fill the name "INSENSITIVE-STRATEGY-NAME", Description "", SMS Text "test" and click save button
    Then Verify strategy name showing hint error message "Strategy with given name already exists."
    And UI stays on The Create Strategy page
    And Delete Strategies after tested
