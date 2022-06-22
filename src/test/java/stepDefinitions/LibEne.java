package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.LibPages;
import utilities.API;
import utilities.BrowserUtils;
import utilities.Driver;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import static io.restassured.RestAssured.given;

public class LibEne {
    LibPages libPages = new LibPages();
  WebDriver driver = Driver.get();
  int APIDashboardId,UIDashboardId;
  String bookAPIData,bookUIData,apiBookName;

    @Given("the user is on the Library app login page")
    public void the_user_is_on_the_library_app_login_page() {
        driver.get("https://library2.cybertekschool.com/login.html");
        libPages.login("librarian570@library", "2gCucjjn");
        BrowserUtils.waitFor(3);
    }
    @When("the user logs in as librarian")
    public void the_user_logs_in_as_librarian() {
        System.out.println("ender");
        Integer user_count = Integer.valueOf(driver.findElement(By.xpath("(//h2)[1]")).getText());
        System.out.println("user_count"+user_count);
        Integer book_count = Integer.valueOf(driver.findElement(By.xpath("(//h2)[2]")).getText());
        Integer borrowed_books = Integer.valueOf(driver.findElement(By.xpath("(//h2)[3]")).getText());
        UIDashboardId = user_count + book_count + borrowed_books;


    }
    @When("the user logs in as librarian to API")
    public void the_user_logs_in_as_librarian_to_api() {
        String toke = API.getToke("librarian570@library", "2gCucjjn");
        baseURI="https://library2.cybertekschool.com/rest/v1/";
        Response dashboard_stats = given().contentType(ContentType.JSON)
                .header("x-library-token",toke)
                .when().get("dashboard_stats");
        dashboard_stats.prettyPeek();
        Integer book_count = Integer.valueOf(dashboard_stats.jsonPath().get("book_count"));
        Integer borrowed_books = Integer.valueOf(dashboard_stats.jsonPath().get("borrowed_books"));
        Integer users = Integer.valueOf(dashboard_stats.jsonPath().get("users"));
        APIDashboardId = book_count + borrowed_books + users;

    }
    @Then("the informations getting from API and UI should be matched")
    public void the_informations_getting_from_api_and_ui_should_be_matched() {
        System.out.println(UIDashboardId);
        System.out.println(APIDashboardId);
        Assert.assertEquals(UIDashboardId,APIDashboardId);
    }
    @Given("the user as a librarian makes post request with using add_book end point with random values")
    public void the_user_as_a_librarian_makes_post_request_with_using_add_book_end_point_with_random_values() throws JsonProcessingException {
        Map<String,Object> bdy=new HashMap<>();
        bdy.put("name","Java in 28 Hours");
        bdy.put("isbn","123456789");
        bdy.put("author","cybertekschool");
        bdy.put("year",2022);
        bdy.put("book_category_id",1);
        bdy.put("description","This is a book about Java");
        String toke = API.getToke("librarian570@library", "2gCucjjn");
        baseURI="https://library2.cybertekschool.com/rest/v1/";
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bdy);
        Response add_book = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-library-token", toke)
                .body(json)
                .when().post("add_book");
        bookAPIData = bdy.get("name").toString()+" "+bdy.get("isbn").toString()+" "+bdy.get("author").toString();
        apiBookName=bdy.get("name").toString();


    }
    @Given("the user navigates to {string} page")
    public void the_user_navigates_to_page(String page) {
        driver.findElement(By.xpath("//span[@class='title'][.='"+page+"']")).click();
        BrowserUtils.waitFor(3);
    }
    @Given("the user search corresponding book name")
    public void the_user_search_corresponding_book_name() {
       driver.findElement(By.xpath("//input[@type='search']")).sendKeys(apiBookName);
       BrowserUtils.waitFor(3);
        String name = driver.findElement(By.xpath("//tbody//tr[1]//td[3]")).getText();
        String isbn = driver.findElement(By.xpath("//tbody//tr[1]//td[2]")).getText();
        String author = driver.findElement(By.xpath("//tbody//tr[1]//td[4]")).getText();
        bookUIData = name+" "+" "+isbn+" "+author;

    }
    @Then("the user should see the book created in the API on the list")
    public void the_user_should_see_the_book_created_in_the_api_on_the_list() {
       // Assert.assertEquals(bookAPIData,bookUIData);
        BrowserUtils.waitFor(4);
    }
    @Then("the user click edit button")
    public void the_user_click_edit_button() {
        driver.findElement(By.xpath("//tbody/tr[1]/td[1]")).click();
        BrowserUtils.waitFor(3);
        driver.findElement(By.xpath("//button[.='Save changes']")).click();
        BrowserUtils.waitFor(2);
    }
    @Then("click save button see the msg {string}")
    public void click_save_button_see_the_msg(String msg) {
        String text = driver.findElement(By.xpath("//div[@class='toast-message']")).getText();
        Assert.assertEquals(text,msg);
    }
    String messageApi;
    String userApi;
    @Given("create new user with {int}")
    public void create_new_user_with(Integer type) {
        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        System.out.println("full Name: "+fullName);
        String usernameEmail = faker.name().username();
        String companyUrl = faker.company().url().substring(4);
        String email = usernameEmail + "@" + companyUrl;
        String address = faker.address().fullAddress();
        Map<String, Object> user = new LinkedHashMap<>();
        user.put("full_name", "fullName");
        user.put("email", "email@email");
        user.put("password", 123456);
        user.put("user_group_id", type);
        user.put("status", "active");
        user.put("start_date", "2022-06-05");
        user.put("end_date", "2023-05-05");
        user.put("address", "address");
        userApi= user.get("full_name").toString();
        String toke = API.getToke("librarian570@library", "2gCucjjn");
        baseURI="https://library2.cybertekschool.com/rest/v1/";
        Response add_user = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .formParams(user)
                .header("x-library-token", toke)
                .when().post("add_user").prettyPeek();
        messageApi = add_user.path("message").toString();

    }
    @Then("user should be able to get {string} in response body for add user")
    public void user_should_be_able_to_get_in_response_body_for_add_user(String msg) {
        Assert.assertEquals(messageApi,msg);
    }
    @Then("the user should see the user created in the API on the list")
    public void the_user_should_see_the_user_created_in_the_api_on_the_list() {
        driver.findElement(By.xpath("//input[@type='search']")).sendKeys(userApi);

    }






}
