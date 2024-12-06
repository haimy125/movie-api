package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryMovieDTO {
    private Long id;
    private CategoryDTO categoryDTO;
    private MovieDTO movie;
}
