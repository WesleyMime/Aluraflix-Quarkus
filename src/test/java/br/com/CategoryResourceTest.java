package br.com;

import br.com.model.category.CategoryForm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryResourceTest {

    private static final String CATEGORY_ENDPOINT = "/categorias";

    @Test
    @Order(2)
    public void testCategoriesEndpoint() {
        given()
                .get(CATEGORY_ENDPOINT)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(2)
    public void testCategoryByIdEndpointOK() {
        given()
                .when()
                .get(CATEGORY_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("titulo", containsString("LIVRE"))
                .body("cor", containsString("FFFFFF"));
    }

    @Test
    @Order(2)
    public void testCategoryByIdEndpointKO() {
        given()
                .when()
                .get(CATEGORY_ENDPOINT + "/999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(2)
    public void testVideosByCategoryEndpointOK() {
        given()
                .when()
                .get(CATEGORY_ENDPOINT + "/1/videos")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    public void testPostCategoryEndpointOK() {
        CategoryForm category = new CategoryForm("LIVRE", "FFFFFF");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category)
                .when()
                .post(CATEGORY_ENDPOINT)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", containsString("/categorias/1"))
                .body("titulo", containsString("LIVRE"))
                .body("cor", containsString("FFFFFF"));
    }

    @Test
    @Order(1)
    public void testPostCategoryEndpointKO() {
        CategoryForm category = new CategoryForm("LIVRE", "");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category)
                .when()
                .post(CATEGORY_ENDPOINT)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(3)
    public void testUpdateCategoryEndpoint() {
        CategoryForm category = new CategoryForm("title", "color");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category)
                .when()
                .put(CATEGORY_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("titulo", containsString("title"))
                .body("cor", containsString("color"));
    }

    @Test
    @Order(4)
    public void testDeleteEndpoint() {
        given()
                .when()
                .delete(CATEGORY_ENDPOINT + "/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}