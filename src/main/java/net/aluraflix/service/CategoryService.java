package net.aluraflix.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.logging.Log;
import net.aluraflix.exception.EntityNotFoundException;
import net.aluraflix.model.category.*;
import net.aluraflix.model.video.Video;
import net.aluraflix.model.video.VideoDTO;
import net.aluraflix.model.video.VideoDTOMapper;
import net.aluraflix.model.video.VideoRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
    public Cursor<CategoryDTO> getAllCategories(Long cursor) {
        List<Category> categoryList = categoryRepository.findWithPaging(cursor, PAGE_SIZE_LIMIT);
        showOnlyFiveVideosPerCategory(categoryList);

        Long next = getNextIdForPaging(categoryList);
        List<CategoryDTO> categoryDtoList = dtoMapper.map(categoryList);
        return new Cursor<>(categoryDtoList, next);
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
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findByIdOptional(id);

        if (optionalCategory.isEmpty()) {
            throw new EntityNotFoundException(Category.class, id);
        }
        return dtoMapper.map(optionalCategory.get());
    }

    @CacheResult(cacheName = "videos-by-category")
    public Cursor<VideoDTO> getVideosByCategory(Long id, Long cursor) {

        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(id);
        if (categoryOptional.isEmpty()) {
            throw new EntityNotFoundException(Category.class, id);
        }

        List<Video> videoList = videoRepository.findVideosByCategoryId(id, cursor, PAGE_SIZE_LIMIT);

        Long next = VideoService.getNextIdForPaging(videoList);
        List<VideoDTO> videoDtoList = videoDTOMapper.map(videoList);
        return new Cursor<>(videoDtoList, next);
    }

    @Transactional
    public CategoryDTO postCategories(CategoryForm categoryForm) {
        Category category = categoryMapper.map(categoryForm);
        categoryRepository.persist(category);

        invalidateCache();
        CategoryDTO categoryDTO = dtoMapper.map(category);
        Long categoryId = categoryDTO.id();
        Log.infov("Successfully posted category id {0}.", categoryId);
        return categoryDTO;
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryForm categoryForm) {
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(id);

        if (categoryOptional.isEmpty()) {
            throw new EntityNotFoundException(Category.class, id);
        }

        Category category = categoryMapper.map(categoryOptional.get(), categoryForm);
        invalidateCache();
        Log.infov("Category id {0} updated.", id);
        return dtoMapper.map(category);
    }

    public boolean deleteCategory(Long id) {
        boolean deleted = categoryRepository.deleteById(id);
        if (!deleted) {
            throw new EntityNotFoundException(Category.class, id);
        }
        invalidateCache();
        Log.infov("Category id {0} deleted.", id);
        return true;
    }

    @CacheInvalidateAll(cacheName = "get-categories")
    @CacheInvalidateAll(cacheName = "category-id")
    @CacheInvalidateAll(cacheName = "videos-by-category")
    void invalidateCache() {}
}
