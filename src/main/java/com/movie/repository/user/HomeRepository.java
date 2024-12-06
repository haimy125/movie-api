package com.movie.repository.user;

import com.movie.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByNewMovie(Boolean newMovie, Pageable pageable);

    List<Movie> findByHotMovie(Boolean hotMovie, Pageable pageable);

    @Query("select m from Movie m  order by m.totalViews desc  limit 10")
    List<Movie> findByTotalviews();
}
