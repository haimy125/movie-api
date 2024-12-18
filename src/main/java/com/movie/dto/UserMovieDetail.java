package com.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMovieDetail {
    MovieDTO movie;
    boolean isFollowed;
    boolean isBuy;
}
