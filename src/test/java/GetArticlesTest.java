import com.griddynamics.response.MultipleArticleResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class GetArticlesTest {
    private static final String BASE_URI = "https://conduit.productionready.io/api";
    String author = "pupurupu";
    String tag = "japan";


    @Test
    public void getArticlesByAuthor() {

//        GET /api/articles  gets a list of articles by given author and checks that returned articles author is the one that was requested

        MultipleArticleResponse response =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .param("author", author)
                .when()
                        .get("/articles").as(MultipleArticleResponse.class);

        response.articles.forEach(
                v -> assertThat(v.author.username, equalTo(author)));

        System.out.println("The number of articles written by user '" + author + "': " + response.articlesCount);
    }

    @Test
    public void getArticlesByTag() {
//        GET /api/articles  gets a list of articles by given tag and checks that tag in returned articles is the one that was requested
//        + max number of articles is 10

        MultipleArticleResponse response =
                given()
                        .contentType("application/json")
                        .baseUri(BASE_URI)
                        .formParams("tag", tag,"limit", 10, "author", "jake")
                .when()
                        .get("/articles").as(MultipleArticleResponse.class);

        response.articles.forEach(
                v -> assertThat(v.tagList, hasItems(tag)));

        System.out.println("The total number of articles with tag '" + tag + "': " + response.articlesCount);
        System.out.println("The number of articles here is: " + response.articles.size());
    }
}