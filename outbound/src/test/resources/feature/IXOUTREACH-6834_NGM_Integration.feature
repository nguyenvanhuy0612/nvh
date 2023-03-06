@IXOUTREACH-7239 @IXOUTREACH-6834
Feature: IXOUTREACH-6834_NGM_Integration.feature
##############################################################################
##############################################################################
##############################################################################



  @NGM @P1
  Scenario: login to workspaces
    Given Login to workspaces
    * The Contact list URL is hit to view the existing contact

  @NGM @P2
  Scenario: Verify action open and close multiple UI tabs on workspace
    Given User is in NGM workspace
    When User close all current sub tabs and UI show empty workspace
    Then User expands "Contacts" menu then open "Contact List" submenu on NGM
    Then Verify help icon not visible on NGM
    Then User expands "Contacts" menu then open "Data Source" submenu on NGM
    Then Verify help icon not visible on NGM
    Then User expands "Campaigns" menu then open "Campaign Manager" submenu on NGM
    Then Verify help icon not visible on NGM
    Then User expands "Campaigns" menu then open "Completion Code" submenu on NGM
    Then Verify help icon not visible on NGM
    Then User expands "Campaigns" menu then open "Campaign Strategy" submenu on NGM
    Then User close all current sub tabs and UI show empty workspace

  @NGM @P2
  Scenario: Verify help icon is visible on Obaas
    When User navigate to "contacts" page
    Then Verify help icon visible on OBaaS
    When User navigate to "datasource" page
    Then Verify help icon visible on OBaaS
    When User navigate to "campaigns-manager" page
    Then Verify help icon visible on OBaaS
    When User navigate to "completion-codes" page
    Then Verify help icon visible on OBaaS
    When User navigate to "campaign-strategy" page
    Then Verify help icon visible on OBaaS