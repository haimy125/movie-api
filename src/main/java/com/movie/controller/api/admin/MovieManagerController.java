package com.movie.controller.api.admin;

import com.movie.config.TokenUtil;
import com.movie.dto.UserMovieDetail;
import com.movie.response.MovieResponse;
import com.movie.dto.MovieDTO;
import com.movie.dto.UserDTO;
import com.movie.service.admin.MovieService;
import com.movie.utils.MimeTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.Arrays;

@RestController
@RequestMapping("/api/admin/movies")
public class MovieManagerController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public MovieResponse getAll(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        MovieResponse result = new MovieResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movieService.getAll(pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyname")
    public MovieResponse getByName(@RequestParam("name") String name, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        MovieResponse result = new MovieResponse();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movieService.getByVn_name(name, pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(
            @RequestParam("vn_name") String vn_name,
            @RequestParam("cn_name") String cn_name,
            @RequestParam("description") String description,
            @RequestHeader("Authorization") String token, // Nhận token từ header Authorization
            @RequestParam("author") String author,
            @RequestParam("categorylist") String categorylist,
            @RequestParam("episode_number") Long episode_number,
            @RequestParam("status") String status,
            @RequestParam("new_movie") Boolean new_movie,
            @RequestParam("hot_movie") Boolean hot_movie,
            @RequestParam("vip_movie") Boolean vip_movie,
            @RequestParam("price") BigDecimal price,
            @RequestParam("image") MultipartFile file,
            @RequestParam("year") Long year,
            @RequestParam("schedulelist") String schedulelist
    ) {
        try {
            // Xử lý token để lấy user ID
            String jwt = token.replace("Bearer ", ""); // Loại bỏ tiền tố "Bearer "
            String userId = TokenUtil.getUserIdFromToken(jwt); // Hàm giải mã token lấy user ID

            // Tạo user
            UserDTO user_dto = new UserDTO();
            user_dto.setId(Long.valueOf(userId));

            // Tạo movie
            MovieDTO movie_dto = new MovieDTO();
            movie_dto.setCnName(cn_name);
            movie_dto.setVnName(vn_name);
            movie_dto.setDescription(description);
            movie_dto.setUserAdd(user_dto);
            movie_dto.setUserUpdate(user_dto);
            movie_dto.setAuthor(author);
            movie_dto.setEpisodeNumber(episode_number);
            movie_dto.setStatus(status);
            movie_dto.setNewMovie(new_movie);
            movie_dto.setHotMovie(hot_movie);
            movie_dto.setVipMovie(vip_movie);
            movie_dto.setPrice(price);
            movie_dto.setTotalViews(0L);
            movie_dto.setYear(year);

            // Gọi service để tạo movie
            movieService.create(movie_dto, file, categorylist, schedulelist);

            return new ResponseEntity<>("Thêm mới thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //    @PostMapping("/create")
//    public ResponseEntity<String> create(@RequestParam("vn_name") String vn_name, @RequestParam("cn_name") String cn_name, @RequestParam("description") String description, @RequestParam("user_add") Long user_add, @RequestParam("author") String author, @RequestParam("categorylist") String categorylist, @RequestParam("episode_number") Long episode_number, @RequestParam("status") String status, @RequestParam("new_movie") Boolean new_movie, @RequestParam("hot_movie") Boolean hot_movie, @RequestParam("vip_movie") Boolean vip_movie, @RequestParam("price") BigDecimal price, @RequestParam("image") MultipartFile file, @RequestParam("year") Long year, @RequestParam("schedulelist") String schedulelist) {
//        try {
//
//            //Tạo user
//            UserDTO user_dto = new UserDTO();
//            user_dto.setId(user_add);
//            //Tạo movie
//            MovieDTO movie_dto = new MovieDTO();
//            movie_dto.setCnName(cn_name);
//            movie_dto.setVnName(vn_name);
//            movie_dto.setDescription(description);
//            movie_dto.setUserAdd(user_dto);
//            movie_dto.setUserUpdate(user_dto);
//            movie_dto.setAuthor(author);
//            movie_dto.setEpisodeNumber(episode_number);
//            movie_dto.setStatus(status);
//            movie_dto.setNewMovie(new_movie);
//            movie_dto.setHotMovie(hot_movie);
//            movie_dto.setVipMovie(vip_movie);
//            movie_dto.setPrice(price);
//            movie_dto.setTotalViews(0L);
//            movie_dto.setYear(year);
//            movieService.create(movie_dto, file, categorylist, schedulelist);
//            return new ResponseEntity<>("Thêm mới thành công!", HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
    //    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(
            @PathVariable("id") Long id,
            @RequestParam("new_movie") Boolean newMovie, // Kiểu Boolean
            @RequestParam("hot_movie") Boolean hotMovie, // Kiểu Boolean
            @RequestParam("vip_movie") Boolean vipMovie, // Kiểu Boolean
            @RequestParam("vn_name") String vnName,
            @RequestParam("cn_name") String cnName,
            @RequestParam("description") String description,
            @RequestParam("user_add") Long userAdd,
            @RequestParam("author") String author,
            @RequestParam("categorylist") String categoryList,
            @RequestParam("episode_number") Long episodeNumber,
            @RequestParam("status") String status,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam("year") Long year,
            @RequestParam("schedulelist") String scheduleList
    ) {
        try {
            // Tạo user
            UserDTO userDto = new UserDTO();
            userDto.setId(userAdd);

            // Tạo movie
            MovieDTO movieDto = new MovieDTO();
            movieDto.setId(Long.valueOf(id));
            movieDto.setNewMovie(newMovie);
            movieDto.setHotMovie(hotMovie);
            movieDto.setVipMovie(vipMovie);

            // Các trường khác
            movieDto.setCnName(cnName);
            movieDto.setVnName(vnName);
            movieDto.setDescription(description);
            movieDto.setUserAdd(userDto);
            movieDto.setUserUpdate(userDto);
            movieDto.setAuthor(author);
            movieDto.setEpisodeNumber(episodeNumber);
            movieDto.setStatus(status);
            movieDto.setPrice(price);
            movieDto.setYear(year);

            // Gọi service
            movieService.update(movieDto, file, categoryList, scheduleList);

            return new ResponseEntity<>("Cập nhật thành công!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            MovieDTO movieDTO = movieService.getByMovieId(id);
            return new ResponseEntity<>(movieDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            movieService.delete(id);
            return new ResponseEntity<>("Xóa thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //    @GetMapping("/view/{id}")
//    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
//        MovieDTO fileDTO = movieService.getById(id);
//        if (fileDTO != null) {
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDTO.getId() + "\"").contentType(MediaType.IMAGE_PNG).contentType(MediaType.IMAGE_JPEG).contentType(MediaType.IMAGE_GIF).body(fileDTO.getImageUrl());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        try {
            // Lấy MovieDTO từ service
            MovieDTO movie = movieService.getById(id);
            System.out.println(Arrays.toString(movie.getImageUrl()));

            if (movie == null || movie.getImageUrl() == null || movie.getImageUrl().length == 0) {
                return ResponseEntity.notFound().build(); // Không tìm thấy movie hoặc không có file
            }

            // Xác định MIME type từ dữ liệu file
            String mimeType = MimeTypeUtil.detectMimeType(movie.getImageUrl());

            // Trả về ResponseEntity với dữ liệu file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"movie_" + id + getFileExtension(mimeType) + "\"")
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(movie.getImageUrl());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Dữ liệu không hợp lệ
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/userMovieDetail")
    public ResponseEntity<?> userMovieDetail(@RequestParam("userId") Long userId, @RequestParam("movieId") Long movieId) {
        try {
            UserMovieDetail userMovieDetail = movieService.getDetail(userId, movieId);
            return new ResponseEntity<>(userMovieDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Lấy phần mở rộng file từ MIME type.
     *
     * @param mimeType MIME type (ví dụ: "image/png")
     * @return phần mở rộng file (ví dụ: ".png")
     */
    private String getFileExtension(String mimeType) {
        switch (mimeType) {
            case "image/png":
                return ".png";
            case "image/jpeg":
                return ".jpg";
            case "image/gif":
                return ".gif";
            case "video/mp4":
                return ".mp4";
            case "video/mpeg":
                return ".mpeg";
            default:
                return ""; // Không rõ định dạng
        }
    }
}
