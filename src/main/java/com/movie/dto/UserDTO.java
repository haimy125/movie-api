package com.movie.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3 đến 25 ký tự")
    private String username;

    @Size(min = 3, max = 50, message = "Fullname phải từ 3 đến 50 ký tự")
    private String fullname;

    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    private Date timeAdd;

    @Min(value = 0, message = "Điểm phải lớn hơn hoặc bằng 0")
    private Long point;

    private RoleDTO role;

//    private byte[] avatar;
    private String avatar;

    private Boolean status;

    private Boolean active;
}
