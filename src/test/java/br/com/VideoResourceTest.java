package br.com;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;

@QuarkusTest
public class VideoResourceTest {

    @Test
    public void testVideosEndpoint() {
        given()
          .when().get("/videos")
          .then()
             .statusCode(Response.Status.OK.getStatusCode())
             .body("title", hasItems("title-1", "title-2", "title-3"))
             .body("description", hasItems("description-1", "description-2", "description-3"))
             .body("url", hasItems("url-1", "url-2", "url-3"));
    }

    @Test
    public void testVideoByIdEndpointOK() {
        given()
                .when().get("/videos/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("title", containsString("title-1"))
                .body("description", containsString("description-1"))
                .body("url", containsString("url-1"));
    }

    @Test
    public void testVideoByIdEndpointKO() {
        given()
                .when().get("/videos/999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testPostVideoEndpointOK() {
        Video video = new Video();
        video.setTitle("title-4");
        video.setDescription("description-4");
        video.setUrl("url-4");

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .post("/videos")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", containsString("/videos/4"))
                .body("title", containsString("title-4"))
                .body("description", containsString("description-4"))
                .body("url", containsString("url-4"));;
    }

    @Test
    public void testPostVideoEndpointKO() {
        Video video = new Video();
        video.setTitle("title-4");
        video.setDescription("description-4");

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .post("/videos")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

}