package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class EpisodeDTO {
    private Long id;
    private String name;
    private String description;
    private Date timeAdd;
    private Date timeUpdate;
    private UserDTO userAdd;
    private UserDTO userUpdate;
    private Long views;
    private Long likes;
    private MovieDTO movie;
    private byte[]  fileEpisodes;
    private byte[] subtitles;
}
