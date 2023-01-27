package net.aluraflix.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.logging.Log;
import net.aluraflix.exception.EntityNotFoundException;
import net.aluraflix.exception.VideoWithInvalidCategoryException;
import net.aluraflix.model.category.Category;
import net.aluraflix.model.category.CategoryRepository;
import net.aluraflix.model.video.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
    public Cursor<VideoDTO> getVideos(Long cursor) {
        List<Video> videoList = videoRepository.findWithPaging(cursor, PAGE_SIZE_LIMIT);

        Long next = getNextIdForPaging(videoList);
        List<VideoDTO> videoDTOList = dtoMapper.map(videoList);
        return new Cursor<>(videoDTOList, next);
    }

    @CacheResult(cacheName = "get-videos-by-title")
    public List<VideoDTO> getVideosByTitle(String title) {
        List<Video> videoList = videoRepository.findByTitle(title);
        return dtoMapper.map(videoList);
    }

    @CacheResult(cacheName = "free-videos")
    public Cursor<VideoDTO> getFreeVideos(Long cursor) {
        List<Video> videoList = videoRepository.findFreeVideos(cursor, PAGE_SIZE_LIMIT);

        Long next = getNextIdForPaging(videoList);
        List<VideoDTO> videoDtoList = dtoMapper.map(videoList);
        return new Cursor<>(videoDtoList, next);
    }

    static Long getNextIdForPaging(List<Video> videoList) {
        Long next = null;
        if (videoList.size() == PAGE_SIZE_LIMIT) {
            Video video = videoList.get(videoList.size() - 1);
            videoList.remove(video);
            next = video.getId();
        }
        return next;
    }

    @CacheResult(cacheName = "videos-id")
    public VideoDTO getVideoById(Long id) {
        Optional<Video> optionalVideo = videoRepository.findByIdOptional(id);

        if (optionalVideo.isEmpty()) {
            throw new EntityNotFoundException(Video.class, id);
        }
        return dtoMapper.map(optionalVideo.get());
    }

    @Transactional
    public VideoDTO postVideo(VideoForm videoForm) {
        Video video = videoMapper.map(videoForm);

        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(videoForm.getCategoriaId());

        if (categoryOptional.isEmpty()) {
            throw new VideoWithInvalidCategoryException(videoForm.getCategoriaId());
        }
        video.setCategory(categoryOptional.get());
        videoRepository.persist(video);

        invalidateCache();
        VideoDTO videoDTO = dtoMapper.map(video);
        Log.infov("Successfully posted video id {0}.", videoDTO.id());
        return videoDTO;
    }

    @Transactional
    public VideoDTO updateVideo(Long id, VideoForm videoForm) {
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(videoForm.getCategoriaId());

        if (categoryOptional.isEmpty()) {
            throw new VideoWithInvalidCategoryException(videoForm.getCategoriaId());
        }
        Optional<Video> videoOptional = videoRepository.findByIdOptional(id);
        if (videoOptional.isEmpty()) {
            throw new EntityNotFoundException(Video.class, id);
        }
        Video video = videoMapper.map(videoOptional.get(), videoForm);
        video.setCategory(categoryOptional.get());

        invalidateCache();
        Log.infov("Video id {0} updated.", id);
        return dtoMapper.map(video);
    }

    public boolean deleteVideo(Long id) {
        boolean deleted = videoRepository.deleteById(id);
        if (!deleted) {
            throw new EntityNotFoundException(Video.class, id);
        }
        invalidateCache();
        Log.infov("Video id {0} deleted.", id);
        return true;
    }

    @CacheInvalidateAll(cacheName = "get-videos")
    @CacheInvalidateAll(cacheName = "get-videos-by-title")
    @CacheInvalidateAll(cacheName = "free-videos")
    @CacheInvalidateAll(cacheName = "videos-id")
    @CacheInvalidateAll(cacheName = "get-categories")
    @CacheInvalidateAll(cacheName = "category-id")
    @CacheInvalidateAll(cacheName = "videos-by-category")
    void invalidateCache() {}
}