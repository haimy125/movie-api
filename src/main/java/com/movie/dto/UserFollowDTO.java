package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowDTO {

    private Long id;

    @NotNull(message = "Người dùng không được để trống")
    @Valid
    private UserDTO user;

    @NotNull(message = "Phim không được để trống")
    @Valid
    private MovieDTO movie;

}
