package base;

import com.griddynamics.model.User;
import com.griddynamics.request.LoginRequest;
import com.griddynamics.request.RequestUser;
import com.griddynamics.response.LoginResponse;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public abstract class AuthenticatedUserTest extends BaseTest {

    protected String token;

    public AuthenticatedUserTest() {
        token = authenticate(User.DEFAULT);
    }

    @Step
    protected String authenticate(User user) {
        RequestUser requestUser = new RequestUser(user.getEmail(), user.getPassword());

        LoginRequest requestBody = new LoginRequest(requestUser);

        LoginResponse response =
                given()
                            .contentType("application/json")
                            .baseUri(BASE_URI)
                            .body(requestBody)
                        .when()
                            .post("/users/login").as(LoginResponse.class);
         System.out.println("The token is: " + response.user.token);

         return response.user.token;
    }
}
