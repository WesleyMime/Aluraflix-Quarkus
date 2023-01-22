package net.aluraflix.model.video;

import net.aluraflix.service.Mapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VideoDTOMapper implements Mapper<Video, VideoDTO> {
    @Override
    public VideoDTO map(Video source) {
        return new VideoDTO(
                source.getId(),
                source.getTitle(),
                source.getDescription(),
                source.getUrl(),
                source.getCategory().getId()
        );
    }

    @Override
    public List<VideoDTO> map(List<Video> source) {
        return source.stream().map(this::map).toList();
    }
}
