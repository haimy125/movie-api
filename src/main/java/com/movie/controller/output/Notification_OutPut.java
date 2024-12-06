package com.movie.controller.output;

import com.movie.dto.NotificationDTO;

import java.util.ArrayList;
import java.util.List;

public class Notification_OutPut {
    private int page;
    private int totalPage;
    private List<NotificationDTO> listResult = new ArrayList<>();

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

    public List<NotificationDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<NotificationDTO> listResult) {
        this.listResult = listResult;
    }
}
