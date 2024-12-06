package com.movie.repository.user;

import com.movie.entity.Movie;
import com.movie.entity.User;
import com.movie.entity.UserFollow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    List<UserFollow> findByUser(User userId, Pageable pageable);

    List<UserFollow> findByUserAndMovie(User userId, Movie movieId);
}
