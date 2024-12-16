package com.movie.service.admin.impl;

import com.movie.entity.RefreshTokens;
import com.movie.repository.admin.RefreshTokensRepository;
import com.movie.service.admin.RefreshTokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class RefreshTokensServiceImpl implements RefreshTokensService {

    @Autowired
    private RefreshTokensRepository refreshTokensRepository;

    /**
     * Tạo và lưu RefreshToken vào cơ sở dữ liệu.
     *
     * @param userId ID của người dùng
     * @param refreshToken Token cần lưu
     * @param expirationMillis Thời gian hết hạn của refresh token tính bằng mili giây
     */
    @Override
    public void create(Long userId, String refreshToken, long expirationMillis) {
        // Chuyển thời gian hết hạn từ mili giây sang LocalDateTime
        LocalDateTime expirationDate = LocalDateTime.now().plus(expirationMillis, ChronoUnit.MILLIS);

        // Tạo mới đối tượng RefreshTokens
        RefreshTokens refreshTokenEntity = new RefreshTokens();
        refreshTokenEntity.setUserId(userId);
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setExpirationDate(expirationDate);
        refreshTokenEntity.setCreatedAt(LocalDateTime.now());

        // Lưu RefreshTokenEntity vào cơ sở dữ liệu
        refreshTokensRepository.save(refreshTokenEntity);
    }

    /**
     * @param refreshToken
     * @return
     */
    @Override
    public RefreshTokens validateRefreshToken(String refreshToken) {
        // Tìm refresh token trong cơ sở dữ liệu
        RefreshTokens storedToken = refreshTokensRepository.findByRefreshToken(refreshToken);

        if (storedToken == null) {
            // Không tìm thấy token
            return null;
        }

        // Kiểm tra xem token đã hết hạn hay chưa
        if (storedToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            // Token đã hết hạn, có thể xóa khỏi DB nếu cần
            refreshTokensRepository.delete(storedToken);
            return null;
        }

        // Token hợp lệ
        return storedToken;
    }
}
