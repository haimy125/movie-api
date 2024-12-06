package com.movie.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for handling JWT tokens.
 * Provides methods to generate, parse, and validate tokens.
 */
public class TokenUtil {

    /**
     * Secret key used for signing JWT tokens.
     */
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a JWT token with the given user ID and expiration time.
     *
     * @param userid           The user ID to include in the token's payload.
     * @param expirationMillis The token's validity duration in milliseconds.
     * @return A signed JWT token as a {@code String}.
     */
    public static String generateToken(String userid, long expirationMillis) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMillis);
        String Data = userid;
        return Jwts.builder()
                .setSubject(Data)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the user ID from a given JWT token.
     *
     * @param token The JWT token from which the user ID should be extracted.
     * @return The user ID embedded in the token.
     */
    public static String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates a JWT token by checking its signature and expiration time.
     *
     * @param token The JWT token to validate.
     * @return {@code true} if the token is valid; {@code false} otherwise.
     */
    public static boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
