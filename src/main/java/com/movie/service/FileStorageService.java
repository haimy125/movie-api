package com.movie.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService() {
        // Tạo đường dẫn tới thư mục Documents/uploads
        this.uploadDir = Paths.get(System.getProperty("user.home"), "Documents", "uploads");

        // Tạo thư mục nếu chưa tồn tại
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục upload", e);
        }
    }

    // Lưu file vào thư mục
    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File trống");
        }

        try {
            // Tạo tên file duy nhất sử dụng UUID
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);

            // Lưu file vào thư mục
            file.transferTo(filePath.toFile());

            // Trả về tên file để lưu vào cơ sở dữ liệu
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + file.getOriginalFilename(), e);
        }
    }

    // Tải file
    public Resource loadFile(String fileName) {
        try {

            if (fileName.contains("..")) {
                throw new IllegalArgumentException("Tên file không hợp lệ: " + fileName);
            }

            Path filePath = uploadDir.resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Không thể đọc file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Lỗi khi tải file: " + fileName, e);
        }
    }

    public String saveFile(byte[] fileContent, String originalFileName) {
        if (fileContent == null || fileContent.length == 0) {
            throw new RuntimeException("File trống");
        }

        try {
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path filePath = uploadDir.resolve(fileName);
            Files.write(filePath, fileContent);
            return fileName; // Trả về tên file để lưu trong cơ sở dữ liệu
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + originalFileName, e);
        }
    }


    // Xóa file
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = uploadDir.resolve(fileName);
            return Files.deleteIfExists(filePath); // Trả về true nếu file đã bị xóa
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa file: " + fileName, e);
        }
    }

    // Liệt kê các file trong thư mục
    public List<String> listFiles() {
        try (Stream<Path> paths = Files.walk(uploadDir, 1)) {
            return paths.filter(Files::isRegularFile) // Chỉ lấy file, bỏ qua thư mục
                    .map(path -> path.getFileName().toString()) // Lấy tên file
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Không thể liệt kê các file trong thư mục", e);
        }
    }
}
