package com.pavel.jogger.web.dto.auth;

/**
 * Data Transfer Object (DTO) for the authentication response.
 * <p>
 * This class is sent back to the client (Flutter app) after a successful login or registration.
 * It contains the JWT token that the client must store and send with future requests.
 * </p>
 */
public class AuthResponse {

    /// The Jason Web Token
    public String token;

    /**
     * Constructor to initialize the response with the token.
     * * @param accessToken The generated JWT string.
     */
    public AuthResponse(String token) {
        this.token = token;
    }
}
