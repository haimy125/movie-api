package com.movie.controller.output;

import com.movie.dto.EpisodeDTO;

import java.util.ArrayList;
import java.util.List;

public class Episode_Output {
    private int page;
    private int totalPage;
    private List<EpisodeDTO> listResult = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<EpisodeDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<EpisodeDTO> listResult) {
        this.listResult = listResult;
    }
}
