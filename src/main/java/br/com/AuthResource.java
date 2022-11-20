package br.com;

import br.com.model.client.Client;
import br.com.model.client.ClientForm;
import org.apache.sshd.common.config.keys.loader.openssh.kdf.BCrypt;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/client")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
@Tag(name = "Authentication Resource", description = "Authentication REST APIs")
@PermitAll
public class AuthResource {

    @POST
    @Path("/login")
    @Operation(
            operationId = "login",
            summary = "Authenticate client",
            description = "Checks the information passed by the client with the database data"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    @APIResponse(
            responseCode = "401",
            description = "Invalid username and password",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Response login(
            @RequestBody(
                    description = "Client to authenticate",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClientForm.class))
            )
            @Valid ClientForm clientForm) {
        Optional<Client> clientOptional = Client.find("username", clientForm.username).firstResultOptional();
        if (clientOptional.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Client client = clientOptional.get();
        if (!BCrypt.checkpw(clientForm.password, client.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(JwtUtil.generateJwt(client.getUsername(), client.getRoles())).build();
    }

    @POST
    @Path("/signin")
    @Transactional
    @Operation(
            operationId = "signIn",
            summary = "Create new client",
            description = "Insert a new client in database"
    )
    @APIResponse(
            responseCode = "201",
            description = "Client created"
    )
    @APIResponse(
            responseCode = "400",
            description = "Client not valid"
    )
    @APIResponse(
            responseCode = "409",
            description = "Client already registered"
    )
    public Response signIn(
            @RequestBody(
                    description = "Client to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClientForm.class))
            )
            @Valid ClientForm clientForm) {
        if (Client.find("username", clientForm.username).firstResultOptional().isPresent()) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        Client client = Client.add(clientForm.username, clientForm.password, "user");
        if (client.isPersistent()) {
            return Response.status(Response.Status.CREATED).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
