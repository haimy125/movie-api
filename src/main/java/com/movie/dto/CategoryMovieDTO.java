package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryMovieDTO {

    private Long id;

    @NotNull(message = "Danh mục không được để trống")
    @Valid
    private CategoryDTO category;

    @NotNull(message = "Phim không được để trống")
    @Valid
    private MovieDTO movie;
}
