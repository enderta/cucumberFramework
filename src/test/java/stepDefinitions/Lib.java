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
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    String bookName;
    String catagory;
    @Given("the user as a librarian makes post request with using add_book end point with random values")
    public void the_user_as_a_librarian_makes_post_request_with_using_add_book_end_point_with_random_values() throws JsonProcessingException {
        baseURI= "https://library2.cydeo.com/rest/v1/";
        Map<String,Object> map=new HashMap<>();
        map.put("email","librarian24@library");
        map.put("password","8v8ZByKA");
        ObjectMapper objectMapper=new ObjectMapper();
        String s = objectMapper.writeValueAsString(map);
        Response login = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .body(s).when().post("login");
        String token = login.jsonPath().getString("token");
        Map<String,Object> map1=new HashMap<>();
        map1.put("name","CodingBook2");
        map1.put("isbn","123456");
        map1.put("year","2020");
        map1.put("author","James");
        map1.put("book_category_id","1");
        map1.put("description","Java is a programming language");
        bookName = map1.get("name").toString();
        ObjectMapper objectMapper1=new ObjectMapper();
        String s1 = objectMapper1.writeValueAsString(map1);
        Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-library-token", token).body(s1).when().post("add_book");
        response.prettyPrint();
        assertEquals(200,response.statusCode());
        BrowserUtils.waitFor(4);
    }
    @When("the user navigates to {string} page")
    public void the_user_navigates_to_page(String page) {
        List<WebElement> elements = Driver.get().findElements(By.xpath("//span"));
        elements.stream().filter(a->a.getText().equals(page)).findFirst().get().click();

    }
    @When("the user search corresponding book name")
    public void the_user_search_corresponding_book_name() {
        Driver.get().findElement(By.xpath("//input")).sendKeys(bookName);
        BrowserUtils.waitFor(2);


    }
    @Then("the user should see the book created in the API on the list")
    public void the_user_should_see_the_book_created_in_the_api_on_the_list() {

    }
    @Then("the user click edit button")
    public void the_user_click_edit_button() {
        Driver.get().findElement(By.xpath("(//a[.=' Edit Book'])[1]")).click();
        BrowserUtils.waitFor(2);
    }
    @Then("click save button see the msg {string}")
    public void click_save_button_see_the_msg(String msg) {
        Driver.get().findElement(By.xpath("//button[.='Save changes']")).click();
        BrowserUtils.waitFor(2);
        String actualMsg = Driver.get().findElement(By.xpath("//div[.='The book has been updated.']")).getText();
        assertEquals(msg,actualMsg);

    }
    String apiName;
    @Given("create new user with {int}")
    public void create_new_user_with(Integer type) throws JsonProcessingException {
        baseURI= "https://library2.cydeo.com/rest/v1/";
        Map<String,Object> map=new HashMap<>();
        map.put("email","librarian24@library");
        map.put("password","8v8ZByKA");
        ObjectMapper objectMapper=new ObjectMapper();
        String s = objectMapper.writeValueAsString(map);
        Response login = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .body(s).when().post("login");
        String token = login.jsonPath().getString("token");
        Map<String,Object> map1=new HashMap<>();
        map1.put("full_name","James Bond");
        map1.put("password","123456");
        map1.put("email","bond@library");
        map1.put("user_group_id",type);
        map1.put("status", "active");
        map1.put("start_date", "2022-09-14");
        map1.put("end_date", "2023-05-05");
        map1.put("address", "MI5");
        apiName = map1.get("full_name").toString();
        ObjectMapper objectMapper1=new ObjectMapper();
        String s1 = objectMapper1.writeValueAsString(map1);
        Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-library-token", token).body(s1).when().post("add_user");

    }
    @Then("user should be able to get {string} in response body for add user")
    public void user_should_be_able_to_get_in_response_body_for_add_user(String string) {
        Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .when().get("get_user_groups");
        List<String> names = response.jsonPath().getList("name");
        assertTrue(names.contains(apiName));


    }
    @Then("the user should see the user created in the API on the list")
    public void the_user_should_see_the_user_created_in_the_api_on_the_list() {

    }

    @When("the user search corresponding user name")
    public void the_user_search_corresponding_user_name() {

    }



}
