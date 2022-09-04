Feature: Only authorized users should be able to login to the application.


  @zero
  Scenario: Login with valid credentials
    Given the user is logged in
    Then the "Account Summary" page should be displayed

