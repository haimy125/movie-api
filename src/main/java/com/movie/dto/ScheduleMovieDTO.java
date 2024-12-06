package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleMovieDTO {
    private Long id;
    private MovieDTO movie;
    private ScheduleDTO schedule;
}
