package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utilities.Driver;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import pages.BookItPages;
import utilities.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Lib {

    @Given("the user is on the Library app login page")
    public void the_user_is_on_the_library_app_login_page() {
        Driver.get().get("https://library2.cydeo.com/");
        Driver.get().findElement(By.id("inputEmail")).sendKeys("librarian1@library");
        Driver.get().findElement(By.id("inputPassword")).sendKeys("qU9mrvur");
        Driver.get().findElement(By.xpath("//button[.='Sign in']")).click();
    }
    int usersUI,usersAPI;
    int booksUI,booksAPI;
    int booksBorrowedUI,booksBorrowedAPI;
    int totalUI,totalAPI;
    @When("the user logs in as librarian")
    public void the_user_logs_in_as_librarian() {
        //Driver.get().findElement(By.id("navbarDropdown")).click();
        String user_count = Driver.get().findElement(By.xpath("//h2[@id='user_count']")).getText();
        List<WebElement> elements = Driver.get().findElements(By.xpath("//h2"));
        Integer reduce = elements.stream().map(a -> Integer.parseInt(a.getText())).collect(Collectors.toList()).stream().reduce(0, Integer::sum);

        totalUI = reduce;

        BrowserUtils.waitFor(4);
        //Driver.get().findElement(By.xpath("//a[.='Log Out']")).click();
    }
    @When("the user logs in as librarian to API")
    public void the_user_logs_in_as_librarian_to_api() throws JsonProcessingException {
    baseURI= "https://library2.cydeo.com/rest/v1/";
    Map<String,Object> map=new HashMap<>();
    map.put("email","librarian24@library");
    map.put("password","8v8ZByKA");
        ObjectMapper objectMapper=new ObjectMapper();
        String s = objectMapper.writeValueAsString(map);
        Response login = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .body(s).when().post("login");
        String token = login.jsonPath().getString("token");
        Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-library-token", token).when().get("dashboard_stats");
        usersAPI = Integer.parseInt(response.jsonPath().getString("book_count"));
        booksAPI = Integer.parseInt(response.jsonPath().getString("borrowed_books"));
        booksBorrowedAPI =Integer.parseInt( response.jsonPath().getString("users"));
        totalAPI = usersAPI+booksAPI+booksBorrowedAPI;
    }
    @Then("the informations getting from API and UI should be matched")
    public void the_informations_getting_from_api_and_ui_should_be_matched() {
     Assert.assertEquals(totalAPI,totalUI);


    }

}
