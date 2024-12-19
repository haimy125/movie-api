package com.movie.service;

import com.movie.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {

    private final Map<String, String> tokenStorage = new HashMap<>();
    private final long EXPIRATION_TIME = 3600000; // 1 hour

    public String generateResetToken(User user) {
        String token = UUID.randomUUID().toString();
        tokenStorage.put(token, user.getEmail());
        return token;
    }

    public String validateToken(String token) {
        String email = tokenStorage.get(token);
        if (email == null) {
            throw new IllegalArgumentException("Token không hợp lệ hoặc đã hết hạn.");
        }
        return email;
    }

    public void expireToken(String token) {
        tokenStorage.remove(token);
    }
}
