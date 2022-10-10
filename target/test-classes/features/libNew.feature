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




  Scenario: verify the amount of borrowed books
    Given user log in as a librarian
    When user take borrowed books number
    Then borrowed books number information must match with DB



  Scenario: verify the common book genre that’s being borrowed
    Given user log in as a librarian
    When user goes to "Books" page
    And user selects "500" records from dropdown
    And user gets most popular book genre
    And execute a query to find the most popular book genre from DB
    Then verify that most popular genre from UI is matching to DB


  Scenario: verify book user that’s being borrowed
    Given user log in as a librarian
    When user goes to "Books" page
    And user selects "500" records from dropdown
    And user gets most popular user who reads the most
    And execute a query to find the most popular user from DB
    Then verify that most popular user from UI is matching to DB

  @dblib
  Scenario: Verify book information with DB
    Given user log in as a librarian
    And user goes to "Books" page
    When user searches for "Kod Da Vinchi" book
    Then book information must match with the Database