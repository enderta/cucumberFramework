/*
package stepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import utilities.Driver;

public class LibStepDefs {
    @Given("the user is on the Library app login page")
    public void the_user_is_on_the_library_app_login_page() {
        Driver.get().get("https://library2.cybertekschool.com/login.html");
        Driver.get().findElement(By.id("inputEmail")).sendKeys("librarian570@library");
        Driver.get().findElement(By.id("inputPassword")).sendKeys("2gCucjjn");
        Driver.get().findElement(By.xpath("//button[@type='submit']")).click();


    }
    @When("the user logs in as librarian")
    public void the_user_logs_in_as_librarian() {
        WebElement element = Driver.get().findElement(By.xpath("//a[.='Log Out']"));
        JavascriptExecutor js = (JavascriptExecutor) Driver.get();
        js.executeScript("arguments[0].click();", element);
    }


}
*/
