package com.movie.controller.api.admin;

import com.movie.dto.ScheduleMovieDTO;
import com.movie.service.admin.ScheduleMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/schedule-movie")
public class ScheduleMovieController {
    
    @Autowired
    private ScheduleMovieService scheduleMovieService;

    @GetMapping("/getbymovie/{id}")
    public List<ScheduleMovieDTO> getByMovie_id(@PathVariable Long id) {
        List<ScheduleMovieDTO> result = scheduleMovieService.findBymovie(id);
        return result;
    }

    @GetMapping("/getbyschedule/{id}")
    public List<ScheduleMovieDTO> getbyschedule(@PathVariable Long id) {
        List<ScheduleMovieDTO> result = scheduleMovieService.getBySchedule(id);
        return result;
    }

    @GetMapping("/all")
    public List<ScheduleMovieDTO> getall() {
        List<ScheduleMovieDTO> result = scheduleMovieService.findAll();
        return result;
    }
}
