package net.aluraflix.exception;

import com.fasterxml.jackson.core.JsonParseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

    @Override
    public Response toResponse(JsonParseException e) {
        ErrorResponse.ErrorMessage errorMessage = new ErrorResponse.ErrorMessage("Invalid JSON");
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }
}