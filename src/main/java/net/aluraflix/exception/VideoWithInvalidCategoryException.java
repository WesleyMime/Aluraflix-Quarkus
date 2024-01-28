package net.aluraflix.exception;

import io.quarkus.logging.Log;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

public class VideoWithInvalidCategoryException extends RuntimeException {

    private static final String MESSAGE = "Video with invalid category id: ";

    public VideoWithInvalidCategoryException(Long categoriaId) {
        super(MESSAGE + categoriaId);
    }

    @Provider
    public static class VideoWithInvalidCategoryExceptionMapper implements ExceptionMapper<VideoWithInvalidCategoryException> {

        @Override
        public Response toResponse(VideoWithInvalidCategoryException e) {
            Log.info(e.getMessage());
            ErrorResponse.ErrorMessage errorMessage = new ErrorResponse.ErrorMessage(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return Response.status(422).entity(errorResponse).build();
        }
    }
}