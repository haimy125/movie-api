package com.movie.service.admin;

import com.movie.entity.RefreshTokens;

public interface RefreshTokensService {
    void create(Long userId, String refreshToken, long expirationMillis);
    RefreshTokens validateRefreshToken(String refreshToken);
    boolean existsByUserId(Long userId);
}
