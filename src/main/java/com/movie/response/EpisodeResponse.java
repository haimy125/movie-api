package com.movie.response;

import com.movie.dto.EpisodeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EpisodeResponse {
    private int page;
    private int totalPage;
    private List<EpisodeDTO> listResult = new ArrayList<>();
}
