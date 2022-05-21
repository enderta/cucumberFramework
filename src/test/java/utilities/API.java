package utilities;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class API {
    public static int statusCode;
    public static String getToke(String email, String password){
        String token="";
        Map<String, String> map=new HashMap<>();
        map.put("email",email);
        map.put("password",password);
        baseURI="https://library2.cybertekschool.com/rest/v1";
        Response post = given().accept(ContentType.JSON).contentType(ContentType.JSON).body(map)
                .post("/login");
        token=post.path("accessToken");
        statusCode=post.getStatusCode();


        return "Bearer "+token;

    }
    public static String getTokeLib(){
        String token="";

        token=getToke("librarian69@library", "KNPXrm3S" );



        return token;

    }
    public static String getTokeStu(){
        String token="";

        token=getToke( "student66@library", "Ys9e3SFW");



        return token;

    }
    public static Map<String, Object> createUser(String userType) {
        int userGroup=0;
        if (userType.equalsIgnoreCase("librarian")) {
            userGroup = (2);
        } else if (userType.equalsIgnoreCase("student")) {
            userGroup = (3);
        }
        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        String usernameEmail = faker.name().username();
        String companyUrl = faker.company().url().substring(4);
        String email = usernameEmail + "@" + companyUrl;
        String address = faker.address().fullAddress();
        Map<String, Object> user = new HashMap<>();
        user.put("full_name", fullName);
        user.put("email", email);
        user.put("password", faker.number().digits(5));
        user.put("user_group_id", userGroup);
        user.put("status", "active");
        user.put("start_date", "2020-05-05");
        user.put("end_date", "2022-05-05");
        user.put("address", address);
        return user;
    }
    public static Response addUser( String userType) {
        // get a token

        String librarianToken = API.getTokeLib();
        // create new user information
       Map<String,Object> user=createUser(userType);
        // create using using the add_user
        Response response = given().
                header("x-library-token", librarianToken).
                formParams(user) .
                log().all().
                when().
                post(ConfigurationReader.get("base_url") + "/add_user").
                prettyPeek();
        response.then().statusCode(200);
        user.put("id", response.path("id"));
        return response;
    }

}
