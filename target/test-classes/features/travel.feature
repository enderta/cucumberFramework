
Feature: User Registration and Login

  @travel
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