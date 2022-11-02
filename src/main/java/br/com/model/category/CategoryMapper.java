package br.com.model.category;

import br.com.Mapper;

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
