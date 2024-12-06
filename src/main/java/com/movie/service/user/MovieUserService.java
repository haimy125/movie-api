package com.movie.service.user;

import com.movie.dto.MovieDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieUserService {

    List<MovieDTO> getListMovie(Long category, Long year, Boolean vip, String sortBy, Pageable pageable);

    List<MovieDTO> getByCategoryid(Long categoryid, Pageable pageable);

    int totalItem();
}
