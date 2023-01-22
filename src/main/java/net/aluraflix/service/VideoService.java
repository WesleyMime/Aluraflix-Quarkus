package net.aluraflix.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.logging.Log;
import net.aluraflix.model.category.Category;
import net.aluraflix.model.category.CategoryRepository;
import net.aluraflix.model.video.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
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

    private static final Integer PAGE_SIZE_LIMIT = 6;

    @CacheResult(cacheName = "get-videos")
    public Response getVideos(String title, Long cursor) {
        if (title.isEmpty()) {
            List<Video> videoList = videoRepository.findWithPaging(cursor, PAGE_SIZE_LIMIT);
            String next = getNextIdForPaging(videoList);

            List<VideoDTO> videoDTOList = dtoMapper.map(videoList);
            return Response.ok(new Cursor<>(videoDTOList, next)).build();
        }

        List<Video> videoList = videoRepository.findByTitle(title);
        List<VideoDTO> videoDtoList = dtoMapper.map(videoList);
        return Response.ok(videoDtoList).build();
    }

    @CacheResult(cacheName = "free-videos")
    public Response getFreeVideos(Long cursor) {
        List<Video> videoList = videoRepository.findFreeVideos(cursor, PAGE_SIZE_LIMIT);

        String next = getNextIdForPaging(videoList);

        List<VideoDTO> videoDtoList = dtoMapper.map(videoList);
        return Response.ok(new Cursor<>(videoDtoList, next)).build();
    }

    static String getNextIdForPaging(List<Video> videoList) {
        String next = null;
        if (videoList.size() == PAGE_SIZE_LIMIT) {
            Video video = videoList.get(videoList.size() - 1);
            videoList.remove(video);
            next = String.valueOf(video.getId());
        }
        return next;
    }

    @CacheResult(cacheName = "videos-id")
    public Response getVideoById(Long id) {
        Optional<Video> optionalVideo = videoRepository.findByIdOptional(id);

        if (optionalVideo.isEmpty()) {
            Log.infov("Video id {0} not found.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        VideoDTO videoDTO = dtoMapper.map(optionalVideo.get());
        return Response.ok(videoDTO).build();
    }

    @Transactional
    public Response postVideo(VideoForm videoForm) {
        Video video = videoMapper.map(videoForm);

        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(videoForm.getCategoriaId());

        if (categoryOptional.isEmpty()) {
            Log.infov("Video with invalid category id: {0}.",videoForm.getCategoriaId());
            return Response.status(422).build();
        }
        video.setCategory(categoryOptional.get());

        videoRepository.persist(video);

        invalidateCache();
        VideoDTO dto = dtoMapper.map(video);
        Log.infov("Successfully posted video id {0}.", dto.id());
        return Response.created(URI.create("/videos/" + dto.id())).entity(dto).build();
    }

    @Transactional
    public Response updateVideo(Long id, VideoForm videoForm) {
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(videoForm.getCategoriaId());

        if (categoryOptional.isEmpty()) {
            Log.infov("Video with invalid category id: {0}.",videoForm.getCategoriaId());
            return Response.status(422).build();
        }
        Optional<Video> videoOptional = videoRepository.findByIdOptional(id);
        if (videoOptional.isEmpty()) {
            Log.infov("Video id {0} not found.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Video video = videoMapper.map(videoOptional.get(), videoForm);
        video.setCategory(categoryOptional.get());

        invalidateCache();
        Log.infov("Video id {0} updated.", id);
        return Response.ok(dtoMapper.map(video)).build();
    }

    public Response deleteVideo(Long id) {
        boolean deleted = videoRepository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        invalidateCache();
        Log.infov("Video id {0} deleted.", id);
        return Response.ok().build();
    }

    @CacheInvalidateAll(cacheName = "get-videos")
    @CacheInvalidateAll(cacheName = "free-videos")
    @CacheInvalidateAll(cacheName = "videos-id")
    @CacheInvalidateAll(cacheName = "get-categories")
    @CacheInvalidateAll(cacheName = "category-id")
    @CacheInvalidateAll(cacheName = "videos-by-category")
    void invalidateCache() {}
}