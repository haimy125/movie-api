package com.movie.config;

import com.movie.entity.Role;
import com.movie.entity.Schedule;
import com.movie.entity.User;
import com.movie.repository.admin.RoleRepository;
import com.movie.repository.admin.ScheduleRepository;
import com.movie.repository.admin.UserRepository;
import jakarta.annotation.PostConstruct;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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

            User user = userRepository.findById(1L).orElseGet(() -> {
                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN không tồn tại"));

                User newUser = new User();
                newUser.setUsername("admin");
                newUser.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
                newUser.setPoint(0L);
                newUser.setEmail("admin@gmail.com");
                newUser.setActive(true);
                newUser.setRole(adminRole);

                try {
                    newUser.setAvatar(loadDefaultAvatar());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load default avatar", e);
                }

                return userRepository.save(newUser);
            });

//             Kiểm tra và tạo các Schedule nếu chưa có
            if (scheduleRepository.count() == 0) {
                int date = 2;
                for (int i = 0; i < 7; i++) {
                    Schedule schedule = new Schedule();
                    if (i == 6) {
                        schedule.setName("Chủ nhật");
                        schedule.setDescription("Tạo mặc định!");
                        schedule.setUserAdd(user);
                        schedule.setUserUpdate(user);
                        scheduleRepository.save(schedule);
                    } else {
                        schedule.setName("Thứ " + date);
                        schedule.setDescription("Tạo mặc định!");
                        schedule.setUserAdd(user);
                        schedule.setUserUpdate(user);
                        scheduleRepository.save(schedule);
                    }
                    date++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing data", e);
        }
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
