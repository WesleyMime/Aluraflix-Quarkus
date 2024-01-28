package net.aluraflix.exception;

import io.quarkus.logging.Log;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Arrays;

@Provider
public class UnexpectedExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        Log.errorv("Error: {0}, at {1}",
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).limit(1L).toList());
        ErrorResponse.ErrorMessage errorMessage = new ErrorResponse.ErrorMessage("An unexpected error has occurred.");
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
    }

}
