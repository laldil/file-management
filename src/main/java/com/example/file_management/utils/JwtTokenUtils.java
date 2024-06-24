package com.example.file_management.utils;

import com.example.file_management.model.UserEntity;
import com.example.file_management.model.enums.UserRole;
import com.example.file_management.properties.JwtTokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aldi
 * @since 17.06.2024
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenUtils {
    private final JwtTokenProperties jwtTokenProperties;

    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        claims.put("role", user.getRole().name());

        var issuedDate = new Date();
        var expiryDate = new Date(issuedDate.getTime() + jwtTokenProperties.getLifetime().toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(issuedDate)
                .setExpiration(expiryDate)
                .signWith(getKey())
                .compact();
    }

    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    public UserRole getRole(String token) {
        try {
            var roleStr = getClaims(token).get("role", String.class);
            return UserRole.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role in token");
        }
    }

    public Long getId(String token) {
        return getClaims(token).get("id", Long.class);
    }

    public Map<String, Object> getDetails(String token) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", getId(token));

        return claims;
    }


    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return false;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtTokenProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
