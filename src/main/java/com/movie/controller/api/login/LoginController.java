package com.movie.controller.api.login;

import com.movie.config.TokenUtil;
import com.movie.controller.output.Notification_OutPut;
import com.movie.dto.RoleDTO;
import com.movie.dto.UserDTO;
import com.movie.service.login.LoginService;
import com.movie.service.user.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService login_Service;

    @Autowired
    private NotificationService notificationService;

    // Đăng nhập tai khoản
    @PostMapping("/login")
    public ResponseEntity<?> login(String username, String password) {
        try {
            //Đăng nhập
            UserDTO user = login_Service.login(username, password);
            user.setPassword(null);
            //Tạo token
            long expirationMillis = 3600000;
            String mainToken = TokenUtil.generateToken(user.getId() + "", expirationMillis);
            Map<String, Object> response = new HashMap<>();
            // Tạo cookie
            ResponseCookie cookie = ResponseCookie.from("token", mainToken)
                    .httpOnly(false)
                    .maxAge(Duration.ofSeconds(3600))
                    .sameSite("Strict")
                    .secure(true)
                    .path("/")
                    .build();
            // Tạo header
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            response.put("token", mainToken);
            response.put("user", user);
            // Trả về token
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Đăng ký tài khoản
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO user) {
        try {
            // Đặt thời gian hiện tại
            Date currentDate = Date.valueOf(LocalDate.now());

            // Thiết lập vai trò mặc định
            RoleDTO role = new RoleDTO();
            role.setId(2L);

            // Thiết lập các thông tin mặc định
            user.setTimeAdd(currentDate);
            user.setPoint(0L);
            user.setRole(role);

            // Avatar mặc định (byte[])
            Path defaultAvatarPath = Paths.get("src/main/resources/static/images/default_avatar.png");
            byte[] defaultAvatar = Files.readAllBytes(defaultAvatarPath);
            user.setAvatar(defaultAvatar);

            // Đăng ký người dùng
            UserDTO user_dto = login_Service.registerUser(user);
            user_dto.setPassword(null);

            // Tạo token và cookie
            long expirationMillis = 3600000;
            String mainToken = TokenUtil.generateToken(user_dto.getId() + "", expirationMillis);
            Map<String, Object> response = new HashMap<>();
            ResponseCookie cookie = ResponseCookie.from("token", mainToken)
                    .httpOnly(false)
                    .maxAge(Duration.ofSeconds(3600000))
                    .sameSite("Strict")
                    .secure(true)
                    .path("/")
                    .domain("localhost")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            response.put("token", mainToken);
            response.put("user", user);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/checktoken")
    public ResponseEntity<?> checkToken(@RequestParam("token") String token) {
        try {
            UserDTO user = login_Service.checkUserByToken(token);
            user.setPassword(null);
            user.setAvatar(null);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/avatar/{id}")
    public ResponseEntity<?> avatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile avatar) {
        try {
            login_Service.uploadAvatar(id, avatar);
            return ResponseEntity.ok("Thay đổi ảnh thành công");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changepassword/{id}")
    public ResponseEntity<?> avatar(@PathVariable Long id, @RequestParam("password") String password, @RequestParam("newpassword") String newpassword, @RequestParam("confirmpassword") String confirmpassword) {
        try {
            UserDTO user_dto = login_Service.changePassword(id, password, newpassword, confirmpassword);
            return ResponseEntity.ok("Thay đổi mk thành công");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateinfo/{id}")
    public ResponseEntity<?> updateinfo(@PathVariable Long id, @RequestParam("fullname") String fullname, @RequestParam("email") String email) {
        try {
            login_Service.updateInfo(id, fullname, email);
            return ResponseEntity.ok("Thay đổi thông tin thành công");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/userprofile")
    public ResponseEntity<?> userProfile(@RequestParam("id") Long id) {
        try {
            UserDTO user = login_Service.userProfile(id);
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        UserDTO fileDTO = login_Service.userByid(id);
        if (fileDTO != null && fileDTO.getAvatar() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDTO.getId() + "\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentType(MediaType.IMAGE_GIF)
                    .body(fileDTO.getAvatar());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/notificaion/user/{id}")
    public Notification_OutPut getAll(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        Notification_OutPut result = new Notification_OutPut();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(notificationService.getByUser(id, pageable));
        result.setTotalPage((int) Math.ceil((double) (notificationService.totalItems()) / limit));
        return result;
    }

    @PostMapping("/changepassword/user")
    public ResponseEntity<?> changepassword(@RequestParam("id") Long id, @RequestParam("newPassword") String newPassword
            , @RequestParam("confirmPassword") String confirmPassword) {
        try {
            UserDTO user = login_Service.forgetPassword(id, newPassword, confirmPassword);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find/user")
    public ResponseEntity<?> findUser(@RequestParam("username") String username, @RequestParam("email") String email) {
        try {
            UserDTO user = login_Service.checkUser(username, email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
