package net.aluraflix.model.category;

import net.aluraflix.service.Mapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CategoryMapper implements Mapper<CategoryForm, Category> {

    @Override
    public Category map(CategoryForm source) {
        Category category = new Category();
        category.setTitle(source.titulo());
        category.setColor(source.cor());
        return category;
    }

    @Override
    public List<Category> map(List<CategoryForm> source) {
        return source.stream().map(this::map).toList();
    }

    public Category map(Category category, CategoryForm form) {
        category.setTitle(form.titulo());
        category.setColor(form.cor());
        return category;
    }
}
