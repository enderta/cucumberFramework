package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
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
import utilities.Driver;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class Deneme {
    public int factorial(int[] n) {
       //that, given an array A of N integers, returns the smallest positive integer (greater than 0) that does not occur in A.


        Arrays.sort(n);
        int i = 0;
        int result = 1;
        while (i < n.length) {
            if (n[i] == result) {
                result++;
                i = 0;
            } else {
                i++;
            }
        }
        return result;

    }

    @Test
    public void test2(){
        System.out.println(factorial(new int[]{-1,-3}));
    }



    @Test
    public void dbTest() throws SQLException {
        String query = "SELECT * FROM room WHERE name = 'amazon'";
        String url = "jdbc:postgresql://room-reservation-qa.cxvqfpt4mc2y.us-east-1.rds.amazonaws.com:5432/room_reservation_qa";
        String userName = "qa_user";
        String pass = "Cybertek11!";
        List<Map<String, Object>> queryResultMap2 = DBUtils.getQueryResultMap2(query, url, userName, pass);
        System.out.println(queryResultMap2.get(0).get("description"));
    }

    @Test
    public void test() {
        String token = "";
        Map<String, String> map = new HashMap<>();
        map.put("email", "librarian570@library");
        map.put("password", "2gCucjjn");
        baseURI = "https://library2.cybertekschool.com/rest/v1/login";
        Response post = given().contentType(ContentType.URLENC)
                .formParam("email", "librarian570@library")
                .formParam("password", "2gCucjjn")
                .post().prettyPeek();
        token = post.path("token");
        System.out.println(token);

    }

    @Test
    public void lib() {
        Driver.get().get("https://library2.cybertekschool.com/login.html");

        new LibPages().login("librarian47@library", "Sdet2022*");
        Driver.get().findElement(By.xpath("//*[.='Users']")).click();

        BrowserUtils.waitFor(3);
        Select select = new Select(Driver.get().findElement(By.xpath("//select[@name='tbl_users_length']")));
        select.selectByValue("500");
        BrowserUtils.waitFor(3);
        List<WebElement> elements = Driver.get().findElements(By.xpath("//tbody//tr"));

        BrowserUtils.waitFor(3);
        List<WebElement> elements2 = Driver.get().findElements(By.xpath("//tbody//tr"));
        while (elements.size() >= 500) {
            Driver.get().findElement(By.xpath("//a[@title='Next']")).click();
            BrowserUtils.waitFor(6);
            elements = Driver.get().findElements(By.xpath("//tbody//tr"));
            BrowserUtils.waitFor(3);
        }
        Driver.get().quit();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/credentials.csv", numLinesToSkip = 1)
    public void test2(String username, String password) throws JsonProcessingException {
        String token = "";
        Map<String, String> map = new HashMap<>();
        map.put("email", "librarian570@library");
        map.put("password", "2gCucjjn");
        baseURI = "https://library2.cybertekschool.com/rest/v1/login";
        Response post = given().contentType(ContentType.URLENC)
                .formParam("email", username)
                .formParam("password", password)
                .post();
        token = post.path("token");
        System.out.println(token);
        Map<String,Object> bdy=new HashMap<>();
        bdy.put("name","Java in 23 Hours");
        bdy.put("isbn",12345678);
        bdy.put("author","cybertekschool");
        bdy.put("year",2020);
        bdy.put("book_category_id",1);
        bdy.put("description","This is a book about Java");
        ObjectMapper mapper=new ObjectMapper();
        String s = mapper.writeValueAsString(bdy);
        //String toke = API.getToke("librarian570@library", "2gCucjjn");
        baseURI="https://library2.cybertekschool.com/rest/v1/";
        Response add_book = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-library-token", token)
                .body(s)
                .when().post("add_book");
        add_book.prettyPeek();
    }
    @Test
    public void meduna() throws JsonProcessingException {
        Map<String, Object> bdy = new HashMap<>();
        bdy.put("username", "admin79");
        bdy.put("password", "admin");
        bdy.put("rememberMe", "true");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bdy);
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(json).
                when().post("https://medunna.com/api/authenticate").prettyPeek();
       String tkn = response.jsonPath().getString("id_token");
        Response response1 = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                queryParams("page",281).

                when().get("https://medunna.com/api/users/").prettyPeek();
        JsonPath jsonPath = response1.jsonPath();
        List<Map<String,Object>> list = jsonPath.getList("");
        System.out.println(list);

        String s = list.stream().filter(x -> (Integer.parseInt(x.get("id").toString()) == 98815)).map(x -> x.get("firstName").toString()).collect(Collectors.joining());
        System.out.println(s);
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/meduna.csv", numLinesToSkip = 1)
    public void meduna2(String ssn,String firstName,String lastName,String username,String email,String password,String lan,String login,String role) throws JsonProcessingException {

        Map<String, Object> bdy1 = new HashMap<>();
        bdy1.put("username", "admin79");
        bdy1.put("password", "admin");
        bdy1.put("rememberMe", "true");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bdy1);
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(json).
                when().post("https://medunna.com/api/authenticate").prettyPeek();
        String tkn = response.jsonPath().getString("id_token");

        Map<String, Object> bdy = new HashMap<>();
        bdy.put("ssn", ssn);
        bdy.put("firstName", firstName);
        bdy.put("lastName", lastName);
        bdy.put("username", username);
        bdy.put("email", email);
        bdy.put("password", password);
        bdy.put("lan", lan);
        bdy.put("login", login);
        bdy.put("role", role);
        ObjectMapper mapper1 = new ObjectMapper();
        String json1 = mapper1.writeValueAsString(bdy);
        Response response1 = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(json1).
                header("Authorization", "Bearer " + tkn).
                when().post("https://medunna.com/api/users").prettyPeek();

        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("log",login).
                when().get("https://medunna.com/api/users/{log}").prettyPeek();
        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                pathParam("log",login).
                when().delete("https://medunna.com/api/users/{log}").prettyPeek();
    }
  @ParameterizedTest()
    @ValueSource(ints={1,2,3,4,5})
    public void test3(int i) throws SQLException {
        baseURI="https://cyf-react.glitch.me/customers/"+i;
        Response response = given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                when().get(baseURI).prettyPeek();
      Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cyf_hotels", "ender", "ender");
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from customers");
      ResultSetMetaData metaData = resultSet.getMetaData();
      List<Map<String,Object>> ls=new ArrayList<>();
      while (resultSet.next()) {
          Map<String,Object> map=new HashMap<>();
          for (int j = 1; j <= metaData.getColumnCount(); j++) {
              map.put(metaData.getColumnName(j),resultSet.getObject(j));
          }
          ls.add(map);
      }
        System.out.println(ls);


  }
}
