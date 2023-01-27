package net.aluraflix.exception;

import io.quarkus.logging.Log;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String msg) {
        super(msg);
    }

    @Provider
    public static class InvalidCredentialsExceptionMapper implements ExceptionMapper<InvalidCredentialsException> {

        @Override
        public Response toResponse(InvalidCredentialsException e) {
            Log.warn(e.getMessage());
            ErrorResponse.ErrorMessage errorMessage = new ErrorResponse.ErrorMessage(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
        }
    }
}
