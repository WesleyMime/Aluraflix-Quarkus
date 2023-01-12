package net.aluraflix.controller;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import net.aluraflix.model.video.VideoForm;
import net.aluraflix.service.VideoService;
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
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getVideos(
            @Parameter(
                    description = "Video title"
            )
            @QueryParam("search") String title) {
        if (title == null) {
            title = "";
            Log.info("Getting all videos.");
        } else Log.infov("Getting video by title {0}.", title);
        return videoService.getVideos(title);
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
        Log.info("Getting videos without authentication.");
        return videoService.getFreeVideos();
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
        Log.infov("Getting video id {0}.", id);
        return videoService.getVideoById(id);
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
    @APIResponse(
            responseCode = "422",
            description = "Invalid category id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response postVideo(
            @RequestBody(
                    description = "Video to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = VideoForm.class))
            )
            @Valid VideoForm videoForm) {
        return videoService.postVideo(videoForm);
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
    @APIResponse(
            responseCode = "422",
            description = "Invalid category id",
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
        return videoService.updateVideo(id, videoForm);
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
        return videoService.deleteVideo(id);
    }
}