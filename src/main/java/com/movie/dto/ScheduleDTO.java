package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class ScheduleDTO {

    private Long id;

    @NotBlank(message = "Tên lịch trình không được để trống")
    @Size(max = 255, message = "Tên lịch trình không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @NotNull(message = "Thời gian thêm không được để trống")
    @PastOrPresent(message = "Thời gian thêm phải là thời điểm hiện tại hoặc trước đó")
    private Date timeAdd;

    @PastOrPresent(message = "Thời gian cập nhật phải là thời điểm hiện tại hoặc trước đó")
    private Date timeupDate;

    @NotNull(message = "Người thêm không được để trống")
    @Valid
    private UserDTO userAdd;

    @Valid
    private UserDTO userUpdate;
}
