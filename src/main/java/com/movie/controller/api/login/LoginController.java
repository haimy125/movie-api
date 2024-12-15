package com.movie.controller.api.login;

import com.movie.config.TokenUtil;
import com.movie.dto.RoleDTO;
import com.movie.dto.UserDTO;
import com.movie.exceptions.InvalidCredentialsException;
import com.movie.request.LoginRequest;
import com.movie.response.NotificationResponse;
import com.movie.service.login.LoginService;
import com.movie.service.user.NotificationService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDTO user = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
            user.setPassword(null);

            long expirationMillis = 30L * 24 * 60 * 60 * 1000;
            String mainToken = TokenUtil.generateToken(String.valueOf(user.getId()), expirationMillis);
            String refreshToken = TokenUtil.generateRefreshToken(String.valueOf(user.getId()), expirationMillis * 2);

            ResponseCookie cookie = createCookie(mainToken, expirationMillis);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            Map<String, Object> response = new HashMap<>();
            response.put("token", mainToken);
            response.put("refreshToken", refreshToken);
            response.put("user", user);

            return ResponseEntity.ok().headers(headers).body(response);

        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO user, BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = result.getFieldErrors().stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return ResponseEntity.badRequest().body(errors);
            }

            user.setPoint(0L);
            user.setRole(getDefaultRole());
            user.setAvatar(loadDefaultAvatar());

            UserDTO registeredUser = loginService.registerUser(user);
            registeredUser.setPassword(null);

            long expirationMillis =  3600000; // 1 hour
            String token = generateToken(registeredUser.getId(), expirationMillis);
            ResponseCookie cookie = createCookie(token, expirationMillis);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());


            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", registeredUser);

            return ResponseEntity.ok().headers(headers).body(response);


        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cannot read default avatar file. Please check server configuration.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unknown error occurred during registration. Please try again later.");
        }
    }

    @GetMapping("/checktoken")
    public ResponseEntity<?> checkToken(@RequestParam("token") String token) {
        try {
            UserDTO user = loginService.checkUserByToken(token);
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
            loginService.uploadAvatar(id, avatar);
            return ResponseEntity.ok("Thay đổi ảnh thành công");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changepassword/{id}")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestParam("password") String currentPassword,
            @RequestParam("newpassword") String newPassword,
            @RequestParam("confirmpassword") String confirmPassword) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body("Mật khẩu mới và xác nhận mật khẩu không khớp.");
            }

            UserDTO userDTO = loginService.changePassword(id, currentPassword, newPassword, confirmPassword);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Thay đổi mật khẩu thành công.");
            response.put("user", userDTO);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/updateinfo/{id}")
    public ResponseEntity<?> updateinfo(@PathVariable String id, @RequestParam("fullname") String fullname, @RequestParam("email") String email) {
        try {
            loginService.updateInfo(Long.valueOf(id), fullname, email);
            return ResponseEntity.ok("Thay đổi thông tin thành công");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> userProfile(@RequestParam("id") String id) {
        try {
            UserDTO user = loginService.userProfile(Long.valueOf(id));
            user.setPassword(null); // bảo vệ pass khi trả về từ api
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        UserDTO fileDTO = loginService.userByid(id);
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
    public NotificationResponse getAll(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        NotificationResponse result = new NotificationResponse();
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
            UserDTO user = loginService.forgetPassword(id, newPassword, confirmPassword);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find/user")
    public ResponseEntity<?> findUser(@RequestParam("username") String username, @RequestParam("email") String email) {
        try {
            UserDTO user = loginService.checkUser(username, email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private RoleDTO getDefaultRole() {
        RoleDTO role = new RoleDTO();
        role.setId(2L); // Default role ID (e.g., ROLE_USER)
        return role;
    }

    private byte[] loadDefaultAvatar() throws IOException {
        Path defaultAvatarPath = Paths.get("src/main/resources/static/images/Default_Avatar.png");
        try{
            return Files.readAllBytes(defaultAvatarPath);
        }catch (IOException e){
            throw new IOException("Cannot read default avatar file",e);
        }
    }

    private String generateToken(Long userId, long expirationMillis) {
        return TokenUtil.generateToken(String.valueOf(userId), expirationMillis);
    }
    private ResponseCookie createCookie(String token, long expirationMillis) {
        return ResponseCookie.from("token", token)
                .httpOnly(true)
                .maxAge(Duration.ofMillis(expirationMillis))
                .sameSite("Strict")
                .secure(true)
                .path("/")
                .build();
    }
}