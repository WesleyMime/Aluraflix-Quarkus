package net.aluraflix.controller;

import io.quarkus.test.junit.QuarkusTest;
import net.aluraflix.model.client.ClientForm;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthResourceTest {

    private static final String CLIENT_ENDPOINT = "/client";
    private static final String LOGIN_ENDPOINT = CLIENT_ENDPOINT + "/login";
    private static final String SIGNIN_ENDPOINT = CLIENT_ENDPOINT + "/signin";

    @Test
    @Order(2)
    void testLoginEndpointOK() {
        ClientForm video = new ClientForm("johndoe@email.com", "foobar");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .contentType(MediaType.TEXT_PLAIN)
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(2)
    void testLoginEndpointKO() {
        ClientForm video = new ClientForm("johndoe@email.com", "invalid");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @Order(1)
    void testSignInEndpointOK() {
        ClientForm video = new ClientForm("johndoe@email.com", "foobar");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .post(SIGNIN_ENDPOINT)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(1)
    void testSignInEndpointKO() {
        ClientForm video = new ClientForm("johndoe", "invalid");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .post(SIGNIN_ENDPOINT)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(2)
    void testSignInEndpointKO2() {
        ClientForm video = new ClientForm("johndoe@email.com", "foobar");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(video)
                .when()
                .post(SIGNIN_ENDPOINT)
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
    }
}