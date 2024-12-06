package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class CommentMovieDTO {

    private Long id;

    private String content;

    private Date timeAdd;

    private UserDTO userAdd;

    private MovieDTO movie;
}
