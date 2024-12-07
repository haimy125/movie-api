package com.movie.controller.api.login;

import com.movie.config.TokenUtil;
import com.movie.controller.output.Notification_OutPut;
import com.movie.dto.RoleDTO;
import com.movie.dto.UserDTO;
import com.movie.service.login.LoginService;
import com.movie.service.user.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO user, BindingResult result) {
        try {
            // Xử lý lỗi validation từ DTO
            if (result.hasErrors()) {
                Map<String, String> errors = result.getFieldErrors().stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage
                        ));
                return ResponseEntity.badRequest().body(errors);
            }

            // Thiết lập các giá trị mặc định
            user.setPoint(0L);

            // Gán vai trò mặc định cho người dùng
            RoleDTO role = new RoleDTO();
            role.setId(String.valueOf(2L)); // ID cho vai trò mặc định (ví dụ: ROLE_USER)
            user.setRole(role);

            // Đọc ảnh đại diện mặc định
            Path defaultAvatarPath = Paths.get("src/main/resources/static/images/default_avatar.png");
            byte[] defaultAvatar = Files.readAllBytes(defaultAvatarPath);
            user.setAvatar(defaultAvatar);

            // Gọi dịch vụ đăng ký người dùng
            UserDTO registeredUser = login_Service.registerUser(user);
            registeredUser.setPassword(null); // Xóa mật khẩu khỏi phản hồi

            // Tạo token để sử dụng sau khi đăng ký
            long expirationMillis = 3600000; // 1 giờ
            String token = TokenUtil.generateToken(String.valueOf(registeredUser.getId()), expirationMillis);

            // Tạo cookie chứa token
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(false)
                    .maxAge(Duration.ofMillis(expirationMillis))
                    .sameSite("Strict")
                    .secure(true)
                    .path("/")
                    .domain("localhost")
                    .build();

            // Chuẩn bị phản hồi
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", registeredUser);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok().headers(headers).body(response);

        } catch (IOException e) {
            // Lỗi đọc file avatar mặc định
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể đọc file ảnh mặc định. Vui lòng kiểm tra cấu hình máy chủ.");
        } catch (RuntimeException e) {
            // Lỗi phát sinh từ logic xử lý (ví dụ: trùng tên đăng nhập)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Lỗi khác không xác định
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.");
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
    public ResponseEntity<?> updateinfo(@PathVariable String id, @RequestParam("fullname") String fullname, @RequestParam("email") String email) {
        try {
            login_Service.updateInfo(Long.valueOf(id), fullname, email);
            return ResponseEntity.ok("Thay đổi thông tin thành công");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> userProfile(@RequestParam("id") String id) {
        try {
            UserDTO user = login_Service.userProfile(Long.valueOf(id));
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
