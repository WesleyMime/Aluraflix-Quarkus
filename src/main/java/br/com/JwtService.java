package br.com;

import io.smallrye.jwt.build.Jwt;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class JwtService {

    public String generateJwt() {
        Set<String> groups = Set.of("user");
        long seconds = System.currentTimeMillis() + 3600;

        return Jwt.issuer("aluraflix")
                .subject("user")
                .groups(groups)
                .expiresAt(seconds)
                .sign();
    }
}
