package br.com;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class CategoryResourceTest {

    @Test
    public void testGetAllCategoriesEndpoint() {
        given()
                .get("/categorias")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testPostCategoryEndpointOK() {
        Category category = new Category();
        category.setTitulo("title-1");
        category.setCor("color-1");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category)
                .post("/categorias")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", containsString("/categorias/1"))
                .body("titulo", containsString("title-1"))
                .body("cor", containsString("color-1"));
    }

    @Test
    public void testPostCategoryEndpointKO() {
        Category category = new Category();
        category.setTitulo("title-1");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category)
                .post("/categorias")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }
}