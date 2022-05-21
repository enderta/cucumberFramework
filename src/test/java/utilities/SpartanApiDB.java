package utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class SpartanApiDB {

    public static String get() {
        baseURI = "http://54.147.17.39:8000/api/";
        Response id1 = given().contentType(ContentType.JSON)

                .when().get("spartans");
        List<Map<String,Object>> list = id1.jsonPath().getList("");
        String name=list.get(list.size() - 1).get("name").toString();
        return name;
    }

    public static String DB() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@54.147.17.39:1521:xe", "SP", "SP");
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from spartans");
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<Map<String, Object>> list = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i), resultSet.getObject(i));

            }
            list.add(map);
        }
        return (list.get(list.size() - 1).get("NAME")).toString();
    }
}
