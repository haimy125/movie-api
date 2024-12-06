package com.movie.repository.user;

import com.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieUserRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m " +
            "JOIN CategoryMovie c ON c.movie.id = m.id " +
            "WHERE (:genre IS NULL OR c.category.id = :genre) " +
            "AND (:year IS NULL OR m.year = :year) " +
            "AND (:vip IS NULL OR m.vipMovie = :vip) " +
            "ORDER BY " +
            "CASE WHEN :sortBy IS null or :sortBy = 'date' THEN m.timeAdd END DESC , " +
            "CASE WHEN :sortBy IS null or  :sortBy = 'views' THEN m.totalViews END DESC, " +
            "CASE WHEN :sortBy IS null or  :sortBy = 'name' THEN m.vnName END ASC")

    Page<Movie> findMovies(
            @Param("genre") Long genre,
            @Param("year") Long year,
            @Param("vip") Boolean vip,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );
}
