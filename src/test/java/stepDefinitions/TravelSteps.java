package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.*;
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
@Given("I am on the hotel search page")
public void i_am_on_the_hotel_search_page() {
	Driver.get().get("https://phptravels.net");
	Driver.get().findElement(By.xpath("//a[@title='hotels']")).click();
	BrowserUtils.waitFor(2);
}
@When("I enter a valid location and dates")
public void i_enter_a_valid_location_and_dates() {
	WebDriver driver = Driver.get();
	driver.findElement(By.id("select2-hotels_city-container")).click();
// Locate the autocomplete element
	WebElement autocomplete = driver.findElement(By.xpath("//input[@class='select2-search__field']"));

// Click on the element to activate the dropdown menu
	autocomplete.click();

// Wait for the dropdown menu to appear
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("select2-results")));

// Locate the search box within the dropdown menu
	WebElement searchBox = driver.findElement(By.className("select2-search__field"));

// Type the text that you want to search for in the search box
	searchBox.sendKeys("Islamabad");

// Wait for the search results to appear
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(text(), 'Islamabad')]")));

// Locate the desired search result
	WebElement searchResult = driver.findElement(By.xpath("//li[contains(text(), 'Islamabad')]"));

// Click on the desired search result to select it
	searchResult.click();


	//date picker for check in

	// find the check-in date field and click it to open the date picker
	WebElement checkinField = driver.findElement(By.name("checkin"));
	checkinField.click();

	// find the month and year switcher and click it until we reach May 2023
	WebElement monthYearSwitcher = driver.findElement(By.xpath("(//th[@class='switch'])[1]"));
	while (!monthYearSwitcher.getText().equals("May 2023")) {
		driver.findElement(By.cssSelector("th.next")).click();
	}

	// find the date element for 15th of May and click it
	WebElement checkinDate = driver.findElement(By.xpath("//td[text()='15']"));
	checkinDate.click();
	BrowserUtils.waitFor(3);
	driver.findElement(By.xpath("(//*[@class='label-text'])[1]")).click();
	BrowserUtils.waitFor(3);
	// find the check-out date field and click it to open the date picker
	WebElement checkoutField = driver.findElement(By.id("checkout"));
	checkoutField.click();

	// find the month and year switcher and click it until we reach May 2023
	WebElement monthYearSwitcher2 = driver.findElement(By.xpath("(//th[@class='switch'])[4]"));
	while (!monthYearSwitcher2.getText().equals("May 2023")) {
		driver.findElement(By.cssSelector("th.next")).click();
	}

	// find the date element for 17th of May and click it
	WebElement checkoutDate = driver.findElement(By.xpath("(//td[text()='17'])[2]"));
	checkoutDate.click();


}
@When("I click the search button")
public void i_click_the_search_button() {
	Driver.get().findElement(By.id("submit")).click();
	BrowserUtils.waitFor(2);
}
@Then("I should see a list of available hotels in the specified location and dates")
public void i_should_see_a_list_of_available_hotels_in_the_specified_location_and_dates() {
	Assert.assertTrue(Driver.get().getCurrentUrl().contains("hotels"));
	BrowserUtils.waitFor(2);
}


}