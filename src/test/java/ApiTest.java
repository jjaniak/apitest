import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class ApiTest {

    private static String baseURI = "https://conduit.productionready.io/api";
    private static String TOKEN;


    @BeforeClass
    static public void getToken() {

        TOKEN = given()
                .contentType("application/json")
                .body("{\n" +
                        "  \"user\":{\n" +
                        "    \"email\": \"pupurupu@pupurupu.com\",\n" +
                        "    \"password\": \"pupurupu\"\n" +
                        "  }\n" +
                        "}")
                .when()
                        .post(baseURI + "/users/login")
                .then()
                        .extract().path("user.token");

        System.out.println("The token is: " + TOKEN);
    }

    @Test
    public void logUser() {
//       Authenticates   "POST /api/users/login"   and checks Status Code is successful

        Response response =
                given()
                       .contentType("application/json")
                        .body("{\n" +
                        "  \"user\":{\n" +
                        "    \"email\": \"pupurupu@pupurupu.com\",\n" +
                        "    \"password\": \"pupurupu\"\n" +
                        "  }\n" +
                        "}")
                .when()
                        .post(baseURI + "/users/login");

        response.then().assertThat().statusCode(200);
        response.prettyPrint();
    }


    @Test
    public void getCurrentUser() {
//        GET Current User and checks ID, username and email address are correct

        RestAssured.given().
                        contentType("application/json").
                        header("Authorization", "Token " + TOKEN).
                    when().
                        get(baseURI+ "/user").
                    then().
                        assertThat().statusCode(200).
                        and().body("user.id", equalTo(66692)).
                        and().body("user.email", equalTo("pupurupu@pupurupu.com")).
                        and().body("user.username", equalTo("pupurupu")).
                        and().log().all();
    }
}