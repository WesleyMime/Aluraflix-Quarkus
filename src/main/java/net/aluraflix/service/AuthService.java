package net.aluraflix.service;

import io.quarkus.logging.Log;
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
            Log.warn("Invalid password.");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Log.info("Successful login.");
        return Response.ok(JwtUtil.generateJwt(client.getUsername(), client.getRoles())).build();
    }

    public Response signIn(ClientForm clientForm) {
        if (Client.find("username", clientForm.username).firstResultOptional().isPresent()) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        Client.add(clientForm.username, clientForm.password, "user");
        Log.info("Successful registration.");
        return Response.status(Response.Status.CREATED).build();
    }
}
