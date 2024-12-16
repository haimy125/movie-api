package com.movie.config;

import com.movie.entity.Role;
import com.movie.entity.Schedule;
import com.movie.entity.User;
import com.movie.repository.admin.RoleRepository;
import com.movie.repository.admin.ScheduleRepository;
import com.movie.repository.admin.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        try {
            // Kiểm tra và tạo các Role nếu chưa có
            if (roleRepository.count() == 0) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);

                Role userRole = new Role();
                userRole.setName("ROLE_USER");
                roleRepository.save(userRole);
            }

            // Kiểm tra và tạo người dùng nếu chưa có
//            User user = userRepository.findById(1L)
//                    .orElseGet(() -> {
//                        User newUser = new User();
//                        newUser.setUsername("admin");
//                        newUser.setPassword("123456"); // Thêm mật khẩu mặc định
//                        newUser.setEmail("admin@gmail.com");
//                        userRepository.save(newUser);
//                        return newUser;
//                    });

            // Kiểm tra và tạo các Schedule nếu chưa có
//            if (scheduleRepository.count() == 0) {
//                int date = 2;
//                for (int i = 0; i < 7; i++) {
//                    Schedule schedule = new Schedule();
//                    if (i == 6) {
//                        schedule.setName("Chủ nhật");
//                        schedule.setDescription("Tạo mặc định!");
//                        schedule.setUserAdd(user);
//                        schedule.setUserUpdate(user);
//                        scheduleRepository.save(schedule);
//                    } else {
//                        schedule.setName("Thứ " + date);
//                        schedule.setDescription("Tạo mặc định!");
//                        schedule.setUserAdd(user);
//                        schedule.setUserUpdate(user);
//                        scheduleRepository.save(schedule);
//                    }
//                    date++;
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing data", e);
        }
    }
}
