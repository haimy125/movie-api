package com.movie.repository.admin;

import com.movie.entity.Movie;
import com.movie.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByNewMovie(Boolean newMovie, Pageable pageable);

    List<Movie> findByHotMovie(Boolean hotMovie, Pageable pageable);

    List<Movie> findByAuthorLike(String author, Pageable pageable);

    List<Movie> findByVnNameLike(String vnName, Pageable pageable);

    List<Movie> findByCnNameLike(String cnName, Pageable pageable);

    List<Movie> findByUserAdd(User userAdd, Pageable pageable);

    List<Movie> findByUserUpdate(User userUpdate, Pageable pageable);

    List<Movie> findByVipMovie(Boolean vipMovie, Pageable pageable);

    List<Movie> findByVipMovie(Boolean vipMovie);

    List<Movie> findByTimeAdd(Date timeAdd, Pageable pageable);

    List<Movie> findByTimeUpdate(Date timeUpdate, Pageable pageable);
}
