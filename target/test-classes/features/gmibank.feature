Feature: GMI Bank

  Background: Login
    Given User is on the GMIBank login page
    When User enters valid "gino.wintheiser" and "%B6B*q1!TH"


  Scenario: Login DB
    Given user is logging to the GMI DB
    When user sends get request to the DB
    Then user should be able to login successfully DB


  Scenario: Login API
    Given user is logging to the GMI API
    When user sends get request to the API
  @gmi
  Scenario Outline: UI API DB
    Given user is getting info about user of "<ID>" from UI
    Then user should be able to get info about user of "<ID>" from DB And API
    Then all the info should be same
    Examples:
      | ID |
      |38016|