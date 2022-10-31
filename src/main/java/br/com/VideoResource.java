package br.com;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
}