package br.com;

import br.com.model.category.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/categorias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private CategoryDTOMapper dtoMapper;

    @Inject
    private CategoryMapper categoryMapper;

    @GET
    public Response getAllCategories() {
        return Response.ok(categoryRepository.listAll()
                .stream()
                .map(dtoMapper::map)).build();
    }

    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        return categoryRepository.findByIdOptional(id)
                .map(dtoMapper::map)
                .map(categoryDTO -> Response.ok(categoryDTO).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response postCategories(@Valid CategoryForm categoryForm) {
        Category category = categoryMapper.map(categoryForm);

        categoryRepository.persist(category);
        if (categoryRepository.isPersistent(category)) {
            CategoryDTO dto = dtoMapper.map(category);

            return Response.created(URI.create("/categorias/" + dto.id())).entity(dto).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateCategory(@PathParam("id") Long id, @Valid CategoryDTO categoryDTO) {
        return categoryRepository.findByIdOptional(id)
                .map(category -> {
                    category.setTitle(categoryDTO.titulo());
                    category.setColor(categoryDTO.cor());
                    CategoryDTO dto = dtoMapper.map(category);
                    return Response.ok(dto).build();
                }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteCategory(@PathParam("id") Long id) {
        boolean deleted = categoryRepository.deleteById(id);
        if (deleted) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
