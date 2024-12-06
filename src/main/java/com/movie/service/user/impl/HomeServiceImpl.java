package com.movie.service.user.impl;

import com.movie.dto.MovieDTO;
import com.movie.entity.Movie;
import com.movie.repository.user.HomeRepository;
import com.movie.service.user.HomeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    
    @Autowired
    private HomeRepository homeRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<MovieDTO> getNewMovies(Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> movies = homeRepository.findByNewMovie(true, pageable);
        if (movies == null)
            throw new RuntimeException("Không có bộ phim mới nào");
        for (Movie movie : movies) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getHotMovies(Pageable pageable) {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> movies = homeRepository.findByHotMovie(true, pageable);
        if (movies == null)
            throw new RuntimeException("Không có bộ phim mới nào");
        for (Movie movie : movies) {
            MovieDTO dto = modelMapper.map(movie, MovieDTO.class);
            dto.setImageUrl(null);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<MovieDTO> getByTopmovie() {
        List<MovieDTO> result = new ArrayList<>();
        List<Movie> Movie = homeRepository.findByTotalviews();
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
    public MovieDTO getImageByid(Long id) {
        try {
            Movie Movie = homeRepository.findById(id).get();
            if (Movie == null)
                throw new RuntimeException("Không có bộ phim nào");
            MovieDTO dto = modelMapper.map(Movie, MovieDTO.class);

            return dto;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int totalItem() {
        return (int) homeRepository.count();
    }
}
