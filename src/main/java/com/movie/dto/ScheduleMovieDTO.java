package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleMovieDTO {

    private String id;

    @NotNull(message = "Phim không được để trống")
    @Valid
    private MovieDTO movie;

    @NotNull(message = "Lịch trình không được để trống")
    @Valid
    private ScheduleDTO schedule;
}
