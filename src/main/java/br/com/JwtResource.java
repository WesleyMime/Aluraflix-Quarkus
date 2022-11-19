package br.com;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/jwt")
@Produces(MediaType.TEXT_PLAIN)
public class JwtResource {

    @Inject
    JwtService jwtService;

    @GET
    public Response getJwt() {
        String jwt = jwtService.generateJwt();
        return Response.ok(jwt).build();
    }
}
