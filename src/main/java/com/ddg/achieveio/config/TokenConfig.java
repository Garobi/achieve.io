package com.ddg.achieveio.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ddg.achieveio.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TokenConfig {

    @Value("${api.security.token.secret}")
    private String secret = "secret";

    public String generateToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secret);

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .toList();

        return JWT.create()
                .withClaim("userId", user.getId().toString())
                .withClaim("roles", roles)
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return  Optional.of(
                    JWTUserData.builder()
                            .email(decodedJWT.getSubject())
                            .userId(UUID.fromString(decodedJWT.getClaim("userId").asString()))
                            .roles(decodedJWT.getClaim("roles").asList(String.class))
                            .build()
            );
        }
        catch (Exception ex){
            return Optional.empty();
        }
    }
}
