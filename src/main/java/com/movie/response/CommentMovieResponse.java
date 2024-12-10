package com.movie.response;

import com.movie.dto.CommentMovieDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentMovieResponse {
    private int page;
    private int totalPage;
    private List<CommentMovieDTO> listResult = new ArrayList<>();
}
