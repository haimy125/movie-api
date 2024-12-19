package com.movie.controller.api.login;

import com.movie.config.TokenUtil;
import com.movie.entity.User;
import com.movie.service.EmailService;
import com.movie.service.TokenService;
import com.movie.service.admin.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {
    private final EmailService emailService;
    private final UserService userService;

    public PasswordResetController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        User user = userService.findByEmail(email);
        System.out.println(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không tồn tại.");
        }

        String token = TokenUtil.generateResetToken(user);
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        return ResponseEntity.ok("Một liên kết khôi phục mật khẩu đã được gửi đến email của bạn.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload) {
        try {
            String token = payload.get("token");
            String newPassword = payload.get("newPassword");

            if (token == null || newPassword == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token và mật khẩu không được để trống.");
            }

            String email = TokenUtil.validateResetToken(token);
            User user = userService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không hợp lệ.");
            }

            userService.updatePassword(user, newPassword);
            return ResponseEntity.ok("Mật khẩu đã được thay đổi thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }
}
