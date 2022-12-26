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

public class CarsOwnerApiDb {
@Test
public void test1() throws SQLException, JsonProcessingException {

	baseURI = "http://localhost:3001";
	Map<String, Object> userBdy = new HashMap<>();
	userBdy.put("email", "John@gmail.com");
	userBdy.put("password", "John123");
	ObjectMapper objectMapper = new ObjectMapper();
	String json = objectMapper.writeValueAsString(userBdy);
	Response register = given().contentType(ContentType.JSON).body(json).when().post("/register");
	JsonPath jsonPath = register.jsonPath();
	String msgRes = jsonPath.getString("message");
	System.out.println("msgRes = " + msgRes);
	Assert.assertEquals("User created", msgRes);
	Response login = given().contentType(ContentType.JSON).body(json).when().post("/login");
	String token = login.jsonPath().getString("token");


	Map<String, Object> carBdy = new HashMap<>();
	carBdy.put("make", "Toyota");
	carBdy.put("model", "Camry");
	carBdy.put("vin", 2019);
	carBdy.put("plate", "123456");
	carBdy.put("owner_id", 1);
	String jsonCar = objectMapper.writeValueAsString(carBdy);
	Response postCar = given().contentType(ContentType.JSON).header("Authorization", token).body(jsonCar).when().post("/cars");
	postCar.then().statusCode(201);

	Response cars = given().contentType(ContentType.JSON).header("Authorization", token).when().get("/cars");

	cars.then().statusCode(200);
	List<Map<String, Object>> carsList = cars.jsonPath().getList("");
	int lastCarId = (int) carsList.get(carsList.size() - 1).get("id");
	Response car = given().contentType(ContentType.JSON).header("Authorization", token).when().get("/cars/" + lastCarId);
	car.then().statusCode(200);
	given().contentType(ContentType.JSON).header("Authorization", token).when().delete("/cars/" + lastCarId).then().statusCode(200);
	given().contentType(ContentType.JSON).header("Authorization", token).when().get("/cars/" + lastCarId).then().statusCode(404);
	Response lastUser = given().contentType(ContentType.JSON).header("Authorization", token).when().get("/users");
	lastUser.then().statusCode(200);
	List<Map<String, Object>> usersList = lastUser.jsonPath().getList("");
	int lastUserId = (int) usersList.get(usersList.size() - 1).get("id");
	given().contentType(ContentType.JSON).header("Authorization", token).when().delete("/users/" + lastUserId).then().statusCode(200);
	given().contentType(ContentType.JSON).header("Authorization", token).when().get("/users/" + lastUserId).then().statusCode(404);


}
}