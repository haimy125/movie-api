package com.movie.response;

import com.movie.dto.MovieDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MovieResponse {
    private int page;
    private int totalPage;
    private List<MovieDTO> listResult = new ArrayList<>();
}
