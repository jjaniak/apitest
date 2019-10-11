import base.BaseTest;
import com.griddynamics.request.LoginRequest;
import com.griddynamics.request.NewUser;
import com.griddynamics.request.RegistrationRequest;
import com.griddynamics.request.RequestUser;
import com.griddynamics.response.LoginResponse;
import com.griddynamics.response.SuccessfulRegistrationResponse;
import com.griddynamics.response.UnsuccessfulRegistrationResponse;
import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.UseDataProvider;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;


public class RegistrationTest extends BaseTest {

    private String blankError = "can't be blank";
    private String invalidError = "is invalid";
    private String alreadyTakenError = "has already been taken";
    private String tooLongPasswordError = "is too long (maximum is 72 characters)";
    private String tooShortPasswordError = "is too short (minimum is 8 characters)";
    private String tooLongUsernameError = "is too long (maximum is 20 characters)";
    private String tooShortUsernameError = "is too short (minimum is 1 character)";


    @DisplayName("Successful new user registration")
    @Description("It sends a POST request with valid data and checks if the response status code is OK, " +
            "if email and username in the response are the same as sent in the request AND it makes a successful login")
    @Test
    public void registerNewUserAndLogin(){

        String validUsername = "scoiattolo";
        String validEmailAddress = "scoiattolo@super.com";
        String validPassword = "scoiattolo";


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


        // After a successful registration checks if user is able to login:

        RequestUser user = new RequestUser(validEmailAddress, validPassword);
        LoginRequest requestBody2 = new LoginRequest(user);

        Response restAssuredResponse2 =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody2)
                        .when()
                        .post("/users/login");

        LoginResponse loginResponse = restAssuredResponse2.as(LoginResponse.class);

        assertThat(loginResponse.user.username, equalTo(validUsername));
        assertThat(loginResponse.user.email, equalTo(validEmailAddress));
        restAssuredResponse.then().assertThat().statusCode(200);
    }

    @DisplayName("Unsuccessful new user registration: all fields are empty")
    @Description("It sends a POST request with empty strings " +
            "and checks if the response status code is 422 and there are expected error messages")
    @Test
    public void registerWithEmptyFields(){

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

    @DisplayName("Unsuccessful new user registration: blank username and password")
    @Description("It sends a POST request with username and password containing only spaces " +
            "and checks if the response status code is 422 and there are expected error messages")
    @Test
    public void registerWithBlankSpaces(){

        String blankUsername = " ";
        String emailAddress = "scoiattolo@super.com";
        String blankPassword = "        ";

        NewUser newUser = new NewUser(blankUsername, emailAddress, blankPassword);

        RegistrationRequest requestBody = new RegistrationRequest(newUser);

        Response restAssuredResponse =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                        .when()
                        .post("/users");


        UnsuccessfulRegistrationResponse response = restAssuredResponse.as(UnsuccessfulRegistrationResponse.class);

        assertThat(response.errors.username, hasItems(blankError));
        assertThat(response.errors.password, hasItems(blankError));
        restAssuredResponse.then().assertThat().statusCode(422);
    }


    @DisplayName("Unsuccessful new user registration: username and email address are already taken")
    @Description("It sends a POST request with valid password and already taken both username and email address. " +
            "It checks if the error message for user and email address is “has already been taken” + status code is 422")
    @Test
    public void registerWithUsernameAndEmailInUse(){

        String takenUsername = "scoiattolo";
        String takenEmailAddress = "scoiattolo@super.com";
        String password = "123456789";

        NewUser newUser = new NewUser(takenUsername, takenEmailAddress, password);
        RegistrationRequest requestBody = new RegistrationRequest(newUser);

        Response restAssuredResponse =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                        .when()
                        .post("/users");


        UnsuccessfulRegistrationResponse response = restAssuredResponse.as(UnsuccessfulRegistrationResponse.class);

        assertThat(response.errors.username, hasItems(alreadyTakenError));
        assertThat(response.errors.email, hasItems(alreadyTakenError));
        restAssuredResponse.then().assertThat().statusCode(422);
    }


    @DataProvider
    public static String[] dataProviderInvalidEmail() {
        return new String[] {
                "xxx@xxx",
                "xxx.com",
                "        ",
                "A@b@c@domain.com",
                "     @    .   "
        };
    }

    @DisplayName("Unsuccessful new user registration: invalid email address + too short password")
    @Description("It sends a POST request with invalid email address and too short password (username is valid). " +
            "Expected invalid email address and too short password error messages, and the status code is 422")
    @Test
    @UseDataProvider("dataProviderInvalidEmail")
    public void registerWithInvalidEmail(String invalidEmail) {

        String username = "cocodrillo";
        String tooShortPassword = "1234567";

        NewUser newUser = new NewUser(username, invalidEmail, tooShortPassword);
        RegistrationRequest requestBody = new RegistrationRequest(newUser);

        Response restAssuredResponse =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                        .when()
                        .post("/users");

        UnsuccessfulRegistrationResponse response = restAssuredResponse.as(UnsuccessfulRegistrationResponse.class);

        assertThat(response.errors.email, hasItems(invalidError));
        assertThat(response.errors.password, hasItems(tooShortPasswordError));
        restAssuredResponse.then().assertThat().statusCode(422);
    }


    @DisplayName("Unsuccessful new user registration: too long username and password")
    @Description("It sends a POST request with too long username (21 chars) and password (73 chars) (email address is valid). " +
            "Expected 'too long' error messages  and the status code is 422")
    @Test
    public void registerWithTooLongUsernameAndPassword() {

        String tooLongUsername = "Ihave21Characters!@#$";
        String emailAddress = "scoiattolo@super.com";
        String tooLongPassword = "Ihave73CharactersIhave73CharactersIhave73CharactersIhave73Characters!@#$%";

        NewUser newUser = new NewUser(tooLongUsername, emailAddress, tooLongPassword);

        RegistrationRequest requestBody = new RegistrationRequest(newUser);

        Response restAssuredResponse =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .body(requestBody)
                        .when()
                        .post("/users");

        UnsuccessfulRegistrationResponse response = restAssuredResponse.as(UnsuccessfulRegistrationResponse.class);

        assertThat(response.errors.username, hasItems(tooLongUsernameError));
        assertThat(response.errors.password, hasItems(tooLongPasswordError));
        restAssuredResponse.then().assertThat().statusCode(422);
    }
}