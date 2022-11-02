package br.com.model.video;

import br.com.Mapper;

import javax.enterprise.context.ApplicationScoped;

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
}
