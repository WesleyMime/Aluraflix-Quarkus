package br.com;

import br.com.model.category.*;
import br.com.model.video.Video;
import br.com.model.video.VideoDTOMapper;
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
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/categorias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Category Resource", description = "Category REST APIs")
public class CategoryResource {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    CategoryDTOMapper dtoMapper;

    @Inject
    CategoryMapper categoryMapper;

    @Inject
    VideoDTOMapper videoDTOMapper;

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
        return Response.ok(categoryRepository.listAll()
                .stream()
                .map(dtoMapper::map)).build();
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
        return categoryRepository.findByIdOptional(id)
                .map(dtoMapper::map)
                .map(categoryDTO -> Response.ok(categoryDTO).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
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
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(id);

        if (categoryOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Video> videos = categoryOptional.get().getVideos();

        return Response.ok(videos.stream().map(videoDTOMapper::map)).build();
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
        Category category = categoryMapper.map(categoryForm);

        categoryRepository.persist(category);
        if (categoryRepository.isPersistent(category)) {
            CategoryDTO dto = dtoMapper.map(category);

            return Response.created(URI.create("/categorias/" + dto.id())).entity(dto).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
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
        return categoryRepository.findByIdOptional(id)
                .map(category -> {
                    category.setTitle(categoryForm.getTitulo());
                    category.setColor(categoryForm.getCor());
                    CategoryDTO dto = dtoMapper.map(category);
                    return Response.ok(dto).build();
                }).orElse(Response.status(Response.Status.NOT_FOUND).build());
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
        boolean deleted = categoryRepository.deleteById(id);
        if (deleted) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
