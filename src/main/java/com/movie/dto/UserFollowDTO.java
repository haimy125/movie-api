package com.movie.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFollowDTO {

    private String id;

    @NotNull(message = "Người dùng không được để trống")
    @Valid
    private UserDTO user;

    @NotNull(message = "Phim không được để trống")
    @Valid
    private MovieDTO movie;
}
