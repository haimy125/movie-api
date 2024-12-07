package com.movie.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFollowDTO {
    private String id;
    private UserDTO user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private MovieDTO movie;
}
