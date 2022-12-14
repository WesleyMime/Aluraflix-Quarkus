package net.aluraflix.controller;

import io.quarkus.security.Authenticated;
import net.aluraflix.model.category.CategoryForm;
import net.aluraflix.service.CategoryService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/categorias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Category Resource", description = "Category REST APIs")
@Authenticated
public class CategoryResource {

    @Inject
    private CategoryService categoryService;

    @GET
    @Operation(
            operationId = "getAllCategories",
            summary = "Get all categories",
            description = "Get all categories in the database"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GET
    @Path("/{id}")
    @Operation(
            operationId = "getCategoryById",
            summary = "Get category by id",
            description = "Get category in the database that have the same id"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "Category not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getCategoryById(
            @Parameter(
                    description = "Category id",
                    required = true
            )
            @PathParam("id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @GET
    @Path("/{id}/videos")
    @Operation(
            operationId = "getVideosByCategoryId",
            summary = "Get videos related to a category",
            description = "Get videos related to a category in the database that have the same id"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "Category not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getVideosByCategory(
            @Parameter(
                    description = "Category id",
                    required = true
            )
            @PathParam("id") Long id) {
        return categoryService.getVideosByCategory(id);
    }

    @POST
    @Transactional
    @Operation(
            operationId = "createCategory",
            summary = "Create a new category",
            description = "Create a new category to add to the database"
    )
    @APIResponse(
            responseCode = "201",
            description = "Category created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Category not valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response postCategories(
            @RequestBody(
                    description = "Category to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CategoryForm.class))
            )
            @Valid CategoryForm categoryForm) {
        return categoryService.postCategories(categoryForm);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(
            operationId = "updateCategory",
            summary = "Update an existing Category",
            description = "Update a category in the database"
    )
    @APIResponse(
            responseCode = "200",
            description = "Category updated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "Category not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Category not valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response updateCategory(
            @Parameter(
                    description = "Category id",
                    required = true
            )
            @PathParam("id") Long id,
            @RequestBody(
                    description = "New data to update category",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CategoryForm.class))
            )
            @Valid CategoryForm categoryForm) {
        return categoryService.updateCategory(id, categoryForm);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(
            operationId = "deleteCategory",
            summary = "Delete an existing Category",
            description = "Delete a category in the database"
    )
    @APIResponse(
            responseCode = "200",
            description = "Category deleted",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "Category not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response deleteCategory(
            @Parameter(
                    description = "Category id",
                    required = true
            )
            @PathParam("id") Long id) {
        return categoryService.deleteCategory(id);
    }
}
