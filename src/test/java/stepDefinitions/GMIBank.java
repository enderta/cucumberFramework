package stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
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

public class GMIBank {
    GMIPages gmiBank = new GMIPages();

    @Given("User is on the GMIBank login page")
    public void user_is_on_the_gmi_bank_login_page() {
        Driver.get().get("https://www.gmibank.com/");
        Driver.get().findElement(By.xpath("(//*[@class='dropdown-toggle nav-link'])[2]")).click();
        Driver.get().findElement(By.id("login-item")).click();
        BrowserUtils.waitFor(4);
    }
    @When("User enters valid {string} and {string}")
    public void user_enters_valid_and(String user, String pass) {
      gmiBank.login(user, pass);
        BrowserUtils.waitFor(4);
    }

    List<Map<String,Object>> ls=new ArrayList<>();
    Map<String,Object> map=new HashMap<>();
    @Given("user is logging to the GMI DB")
    public void user_is_logging_to_the_gmi_db() throws SQLException {
        Connection conn= DriverManager.getConnection("jdbc:postgresql://157.230.48.97:5432/gmibank_db","techprodb_user","Techpro_@126" );
        Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from tp_customer");
        ResultSetMetaData metaData = resultSet.getMetaData();

        while (resultSet.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            ls.add(map);
        }


    }
    @When("user sends get request to the DB")
    public void user_sends_get_request_to_the_db() {
        //ls.forEach(System.out::println);
    }
    @Then("user should be able to login successfully DB")
    public void user_should_be_able_to_login_successfully_db() {
     ls.stream().filter(x->x.get("last_name").equals("Romaguera")).forEach(System.out::println);
    }
    List<Map<String,Object>> ls2=new ArrayList<>();
    @Given("user is logging to the GMI API")
    public void user_is_logging_to_the_gmi_api() {
            baseURI="https://www.gmibank.com/api/";

        Response authorization = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON).
                header("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaW5vLndpbnRoZWlzZXIiLCJhdXRoIjoiUk9MRV9FTVBMT1lFRSIsImV4cCI6MTY1ODE0NzM4OH0.iKyq8YZu6D05Wr8rF43yWaKHOMhNGBDJVeFizpF0mWHql2gqzo-xuCMt9TmG8Gnvq9LWYuMww6DVdqsRJZHbOg").
                when().get("tp-customers");

       ls2 = authorization.jsonPath().getList("");


    }
    @When("user sends get request to the API")
    public void user_sends_get_request_to_the_api() {
        ls2.stream().filter(x->x.get("firstName").equals("Ali")).forEach(System.out::println);
    }
    @Then("user should be able to login successfully API")
    public void user_should_be_able_to_login_successfully_api() {

    }
    String nameUI;
    String nameAPI;
    String nameDB;
    @Given("user is getting info about user of {string} from UI")
    public void user_is_getting_info_about_user_of_from_ui(String ID) {
        Driver.get().findElement(By.xpath("//nav//li[@id='entity-menu']")).click();
        Driver.get().findElement(By.xpath("(//nav//li[@id='entity-menu']//a)[2]")).click();
        BrowserUtils.waitFor(4);
        Driver.get().findElement(By.xpath("//tbody/tr//td[.='"+ID+"']")).click();
        BrowserUtils.waitFor(4);
       nameUI= Driver.get().findElement(By.xpath("//dd[1]")).getText() + " " + Driver.get().findElement(By.xpath("//dd[2]")).getText();


    }
    @Then("user should be able to get info about user of {string} from DB And API")
    public void user_should_be_able_to_get_info_about_user_of_from_db_and_api(String ID) throws SQLException  {
        baseURI="https://www.gmibank.com/api/";

        Response authorization = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON).
                pathParam("ID",ID).
                header("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaW5vLndpbnRoZWlzZXIiLCJhdXRoIjoiUk9MRV9FTVBMT1lFRSIsImV4cCI6MTY1ODIzNjMyMX0.fx3ydSIrFh25-CDG_1BnIWUGKy5r-oUJZU8kIXyhKSVn7p5EfB_TMs9ef2ASzgnkwe1q6shAd9iHT_sPJTerRQ").
                when().get("tp-customers/{ID}");
        nameAPI= authorization.jsonPath().getString("firstName") + " " + authorization.jsonPath().getString("lastName");
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
         nameDB=map.get("first_name")+" "+map.get("last_name");



    }
    @Then("all the info should be same")
    public void all_the_info_should_be_same() {
        System.out.println(nameUI);
        System.out.println(nameAPI);
        System.out.println(nameDB);
        Assert.assertEquals(nameUI,nameAPI);
        Assert.assertEquals(nameUI,nameDB);
    }
    String balanceUI,BalanceAPI,BalanceDB;
    @Given("user is getting info about acc of {string} from UI")
    public void user_is_getting_info_about_acc_of_from_ui(String ID) {
        Driver.get().findElement(By.xpath("//nav//li[@id='entity-menu']")).click();
        Driver.get().findElement(By.xpath("(//nav//li[@id='entity-menu']//a)[3]")).click();
        BrowserUtils.waitFor(4);
        Driver.get().findElement(By.xpath("//tbody/tr//td[.='"+ID+"']")).click();
        BrowserUtils.waitFor(4);
        balanceUI= Driver.get().findElement(By.xpath("//dd[2]")).getText();
    }
    @Then("user should be able to get info about acc of {string} from DB And API")
    public void user_should_be_able_to_get_info_about_acc_of_from_db_and_api(String ID) {
        baseURI="https://www.gmibank.com/api/";

        Response authorization = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON).
                pathParam("ID",ID).
                header("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaW5vLndpbnRoZWlzZXIiLCJhdXRoIjoiUk9MRV9FTVBMT1lFRSIsImV4cCI6MTY1ODMyNzM1OH0.4lyIzVBJfe7c575bUJAzCTz_KHFXCoxoS-NBflBDL7VNX1EdW6waxzwQnnTO_KZS4QyeFXko_TzLIyYJ9IB5QQ").
                when().get("tp-accounts/{ID}");
        BalanceAPI= authorization.jsonPath().getString("balance");

    }
    @Then("all same acc info should be same")
    public void all_same_acc_info_should_be_same() {
        System.out.println(balanceUI);
        System.out.println(BalanceAPI);
        Assert.assertEquals(balanceUI,BalanceAPI);
    }





//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
}
