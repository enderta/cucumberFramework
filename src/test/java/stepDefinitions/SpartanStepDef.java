package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.SpartanPages;
import utilities.BrowserUtils;
import utilities.Driver;
import utilities.SpartanApiDB;

import java.sql.SQLException;

public class SpartanStepDef {
    SpartanPages sp = new SpartanPages();
    WebDriver driver= Driver.get();

    String name1="";
    @Given("on the spartan page")
    public void on_the_spartan_page() {
        driver.get("http://54.147.17.39:8000/spartans");
    }
    @When("I click on the add button")
    public void i_click_on_the_add_button() {
        driver.findElement(sp.getForm()).click();
        BrowserUtils.waitFor(4);
    }
    @Then("I fill in the form with {string}, {string}, {string}")
    public void i_fill_in_the_form_with(String name, String gender, String phone) {
        this.name1=name;

        driver.findElement(sp.getName()).sendKeys(name);
        Select gnder = new Select(driver.findElement(sp.getGender()));
        gnder.selectByValue(gender);
        driver.findElement(sp.getPhone()).sendKeys(phone);
        driver.findElement(sp.getSumbit()).click();
    }
    @Then("I should see same person API and Database")
    public void i_should_see_same_person_api_and_database() throws SQLException {
        BrowserUtils.waitFor(4);
        String apiName = SpartanApiDB.get();
        Assert.assertEquals(name1,apiName);
        String DBName = SpartanApiDB.DB();
        Assert.assertEquals(name1,DBName);
    }

}
