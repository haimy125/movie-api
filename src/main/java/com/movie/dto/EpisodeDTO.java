package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class EpisodeDTO {

    private Long id;

    @NotBlank(message = "Tên tập phim không được để trống")
    @Size(max = 255, message = "Tên tập phim không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 5000, message = "Mô tả không được vượt quá 5000 ký tự")
    private String description;

    @NotNull(message = "Thời gian thêm không được để trống")
    @PastOrPresent(message = "Thời gian thêm phải là thời điểm hiện tại hoặc trước đó")
    private Date timeAdd;

    @PastOrPresent(message = "Thời gian cập nhật phải là thời điểm hiện tại hoặc trước đó")
    private Date timeUpdate;

    @NotNull(message = "Người thêm không được để trống")
    @Valid
    private UserDTO userAdd;

    private UserDTO userUpdate;

    @NotNull(message = "Số lượt xem không được để trống")
    @Min(value = 0, message = "Số lượt xem phải lớn hơn hoặc bằng 0")
    private Long views;

    @NotNull(message = "Số lượt thích không được để trống")
    @Min(value = 0, message = "Số lượt thích phải lớn hơn hoặc bằng 0")
    private Long likes;

    @NotNull(message = "Thông tin phim không được để trống")
    @Valid
    private MovieDTO movie;

    @NotNull(message = "File tập phim không được để trống")
//    private byte[] fileEpisodes;
    private String fileEpisodes;

    private String subtitles;
//    private byte[] subtitles; // Không bắt buộc, có thể để null
}
