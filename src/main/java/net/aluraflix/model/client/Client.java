package net.aluraflix.model.client;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;

@Entity
@UserDefinition
public class Client extends PanacheEntity {

    @Username
    private String username;

    @Password
    private String password;

    @Roles
    @Length(max = 20)
    private String roles;

    public static Client add(String username, String password, String roles) {
        Client client = new Client();
        client.username = username;
        client.password = BcryptUtil.bcryptHash(password);
        client.roles = roles;
        client.persist();
        return client;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }
}
