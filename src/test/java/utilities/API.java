package utilities;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class    API {
    public static int statusCode;
    public static String getToke(String email, String password){
        String token="";
        baseURI = "https://library2.cybertekschool.com/rest/v1/login";
        Response post = given().contentType(ContentType.URLENC)
                .formParam("email", email)
                    .formParam("password", password)
                .post().prettyPeek();
        token = post.path("token");


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


}
