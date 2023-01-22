package net.aluraflix.model.category;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {

    public List<Category> findWithPaging(Long cursor, Integer pageSizeLimit) {
        return find("from Category where id >= ?1", cursor)
                .page(Page.ofSize(pageSizeLimit))
                .list();
    }
}
