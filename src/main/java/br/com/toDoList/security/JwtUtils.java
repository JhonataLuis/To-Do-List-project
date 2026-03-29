package br.com.toDoList.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationsMs;

    private SecretKey getSigningKey(){
        if (jwtSecret == null || jwtSecret.length() < 32){
            throw new RuntimeException("ERRO: A chave jwt.secret precisa ter pelo menos 32 caracteres no application.properties!");
        }
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication){
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
            .subject(user.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationsMs))
            .signWith(getSigningKey())
            .compact();
    }

    public String getEmailFromToken(String token){
        return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token);
            return true;
        } catch (Exception e) {

            // Logar o erro para saber por que o token é inválido (Expirado, malformado, etc)
            System.err.println("Erro na validação do Token: " + e.getMessage());
            return false;
        }
    }

}
