package net.aluraflix.model.category;

import net.aluraflix.model.video.VideoDTO;
import net.aluraflix.model.video.VideoDTOMapper;
import net.aluraflix.service.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CategoryDTOMapper implements Mapper<Category, CategoryDTO> {

    @Inject
    private VideoDTOMapper videoDTOMapper;
    @Override
    public CategoryDTO map(Category source) {
        List<VideoDTO> videoDTOS = source.getVideos().stream().map(videoDTOMapper::map).toList();

        return new CategoryDTO(source.getId(), source.getTitle(), source.getColor(), videoDTOS);
    }
}
