package net.aluraflix.model.category;

import net.aluraflix.model.video.VideoDTO;
import net.aluraflix.model.video.VideoDTOMapper;
import net.aluraflix.service.Mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CategoryDTOMapper implements Mapper<Category, CategoryDTO> {

    @Inject
    private VideoDTOMapper videoDTOMapper;
    @Override
    public CategoryDTO map(Category source) {
        List<VideoDTO> videoDTOList = videoDTOMapper.map(source.getVideos());

        return new CategoryDTO(source.getId(), source.getTitle(), source.getColor(), videoDTOList);
    }

    @Override
    public List<CategoryDTO> map(List<Category> source) {
        return source.stream().map(this::map).toList();
    }
}
