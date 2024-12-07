package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRatingDTO {

    private String id;

    @NotNull(message = "Người dùng không được để trống")
    @Valid
    private UserDTO user;

    @NotNull(message = "Phim không được để trống")
    @Valid
    private MovieDTO movie;

    @NotNull(message = "Điểm đánh giá không được để trống")
    @Min(value = 1, message = "Điểm đánh giá phải từ 1 đến 5")
    @Max(value = 5, message = "Điểm đánh giá phải từ 1 đến 5")
    private Long rating;
}
