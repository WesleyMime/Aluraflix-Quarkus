package net.aluraflix.model.category;

import net.aluraflix.service.Mapper;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryMapper implements Mapper<CategoryForm, Category> {

    @Override
    public Category map(CategoryForm source) {
        Category category = new Category();
        category.setTitle(source.getTitulo());
        category.setColor(source.getCor());
        return category;
    }
}
