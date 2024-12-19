package com.movie.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Lớp tiện ích dùng để xử lý các token JWT.
 * Cung cấp các phương thức để tạo, phân tích và xác thực token.
 */
@Component
public class TokenUtil {

    /**
     * Khóa bí mật được sử dụng để ký các token JWT.
     */
     private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Tạo một token JWT với ID người dùng và thời gian hết hạn.
     *
     * @param userid           ID của người dùng sẽ được thêm vào payload của token.
     * @param expirationMillis Thời gian hiệu lực của token tính bằng mili giây.
     * @return Token JWT đã được ký dưới dạng {@code String}.
     */
    public static String generateToken1(String userid, long expirationMillis) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMillis);
        String data = userid;
        return Jwts.builder()
                .setSubject(data)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String generateRefreshToken1(String subject, long refreshExpirationMillis) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMillis))
                .claim("type", "refresh") // Đánh dấu loại token
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Trích xuất ID người dùng từ một token JWT.
     *
     * @param token Token JWT cần trích xuất ID người dùng.
     * @return ID người dùng được nhúng trong token.
     */
    public static String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Xác thực một token JWT bằng cách kiểm tra chữ ký và thời gian hết hạn.
     *
     * @param token Token JWT cần xác thực.
     * @return {@code true} nếu token hợp lệ; {@code false} nếu không hợp lệ.
     */
    public static boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static Claims validateTokenClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Hàm tạo Token (chung cho AccessToken và RefreshToken)
    public static String generateToken(String userId, long expirationMillis, String tokenType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        // Tạo và ký token JWT
        return Jwts.builder()
                .setSubject(userId) // Gắn userId làm subject
                .claim("type", tokenType) // Thêm loại token (AccessToken/RefreshToken)
                .setIssuedAt(now) // Thời điểm tạo token
                .setExpiration(expiryDate) // Thời hạn hết hạn
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Hàm tạo AccessToken
    public static String generateAccessToken(String userId, long expirationMillis) {
        return generateToken(userId, expirationMillis, "AccessToken");
    }

    // Hàm tạo RefreshToken
    public static String generateRefreshToken(String userId, long expirationMillis) {
        return generateToken(userId, expirationMillis, "RefreshToken");
    }

    private ResponseCookie createCookie(String token, long maxAgeMillis) {
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true) // Bảo vệ cookie khỏi JavaScript (giảm rủi ro XSS)
                .secure(true)   // Chỉ gửi cookie qua HTTPS
                .path("/")      // Đường dẫn cookie
                .maxAge(maxAgeMillis / 1000) // maxAge tính bằng giây
                .build();
    }
}
