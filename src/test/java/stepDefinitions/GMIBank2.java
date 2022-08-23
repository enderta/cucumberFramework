package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import pages.GMIPages;
import utilities.BrowserUtils;
import utilities.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;


public class GMIBank2 {

  @Given("User is on the GMIBank login page")
  public void user_is_on_the_gmi_bank_login_page() {
    Driver.get().get("https://www.gmibank.com/");
    Driver.get().findElement(By.xpath("(//*[@class='dropdown-toggle nav-link'])[2]")).click();
    Driver.get().findElement(By.id("login-item")).click();


  }
  @When("User enters valid {string} and {string}")
  public void user_enters_valid_and(String user, String pass) {
    Driver.get().findElement(By.xpath("//input[@id='username']")).sendKeys(user);
    Driver.get().findElement(By.xpath("//input[@id='password']")).sendKeys(pass);
    Driver.get().findElement(By.xpath("//button[@type='submit']")).click();
    BrowserUtils.waitFor(4);
    String text = Driver.get().findElement(By.xpath("//span[.='Joe King']")).getText();
    Assert.assertEquals("Joe King", text);

  }
  String tkn= "";
  @Given("user is logging to the GMI API")
  public void user_is_logging_to_the_gmi_api() throws JsonProcessingException {
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


    tkn = authorization.jsonPath().getString("id_token");


   /* List<Object> list = authorization.jsonPath().getList("");*/
  }
  @When("user sends get request to the API")
  public void user_sends_get_request_to_the_api() {
    Response authorization = given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
            when().
            get("tp-customers").prettyPeek();
    JsonPath jp = authorization.jsonPath();
    List<Map<String,Object>> list = jp.getList("");
    list.stream().map(x->x.get("id")).forEach(System.out::println);
  }
  Connection con=null;
  Statement st=null;
  ResultSet rs=null;
  ResultSetMetaData metaData=null;

  @Given("user is logging to the GMI DB")
  public void user_is_logging_to_the_gmi_db() throws SQLException {


      con = DriverManager.getConnection("jdbc:postgresql://157.230.48.97:5432/gmibank_db","techprodb_user","Techpro_@126");
       st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
       rs = st.executeQuery("select * from tp_customer");
     metaData = rs.getMetaData();


  }
  List<Map<String,Object>> list= new ArrayList<>();
  @When("user sends get request to the DB")
  public void user_sends_get_request_to_the_db() throws SQLException {

    while(rs.next()){
      Map<String,Object> map = new HashMap<>();
      for(int i=1;i<=metaData.getColumnCount();i++){
        map.put(metaData.getColumnName(i),rs.getObject(i));
      }
      list.add(map);
    }
   // System.out.println(list);

  }
  @Then("user should be able to login successfully DB")
  public void user_should_be_able_to_login_successfully_db() {
   list.stream().filter(x->x.get("first_name").equals("Joe")).map(x->x.get("last_name")).forEach(System.out::println);
  }
String nameUI="";
  String lastNameUI="";
  @Given("user is getting info about acc of {string} from UI")
  public void user_is_getting_info_about_acc_of_from_ui(String id) {
   Driver.get().findElement(By.id("entity-menu")).click();
    Driver.get().findElement(By.xpath("//*[.='Manage Customers']")).click();
    Driver.get().findElement(By.xpath("//tbody//td[1][.='"+id+"']")).click();
    BrowserUtils.waitFor(4);
    nameUI=Driver.get().findElement(By.xpath("(//dt//span[@id='firstName']//..//..//..//dd)[1]")).getText();
    lastNameUI=Driver.get().findElement(By.xpath("(//dt//span[@id='lastName']//..//..//..//dd)[2]")).getText();
  }
  String nameAPI="";
  String lastNameAPI="";
  @Then("user should be able to get info about acc of {string} from DB And API")
  public void user_should_be_able_to_get_info_about_acc_of_from_db_and_api(String id) throws JsonProcessingException {
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
   tkn = authorization.jsonPath().getString("id_token");
    Response authorization1 = given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
            when().
            get("tp-customers/"+id);
    JsonPath jp = authorization1.jsonPath();
    nameAPI=jp.getString("firstName");
    lastNameAPI=jp.getString("lastName");


    tkn = authorization.jsonPath().getString("id_token");
  }
  @Then("all same acc info should be same")
  public void all_same_acc_info_should_be_same() {
    Assert.assertEquals(nameUI,nameAPI);
    Assert.assertEquals(lastNameUI,lastNameAPI);
  }

  @Given("update created a country using api end point {string} and its extension {string} bank8")
  public void update_created_a_country_using_api_end_point_and_its_extension_bank8(String name, String string2) throws JsonProcessingException {
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
    tkn = authorization.jsonPath().getString("id_token");
    Map<String,Object> bdy2= new HashMap<>();
    bdy2.put("name",name);

    ObjectMapper mapper2 = new ObjectMapper();
    String s2 = mapper2.writeValueAsString(bdy2);
    Response authorization1 = given().contentType(ContentType.JSON)
            .accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
            body(s2).
            when().
            post("tp-countries");
    JsonPath jp = authorization1.jsonPath();

    String id = jp.getString("id");
    String name1 = jp.getString("name");
    given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
            when().
            get("tp-countries/"+id);
  Assert.assertEquals(name,name1);
    given().accept(ContentType.JSON).
            header("Authorization", "Bearer " + tkn).
            when().
            delete("tp-countries/"+id).prettyPrint();


  }

}
