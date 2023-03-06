@IXOUTREACH-3969 @IXOUTREACH-3971 @P1
@Campaign
Feature: IXOUTREACH-3971_Campaign_Summary_View

##############################################################################
##############################################################################
##############################################################################

  @TC1
  @Campaign @CampaignSummaryView
  Scenario: Verify Campaign summary work correctly with Name and Description fields no special character
    * Load test data: DataFile = Campaigns, Data = Campaign-summary
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Clicks the three dots beside campaign name
    And Click campaign summary view on dropdown
    Then "Campaign summary" page display after click menu on dropdown
    Then All value of field on the campaign summary sheet should be non editable
    And Value "Name" field should display correctly in campaign summary page
    And Value "Campaign Type" field should display correctly in campaign summary page
    And Value "Description" field should display correctly in campaign summary page
    And Value "Dialing Order" field should display correctly in campaign summary page
    And Value "Campaign Strategy" field should display correctly in campaign summary page
    And Value "Contact List" field should display correctly in campaign summary page
    And Campaign summary page should close when click close button
    And Campaign Manager landing page displayed
    And Clean up all data for testing

  @TC2
  @Campaign @CampaignSummaryView
  Scenario: Verify Campaign summary work correctly with Name, Description fields have special character and change dialing order
    * Load test data: DataFile = Campaigns, Data = Campaign-summary-special
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Clicks the three dots beside campaign name
    And Click campaign summary view on dropdown
    Then Value "Name" field should display correctly in campaign summary page
    And Value "Description" field should display correctly in campaign summary page
    And Value "Dialing Order" field should display correctly in campaign summary page
    And Clean up all data for testing

  @TC3
  @Campaign @CampaignSummaryView
  Scenario: Verify Campaign summary work correctly with Description fields is Empty and campaign type infinite
    * Load test data: DataFile = Campaigns, Data = Campaign-summary_empty
    * Create 1 campaign using API
    Given The Campaign management URL is hit to view the existing campaigns
    When Clicks the three dots beside campaign name
    And Click campaign summary view on dropdown
    Then Action menu option "Campaign Summary" will be disappeared after user select action
    And Value "Description" field should display correctly in campaign summary page
    And Value "Campaign Type" field should display correctly in campaign summary page
    And Clean up all data for testing
