package com.example.TO_Do_List.To_Do_List.Security;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final String SECRET_KEY = "thisIsASuperSecretKeyThatIs256BitsLongAndShouldBeKeptSafe!!";  // 256-bit key (32 characters)

    // Generate token method
    public String generateToken(String username) {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(key)  // Sign the JWT with HMAC SHA-512
                .compact();
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        try {
            Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());
            return Jwts.parserBuilder()  // Updated method for parsing
                    .setSigningKey(key)  // Set the key
                    .build()
                    .parseClaimsJws(token)  // Parse the JWT string into Claims
                    .getBody()
                    .getSubject();  // Extract the subject (username)
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());

            // Validate the token
            Jwts.parserBuilder()  // Updated method for parsing
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);  // Parse and check the token validity

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid token: {}", e.getMessage());
            return false;  // If an exception occurs, the token is invalid
        }
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = extractExpirationDate(token);
            return expirationDate.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Error checking token expiration: {}", e.getMessage());
            return false;
        }
    }

    // Extract expiration date from JWT token
    private Date extractExpirationDate(String token) {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        return Jwts.parserBuilder()  // Updated method for parsing
                .setSigningKey(key)  // Set the key
                .build()
                .parseClaimsJws(token)  // Parse the JWT string into Claims
                .getBody()
                .getExpiration();  // Extract expiration date
    }
}

