package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class CommentEpisodeDTO {

    private Long id;
    private String content;
    private Date timeAdd;
    private UserDTO userAdd;
    private EpisodeDTO episode;
}
