package com.movie.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Lớp xử lý ngoại lệ toàn cục cho ứng dụng.
 * Được sử dụng để bắt và xử lý các ngoại lệ cụ thể xảy ra trong quá trình thực thi.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Phương thức xử lý ngoại lệ cho các lỗi xác thực tham số phương thức.
     * Khi xảy ra ngoại lệ {@link MethodArgumentNotValidException}, phương thức này sẽ thu thập
     * các lỗi từ đối tượng ngoại lệ và trả về danh sách các trường bị lỗi kèm thông báo lỗi tương ứng.
     *
     * @param ex Ngoại lệ {@code MethodArgumentNotValidException} chứa thông tin về lỗi xác thực.
     * @return Đối tượng {@link ResponseEntity} chứa mã phản hồi HTTP 400 (BAD REQUEST) và danh sách lỗi.
     *         Mỗi lỗi được biểu diễn dưới dạng một cặp <tên trường, thông báo lỗi>.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
