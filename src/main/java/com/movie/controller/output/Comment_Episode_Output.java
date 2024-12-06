package com.movie.controller.output;

import com.movie.dto.CommentEpisodeDTO;

import java.util.ArrayList;
import java.util.List;

public class Comment_Episode_Output {
    private int page;
    private int totalPage;
    private List<CommentEpisodeDTO> listResult = new ArrayList<>();

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

    public List<CommentEpisodeDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<CommentEpisodeDTO> listResult) {
        this.listResult = listResult;
    }
}
