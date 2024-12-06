package com.movie.config;

public class Utils {

    public static void main(String[] args) {

        // Tạo token
        String token = TokenUtil.generateToken(1 + "", 60000); // Token có hiệu lực trong 1 phút
        System.out.println("Generated Token: " + token);

        // Xác thực token
        boolean isValid = TokenUtil.validateToken(token);
        System.out.println("Is token valid? " + isValid);

        // Lấy userId từ token
        if (isValid) {
            String userId = TokenUtil.getUserIdFromToken(token);
            System.out.println("User ID from token: " + userId);
        }
    }
}
