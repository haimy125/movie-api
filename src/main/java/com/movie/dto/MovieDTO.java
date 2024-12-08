package com.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class MovieDTO {

    private Long id;

    @NotBlank(message = "Tên tiếng Việt không được để trống")
    @Size(max = 255, message = "Tên tiếng Việt không được vượt quá 255 ký tự")
    private String vnName;

    @Size(max = 255, message = "Tên tiếng Trung không được vượt quá 255 ký tự")
    private String cnName;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    private byte[] imageUrl; // Có thể không bắt buộc, tuỳ nhu cầu hệ thống

    @NotNull(message = "Thời gian thêm không được để trống")
    @PastOrPresent(message = "Thời gian thêm phải là thời điểm hiện tại hoặc trước đó")
    private Date timeAdd;

    @PastOrPresent(message = "Thời gian cập nhật phải là thời điểm hiện tại hoặc trước đó")
    private Date timeUpdate;

    @NotNull(message = "Người thêm không được để trống")
    @Valid
    private UserDTO userAdd;

    @Valid
    private UserDTO userUpdate;

    @NotBlank(message = "Tên tác giả không được để trống")
    @Size(max = 255, message = "Tên tác giả không được vượt quá 255 ký tự")
    private String author;

    @NotNull(message = "Số tập không được để trống")
    @Min(value = 1, message = "Số tập phải lớn hơn hoặc bằng 1")
    private Long episodeNumber;

    @NotBlank(message = "Trạng thái không được để trống")
    @Size(max = 50, message = "Trạng thái không được vượt quá 50 ký tự")
    private String status;

    @NotNull(message = "Trạng thái phim mới không được để trống")
    private Boolean newMovie;

    @NotNull(message = "Trạng thái phim hot không được để trống")
    private Boolean hotMovie;

    @NotNull(message = "Trạng thái phim VIP không được để trống")
    private Boolean vipMovie;

    @NotNull(message = "Giá phim không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá phim phải lớn hơn hoặc bằng 0")
    @Digits(integer = 10, fraction = 2, message = "Giá phim không được vượt quá 10 chữ số nguyên và 2 chữ số thập phân")
    private BigDecimal price;

    @NotNull(message = "Tổng lượt xem không được để trống")
    @Min(value = 0, message = "Tổng lượt xem phải lớn hơn hoặc bằng 0")
    private Long totalViews;

    @NotNull(message = "Năm phát hành không được để trống")
    @Min(value = 1900, message = "Năm phát hành phải lớn hơn hoặc bằng 1900")
    @Max(value = 2100, message = "Năm phát hành phải nhỏ hơn hoặc bằng 2100")
    private Long year;
}
