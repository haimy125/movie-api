package com.movie.service.user.impl;

import com.movie.dto.NotificationDTO;
import com.movie.entity.Notification;
import com.movie.entity.User;
import com.movie.repository.admin.NotificationRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.service.user.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<NotificationDTO> getByUser(Long id, Pageable pageable) {
        List<NotificationDTO> UserDTOS = new ArrayList<>();
        // tìm kiếm tất cả người dùng
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        List<Notification> list = notificationRepository.findByUser(user, pageable);
        // chuyển đổi sang DTO
        for (Notification item : list) {
            NotificationDTO UserDTO = modelMapper.map(item, NotificationDTO.class);
            UserDTOS.add(UserDTO);
        }
        return UserDTOS;
    }

    @Override
    public List<NotificationDTO> getByUser(Long id) {
        List<NotificationDTO> UserDTOS = new ArrayList<>();
        // tìm kiếm tất cả người dùng
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        List<Notification> list = notificationRepository.findByUser(user);
        if (list.size() == 0)
            throw new RuntimeException("Không có thông báo nào");
        // chuyển đổi sang DTO
        for (Notification item : list) {
            NotificationDTO UserDTO = modelMapper.map(item, NotificationDTO.class);
            UserDTOS.add(UserDTO);
        }
        return UserDTOS;
    }

    @Override
    public int totalItems() {
        return (int) notificationRepository.count();
    }
}
