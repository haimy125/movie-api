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
public class CommentEpisodeDTO {

    private String id;

    @NotBlank(message = "Nội dung bình luận không được để trống")
    @Size(max = 500, message = "Nội dung bình luận không được vượt quá 500 ký tự")
    private String content;

    @NotNull(message = "Thời gian thêm không được để trống")
    @PastOrPresent(message = "Thời gian thêm phải là thời gian hiện tại hoặc trước đó")
    private Date timeAdd;

    @NotNull(message = "Người thêm bình luận không được để trống")
    @Valid
    private UserDTO userAdd;

    @NotNull(message = "Thông tin tập phim không được để trống")
    @Valid
    private EpisodeDTO episode;
}
