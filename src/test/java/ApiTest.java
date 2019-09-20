import com.griddynamics.request.LoginRequest;
import com.griddynamics.request.User;
import com.griddynamics.response.LoginResponse;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class ApiTest {
    private static String email = "pupurupu@pupurupu.com";
    private static String password = "pupurupu";
    private static final String BASE_URI = "https://conduit.productionready.io/api";
    private static String token;


    @BeforeClass
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
    public void logUser() {
//       Authenticates   "POST /api/users/login" and checks Status Code is successful

        User user = new User("pupurupu@pupurupu.com","pupurupu");

        LoginRequest requestBody = new LoginRequest(user);

        Response response =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                .when()
                        .post("/users/login");

        response.then().assertThat().statusCode(200);
        response.prettyPrint();
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
                .body("user.email", equalTo("pupurupu@pupurupu.com"))
                .body("user.username", equalTo("pupurupu"))
                .log().all();
    }
}