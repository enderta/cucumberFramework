package utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
public class BookitAPI {
    public static String token(String email,String password){
        String tkn="";
        baseURI=ConfigurationReader.get("bookit_api_url");
        Response response = given().accept(ContentType.JSON).queryParam("email", email).queryParam("password", password)
                .when().get("/sign");
        tkn=response.path("accessToken");


        return "Bearer"+tkn;

    }
}
