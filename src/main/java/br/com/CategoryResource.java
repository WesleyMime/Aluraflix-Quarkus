package br.com;

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

    @GET
    public Response getAllCategories() {
        return Response.ok(categoryRepository.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        return categoryRepository.findByIdOptional(id)
                .map(category -> Response.ok(category).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());

    }

    @POST
    @Transactional
    public Response postCategories(@Valid Category category) {
        categoryRepository.persist(category);
        if (categoryRepository.isPersistent(category)) {
            return Response.created(URI.create("/categorias/" + category.getId())).entity(category).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
