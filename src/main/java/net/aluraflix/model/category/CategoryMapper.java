package net.aluraflix.model.category;

import net.aluraflix.service.Mapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CategoryMapper implements Mapper<CategoryForm, Category> {

    @Override
    public Category map(CategoryForm source) {
        Category category = new Category();
        category.setTitle(source.getTitulo());
        category.setColor(source.getCor());
        return category;
    }

    @Override
    public List<Category> map(List<CategoryForm> source) {
        return source.stream().map(this::map).toList();
    }

    public Category map(Category category, CategoryForm form) {
        category.setTitle(form.getTitulo());
        category.setColor(form.getCor());
        return category;
    }
}
