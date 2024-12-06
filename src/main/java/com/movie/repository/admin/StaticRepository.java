package com.movie.repository.admin;

import com.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaticRepository extends JpaRepository<Movie, Long> {
}
