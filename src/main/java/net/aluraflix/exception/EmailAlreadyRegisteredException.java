package net.aluraflix.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException() {
        super("Email already registered.");
    }

    @Provider
    public static class EmailAlreadyRegisteredExceptionMapper implements ExceptionMapper<EmailAlreadyRegisteredException> {

        @Override
        public Response toResponse(EmailAlreadyRegisteredException e) {
            ErrorResponse.ErrorMessage errorMessage = new ErrorResponse.ErrorMessage(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return Response.status(Response.Status.CONFLICT).entity(errorResponse).build();
        }
    }
}
