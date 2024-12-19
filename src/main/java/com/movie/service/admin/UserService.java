package com.movie.service.admin;

import com.movie.dto.UserDTO;
import com.movie.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    
    List<UserDTO> getAll(Pageable pageable);

    List<UserDTO> getByName(String name, Pageable pageable);

    List<UserDTO> getByRole(Long role, Pageable pageable);

    UserDTO getById(Long id);

    UserDTO create(UserDTO user);

    UserDTO update(UserDTO user);

    UserDTO updateRole(Long id, Long roleid);

    UserDTO recharge(Long id, Long point);

    UserDTO napTien(String token, Long point);

    void delete(Long id);

    int totalItem();

    User findByEmail(String email);

    void updatePassword(User user, String newPassword);
}
