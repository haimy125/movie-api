package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class NotificationDTO {

    private String id;

    @NotBlank(message = "Nội dung thông báo không được để trống")
    @Size(max = 500, message = "Nội dung thông báo không được vượt quá 500 ký tự")
    private String content;

    @NotNull(message = "Thời gian thêm không được để trống")
    @PastOrPresent(message = "Thời gian thêm phải là thời điểm hiện tại hoặc trước đó")
    private Date timeAdd;

    @PastOrPresent(message = "Thời gian cập nhật phải là thời điểm hiện tại hoặc trước đó")
    private Date timeupDate;

    @NotNull(message = "Người gửi thông báo không được để trống")
    @Valid
    private UserDTO user;

    @NotNull(message = "Trạng thái thông báo không được để trống")
    private Boolean status;
}
