package com.movie.service.admin.impl;

import com.movie.entity.RefreshTokens;
import com.movie.repository.admin.RefreshTokensRepository;
import com.movie.service.admin.RefreshTokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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

        // Tạo mới đối tượng RefreshTokens
        RefreshTokens refreshTokenEntity = new RefreshTokens();
        refreshTokenEntity.setUserId(userId);
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setExpirationDate(Date.valueOf(LocalDate.now()));
        refreshTokenEntity.setCreatedAt(Date.valueOf(LocalDate.now()));

        // Lưu RefreshTokenEntity vào cơ sở dữ liệu
        refreshTokensRepository.save(refreshTokenEntity);
    }

    /**
     * Xác thực refresh token.
     *
     * @param refreshToken Chuỗi refresh token cần xác thực.
     * @return RefreshTokens nếu token hợp lệ, null nếu không hợp lệ hoặc đã hết hạn.
     */
    @Override
    public RefreshTokens validateRefreshToken(String refreshToken) {
        // Tìm kiếm token trong cơ sở dữ liệu
        RefreshTokens storedToken = refreshTokensRepository.findByRefreshToken(refreshToken);

        // Nếu token không tồn tại
        if (storedToken == null) {
            return null;
        }

        // Kiểm tra hạn sử dụng của token
        boolean isTokenExpired = storedToken.getExpirationDate().before(Date.valueOf(LocalDate.now()));

        if (isTokenExpired) {
            // Xóa token đã hết hạn khỏi cơ sở dữ liệu
            refreshTokensRepository.delete(storedToken);
            return null;
        }

        // Trả về token hợp lệ
        return storedToken;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public boolean existsByUserId(Long userId) {
        return refreshTokensRepository.existsByUserId(userId);
    }
}
