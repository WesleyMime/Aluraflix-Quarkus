package br.com;

import br.com.model.video.VideoForm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VideoResourceTest {

    public static final String VIDEOS_ENDPOINT = "/videos";
    public static final String URL = "https://www.foo.com";

    @Test
    @Order(2)
    public void testVideosEndpoint() {
        given()
                .when()
                .get(VIDEOS_ENDPOINT)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("titulo", hasItems("title-1"))
                .body("descricao", hasItems("description-1"))
                .body("url", hasItems(URL));
    }

    @Test
    @Order(2)
    public void testVideoByIdEndpointOK() {
        given()
                .when()
                .get(VIDEOS_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("titulo", containsString("title-1"))
                .body("descricao", containsString("description-1"))
                .body("url", containsString(URL));
    }

    @Test
    @Order(2)
    public void testVideoByIdEndpointKO() {
        given()
                .when()
                .get(VIDEOS_ENDPOINT + "/999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(1)
    public void testPostVideoEndpointOK() {
        VideoForm video = new VideoForm("title-1", "description-1", URL, 2L);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .post(VIDEOS_ENDPOINT)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", containsString("/videos/1"))
                .body("titulo", containsString("title-1"))
                .body("descricao", containsString("description-1"))
                .body("url", containsString(URL));
    }

    @Test
    @Order(1)
    public void testPostVideoEndpointKO() {
        VideoForm video = new VideoForm("title-1", "description-1", "", 2L);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .post(VIDEOS_ENDPOINT)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(3)
    public void testPutVideoEndpoint() {
        VideoForm video = new VideoForm("title", "description", URL, 2L);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .put(VIDEOS_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("titulo", containsString("title"))
                .body("descricao", containsString("description"))
                .body("url", containsString(URL));
    }

    @Test
    @Order(4)
    public void testDeleteEndpoint() {
        given()
                .delete(VIDEOS_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}