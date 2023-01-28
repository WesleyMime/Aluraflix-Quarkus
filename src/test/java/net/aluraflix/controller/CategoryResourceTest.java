package net.aluraflix.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import net.aluraflix.model.category.CategoryForm;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestSecurity(user = "testUser", roles = {"user"})
class CategoryResourceTest {

    private static final String CATEGORY_ENDPOINT = "/categorias";

    @Test
    @Order(2)
    public void testCategoriesEndpoint() {
        given()
                .get(CATEGORY_ENDPOINT)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("items.id", hasItem(2))
                .body("items.titulo", hasItem("Aluraflix"))
                .body("items.cor", hasItem("#2a7ae4"))
                .body("next", is(6));
    }

    @Test
    @Order(2)
    public void testCategoryByIdEndpoint() {
        given()
                .when()
                .get(CATEGORY_ENDPOINT + "/7")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(7))
                .body("titulo", containsString("LIVRE"))
                .body("cor", containsString("#FFFFFF"));

        given()
                .when()
                .get(CATEGORY_ENDPOINT + "/999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body("errors.message", hasItem("Category id 999 not found."));
    }

    @Test
    @Order(2)
    public void testVideosByCategoryEndpoint() {
        given()
                .when()
                .get(CATEGORY_ENDPOINT + "/1/videos")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("items.id", hasItem(1))
                .body("items.titulo", hasItem("Aluraflix3"))
                .body("items.descricao", hasItem("Description3"))
                .body("items.url", hasItem("Url3"))
                .body("items.categoriaId", hasItem(1));

        given()
                .when()
                .get(CATEGORY_ENDPOINT + "/999/videos")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body("errors.message", hasItem("Category id 999 not found."));
    }

    @Test
    @Order(1)
    public void testPostCategoryEndpointOK() {
        CategoryForm category = new CategoryForm("LIVRE", "#FFFFFF");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category)
                .when()
                .post(CATEGORY_ENDPOINT)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", containsString("/categorias/7"))
                .body("id", is(7))
                .body("titulo", containsString("LIVRE"))
                .body("cor", containsString("#FFFFFF"));

        CategoryForm category2 = new CategoryForm("LIVRE", "");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category2)
                .when()
                .post(CATEGORY_ENDPOINT)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("errors.field", hasItem("cor"));
    }

    @Test
    @Order(3)
    public void testUpdateCategoryEndpointOK() {
        CategoryForm category = new CategoryForm("title", "color");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category)
                .when()
                .put(CATEGORY_ENDPOINT + "/7")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(7))
                .body("titulo", containsString("title"))
                .body("cor", containsString("color"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(category)
                .when()
                .put(CATEGORY_ENDPOINT + "/999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body("errors.message", hasItem("Category id 999 not found."));
    }

    @Test
    @Order(4)
    public void testDeleteEndpointOK() {
        given()
                .when()
                .delete(CATEGORY_ENDPOINT + "/7")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(CATEGORY_ENDPOINT + "/7")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        given()
                .when()
                .delete(CATEGORY_ENDPOINT + "/999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body("errors.message", hasItem("Category id 999 not found."));
    }
}