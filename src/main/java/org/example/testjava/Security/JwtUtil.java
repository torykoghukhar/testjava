package org.example.testjava.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-time}")
    private Long lifeTime;

    private SecretKey key;

    public void init() {
        if(secret != null){
            key = Keys.hmacShaKeyFor(secret.getBytes());
        }else {
            throw new IllegalArgumentException("JWT secret is null");
        }
    }

    public String generateToken(Authentication authentication) {
        if(key == null)
            init();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifeTime))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        if(key == null)
            init();
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token).getPayload().getSubject();
        } catch (JwtException e) {
            logger.error("Error extracting username from JWT token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        if(key == null)
            init();
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername());
        } catch (Exception e) {
            logger.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
