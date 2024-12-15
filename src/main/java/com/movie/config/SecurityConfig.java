package com.movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Lớp cấu hình bảo mật cho ứng dụng.
 * Thiết lập các cấu hình liên quan đến bảo mật, bao gồm CORS và CSRF.
 */
@Configuration
public class SecurityConfig {

    private static final String FRONTEND_URL = "http://localhost:3000";
    private static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private static final List<String> ALLOWED_HEADERS = List.of("Origin", "Content-Type", "Accept", "Authorization");

    /**
     * Định nghĩa chuỗi bộ lọc bảo mật cho ứng dụng.
     *
     * - Tắt CSRF để hỗ trợ các ứng dụng không yêu cầu CSRF protection.
     * - Cho phép tất cả các yêu cầu HTTP mà không cần xác thực.
     * - Kích hoạt hỗ trợ CORS để cho phép giao tiếp giữa frontend và backend.
     *
     * @param http Đối tượng {@link HttpSecurity} để cấu hình bảo mật.
     * @return Đối tượng {@link SecurityFilterChain} đã được cấu hình.
     * @throws Exception Nếu có lỗi trong quá trình cấu hình.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .cors(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Cấu hình CORS (Cross-Origin Resource Sharing) để cho phép giao tiếp từ các nguồn khác nhau.
     *
     * - Cho phép nguồn gốc từ URL frontend (ví dụ: http://localhost:3000).
     * - Hỗ trợ các phương thức HTTP như GET, POST, PUT, DELETE, và OPTIONS.
     * - Hỗ trợ các header HTTP cần thiết như Authorization.
     * - Cho phép gửi cookie qua các yêu cầu.
     *
     * @return Đối tượng {@link CorsConfigurationSource} chứa cấu hình CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(FRONTEND_URL));
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
