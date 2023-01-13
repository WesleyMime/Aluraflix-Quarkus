package net.aluraflix.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.logging.Log;
import net.aluraflix.model.category.*;
import net.aluraflix.model.video.VideoDTO;
import net.aluraflix.model.video.VideoDTOMapper;

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
    CategoryDTOMapper dtoMapper;

    @Inject
    CategoryMapper categoryMapper;

    @Inject
    VideoDTOMapper videoDTOMapper;

    @CacheResult(cacheName = "get-categories")
    public Response getAllCategories() {
        List<CategoryDTO> categoryDTOList = categoryRepository.listAll()
                .stream()
                .map(dtoMapper::map)
                .toList();
        return Response.ok(categoryDTOList).build();
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
    public Response getVideosByCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(id);

        if (categoryOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<VideoDTO> videosDtoList = categoryOptional.get()
                .getVideos()
                .stream()
                .map(videoDTOMapper::map)
                .toList();
        return Response.ok(videosDtoList).build();
    }

    @Transactional
    public Response postCategories(CategoryForm categoryForm) {
        Category category = categoryMapper.map(categoryForm);

        categoryRepository.persist(category);

        invalidateCache();
        CategoryDTO dto = dtoMapper.map(category);
        Log.infov("Successfully posted category id {0}.", dto.id());
        return Response.created(URI.create("/categorias/" + dto.id())).entity(dto).build();
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
