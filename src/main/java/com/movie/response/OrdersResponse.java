package com.movie.response;

import com.movie.dto.OrderDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrdersResponse {
    private int page;
    private int totalPage;
    private List<OrderDTO > listResult = new ArrayList<>();
}
