package com.movie.service.user.impl;

import com.movie.dto.MovieDTO;
import com.movie.entity.Category;
import com.movie.entity.CategoryMovie;
import com.movie.entity.Movie;
import com.movie.repository.admin.CategoryMovieRepository;
import com.movie.repository.admin.CategoryRepository;
import com.movie.repository.user.MovieUserRepository;
import com.movie.service.user.MovieUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieUserServiceImpl implements MovieUserService {

    @Autowired
    private MovieUserRepository movie_UserRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryMovieRepository categoryMovieRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<MovieDTO> getListMovie(Long category, Long year, Boolean vip, String sortBy, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        Page<Movie> Movie = movie_UserRepository.findMovies(category, year, vip, sortBy, pageable);
        if (Movie == null)
            throw new RuntimeException("Không có bộ phim nào");
        for (Movie movie : Movie) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByCategoryid(Long categoryid, Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        Category Category = categoryRepository.findById(categoryid).orElseThrow(() -> new RuntimeException("Không có thể tìm thấy danh mục này"));
        List<CategoryMovie> categoryMovieEntities = categoryMovieRepository.findByCategory(Category, pageable);
        if (categoryMovieEntities == null)
            throw new RuntimeException("Không có bộ phim nào");
        for (CategoryMovie categoryMovie : categoryMovieEntities) {
            MovieDTO dto = modelMapper.map(categoryMovie.getMovie(), MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public int totalItem() {
        return (int) movie_UserRepository.count();
    }
}
