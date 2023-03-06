@IXOUTREACH-5755 @IXOUTREACH-5327 @Contact @ContactList @P1
Feature: Story IXOUTREACH-5327 Advance search option in contact list


  @Contact @AC1 @TC1
  Scenario: Verify filter button and field advance search display on contact list page when no contact list display on contact landing page
    Given The Contact list URL is hit to view the existing contact
    When There are no contact list display on contact list landing page
    Then Verify all option search are displayed
    And Name Column, Operator and Search field display on contact list landing page after click filter button


  @Contact @AC1 @TC2
  Scenario: Verify Name and Last Update only display on dropdown of Column Name when no contact list display on contact list landing page
    Given The Contact list URL is hit to view the existing contact
    When There are no contact list display on contact list landing page
    Then  Name and Last updated column only be used to advance search
      | Name | Last Updated |


  @Contact @AC1 @TC3
  Scenario: Verify filter button and field advance search display on contact list page when have contact list configured
    * Load test data: DataFile = ContactList, Data = Advance_Search_CTL1
    * Create a contact list by API
    Given The Contact list URL is hit to view the existing contact
    Then Option advance search displayed with 1 contact list configured on contact list landing page
    And Name Column, Operator and Search field display on contact list landing page after click filter button


  @Contact @AC1 @TC4
  Scenario: Verify Name and Last Update only display on dropdown of Column Name with at least a contact list configured
    * Load test data: DataFile = ContactList, Data = Advance_Search_CTL2
    * Create a contact list by API
    Given The Contact list URL is hit to view the existing contact
    Then Name and Last updated column only be used to advance search
      | Name | Last Updated |


  @Contact @AC1 @TC5-6
  Scenario Outline: Verify list operator display correctly for each advance search attribute
    Given The Contact list URL is hit to view the existing contact
    Then Verify list "<Operator>" on dropdown display correctly for attribute "<Column Name>"

    @Contact @AC1 @TC5-6
    Examples:
      | Column Name  | Operator              |
      | Name         | =,!=,In,Like,Not Like |
      | Last Updated | <,>,Between           |


  @Contact @AC2 @TC1-TC5
  Scenario Outline: Verify that user can search contact list by Name with operators
    * Load test data: DataFile = ContactList, Data = Advance_Search_ContactList1
    * Create multiple contact lists by API
    Given The Contact list URL is hit to view the existing contact
    When Advance search for field "<Column Name>" by "<Operator>" with "<Input type>" text "<Value>" on contact list page an verify advance search result
    * Clean up contact list data for testing

    @Contact @AC2 @TC1-TC5
    Examples:
      | Column Name | Operator | Value  | Input type |
      | Name        | Like     | list   | Input      |
      | Name        | In       | list   | Input      |
      | Name        | =        | LIST_C | Input      |
      | Name        | !=       | list   | Input      |
      | Name        | Not Like | list   | Input      |


  @Contact @AC2 @TC6-TC13
  Scenario Outline: Verify that user can search contact list by Last updated time with operators
    * Load test data: DataFile = ContactList, Data = Advance_Search_ContactList2
    * Create a list contact lists with interval time
    Given The Contact list URL is hit to view the existing contact
    Then Advance search for field "<Column Name>" by "<Operator>" with "<Input type>" text "<Value>" on contact list page an verify advance search result
    * Clean up contact list data for testing

    @Contact @AC2 @TC6-TC13
    Examples:
      | Column Name  | Input type    | Operator | Value                               |
      | Last Updated | Input         | >        | contactlist:ct002                   |
      | Last Updated | Select        | >        | contactlist:ct004                   |
      | Last Updated | Input         | <        | contactlist:ct005                   |
      | Last Updated | Select        | <        | contactlist:ct002                   |
      | Last Updated | Select,Select | Between  | contactlist:ct002,contactlist:ct004 |
      | Last Updated | Input,Input   | Between  | contactlist:ct001,contactlist:ct004 |
      | Last Updated | Select,Input  | Between  | contactlist:ct001,contactlist:ct005 |
      | Last Updated | Input,Select  | Between  | contactlist:ct002,contactlist:ct003 |


  @Contact @AC2 @TC14-TC18
  Scenario Outline: Verify that search function should work correctly for Name column when updating input value on during advance search
    * Load test data: DataFile = ContactList, Data = Advance_Search_ContactList3
    * Create multiple contact lists by API
    Given The Contact list URL is hit to view the existing contact
    When Advance search for field "<Column Name>" by "<Operator>" with "<Input type>" text "<Value>" on contact list page
    And Verify advance search result
    And Update input value for advance search "<Column Name>" to "<New value>" by "<Input type>"
    Then Verify advance search result after updating input value
    * Clean up contact list data for testing

    @Contact @AC2 @TC14-TC18
    Examples:
      | Column Name | Operator | Value  | New value           | Input type |
      | Name        | Like     | LIST   | list                | Input      |
      | Name        | In       | list-1 | list-1,CONTACTLIST1 | Input      |
      | Name        | =        | LIST_D | contactlist2        | Input      |
      | Name        | !=       | list   | CONTACTLIST1        | Input      |
      | Name        | Not Like | list   | LIST_D              | Input      |


  @Contact @AC2 @TC19-TC21
  Scenario Outline: Verify that search function should work correctly for Last Updated column when updating input value on during advance search
    * Load test data: DataFile = ContactList, Data = Advance_Search_ContactList4
    * Create a list contact lists with interval time
    Given The Contact list URL is hit to view the existing contact
    When Advance search for field "<Column Name>" by "<Operator>" with "<Input type>" text "<Value>" on contact list page
    And Verify advance search result
    And Update input value for advance search "<Column Name>" to "<New value>" by "<Input type>"
    Then Verify advance search result after updating input value
    * Clean up contact list data for testing

    @Contact @AC2 @TC19-TC21
    Examples:
      | Column Name  | Operator | Value                                   | New value                               | Input type  |
      | Last Updated | >        | contactlist:ctl-002                     | contactlist:ctl-004                     | Select      |
      | Last Updated | <        | contactlist:ctl-005                     | contactlist:ctl-002                     | Input       |
      | Last Updated | Between  | contactlist:ctl-001,contactlist:ctl-003 | contactlist:ctl-002,contactlist:ctl-005 | Input,Input |


  @Contact @AC2 @TC21-TC27
  Scenario Outline: Verify that search function should work correctly when changing operator on during advance search
    * Load test data: DataFile = ContactList, Data = Advance_Search_ContactList5
    * Create multiple contact lists by API
    Given The Contact list URL is hit to view the existing contact
    When Advance search for field "<Column Name>" by "<Operator>" with "<Input type>" text "<Value>" on contact list page
    And Verify advance search result
    And Update operator of advance search column "<New operator>"
    Then Verify advance search result after updating operator
    * Clean up contact list data for testing

    @Contact @AC2 @TC21-TC27
    Examples:
      | Column Name  | Operator | Value                   | New operator | Input type |
      | Name         | Like     | testA                   | =            | Input      |
      | Name         | In       | test01,TEST,Test-ctlist | !=           | Input      |
      | Name         | =        | TEST                    | Like         | Input      |
      | Name         | !=       | TEST_B                  | Not Like     | Input      |
      | Name         | Not Like | testA                   | In           | Input      |
      | Last Updated | >        | contactlist:testA       | <            | Select     |
      | Last Updated | <        | contactlist:test01      | >            | Input      |


  @Contact @AC2 @TC28
  Scenario: Verify User can view contact when using advance search
    * Load test data: DataFile = ContactList, Data = Advance_Search_CTL28
    * Clean up contact list data for testing
    Given The Contact list URL is hit to view the existing contact
    And Create a contact list and import datasource by API
    When Search contact list with operator and search text
    Then User can view contact for contact list
    * Clean up contact list data for testing


  @Contact @AC2 @TC29
  Scenario: Verify that full contact list display after clearing search text of advance search
    * Load test data: DataFile = ContactList, Data = Advance_Search_CTL29
    Given The Contact list URL is hit to view the existing contact
    And Create a list 24 contact lists by API
    When Search contact list with operator and search text
    Then Full contact lists display on landing page after "clear search text"
    * Clean up contact list data for testing


  @Contact @AC2 @TC30
  Scenario: Verify message display when input invalid or unmatch search text
    * Load test data: DataFile = ContactList, Data = Advance_Search_CTL30
    Given The Contact list URL is hit to view the existing contact
    When Search contact list with operator and search text
    Then No record show on contact list landing page
    And Message "Record list is empty" is displayed in place of row on contact list landing page


  @Contact @AC2 @TC31
  Scenario: Verify User can import contact when using advance search
    * Load test data: DataFile = ContactList, Data = Advance_Search_CTL31
    * Create a contact list by API
    Given The Contact list URL is hit to view the existing contact
    When Search contact list with operator and search text
    And User can import contact for contact list searched
    * Clean up contact list data for testing


  @Contact @AC2 @TC32
  Scenario: Verify contact list should display full record after click Refresh button
    * Load test data: DataFile = ContactList, Data = Advance_Search_CTL32
    Given The Contact list URL is hit to view the existing contact
    And Create a list 32 contact lists by API
    When Search contact list with operator and search text
    Then Full contact lists display on landing page after "click refresh button"
    * Clean up contact list data for testing


  @Contact @AC2 @TC33
  Scenario: Verify all field should return default value after click Refresh button
    * Load test data: DataFile = ContactList, Data = Advance_Search_CTL33
    * Create a list 8 contact lists by API
    Given The Contact list URL is hit to view the existing contact
    When Search contact list with attribute, operator, type input and value search
    And User clicks the refresh button on contact list page
    Then All field return default value after click refresh button
    * Clean up contact list data for testing


  @Contact @AC2 @TC34-TC36
  Scenario Outline: Verify that Search box is cleared after changing operator on the operator field for Last Updated column
    * Load test data: DataFile = ContactList, Data = Advance_Search_ContactList6
    * Create a list contact lists with interval time
    Given The Contact list URL is hit to view the existing contact
    And Advance search for field "<Column Name>" by "<Operator>" with "<Input type>" text "<Value>" on contact list page
    When Changing "<New operator>" operator on the operator field
    Then Verify Search box is cleared with "<Column Name>" column, "<New operator>" operator
    * Clean up contact list data for testing

    @Contact @AC2 @TC34-TC36
    Examples:
      | Column Name  | Operator | Value                               | New operator | Input type |
      | Last Updated | <        | contactlist:ctl04                   | Between      | Input      |
      | Last Updated | Between  | contactlist:ctl01,contactlist:ctl05 | >            | Select     |

  @Contact @AC2 @TC43-TC44
  Scenario Outline: Verify that the Operator & Search box field are updated correctly after the colum name is changed
    * Load test data: DataFile = ContactList, Data = Advance_Search_ContactList7
    Given The Contact list URL is hit to view the existing contact
    * Create multiple contact lists by API
    And Advance search for field "<Column Name>" by "<Operator>" with "<Input type>" text "<Value>" on contact list page
    When Changing the column name to "<Column Name Change>"
    Then Verify that the Operator & Search box field are updated correctly after the colum name is changed to "<Column Name Change>"
    * Clean up contact list data for testing

    @Contact @AC2 @TC43-TC44
    Examples:
      | Column Name  | Operator | Value              | Input type | Column Name Change |
      | Name         | Like     | test01             | Input      | Last Updated       |
      | Last Updated | <        | contactlist:test01 | Input      | Name               |


  @Contact @AC2 @TC45
  Scenario: Verify that no failure toast message display after clearing search text
    * Load test data: DataFile = ContactList, Data = Advance_Search_CTL45
    Given The Contact list URL is hit to view the existing contact
    And Create a contact list by API
    When Search contact list with attribute, operator, type input and value search
    Then Clear search text and verify no failure toast message display
    * Clean up contact list data for testing


  @Contact @AC2 @TC46
  Scenario Outline: Verify that toast message will be display when input end time less than start time
    Given The Contact list URL is hit to view the existing contact
    When Advance search for field "<Column Name>" by "<Operator>" with "<Input type>" text "<Value>" on contact list page
    Then Verify that toast message "<Toast message>" will be display

    @Contact @AC2 @TC46
    Examples:
      | Column Name  | Operator | Value                   | Input type  | Toast message                                                  |
      | Last Updated | Between  | offset:+60s,offset:-60s | Input,Input | Invalid dates provided. From date should be less then to date. |

  @Contact @ContactList @QuickSearching @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case contact list name is listed using Lowercase and Uppercase Quick Search
    * Delete all contact list with Name contains "ContactList_MixCaseIn"
    Given The Contact list URL is hit to view the existing contact
    And Create a list of 1 consecutive contact list with name with Name start with "ContactList_MixCaseIn" and Description "ContactList_MixCaseIn"
    And Search contact list "contactlist_mixcasein"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn |
    And Search contact list "CONTACTLIST_MIXCASEIN"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn |
    And Delete all contact list with Name contains "ContactList_MixCaseIn"

  @Contact @ContactList @AdvanceSearching @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case contact list name is listed using Lowercase and Uppercase Advance Search equal operator
    * Delete all contact list with Name contains "ContactList_MixCaseIn"
    Given The Contact list URL is hit to view the existing contact
    And Create a list of 3 consecutive contact list with name with Name start with "ContactList_MixCaseIn" and Description "ContactList_MixCaseIn"
    When Search contact list with attribute "Name", operator "=", type input "Input" and value search "contactlist_mixcasein_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    When Search contact list with attribute "Name", operator "=", type input "Input" and value search "CONTACTLIST_MIXCASEIN_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    And Delete all contact list with Name contains "ContactList_MixCaseIn"

  @Contact @ContactList @AdvanceSearching @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case contact list name is listed using Lowercase and Uppercase Advance Search NOT equal operator
    * Delete all contact list with Name contains "ContactList_MixCaseIn"
    Given The Contact list URL is hit to view the existing contact
    And Create a list of 3 consecutive contact list with name with Name start with "ContactList_MixCaseIn" and Description "ContactList_MixCaseIn"
    When Search contact list with attribute "Name", operator "!=", type input "Input" and value search "contactlist_mixcasein_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    When Search contact list with attribute "Name", operator "!=", type input "Input" and value search "CONTACTLIST_MIXCASEIN_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    And Delete all contact list with Name contains "ContactList_MixCaseIn"

  @Contact @ContactList @AdvanceSearching @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case contact list name is listed using Lowercase and Uppercase Advance Search In operator
    * Delete all contact list with Name contains "ContactList_MixCaseIn"
    Given The Contact list URL is hit to view the existing contact
    And Create a list of 3 consecutive contact list with name with Name start with "ContactList_MixCaseIn" and Description "ContactList_MixCaseIn"
    When Search contact list with attribute "Name", operator "In", type input "Input" and value search "contactlist_mixcasein_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    When Search contact list with attribute "Name", operator "In", type input "Input" and value search "CONTACTLIST_MIXCASEIN_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    And Delete all contact list with Name contains "ContactList_MixCaseIn"

  @Contact @ContactList @AdvanceSearching @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case contact list name is listed using Lowercase and Uppercase Advance Search Like operator
    * Delete all contact list with Name contains "ContactList_MixCaseIn"
    Given The Contact list URL is hit to view the existing contact
    And Create a list of 3 consecutive contact list with name with Name start with "ContactList_MixCaseIn" and Description "ContactList_MixCaseIn"
    When Search contact list with attribute "Name", operator "Like", type input "Input" and value search "contactlist_mixcasein_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    When Search contact list with attribute "Name", operator "Like", type input "Input" and value search "CONTACTLIST_MIXCASEIN_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    And Delete all contact list with Name contains "ContactList_MixCaseIn"

  @Contact @ContactList @AdvanceSearching @P2 @IXOUTREACH-6181
  Scenario: Verify mixed Case contact list name is listed using Lowercase and Uppercase Advance Search NOT Like operator
    * Delete all contact list with Name contains "ContactList_MixCaseIn"
    Given The Contact list URL is hit to view the existing contact
    And Create a list of 3 consecutive contact list with name with Name start with "ContactList_MixCaseIn" and Description "ContactList_MixCaseIn"
    When Search contact list with attribute "Name", operator "Not Like", type input "Input" and value search "contactlist_mixcasein_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    When Search contact list with attribute "Name", operator "Not Like", type input "Input" and value search "CONTACTLIST_MIXCASEIN_2"
    Then Verify expected contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn\|ContactList_MixCaseIn_1 |
    Then Verify Not contactLists on Contact List
      | ContactListNames | ContactList_MixCaseIn_2 |
    And Delete all contact list with Name contains "ContactList_MixCaseIn"
