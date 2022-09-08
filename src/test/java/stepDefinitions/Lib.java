package stepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import utilities.Driver;

public class Lib {

    @Given("the user is on the Library app login page")
    public void the_user_is_on_the_library_app_login_page() {
        Driver.get().get("https://library2.cydeo.com/login.html");
        Driver.get().findElement(By.id("inputEmail")).sendKeys("librarian1@library");
        Driver.get().findElement(By.id("inputPassword")).sendKeys("qU9mrvur");
        Driver.get().findElement(By.xpath("//button[.='Sign in']")).click();
    }
    @When("the user logs in as librarian")
    public void the_user_logs_in_as_librarian() {
        Driver.get().findElement(By.id("navbarDropdown")).click();
       Driver.get().findElement(By.xpath("//a[.='Log Out']")).click();
    }
}
