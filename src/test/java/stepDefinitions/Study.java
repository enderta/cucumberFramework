package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.openqa.selenium.json.Json;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static java.sql.DriverManager.getConnection;

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
@Test
public void DbTest() throws SQLException {
	Connection con=null;
	Statement st=null;
	ResultSet rs=null;
	ResultSetMetaData metaData=null;
	con = DriverManager.getConnection("jdbc:postgresql://157.230.48.97:5432/gmibank_db","techprodb_user","Techpro_@126");
	st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	rs = st.executeQuery("select * from tp_customer");
	metaData = rs.getMetaData();
	List<Map<String,Object>> list= new ArrayList<>();
	while(rs.next()){
		Map<String,Object> map = new HashMap<>();
		for(int i=1;i<=metaData.getColumnCount();i++){
			map.put(metaData.getColumnName(i),rs.getObject(i));
		}
		list.add(map);
	}
	list.stream().filter(x->x.get("id").toString().equals("40375")).forEach(System.out::println);

}
@Test
public void testD() throws SQLException {
	baseURI="http://localhost:3001/lists";
	Response response = given().contentType("application/json").when().get().then().statusCode(200).extract().response();
	JsonPath jp=response.jsonPath();
List<Map<String,Object>> listApi = jp.getList("");
Connection con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/emp","postgres","ender");
Statement st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
ResultSet rs=st.executeQuery("select * from groups");
ResultSetMetaData metaData=rs.getMetaData();
List<Map<String,Object>> listDB=new ArrayList<>();
while (rs.next()){
	Map<String,Object> map=new HashMap<>();
	for(int i=1;i<=metaData.getColumnCount();i++){
		map.put(metaData.getColumnName(i),rs.getObject(i));
	}
	listDB.add(map);
}
assert listApi.size()==listDB.size();
}


}