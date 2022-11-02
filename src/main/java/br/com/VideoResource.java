package br.com;

import br.com.model.video.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VideoResource {

    @Inject
    private VideoRepository videoRepository;

    @Inject
    private VideoDTOMapper dtoMapper;

    @Inject
    private VideoMapper videoMapper;

    @GET
    public Response getAllVideos() {
        return Response.ok(videoRepository.listAll()
                .stream()
                .map(dtoMapper::map)).build();
    }

    @GET
    @Path("/{id}")
    public Response getVideoById(@PathParam("id") Long id) {
        return videoRepository.findByIdOptional(id)
                .map(dtoMapper::map)
                .map(video -> Response.ok(video).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response postVideo(@Valid VideoForm videoForm) {
        Video video = videoMapper.map(videoForm);

        videoRepository.persist(video);
        if (videoRepository.isPersistent(video)) {
            VideoDTO dto = dtoMapper.map(video);
            return Response.created(URI.create("/videos/" + dto.id())).entity(dto).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateVideo(@PathParam("id") Long id, @Valid VideoForm videoForm) {
        return videoRepository.findByIdOptional(id)
                .map(video -> {
                    video.setTitle(videoForm.getTitulo());
                    video.setDescription(videoForm.getDescricao());
                    video.setUrl(videoForm.getUrl());
                    VideoDTO dto = dtoMapper.map(video);
                    return Response.ok(dto).build();
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