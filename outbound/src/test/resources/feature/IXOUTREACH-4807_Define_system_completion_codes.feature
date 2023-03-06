@IXOUTREACH-3981 @IXOUTREACH-4807 @P1
@CompletionCode
Feature: Story IXOUTREACH-4807 Define system completion codes

##############################################################################
##############################################################################
##############################################################################

  @Campaign @AC1_TC1 @P1
  Scenario: Verify that header table on completion code landing page includes Code, Description, Type, RPC, Success and Closure column
    Given The Completion code URL is hit to view the existing complete code
    Then All column as "Code,Description,Type,RPC,Success,Closure" display on header table

  @Campaign @AC1_TC2 @P1
  Scenario Outline: Verify that data system completion code display correctly on cc landing page
    Given The Completion code URL is hit to view the existing complete code
    Then Data of system completion code display correctly on landing cc page
      | Code   | Description   | Type   | RPC   | Success   | Closure   |
      | <Code> | <Description> | <Type> | <RPC> | <Success> | <Closure> |

    @Campaign @AC1_TC2 @P1
    Examples:
      | Code                 | Description                              | Type   | RPC   | Success | Closure |
      | SMS_Queued           | SMS queued in the SMS server             | SYSTEM | false | false   | false   |
      | SMS_Sent             | SMS Sent to Service Provider Network     | SYSTEM | false | false   | false   |
      | SMS_Delivered        | SMS Delivered to Phone                   | SYSTEM | false | false   | false   |
      | SMS_Failed           | Outbound service failed to send SMS      | SYSTEM | false | false   | false   |
      | SMS_Rejected         | SMS Rejected by the SMS service          | SYSTEM | false | false   | false   |
      | SMS_Couldnot_Send    | Outbound Service can not send the SMS    | SYSTEM | false | false   | false   |
      | SMS_Couldnot_Deliver | Outbound Service can not Deliver the SMS | SYSTEM | false | false   | false   |

  @Campaign @AC2_TC1 @P1
  Scenario: Verify that all system completion codes display correct with System type
    Given The Completion code URL is hit to view the existing complete code
    Then Verify that system completion code is defined as System type
      | Code                 | Type   |
      | SMS_Queued           | SYSTEM |
      | SMS_Sent             | SYSTEM |
      | SMS_Delivered        | SYSTEM |
      | SMS_Failed           | SYSTEM |
      | SMS_Rejected         | SYSTEM |
      | SMS_Couldnot_Send    | SYSTEM |
      | SMS_Couldnot_Deliver | SYSTEM |

  @Campaign @AC3_TC1 @P1
  Scenario: Verify that all system completion codes display correct with three properties
    Given The Completion code URL is hit to view the existing complete code
    Then Verify that each system completion code has following three properties
      | Code                 | RPC   | Success | Closure |
      | SMS_Queued           | false | false   | false   |
      | SMS_Sent             | false | false   | false   |
      | SMS_Delivered        | false | false   | false   |
      | SMS_Failed           | false | false   | false   |
      | SMS_Rejected         | false | false   | false   |
      | SMS_Couldnot_Send    | false | false   | false   |
      | SMS_Couldnot_Deliver | false | false   | false   |

  @Campaign @AC4_TC1 @P1
  Scenario Outline: Verify that sort function should be work fine on Completion code landing page
    Given The Completion code URL is hit to view the existing complete code
    Then User should be able to sort the "<nameColum>" colum

    Examples:
      | nameColum |
      | Code      |
      | Type      |
      | RPC       |
      | Success   |
      | Closure   |

  @Campaign @AC4_TC2 @P1
  Scenario: Verify the sort icon should be displayed after clicking sort on the completion code landing page
    Given The Completion code URL is hit to view the existing complete code
    Then  Sorting each colum and the sort icon should be displayed
      | Code | Type | RPC | Success | Closure |

  @Campaign @AC5_TC1 @P1
  Scenario: Verify search text box and clear search display on completion code page
    Given The Completion code URL is hit to view the existing complete code
    Then  search text box displayed on completion code page
    And Clear search display on completion code page

  @Campaign @AC5_TC2 @P1
  Scenario: Verify the user should be able to search the completion code by Name using Like operator
    Given The Completion code URL is hit to view the existing complete code
    Then Search results displayed correctly by using like operator when user search with Name is "SMS_Queued"

  @Campaign @AC5_TC3 @P1
  Scenario: Verify no results display when user input invalid/unmatch to search box
    Given The Completion code URL is hit to view the existing complete code
    When User input "Call" to search text box and "yes" click Enter
    Then Search results displayed empty on completion code page
    And Message "Record list is empty" is displayed in place of row on completion code landing page

  @Campaign @AC5_TC4 @P1
  Scenario: Verify user can re-input text to search text box and it work correctly.
    Given The Completion code URL is hit to view the existing complete code
    When User input "SMS_Queued" to search text box and "yes" click Enter
    Then Search results displayed correctly by using like operator when user re-input with Name is "SMS_Couldnot"

  @CompletionCode @P4 @IXOUTREACH-6020 @Campaign
  Scenario: Verify Quick search on Completion code allows 30 charaters
    Given The Completion code URL is hit to view the existing complete code
    And Quick search code on Completion code
      | CompletioncodeNames | C1234567891234567890123456789_1 |
    Then Verify searched Completion code length
      | SearchStringLength | 30 |


  @CompletionCode @P2 @IXOUTREACH-6181 @Campaign
  Scenario: Verify mixed Case Completion code name is listed using Lowercase and Uppercase Quick Search
    Given The Completion code URL is hit to view the existing complete code
    Then Search results displayed correctly by using like operator when user search with Name is "sms_sent"
    Then Search results displayed correctly by using like operator when user search with Name is "SMS_SENT"

  @IXOUTREACH-6458 @Campaign
  Scenario: Verify failure toast message should not display when input special character on text box search
    Given The Completion code URL is hit to view the existing complete code
    Then Failure toast message is not display when user try entering the characters "`~!@#$%^&*()-_+=[]\\{}|;':\",./<>? " in text box "basic" search
