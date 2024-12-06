package com.movie.repository.user;

import com.movie.entity.CommentMovie;
import com.movie.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMovieRepository extends JpaRepository<CommentMovie, Long> {
    List<CommentMovie> findByMovie(Movie movieid, Pageable pageable);
}
