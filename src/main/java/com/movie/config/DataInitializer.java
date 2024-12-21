package com.movie.config;

import com.movie.entity.Role;
import com.movie.entity.Schedule;
import com.movie.entity.User;
import com.movie.repository.admin.RoleRepository;
import com.movie.repository.admin.ScheduleRepository;
import com.movie.repository.admin.UserRepository;
import com.movie.service.FileStorageService;
import com.movie.utils.ByteArrayMultipartFile;
import jakarta.annotation.PostConstruct;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private FileStorageService fileStorageService;

    @PostConstruct
    public void init() {
        try {
            initRoles();
            User adminUser = initAdminUser();
            initSchedules(adminUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing data", e);
        }
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
    }

    private User initAdminUser() {
        return userRepository.findById(1L).orElseGet(() -> {
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

            // Lấy file avatar mặc định từ thư mục ngoài
            String defaultAvatarFileName = "Default_Avatar.png";

            try {
                // Lấy tên file đã lưu từ hệ thống
                String savedAvatarFileName = getFileFromExternalPathAndSave(defaultAvatarFileName);

                // Lưu tên file vào cơ sở dữ liệu
                newUser.setAvatar(savedAvatarFileName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to set default avatar", e);
            }

            return userRepository.save(newUser);
        });
    }

    public String getFileFromExternalPathAndSave(String fileName) throws IOException {
        Path path = Paths.get(System.getProperty("user.home"), "Documents", "uploads", fileName);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);

        // Tạo tên file mới hoặc sử dụng file cũ
        String savedFileName = fileStorageService.saveFile(content, fileName);
        return savedFileName;
    }


    private void initSchedules(User adminUser) {
        if (scheduleRepository.count() == 0) {
            String[] weekDays = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
            for (String day : weekDays) {
                Schedule schedule = new Schedule();
                schedule.setName(day);
                schedule.setDescription("Tạo mặc định!");
                schedule.setUserAdd(adminUser);
                schedule.setUserUpdate(adminUser);
                scheduleRepository.save(schedule);
            }
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
