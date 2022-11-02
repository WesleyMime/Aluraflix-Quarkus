package br.com.model.category;

import br.com.Mapper;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryDTOMapper implements Mapper<Category, CategoryDTO> {
    @Override
    public CategoryDTO map(Category source) {
        return new CategoryDTO(source.getId(), source.getTitle(), source.getColor());
    }
}
