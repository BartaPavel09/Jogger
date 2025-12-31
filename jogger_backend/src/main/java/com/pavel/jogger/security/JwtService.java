package com.pavel.jogger.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * Service responsible for managing JSON Web Tokens (JWT).
 * <p>
 * This class handles two main operations:
 * <br>
 * 1. Generating tokens (Signing) when a user successfully logs in.
 * <br>
 * 2. Decoding tokens (Validation) when a user makes a request.
 * </p>
 */
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    /**
     * Constructor that initializes the JWT encoder and decoder.
     * @param secret The secret key used to sign the tokens. <br>
     * It is injected from application.properties (app.jwt.secret). <br>
     * This key acts like a digital signature, only the server knows it.
     */
    public JwtService(@Value("${app.jwt.secret}") String secret) {

        SecretKey key = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        this.jwtEncoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(key)
        );

        this.jwtDecoder = NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    /**
     * Generates a new JWT token for a user.
     * @param username The user's identifier (usually email).
     * @param role     The user's role (e.g., "USER", "ADMIN").
     * @return A String representing the signed JWT token.
     */
    public String generateToken(String username, String role) {

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 60 * 24)) 
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    /**
     * Validates and decodes a JWT token string.
     * @param token The raw token string coming from the client.
     * @return The parsed Jwt object containing the claims.
     * @throws JwtException If the token is expired, invalid, or the signature doesn't match.
     */
    public Jwt decode(String token) {
        return jwtDecoder.decode(token);
    }
}
