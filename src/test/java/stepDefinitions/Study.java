package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.openqa.selenium.json.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class Study {
@Test()
public void apiTest() throws JsonProcessingException {
	baseURI="https://www.gmibank.com/api";
	Map<String,Object> bdy=new HashMap<>();
	bdy.put("username","team18_admin");
	bdy.put("password","Team18admin");
	bdy.put("rememberme","true");
	ObjectMapper objectMapper=new ObjectMapper();
	String json = objectMapper.writeValueAsString(bdy);

	Response post = given().contentType("application/json").body(json).when().post("/authenticate");
	JsonPath jp=post.jsonPath();
	String token = jp.getString("id_token");
	System.out.println("token = " + token);

	Response authorization = given().header("Authorization", "Bearer " + token).when().get("/tp-customers?page=0&size=20&sort=id,asc&cacheBuster=1670850532529");
	JsonPath jp1=authorization.jsonPath();
	List<Map<String,Object>> list = jp1.getList("");
	String id = list.stream().map(x -> x.get("id")).collect(Collectors.toList()).get(0).toString();
	given().accept(ContentType.JSON).header("Authorization", "Bearer " + token).pathParam("id",id).when().get("/tp-customers/{id}").prettyPrint();
}
}