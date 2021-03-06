import com.griddynamics.request.Article;
import com.griddynamics.request.LoginRequest;
import com.griddynamics.request.NewArticleRequest;
import com.griddynamics.request.User;
import com.griddynamics.response.ArticleResponse;
import com.griddynamics.response.LoginResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("Post")
@Story("Article")
public class PostArticleTest {
    private static String email = "pupurupu@pupurupu.com";
    private static String password = "pupurupu";
    private static String token;
    private static final String BASE_URI = "https://conduit.productionready.io/api";

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

    @Issue("12345")
    @Description("It sends a POST request to /api/articles " +
            "and it checks if title, description, body, and tags in the response are the same as the sent ones.")
    @DisplayName("Publish New Article")
    @Test
    public void publishNewArticle() {

        String title = "By the rivers of Babylon";
        String description = "captivity song";
        String body = "there we sat down, Yeeah, we wept, when we remembered Zion";

        Article article = new Article(title, description, body);

        NewArticleRequest requestBody = new NewArticleRequest(article.withTagList("river", "cry", "Babylon"));

        ArticleResponse articleResponse =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .header("Authorization", "Token " + token)
                        .body(requestBody)
                        .when()
                        .post("/articles").as(ArticleResponse.class);


        assertThat(articleResponse.article.title, equalTo(title));
        assertThat(articleResponse.article.description, equalTo(description));
        assertThat(articleResponse.article.body, equalTo(body));
        assertThat(articleResponse.article.tagList, hasItems("river", "cry", "Babylon"));
    }
}
