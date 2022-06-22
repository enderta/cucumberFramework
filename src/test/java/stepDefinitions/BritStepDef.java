package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import pages.BritPAge;
import utilities.BrowserUtils;
import utilities.Driver;

public class BritStepDef {
    BritPAge britPAge = new BritPAge();
    @Given("Manager on Invoicing Page")
    public void manager_on_invoicing_page() {
       britPAge.login("Lunch_InvoicingManager2@info.com","LD686gfX23");
        String text = Driver.get().findElement(By.xpath("//ol//li[@class='active']")).getText();
        BrowserUtils.waitFor(3);
        Assert.assertEquals("#Inbox", text);
        Driver.get().findElement(By.xpath("(//span[@class ='oe_menu_text'])[8]")).click();
        BrowserUtils.waitFor(4);


    }

    @Given("user clicks on Configuration=>Accounting Link from left menu")
    public void user_clicks_on_configuration_accounting_link_from_left_menu() {
       Driver.get().findElement(By.xpath("//span[@class='oe_menu_text'][contains(text(),'Accounting')]")).click();
       BrowserUtils.waitFor(3);
       Driver.get().findElement(By.xpath("//span[@class='oe_menu_text'][contains(text(),'Taxes')]")).click();
         BrowserUtils.waitFor(3);
    }
    @Then("user clicks on Taxes and create btn")
    public void user_clicks_on_taxes_and_create_btn() {
        Driver.get().findElement(By.xpath("//button[contains(text(),'Create')]")).click();
        BrowserUtils.waitFor(3);
    }
    @Then("user fills out required information {string}, {string}")
    public void user_fills_out_required_information(String name, String amount) {
       Driver.get().findElement(By.xpath("//input[@name='name']")).sendKeys(name);
         Driver.get().findElement(By.xpath("//input[@name='amount']")).sendKeys(amount);
            BrowserUtils.waitFor(3);
    }
    @Then("user clicks on save button")
    public void user_clicks_on_save_button() {
       Driver.get().findElement(By.xpath("//button[@class='btn btn-primary btn-sm o_form_button_save']")).click();
        BrowserUtils.waitFor(3);
    }
    @Then("user should see and verifies details that entered {string}")
    public void user_should_see_and_verifies_details_that_entered(String name) {
        String text = Driver.get().findElement(By.xpath("//span[@name='name']")).getText();
        Assert.assertEquals(name,text);

    }
    @Given("user open a tax that has {string}")
    public void user_open_a_tax_that_has(String name) {
        Driver.get().findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys(name, Keys.ENTER);

    }
    @Then("user clicks on Action button")
    public void user_clicks_on_action_button() {
      Driver.get().findElement(By.xpath("//tbody//tr//td[3]")).click();
        BrowserUtils.waitFor(3);
    }
    @Then("user clicks Delete option")
    public void user_clicks_delete_option() {
        Driver.get().findElement(By.xpath("//button[contains(text(),'Action')]")).click();
        BrowserUtils.waitFor(3);

       Driver.get().findElement(By.xpath("//button[contains(text(),'Action')]//..//ul//li/a[contains(text(),'Delete')]")).click();
    }
    @Then("user should see {string} message")
    public void user_should_see_message(String msg) {
        String text = Driver.get().findElement(By.xpath("//div[@class='modal-body']")).getText();
        Assert.assertEquals(msg,text);

    }
    @Then("user clicks to Ok button")
    public void user_clicks_to_ok_button() {
        Driver.get().findElement(By.xpath("//button[@type='button']//span[.='Ok']")).click();
    }





}
