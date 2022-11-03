package br.com;

import br.com.model.video.Video;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VideoRepository implements PanacheRepository<Video> {

    public List<Video> findByTitle(String title) {
        return list("lower(title) LIKE ?1", "%" + title.toLowerCase() + "%");
    }
}
