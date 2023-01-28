package net.aluraflix.model.video;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VideoRepository implements PanacheRepository<Video> {

    public List<Video> findByTitle(String title) {
        return find("lower(title) LIKE ?1", "%" + title.toLowerCase() + "%").list();
    }

    public List<Video> findWithPaging(Long cursor, Integer pageSizeLimit) {
        return find("from Video where id >= ?1", cursor)
                .page(Page.ofSize(pageSizeLimit))
                .list();
    }

    public List<Video> findFreeVideos(Long cursor, Integer pageSizeLimit) {
        return find("from Video v where category.title = 'LIVRE' and id >= ?1", cursor)
                .page(Page.ofSize(pageSizeLimit))
                .list();
    }

    public List<Video> findVideosByCategoryId(Long id, Long cursor, Integer pageSizeLimit) {
        return find("from Video where category.id = ?1 and id >= ?2", id, cursor)
                .page(Page.ofSize(pageSizeLimit))
                .list();
    }
}
