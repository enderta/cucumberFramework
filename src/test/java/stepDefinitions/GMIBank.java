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
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }




//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
}
