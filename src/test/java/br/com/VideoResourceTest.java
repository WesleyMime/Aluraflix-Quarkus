package br.com;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;

@QuarkusTest
public class VideoResourceTest {

    @Test
    public void testVideosEndpoint() {
        given()
          .when().get("/videos")
          .then()
             .statusCode(200)
             .body("title", hasItems("title-1", "title-2", "title-3"))
             .body("description", hasItems("description-1", "description-2", "description-3"))
             .body("url", hasItems("url-1", "url-2", "url-3"));
    }

}