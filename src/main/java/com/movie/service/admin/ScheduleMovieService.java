package com.movie.service.admin;


import com.movie.dto.ScheduleMovieDTO;

import java.util.List;

public interface ScheduleMovieService {

    List<ScheduleMovieDTO> findAll();

    List<ScheduleMovieDTO> findBymovie(Long scheduleId);

    List<ScheduleMovieDTO> getBySchedule(Long scheduleId);
}
