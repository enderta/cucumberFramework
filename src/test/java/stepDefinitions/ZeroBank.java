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

public class ZeroBank {
  @Given("the user is logged in")
  public void the_user_is_logged_in() {
    Driver.get().get("http://zero.webappsecurity.com/login.html");
   Driver.get().findElement(By.id("user_login")).sendKeys("username");
    Driver.get().findElement(By.id("user_password")).sendKeys("password");
    Driver.get().findElement(By.name("submit")).click();

  }
  @Then("the {string} page should be displayed")
  public void the_page_should_be_displayed(String title) {
    String actualTitle = Driver.get().getTitle();
    Assert.assertEquals(title, actualTitle);


  }

}
