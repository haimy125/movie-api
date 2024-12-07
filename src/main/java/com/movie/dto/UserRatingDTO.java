package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRatingDTO {
    private String id;
    private UserDTO user;
    private MovieDTO movie;
    private Long rating;
}
