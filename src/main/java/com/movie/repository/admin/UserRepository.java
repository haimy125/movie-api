package com.movie.repository.admin;

import com.movie.entity.Role;
import com.movie.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // tim kiem theo username
    User findByUsername(String username);

    // tìm kiểm gần đúng theo username
    List<User> findByUsernameLike(String username, Pageable pageable);

    // tìm kiếm theo quyền hạn
    List<User> findByRole(Role role, Pageable pageable);

    // tìm kiếm theo email
    User findByEmail(String email);

    // tìm kiếm theo username và password
    User findByUsernameAndPassword(String username, String password);

    // tìm kiếm theo username và email
    User findByUsernameAndEmail(String username, String email);
}
