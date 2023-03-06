@IXOUTREACH-4826 @IXOUTREACH-5457
Feature: Story IXOUTREACH-5457 Configure SMS pacing in strategy
##############################################################################
##############################################################################
##############################################################################
  @ConfigSMSPacing @TC01 @P1 @Campaign @AC1
  Scenario: Verify Campaign strategy has option Pace field while creating Strategy
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    Then Verify "Pace" field is displayed while "Creating" strategy

  @ConfigSMSPacing @TC03 @P1 @Campaign @AC1
  Scenario: Verify Campaign strategy has option TimeUnit field while creating Strategy
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    Then Verify "TimeUnit" field is displayed while "Creating" strategy

  @ConfigSMSPacing @TC04 @P1 @Campaign @AC1
  Scenario: Verify TimeUnit has option Second while creating Strategy
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    Then Verify "Second" value is displayed in TimeUnit while "creating" strategy

  @ConfigSMSPacing @TC05 @P1 @Campaign @AC1
  Scenario: Verify TimeUnit has option Minute while creating Strategy
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    Then Verify "Minute" value is displayed in TimeUnit while "creating" strategy

  @ConfigSMSPacing @TC06 @P1 @Campaign @AC1
  Scenario: Verify TimeUnit has option Hour while creating Strategy
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    Then Verify "Hour" value is displayed in TimeUnit while "creating" strategy

  @ConfigSMSPacing @TC10 @P1 @Campaign @AC1
  Scenario: Verify The default value of TimeUnit is Second while creating Strategy
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    Then Verify The default value of TimeUnit is "Second"

  @ConfigSMSPacing @TC27 @P1 @Campaign @AC1
  Scenario Outline: Verify that User can input valid value for Pace filed while Creating strategy
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "<Data>"
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field with valid Pace value
    Then Verify error does not show when input valid values of Pace field while "creating" strategy
    Examples:
      | Data                       |
      | ConfigSMSPacing_AC1_TC11_1 |
      | ConfigSMSPacing_AC1_TC11_2 |
      | ConfigSMSPacing_AC1_TC11_3 |

  @ConfigSMSPacing @TC02 @P1 @Campaign @AC1
  Scenario: Verify Non-numeric characters shouldn't be allowed in Pace filed  while creating Strategy
    Given Load testcase data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC1_TC02
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field with valid Pace value
    Then Verify Non Numeric characters are not allowed in Pace filed while "creating" strategy

  @ConfigSMSPacing @TC07 @P1 @Campaign @AC1
  Scenario: Verify The maximum number of SMS will be 40 per second while creating Strategy
    Given Load testcase data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC1_TC07
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field with valid Pace value
    Then Verify error "Entered value should not be greater than 40." when input invalid values of Pace field while "creating" strategy

  @ConfigSMSPacing @TC08 @P1 @Campaign @AC1
  Scenario: Verify The maximum number of SMS will be 2400 per Minute while creating Strategy
    Given Load testcase data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC1_TC08
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field with valid Pace value
    Then Verify error "Entered value should not be greater than 2400." when input invalid values of Pace field while "creating" strategy

  @ConfigSMSPacing @TC09 @P1 @Campaign @AC1
  Scenario: Verify The maximum number of SMS will be 144000 per Hour while creating Strategy
    Given Load testcase data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC1_TC09
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field with valid Pace value
    Then Verify error "Entered value should not be greater than 144000." when input invalid values of Pace field while "creating" strategy

  @ConfigSMSPacing @TC28 @P1 @Campaign @AC1
  Scenario: Verify The minimum number of SMS will be 1 per Second while creating Strategy
    Given Load testcase data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC1_TC12
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field with valid Pace value
    Then Verify error "Entered value should not be less than 1." when input invalid values of Pace field while "creating" strategy

  @ConfigSMSPacing @TC29 @P1 @Campaign @AC1
  Scenario: Verify The minimum number of SMS will be 1 per Minute while creating Strategy
    Given Load testcase data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC1_TC13
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field with valid Pace value
    Then Verify error "Entered value should not be less than 1." when input invalid values of Pace field while "creating" strategy

  @ConfigSMSPacing @TC30 @P1 @Campaign @AC1
  Scenario: Verify The minimum number of SMS will be 1 per Hour while creating Strategy
    Given Load testcase data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC1_TC14
    Given The Campaign Strategy URL is hit
    When clicks on new campaign strategy
    And Input value Strategy field with valid Pace value
    Then Verify error "Entered value should not be less than 1." when input invalid values of Pace field while "creating" strategy

  @ConfigSMSPacing @TC11 @P1 @Campaign @AC2
  Scenario: Verify User can modify Pace field while modifying the strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC11
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Verify "Pace" field is displayed while "Modify" strategy
    And Verify "Pace" field can modify
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC12 @P1 @Campaign @AC2
  Scenario: Verify User can modify Time unit field while modifying the strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC12
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Verify "Timeunit" field is displayed while "Modify" strategy
    And Verify "Timeunit" field can modify
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC13 @P1 @Campaign @AC2
  Scenario: Verify User can modify TimeUnit to Second while modifying the strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC13
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    And Update timeunit to "Second"
    And Save Strategy
    Then Verify notification is displayed
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC14 @P1 @Campaign @AC2
  Scenario: Verify User can modify TimeUnit to Hour while modifying the strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC14
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Update timeunit to "Hour"
    And Save Strategy
    Then Verify notification is displayed
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC15 @P1 @Campaign @AC2
  Scenario: Verify User can modify TimeUnit to Minute while modifying the strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC15
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Update timeunit to "Minute"
    And Save Strategy
    Then Verify notification is displayed
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC16 @P1 @Campaign @AC2
  Scenario: Verify The pace maximum of SMS will be 40 per second while modifying Strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC16
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then verify the pace sms maximum is 40 per "Second"
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC17 @P1 @Campaign @AC2
  Scenario: Verify The pace maximum of SMS will be 2400 per minute while modifying Strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC17
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then verify the pace sms maximum is 2400 per "Minute"
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC18 @P1 @Campaign @AC2
  Scenario: Verify The pace maximum of SMS will be 144000 per hour while modifying Strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC18
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then verify the pace sms maximum is 144000 per "Hour"
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC19 @P1 @Campaign @AC2
  Scenario: Verify Non-numeric characters shouldn't be allowed in Pace filed  while modififying Strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC19
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    And Verify Non Numeric characters are not allowed in Pace filed while "updating" strategy
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC20 @P1 @Campaign @AC2
  Scenario: Verify Pace field and TimeUnit field keep in strategy editor after saved successful
    Given The Campaign Strategy URL is hit
    And Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC20
    And Create strategy by "UI"
    When Open strategy editor using "Strategy name link"
    Then Verify pace field and timeunit field keep in strategy editor
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC32 @P1 @Campaign @AC2
  Scenario: Verify Pace field and TimeUnit field keep in strategy editor after modify then saved successful
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC32
    And Create strategy by API
    And The Campaign Strategy URL is hit
    And Open strategy editor using "Strategy name link"
    And update pace field and timeunit
    And Save Strategy
    And Open strategy editor using "Strategy name link"
    Then Verify pace field and timeunit field keep in strategy editor
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC23 @P1 @Campaign @AC3
  Scenario: Verify User cannot save if the Pace field is null while modifying strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC23
    And Create strategy by API
    And The Campaign Strategy URL is hit
    And Open strategy editor using "Strategy name link"
    When update pace field and timeunit
    Then Verify error "Pace should not be null or blank." when input invalid values of Pace field while "updating" strategy
    And Delete campaign strategies was created - 1

  @ConfigSMSPacing @TC24 @P1 @Campaign @AC3
  Scenario Outline: Verify User can save strategy while modifying strategy with valid fields
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_TC24
    And Create strategy by API
    And The Campaign Strategy URL is hit
    And Open strategy editor using "Strategy name link"
    When  Update timeunit to "<Timeunit>"
    Then Save Strategy
    Then Verify notification is displayed
    And Delete campaign strategies was created - 1
    Examples:
      | Timeunit |
      | SECOND   |
      | MINUTE   |
      | HOUR     |

  @ConfigSMSPacing @Campaign @AC3 @TC21 @P1
  Scenario: Verify User cannot save if the Pace field is null while Creating strategy
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC3_TC01
    And The Campaign Strategy URL is hit
    When Create Strategy
    Then Verify error "Pace should not be null or blank." when input invalid values of Pace field while "creating" strategy

  @ConfigSMSPacing @Campaign @AC3 @TC22 @P1
  Scenario: Verify User can save strategy while Creating strategy with valid Pace and SECOND
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC3_TC02
    And The Campaign Strategy URL is hit
    When Create Strategy
    And Verify User can save strategy with valid value of Pace field while "creating" strategy
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy         |
      | Data     | ConfigSMSPacing_AC3_TC02 |

  @ConfigSMSPacing @Campaign @AC3 @TC25 @P1
  Scenario: Verify User can save strategy while Creating strategy with valid Pace and MINUTE
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_AC3_TC05
    And The Campaign Strategy URL is hit
    When Create Strategy
    And Verify User can save strategy with valid value of Pace field while "creating" strategy
    And Clean up Strategies after tested
      | DataFile | CampaignStrategy         |
      | Data     | ConfigSMSPacing_AC3_TC05 |

  @ConfigSMSPacing @Campaign @AC3 @TC31 @P1
  Scenario Outline:: Verify User can save strategy while Creating strategy with valid Pace and HOUR
    Given Load test data: DataFile = CampaignStrategy, Data = <testData>
    And The Campaign Strategy URL is hit
    When Create Strategy
    And Verify User can save strategy with valid value of Pace field while "creating" strategy
    And Delete campaign strategies was created - 1
    Examples:
      | testData                   |
      | ConfigSMSPacing_AC3_TC07_1 |
      | ConfigSMSPacing_AC3_TC07_2 |
      | ConfigSMSPacing_AC3_TC07_3 |

  # TCs for cover isssue: IXOUTREACH-7409
  # Strategy - The maximum value of Pace is set back to 40 (SECOND) when editing strategy
  @ConfigSMSPacing @IXOUTREACH-7409
  Scenario: Verify do not any error message when edit strategy with valid value Pace field - Second unit
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_PaceValue_Second
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Verify do not any error message when edit Pace field with sequentially each value "20|40|1" to Pace field
    And Delete campaign strategy was created

  @ConfigSMSPacing @IXOUTREACH-7409
  Scenario: Verify do not any error message when edit strategy with valid value Pace field - Minute unit
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_PaceValue_Minute
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Verify do not any error message when edit Pace field with sequentially each value "1200|2400|1" to Pace field
    And Delete campaign strategy was created

  @ConfigSMSPacing @IXOUTREACH-7409
  Scenario: Verify do not any error message when edit strategy with valid value Pace field - Hour unit
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_PaceValue_Hour
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Verify do not any error message when edit Pace field with sequentially each value "72000|144000|1" to Pace field
    And Delete campaign strategy was created

  @ConfigSMSPacing @IXOUTREACH-7409
  Scenario: Verify do not any error message when edit strategy using arrow key to change value Pace field - Second unit
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_PaceValue_Second_key
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Verify Pace value and no error message with using arrow key to change Pace field at Min and Max value - "Second" unit
    And Delete campaign strategy was created

  @ConfigSMSPacing @IXOUTREACH-7409
  Scenario: Verify do not any error message when edit strategy using arrow key to change value Pace field  - Minute unit
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_PaceValue_Minute_key
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Verify Pace value and no error message with using arrow key to change Pace field at Min and Max value - "Minute" unit
    And Delete campaign strategy was created

  @ConfigSMSPacing @IXOUTREACH-7409
  Scenario: Verify do not any error message when edit strategy using arrow key to change value Pace field - Hour unit
    Given Load test data: DataFile = CampaignStrategy, Data = ConfigSMSPacing_PaceValue_Hour_key
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Verify Pace value and no error message with using arrow key to change Pace field at Min and Max value - "Hour" unit
    And Delete campaign strategy was created