package net.aluraflix.service;

import io.smallrye.jwt.build.Jwt;

import java.time.Instant;

public abstract class JwtUtil {

    public static String generateJwt(String username, String roles) {
        return Jwt.issuer("Aluraflix")
                .subject(username)
                .groups(roles)
                .expiresAt(Instant.now().plusSeconds(1800))
                .sign();
    }
}
