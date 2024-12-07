package com.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class MovieDTO {
    private String id;
    private String vnName;
    private String cnName;
    private String description;
    private byte[] imageUrl;
    private Date timeAdd;
    private Date timeUpdate;
    private UserDTO userAdd;
    private UserDTO userUpdate;
    private String author;
    private Long episodeNumber;
    private String status;
    private Boolean newMovie;
    private Boolean hotMovie;
    private Boolean vipMovie;
    private BigDecimal price;
    private Long totalViews;
    private Long year;
}
