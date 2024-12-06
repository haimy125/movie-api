package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String username;
    private String fullname;
    private String password;
    private String email;
    private Date timeAdd;
    private Long point;
    private RoleDTO role;
    private byte[]  avatar;
    private Boolean status;
    private Boolean active;
}
