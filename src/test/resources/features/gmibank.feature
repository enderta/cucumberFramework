Feature: GMI Bank
  @gmi
  Scenario: Login
    Given User is on the GMIBank login page
    When User enters valid "gino.wintheiser" and "%B6B*q1!TH"
    Then User should be able to login successfully
