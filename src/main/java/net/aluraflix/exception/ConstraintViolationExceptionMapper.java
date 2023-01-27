
package net.aluraflix.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<ErrorResponse.ErrorMessage> errorMessages = e.getConstraintViolations().stream()
                .map(constraintViolation -> new ErrorResponse.ErrorMessage(
                        getAttribute(constraintViolation),
                        constraintViolation.getMessage()))
                .collect(Collectors.toList());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(errorMessages)).build();
    }

    private String getAttribute(ConstraintViolation<?> constraintViolation) {
        String[] paths = constraintViolation.getPropertyPath().toString().split("\\.");
        return paths[paths.length - 1];
    }
}