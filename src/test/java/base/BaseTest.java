package base;

import com.griddynamics.request.LoginRequest;
import io.qameta.allure.Attachment;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;

public class BaseTest {
    protected static final String BASE_URI = "https://conduit.productionready.io/api";

    @AfterEach
    @Attachment(value = "Response", type = "application/json")
    public String attachResponse(Object response) {
        return response.toString();    // or prettyPrint instead of toString

        //todo make both attachments work

        // put adding attachments in AfterEach?
        // type of response and requests?  RestAssuredResponse?
    }

    @Attachment(value = "Request", type = "application/json")
    public String attachRequest(Object request) {
        return request.toString();

//      I only get object reference (but I should get body)  use RespectSpecification?
    }
}