package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utilities.BrowserUtils;
import utilities.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Medduna3 {

  @Given("user is on the login page and singup page")
  public void user_is_on_the_login_page_and_singup_page() {
    Driver.get().get("https://medunna.com/");
    Driver.get().findElement(By.xpath("//li[@id='account-menu']")).click();
    Driver.get().findElement(By.id("login-item")).click();
    BrowserUtils.waitFor(4);
  }
  @Given("user sends username {string} and password {string}")
  public void user_sends_username_and_password(String user, String pass) {
  Driver.get().findElement(By.name("username")).sendKeys(user);
    Driver.get().findElement(By.name("password")).sendKeys(pass);
    BrowserUtils.waitFor(4);
    Driver.get().findElement(By.xpath("//button[@type='submit']")).click();
    BrowserUtils.waitFor(4);
  }
  @Then("verify the my page and logout")
  public void verify_the_my_page_and_logout() {
  Driver.get().findElement(By.xpath("//span[.='admin account']")).click();
  Driver.get().findElement(By.xpath("//a[@href='/logout']")).click();
    String text = Driver.get().findElement(By.xpath("//h2")).getText();
    Assert.assertEquals("THANK YOU FOR CHOOSING US...", text);
  }

  @Then("user click administration and click on user management")
  public void user_click_administration_and_click_on_user_management() {
    Driver.get().findElement(By.xpath("//span[.='Administration']")).click();
    Driver.get().findElement(By.xpath("//span[.='User management']")).click();
    BrowserUtils.waitFor(4);
  }
  @Then("user click on add user")
  public void user_click_on_add_user() {
   Driver.get().findElement(By.xpath("//span[.='Create a new user']")).click();

  }
  String login="";
  String firstNameUI="";
  @When("enter {string} {string} {string} {string} {string}")
  public void enter(String login, String fn, String ln, String email, String ssn) {
    this.login=login;
    this.firstNameUI=fn;
    Driver.get().findElement(By.id("login")).sendKeys(login);
    Driver.get().findElement(By.id("firstName")).sendKeys(fn);
    Driver.get().findElement(By.id("lastName")).sendKeys(ln);
    Driver.get().findElement(By.id("email")).sendKeys(email);
    Driver.get().findElement(By.id("ssn")).sendKeys(ssn);
    WebElement langKey = Driver.get().findElement(By.id("langKey"));
    Select select = new Select(langKey);
select.selectByVisibleText("English");
    WebElement authorities = Driver.get().findElement(By.id("authorities"));
    Select select1 = new Select(authorities);
    select1.selectByVisibleText("ROLE_USER");


  }
  @Then("user click on save")
  public void user_click_on_save() {
    Driver.get().findElement(By.xpath("//button[@type='submit']")).click();
    BrowserUtils.waitFor(4);

  }
  @Then("user should see the user created successfully")
  public void user_should_see_the_user_created_successfully() {
   // String text = Driver.get().findElement(By.xpath("//div[@role='alert']")).getText();

    //Assert.assertTrue(text.contains("created"));
    BrowserUtils.waitFor(4);
  }
  @Then("user sees this user in api and db")
  public void user_sees_this_user_in_api_and_db() throws JsonProcessingException {
    Map<String, Object> bdy1 = new HashMap<>();
    bdy1.put("username", "admin79");
    bdy1.put("password", "admin");
    bdy1.put("rememberMe", "true");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(bdy1);
    Response response = given().
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
            body(json).
            when().post("https://medunna.com/api/authenticate").prettyPeek();
    String tkn = response.jsonPath().getString("id_token");
    Response user = given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
            when().get("https://medunna.com/api/users/"+login ).prettyPeek()   ;
   user.jsonPath().getString("firstName");
    Assert.assertEquals(firstNameUI, user.jsonPath().getString("firstName"));


  }
  String tkn="";

  @Given("user is on APi")
  public void user_is_on_a_pi() throws JsonProcessingException {
    Map<String, Object> bdy1 = new HashMap<>();
    bdy1.put("username", "admin79");
    bdy1.put("password", "admin");
    bdy1.put("rememberMe", "true");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(bdy1);
    Response response = given().
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
            body(json).
            when().post("https://medunna.com/api/authenticate").prettyPeek();
     tkn = response.jsonPath().getString("id_token");
  }

  @When("user use delet method by usin {string}")
  public void user_use_delet_method_by_usin(String log) {
    this.login=log;
   given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
           pathParam("login", log).
            when().delete("https://medunna.com/api/users/{login}").prettyPeek();

  }
  @Then("user use get mehtod and not find the user")
  public void user_use_get_mehtod_and_not_find_the_user() {
   given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
         pathParam("login", login).
            when().get("https://medunna.com/api/users/{login}").then().statusCode(404);

  }
  int countryId=0;
  @Given("user create country in API")
  public void user_create_country_in_api() throws JsonProcessingException {

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

      Map<String,Object> bdy1=new HashMap<>();
      bdy1.put("name","test234");
      ObjectMapper mapper1 = new ObjectMapper();
      String s = mapper1.writeValueAsString(bdy1);
      Response posCountry = given().
              contentType(ContentType.JSON).
              accept(ContentType.JSON).
              body(s).
              header("Authorization", "Bearer " + tkn).
              when().post("https://medunna.com/api/countries").prettyPeek();
    countryId=posCountry.jsonPath().getInt("id");


  }
  @Then("user click administration and click on country management")
  public void user_click_administration_and_click_on_country_management() {
    Driver.get().findElement(By.xpath("//span[.='Items&Titles']")).click();
    Driver.get().findElement(By.xpath("//span[.='Country']")).click();
    List<WebElement> elements = Driver.get().findElements(By.xpath("//tbody//tr//td[1]"));
    BrowserUtils.waitFor(4);
    for (WebElement element : elements) {
     if( Integer.parseInt(element.getText())== countryId){
       Assert.assertTrue(true);
       break;
     }

    }

  }
  @Then("the user should see the country created in the API on the list")
  public void the_user_should_see_the_country_created_in_the_api_on_the_list() {
    Response delCountry = given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
           pathParams("id", countryId).
            when().delete("https://medunna.com/api/countries/{id}").prettyPeek();
    Assert.assertEquals(delCountry.statusCode(), 204);
  }

  @Then("user click administration and click on room management")
  public void user_click_administration_and_click_on_room_management() {

  }
  @Then("user click on add room")
  public void user_click_on_add_room() {
    Driver.get().findElement(By.xpath("//span[.='Items&Titles']")).click();
    Driver.get().findElement(By.xpath("//span[.='Room']")).click();
    Driver.get().findElement(By.xpath("//span[.='Create a new Room']")).click();
    BrowserUtils.waitFor(4);

  }
  @When("enter {string} {string} {string} {string}")
  public void enter(String number, String type, String price, String desc) {
    Driver.get().findElement(By.xpath("//input[@name='roomNumber']")).sendKeys(number);
    WebElement element = Driver.get().findElement(By.id("room-roomType"));
    Select select = new Select(element);
    select.selectByVisibleText(type);
    Driver.get().findElement(By.xpath("//input[@name='price']")).sendKeys(price);
    Driver.get().findElement(By.xpath("//input[@name='description']")).sendKeys(desc);
    Driver.get().findElement(By.xpath("//span[.='Save']")).click();
    BrowserUtils.waitFor(4);

  }
  @Then("user should see the room created successfully")
  public void user_should_see_the_room_created_successfully() {
    String text = Driver.get().findElement(By.xpath("//div[@role='alert']")).getText();
    Assert.assertEquals(text, "A new room is created");

  }
  @Then("user sees this room in api and db")
  public void user_sees_this_room_in_api_and_db() throws JsonProcessingException, SQLException {
    Driver.get().findElement(By.xpath("//th[7]")).click();
    BrowserUtils.waitFor(4);
    String roomID = Driver.get().findElement(By.xpath("//tbody//tr[1]//td[1]")).getText();
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
            pathParam("id",roomID).
            when().get("https://medunna.com/api/rooms/{id}");
    JsonPath jsonPath = response1.jsonPath();
    String roomNumber = jsonPath.getString("roomNumber");
    Assert.assertEquals(roomNumber, roomNum);
    /*Connection con = DriverManager.getConnection("jdbc:postgresql://medunna.com:5432/medunna_db", "medunnadb_user", "Medunnadb_@129");
    Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    ResultSet rs = statement.executeQuery("select * from room where id= " + Integer.valueOf(roomID));
    ResultSetMetaData metaData = rs.getMetaData();
    List<Map<String,Object>> ls=new ArrayList<>();
    while (rs.next()) {
      Map<String,Object> map=new HashMap<>();
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        map.put(metaData.getColumnName(i), rs.getObject(i));
      }
      ls.add(map);
    }
    Map<String,Object> map=ls.get(0);
    String roomNumber1 = map.get("roomNumber").toString();
    Assert.assertEquals(roomNumber1, roomNum);
*/
    given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
            pathParams("id", roomID).
            when().delete("https://medunna.com/api/rooms/{id}").then().statusCode(204);


  }





}
