package com.patient.registry.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private static final String SECRET = "6f162db5934977ef21512d8be0b4bea9a5604b9a6b6908a1bff83d53f77e13b9ec5735c5f0bd8757f0239c6371de7cc8881e013cdd8982a9e2b8f6ca758cd60e";
    private static final long EXPIRATION = 24 * 60 * 60 * 1000; // 24 hours

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }


    public Jws<Claims> validateToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public String getUsername(String token) {
        return validateToken(token).getPayload().getSubject();
    }

    public List<String> getRoles(String token) {
        return (List<String>) validateToken(token).getPayload().get("roles");
    }
}
