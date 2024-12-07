package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class OrderDTO {
    private String id;
    private Long point;
    private UserDTO user;
    private MovieDTO movie;
    private Date date;
    private String status;
}
