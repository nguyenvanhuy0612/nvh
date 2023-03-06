@IXOUTREACH-3969 @IXOUTREACH-3973 @Campaign @UpdateCampaign @Sanity @P1
Feature: Story IXOUTREACH-3973 Update campaign - for Non running campaign
##############################################################################
##############################################################################
##############################################################################

  @AddData
  @TC0
  @Campaign @UpdateCampaign @P1
  Scenario: Add data for story by API
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate0
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC0" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    * Clean up campaign and contact list and strategy

  @TC1
  @Campaign @UpdateCampaign @P1
  Scenario: Using campaign name link verify campaign name and campaign type should be non-edit table
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate1
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC1" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When User "Clicks a campaign name link" for update campaign
    Then Name campaign and type can not edit
    * Clean up campaign and contact list and strategy


  @TC2
  @Campaign @UpdateCampaign @P1
  Scenario: Clicks the triple dots then selects edit menu verify campaign name and campaign type should be non-edit table
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate2
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC2" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When User "Clicks the triple dots then selects Edit menu" for update campaign
    Then Name campaign and type can not edit
    * Clean up campaign and contact list and strategy

  @TC3
  @Campaign @UpdateCampaign @P1
  Scenario: Using campaign name link verify that the changing values of fields are keeping after click on Cancel button and stay on campaign editor page
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate3a
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC3" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When  User "Clicks a campaign name link" for update campaign
    And The admin has made changes for one or more editable values for campaign "testCampaign_TC3"
    And User clicks "cancel"
    And User click "stay on this page" after cancel
    Then Edited variables are kept on the page
    Given Load testcase data: DataFile = Campaigns, Data = CampaignUpdate3b
    When  User "Clicks a campaign name link" for update campaign
    And The admin has made changes for one or more editable values for campaign "testCampaign_TC3"
    And User clicks "cancel"
    And User click "stay on this page" after cancel
    Then Edited variables are kept on the page
    * Clean up campaign and contact list and strategy

  @TC4
  @Campaign @UpdateCampaign @P1
  Scenario: Clicks the triple dots then selects edit menu verify that the changing values of fields are keeping after click on Cancel button and stay on campaign editor page
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate4a
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC4" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When  User "clicks the triple dots then selects edit menu" for update campaign
    And The admin has made changes for one or more editable values for campaign "testCampaign_TC4"
    And User clicks "cancel"
    And User click "stay on this page" after cancel
    Then Edited variables are kept on the page
    Given Load testcase data: DataFile = Campaigns, Data = CampaignUpdate4b
    When User "Clicks the triple dots then selects Edit menu" for update campaign
    And The admin has made changes for one or more editable values for campaign "testCampaign_TC4"
    And User clicks "cancel"
    And User click "stay on this page" after cancel
    Then Edited variables are kept on the page
    * Clean up campaign and contact list and strategy

  @TC5
  @Campaign @UpdateCampaign @P1
  Scenario:Clicks a campaign name link verify that the old values of fields are not changed after click on Cancel button and leave campaign editor page
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate5
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC5" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When  User "Clicks a campaign name link" for update campaign
    Then The admin has made changes for one or more editable values for campaign "testCampaign_TC5", user clicks "cancel",user click "leaver this page" after cancel. Then Variables are not changed for the campaign
    * Clean up campaign and contact list and strategy

  @TC6
  @Campaign @UpdateCampaign @P1
  Scenario:Clicks the triple dots then selects Edit menu verify that the old values of fields are not changed after click on Cancel button and leave campaign editor page
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate6
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC6" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When  User "Clicks the triple dots then selects Edit menu" for update campaign
    Then The admin has made changes for one or more editable values for campaign "testCampaign_TC6", user clicks "cancel",user click "leaver this page" after cancel. Then Variables are not changed for the campaign
    * Clean up campaign and contact list and strategy

  @TC7
  @Campaign @UpdateCampaign @P1
  Scenario: Using campaign name link verify that user is able edit description, Sender's Display Name, Sender's Address, Dialing Order, Time Based Finish after
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate7
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC7" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When User "clicks a campaign name link" for update campaign
    And Edit value for campaign "with value valid"
    And User clicks "save"
    Then A user friendly toast message indicating that the edited values are saved successfully is displayed
    And User "clicks a campaign name link" for update campaign
    And The new values are saved successfully for the selected campaign.
    * Clean up campaign and contact list and strategy

  @TC8
  @Campaign @UpdateCampaign @P1
  Scenario:  Using campaign name link verify that user is able edit description, Sender's Display Name, Sender's Address, Dialing Order, Time Based Finish at
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate8
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC8" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When User "clicks a campaign name link" for update campaign
    And Edit value for campaign "with value valid"
    And User clicks "save"
    Then A user friendly toast message indicating that the edited values are saved successfully is displayed
    And User "clicks a campaign name link" for update campaign
    And The new values are saved successfully for the selected campaign.
    * Clean up campaign and contact list and strategy

  @TC9
  @Campaign @UpdateCampaign @P1
  Scenario: Clicks the triple dots then selects edit menu verify that user is able edit description, Sender's Display Name, Sender's Address, Dialing Order, time based finish after
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate9
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC9" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When User "clicks the triple dots then selects edit menu" for update campaign
    And Edit value for campaign "with value valid"
    And User clicks "Save"
    Then A user friendly toast message indicating that the edited values are saved successfully is displayed
    And User "clicks the triple dots then selects edit menu" for update campaign
    And The new values are saved successfully for the selected campaign.
    * Clean up campaign and contact list and strategy

  @TC10
  @Campaign @UpdateCampaign @P1
  Scenario: Clicks the triple dots then selects edit menu verify that user is able edit description, Sender's Display Name, Sender's Address, Dialing Order, time based finish at
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate10
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC10" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When User "clicks the triple dots then selects Edit menu" for update campaign
    And Edit value for campaign "with value valid"
    And User clicks "save"
    Then A user friendly toast message indicating that the edited values are saved successfully is displayed
    And User "Clicks the triple dots then selects Edit menu" for update campaign
    And The new values are saved successfully for the selected campaign.
    * Clean up campaign and contact list and strategy

  @TC11
  @Campaign @UpdateCampaign @P1 @IXOUTREACH-6208
  Scenario: clicks a campaign name link verify that user can not edit description, Sender's Display Name, Sender's Address more than 255 characters
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate11
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC11" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When  User "clicks a campaign name link" for update campaign
    And Edit value for campaign "description, sender's display name, sender's address more than 255 characters"
    Then The new variable "description" has a maximum length of 128 characters
    And The new variable "sender's display name" has a maximum length of 11 characters
    And The new variable "sender's address" has a maximum length of 255 characters
    * Clean up campaign and contact list and strategy

  @TC12
  @Campaign @UpdateCampaign @P1 @IXOUTREACH-6208
  Scenario: Clicks the triple dots then selects edit menu verify that user can not edit description, Sender's Display Name, Sender's Address more than 255 characters
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate12
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC12" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When  User "clicks the triple dots then selects edit menu" for update campaign
    And Edit value for campaign "description, sender's display name, sender's address more than 255 characters"
    Then The new variable "description" has a maximum length of 128 characters
    And The new variable "sender's display name" has a maximum length of 11 characters
    And The new variable "sender's address" has a maximum length of 255 characters
    * Clean up campaign and contact list and strategy


  @TC13
  @Campaign @UpdateCampaign @P1
  Scenario: Using campaign name link verify that User is edit description, Sender's Display Name, Sender's Address, Dialing Order, Time Based Finish Criteria after save with error unknown reason
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate13
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC13" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When  User "Clicks a campaign name link" for update campaign
    Then An error toast message for re-trial after sometime should be displayed with reason for failure as Unknow error occurred
    * Clean up campaign and contact list and strategy

  @TC14
  @Campaign @UpdateCampaign @P1
  Scenario: Clicks the triple dots then selects Edit menu_Verify that User is edit description, Sender's Display Name, Sender's Address, Dialing Order, Time Based Finish Criteria after save with error unknown reason
    * Load testcase data: DataFile = Campaigns, Data = CampaignUpdate14
    * Clean up campaign and contact list and strategy before run
    Given Create contact list with name "CampaignUpdateCLS_TC14" and description "descriptionCLS" by API
    And Create strategy by API new Json
    When The Campaign management URL is hit
    Then Add data for story
    When  User "clicks the triple dots then selects Edit menu" for update campaign
    Then An error toast message for re-trial after sometime should be displayed with reason for failure as Unknow error occurred
    * Clean up campaign and contact list and strategy
