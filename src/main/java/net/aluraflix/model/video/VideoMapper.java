package net.aluraflix.model.video;

import net.aluraflix.service.Mapper;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VideoMapper implements Mapper<VideoForm, Video> {
    @Override
    public Video map(VideoForm source) {
        Video video = new Video();
        video.setTitle(source.titulo());
        video.setDescription(source.descricao());
        video.setUrl(source.url());
        return video;
    }

    @Override
    public List<Video> map(List<VideoForm> source) {
        return source.stream().map(this::map).toList();
    }

    public Video map(Video video, VideoForm form) {
        video.setTitle(form.titulo());
        video.setDescription(form.descricao());
        video.setUrl(form.url());
        return video;
    }
}
