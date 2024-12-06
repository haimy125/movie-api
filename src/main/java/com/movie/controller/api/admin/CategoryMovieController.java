package com.movie.controller.api.admin;

import com.movie.dto.CategoryMovieDTO;
import com.movie.dto.MovieDTO;
import com.movie.service.admin.CategoryMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/category_movie")
public class CategoryMovieController {

    @Autowired
    private CategoryMovieService categoryMovieService;

    @GetMapping("/getBymovie/{id}")
    public List<CategoryMovieDTO> getByMovie_id(@PathVariable("id") Long movie_id) {
        List<CategoryMovieDTO> list_entity = new ArrayList<>();
        List<CategoryMovieDTO> list = categoryMovieService.getByMovie_id(movie_id);

        for (CategoryMovieDTO item : list) {
            MovieDTO movie_dto = item.getMovie();
            movie_dto.setImageUrl(null);
            item.setMovie(movie_dto);
            list_entity.add(item);
        }
        return list_entity;
    }

    @GetMapping("/getBycategory/{id}")
    public List<CategoryMovieDTO> getByCategory_id(@PathVariable("id") Long category_id) {
        return categoryMovieService.getByCategory_id(category_id);
    }
}
