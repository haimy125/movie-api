package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class OrderDTO {

    private String id;

    @NotNull(message = "Điểm tích lũy không được để trống")
    @Min(value = 0, message = "Điểm tích lũy phải lớn hơn hoặc bằng 0")
    private Long point;

    @NotNull(message = "Người đặt hàng không được để trống")
    @Valid
    private UserDTO user;

    @NotNull(message = "Phim không được để trống")
    @Valid
    private MovieDTO movie;

    @NotNull(message = "Ngày đặt hàng không được để trống")
    @PastOrPresent(message = "Ngày đặt hàng phải là thời điểm hiện tại hoặc trước đó")
    private Date date;

    @NotBlank(message = "Trạng thái đơn hàng không được để trống")
    @Size(max = 50, message = "Trạng thái đơn hàng không được vượt quá 50 ký tự")
    private String status;
}
