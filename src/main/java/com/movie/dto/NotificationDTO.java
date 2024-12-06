package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class NotificationDTO {
    private Long id;
    private String content;
    private Date timeAdd;
    private Date timeupDate;
    private UserDTO user;
    private Boolean status;
}
