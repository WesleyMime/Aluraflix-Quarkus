package net.aluraflix.controller;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import net.aluraflix.exception.ErrorResponse;
import net.aluraflix.model.category.CategoryDTO;
import net.aluraflix.model.category.CategoryForm;
import net.aluraflix.model.video.VideoDTO;
import net.aluraflix.service.CategoryService;
import net.aluraflix.service.Cursor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

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
            description = "Operation Completed",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(example = EXAMPLE))
    )
    public Response getAllCategories(
            @Parameter(
                    description = "Category id for cursor pagination"
            )
            @QueryParam("cursor") Long cursor) {
        Log.info("Get all categories.");
        if (cursor == null) cursor = 0L;
        Cursor<CategoryDTO> categories = categoryService.getAllCategories(cursor);
        return Response.ok(categories).build();
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
            description = "Operation Completed",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = CategoryDTO.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Category Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response getCategoryById(
            @Parameter(
                    description = "Category id",
                    required = true
            )
            @PathParam("id") Long id) {
        Log.infov("Getting category by id {0}.", id);
        CategoryDTO categoryById = categoryService.getCategoryById(id);
        return Response.ok(categoryById).build();
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
            description = "Operation Completed",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Cursor.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Category Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response getVideosByCategory(
            @Parameter(
                    description = "Category id",
                    required = true
            )
            @PathParam("id") Long id,
            @Parameter(
                    description = "Video id for cursor pagination"
            )
            @QueryParam("cursor") Long cursor) {
        Log.infov("Getting videos by category id {0}.", id);
        if (cursor == null) cursor = 0L;
        Cursor<VideoDTO> videosByCategory = categoryService.getVideosByCategory(id, cursor);
        return Response.ok(videosByCategory).build();
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
            description = "Category Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = CategoryDTO.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Category Not Valid",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response postCategories(
            @RequestBody(
                    description = "Category to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CategoryForm.class))
            )
            @Valid CategoryForm categoryForm) {
        CategoryDTO categoryDTO = categoryService.postCategories(categoryForm);
        return Response.created(URI.create("/categorias/" + categoryDTO.id())).entity(categoryDTO).build();
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
            description = "Category Updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = CategoryDTO.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Category Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Category Not Valid",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
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
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryForm);
        return Response.ok(updatedCategory).build();
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
            responseCode = "204",
            description = "Category Deleted"
    )
    @APIResponse(
            responseCode = "404",
            description = "Category Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response deleteCategory(
            @Parameter(
                    description = "Category id",
                    required = true
            )
            @PathParam("id") Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (!deleted) throw new RuntimeException();
        return Response.noContent().build();
    }

    private static final String EXAMPLE = """
                            {
                              "items": [
                                {
                                  "id": 0,
                                  "titulo": "string",
                                  "cor": "string",
                                  "videos": [
                                    {
                                      "id": 0,
                                      "titulo": "string",
                                      "descricao": "string",
                                      "url": "string",
                                      "categoriaId": 0
                                    }
                                  ]
                                }
                              ],
                              "next": 0
                            }
                            """;
}
