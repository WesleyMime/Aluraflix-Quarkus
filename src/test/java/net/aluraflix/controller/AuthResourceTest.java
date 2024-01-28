package net.aluraflix.controller;

import io.quarkus.test.junit.QuarkusTest;
import net.aluraflix.model.client.ClientForm;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;

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
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("type", containsString("Bearer"))
                .body("token", containsString("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9"));
    }

    @Test
    @Order(2)
    void testLoginEndpointKO() {
        ClientForm loginForm = new ClientForm("johndoe@email.com", "invalid");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginForm)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
                .body("errors.message", hasItem("Invalid password"));

        ClientForm loginForm2 = new ClientForm("invalid@email.com", "invalid");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginForm2)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
                .body("errors.message", hasItem("Invalid username"));
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
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("errors.field", hasItem("username"));
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
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body("errors.message", hasItem("Email already registered."));
    }
}