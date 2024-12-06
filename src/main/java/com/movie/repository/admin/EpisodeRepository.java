package com.movie.repository.admin;

import com.movie.entity.Episode;
import com.movie.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findByMovie(Movie movie, Pageable pageable);
    List<Episode> findByMovie(Movie movie);
}
