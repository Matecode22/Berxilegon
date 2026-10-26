package edu.co.icesi.introspringboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${app.security.secretkey}")
    private String secret;

    @Value("${app.security.expirationMinutes}")
    private int expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Map<String, Object> createClaims(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities",
                userDetails.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .toList());
        return claims;
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000L * 60L * expiration);

        return Jwts.builder()
                .setClaims(createClaims(userDetails))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
