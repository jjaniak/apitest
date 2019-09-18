import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.request.LoginRequest;
import com.griddynamics.request.RequestUser;
import com.griddynamics.response.LoginResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class ApiTest {

    private static final String BASE_URI = "https://conduit.productionready.io/api";
    private static String TOKEN;


    @BeforeClass
    public static void getToken() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        RequestUser user = new RequestUser("pupurupu@pupurupu.com","pupurupu");

        LoginRequest requestBody = new LoginRequest(user);

        LoginResponse response = mapper.readValue(
                given()
                    .contentType("application/json")
                    .body(requestBody)
                .when()
                    .post(BASE_URI + "/users/login")
                    .body().prettyPrint(), LoginResponse.class);

        TOKEN = response.user.token;
        System.out.println("The token is: " + response.user.token);
    }

    @Test
    public void logUser() {
//       Authenticates   "POST /api/users/login"   and checks Status Code is successful

        RequestUser user = new RequestUser("pupurupu@pupurupu.com","pupurupu");

        LoginRequest requestBody = new LoginRequest(user);

        Response response =
                given()
                       .contentType("application/json")
                       .body(requestBody)
                .when()
                        .post(BASE_URI + "/users/login");

        response.then().assertThat().statusCode(200);
        response.prettyPrint();
    }


    @Test
    public void getCurrentUser() {
//        GET Current User and checks ID, username and email address are correct

        given()
                .contentType("application/json")
                .header("Authorization", "Token " + TOKEN)
        .when()
                .get(BASE_URI + "/user")
        .then()
                .assertThat().statusCode(200)
                .body("user.id", equalTo(66692))
                .body("user.email", equalTo("pupurupu@pupurupu.com"))
                .body("user.username", equalTo("pupurupu"))
                .log().all();
    }
}