import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.response.MultipleArticleResponse;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class ArticlesTest {
    private static final String BASE_URI = "https://conduit.productionready.io/api";
    String author = "jake";


    @Test
    public void getArticle() throws Exception{

//        GET /api/articles  gets a list of articles by given author

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MultipleArticleResponse response = mapper.readValue(
                given()
                        .contentType("application/json")
                .when()
                        .get(BASE_URI + "/articles?author=" + author)
                        .body().prettyPrint(), MultipleArticleResponse.class);

         System.out.println("The number of articles written by user '" + author + "': " + response.articlesCount);

         System.out.println("What's really in articles here: " + response.articles);
    }
}
