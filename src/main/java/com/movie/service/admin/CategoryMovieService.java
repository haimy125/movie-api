package com.movie.service.admin;


import com.movie.dto.CategoryMovieDTO;

import java.util.List;

public interface CategoryMovieService {

    List<CategoryMovieDTO> getByMovie_id(Long movie_id);

    List<CategoryMovieDTO> getByCategory_id(Long category_id);
}
