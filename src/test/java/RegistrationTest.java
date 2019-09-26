import com.griddynamics.request.NewUser;
import com.griddynamics.request.RegistrationRequest;
import com.griddynamics.response.SuccessfulRegistrationResponse;
import com.griddynamics.response.UnsuccessfulRegistrationResponse;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class RegistrationTest {

    private static final String BASE_URI = "https://conduit.productionready.io/api";

    private String blankError = "can't be blank";
    private String invalidError = "is invalid";
    private String alreadyTakenError = "has already been taken";
    private String tooLongPasswordError = "is too long (maximum is 72 characters)";
    private String tooShortPasswordError = "is too short (minimum is 8 characters)";
    private String tooLongUsernameError = "is too long (maximum is 20 characters)";
    private String tooShortUsernameError = "is too short (minimum is 1 character)";


    @DisplayName("Successful new user registration")
    @Description("It sends a POST request with valid data and checks if the response status code is OK " +
            "and email and username in the response are the same as sent in the request AND login")
    @Test
    public void registerNewUser(){

        String validUsername = "scoiattolo16";
        String validEmailAddress = "scoiattolo16@super.com";
        String validPassword = "scoiattolo16";


        NewUser newUser = new NewUser(validUsername, validEmailAddress, validPassword);

        RegistrationRequest requestBody = new RegistrationRequest(newUser);

        Response restAssuredResponse =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                .when()
                        .post("/users");
        SuccessfulRegistrationResponse response = restAssuredResponse.as(SuccessfulRegistrationResponse.class);

        assertThat(response.user.username, equalTo(validUsername));
        assertThat(response.user.email, equalTo(validEmailAddress));

        restAssuredResponse.then().assertThat().statusCode(200);
//       todo   add LOGIN check

    }

    @DisplayName("New user registration: all fields are empty")
    @Description("It sends a POST request with empty strings and checks if the response status code is 422 and there are expected error messages")
    @Test
    public void registerNewUserWithEmptyFields(){

        String emptyString = "";

        NewUser newUser = new NewUser(emptyString, emptyString, emptyString);

        RegistrationRequest requestBody = new RegistrationRequest(newUser);

        Response restAssuredResponse =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                .when()
                        .post("/users");


        UnsuccessfulRegistrationResponse response = restAssuredResponse.as(UnsuccessfulRegistrationResponse.class);

        assertThat(response.errors.username, hasItems(blankError, tooShortUsernameError));
        assertThat(response.errors.email, hasItems(blankError));
        assertThat(response.errors.password, hasItems(blankError));

        restAssuredResponse.then().assertThat().statusCode(422);
    }
}
