package net.aluraflix.controller;

import net.aluraflix.exception.ErrorResponse;
import net.aluraflix.model.client.ClientForm;
import net.aluraflix.service.AuthService;
import net.aluraflix.service.JwtUtil;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/client")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = JwtUtil.JwtDTO.class))
    )
    @APIResponse(
            responseCode = "401",
            description = "Invalid username and password",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response login(
            @RequestBody(
                    description = "Client to authenticate",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClientForm.class))
            )
            @Valid ClientForm clientForm) {
        JwtUtil.JwtDTO token = authService.login(clientForm);
        return Response.ok(token).build();
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
            description = "Client not valid",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "409",
            description = "Client already registered",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response signIn(
            @RequestBody(
                    description = "Client to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClientForm.class))
            )
            @Valid ClientForm clientForm) {
        boolean signedIn = authService.signIn(clientForm);
        if (!signedIn) throw new RuntimeException();
        return Response.status(Response.Status.CREATED).build();
    }
}
