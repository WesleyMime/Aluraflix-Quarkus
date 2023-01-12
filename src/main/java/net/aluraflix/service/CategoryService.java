package net.aluraflix.service;

import net.aluraflix.model.category.*;
import net.aluraflix.model.video.Video;
import net.aluraflix.model.video.VideoDTOMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

    public Response getAllCategories() {
        return Response.ok(categoryRepository.listAll()
                .stream()
                .map(dtoMapper::map)).build();
    }

    public Response getCategoryById(Long id) {
        return categoryRepository.findByIdOptional(id)
                .map(dtoMapper::map)
                .map(categoryDTO -> Response.ok(categoryDTO).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    public Response getVideosByCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(id);

        if (categoryOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Video> videos = categoryOptional.get().getVideos();
        return Response.ok(videos.stream().map(videoDTOMapper::map)).build();
    }

    public Response postCategories(CategoryForm categoryForm) {
        Category category = categoryMapper.map(categoryForm);

        categoryRepository.persist(category);
        if (!categoryRepository.isPersistent(category)) {
            return Response.serverError().build();
        }

        CategoryDTO dto = dtoMapper.map(category);
        return Response.created(URI.create("/categorias/" + dto.id())).entity(dto).build();
    }

    public Response updateCategory(Long id, CategoryForm categoryForm) {
        return categoryRepository.findByIdOptional(id)
                .map(category -> {
                    category.setTitle(categoryForm.getTitulo());
                    category.setColor(categoryForm.getCor());
                    CategoryDTO dto = dtoMapper.map(category);
                    return Response.ok(dto).build();
                }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    public Response deleteCategory(Long id) {
        boolean deleted = categoryRepository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}
