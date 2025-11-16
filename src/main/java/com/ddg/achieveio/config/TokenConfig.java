package com.ddg.achieveio.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ddg.achieveio.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TokenConfig {

    private static final String USER_ID_CLAIM = "userId";
    private static final String ROLES_CLAIM = "roles";
    private static final long TOKEN_EXPIRATION_HOURS = 24;

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        Instant now = Instant.now();
        
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .toList();

        return JWT.create()
                .withClaim(USER_ID_CLAIM, user.getId().toString())
                .withClaim(ROLES_CLAIM, roles)
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(TOKEN_EXPIRATION_HOURS, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256(secret));
    }

    public Optional<JWTUserData> validateToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);

            return Optional.of(JWTUserData.builder()
                    .userId(UUID.fromString(jwt.getClaim(USER_ID_CLAIM).asString()))
                    .email(jwt.getSubject())
                    .roles(jwt.getClaim(ROLES_CLAIM).asList(String.class))
                    .build());
                    
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}