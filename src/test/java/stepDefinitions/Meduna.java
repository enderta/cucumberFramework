/*
package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BookItPages;
import utilities.*;

import java.sql.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import utilities.BrowserUtils;
import utilities.Driver;

import static io.restassured.RestAssured.given;

public class Meduna {
    @Given("user is on the login page and singup page")
    public void user_is_on_the_login_page_and_singup_page() {
        Driver.get().get("https://medunna.com/");
        Driver.get().findElement(By.id("account-menu")).click();
        Driver.get().findElement(By.id("login-item")).click();
        BrowserUtils.waitFor(4);
    }

    @Given("user sends username {string} and password {string}")
    public void user_sends_username_and_password(String user, String pass) {
        Driver.get().findElement(By.id("username")).sendKeys(user);
        Driver.get().findElement(By.id("password")).sendKeys(pass);
        Driver.get().findElement(By.xpath("//button[@class='btn btn-primary']")).click();

        BrowserUtils.waitFor(4);

    }

    @Then("verify the my page and logout")
    public void verify_the_my_page_and_logout() {
        String text = Driver.get().findElement(By.xpath("(//li[@id='account-menu']//a//span)[1]")).getText();
        Assert.assertEquals("admin account", text);
        Driver.get().findElement(By.xpath("(//li[@id='account-menu']//a//span)[1]")).click();
        Driver.get().findElement(By.xpath("(//li[@id='account-menu']//a//span)[4]")).click();
        BrowserUtils.waitFor(4);
        String text1 = Driver.get().findElement(By.xpath("//h2")).getText();
        Assert.assertEquals("THANK YOU FOR CHOOSING US...", text1);

    }

    String tkn;

    @Given("user is on the login to api")
    public void user_is_on_the_login_to_api() throws JsonProcessingException {
        Map<String, Object> bdy = new HashMap<>();
        bdy.put("username", "admin79");
        bdy.put("password", "admin");
        bdy.put("rememberMe", "true");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bdy);
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(json).
                    when().post("https://medunna.com/api/authenticate").prettyPeek();
        tkn = response.jsonPath().getString("id_token");
    }

    String nameApi, lastNameApi, fullNameApi;

    @Then("get all user and look for {int} user")
    public void get_all_user_and_look_for_user(Integer id) {
        Response response = given().contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("id", id).


                when().get("https://medunna.com/api/staff/{id}").prettyPeek();
        nameApi = response.jsonPath().getString("firstName");
        lastNameApi = response.jsonPath().getString("lastName");
        fullNameApi = nameApi + " " + lastNameApi;


    }

    String fullNamaDB;

    @Then("Check {int} on db")
    public void check_on_db(Integer int1) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://medunna.com:5432/medunna_db", "medunnadb_user", "Medunnadb_@129");
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery("select * from staff where id=" + int1);
        ResultSetMetaData metaData = rs.getMetaData();
        Map<String, Object> map = new HashMap<>();
        while (rs.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i), rs.getObject(i));
            }
        }
        fullNamaDB = map.get("first_name") + " " + map.get("last_name");

    }

    @Then("both information should be same")
    public void both_information_should_be_same() {

        Assert.assertEquals(fullNameApi, fullNamaDB);
    }

    //(//li[@class='page-item'])[5]//a
    @Then("user click administration and click on user management")
    public void user_click_administration_and_click_on_user_management() {
        Driver.get().findElement(By.xpath("//span[.='Administration']")).click();
        Driver.get().findElement(By.xpath("//span[.='User management']")).click();
        BrowserUtils.waitFor(4);
    }

    @Then("user click on add user")
    public void user_click_on_add_user() {
        Driver.get().findElement(By.xpath("//span[.='Create a new user']")).click();
        BrowserUtils.waitFor(4);
    }

    String nameUI;

    @When("enter {string} {string} {string} {string} {string}")
    public void enter(String log, String firstname, String lastname, String email, String ssn) {
        Driver.get().findElement(By.xpath("//input[@name='login']")).sendKeys(log);
        Driver.get().findElement(By.xpath("//input[@name='firstName']")).sendKeys(firstname);
        Driver.get().findElement(By.xpath("//input[@name='lastName']")).sendKeys(lastname);
        Driver.get().findElement(By.xpath("//input[@name='email']")).sendKeys(email);
        Driver.get().findElement(By.xpath("//input[@name='ssn']")).sendKeys(ssn);
        WebElement authorities = Driver.get().findElement(By.id("authorities"));
        Select select = new Select(authorities);
        select.selectByValue("ROLE_USER");
        BrowserUtils.waitFor(4);
        nameUI = log;
    }

    @Then("user click on save")
    public void user_click_on_save() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.get();
        WebElement element = Driver.get().findElement(By.xpath("(//span[.='Save']//..)[1]"));
        js.executeScript("arguments[0].click();", element);

        BrowserUtils.waitFor(4);
    }

    @Then("user should see the user created successfully")
    public void user_should_see_the_user_created_successfully() {


        String text = Driver.get().findElement(By.xpath("//div[@role='alert']")).getText();

        Assert.assertTrue(text.contains("A new user is created"));
        BrowserUtils.waitFor(4);
    }

    @Then("user sees this user in api and db")
    public void user_sees_this_user_in_api_and_db() throws JsonProcessingException, SQLException {
            Driver.get().findElement(By.xpath("//span[.='Created date']")).click();
            BrowserUtils.waitFor(4);
            String id = Driver.get().findElement(By.xpath("//tbody//tr//td[2][.='" + nameUI + "']//..//td[1]")).getText();
            Map<String, Object> bdy = new HashMap<>();
            bdy.put("username", "admin79");
            bdy.put("password", "admin");
            bdy.put("rememberMe", "true");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(bdy);
            Response response = given().
                    contentType(ContentType.JSON).
                    accept(ContentType.JSON).
                    body(json).
                    when().post("https://medunna.com/api/authenticate").prettyPeek();
            tkn = response.jsonPath().getString("id_token");
            Response response1 = given().
                    contentType(ContentType.JSON).
                    accept(ContentType.JSON).
                    header("Authorization", "Bearer " + tkn).
                    queryParams("page", 281).

                    when().get("https://medunna.com/api/users/").prettyPeek();
            JsonPath jsonPath = response1.jsonPath();
            List<Map<String, Object>> list = jsonPath.getList("");


            String firstNameApi = list.stream().filter(x -> (Integer.parseInt(x.get("id").toString()) == Integer.parseInt(id))).map(x -> x.get("firstName").toString()).collect(Collectors.joining());
        Connection conn = DriverManager.getConnection("jdbc:postgresql://medunna.com:5432/medunna_db", "medunnadb_user", "Medunnadb_@129");
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery("select * from jhi_user where id= " + Integer.valueOf(id));
        ResultSetMetaData metaData = rs.getMetaData();
        Map<String, Object> map = new HashMap<>();
        while (rs.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i), rs.getObject(i));
            }
        }
        String first_namedb = map.get("first_name").toString();
        Assert.assertEquals(firstNameApi, first_namedb);
    }

    @Given("user is on APi")
    public void user_is_on_a_pi() throws JsonProcessingException {
        Map<String, Object> bdy = new HashMap<>();
        bdy.put("username", "admin79");
        bdy.put("password", "admin");
        bdy.put("rememberMe", "true");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bdy);
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(json).
                when().post("https://medunna.com/api/authenticate").prettyPeek();
        tkn = response.jsonPath().getString("id_token");
    }

    String log;

    @When("user use delet method by usin {string}")
    public void user_use_delet_method_by_usin(String log) {
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("login", log).

                when().delete("https://medunna.com/api/users/{login}").prettyPeek();
        this.log = log;

    }

    @Then("user use get mehtod and not find the user")
    public void user_use_get_mehtod_and_not_find_the_user() {
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("login", log).

                when().get("https://medunna.com/api/users/{login}").prettyPeek();
        String title = response.jsonPath().getString("title");
        Assert.assertEquals(404, response.getStatusCode());
        Assert.assertEquals(title, "Not Found");

    }

    @Then("user click administration and click on room management")
    public void user_click_administration_and_click_on_room_management() {
        Driver.get().findElement(By.id("entity-menu")).click();
        Driver.get().findElement(By.xpath("//li//a//span[.='Room']")).click();
    }

    @Then("user click on add room")
    public void user_click_on_add_room() {
        Driver.get().findElement(By.id("jh-create-entity")).click();

    }

    @When("enter {string} {string} {string} {string}")
    public void enter(String number, String type, String price, String description) {
        Driver.get().findElement(By.id("room-roomNumber")).sendKeys(number);
        WebElement roomTypeLabel = Driver.get().findElement(By.id("room-roomType"));
        Select select = new Select(roomTypeLabel);
        select.selectByValue(type);
        Driver.get().findElement(By.id("room-status")).click();
        Driver.get().findElement(By.id("room-price")).sendKeys(price);
        Driver.get().findElement(By.id("room-description")).sendKeys(description);

    }

    @Then("user should see the room created successfully")
    public void user_should_see_the_room_created_successfully() {
        String text = Driver.get().findElement(By.xpath("//div[@role='alert']")).getText();
        Assert.assertTrue(text.contains("A new Room is created"));

    }

    @Then("user sees this room in api and db")
    public void user_sees_this_room_in_api_and_db() throws JsonProcessingException {
        Driver.get().findElement(By.xpath("//thead//tr//th[7]")).click();
        BrowserUtils.waitFor(4);
        String roomId = Driver.get().findElement(By.xpath("//tbody//tr[1]//td[1]")).getText();
        String roomNum = Driver.get().findElement(By.xpath("//tbody//tr[1]//td[2]")).getText();

        Map<String, Object> bdy = new HashMap<>();
        bdy.put("username", "admin79");
        bdy.put("password", "admin");
        bdy.put("rememberMe", "true");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bdy);
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(json).
                when().post("https://medunna.com/api/authenticate").prettyPeek();
        tkn = response.jsonPath().getString("id_token");
        Response response1 = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("id", roomId).
                when().get("https://medunna.com/api/rooms/{id}").prettyPeek();
        String roomNumber = response1.jsonPath().getString("roomNumber");
        Assert.assertEquals(roomNum, roomNumber);
        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("id", roomId).
                when().delete("https://medunna.com/api/rooms/{id}").prettyPeek();



    }
    int id=0;
    String name="";
    @Given("user create country in API")
    public void user_create_country_in_api() throws JsonProcessingException, InterruptedException {
        Map<String, Object> bdy = new HashMap<>();
        bdy.put("username", "admin79");
        bdy.put("password", "admin");
        bdy.put("rememberMe", "true");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bdy);
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(json).
                when().post("https://medunna.com/api/authenticate").prettyPeek();
        tkn = response.jsonPath().getString("id_token");
         name="test";
         Map<String,Object> bdy1=new HashMap<>();
            bdy1.put("name",name);
         ObjectMapper mapper1 = new ObjectMapper();
        String s = mapper1.writeValueAsString(bdy);
        Response response1 = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(s).
                header("Authorization", "Bearer " + tkn).
                when().post("https://medunna.com/api/countries").prettyPeek();
        id=response1.jsonPath().getInt("id");
        Thread.sleep(5000);

    }
    @Then("user click administration and click on country management")
    public void user_click_administration_and_click_on_country_management() {
        Driver.get().findElement(By.id("entity-menu")).click();
        Driver.get().findElement(By.xpath("//li//a//span[.='Country']")).click();
    }
    @Then("the user should see the country created in the API on the list")
    public void the_user_should_see_the_country_created_in_the_api_on_the_list() {
        List<WebElement> elements = Driver.get().findElements(By.xpath("//tbody//tr//td[1]"));
        String country_not_found = elements.stream().map(WebElement::getText).filter(s -> s.equals(id + "")).findFirst().orElseThrow(() -> new RuntimeException("Country not found"));
        Assert.assertEquals(country_not_found, id + "");
        Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON).header("Authorization", "Bearer " + tkn).
                pathParam("id", id).when().
                delete("https://medunna.com/api/countries/{id}").prettyPeek();
        Assert.assertEquals(response.getStatusCode(), 204);
        Response response1 = given().contentType(ContentType.JSON).accept(ContentType.JSON).header("Authorization", "Bearer " + tkn).
                pathParam("id", id).when().
                get("https://medunna.com/api/countries/{id}").prettyPeek();
        Assert.assertEquals(response1.getStatusCode(), 404);
    }



}
*/
