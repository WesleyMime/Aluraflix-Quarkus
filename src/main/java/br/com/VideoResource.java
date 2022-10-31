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

    @POST
    @Transactional
    public Response postVideo(@Valid Video video) {
        videoRepository.persist(video);
        if (videoRepository.isPersistent(video)) {
            return Response.created(URI.create("/videos/" + video.getId())).entity(video).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}