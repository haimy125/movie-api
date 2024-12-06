package com.movie.service.login;

import com.movie.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface LoginService {
    UserDTO login(String username, String password);

    UserDTO registerUser(UserDTO user);

    UserDTO changePassword(Long id, String oldPassword, String newPassword, String confirmPassword);

    UserDTO checkUser(String username, String email);

    UserDTO forgetPassword(Long id, String newPassword, String confirmPassword);

    UserDTO checkUserByToken(String token);

    UserDTO userProfile(Long id);

    UserDTO userByid(Long id);

    void uploadAvatar(Long id, MultipartFile file);

    void updateInfo(Long id, String fullname, String email);
}
