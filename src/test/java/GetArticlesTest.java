import base.BaseTest;
import com.griddynamics.response.MultipleArticleResponse;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class GetArticlesTest extends BaseTest {
    String author = "pupurupu";
    String tag = "japan";


    @DisplayName("Get articles by author")
    @Description("It gets a list of articles by given author and checks that returned articles author is the one that was requested ")
    @Test
    public void getArticlesByAuthor() {

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


    @DisplayName("Get articles by tag")
    @Description("It gets a list of 10 articles by given tag and checks that tag in returned articles is the one that was requested")
    @Test
    public void getArticlesByTag() {

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