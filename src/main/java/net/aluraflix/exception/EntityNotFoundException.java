package net.aluraflix.exception;

import io.quarkus.logging.Log;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(Class<?> clazz, Long id) {
        super(clazz.getSimpleName() + " id " + id + " not found.");
    }

    @Provider
    public static class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

        @Override
        public Response toResponse(EntityNotFoundException e) {
            Log.info(e.getMessage());
            ErrorResponse.ErrorMessage errorMessage = new ErrorResponse.ErrorMessage(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }
    }
}
