package com.movie.controller.api.admin;

import com.movie.controller.output.Movie_output;
import com.movie.dto.MovieDTO;
import com.movie.dto.UserDTO;
import com.movie.service.admin.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin/movies")
public class MovieManagerController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public Movie_output getAll(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movieService.getAll(pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyname")
    public Movie_output getAll(@RequestParam("name") String name, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movieService.getByVn_name(name, pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestParam("vn_name") String vn_name, @RequestParam("cn_name") String cn_name, @RequestParam("description") String description, @RequestParam("user_add") Long user_add, @RequestParam("author") String author, @RequestParam("categorylist") String categorylist, @RequestParam("episode_number") Long episode_number, @RequestParam("status") String status, @RequestParam("new_movie") Boolean new_movie, @RequestParam("hot_movie") Boolean hot_movie, @RequestParam("vip_movie") Boolean vip_movie, @RequestParam("price") BigDecimal price, @RequestParam("image") MultipartFile file, @RequestParam("year") Long year, @RequestParam("schedulelist") String schedulelist) {
        try {

            //Tạo user
            UserDTO user_dto = new UserDTO();
            user_dto.setId(user_add);
            //Tạo movie
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
            movieService.create(movie_dto, file, categorylist, schedulelist);
            return new ResponseEntity<>("Thêm mới thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestParam("vn_name") String vn_name, @RequestParam("cn_name") String cn_name, @RequestParam("description") String description, @RequestParam("user_add") Long user_add, @RequestParam("author") String author, @RequestParam("categorylist") String categorylist, @RequestParam("episode_number") Long episode_number, @RequestParam("status") String status, @RequestParam("new_movie") Boolean new_movie, @RequestParam("hot_movie") Boolean hot_movie, @RequestParam("vip_movie") Boolean vip_movie, @RequestParam("price") BigDecimal price, @RequestParam(value = "image", required = false) MultipartFile file, @RequestParam("year") Long year, @RequestParam("schedulelist") String schedulelist) {
        try {

            //Tạo user
            UserDTO user_dto = new UserDTO();
            user_dto.setId(user_add);
            //Tạo movie
            MovieDTO movie_dto = new MovieDTO();
            movie_dto.setId(id);
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
            movie_dto.setYear(year);
            movieService.update(movie_dto, file, categorylist, schedulelist);

            return new ResponseEntity<>("Thêm mới thành công!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getByid(@PathVariable Long id) {
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

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        MovieDTO fileDTO = movieService.getById(id);
        if (fileDTO != null && fileDTO.getImageUrl() != null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDTO.getId() + "\"").contentType(MediaType.IMAGE_PNG).contentType(MediaType.IMAGE_JPEG).contentType(MediaType.IMAGE_GIF).body(fileDTO.getImageUrl());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
