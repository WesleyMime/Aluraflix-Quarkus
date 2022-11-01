package br.com;

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
                .body("url", hasItems("url-1"));
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
                .body("url", containsString("url-1"));
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
        Video video = new Video();
        video.setTitulo("title-1");
        video.setDescricao("description-1");
        video.setUrl("url-1");

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
                .body("url", containsString("url-1"));
    }

    @Test
    @Order(1)
    public void testPostVideoEndpointKO() {
        Video video = new Video();
        video.setTitulo("title-1");
        video.setDescricao("description-1");

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
        Video video = new Video();
        video.setTitulo("title");
        video.setDescricao("description");
        video.setUrl("url");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .put(VIDEOS_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("titulo", containsString("title"))
                .body("descricao", containsString("description"))
                .body("url", containsString("url"));
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