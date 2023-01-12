package net.aluraflix.model.video;

import net.aluraflix.service.Mapper;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VideoMapper implements Mapper<VideoForm, Video> {
    @Override
    public Video map(VideoForm source) {
        Video video = new Video();
        video.setTitle(source.getTitulo());
        video.setDescription(source.getDescricao());
        video.setUrl(source.getUrl());
        return video;
    }
}
