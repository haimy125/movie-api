package com.movie.repository.admin;

import com.movie.entity.Category;
import com.movie.entity.CategoryMovie;
import com.movie.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMovieRepository extends JpaRepository<CategoryMovie, Long> {

    List<CategoryMovie> findByCategory(Category category);

    List<CategoryMovie> findByCategory(Category category, Pageable pageable);

    List<CategoryMovie> findByMovie(Movie movie);
}
