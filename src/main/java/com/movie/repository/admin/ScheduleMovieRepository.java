package com.movie.repository.admin;

import com.movie.entity.Movie;
import com.movie.entity.Schedule;
import com.movie.entity.ScheduleMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleMovieRepository extends JpaRepository<ScheduleMovie, Long> {
    List<ScheduleMovie> findByMovie(Movie movieId);

    List<ScheduleMovie> findBySchedule(Schedule scheduleId);

    List<ScheduleMovie> findAllBySchedule(Schedule scheduleId);

}
