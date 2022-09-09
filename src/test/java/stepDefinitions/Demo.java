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
    public void test1() throws SQLException {
      Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@54.87.41.10:1521:xe", "hr", "hr");
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from employees");
        ResultSetMetaData metaData = resultSet.getMetaData();
        System.out.println(metaData.getColumnCount());
        List<Map<String, Object>> list = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            list.add(row);
        }
       list.stream().forEach(System.out::println);
    }
}
