Feature: GMI Bank

  Scenario: Login
    Given User is on the GMIBank login page
    When User enters valid "gino.wintheiser" and "%B6B*q1!TH"
    

  Scenario: Login DB
  Given user is logging to the GMI DB
  When user sends get request to the DB
  Then user should be able to login successfully DB

    @gmi
    Scenario: Login API
    Given user is logging to the GMI API
    When user sends get request to the API
