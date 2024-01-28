package net.aluraflix.model.client;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Client", description = "Client representation")
public record ClientForm(@Schema(example = "username@email.com", required = true) @Email String username,
                        @Schema(example = "password", required = true) @NotBlank String password) {
}
