package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utilities.Driver;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import pages.BookItPages;
import utilities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData metaData;
    @Given("Establish the database connection")
    public void establish_the_database_connection() throws SQLException {
         connection= DriverManager.getConnection("jdbc:mysql://34.230.35.214:3306/library2","library2_client","6s2LQQTjBcGFfDhY");
        statement= connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

    }
    List<Map<String,Object>> ls;
    @When("Execute query to get all columns")
    public void execute_query_to_get_all_columns() throws SQLException {
        resultSet= statement.executeQuery("select * from users");
        metaData = resultSet.getMetaData();
       ls=new ArrayList<>();
        while(resultSet.next()){
            Map<String,Object> map=new HashMap<>();
            for(int i=1;i<=metaData.getColumnCount();i++){
                map.put(metaData.getColumnName(i),resultSet.getObject(i));
            }
            ls.add(map);
        }

    }
    @Then("verify the below columns are listed in result")
    public void verify_the_below_columns_are_listed_in_result(List<String> table) {

        for (Map<String, Object> map : ls) {
            for (String s : table) {
                assertTrue(map.containsKey(s));
            }
        }
    }

    @Given("user on the login page of the library")
    public void user_on_the_login_page_of_the_library() {
       Driver.get().get("https://library2.cydeo.com/login.html");
    }
    @When("user enters {string} and {string}")
    public void user_enters_and(String user, String pass) {
       Driver.get().findElement(By.id("inputEmail")).sendKeys(user);
         Driver.get().findElement(By.id("inputPassword")).sendKeys(pass);

    }
    @When("user click {string} button")
    public void user_click_button(String btn) {
        Driver.get().findElement(By.tagName("button")).click();
    }
    @Then("Verify user see the {string} page")
    public void verify_user_see_the_page(String title) {
        BrowserUtils.waitFor(2);
        String actualTitle = Driver.get().getTitle();
        assertEquals(title,actualTitle);
    }
    @Given("user log in as a librarian")
    public void user_log_in_as_a_librarian() {
      Driver.get().get("https://library2.cydeo.com/login.html");
        Driver.get().findElement(By.id("inputEmail")).sendKeys("librarian1@library");
        Driver.get().findElement(By.id("inputPassword")).sendKeys("qU9mrvur");
        Driver.get().findElement(By.tagName("button")).click();
        BrowserUtils.waitFor(10);
    }
    int borrowedUI=0;
    @When("user take borrowed books number")
    public void user_take_borrowed_books_number() {

        WebElement element = Driver.get().findElement(By.xpath("(//h2)[3]"));
        borrowedUI = Integer.parseInt(element.getText());
        System.out.println("borrowedUI = " + borrowedUI);



    }
    @Then("borrowed books number information must match with DB")
    public void borrowed_books_number_information_must_match_with_db() throws SQLException {
        connection= DriverManager.getConnection("jdbc:mysql://34.230.35.214:3306/library2","library2_client","6s2LQQTjBcGFfDhY");
        statement= connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        resultSet= statement.executeQuery("select * from book_borrow where  returned_date is null");

        ResultSetMetaData metaData1 = resultSet.getMetaData();
        List<Map<String,Object>> ls=new ArrayList<>();
        while(resultSet.next()){
            Map<String,Object> map=new HashMap<>();
            for(int i=1;i<=metaData1.getColumnCount();i++){
                map.put(metaData1.getColumnName(i),resultSet.getObject(i));
            }
            ls.add(map);
        }
        System.out.println(ls.size());
        int size = ls.size();
        Assert.assertEquals(size,borrowedUI);
    }
    @When("user goes to {string} page")
    public void user_goes_to_page(String page) {
      Driver.get().findElement(By.xpath("//span[.='"+page+"']")).click();

    }
    @When("user selects {string} records from dropdown")
    public void user_selects_records_from_dropdown(String dd) {
        WebElement element = Driver.get().findElement(By.xpath("(//select)[2]"));
        Select ss=new Select(element);
        ss.selectByVisibleText(dd);
        BrowserUtils.waitFor(8);


    }
    String genreUI;
    @When("user gets most popular book genre")
    public void user_gets_most_popular_book_genre() {

        List<WebElement> elements = Driver.get().findElements(By.xpath("//tbody//td[5]"));

        elements.stream().map(WebElement::getText).collect(Collectors.toList()).stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).ifPresent(e->genreUI=e.getKey());
        System.out.println("genreUI = " + genreUI);
    }
String genreDB;
    @When("execute a query to find the most popular book genre from DB")
    public void execute_a_query_to_find_the_most_popular_book_genre_from_db() throws SQLException {
        connection= DriverManager.getConnection("jdbc:mysql://34.230.35.214:3306/library2","library2_client","6s2LQQTjBcGFfDhY");
        statement= connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        resultSet= statement.executeQuery("select name from book_categories where id =(select (count(*)) from books b join\n" +
                "    book_borrow bb on b.id = bb.book_id where bb.is_returned = 0 group by b.book_category_id order by b.book_category_id desc limit 1);\n");
        ResultSetMetaData metaData1 = resultSet.getMetaData();
        List<Map<String,Object>> ls=new ArrayList<>();
        while(resultSet.next()){
            Map<String,Object> map=new HashMap<>();
            for(int i=1;i<=metaData1.getColumnCount();i++){
                map.put(metaData1.getColumnName(i),resultSet.getObject(i));
            }
            ls.add(map);
        }
        genreDB=ls.get(0).get("name").toString();

    }
    @Then("verify that most popular genre from UI is matching to DB")
    public void verify_that_most_popular_genre_from_ui_is_matching_to_db() throws JsonProcessingException {
        baseURI="https://library2.cydeo.com/rest/v1";
        Map<String,Object> map=new HashMap<>();
        map.put("email","librarian24@library");
        map.put("password","8v8ZByKA");
        ObjectMapper objectMapper=new ObjectMapper();
        String json = objectMapper.writeValueAsString(map);
        Response response = given().contentType(ContentType.JSON).body(json).when().post("/login");
        String token = response.jsonPath().getString("token");
        System.out.println("token = " + token);
        Response response1 = given().header("x-library-token", token).when().get("/dashboard_stats");
        JsonPath jsonPath = response1.jsonPath();
        String book_count = jsonPath.getString("book_count");
        System.out.println("book_count = " + book_count);



        Assert.assertEquals(genreDB,genreUI);
    }





}
