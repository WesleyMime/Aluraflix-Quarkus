package br.com;

import io.smallrye.jwt.build.Jwt;

public abstract class JwtUtil {

    public static String generateJwt(String username, String roles) {
        long millisecondsInThirtyMinutes = 1_800_000;

        return Jwt.issuer("Aluraflix")
                .subject(username)
                .groups(roles)
                .expiresAt(System.currentTimeMillis() + millisecondsInThirtyMinutes)
                .sign();
    }
}
