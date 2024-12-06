package com.movie.controller.output;

import com.movie.dto.UserFollowDTO;

import java.util.ArrayList;
import java.util.List;

public class User_Follow_Output {
    private int page;
    private int totalPage;
    private List<UserFollowDTO> listResult = new ArrayList<>();

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<UserFollowDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<UserFollowDTO> listResult) {
        this.listResult = listResult;
    }
}
