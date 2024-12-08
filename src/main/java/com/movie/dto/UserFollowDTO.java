package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFollowDTO {

    private Long id;

    @NotNull(message = "Người dùng không được để trống")
    @Valid
    private UserDTO user;

    @NotNull(message = "Phim không được để trống")
    @Valid
    private MovieDTO movie;
}
