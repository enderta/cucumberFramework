/*
package stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import pages.BookItPages;
import utilities.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class BookIt {
    BookItPages bookIT=new BookItPages();
    BookitAPI bookitAPI=new BookitAPI();
    Response authorization;
    String nameUI,nameAPI,romUI,token,romNameAPI,APIDescription,DBDescription,email,pass;
    @Given("user logs in using {string} {string}")
    public void user_logs_in_using(String email, String pass) {
        this.email=email;
        this.pass=pass;
        Driver.get().get("https://cybertek-reservation-qa3.herokuapp.com/sign-in");
       bookIT.login(email,pass);
    }
    @When("user is on the my self page")
    public void user_is_on_the_my_self_page() {
    bookIT.mySelf();
        BrowserUtils.waitFor(3);
        nameUI = bookIT.nameUI.getText();
        bookIT.logout();
    }
    @When("I logged Bookit api using {string} and {string}")
    public void i_logged_bookit_api_using_and(String email, String pass) {
        baseURI= ConfigurationReader.get("bookitAPI");
         token = BookitAPI.token(email, pass);
         authorization = given().accept(ContentType.JSON).header("Authorization", token).get("/api/users/me").prettyPeek();



    }
    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {
        nameAPI = authorization.jsonPath().getString("firstName")+" "+authorization.jsonPath().getString("lastName");
    }
    @Then("UI,API and Database user information must be match")
    public void ui_api_and_database_user_information_must_be_match() {
        Assert.assertEquals(nameAPI,nameUI);
    }
    @Given("user is on the {string} page")
    public void user_is_on_the_page(String room) {
        Driver.get().findElement(By.xpath("//a[.='"+room+"']")).click();

    }
    @Then("user sees {string} header")
    public void user_sees_header(String room) {
        BrowserUtils.waitFor(3);
         romUI = Driver.get().findElement(By.xpath("//h2")).getText();
        Assert.assertEquals(romUI,room);
    }
    @Then("User go to API {string} page")
    public void user_go_to_api_page(String room) {
        String token1 = BookitAPI.token(email, pass);
        Response authorization1 = given().accept(ContentType.JSON).header("Authorization", token1).get("/api/rooms/" + room).prettyPeek();
         romNameAPI = authorization1.jsonPath().getString("name");
         APIDescription=authorization1.jsonPath().getString("description");
    }
    @Then("API and UI rooms are the same name")
    public void api_and_ui_rooms_are_the_same_name() {
        Assert.assertEquals(romNameAPI,romUI);
        String query = "SELECT * FROM room WHERE name = '" + romUI + "'";
        String url="jdbc:postgresql://room-reservation-qa.cxvqfpt4mc2y.us-east-1.rds.amazonaws.com:5432/room_reservation_qa";
        String userName="qa_user";
        String pass="Cybertek11!";
        List<Map<String, Object>> queryResultMap2 = DBUtils.getQueryResultMap2(query, url, userName, pass);
        DBDescription=queryResultMap2.get(0).get("description").toString();
        Assert.assertEquals(DBDescription,APIDescription);
    }

}
*/
