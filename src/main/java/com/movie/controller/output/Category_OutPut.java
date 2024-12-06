package com.movie.controller.output;

import com.movie.dto.CategoryDTO;

import java.util.ArrayList;
import java.util.List;

public class Category_OutPut {
    private int page;
    private int totalPage;
    private List<CategoryDTO> listResult = new ArrayList<>();

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

    public List<CategoryDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<CategoryDTO> listResult) {
        this.listResult = listResult;
    }
}
