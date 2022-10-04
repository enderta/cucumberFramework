Feature: As a data consumer, I want the user information are
  stored in MySQL DB correctly in users table


  Scenario: verify users table columns
    Given Establish the database connection
    When Execute query to get all columns
    Then verify the below columns are listed in result
      | id            |
      | full_name     |
      | email         |
      | password      |
      | user_group_id |
      | image         |
      | extra_data    |
      | status        |
      | is_admin      |
      | start_date    |
      | end_date      |
      | address       |


  Scenario Outline: Login with valid credentials as Student and Librarian
    Given user on the login page of the library
    When user enters "<username>" and "<password>"
    And user click "Sign in" button
    Then Verify user see the "Library" page
    Examples:
      | username            | password |
      | student6@library    | NXhpXJdC |
      | student58@library   | WS3rm9xG |



  @dblib
  Scenario: verify the amount of borrowed books
    Given user log in as a librarian
    When user take borrowed books number
    Then borrowed books number information must match with DB