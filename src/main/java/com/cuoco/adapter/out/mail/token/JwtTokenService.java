package com.cuoco.adapter.out.mail.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenService implements TokenService{

    @Value("${jwt.secret}")
    private String secret;

    // 24 horas
    private static final long EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000;

    @Override
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public String validateTokenAndGetEmail(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado");
        } catch (Exception e) {
            throw new RuntimeException("Token inválido");
        }
    }
}
