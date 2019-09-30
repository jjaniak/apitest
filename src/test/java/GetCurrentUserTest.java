import base.AuthenticatedUserTest;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetCurrentUserTest extends AuthenticatedUserTest {
    private String email = "pupurupu@pupurupu.com";
    private String username = "pupurupu";
    private String id = "66692";


    @DisplayName("Get current user")
    @Description("It gets current user and checks if ID, username and email address are correct")
    @Test
    public void getCurrentUser() {

        given()
                .contentType("application/json")
                .baseUri(BASE_URI)
                .header("Authorization", "Token " + token)
                .when()
                .get("/user")
                .then()
                .assertThat().statusCode(200)
                .body("user.id", equalTo(id))
                .body("user.email", equalTo(email))
                .body("user.username", equalTo(username));

//        todo change response type in this test
    }
}