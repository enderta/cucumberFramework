package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.json.Json;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static java.sql.DriverManager.getConnection;

public class Study {
@Test()
public void apiTest() throws JsonProcessingException {
	baseURI = "https://www.gmibank.com/api";
	Map<String, Object> bdy = new HashMap<>();
	bdy.put("username", "team18_admin");
	bdy.put("password", "Team18admin");
	bdy.put("rememberme", "true");
	ObjectMapper objectMapper = new ObjectMapper();
	String json = objectMapper.writeValueAsString(bdy);

	Response post = given().contentType("application/json").body(json).when().post("/authenticate");
	post.prettyPrint();
	JsonPath jp = post.jsonPath();
	String token = jp.getString("id_token");
	System.out.println("token = " + token);

	Response authorization = given().header("Authorization", "Bearer " + token).when().get("/tp-customers?page=0&size=20&sort=id,asc&cacheBuster=1670850532529");
	JsonPath jp1 = authorization.jsonPath();
	List<Map<String, Object>> list = jp1.getList("");
	String id = list.stream().map(x -> x.get("id")).collect(Collectors.toList()).get(0).toString();
	given().accept(ContentType.JSON).header("Authorization", "Bearer " + token).pathParam("id", id).when().get("/tp-customers/{id}").prettyPrint();
}

@Test
public void DbTest() throws SQLException {
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	ResultSetMetaData metaData = null;
	con = DriverManager.getConnection("jdbc:postgresql://157.230.48.97:5432/gmibank_db", "techprodb_user", "Techpro_@126");
	st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	rs = st.executeQuery("select * from tp_customer");
	metaData = rs.getMetaData();
	List<Map<String, Object>> list = new ArrayList<>();
	while (rs.next()) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			map.put(metaData.getColumnName(i), rs.getObject(i));
		}
		list.add(map);
	}
	list.stream().filter(x -> x.get("id").toString().equals("40375")).forEach(System.out::println);

}

@Test
public void testD() throws SQLException, JsonProcessingException {
	Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/emp", "postgres", "ender");
	Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	baseURI = "http://localhost:3001/lists";
	Map<String, Object> bdy = new HashMap<>();
	bdy.put("name", "test");
	ObjectMapper objectMapper = new ObjectMapper();
	String json = objectMapper.writeValueAsString(bdy);
	Response postName = given().contentType("application/json").accept(ContentType.JSON).body(json).when().post("/name");
	postName.prettyPrint();
	JsonPath post = postName.jsonPath();
	String nameApi = post.getString("name").replace("[", "").replace("]", "");
	String id = post.getString("id").replace("[", "").replace("]", "");
	System.out.println("nameApi = " + nameApi);
	System.out.println("id = " + id);
	ResultSet rs = st.executeQuery("select * from groups");
	ResultSetMetaData metaData = rs.getMetaData();
	List<Map<String, Object>> list = new ArrayList<>();
	while (rs.next()) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			map.put(metaData.getColumnName(i), rs.getObject(i));
		}
		list.add(map);
	}

	System.out.println("nameApi = " + nameApi);
	Map<String, Object> bdy2 = new HashMap<>();
	bdy2.put("group_id",Integer.parseInt(id) );
	bdy2.put("email", Faker.instance().internet().emailAddress());
	json = objectMapper.writeValueAsString(bdy2);
	Response postMem = given().contentType(ContentType.JSON).accept(ContentType.JSON).when().body(json).post("/member").then().statusCode(201).extract().response();
	JsonPath post2 = postMem.jsonPath();
	String emailApi = post2.getString("email");
	String idMem = post2.getString("id").replace("[", "").replace("]", "");
	System.out.println(idMem);
	ResultSet rs2 = st.executeQuery("select * from members");
	ResultSetMetaData metaData2 = rs2.getMetaData();
	List<Map<String, Object>> list2 = new ArrayList<>();
	while (rs2.next()) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 1; i <= metaData2.getColumnCount(); i++) {
			map.put(metaData2.getColumnName(i), rs2.getObject(i));
		}
		list2.add(map);
	}
	System.out.println("emailApi = " + emailApi);
	System.out.println(list2);

	Response response = given().contentType("application/json").when().get().then().statusCode(200).extract().response();
	JsonPath jp = response.jsonPath();
	List<Map<String, Object>> listApi = jp.getList("");

	ResultSet rs3 = st.executeQuery("select * from groups");
	ResultSetMetaData metaData3 = rs3.getMetaData();

	List<Map<String, Object>> listDB = new ArrayList<>();
	while (rs3.next()) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 1; i <= metaData3.getColumnCount(); i++) {
			map.put(metaData3.getColumnName(i), rs3.getObject(i));
		}
		listDB.add(map);
	}
	assert listApi.size() == listDB.size();
	Response all = given().contentType("application/json").when().get("/all").then().statusCode(200).extract().response();
	JsonPath jp1 = all.jsonPath();
	List<Map<String, Object>> listApiAll = jp1.getList("");
	//assert listApiAll.size() == listDB.size();

	given().contentType("application/json").when().delete("/members/" + idMem).then().statusCode(200);
	given().contentType("application/json").when().get("/all").prettyPrint();
	given().contentType("application/json").when().delete("/"+nameApi).then().statusCode(200);

}
@Test
public void testHack(){
	System.out.println(run("This is p"));
}
public static String run(String p) {
	String combined_queries = "";
	int countVovel=0;
	int countConst=0;

	for (int i = 0; i < p.length(); i++) {
		char c = Character.toLowerCase(p.charAt(i));
		if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
			countVovel++;
		} else if (c >= 'a' && c <= 'z') {
			countConst++;
		}
	}

	String reverse = new StringBuilder(p).reverse().toString();
	String reverseCase = "";

	for (int i = 0; i < reverse.length(); i++) {
		char c = reverse.charAt(i);
		if (Character.isUpperCase(c)) {
			reverseCase += Character.toLowerCase(c);
		} else if (Character.isLowerCase(c)) {
			reverseCase += Character.toUpperCase(c);
		} else {
			reverseCase += c;
		}
	}

	var split = p.split(" ");
	String dash = "";
	for (int i = 0; i < split.length; i++) {
		dash += split[i] + "-";
	}
	dash = dash.substring(0, dash.length() - 1);

	String pv = "";
	for (int i = 0; i < p.length(); i++) {
		char c = Character.toLowerCase(p.charAt(i));
		if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
			pv += "pv" + c;
		} else {
			pv += p.charAt(i);
		}
	}

	combined_queries = countVovel+ " " + countConst  + "::" + reverseCase + "::" + dash + "::" + pv;
	return combined_queries;
}


}