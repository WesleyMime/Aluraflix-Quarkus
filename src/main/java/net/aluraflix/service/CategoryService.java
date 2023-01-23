package net.aluraflix.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.logging.Log;
import net.aluraflix.model.category.*;
import net.aluraflix.model.video.Video;
import net.aluraflix.model.video.VideoDTO;
import net.aluraflix.model.video.VideoDTOMapper;
import net.aluraflix.model.video.VideoRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    VideoRepository videoRepository;

    @Inject
    CategoryDTOMapper dtoMapper;

    @Inject
    CategoryMapper categoryMapper;

    @Inject
    VideoDTOMapper videoDTOMapper;

    private static final Integer PAGE_SIZE_LIMIT = 6;

    @CacheResult(cacheName = "get-categories")
    public Response getAllCategories(Long cursor) {
        List<Category> categoryList = categoryRepository.findWithPaging(cursor, PAGE_SIZE_LIMIT);
        showOnlyFiveVideosPerCategory(categoryList);

        Long next = getNextIdForPaging(categoryList);

        List<CategoryDTO> categoryDtoList = dtoMapper.map(categoryList);
        return Response.ok(new Cursor<>(categoryDtoList, next)).build();
    }

    private void showOnlyFiveVideosPerCategory(List<Category> categoryList) {
        categoryList.forEach(category -> {
            List<Video> videos = category.getVideos();
            List<Video> fiveVideosList = videos.stream()
                    .limit(PAGE_SIZE_LIMIT - 1)
                    .toList();
            category.setVideos(fiveVideosList);
        });
    }

    static Long getNextIdForPaging(List<Category> categoryList) {
        Long next = null;
        if (categoryList.size() == PAGE_SIZE_LIMIT) {
            Category category = categoryList.get(categoryList.size() - 1);
            categoryList.remove(category);
            next = category.getId();
        }
        return next;
    }

    @CacheResult(cacheName = "category-id")
    public Response getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findByIdOptional(id);

        if (optionalCategory.isEmpty()) {
            Log.infov("Category id {0} not found.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        CategoryDTO categoryDTO = dtoMapper.map(optionalCategory.get());
        return Response.ok(categoryDTO).build();
    }

    @CacheResult(cacheName = "videos-by-category")
    public Response getVideosByCategory(Long id, Long cursor) {

        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(id);
        if (categoryOptional.isEmpty()) {
            Log.infov("Category id {0} not found.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Video> videoList = videoRepository.findVideosByCategoryId(id, cursor, PAGE_SIZE_LIMIT);

        Long next = VideoService.getNextIdForPaging(videoList);

        List<VideoDTO> videoDtoList = videoDTOMapper.map(videoList);
        return Response.ok(new Cursor<>(videoDtoList, next)).build();
    }

    @Transactional
    public Response postCategories(CategoryForm categoryForm) {
        Category category = categoryMapper.map(categoryForm);

        categoryRepository.persist(category);

        invalidateCache();
        CategoryDTO categoryDTO = dtoMapper.map(category);
        Long categoryId = categoryDTO.id();
        Log.infov("Successfully posted category id {0}.", categoryId);
        return Response.created(URI.create("/categorias/" + categoryId)).entity(categoryDTO).build();
    }

    @Transactional
    public Response updateCategory(Long id, CategoryForm categoryForm) {
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(id);

        if (categoryOptional.isEmpty()) {
            Log.infov("Category id {0} not found.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Category category = categoryMapper.map(categoryOptional.get(), categoryForm);

        invalidateCache();
        Log.infov("Category id {0} updated.", id);
        return  Response.ok(dtoMapper.map(category)).build();
    }

    public Response deleteCategory(Long id) {
        boolean deleted = categoryRepository.deleteById(id);
        if (!deleted) {
            Log.infov("Category id {0} not found.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        invalidateCache();
        Log.infov("Category id {0} deleted.", id);
        return Response.ok().build();
    }

    @CacheInvalidateAll(cacheName = "get-categories")
    @CacheInvalidateAll(cacheName = "category-id")
    @CacheInvalidateAll(cacheName = "videos-by-category")
    void invalidateCache() {}
}
