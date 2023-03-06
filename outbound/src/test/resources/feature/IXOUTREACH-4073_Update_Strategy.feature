@IXOUTREACH-4073
Feature: IXOUTREACH-4073 Update Strategy
  This feature for ID IXOUTREACH-4073 Update Strategy
##############################################################################
##############################################################################
##############################################################################

  @AC1_TC01
  @Campaign @UpdateStrategy
  Scenario: Verify Campaign strategy page has option to edit strategy using take more action three dots
    Given Load testcase data: DataFile = CampaignStrategy, Data = UpdateStrategyAC5_TC01
    And The Campaign Strategy URL is hit
    And Click Add New Strategy button
    And Enter valid strategy details
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC01 |
    And Save Strategy
    And User click on Edit option of strategy
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC01 |
    Then User should able to see Edit strategy page for strategy
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC01 |
    And Enter valid strategy details
      | DataFile | CampaignStrategy         |
      | Data     | UpdateStrategyOnEditPage |
    And User clicks button "cancel"
    And User clicks Leave this page button
    And User click on Strategy name link
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC01 |
    Then User should able to see Edit strategy page for strategy
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC01 |


  @AC5_TC02
  @Campaign @UpdateStrategy @IgnoreInParallel
  Scenario: Verify user should able to see cancel confirmation pop up while canceling and navigating to other pages
    Given Load testcase data: DataFile = CampaignStrategy, Data = UpdateStrategyAC5_TC02
    And The Campaign Strategy URL is hit
    And Click Add New Strategy button
    And Enter valid strategy details
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC02 |
    And Save Strategy
    And User click on Edit option of strategy
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC02 |
    And Enter valid strategy details
      | DataFile | CampaignStrategy         |
      | Data     | UpdateStrategyOnEditPage |
    And User clicks button "cancel"
    Then User should able to see leave this page pop up
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC02 |
    When User clicks Leave this page button
    Then User should able to see strategy on to of strategy management screen
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC02 |
    And User click on Edit option of strategy
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC02 |
    And Enter valid strategy details
      | DataFile | CampaignStrategy         |
      | Data     | UpdateStrategyOnEditPage |
    When User clicks button "cancel"
    And User clicks button Stay on this page
    Then User should able to see Edit strategy page for strategy
      | DataFile | CampaignStrategy         |
      | Data     | UpdateStrategyOnEditPage |
    When User click on navigation link "Campaign Strategy"
    Then User should able to see leave this page pop up
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC02 |

  @Campaign @UpdateStrategy @TC16 @AC2
  Scenario: Verify that strategy editor should come up when user clicks Strategy name link
    Given Load test data: DataFile = CampaignStrategy, Data = updateStrategy_AC2_TC16-open-with-linkName
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Strategy editor should come up
    And Strategy editor show old data correctly

  @AC5_TC03
  @Campaign @UpdateStrategy
  Scenario: Verify user should not able to see cancel confirmation pop up if user is not edited any field
    Given Load testcase data: DataFile = CampaignStrategy, Data = UpdateStrategyAC5_TC03
    And The Campaign Strategy URL is hit
    And Click Add New Strategy button
    And Enter valid strategy details
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC03 |
    And Save Strategy
    And User click on Edit option of strategy
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC03 |
    And User clicks button "cancel"
    Then User should not able to see "Leave this page?" pop up
    Then User should able to see strategy on to of strategy management screen
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC5_TC03 |

  @Campaign @UpdateStrategy @TC17 @AC2
  Scenario: Verify that strategy editor should come up when user clicks on triple dot then select edit strategy
    Given Load test data: DataFile = CampaignStrategy, Data = updateStrategy_AC2_TC17-open-with-moreOption
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Open strategy editor using "clicks on triple dot then select edit strategy"
    Then Strategy editor should come up
    And Strategy editor show old data correctly

  @Campaign @UpdateStrategy @TC18 @AC2
  Scenario: Open stategy editor Using strategy name link verify SMS Text can modify
    Given Load test data: DataFile = CampaignStrategy, Data = updateStrategy_AC2_TC18_LinkName-SMS-modify
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Strategy editor should come up
    And Strategy editor show old data correctly
    And Verify "SMS Text" field can modify

  @Campaign @UpdateStrategy @TC19 @AC2
  Scenario: Open stategy editor Using Clicks the triple dots then selects edit strategy menu verify SMS Text can modify
    Given Load test data: DataFile = CampaignStrategy, Data = updateStrategy_AC2_TC19_moreOption-SMS-modify
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Open strategy editor using "clicks on triple dot then select edit strategy"
    Then Strategy editor should come up
    And Strategy editor show old data correctly
    And Verify "SMS Text" field can modify

  @Campaign @UpdateStrategy @TC20 @AC2
  Scenario: Using strategy name link verify Strategy Description can modify
    Given Load test data: DataFile = CampaignStrategy, Data = updateStrategy_AC2_TC20_LinkName-Des-modify
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Strategy editor should come up
    And Strategy editor show old data correctly
    And Verify "Description" field can modify

  @Campaign @UpdateStrategy @TC21 @AC2
  Scenario: Clicks the triple dots then selects edit strategy menu verify strategy description can modify
    Given Load test data: DataFile = CampaignStrategy, Data = updateStrategy_AC2_TC21_moreOption-Des-modify
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Open strategy editor using "clicks on triple dot then select edit strategy"
    Then Strategy editor should come up
    And Strategy editor show old data correctly
    And Verify "Description" field can modify

  @Campaign @UpdateStrategy @TC22 @AC2
  Scenario: Using strategy name link verify Strategy name should be non-edit
    Given Load test data: DataFile = CampaignStrategy, Data = updateStrategy_AC2_TC22_LinkName-name-modify
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Open strategy editor using "Strategy name link"
    Then Strategy editor should come up
    And Strategy editor show old data correctly
    And Verify "strategy name" field cannot modify

  @Campaign @UpdateStrategy @TC23 @AC2
  Scenario: Clicks the triple dots then selects edit strategy menu verify strategy name should be non-edit
    Given Load test data: DataFile = CampaignStrategy, Data = updateStrategy_AC2_TC23_moreOption-name-modify
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Open strategy editor using "clicks on triple dot then select edit strategy"
    Then Strategy editor should come up
    And Strategy editor show old data correctly
    And Verify "strategy name" field cannot modify

  @Campaign @UpdateStrategy @AC3 @G-14 @TC01
  Scenario: Verify strategy can be edited when input SMS TEXT with Simplified Chinese
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC01_Simplified_Chinese"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                                       |
      | Data     | UpdateStrategy_AC3_TC01_Simplified_Chinese_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                                       |
      | Data     | UpdateStrategy_AC3_TC01_Simplified_Chinese_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                                       |
      | Data     | UpdateStrategy_AC3_TC01_Simplified_Chinese_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC02
  Scenario: Verify strategy can be edited when input SMS TEXT with Japanese
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC02_Japanese"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                             |
      | Data     | UpdateStrategy_AC3_TC02_Japanese_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                             |
      | Data     | UpdateStrategy_AC3_TC02_Japanese_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                             |
      | Data     | UpdateStrategy_AC3_TC02_Japanese_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC03
  Scenario: Verify strategy can be edited when input SMS TEXT with Korean
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC03_Korean"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC03_Korean_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC03_Korean_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC03_Korean_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC04
  Scenario: Verify strategy can be edited when input SMS TEXT with English
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC04_English"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC04_English_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC04_English_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC04_English_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC05
  Scenario: Verify strategy can be edited when input SMS TEXT with French
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC05_French"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC05_French_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC05_French_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC05_French_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC06
  Scenario: Verify strategy can be edited when input SMS TEXT with German
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC06_German"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC06_German_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC06_German_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC06_German_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC07
  Scenario: Verify strategy can be edited when input SMS TEXT with Italian
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC07_Italian"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC07_Italian_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC07_Italian_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC07_Italian_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC08
  Scenario: Verify strategy can be edited when input SMS TEXT with Russian
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC08_Russian"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC08_Russian_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC08_Russian_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC08_Russian_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC09
  Scenario: Verify strategy can be edited when input SMS TEXT with Latin
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC09_Latin"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                          |
      | Data     | UpdateStrategy_AC3_TC09_Latin_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                          |
      | Data     | UpdateStrategy_AC3_TC09_Latin_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                          |
      | Data     | UpdateStrategy_AC3_TC09_Latin_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC10
  Scenario: Verify strategy can be edited when input SMS TEXT with Spanish
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC10_Spanish"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC10_Spanish_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC10_Spanish_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                            |
      | Data     | UpdateStrategy_AC3_TC10_Spanish_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC11
  Scenario: Verify strategy can be edited when input SMS TEXT with Brazilian-Portuguese
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC11_Brazilian-Portuguese"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                                         |
      | Data     | UpdateStrategy_AC3_TC11_Brazilian-Portuguese_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                                         |
      | Data     | UpdateStrategy_AC3_TC11_Brazilian-Portuguese_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                                         |
      | Data     | UpdateStrategy_AC3_TC11_Brazilian-Portuguese_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC12
  Scenario: Verify strategy can be edited when input SMS TEXT with Hebrew
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC12_Hebrew"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC12_Hebrew_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC12_Hebrew_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                           |
      | Data     | UpdateStrategy_AC3_TC12_Hebrew_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @G-14 @TC13
  Scenario: Verify strategy can be edited when input SMS TEXT with India-Hindi
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC13_India-Hindi"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                                |
      | Data     | UpdateStrategy_AC3_TC13_India-Hindi_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                                |
      | Data     | UpdateStrategy_AC3_TC13_India-Hindi_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                                |
      | Data     | UpdateStrategy_AC3_TC13_India-Hindi_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @TC14
  Scenario: Verify strategy can be edited with valid lenght (1 character) sms text field
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC014_1character"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                                |
      | Data     | UpdateStrategy_AC3_TC014_1character_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                                |
      | Data     | UpdateStrategy_AC3_TC014_1character_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                                |
      | Data     | UpdateStrategy_AC3_TC014_1character_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @TC15
  Scenario: Verify strategy can be edited with valid lenght (10 characters) sms text field
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC015_10characters"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                                  |
      | Data     | UpdateStrategy_AC3_TC015_10characters_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                                  |
      | Data     | UpdateStrategy_AC3_TC015_10characters_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                                  |
      | Data     | UpdateStrategy_AC3_TC015_10characters_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @TC16
  Scenario: Verify strategy can be edited with valid lenght (256 characters) sms text field
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC016_256characters"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                                   |
      | Data     | UpdateStrategy_AC3_TC016_256characters_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                                   |
      | Data     | UpdateStrategy_AC3_TC016_256characters_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                                   |
      | Data     | UpdateStrategy_AC3_TC016_256characters_EditSmsText |

  @Campaign @UpdateStrategy @AC3 @TC17
  Scenario: Verify strategy can be edited with valid lenght (0 character) description field
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC017_0character"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                                    |
      | Data     | UpdateStrategy_AC3_TC017_0character_EditDescription |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                                    |
      | Data     | UpdateStrategy_AC3_TC017_0character_EditDescription |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                                    |
      | Data     | UpdateStrategy_AC3_TC017_0character_EditDescription |

  @Campaign @UpdateStrategy @AC3 @TC18
  Scenario: Verify strategy can be edited with valid lenght (10 characters) description field
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC018_10characters"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                                      |
      | Data     | UpdateStrategy_AC3_TC018_10characters_EditDescription |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                                      |
      | Data     | UpdateStrategy_AC3_TC018_10characters_EditDescription |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                                      |
      | Data     | UpdateStrategy_AC3_TC018_10characters_EditDescription |

  @Campaign @UpdateStrategy @AC3 @TC19
  Scenario: Verify strategy can be edited with valid lenght (128 characters) description field
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC019_128characters"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                                       |
      | Data     | UpdateStrategy_AC3_TC019_128characters_EditDescription |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                                       |
      | Data     | UpdateStrategy_AC3_TC019_128characters_EditDescription |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                                       |
      | Data     | UpdateStrategy_AC3_TC019_128characters_EditDescription |

  @Campaign @UpdateStrategy @AC3 @TC20
  Scenario: Verify strategy can be edited with special characters for Sms text field
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC20"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                    |
      | Data     | UpdateStrategy_AC3_TC20_EditSmsText |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                    |
      | Data     | UpdateStrategy_AC3_TC20_EditSmsText |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                    |
      | Data     | UpdateStrategy_AC3_TC20_EditSmsText |


  @Campaign @UpdateStrategy @AC3 @TC21
  Scenario: Verify strategy can be edited with special characters for description field
    Given Load testcase data: DataFile = "CampaignStrategy", Data = "UpdateStrategy_AC3_TC021"
    And Create strategy by "API"
    And The Campaign Strategy URL is hit
    When Edit smsText field
      | DataFile | CampaignStrategy                         |
      | Data     | UpdateStrategy_AC3_TC021_EditDescription |
    Then Verify notification is displayed
    Then Verify Strategy is listed
      | DataFile | CampaignStrategy                         |
      | Data     | UpdateStrategy_AC3_TC021_EditDescription |
    Then Verify info is correct after edit
      | DataFile | CampaignStrategy                         |
      | Data     | UpdateStrategy_AC3_TC021_EditDescription |

  @Campaign @UpdateStrategy @AC4 @TC01
  Scenario: Verify that strategy could not edit with null SMS text field
    Given Load testcase data: DataFile = CampaignStrategy, Data = UpdateStrategyAC4_TC01
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When User click on Edit option of strategy
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC4_TC01 |
    And Enter invalid strategy details
      | DataFile | CampaignStrategy                 |
      | Data     | UpdateStrategyOnEditPageAC4_TC01 |
    Then Validation the error message required field "SMS Text"
    And Verify user still stay on "Edit Strategy" page

  @Campaign @UpdateStrategy @AC4 @TC02
  Scenario: Verify that user can re-edit SMS text field after edited with null
    Given Load testcase data: DataFile = CampaignStrategy, Data = UpdateStrategyAC4_TC02
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When User click on Edit option of strategy
      | DataFile | CampaignStrategy       |
      | Data     | UpdateStrategyAC4_TC02 |
    And Enter invalid strategy details
      | DataFile | CampaignStrategy                 |
      | Data     | UpdateStrategyOnEditPageAC4_TC02 |
    Then Validation the error message required field "SMS Text"
    And Verify user still stay on "Edit Strategy" page
    When Enter valid strategy details
      | DataFile | CampaignStrategy                   |
      | Data     | ReUpdateStrategyOnEditPageAC4_TC02 |
    And Save Strategy
    Then Verify notification is displayed

  @Campaign @UpdateStrategy @AC4 @TC03
  Scenario: Verify that Strategy could not edit with Description length more than 128 characters
    Given Load testcase data: DataFile = CampaignStrategy, Data = UpdateStrategyAC4_TC03
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Edit description field
      | DataFile | CampaignStrategy         |
      | Data     | ReUpdateStrategyAC4_TC03 |
    Then Verify notification is displayed
    And Verify maximum allowed length of the description field
      | DataFile | CampaignStrategy         |
      | Data     | ReUpdateStrategyAC4_TC03 |

  @Campaign @UpdateStrategy @AC4 @TC04
  Scenario: Verify that Strategy could not edit with sms text length more than 256 characters
    Given Load testcase data: DataFile = CampaignStrategy, Data = UpdateStrategyAC4_TC04
    And Create strategy by API
    And The Campaign Strategy URL is hit
    When Edit description field
      | DataFile | CampaignStrategy         |
      | Data     | ReUpdateStrategyAC4_TC04 |
    Then Verify notification is displayed
    And Verify maximum allowed length of the smsText field
      | DataFile | CampaignStrategy         |
      | Data     | ReUpdateStrategyAC4_TC04 |
