@IXOUTREACH-6328
Feature: Story IXOUTREACH-6328 System defined attribute association with contact

##############################################################################
##############################################################################
##############################################################################

  @Contact @ContactAttribute @SystemDefinedContactAttribute @P1
  Scenario: When new list is created then verify system defined attributes populated in list attribute page
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_systemDefined01
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then Verify system defined attributes displayed on list page
    * Clean up contact list data for testing


  @Contact @ContactAttribute @SystemDefinedContactAttribute @P1
  Scenario: Verify user is not allowed to create a new attribute with same name like system attribute
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_systemDefined02
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User try to add new attribute with same name like system attribute with then verify message displayed
      | name               | dataType | Message                                                                                                                |
      | StatusFlag         | String   | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | Notes              | String   | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | NuisanceTimeStamp  | String   | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | LastAttemptTime    | String   | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | LastAddress        | String   | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | HandledBy          | String   | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | Id                 | String   | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | UpdatedOn          | DATETIME | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | CreatedOn          | DATETIME | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | LastCompletionCode | String   | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
    * Clean up contact list data for testing

  @Contact @ContactAttribute @PreDefinedContactAttribute @P1
  Scenario: Verify we can add Phone type of attribute with 30 char and verify all child gets created successfully
    * Load test data: DataFile = ContactAttributeTestData, Data = PhoneAttribute_PreDefined01
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attribute information is correct
    Then Verify "Phone" child attributes displayed on list page
    * Clean attribute testcase data

  @Contact @ContactAttribute @PreDefinedContactAttribute @P1
  Scenario: Verify we can add Email type of attribute with 30 char and verify all child gets created successfully
    * Load test data: DataFile = ContactAttributeTestData, Data = EmailAttribute_PreDefined02
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attribute information is correct
    Then Verify "Email" child attributes displayed on list page
    * Clean attribute testcase data

  @Contact @ContactAttribute @PreDefinedContactAttribute @P1
  Scenario: Verify user is not allowed to create a new Phone attribute when one of the associate attribute exist
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Predefine01
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User try to add new phone attribute however its associate attribute is matching with one existing attribute then verify message displayed
      | ChildAttributeName | ChildAttributeDataType | AttributeName        | DataType | Message                                                                                                                |
      | _Consent           | String                 | TestPredefinedPhone1 | Phone    | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | _Area_TZ           | String                 | TestPredefinedPhone2 | Phone    | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | _State             | String                 | TestPredefinedPhone3 | Phone    | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | _Type              | String                 | TestPredefinedPhone4 | Phone    | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | _Country           | String                 | TestPredefinedPhone5 | Phone    | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | _Zipcode           | String                 | TestPredefinedPhone6 | Phone    | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | _Status            | String                 | TestPredefinedPhone7 | Phone    | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
    * Clean attribute testcase data

  @Contact @ContactAttribute @PreDefinedContactAttribute @P1
  Scenario: Verify user is not allowed to create a new Email attribute when one of the associate attribute exist
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Predefine02
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User try to add new phone attribute however its associate attribute is matching with one existing attribute then verify message displayed
      | ChildAttributeName | ChildAttributeDataType | AttributeName        | DataType | Message                                                                                                                |
      | _Consent           | String                 | TestPredefinedEmail1 | EMAIL    | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
      | _Status            | Boolean                | TestPredefinedEmail1 | EMAIL    | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
    * Clean attribute testcase data

  @Contact @ContactAttribute @PreDefinedContactAttribute @P1
  Scenario: Verify user is not allowed to create a new Zip attribute when one of the associate attribute exist
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Predefine04
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User try to add new phone attribute however its associate attribute is matching with one existing attribute then verify message displayed
      | ChildAttributeName | ChildAttributeDataType | AttributeName      | DataType | Message                                                                                                                |
      | _TZ                | String                 | TestPredefinedZip1 | ZIP      | Attribute or it's associated attribute with given name already exists. Please provide a unique contact attribute name. |
    * Clean attribute testcase data

  @Contact @ContactAttribute @PreDefinedContactAttribute @P1
  Scenario: Verify user is allowed to create a new non Phone attribute when one of the phone type associate attribute exist
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Predefine03
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User try to add new "non phone" attribute when predefined attribute is matching with one existing attribute then verify message displayed
      | ChildAttributeName | ChildAttributeDataType | AttributeName        | DataType  | Message                                      |
      | _Consent           | String                 | TestPredefinedPhone1 | CHARACTER | Contact list attribute created successfully. |
      | _Area_TZ           | String                 | TestPredefinedPhone2 | STRING    | Contact list attribute created successfully. |
      | _State             | String                 | TestPredefinedPhone3 | FLOAT     | Contact list attribute created successfully. |
      | _Type              | String                 | TestPredefinedPhone4 | DATE      | Contact list attribute created successfully. |
      | _Country           | String                 | TestPredefinedPhone5 | TIME      | Contact list attribute created successfully. |
      | _Zipcode           | String                 | TestPredefinedPhone6 | DATETIME  | Contact list attribute created successfully. |
      | _Status            | String                 | TestPredefinedPhone7 | BOOLEAN   | Contact list attribute created successfully. |
      | _Status            | String                 | TestPredefinedPhone8 | INTEGER   | Contact list attribute created successfully. |
    * Clean attribute testcase data

  @Contact @ContactAttribute @PreDefinedContactAttribute @P1
  Scenario: Verify user is allowed to create a new non Email attribute when one of the Email type associate attribute exist
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Predefine05
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User try to add new "non Email" attribute when predefined attribute is matching with one existing attribute then verify message displayed
      | ChildAttributeName | ChildAttributeDataType | AttributeName        | DataType  | Message                                      |
      | _Consent           | String                 | TestPredefinedEmail1 | CHARACTER | Contact list attribute created successfully. |
      | _Status            | String                 | TestPredefinedEmail2 | INTEGER   | Contact list attribute created successfully. |
    * Clean attribute testcase data

  @Contact @ContactAttribute @PreDefinedContactAttribute @P1
  Scenario: Verify user is allowed to create a new non Zip attribute when one of the Zip type associate attribute exist
    * Load test data: DataFile = ContactAttributeTestData, Data = ContactAttribute_Predefine06
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User try to add new "non ZIP" attribute when predefined attribute is matching with one existing attribute then verify message displayed
      | ChildAttributeName | ChildAttributeDataType | AttributeName        | DataType  | Message                                      |
      | _TZ                | String                 | TestPredefinedZipAtt | CHARACTER | Contact list attribute created successfully. |
    * Clean attribute testcase data


  @Contact @ContactAttribute @PreDefinedContactAttribute @P1
  Scenario: Verify we can add Zip type of attribute with 30 char and verify all child gets created successfully
    * Load test data: DataFile = ContactAttributeTestData, Data = ZipAttribute_PreDefined03
    Given user create contact list
    Then create contact list page is not closed and Attributes tab is enabled
    Then navigate to attribute tab
    Then User add new Contact Attribute
    Then Verify new attribute information is correct
    Then Verify "Zip" child attributes displayed on list page
    * Clean attribute testcase data
