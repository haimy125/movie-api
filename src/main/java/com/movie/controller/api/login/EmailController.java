package com.movie.controller.api.login;

import com.movie.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String sendEmail() {
        try {
            emailService.sendEmail("recipient_email@example.com", "Tiêu đề Email", "Nội dung Email");
            return "Email đã được gửi thành công!";
        } catch (Exception e) {
            return "Có lỗi xảy ra khi gửi email: " + e.getMessage();
        }
    }
}
