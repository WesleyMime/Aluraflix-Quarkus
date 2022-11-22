package br.com;

import br.com.model.category.Category;
import br.com.model.video.*;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Optional;

@Path("/videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Video Resource", description = "Video REST APIs")
@Authenticated
public class VideoResource {

    @Inject
    VideoRepository videoRepository;

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    VideoDTOMapper dtoMapper;

    @Inject
    VideoMapper videoMapper;

    @GET
    @Operation(
            operationId = "getVideos",
            summary = "Get videos",
            description = "Get all videos in the database"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getVideos(
            @Parameter(
                    description = "Video title"
            )
            @QueryParam("search") String title) {
        if (title != null) {
            return Response.ok(videoRepository.findByTitle(title)
                    .stream()
                    .map(dtoMapper::map)).build();
        }
        return Response.ok(videoRepository.listAll()
                .stream()
                .map(dtoMapper::map)).build();
    }

    @GET
    @Path("/free")
    @Operation(
            operationId = "getFreeVideos",
            summary = "Get videos without authentication",
            description = "Get videos from category 'aluraflix' in the database"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @PermitAll
    public Response getFreeVideos() {
        return Response.ok(videoRepository.find("category.title", "Aluraflix")
                .stream()
                .map(dtoMapper::map)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(
            operationId = "getVideosById",
            summary = "Get video by id",
            description = "Get video in the database that have the same id"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "Video not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getVideoById(
            @Parameter(
                    description = "Video id",
                    required = true
            )
            @PathParam("id") Long id) {
        return videoRepository.findByIdOptional(id)
                .map(dtoMapper::map)
                .map(video -> Response.ok(video).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Operation(
            operationId = "createVideo",
            summary = "Create a new video",
            description = "Create a new video to add to the database"
    )
    @APIResponse(
            responseCode = "201",
            description = "Video created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Video not valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response postVideo(
            @RequestBody(
                    description = "Video to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = VideoForm.class))
            )
            @Valid VideoForm videoForm) {
        Video video = videoMapper.map(videoForm);

        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(videoForm.getCategoriaId());

        if (categoryOptional.isPresent()) {
            video.setCategory(categoryOptional.get());
            videoRepository.persist(video);
            if (videoRepository.isPersistent(video)) {
                VideoDTO dto = dtoMapper.map(video);
                return Response.created(URI.create("/videos/" + dto.id())).entity(dto).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(
            operationId = "updateVideo",
            summary = "Update an existing Video",
            description = "Update a video in the database"
    )
    @APIResponse(
            responseCode = "200",
            description = "Video updated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "Video not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Video not valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response updateVideo(
            @Parameter(
                    description = "Video id",
                    required = true
            )
            @PathParam("id") Long id,
            @RequestBody(
                    description = "New data to update video",
                    required = true,
                    content = @Content(schema = @Schema(implementation = VideoForm.class))
            )
            @Valid VideoForm videoForm) {
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(videoForm.getCategoriaId());

        if (categoryOptional.isPresent()) {
            return videoRepository.findByIdOptional(id)
                    .map(video -> {
                        video.setTitle(videoForm.getTitulo());
                        video.setDescription(videoForm.getDescricao());
                        video.setUrl(videoForm.getUrl());
                        video.setCategory(categoryOptional.get());
                        VideoDTO dto = dtoMapper.map(video);
                        return Response.ok(dto).build();
                    }).orElse(Response.status(Response.Status.NOT_FOUND).build());
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(
            operationId = "deleteVideo",
            summary = "Delete an existing Video",
            description = "Delete a video in the database"
    )
    @APIResponse(
            responseCode = "200",
            description = "Video deleted",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "Video not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response deleteVideo(
            @Parameter(
                    description = "Video id",
                    required = true
            )
            @PathParam("id") Long id) {
        boolean deleted = videoRepository.deleteById(id);
        if (deleted) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}