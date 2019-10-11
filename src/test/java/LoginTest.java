import base.BaseTest;
import com.griddynamics.request.LoginRequest;
import com.griddynamics.request.RequestUser;
import com.griddynamics.response.LoginResponse;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class LoginTest extends BaseTest {
    private String email = "pupurupu@pupurupu.com";
    private String password = "pupurupu";
    private String username = "pupurupu";
    private String id = "66692";

    @DisplayName("Log default user")
    @Description("It sends a POST request to /api/users/login and checks if Status Code is successful and username, email and id are as expected")
    @Test
    public void logUser() {
        RequestUser user = new RequestUser(email, password);

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
    }
}