Feature: End to end login test

  Scenario: Login as a student to UI
    Given the user is on the Library app login page
    When  the user logs in as librarian


  Scenario: Login as a librarian to UI
    Given the user is on the Library app login page
    When the user logs in as librarian

  Scenario: Checking dashboard datas
    Given the user is on the Library app login page
    When the user logs in as librarian
    When the user logs in as librarian to API
    Then the informations getting from API and UI should be matched

  Scenario: Add book as a librarian on api
    Given the user as a librarian makes post request with using add_book end point with random values
    Given the user is on the Library app login page
    When the user logs in as librarian
    And the user navigates to "Books" page
    And the user search corresponding book name
    Then the user should see the book created in the API on the list
    And the user click edit button
    Then click save button see the msg "The book has been updated."


  @lib
  Scenario Outline: user can create user on api <userType>
    Given create new user with <userType>
    Then user should be able to get "The user has been created." in response body for add user
    When the user logs in as librarian
    And the user navigates to "User" page
    And the user search corresponding user name
    Then the user should see the user created in the API on the list
    And the user click edit button
    Then click save button see the msg "The user updated."
    Examples:
      | userType |
      | 2        |
