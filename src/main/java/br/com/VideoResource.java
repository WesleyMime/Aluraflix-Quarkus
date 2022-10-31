package br.com;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VideoResource {

    @Inject
    private VideoRepository videoRepository;

    @GET
    public Response getAllVideos() {
        List<Video> videos = videoRepository.listAll();
        return Response.ok(videos).build();
    }

    @GET
    @Path("/{id}")
    public Response getVideoById(@PathParam("id") Long id) {
        return videoRepository.findByIdOptional(id)
                .map(video -> Response.ok(video).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response postVideo(@Valid Video video) {
        videoRepository.persist(video);
        if (videoRepository.isPersistent(video)) {
            return Response.created(URI.create("/videos/" + video.getId())).entity(video).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateVideo(@PathParam("id") Long id, @Valid Video updated) {
        return videoRepository.findByIdOptional(id)
                .map(video -> {
                    video.setTitle(updated.getTitle());
                    video.setDescription(updated.getDescription());
                    video.setUrl(updated.getUrl());
                    return Response.ok(video).build();
                }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteVideo(@PathParam("id") Long id) {
        boolean deleted = videoRepository.deleteById(id);
        if (deleted) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}