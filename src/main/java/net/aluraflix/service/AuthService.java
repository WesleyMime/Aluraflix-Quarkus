package net.aluraflix.service;

import net.aluraflix.model.client.Client;
import net.aluraflix.model.client.ClientForm;
import org.apache.sshd.common.config.keys.loader.openssh.kdf.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    public Response login(ClientForm clientForm) {
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

    public Response signIn(ClientForm clientForm) {
        if (Client.find("username", clientForm.username).firstResultOptional().isPresent()) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        Client client = Client.add(clientForm.username, clientForm.password, "user");
        if (!client.isPersistent()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }
}
