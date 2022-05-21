Feature: spartan crud
@spartan
  Scenario Outline: crud
    Given on the spartan page
    When I click on the add button
    Then I fill in the form with "<name>", "<gender>", "<phone>"
    And I should see same person API and Database
    Examples:
      | name | gender | phone      |
      | Aly2  | Female | 4218971348 |
      | Aly3  | Female | 4218971348 |


