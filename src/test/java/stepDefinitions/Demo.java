package stepDefinitions;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import utilities.API;
import utilities.Driver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.it.Ma;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import pages.LibPages;
import utilities.API;
import utilities.BrowserUtils;
import utilities.DBUtils;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.bonigarcia.wdm.WebDriverManager.isDockerAvailable;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;


import static io.restassured.RestAssured.given;

public class Demo {
    @Test
    public void test1() throws SQLException, JsonProcessingException {
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
    }
    @Test
public void GMIAPI() throws JsonProcessingException {
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


        String tkn = authorization.jsonPath().getString("id_token");
        System.out.println("tkn = " + tkn);
    }
    @Test
public void test2(){
    Driver.get().get("http://localhost:3000/");
        WebElement element = Driver.get().findElement(By.xpath("//button[contains(text(),'Add')]"));
        String attribute = element.getAttribute("aria-expanded");
        Assert.assertEquals("false",attribute);
        element.click();
        String attribute1 = element.getAttribute("aria-expanded");
        Assert.assertEquals("true",attribute1);
        Driver.get().findElement(By.xpath("//input[@name='title']")).sendKeys("yeni turku");
        Driver.get().findElement(By.xpath("//input[@name='url']")).sendKeys("https://www.youtube.com/watch?v=JmhSBFeEGtU");
        Driver.get().findElement(By.xpath("//button[contains(text(),'Submit')]")).click();
        BrowserUtils.waitFor(4);
        Driver.get().switchTo().alert().accept();
        Driver.get().navigate().refresh();
    BrowserUtils.waitFor(2);
        for (WebElement webElement : Driver.get().findElements(By.xpath("//h6"))) {
            if(webElement.getText().equals("yeni turku")){
                Assert.assertTrue(true);
                break;
            }
        }


        Driver.get().quit();

    }


}