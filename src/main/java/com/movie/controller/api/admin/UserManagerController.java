package com.movie.controller.api.admin;

import com.movie.config.TokenUtil;
import com.movie.response.UserResponse;
import com.movie.dto.UserDTO;
import com.movie.service.admin.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public UserResponse getAll(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        UserResponse result = new UserResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(userService.getAll(pageable));
        result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyname")
    public UserResponse getByName(@RequestParam("name") String name, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        UserResponse result = new UserResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(userService.getByName(name, pageable));
        result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyrole")
    public UserResponse getByRole(@RequestParam("roleid") Long roleid, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        UserResponse result = new UserResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(userService.getByRole(roleid, pageable));
        result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserDTO userDTO, BindingResult result, HttpServletRequest request) {

        try {
            // Lấy token từ header
            String token = request.getHeader("Authorization");

            System.out.println(token);

            // Kiểm tra token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid");
            }

            // Loại bỏ prefix "Bearer "
            token = token.substring(7);

            // Xác thực token (giả sử bạn có lớp TokenUtils để xử lý)
            if (!TokenUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Xử lý lỗi validation từ DTO
            if (result.hasErrors()) {
                Map<String, String> errors = result.getFieldErrors().stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage
                        ));
                return ResponseEntity.badRequest().body(errors);
            }

            UserDTO user = userService.create(userDTO);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {
            // Lấy token từ header
            String token = request.getHeader("Authorization");

            // Kiểm tra token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token bị thiếu hoặc không hợp lệ");
            }

            // Loại bỏ prefix "Bearer "
            token = token.substring(7);

            // Xác thực token (giả sử bạn có lớp TokenUtils để xử lý)
            if (!TokenUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
            }

//            // Xử lý lỗi validation từ DTO
//            if (result.hasErrors()) {
//                Map<String, String> errors = result.getFieldErrors().stream()
//                        .collect(Collectors.toMap(
//                                FieldError::getField,
//                                FieldError::getDefaultMessage
//                        ));
//                return ResponseEntity.badRequest().body(errors);
//            }

            // Gọi service để cập nhật user
            UserDTO updatedUser = userService.update(userDTO);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update/role")
    public ResponseEntity<?> update(@RequestParam("userid") Long userid, @RequestParam("roleid") Long roleid) {
        try {
            UserDTO user = userService.updateRole(userid, roleid);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/recharge")
    public ResponseEntity<?> recharge(@RequestParam("userid") Long userid, @RequestParam("point") Long point) {
        try {
            UserDTO user = userService.recharge(userid, point);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/napTien")
    public ResponseEntity<?> napTien(@RequestHeader("Authorization") String token, @RequestParam("point") Long point) {
        try {
            UserDTO user = userService.napTien(token, point);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok("Xoá thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
