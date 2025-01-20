package com.marParaiso.infrastructure.configuration;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey = "MySuperSecureKeyWithAtLeast32Characters12345!";
    private final long validityInMilliseconds = 3600000; // 1 hora

    public String createToken(String username) {
        byte[] keyBytes = secretKey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(keySpec) // Usa la clave segura
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            byte[] keyBytes = secretKey.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
            Jwts.parserBuilder().setSigningKey(keySpec).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        byte[] keyBytes = secretKey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder()
                .setSigningKey(keySpec)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

