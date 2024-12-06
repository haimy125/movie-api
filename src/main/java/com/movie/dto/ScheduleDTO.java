package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class ScheduleDTO {
    private Long id;
    private String name;
    private String description;
    private Date timeAdd;
    private Date timeupDate;
    private UserDTO userAdd;
    private UserDTO userUpdate;
}
