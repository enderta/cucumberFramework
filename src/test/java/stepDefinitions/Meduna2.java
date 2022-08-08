/*
package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.support.ui.Select;
import utilities.BrowserUtils;
import utilities.Driver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Meduna2 {
    WebDriver driver = Driver.get();

    @Given("user is on the login page and singup page")
    public void user_is_on_the_login_page_and_singup_page() {
        driver.get("https://medunna.com/");
        driver.findElement(By.id("account-menu")).click();
        driver.findElement(By.id("login-item")).click();

    }

    @Given("user sends username {string} and password {string}")
    public void user_sends_username_and_password(String user, String pass) {
        driver.findElement(By.id("username")).sendKeys(user);
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        BrowserUtils.waitFor(4);
    }

    @Then("verify the my page and logout")
    public void verify_the_my_page_and_logout() {
        driver.findElement(By.id("account-menu")).click();
        List<WebElement> elements = driver.findElements(By.xpath("//li[@id='account-menu']//a"));
        boolean my_account = elements.stream().anyMatch(element -> element.getAttribute("href").contains("/logout"));
        Assert.assertTrue(my_account);
    }

    String tkn = "";

    @Given("user is on the login to api")
    public void user_is_on_the_login_to_api() throws JsonProcessingException {
        baseURI = "https://medunna.com/api/";
        Map<String, Object> bdy = new HashMap<>();
        bdy.put("username", "adminaccount");
        bdy.put("password", "12345");
        bdy.put("rememberMe", "true");
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(bdy);
        Response post = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(s).when().
                post("/authenticate");
        tkn = post.jsonPath().getString("id_token");
    }

    String nameApi, lastNameApi, fullNameApi;

    @Then("get all user and look for {int} user")
    public void get_all_user_and_look_for_user(Integer int1) throws JsonProcessingException {
        baseURI = "https://medunna.com/api/";
        Response response = given().contentType(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("id", int1).
                when().
                get("/staff/{id}");
        JsonPath jsonPath = response.jsonPath();
        nameApi = jsonPath.getString("firstName");
        lastNameApi = jsonPath.getString("lastName");
        fullNameApi = nameApi + " " + lastNameApi;


    }

    String fullNamaDB = "";

    @Then("Check {int} on db")
    public void check_on_db(Integer int1) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://medunna.com:5432/medunna_db", "medunnadb_user", "Medunnadb_@129");
        Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from staff where id=" + int1);
        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<String, Object> map = new HashMap<>();
        while (resultSet.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
        }
        fullNamaDB = map.get("first_name") + " " + map.get("last_name");
        System.out.println(fullNamaDB);
        System.out.println(fullNameApi);


    }

    @Then("both information should be same")
    public void both_information_should_be_same() {
        Assert.assertEquals(fullNamaDB, fullNameApi);

    }

    @Then("user click administration and click on user management")
    public void user_click_administration_and_click_on_user_management() {
        driver.findElement(By.id("admin-menu")).click();
        driver.findElement(By.xpath("(//li[@id='admin-menu']//a)[2]")).click();
    }

    @Then("user click on add user")
    public void user_click_on_add_user() {
        driver.findElement(By.xpath("//span[.='Create a new user']")).click();
    }
String nameUI="";
    @When("enter {string} {string} {string} {string} {string}")
    public void enter(String login, String firstName, String lastName, String email, String ssn) {
        driver.findElement(By.id("login")).sendKeys(login);
        driver.findElement(By.id("firstName")).sendKeys(firstName);
        driver.findElement(By.id("lastName")).sendKeys(lastName);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("ssn")).sendKeys(ssn);
       // driver.findElement(By.id("activated")).click();
        WebElement langKey = driver.findElement(By.id("langKey"));
        WebElement authorities = Driver.get().findElement(By.id("authorities"));
        Select select = new Select(authorities);
        select.selectByValue("ROLE_USER");
        BrowserUtils.waitFor(4);
        nameUI = login;

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
    public void user_sees_this_user_in_api_and_db() throws JsonProcessingException {
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
                when().post("https://medunna.com/api/authenticate");
        tkn = response.jsonPath().getString("id_token");
        Response response1 = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
               pathParam("login",nameUI).

                when().get("https://medunna.com/api/users/{login}");
        JsonPath jsonPath = response1.jsonPath();
        String firstName = jsonPath.getString("firstName");



        System.out.println("firstNameAPi= "+firstName);
        Map<Object, Object> map = jsonPath.getMap("");
        System.out.println("map= "+map);
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
                when().post("https://medunna.com/api/authenticate");
        tkn = response.jsonPath().getString("id_token");
    }
    String log="";
    @When("user use delet method by usin {string}")
    public void user_use_delet_method_by_usin(String log) {
      given().contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("login",log).
                when().delete("https://medunna.com/api/users/{login}");
      this.log=log;

    }

    @Then("user use get mehtod and not find the user")
    public void user_use_get_mehtod_and_not_find_the_user() {
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("login", log).

                when().get("https://medunna.com/api/users/{login}");
        String title = response.jsonPath().getString("title");
        Assert.assertEquals(404, response.getStatusCode());
        Assert.assertEquals(title, "Not Found");

    }

    @Then("user click administration and click on room management")
    public void user_click_administration_and_click_on_room_management() {
        WebElement element = driver.findElement(By.xpath("//li[@id='entity-menu']//a//*[.='Room']"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", element);
    }
    @Then("user click on add room")
    public void user_click_on_add_room() {
       driver.findElement(By.xpath("//*[.='Create a new Room']")).click();
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
        String text = driver.findElement(By.xpath("//div[@role='alert']")).getText();
      //  Assert.assertTrue(text.contains("A new room is created"));
        System.out.println(text);
    }
    @Then("user sees this room in api and db")
    public void user_sees_this_room_in_api_and_db() throws JsonProcessingException, SQLException {
        driver.findElement(By.xpath("//thead//tr//th[7]")).click();
        BrowserUtils.waitFor(4);
        String roomId = Driver.get().findElement(By.xpath("//tbody//tr[1]//td[1]")).getText();
        String roomNum = Driver.get().findElement(By.xpath("//tbody//tr[1]//td[2]")).getText();
        Map<String,Object> bdy = new HashMap<>();
        bdy.put("username", "admin79");
        bdy.put("password", "admin");
        bdy.put("rememberMe", "true");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bdy);
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(json).
                when().post("https://medunna.com/api/authenticate");
        tkn = response.jsonPath().getString("id_token");
        Response response1 = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("id",roomId).
                when().get("https://medunna.com/api/rooms/{id}");
        JsonPath jsonPath = response1.jsonPath();
        String roomNumber = jsonPath.getString("roomNumber");
        Assert.assertEquals(roomNumber, roomNum);

        Connection conn = DriverManager.getConnection("jdbc:postgresql://medunna.com:5432/medunna_db", "medunnadb_user", "Medunnadb_@129");
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery("select * from room where id= " + Integer.valueOf(roomId));
        ResultSetMetaData metaData = rs.getMetaData();
        Map<String, Object> map = new HashMap<>();
        while (rs.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                String value = rs.getString(i);
                map.put(columnName, value);
            }
        }

        Assert.assertEquals(map.get("room_number"), roomNum);



        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("id", roomId).
                when().delete("https://medunna.com/api/rooms/{id}").prettyPeek();

    }



}
*/
