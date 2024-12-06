package com.movie.service.user;

import com.movie.dto.CommentMovieDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentMovieService {
    
    List<CommentMovieDTO> getByMovie(Long movieid, Pageable pageable);

    CommentMovieDTO save(CommentMovieDTO comment);

    int totalItem();
}
