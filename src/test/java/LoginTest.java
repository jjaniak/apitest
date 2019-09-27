import com.griddynamics.request.LoginRequest;
import com.griddynamics.request.User;
import com.griddynamics.response.LoginResponse;
import io.qameta.allure.Attachment;
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

        Response restAssuredResponse =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                .when()
                        .post("/users/login");

        LoginResponse loginResponse = restAssuredResponse.as(LoginResponse.class);

        assertThat(loginResponse.user.username, equalTo(username));
        assertThat(loginResponse.user.email, equalTo(email));
        assertThat(loginResponse.user.id, equalTo(id));

        restAssuredResponse.then().assertThat().statusCode(200);

        attachJsonRequest(requestBody);
        attachJsonResponse(restAssuredResponse);

    }

    @Attachment(value = "My response", type = "application/json")
    public String attachJsonResponse(Response response) {
        return response.prettyPrint();

        // what about the headers?  I just get the body here
        // type of response and requests?  RestAssuredResponse?  how to keep it generic?

        // how to add attachments in AfterEach?    Or just on test failure?   Or add deleting attachments on passed tests?
    }

    @Attachment(value = "My request", type = "application/json")
    public String attachJsonRequest(LoginRequest request) {
        return request.toString();

//      I only get object reference (body, headers?)  use RespectSpecification?
    }
}