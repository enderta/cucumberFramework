package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.BrowserUtils;
import utilities.Driver;

import java.time.Duration;

public class TravelSteps {

@Given("I am on the registration page")
public void i_am_on_the_registration_page() {
	Driver.get().get("https://phptravels.net");
	Driver.get().findElement(By.id("ACCOUNT")).click();
	Driver.get().findElement(By.xpath("//a[.='Customer Login']")).click();
	BrowserUtils.waitFor(2);
}

@When("I fill in the registration form with valid information")
public void i_fill_in_the_registration_form_with_valid_information() {


	BrowserUtils.waitFor(2);


}

@When("I submit the registration form")
public void i_submit_the_registration_form() {
	BrowserUtils.waitFor(2);
}

@Then("I should be redirected to the login page")
public void i_should_be_redirected_to_the_login_page() {
	Assert.assertTrue(Driver.get().getTitle().contains("Login"));
	BrowserUtils.waitFor(2);

}

@Then("I should see a success message")
public void i_should_see_a_success_message() {
	Driver.get().findElement(By.name("email")).sendKeys("et@gmail.com");
	Driver.get().findElement(By.name("password")).sendKeys("123456");

	//Driver.get().findElement(By.id("rememberchb")).click();
	Driver.get().findElement(By.xpath("//button[.='Login']")).click();
	BrowserUtils.waitFor(2);
	Assert.assertTrue(Driver.get().getCurrentUrl().contains("account"));

}


}