package com.movie.response;

import com.movie.dto.CommentEpisodeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentEpisodeResponse {
    private int page;
    private int totalPage;
    private List<CommentEpisodeDTO> listResult = new ArrayList<>();
}
