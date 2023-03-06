@IXOUTREACH-4104 @IXOUTREACH-4771
Feature: Story IXOUTREACH-4771 Create contact attribute

##############################################################################
##############################################################################
##############################################################################

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type Integer
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_intType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type String
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_StringType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute using hyphen and underscore
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Hyphen_underscore
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type Float
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_FloatType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type Phone
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_PhoneType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attribute information is correct
    Then Verify "Phone" child attributes displayed on list page
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type Email
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_EmailType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attribute information is correct
    Then Verify "Email" child attributes displayed on list page
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type Date
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_DateType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type Time
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_TimeType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type DateTime
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_DateTimeType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type Boolean
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_BooleanType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type CHARACTER
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_CHARACTERType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create a new attribute with type ZIP
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_ZipType
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attribute information is correct
    Then Verify "Zip" child attributes displayed on list page
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages Japanese
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Japanese
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages Simplified Chinese
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_SimplifiedChinese
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages Korean
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Korean
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages French
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_French
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages German
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_German
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages Italian
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Italian
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages Russian
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Russian
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages Latin-Spanish
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Latin-Spanish
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages Brazilian-Portuguese
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Portuguese
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P3 @G-14
  Scenario: Verify create a new attribute with G-14 languages Hebrew
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Hebrew
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create attribute option during contact list creation
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_addAttributeButton
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then Verify Add New Attribute button is display
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify create attribute tab is not enable until contact list is created/saved
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_attributeTab
    Given User input information to create contact list without clicking save button
    Then Verify Attributes tab is disable
    Then Click to "Save" Button
    Then create contact list page is not closed and Attributes tab is enabled
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify the attribute name including space characters
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_spaceCharacter
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute with invalid value
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify the attribute name including special characters
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_specialCharacter
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute with Value character
      | Value |
      | !     |
      | @     |
      | #     |
      | $     |
      | %     |
      | ^     |
      | &     |
      | *     |
      | (     |
      | )     |
      | &     |
      | +     |
      | =     |
      | :     |
      | \"    |
      | ?     |
      | 1234  |
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify the attribute name is over 30 characters
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_over30Character
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    When User add new Contact Attribute
    Then Verify new attributes displayed is less than 30 characters
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify the attribute name is empty
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_EmptyName
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute with empty value
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify duplicate attribute name is not allowed
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Duplicate_1a
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Duplicate_1b
    Then User add new Contact Attribute with invalid value
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify attribute type is empty is not allowed
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_AttributeTypeEmpty
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute without saving
    Then Verify the save button is disabled
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify basic search work correctly
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_basicSearch
    Given user create contact list
    Then navigate to attribute tab
    Then User add new list Contact Attribute
    Then User input search string in basic search
    Then Verify basic search work as expected
    * Clean list attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify the search box should be clear and all data back to default when user click clear search
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_clearSearch
    Given user create contact list
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then User input search string in basic search
    Then Click to Clear Search button
    Then Verify the search box should be clear and all data back to default
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify the name column should be sorted when user click the name title
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_sortName
    Given user create contact list
    Then navigate to attribute tab
    When Create a list of 4 consecutive attribute by name
    Then User input search string in basic search
    Then Default order contact list of "Name" column displayed correctly after toggle Asc and Desc orders and go back to the default
    Then "Name" column should be sorted in desired order as per the admin's selection.
    * Clean consecutive attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify the data type column should be sorted when user click the data type title
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_dataType
    Given user create contact list
    Then navigate to attribute tab
    When Create a list of 9 consecutive attribute by data type
    Then User input search string in basic search
    Then Default order contact list of "Data Type" column displayed correctly after toggle Asc and Desc orders and go back to the default
    Then Verify page navigation with page size "100"
    Then "Data Type" column should be sorted in desired order as per the admin's selection.
    * Clean consecutive attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify the attribute type column should be sorted when user click the attribute type title
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_AttTypeName
    Given user create contact list
    Then navigate to attribute tab
    When Create a list of 4 consecutive attribute by name
    Then User input search string in basic search
    Then Default order contact list of "Type" column displayed correctly after toggle Asc and Desc orders and go back to the default
    Then "Type" column should be sorted in desired order as per the admin's selection.
    * Clean consecutive attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify search with *attname
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_regularExpression1
    Given user create contact list
    Then navigate to attribute tab
    Then User add new list Contact Attribute
    Then User input search string in basic search
    Then Verify basic search work as expected
    * Clean list attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify search with attname*
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_regularExpression2
    Given user create contact list
    Then navigate to attribute tab
    Then User add new list Contact Attribute
    Then User input search string in basic search
    Then Verify basic search work as expected
    * Clean list attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify search is case insensitive
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_insensitive
    Given user create contact list
    Then navigate to attribute tab
    Then User add new list Contact Attribute
    Then User input search string in basic search
    Then Verify basic search work as expected
    * Clean list attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify user click on Cancel button without input fields
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_CancelNonEdit
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then Click to "add" Button
    Then User clicks button "cancel" in dialog
    Then Verify cancel form when all values are not changed
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify user return back to attribute list page when user click Leave this page in cancel message confirmation box
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_CancelLeave
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute without saving
    Then User clicks button "cancel" in dialog
    Then Verify cancel form with "Leave this page" action
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify user will stay on that page when user click Stay on this page in cancel message confirmation box
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_CancelStay
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute without saving
    Then User clicks button "cancel" in dialog
    Then Verify cancel form with "Stay on this page" action
    Then Verify dialog "Add New Attribute" is still visible
    * Clean attribute testcase data

  @Contact @ContactAttribute @P1
  Scenario: Verify data is refresh and back to default when user click refresh
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_refreshButton
    Given user create contact list
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then User input search string in basic search
    Then Refresh button is available on page
    Then Click to "Refresh" Button
    Then Verify current page size is 10
    Then Verify data is refresh and back to default
    * Clean attribute testcase data

  @Contact @ContactAttribute @P2 @AC2
  Scenario: Verify user is not able to create a new attribute with hyphen and underscore only
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttr_Underscore_HYPHEN_ONLY
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute with invalid value
    * Clean attribute testcase data

  @Contact @ContactAttribute @P2 @AC2
  Scenario: Verify user is not able to create a new attribute starting with hyphen
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttr_Hyphen
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute with invalid value
    * Clean attribute testcase data

  @Contact @ContactAttribute @P2 @AC2
  Scenario: Verify user is not able to create a new attribute starting with Underscore
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttr_UnderScore
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute with invalid value
    * Clean attribute testcase data

  @Contact @ContactAttribute @P2 @AC3 @IgnoreInParallel
  Scenario: Verify newly added attribute must be displayed at top of the list in the attribute table
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_User
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attributes displayed on list page
    Then Verify new attribute information is correct
    Then Verify new the attributes created will have type "USER"
    * Clean attribute testcase data

  @Contact @ContactAttribute @P2 @AC7
  Scenario: Verify page navigation options on contact attribute list page
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Pagination01
    Given user create contact list
    When Check if page have 22 attributes
    Then Page size value displays "10" by default
    Then Verify page navigation with page size "10"
    Then Verify page navigation with page size "20"
    * Clean consecutive attribute testcase data

  @Contact @ContactAttribute @P2 @AC7
  Scenario: Verify page navigation options and wrong value in Go to page option
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Pagination02
    Given user create contact list
    When Check if page have 102 attributes
    Then Page size value displays "10" by default
    Then Verify page navigation with page size "50"
    Then Verify page navigation with page size "100"
    Then Input page number value "0,99999" which is not exist on landing page into Go to page field and verify message display
    * Clean consecutive attribute testcase data

  @Contact @ContactAttribute @1
  Scenario: Verify advanced search available on page
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_advSearchButton
    Given user create contact list
    Then navigate to attribute tab
    Then Click to filter button
    Then Click to filter button to disable
    Then Verify Advance search options are not displayed
    * Clean attribute testcase data

  @Contact @ContactAttribute @1
  Scenario: Verify advanced search work correctly
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_advSearch
    Given user create contact list
    Then navigate to attribute tab
    When Create a list of 4 consecutive attribute by name
    Then Click to filter button
    Then Apply Advanced search for field with operator and the value name and verify search result
      | Column | Operator | Value                                         |
      | Name   | Like     | Attr_verifyADVSearch                          |
      | Name   | In       | Attr_verifyADVSearch_1,Attr_verifyADVSearch_2 |
      | Name   | =        | Attr_verifyADVSearch_1                        |
      | Name   | !=       | Attr_verifyADVSearch_1                        |
      | Name   | Not Like | anyDifferentValue                             |
    * Clean consecutive attribute testcase data

  @Contact @ContactAttribute @1
  Scenario: Verify advanced search work correctly with empty result
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_advSearchEmpty
    Given user create contact list
    Then navigate to attribute tab
    When Create a list of 4 consecutive attribute by name
    Then Click to filter button
    Then Apply Advanced search for field with operator and the value name and verify empty search result
      | Column | Operator | Value                                             |
      | Name   | Like     | attr_AdvSearchEmpty_Dup                           |
      | Name   | In       | attr_AdvSearchEmpty1_Dup,attr_AdvSearchEmpty2_Dup |
      | Name   | =        | attr_AdvSearchEmpty_Dup                           |
    * Clean consecutive attribute testcase data
