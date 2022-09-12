package stepDefinitions;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import utilities.API;

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
        baseURI= "https://library2.cydeo.com/rest/v1/";
        Map<String,Object> map=new HashMap<>();
        map.put("email","librarian24@library");
        map.put("password","8v8ZByKA");
        ObjectMapper objectMapper=new ObjectMapper();
        String s = objectMapper.writeValueAsString(map);
        Response login = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .body(s).when().post("login");
        String token = login.jsonPath().getString("token");
        System.out.println(token);
    }
}
