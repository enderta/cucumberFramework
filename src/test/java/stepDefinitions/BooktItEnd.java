package stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.BookItPages;
import utilities.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class BooktItEnd {
    @Given("user logs in using {string} {string}")
    public void user_logs_in_using(String username, String pass) {
        Driver.get().get("https://cybertek-reservation-qa3.herokuapp.com/sign-in");
        Driver.get().findElement(By.xpath("(//input)[1]")).sendKeys(username);
        Driver.get().findElement(By.xpath("(//input)[2]")).sendKeys(pass);
        Driver.get().findElement(By.xpath("//button")).click();
        BrowserUtils.waitFor(4);
    }
    String nameUI,nameAPI,romUI,token,romNameAPI,APIDescription,DBDescription,email,pass;
    @When("user is on the my self page")
    public void user_is_on_the_my_self_page() {
        Actions actions = new Actions(Driver.get());
        WebElement element = Driver.get().findElement(By.xpath("(//a[.='my'])[2]") );
        WebElement element1 = Driver.get().findElement(By.xpath("(//a[.='self'])"));
        actions.moveToElement(element).click(element1).perform();
        BrowserUtils.waitFor(3);
        nameUI=Driver.get().findElement(By.xpath("(//*[.='name']//..//p)[1]")).getText();
    }
    @When("I logged Bookit api using {string} and {string}")
    public void i_logged_bookit_api_using_and(String mail, String pass) {
        baseURI="https://cybertek-reservation-api-qa2.herokuapp.com";
        Response tkn = given().accept(ContentType.JSON).queryParam("email", mail).queryParam("password", pass).when().get("/sign").prettyPeek();
        token = tkn.jsonPath().getString("accessToken");


    }
    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {
        Response authorization = given().accept(ContentType.JSON).header("Authorization", token).get("/api/users/me").prettyPeek();
        nameAPI = authorization.jsonPath().getString("firstName")+" "+authorization.jsonPath().getString("lastName");

    }
    @Then("UI,API and Database user information must be match")
    public void ui_api_and_database_user_information_must_be_match() {

        Assert.assertEquals(nameUI,nameAPI);
    }

}
