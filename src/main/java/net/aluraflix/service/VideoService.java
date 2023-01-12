package net.aluraflix.service;

import net.aluraflix.model.category.Category;
import net.aluraflix.model.category.CategoryRepository;
import net.aluraflix.model.video.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.util.Optional;

@ApplicationScoped
public class VideoService {

    @Inject
    VideoRepository videoRepository;

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    VideoDTOMapper dtoMapper;

    @Inject
    VideoMapper videoMapper;

    public Response getVideos(String title) {
        if (title != null) {
            return Response.ok(videoRepository.findByTitle(title)
                    .stream()
                    .map(dtoMapper::map)).build();
        }
        return Response.ok(videoRepository.listAll()
                .stream()
                .map(dtoMapper::map)).build();
    }

    public Response getFreeVideos() {
        return Response.ok(videoRepository.find("category.title", "Aluraflix")
                .stream()
                .map(dtoMapper::map)).build();
    }

    public Response getVideoById(Long id) {
        return videoRepository.findByIdOptional(id)
                .map(dtoMapper::map)
                .map(video -> Response.ok(video).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    public Response postVideo(VideoForm videoForm, SecurityContext sec) {
        Video video = videoMapper.map(videoForm);

        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(videoForm.getCategoriaId());

        if (categoryOptional.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        video.setCategory(categoryOptional.get());
        videoRepository.persist(video);
        if (!videoRepository.isPersistent(video)) {
            return Response.serverError().build();
        }

        VideoDTO dto = dtoMapper.map(video);
        return Response.created(URI.create("/videos/" + dto.id())).entity(dto).build();
    }

    public Response updateVideo(Long id, VideoForm videoForm, SecurityContext sec) {
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(videoForm.getCategoriaId());

        if (categoryOptional.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
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

    public Response deleteVideo(Long id, SecurityContext sec) {
        boolean deleted = videoRepository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}