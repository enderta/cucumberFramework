package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import utilities.BrowserUtils;
import utilities.Driver;

public class Medduna3 {

  @Given("user is on the login page and singup page")
  public void user_is_on_the_login_page_and_singup_page() {
    Driver.get().get("https://medunna.com/");
    Driver.get().findElement(By.xpath("//li[@id='account-menu']")).click();
    Driver.get().findElement(By.id("login-item")).click();
    BrowserUtils.waitFor(4);
  }
  @Given("user sends username {string} and password {string}")
  public void user_sends_username_and_password(String user, String pass) {
  Driver.get().findElement(By.name("username")).sendKeys(user);
    Driver.get().findElement(By.name("password")).sendKeys(pass);
    BrowserUtils.waitFor(4);
    Driver.get().findElement(By.xpath("//button[@type='submit']")).click();
    BrowserUtils.waitFor(4);
  }
  @Then("verify the my page and logout")
  public void verify_the_my_page_and_logout() {
  Driver.get().findElement(By.xpath("//span[.='admin account']")).click();
  Driver.get().findElement(By.xpath("//a[@href='/logout']")).click();
    String text = Driver.get().findElement(By.xpath("//h2")).getText();
    Assert.assertEquals("THANK YOU FOR CHOOSING US...", text);
  }

}
