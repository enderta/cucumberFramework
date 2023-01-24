package stepDefinitions;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
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
public void test2() throws InterruptedException {


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
        Thread.sleep(5000);
        baseURI="http://localhost:3001/";
        Response response = given().accept(ContentType.JSON).when().get("/videos").then().extract().response();
        List<Map<String,Object>> list = response.jsonPath().getList("");
        list.stream().filter(map->map.get("title").equals("yeni turku")).forEach(map->{
            Assert.assertEquals("https://www.youtube.com/watch?v=JmhSBFeEGtU",map.get("url"));
        });

        WebElement element1 = Driver.get().findElement(By.xpath("(//button)[30]"));
        JavascriptExecutor js = (JavascriptExecutor) Driver.get();
        js.executeScript("arguments[0].click();",element1);


        Driver.get().quit();

    }
    @Test
    public void rating(){
        Driver.get().get("http://localhost:3000/");
BrowserUtils.waitFor(4);
        WebElement element1 = Driver.get().findElement(By.xpath("((//div[@class='container'])[2]//span)[2]"));
        JavascriptExecutor js = (JavascriptExecutor) Driver.get();
        //get text using javascript
        String text = (String) js.executeScript("return arguments[0].textContent", element1);

        int i = Integer.parseInt(text);
        WebElement element = Driver.get().findElement(By.xpath("(//*[.='+'])[1]"));
        element.click();
        BrowserUtils.waitFor(4);
        Driver.get().navigate().refresh();
        WebElement element2 = Driver.get().findElement(By.xpath("((//div[@class='container'])[2]//span)[2]"));
        String text1 = (String) js.executeScript("return arguments[0].textContent", element2);
        int i1 = Integer.parseInt(text1);
        Assert.assertEquals(i+1,i1);
        Driver.get().quit();
    }


}