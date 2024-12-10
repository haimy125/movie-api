package com.movie.response;

import com.movie.dto.NotificationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NotificationResponse {
    private int page;
    private int totalPage;
    private List<NotificationDTO> listResult = new ArrayList<>();
}
