package net.aluraflix.model.video;

import net.aluraflix.service.Mapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

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

    @Override
    public List<Video> map(List<VideoForm> source) {
        return source.stream().map(this::map).toList();
    }

    public Video map(Video video, VideoForm form) {
        video.setTitle(form.getTitulo());
        video.setDescription(form.getDescricao());
        video.setUrl(form.getUrl());
        return video;
    }
}
