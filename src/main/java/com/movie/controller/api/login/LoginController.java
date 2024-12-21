package com.movie.controller.api.login;

import com.movie.config.TokenUtil;
import com.movie.dto.RoleDTO;
import com.movie.dto.UserDTO;
import com.movie.entity.RefreshTokens;
import com.movie.exceptions.InvalidCredentialsException;
import com.movie.request.LoginRequest;
import com.movie.response.NotificationResponse;
import com.movie.service.FileStorageService;
import com.movie.service.admin.RefreshTokensService;
import com.movie.service.login.LoginService;
import com.movie.service.user.NotificationService;
import jakarta.validation.Valid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RefreshTokensService refreshTokensService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Xác thực thông tin đăng nhập
            UserDTO user = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
            user.setPassword(null); // Không trả mật khẩu trong phản hồi

            // Thời gian sống của các token
            long accessTokenExpirationMillis = 10L * 24 * 60 * 60 * 1000; // 1 ngày
            long refreshTokenExpirationMillis = 30L * 24 * 60 * 60 * 1000; // 30 ngày

            // Tạo AccessToken và RefreshToken
            String accessToken = TokenUtil.generateAccessToken(String.valueOf(user.getId()), accessTokenExpirationMillis);
            String refreshToken = TokenUtil.generateRefreshToken(String.valueOf(user.getId()), refreshTokenExpirationMillis);

            // Kiểm tra nếu userId đã tồn tại trong cơ sở dữ liệu
            if (!refreshTokensService.existsByUserId(user.getId())) {
                // Lưu RefreshToken vào cơ sở dữ liệu
                refreshTokensService.create(user.getId(), refreshToken, refreshTokenExpirationMillis);
            }

            // Tạo cookie chứa AccessToken
            ResponseCookie cookie = createCookie(accessToken, accessTokenExpirationMillis);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            // Chuẩn bị phản hồi
            Map<String, Object> response = new HashMap<>();
            response.put("token", accessToken);
            response.put("user", user);

            // Trả về phản hồi thành công
            return ResponseEntity.ok().headers(headers).body(response);

        } catch (InvalidCredentialsException e) {
            // Xử lý lỗi thông tin đăng nhập
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tên đăng nhập hoặc mật khẩu không hợp lệ.");
        } catch (Exception e) {
            // Xử lý các lỗi khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi trong quá trình đăng nhập.");
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO user, BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = result.getFieldErrors().stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return ResponseEntity.badRequest().body(errors);
            }

            Date currentDate = Date.valueOf(LocalDate.now());
            user.setTimeAdd(currentDate);
            user.setPoint(0L);
            user.setRole(getDefaultRole());

            // Lấy file avatar mặc định từ thư mục ngoài
            String defaultAvatarFileName = "Default_Avatar.png";

            try {
                // Lấy tên file đã lưu từ hệ thống
                String savedAvatarFileName = getFileFromExternalPathAndSave(defaultAvatarFileName);

                // Lưu tên file vào cơ sở dữ liệu
                user.setAvatar(savedAvatarFileName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to set default avatar", e);
            }

            user.setFullname(user.getFullname());

            loginService.registerUser(user);
            return ResponseEntity.ok().body("Đăng ký thành công. Vui lòng xác minh email để kích hoạt tài khoản.");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi không xác định trong quá trình đăng ký. Vui lòng thử lại sau.");
        }
    }

    public String getFileFromExternalPathAndSave(String fileName) throws IOException {
        Path path = Paths.get(System.getProperty("user.home"), "Documents", "uploads", fileName);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);

        // Tạo tên file mới hoặc sử dụng file cũ
        String savedFileName = fileStorageService.saveFile(content, fileName);
        return savedFileName;
    }

    @GetMapping("/api/checktoken")
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

    @PostMapping("/api/avatar/{id}")
    public ResponseEntity<?> avatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile avatar) {
        try {
            loginService.uploadAvatar(id, avatar);
            return ResponseEntity.ok("Thay đổi ảnh thành công");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/changepassword/{id}")
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

    @PostMapping("/api/updateinfo/{id}")
    public ResponseEntity<?> updateinfo(@PathVariable String id, @RequestParam("fullname") String fullname, @RequestParam("email") String email) {
        try {
            loginService.updateInfo(Long.valueOf(id), fullname, email);
            return ResponseEntity.ok("Thay đổi thông tin thành công");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/user/profile")
    public ResponseEntity<?> userProfile(@RequestParam("id") String id) {
        try {
            UserDTO user = loginService.userProfile(Long.valueOf(id));
            user.setPassword(null); // bảo vệ pass khi trả về từ api
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/api/view/{id}")
//    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
//        UserDTO fileDTO = loginService.userByid(id);
//        if (fileDTO != null && fileDTO.getAvatar() != null) {
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDTO.getId() + "\"")
//                    .contentType(MediaType.IMAGE_PNG)
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .contentType(MediaType.IMAGE_GIF)
//                    .body(fileDTO.getAvatar());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/api/view/{id}")
    public ResponseEntity<?> viewFile(@PathVariable Long id) {
        try {
            // Lấy thông tin user từ service
            UserDTO userDTO = loginService.userByid(id);

            // Kiểm tra nếu không tìm thấy user hoặc không có avatar
            if (userDTO == null || userDTO.getAvatar() == null || userDTO.getAvatar().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Tải file từ đường dẫn lưu trong cơ sở dữ liệu sử dụng loadFile
            Resource fileResource = fileStorageService.loadFile(userDTO.getAvatar());

            // Kiểm tra nếu file không tồn tại hoặc không đọc được
            if (fileResource == null || !fileResource.exists() || !fileResource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Xác định MIME type của file
            Path filePath = Paths.get(userDTO.getAvatar()).normalize();
            String mimeType = Files.probeContentType(filePath);
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // MIME mặc định nếu không xác định được
            }

            // Trả về ResponseEntity với dữ liệu file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(fileResource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body("URL không hợp lệ.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tải file.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/notificaion/user/{id}")
    public NotificationResponse getAll(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        NotificationResponse result = new NotificationResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(notificationService.getByUser(id, pageable));
        result.setTotalPage((int) Math.ceil((double) (notificationService.totalItems()) / limit));
        return result;
    }

    @PostMapping("/api/changepassword/user")
    public ResponseEntity<?> changepassword(@RequestParam("id") Long id, @RequestParam("newPassword") String newPassword
            , @RequestParam("confirmPassword") String confirmPassword) {
        try {
            UserDTO user = loginService.forgetPassword(id, newPassword, confirmPassword);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/find/user")
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
        try {
            return Files.readAllBytes(defaultAvatarPath);
        } catch (IOException e) {
            throw new IOException("Cannot read default avatar file", e);
        }
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

    @PostMapping("/api/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> tokenRequest) {
        try {
            String refreshToken = tokenRequest.get("refreshToken");
            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.badRequest().body("Refresh token is required.");
            }

            // Gọi validateRefreshToken
            RefreshTokens storedToken = refreshTokensService.validateRefreshToken(refreshToken);
            if (storedToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token.");
            }

            // Tạo AccessToken mới
            long accessTokenExpirationMillis = 1L * 24 * 60 * 60 * 1000; // 1 ngày
            String newAccessToken = TokenUtil.generateAccessToken(String.valueOf(storedToken.getUserId()), accessTokenExpirationMillis);

            // Tạo cookie chứa AccessToken
            ResponseCookie cookie = createCookie(newAccessToken, accessTokenExpirationMillis);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            // Trả về AccessToken mới
            Map<String, String> response = new HashMap<>();
            response.put("token", newAccessToken);
            return ResponseEntity.ok().headers(headers).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during token refresh.");
        }
    }
}