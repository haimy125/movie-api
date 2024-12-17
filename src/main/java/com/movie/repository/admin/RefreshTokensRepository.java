package com.movie.repository.admin;

import com.movie.entity.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Long> {
    RefreshTokens findByRefreshToken(String refreshToken);
    boolean existsByUserId(Long userId);
}
