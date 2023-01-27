package net.aluraflix.service;

import io.quarkus.logging.Log;
import net.aluraflix.exception.EmailAlreadyRegisteredException;
import net.aluraflix.exception.InvalidCredentialsException;
import net.aluraflix.model.client.Client;
import net.aluraflix.model.client.ClientForm;
import org.apache.sshd.common.config.keys.loader.openssh.kdf.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    public JwtUtil.JwtDTO login(ClientForm clientForm) {
        Optional<Client> clientOptional = Client.find("username", clientForm.username).firstResultOptional();
        if (clientOptional.isEmpty()) {
            throw new InvalidCredentialsException("Invalid username");
        }
        Client client = clientOptional.get();
        if (!BCrypt.checkpw(clientForm.password, client.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        Log.info("Successful login.");
        String jwt = JwtUtil.generateJwt(client.getUsername(), client.getRoles());
        return new JwtUtil.JwtDTO("Bearer", jwt);
    }

    public boolean signIn(ClientForm clientForm) {
        if (Client.find("username", clientForm.username).firstResultOptional().isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }
        Client.add(clientForm.username, clientForm.password, "user");
        Log.info("Successful registration.");
        return true;
    }
}
