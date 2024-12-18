package com.movie.service.admin.impl;

import com.movie.config.TokenUtil;
import com.movie.dto.UserDTO;
import com.movie.entity.Notification;
import com.movie.entity.Role;
import com.movie.entity.User;
import com.movie.repository.admin.NotificationRepository;
import com.movie.repository.admin.RoleRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.service.admin.UserService;
import com.movie.utils.PasswordUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // hiển thị tất cả người dùng
    @Override
    public List<UserDTO> getAll(Pageable pageable) {
        List<UserDTO> UserDTOS = new ArrayList<>();
        // tìm kiếm tất cả người dùng
        List<User> list = userRepository.findAll(pageable).getContent();
        // chuyển đổi sang DTO
        for (User User : list) {
            UserDTO UserDTO = modelMapper.map(User, UserDTO.class);
            UserDTOS.add(UserDTO);
        }
        return UserDTOS;
    }

    @Override
    public List<UserDTO> getByName(String name, Pageable pageable) {
        List<UserDTO> UserDTOS = new ArrayList<>();
        // tìm kiếm tất cả người dùng
        List<User> list = userRepository.findByUsernameLike("%" + name + "%", pageable);
        // chuyển đổi sang DTO
        for (User User : list) {
            UserDTO UserDTO = modelMapper.map(User, UserDTO.class);
            UserDTOS.add(UserDTO);
        }
        return UserDTOS;
    }

    @Override
    public List<UserDTO> getByRole(Long role, Pageable pageable) {
        List<UserDTO> UserDTOS = new ArrayList<>();
        // tìm kiếm theo quyền hạn id
        Role Role = roleRepository.findById(role).orElseThrow(() -> new RuntimeException("Không có quyền này!"));
        // tìm kiếm tất cả người dùng
        List<User> list = userRepository.findByRole(Role, pageable);
        // chuyển đổi sang DTO
        for (User User : list) {
            UserDTO UserDTO = modelMapper.map(User, UserDTO.class);
            UserDTOS.add(UserDTO);
        }
        return UserDTOS;
    }

    @Override
    public UserDTO getById(Long id) {
        try {
            // tìm kiếm người dùng theo id
            User User = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            // chuyển đổi sang DTO
            UserDTO userDTO = modelMapper.map(User, UserDTO.class);
            return userDTO;
        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("Bạn chưa nhập dữ liệu!");
        }

        // Kiểm tra role trước khi xử lý
        if (userDTO.getRole() == null || userDTO.getRole().getId() == null) {
            throw new IllegalArgumentException("Role không được để trống và phải có id!");
        }

        try {
            System.out.println("role id: " + userDTO.getRole().getId());

            // Map UserDTO sang User entity
            User user = modelMapper.map(userDTO, User.class);
            user.setAvatar(loadDefaultAvatar());
            user.setPassword(PasswordUtils.hashPassword(userDTO.getPassword()));

            // Lấy Role từ database
            Role role = roleRepository.findById(userDTO.getRole().getId())
                    .orElseThrow(() -> new RuntimeException("Không có quyền này!"));
            role.setId(userDTO.getRole().getId());

            user.setRole(role);

            // Lưu user vào database
            userRepository.save(user);

            // Map ngược lại sang UserDTO để trả về
            UserDTO savedUserDTO = modelMapper.map(user, UserDTO.class);
            return savedUserDTO;

        } catch (IllegalArgumentException e) {
            throw e; // Bảo toàn lỗi cụ thể
        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi xử lý dữ liệu: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định xảy ra: " + e.getMessage(), e);
        }
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        try {
            if (userDTO == null)
                throw new RuntimeException("Bạn chưa nhập dữ liệu!");
            User user = userRepository.findById(Long.valueOf(userDTO.getId())).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            Role role = roleRepository.findById(Long.valueOf(userDTO.getRole().getId())).orElseThrow(() -> new RuntimeException("Không có quyền này!"));

            user.setRole(role);
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());

            userRepository.save(user);
            UserDTO UserDTO = modelMapper.map(user, UserDTO.class);
            return UserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra");
        }

    }

    @Override
    public UserDTO updateRole(Long id, Long roleid) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            Role role = roleRepository.findById(roleid).orElseThrow(() -> new RuntimeException("Không có quyền này!"));
            user.setRole(role);
            userRepository.save(user);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            return userDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra");
        }
    }

    @Override
    public UserDTO recharge(Long id, Long point) {
        try {
            if (point == null)
                throw new RuntimeException("Bạn chưa nhập dữ liệu!");
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            user.setPoint(user.getPoint() + point);

            userRepository.save(user);
            Notification notificationEntity = new Notification();
            notificationEntity.setUser(user);
            notificationEntity.setTimeAdd(Date.valueOf(LocalDate.now()));
            notificationEntity.setContent("Đã nạp " + point + " xu \n số dư hiện tại là: " + user.getPoint() + " xu.");
            notificationEntity.setStatus(true);
            notificationRepository.save(notificationEntity);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            return userDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra");
        }
    }

    /**
     * @param token
     * @param point
     * @return
     */
    @Override
    public UserDTO napTien(String token, Long point) {
        try {
            if (point == null)
                throw new RuntimeException("Bạn chưa nhập dữ liệu!");
            // Xử lý token để lấy user ID
            String jwt = token.replace("Bearer ", "");
            String userId = TokenUtil.getUserIdFromToken(jwt);
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            user.setPoint(user.getPoint() + point);
            user.setPassword(null);
            userRepository.save(user);

            Notification notificationEntity = new Notification();
            notificationEntity.setUser(user);
            notificationEntity.setTimeAdd(Date.valueOf(LocalDate.now()));
            notificationEntity.setTimeUpdate(Date.valueOf(LocalDate.now()));
            notificationEntity.setContent("Đã nạp " + point + " xu \n số dư hiện tại là: " + user.getPoint() + " xu.");
            notificationEntity.setStatus(true);
            notificationRepository.save(notificationEntity);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            return userDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra");
        }
    }


    @Override
    public void delete(Long id) {
        try {
            if (id == null)
                throw new RuntimeException("Bạn chưa nhập dữ liệu!");
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            userRepository.deleteById(Long.valueOf(user.getId()));
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra");
        }
    }

    @Override
    public int totalItem() {
        return (int) userRepository.count();
    }

    private byte[] loadDefaultAvatar() throws IOException {
        Path defaultAvatarPath = Paths.get("src/main/resources/static/images/Default_Avatar.png");
        try {
            return Files.readAllBytes(defaultAvatarPath);
        } catch (IOException e) {
            throw new IOException("Cannot read default avatar file", e);
        }
    }
}
