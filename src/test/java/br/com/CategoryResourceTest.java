package br.com;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CategoryResourceTest {

    @Test
    public void testGetAllCategoriesEndpoint() {
        given()
                .get("/categorias")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}