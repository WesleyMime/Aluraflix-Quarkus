package net.aluraflix.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import net.aluraflix.model.video.VideoForm;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasItem;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestSecurity(user = "testUser", roles = {"user"})
class VideoResourceTest {

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
                .body("items.id", hasItem(1))
                .body("items.titulo", hasItem("title-1"))
                .body("items.descricao", hasItem("description-1"))
                .body("items.url", hasItem(URL))
                .body("items.categoriaId", hasItem(1))
                .body("next", is(6));
    }

    @Test
    @Order(2)
    public void testFreeVideosEndpoint() {
        given()
                .when()
                .get(VIDEOS_ENDPOINT + "/free")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("items.id", hasItem(1))
                .body("items.titulo", hasItem("title-1"))
                .body("items.descricao", hasItem("description-1"))
                .body("items.url", hasItem(URL))
                .body("items.categoriaId", hasItem(1));
    }

    @Test
    @Order(2)
    public void testVideoSearchEndpointOK() {
        given()
                .when()
                .param("search", "2")
                .get(VIDEOS_ENDPOINT)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", hasItem(6))
                .body("titulo", hasItems("title-2"))
                .body("descricao", hasItems("description-2"))
                .body("url", hasItems(URL))
                .body("categoriaId", hasItem(3));
    }

    @Test
    @Order(2)
    public void testVideoByIdEndpoint() {
        given()
                .when()
                .get(VIDEOS_ENDPOINT + "/5")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(5))
                .body("titulo", containsString("title-1"))
                .body("descricao", containsString("description-1"))
                .body("url", containsString(URL))
                .body("categoriaId", is(1));

        given()
                .when()
                .get(VIDEOS_ENDPOINT + "/999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body("errors.message", hasItem("Video id 999 not found."));
    }

    @Test
    @Order(1)
    public void testPostVideoEndpointOK() {
        VideoForm video1 = new VideoForm("title-1", "description-1", URL, null);
        VideoForm video2 = new VideoForm("title-2", "description-2", URL, 3L);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video1)
                .when()
                .post(VIDEOS_ENDPOINT)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", containsString("/videos/5"))
                .body("id", is(5))
                .body("titulo", containsString("title-1"))
                .body("descricao", containsString("description-1"))
                .body("url", containsString(URL))
                .body("categoriaId", is(1));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video2)
                .when()
                .post(VIDEOS_ENDPOINT)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", containsString("/videos/6"))
                .body("id", is(6))
                .body("titulo", containsString("title-2"))
                .body("descricao", containsString("description-2"))
                .body("url", containsString(URL))
                .body("categoriaId", is(3));

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

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(VIDEOS_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("titulo", containsString("title"))
                .body("descricao", containsString("description"))
                .body("url", containsString(URL));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .put(VIDEOS_ENDPOINT + "/999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body("errors.message", hasItem("Video id 999 not found."));
    }

    @Test
    @Order(4)
    public void testDeleteEndpoint() {
        given()
                .delete(VIDEOS_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(VIDEOS_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        given()
                .when()
                .delete(VIDEOS_ENDPOINT + "/999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body("errors.message", CoreMatchers.hasItem("Video id 999 not found."));
    }
}