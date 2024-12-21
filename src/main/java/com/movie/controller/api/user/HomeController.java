package com.movie.controller.api.user;

import com.movie.response.MovieResponse;
import com.movie.dto.MovieDTO;
import com.movie.service.FileStorageService;
import com.movie.service.user.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/user/home")
public class HomeController {
    
    @Autowired
    private HomeService home_service;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/new")
    public MovieResponse getNewMovies(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        MovieResponse result = new MovieResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(home_service.getNewMovies(pageable));
        result.setTotalPage((int) Math.ceil((double) (home_service.totalItem()) / limit));
        return result;
    }

    @GetMapping("/hot")
    public MovieResponse getHotMovies(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        MovieResponse result = new MovieResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(home_service.getHotMovies(pageable));
        result.setTotalPage((int) Math.ceil((double) (home_service.totalItem()) / limit));
        return result;
    }

    @GetMapping("/top")
    public List<MovieDTO> getHotMovies() {
        List<MovieDTO> list = home_service.getByTopmovie();
        return list;
    }

//    @GetMapping("/view/{id}")
//    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
//        MovieDTO fileDTO = home_service.getImageByid(id);
//        if (fileDTO != null && fileDTO.getImageUrl() != null) {
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDTO.getId() + "\"")
//                    .contentType(MediaType.IMAGE_PNG)
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .contentType(MediaType.IMAGE_GIF)
//                    .body(fileDTO.getImageUrl());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/view/{id}")
    public ResponseEntity<?> viewFile(@PathVariable Long id) {
        try {
            MovieDTO fileDTO = home_service.getImageByid(id);

            // Kiểm tra nếu không tìm thấy user hoặc không có avatar
            if (fileDTO == null || fileDTO.getImageUrl() == null || fileDTO.getImageUrl().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Tải file từ đường dẫn lưu trong cơ sở dữ liệu sử dụng loadFile
            Resource fileResource = fileStorageService.loadFile(fileDTO.getImageUrl());

            // Kiểm tra nếu file không tồn tại hoặc không đọc được
            if (fileResource == null || !fileResource.exists() || !fileResource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Xác định MIME type của file
            Path filePath = Paths.get(fileDTO.getImageUrl()).normalize();
            String mimeType = Files.probeContentType(filePath);
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // MIME mặc định nếu không xác định được
            }

            // Trả về ResponseEntity với dữ liệu file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(fileResource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body("URL không hợp lệ.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tải file.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
