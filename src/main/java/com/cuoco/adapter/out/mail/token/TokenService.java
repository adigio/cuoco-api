package com.cuoco.adapter.out.mail.token;

public interface TokenService {
    String generateToken(String email);
    String validateTokenAndGetEmail(String token);
}

