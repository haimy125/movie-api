package com.movie.controller.output;

import com.movie.dto.MovieDTO;

import java.util.ArrayList;
import java.util.List;

public class Movie_output {
    private int page;
    private int totalPage;
    private List<MovieDTO> listResult = new ArrayList<>();

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

    public List<MovieDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<MovieDTO> listResult) {
        this.listResult = listResult;
    }
}
