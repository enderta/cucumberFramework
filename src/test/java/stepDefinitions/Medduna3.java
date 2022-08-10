package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utilities.BrowserUtils;
import utilities.Driver;

import java.util.HashMap;
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
    String text = Driver.get().findElement(By.xpath("//div[@role='alert']")).getText();

    Assert.assertTrue(text.contains("A new user is created"));
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



}
