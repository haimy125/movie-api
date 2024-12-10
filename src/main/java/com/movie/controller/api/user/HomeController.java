package com.movie.controller.api.user;

import com.movie.response.MovieResponse;
import com.movie.dto.MovieDTO;
import com.movie.service.user.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/home")
public class HomeController {
    
    @Autowired
    private HomeService home_service;

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

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        MovieDTO fileDTO = home_service.getImageByid(id);
        if (fileDTO != null && fileDTO.getImageUrl() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDTO.getId() + "\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentType(MediaType.IMAGE_GIF)
                    .body(fileDTO.getImageUrl());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
