package net.aluraflix.controller;

import net.aluraflix.model.client.ClientForm;
import net.aluraflix.service.AuthService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/client")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
@Tag(name = "Authentication Resource", description = "Authentication REST APIs")
@PermitAll
public class AuthResource {

    @Inject
    private AuthService authService;

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
            content = @Content(
                    mediaType = MediaType.TEXT_PLAIN,
                    schema = @Schema(implementation = String.class),
                    example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9")
    )
    @APIResponse(
            responseCode = "401",
            description = "Invalid username and password"
    )
    public Response login(
            @RequestBody(
                    description = "Client to authenticate",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClientForm.class))
            )
            @Valid ClientForm clientForm) {
        return authService.login(clientForm);
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
        return authService.signIn(clientForm);
    }
}
