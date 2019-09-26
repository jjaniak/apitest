import com.griddynamics.request.LoginRequest;
import com.griddynamics.request.User;
import com.griddynamics.response.LoginResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetCurrentUserTest {

    private static String email = "pupurupu@pupurupu.com";
    private static String password = "pupurupu";
    private static final String BASE_URI = "https://conduit.productionready.io/api";
    private static String token;
    private String username = "pupurupu";
    private String id = "66692";


    @BeforeAll
    public static void getToken() {

        User user = new User(email, password);

        LoginRequest requestBody = new LoginRequest(user);

        LoginResponse response =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                        .when()
                        .post("/users/login").as(LoginResponse.class);

        token = response.user.token;
        System.out.println("The token is: " + token);
    }

    @Test
    public void getCurrentUser() {
//        GET Current User and checks ID, username and email address are correct

        given()
                .contentType("application/json")
                .baseUri(BASE_URI)
                .header("Authorization", "Token " + token)
                .when()
                .get("/user")
                .then()
                .assertThat().statusCode(200)
                .body("user.id", equalTo(66692))
                .body("user.email", equalTo(email))
                .body("user.username", equalTo(username))
                .log().all();

//        todo change response type in this test
    }
}