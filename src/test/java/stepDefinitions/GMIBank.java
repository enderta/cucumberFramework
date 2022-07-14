package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import pages.GMIPages;
import utilities.BrowserUtils;
import utilities.Driver;

public class GMIBank {
    GMIPages gmiBank = new GMIPages();

    @Given("User is on the GMIBank login page")
    public void user_is_on_the_gmi_bank_login_page() {
        Driver.get().get("https://www.gmibank.com/");
        Driver.get().findElement(By.xpath("(//*[@class='dropdown-toggle nav-link'])[2]")).click();
        Driver.get().findElement(By.id("login-item")).click();
        BrowserUtils.waitFor(4);
    }
    @When("User enters valid {string} and {string}")
    public void user_enters_valid_and(String user, String pass) {
      gmiBank.login(user, pass);
        BrowserUtils.waitFor(4);
    }

    @Then("User should be able to login successfully")
    public void user_should_be_able_to_login_successfully() {
        String text = Driver.get().findElement(By.xpath("(//li[@id='account-menu']//a//span)[1]")).getText();
        Assert.assertEquals("Joe King", text);
    }

}
