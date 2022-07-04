Feature:admin should be login


  Scenario Outline:login_with_employee_credentials
    Given user is on the login page and singup page
    And user sends username "<username>" and password "<password>"
    Then verify the my page and logout

    Examples: Credential
      | username     | password |
      | adminaccount | 12345    |


  Scenario Outline: Api and DB connection
    Given user is on the login to api
    Then get all user and look for <id> user
    Then Check <id> on db
    And both information should be same

    Examples: User
      | id    |
      | 83551 |

 
  Scenario Outline: :Creating user
    Given user is on the login page and singup page
    And user sends username "adminaccount" and password "12345"
    Then user click administration and click on user management
    And user click on add user
    When enter "<Login>" "<firstname>" "<lastname>" "<email>" "<ssn>"
    Then user click on save
    Then user should see the user created successfully
    Then user sees this user in api and db
    Examples:
      | Login | firstname | lastname | email           | ssn         |
      | asfg4 | dsfg      | ksfg     | ksfg4@gmail.com | 515-55-9907 |

 @meduna
  Scenario Outline: Deleting user
    Given user is on APi
    When user use delet method by usin "<logname>"
    Then user use get mehtod and not find the user
    Examples:
      | logname |
      | asfg4   |


  Scenario Outline: Room creation
    Given user is on the login page and singup page
    And user sends username "adminaccount" and password "12345"
    Then user click administration and click on room management
    And user click on add room
    When enter "<roomnumber>" "<roomtype>" "<roomprice>" "<roomdescription>"
    Then user click on save
    Then user should see the room created successfully
    Then user sees this room in api and db
    Examples:
      | roomnumber | roomtype | roomprice | roomdescription |
      | 80000      | TWIN     | 100       | with TV         |

    Scenario: Country creation
        Given user create country in API
        Given user is on the login page and singup page
        And user sends username "adminaccount" and password "12345"
        Then user click administration and click on country management
        And the user should see the country created in the API on the list