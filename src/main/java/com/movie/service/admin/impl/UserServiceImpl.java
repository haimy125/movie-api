package com.movie.service.admin.impl;

import com.movie.dto.UserDTO;
import com.movie.entity.Notification;
import com.movie.entity.Role;
import com.movie.entity.User;
import com.movie.repository.admin.NotificationRepository;
import com.movie.repository.admin.RoleRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.service.admin.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        try {
            if (userDTO == null)
                throw new RuntimeException("Bạn chưa nhập dữ liệu!");
            User user = modelMapper.map(userDTO, User.class);
            Role role = roleRepository.findById(Long.valueOf(userDTO.getRole().getId())).orElseThrow(() -> new RuntimeException("Không có quyền này!"));
            user.setRole(role);
            userRepository.save(user);
            UserDTO userDTO1 = modelMapper.map(user, UserDTO.class);
            return userDTO1;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra");
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
            User User = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không có người dùng này!"));
            User.setPoint(User.getPoint() + point);
            userRepository.save(User);
            Notification notificationEntity = new Notification();
            notificationEntity.setUser(User);
            notificationEntity.setTimeAdd(Date.valueOf(LocalDate.now()));
            notificationEntity.setContent("Đã nạp " + point + " xu \n số dư hiện tại là: " + User.getPoint() + " xu.");
            notificationEntity.setStatus(true);
            notificationRepository.save(notificationEntity);
            UserDTO userDTO = modelMapper.map(User, UserDTO.class);
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
}
