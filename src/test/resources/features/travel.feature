
Feature: User Registration and Login


  Scenario: Register a new user
    Given I am on the registration page
    When I fill in the registration form with valid information
    And I submit the registration form
    Then I should be redirected to the login page
    And I should see a success message

  Scenario: Login as a registered user
    Given I am on the login page
    When I fill in the login form with valid credentials
    And I submit the login form
    Then I should be redirected to the user dashboard
    And I should see a welcome message

  @travel
  Scenario: Search for a hotel by location and dates
    Given I am on the hotel search page
    When I enter a valid location and dates
    And I click the search button
    Then I should see a list of available hotels in the specified location and dates