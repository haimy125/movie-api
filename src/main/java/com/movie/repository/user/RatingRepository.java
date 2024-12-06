package com.movie.repository.user;

import com.movie.entity.Movie;
import com.movie.entity.User;
import com.movie.entity.UserRating;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<UserRating, Long> {
    List<UserRating> findByUser(User userId, Pageable pageable);

    List<UserRating> findByMovie(Movie movieId, Pageable pageable);

    UserRating findByUserAndMovie(User userId, Movie movieId);
}
