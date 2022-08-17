package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import utilities.BrowserUtils;
import utilities.Driver;

public class GMIBank2 {

  @Given("User is on the GMIBank login page")
  public void user_is_on_the_gmi_bank_login_page() {
    Driver.get().get("https://www.gmibank.com/");
    Driver.get().findElement(By.xpath("(//*[@class='dropdown-toggle nav-link'])[2]")).click();
    Driver.get().findElement(By.id("login-item")).click();


  }
  @When("User enters valid {string} and {string}")
  public void user_enters_valid_and(String user, String pass) {
    Driver.get().findElement(By.xpath("//input[@id='username']")).sendKeys(user);
    Driver.get().findElement(By.xpath("//input[@id='password']")).sendKeys(pass);
    Driver.get().findElement(By.xpath("//button[@type='submit']")).click();
    BrowserUtils.waitFor(4);
    String text = Driver.get().findElement(By.xpath("//span[.='Joe King']")).getText();
    Assert.assertEquals("Joe King", text);

  }

}
