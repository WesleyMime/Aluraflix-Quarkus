package net.aluraflix.model.client;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Schema(name = "Client", description = "Client representation")
public class ClientForm {

    @Email
    @Schema(example = "username@email.com", required = true)
    public String username;
    @NotBlank
    @Schema(example = "password", required = true)
    public String password;

    public ClientForm(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
