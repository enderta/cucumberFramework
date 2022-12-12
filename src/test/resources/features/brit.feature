@brit
Feature: Login tests


  Scenario: Login as a manager
    Given Manager on Invoicing Page




  Scenario Outline: Create a new Tax
    Given Manager on Invoicing Page
    And user clicks on Configuration=>Accounting Link from left menu
    Then user clicks on Taxes and create btn
    Then user fills out required information "<TaxName>", "<TaxAmount>"
    Then user clicks on save button
    And user should see and verifies details that entered "<TaxName>"

    Examples:
      | TaxName    | TaxAmount |
      | SimpleTax1 | 15        |
      | SimpleTax2 | 20        |


  Scenario Outline: Delete a Tax
    Given Manager on Invoicing Page
    And user clicks on Configuration=>Accounting Link from left menu
    And user open a tax that has "<TaxName>"
    Then user clicks on Action button
    And user clicks Delete option
    And user should see "Are you sure you want to delete this record ?" message
    Then user clicks to Ok button


    Examples:
      | TaxName    |
      | SimpleTax1 |
      | SimpleTax2 |