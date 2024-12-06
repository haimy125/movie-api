package com.movie.service.admin.impl;

import com.movie.dto.ScheduleMovieDTO;
import com.movie.entity.Movie;
import com.movie.entity.Schedule;
import com.movie.entity.ScheduleMovie;
import com.movie.repository.admin.MovieRepository;
import com.movie.repository.admin.ScheduleMovieRepository;
import com.movie.repository.admin.ScheduleRepository;
import com.movie.service.admin.ScheduleMovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleMovieServiceImpl implements ScheduleMovieService {

    @Autowired
    private ScheduleMovieRepository scheduleMovieRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<ScheduleMovieDTO> findAll() {
        List<ScheduleMovieDTO> result = new ArrayList<>();
        List<ScheduleMovie> scheduleMovieEntities = scheduleMovieRepository.findAll();
        if (scheduleMovieEntities == null) {
            throw new RuntimeException("Không có bộ phim này");
        }
        for (ScheduleMovie scheduleMovieEntity : scheduleMovieEntities) {
            ScheduleMovieDTO dto = modelMapper.map(scheduleMovieEntity, ScheduleMovieDTO.class);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<ScheduleMovieDTO> findBymovie(Long scheduleId) {
        List<ScheduleMovieDTO> result = new ArrayList<>();
        Movie movie = movieRepository.findById(scheduleId).orElseThrow(() -> new RuntimeException("Không có bộ phim này"));
        List<ScheduleMovie> scheduleMovieEntities = scheduleMovieRepository.findByMovie(movie);
        if (scheduleMovieEntities == null) {
            throw new RuntimeException("Không có bộ phim này");
        }
        for (ScheduleMovie scheduleMovieEntity : scheduleMovieEntities) {
            ScheduleMovieDTO dto = modelMapper.map(scheduleMovieEntity, ScheduleMovieDTO.class);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<ScheduleMovieDTO> getBySchedule(Long scheduleId) {
        List<ScheduleMovieDTO> result = new ArrayList<>();
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RuntimeException("Không có bộ phim này"));
        List<ScheduleMovie> scheduleMovieEntities = scheduleMovieRepository.findBySchedule(schedule);
        if (scheduleMovieEntities == null) {
            throw new RuntimeException("Không có bộ phim này");
        }
        for (ScheduleMovie scheduleMovieEntity : scheduleMovieEntities) {
            ScheduleMovieDTO dto = modelMapper.map(scheduleMovieEntity, ScheduleMovieDTO.class);
            result.add(dto);
        }
        return result;
    }
}
