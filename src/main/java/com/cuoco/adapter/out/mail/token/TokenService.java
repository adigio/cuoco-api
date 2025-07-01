package com.cuoco.adapter.out.mail.token;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private static final long EXPIRATION_SECONDS = 24 * 60 * 60; // 24 horas

    private final Map<String, TokenData> tokenStore = new ConcurrentHashMap<>();

    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, new TokenData(userId, Instant.now().plusSeconds(EXPIRATION_SECONDS)));
        return token;
    }

    public Long validateToken(String token) {
        TokenData data = tokenStore.get(token);
        if (data == null || data.expiration.isBefore(Instant.now())) {
            tokenStore.remove(token);
            return null;
        }
        return data.userId;
    }

    private record TokenData(Long userId, Instant expiration) {}
}

