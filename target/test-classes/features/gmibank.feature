Feature: GMI Bank

  Scenario: Login
    Given User is on the GMIBank login page
    When User enters valid "gino.wintheiser" and "%B6B*q1!TH"
    Then User should be able to login successfully
  @gmi
  Scenario: Login DB
  Given user is logging to the GMI DB
  When user sends get request to the DB
  Then user should be able to login successfully DB
