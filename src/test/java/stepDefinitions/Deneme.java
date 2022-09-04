package stepDefinitions;

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
import utilities.Driver;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.bonigarcia.wdm.WebDriverManager.isDockerAvailable;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

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
    @Test
    public void gmi() throws JsonProcessingException {
        baseURI="https://www.gmibank.com/api/authenticate";
        Map<String,Object> bdy=new HashMap<>();
        bdy.put("username","gino.wintheiser");
        bdy.put("password","%B6B*q1!TH");
        ObjectMapper mapper=new ObjectMapper();
        String s=mapper.writeValueAsString(bdy);
        Response response=given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(s).
                when().post().prettyPeek();
        String tkn=response.jsonPath().getString("Authorization");

        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                when().get("https://www.gmibank.com/api/accounts").prettyPeek();
    }
    @Test
    public void gmi2() throws JsonProcessingException, SQLException {
        baseURI="https://www.gmibank.com/api/";
        String ID="38016";
        Response authorization = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON).
                pathParam("ID",ID).

                header("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaW5vLndpbnRoZWlzZXIiLCJhdXRoIjoiUk9MRV9FTVBMT1lFRSIsImV4cCI6MTY1ODIzNjMyMX0.fx3ydSIrFh25-CDG_1BnIWUGKy5r-oUJZU8kIXyhKSVn7p5EfB_TMs9ef2ASzgnkwe1q6shAd9iHT_sPJTerRQ").
                when().delete("tp-customers/{ID}").prettyPeek();
        String nameAPI= authorization.jsonPath().getString("firstName") + " " + authorization.jsonPath().getString("lastName");
        System.out.println(nameAPI);
        Connection conn= DriverManager.getConnection("jdbc:postgresql://157.230.48.97:5432/gmibank_db","techprodb_user","Techpro_@126" );
        Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from tp_customer where id="+ID);
        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<String,Object> map=new HashMap<>();
        while (resultSet.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
        }
        String nameDB=map.get("first_name")+" "+map.get("last_name");
        System.out.println(nameDB);


    }
    @Test
    public void worldBank(){

        baseURI="https://api.worldbank.org/v2/country/indicator/SP.POP.TOTL?format=json&date=2021&per_page=300";
        Response response=given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                when().get(baseURI);
        JsonPath js=response.jsonPath();
        List<Object> ls=js.getList("");
        List<Map<String,Object>> ls2=new ArrayList<>();
        for (Object o:ls) {
            if(o instanceof List){
                List<Map<String,Object>> ls3=(List<Map<String,Object>>)o;
                for (Map<String,Object> map:ls3) {
                    ls2.add(map);
                }
            }
        }
        ls2.stream().filter(map->map.get("value")!=null).filter(map->Long.parseLong(map.get("value").toString())>80000000).forEach(map->System.out.println(map.get("value")+" "+map.get("country")));
    }
    @Test
    public void pojo() throws JsonProcessingException {

        baseURI="https://api.worldbank.org/v2/country/indicator/SP.POP.TOTL?format=json&date=2021&per_page=2";
        Response response=given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                when().get(baseURI);
        ObjectMapper mapper=new ObjectMapper();
        List list = mapper.readValue(response.asString(), List.class);
        List<Map<String,Object>> ls=new ArrayList<>();
        for (Object o:list) {
            if(o instanceof List){
                List<Map<String,Object>> ls3=(List<Map<String,Object>>)o;
                for (Map<String,Object> map:ls3) {
                    ls.add(map);
                }
            }
        }
        WBPojo wbPojo=new WBPojo();
        Map<String, Object> additionalProperties = wbPojo.getAdditionalProperties();
        for (Map<String,Object> map:ls) {
            if(map.get("country")!=null){
                additionalProperties.put("country",map.get("country"));
            }
            if(map.get("value")!=null){
                additionalProperties.put("value",map.get("value"));
            }
        }
        System.out.println(additionalProperties.get("country") + " " + additionalProperties.get("value"));
    }
    @Test
    public void enf() throws JsonProcessingException {
       /* baseURI="https://inflation-by-api-ninjas.p.rapidapi.com/v1/inflation";
        Map<String,Object> headers=new HashMap<>();
        headers.put("x-rapidapi-host","inflation-by-api-ninjas.p.rapidapi.com");
        headers.put("x-rapidapi-key","07475edaadmsh706c2d5e735b7aep1e4912jsnbb5010132758");
        Response response=given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                headers(headers).
                when().get(baseURI);
        JsonPath js=response.jsonPath();
        List<Map<String,Object>> ls=js.getList("");
       ls.stream().filter(m->Double.parseDouble(m.get("yearly_rate_pct").toString())>10).map(m->m.get("country")+" "+m.get("yearly_rate_pct")).forEach(System.out::println);
        */
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


        Response user = given().accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                when().get("https://medunna.com/api/users/"+"patient123" ).prettyPeek()   ;
        System.out.println(user.jsonPath().getString("firstName"));



    }
    public static int factorial(int n){
       int res=1;
         for (int i = 1; i <= n; i++) {
              res*=i;
         }
        return res;

    }
    public static boolean isFactorial(int n){

        for (int i = 1; i <=n ; i++) {
            if(factorial(i)==n){
                return true;
            }

        }

        return false;
    }

    @Test
    public void factorialTest() throws JsonProcessingException, SQLException {
       // System.out.println(isFactorial(27));
      //  WebDriverManager webDriverManager = WebDriverManager.chromedriver().browserInDocker();
       // assumeThat(isDockerAvailable()).isTrue();
      /*  WebDriver driver = webDriverManager.create();
        driver.get("https://bonigarcia.dev/selenium-webdriverjava/");
        assert driver.getTitle().contains("Selenium WebDriver");
*/     /* Map<String,Object> bdy1 = new HashMap<>();
ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bdy1);
        bdy1.put("search_product","top");
        given().accept(ContentType.JSON).

                when().post("https://automationexercise.com/api/searchProduct/{search_product}") .prettyPeek();*/
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
        Response accs = given().accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                when().get("users");
        JsonPath js2= accs.jsonPath();
        List<Map<String,Object>> list = js2.getList("");
       Connection con= DriverManager.getConnection("jdbc:postgresql://157.230.48.97:5432/gmibank_db","techprodb_user","Techpro_@126");
        Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = statement.executeQuery("select * from jhi_user");
        ResultSetMetaData metaData = rs.getMetaData();
        List<Map<String,Object>> ls=new ArrayList<>();
        while (rs.next()) {
            Map<String,Object> map=new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i),rs.getObject(i));
            }
            ls.add(map);
        }
       ls.stream().filter(m->m.get("id").toString().equals("114348")).map(m->m.get("first_name"))
               .forEach(System.out::println);
        list.stream().filter(m->m.get("id").toString().equals("114348")).map(m->m.get("firstName"))
                .forEach(System.out::println);



    }
    @ParameterizedTest
    @ValueSource(ints={123169,3976})
    public void testingDB(int id) throws SQLException, JsonProcessingException {
       /* Connection con = DriverManager.getConnection("jdbc:postgresql://157.230.48.97:5432/gmibank_db", "techprodb_user", "Techpro_@126");
        Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = statement.executeQuery(" select * from tp_account where id =" + id);
        ResultSetMetaData metaData = rs.getMetaData();
        Map<String, Object> map = new HashMap<>();
        while (rs.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i), rs.getObject(i));
            }
        }*/
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
        Response accs = given().accept(ContentType.JSON).
                header("Authorization", "Bearer " + tkn).
                when().get("tp-accounts/");
        JsonPath js2= accs.jsonPath();
       List<Map<String,Object>> map2 = js2.getList("");
         //System.out.println(map2);
        map2.stream().filter(m->m.get("accountType").toString().equalsIgnoreCase("Saving"))
                .forEach(System.out::println);

    }

}









