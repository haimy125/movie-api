package com.movie.service.admin.impl;

import com.movie.dto.CategoryMovieDTO;
import com.movie.entity.Category;
import com.movie.entity.CategoryMovie;
import com.movie.entity.Movie;
import com.movie.repository.admin.CategoryMovieRepository;
import com.movie.repository.admin.CategoryRepository;
import com.movie.repository.admin.MovieRepository;
import com.movie.service.admin.CategoryMovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryMovieServiceImpl implements CategoryMovieService {

    @Autowired
    private CategoryMovieRepository categoryMovieRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryMovieDTO> getByMovie_id(Long movie_id) {
        List<CategoryMovieDTO> result = new ArrayList<>();
        Movie movie = movieRepository.findById(movie_id).orElseThrow(() -> new RuntimeException("Không có bộ phim này"));
        List<CategoryMovie> categoryMovieEntities = categoryMovieRepository.findByMovie(movie);
        for (CategoryMovie categoryMovieEntity : categoryMovieEntities) {
            CategoryMovieDTO dto = modelMapper.map(categoryMovieEntity, CategoryMovieDTO.class);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<CategoryMovieDTO> getByCategory_id(Long category_id) {
        List<CategoryMovieDTO> result = new ArrayList<>();
        Category category = categoryRepository.findById(category_id).orElseThrow(() -> new RuntimeException("Không có bộ the loại"));
        List<CategoryMovie> categoryMovieEntities = categoryMovieRepository.findByCategory(category);
        for (CategoryMovie categoryMovieEntity : categoryMovieEntities) {
            CategoryMovieDTO dto = modelMapper.map(categoryMovieEntity, CategoryMovieDTO.class);

            result.add(dto);
        }
        return result;
    }
}
