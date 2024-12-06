package com.movie.service.user;

import com.movie.dto.MovieDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HomeService {

    List<MovieDTO> getNewMovies(Pageable pageable);

    List<MovieDTO> getHotMovies(Pageable pageable);

    List<MovieDTO> getByTopmovie();

    MovieDTO getImageByid(Long id);

    int totalItem();
}
