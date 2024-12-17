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
}
