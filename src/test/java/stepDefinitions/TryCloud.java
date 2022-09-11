package stepDefinitions;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.SpartanPages;
import utilities.BrowserUtils;
import utilities.Driver;
import utilities.SpartanApiDB;

import java.sql.SQLException;
public class TryCloud {

    @Given("user on the login page")
    public void user_on_the_login_page() {
       Driver.get().get("http://qa3.trycloud.net/index.php/login?clear=1");
       BrowserUtils.waitFor(4);
    }
    @When("user enters username {string} and password {string}")
    public void user_enters_username_and_password(String user, String pass) {
        Driver.get().findElement(By.id("user")).sendKeys(user);
        Driver.get().findElement(By.id("password")).sendKeys(pass);

        BrowserUtils.waitFor(4);
    }
    @When("user click the login button")
    public void user_click_the_login_button() {
        Driver.get().findElement(By.id("submit-form")).click();
        BrowserUtils.waitFor(4);
    }
    @Then("verify the user should be at the dashboard page")
    public void verify_the_user_should_be_at_the_dashboard_page() {
        String expected = "Dashboard";
        String actual = Driver.get().getTitle();
        Assert.assertTrue(actual.contains(expected));
        BrowserUtils.waitFor(4);
    }

}
