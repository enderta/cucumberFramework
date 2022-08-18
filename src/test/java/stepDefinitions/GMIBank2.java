package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import pages.GMIPages;
import utilities.BrowserUtils;
import utilities.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;


public class GMIBank2 {

  @Given("User is on the GMIBank login page")
  public void user_is_on_the_gmi_bank_login_page() {
    Driver.get().get("https://www.gmibank.com/");
    Driver.get().findElement(By.xpath("(//*[@class='dropdown-toggle nav-link'])[2]")).click();
    Driver.get().findElement(By.id("login-item")).click();


  }
  @When("User enters valid {string} and {string}")
  public void user_enters_valid_and(String user, String pass) {
    Driver.get().findElement(By.xpath("//input[@id='username']")).sendKeys(user);
    Driver.get().findElement(By.xpath("//input[@id='password']")).sendKeys(pass);
    Driver.get().findElement(By.xpath("//button[@type='submit']")).click();
    BrowserUtils.waitFor(4);
    String text = Driver.get().findElement(By.xpath("//span[.='Joe King']")).getText();
    Assert.assertEquals("Joe King", text);

  }
  String tkn= "";
  @Given("user is logging to the GMI API")
  public void user_is_logging_to_the_gmi_api() throws JsonProcessingException {
    baseURI="https://www.gmibank.com/api/";
    Map<String,Object> bdy= new HashMap<>();
    bdy.put("username","team18_admin");
    bdy.put("password","Team18admin");
    bdy.put("rememberme","true");
    ObjectMapper mapper = new ObjectMapper();
    String s = mapper.writeValueAsString(bdy);
    Response authorization = given().contentType(ContentType.JSON)
            .accept(ContentType.JSON).
                    body(s).
                    when().
                    post("authenticate");


    tkn = authorization.jsonPath().getString("id_token");


   /* List<Object> list = authorization.jsonPath().getList("");*/
  }
  @When("user sends get request to the API")
  public void user_sends_get_request_to_the_api() {
    Response authorization = given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
            when().
            get("tp-customers").prettyPeek();
    JsonPath jp = authorization.jsonPath();
    List<Map<String,Object>> list = jp.getList("");
    list.stream().map(x->x.get("id")).forEach(System.out::println);
  }

}
