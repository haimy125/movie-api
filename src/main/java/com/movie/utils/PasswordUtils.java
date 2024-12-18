package com.movie.utils;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
     * Băm mật khẩu bằng BCrypt.
     *
     * @param plainPassword Mật khẩu gốc.
     * @return Mật khẩu đã được băm.
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Kiểm tra mật khẩu đầu vào với mật khẩu đã băm.
     *
     * @param plainPassword Mật khẩu gốc.
     * @param hashedPassword Mật khẩu đã được băm.
     * @return true nếu mật khẩu khớp, false nếu không khớp.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static boolean isBCryptHash(String passwordHash) {
        // Kiểm tra chuỗi không null và có độ dài đủ
        if (passwordHash == null || passwordHash.length() != 60) {
            return false;
        }
        // Kiểm tra định dạng bắt đầu bằng $2a$, $2b$, hoặc $2y$
        return passwordHash.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    }

}
