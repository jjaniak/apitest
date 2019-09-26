import com.griddynamics.request.LoginRequest;
import com.griddynamics.request.User;
import com.griddynamics.response.LoginResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class LoginTest {
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
    public void logUser() {
//       Authenticates   "POST /api/users/login" and checks Status Code is successful

        User user = new User(email,password);

        LoginRequest requestBody = new LoginRequest(user);

        LoginResponse loginResponse =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                .when()
                        .post("/users/login").as(LoginResponse.class);

        assertThat(loginResponse.user.username, equalTo(username));
        assertThat(loginResponse.user.email, equalTo(email));
        assertThat(loginResponse.user.id, equalTo(id));


        //todo    public ResultMatcher isOk()
        // Assert the response status code is HttpStatus.OK (200).
    }
}