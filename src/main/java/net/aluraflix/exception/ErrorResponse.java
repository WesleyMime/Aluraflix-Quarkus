
package net.aluraflix.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public class ErrorResponse {

    private final List<ErrorMessage> errors;

    public ErrorResponse(ErrorMessage errorMessage) {
        this.errors = List.of(errorMessage);
    }

    public ErrorResponse(List<ErrorMessage> errors) {
        this.errors = errors;
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }

    @Schema(name = "Error", description = "Error representation")
    public static class ErrorMessage {

        @Schema(example = "Field related to the error, if any.")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private final String field;

        @Schema(example = "Message describing the error.")
        private final String message;

        public ErrorMessage(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public ErrorMessage(String message) {
            this.field = null;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

    }

}