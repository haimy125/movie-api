package com.movie.controller.output;

import com.movie.dto.CommentMovieDTO;

import java.util.ArrayList;
import java.util.List;

public class Comment_movie_output {
    private int page;
    private int totalPage;
    private List<CommentMovieDTO> listResult = new ArrayList<>();

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

    public List<CommentMovieDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<CommentMovieDTO> listResult) {
        this.listResult = listResult;
    }
}
