package br.com.model.category;

import br.com.Mapper;
import br.com.model.video.VideoDTO;
import br.com.model.video.VideoDTOMapper;

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
