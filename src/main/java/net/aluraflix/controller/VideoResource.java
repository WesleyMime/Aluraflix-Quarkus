package net.aluraflix.controller;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import net.aluraflix.exception.ErrorResponse;
import net.aluraflix.model.video.VideoDTO;
import net.aluraflix.model.video.VideoForm;
import net.aluraflix.service.Cursor;
import net.aluraflix.service.VideoService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Video Resource", description = "Video REST APIs")
@Authenticated
public class VideoResource {

    @Inject
    VideoService videoService;

    @GET
    @Operation(
            operationId = "getVideos",
            summary = "Get videos",
            description = "Get all videos in the database"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation Completed",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Cursor.class))
    )
    public Response getVideos(
            @Parameter(
                    description = "Video title"
            )
            @QueryParam("search") String title,
            @Parameter(
                    description = "Video id for cursor pagination"
            )
            @QueryParam("cursor") Long cursor) {
        if (title == null) {
            if (cursor == null) cursor = 0L;
            Log.info("Getting all videos.");
            Cursor<VideoDTO> videos = videoService.getVideos(cursor);
            return Response.ok(videos).build();
        }
        Log.infov("Getting video by title {0}.", title);
        List<VideoDTO> videosByTitle = videoService.getVideosByTitle(title);
        return Response.ok(videosByTitle).build();
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
            description = "Operation Completed",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Cursor.class))
    )
    @PermitAll
    public Response getFreeVideos(
            @Parameter(
                    description = "Video id for cursor pagination"
            )
            @QueryParam("cursor") Long cursor) {
        if (cursor == null) cursor = 0L;
        Log.info("Getting videos without authentication.");
        Cursor<VideoDTO> freeVideos = videoService.getFreeVideos(cursor);
        return Response.ok(freeVideos).build();
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
            description = "Operation Completed",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = VideoDTO.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Video Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response getVideoById(
            @Parameter(
                    description = "Video id",
                    required = true
            )
            @PathParam("id") Long id) {
        Log.infov("Getting video id {0}.", id);
        VideoDTO video = videoService.getVideoById(id);
        return Response.ok(video).build();
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
            description = "Video Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = VideoDTO.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Video Not Valid",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "422",
            description = "Invalid Category Id",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response postVideo(
            @RequestBody(
                    description = "Video to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = VideoForm.class))
            )
            @Valid VideoForm videoForm) {
        VideoDTO video = videoService.postVideo(videoForm);
        return Response.created(URI.create("/videos/" + video.id())).entity(video).build();
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
            description = "Video Updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = VideoDTO.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Video Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Video Not Valid",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "422",
            description = "Invalid Category Id",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
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
        VideoDTO videoDTO = videoService.updateVideo(id, videoForm);
        return Response.ok(videoDTO).build();
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
            description = "Video Deleted"
    )
    @APIResponse(
            responseCode = "404",
            description = "Video Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response deleteVideo(
            @Parameter(
                    description = "Video id",
                    required = true
            )
            @PathParam("id") Long id) {
        boolean deleted = videoService.deleteVideo(id);
        if (!deleted) throw new RuntimeException();
        return Response.noContent().build();
    }
}