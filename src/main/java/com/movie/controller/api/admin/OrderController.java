package com.movie.controller.api.admin;

import com.movie.controller.output.Orders_OutPut;
import com.movie.service.admin.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public Orders_OutPut getAll(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Orders_OutPut result = new Orders_OutPut();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(orderService.getAll(pageable));
        result.setTotalPage((int) Math.ceil((double) (orderService.totalItem()) / limit));
        return result;
    }

    @GetMapping("/getbyuser")
    public Orders_OutPut getbyuser(@RequestParam("userid") Long userid,@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Orders_OutPut result = new Orders_OutPut();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(orderService.getOrderByUserId(userid,pageable));
        result.setTotalPage((int) Math.ceil((double) (orderService.totalItem()) / limit));
        return result;
    }
}
