package com.movie.controller.api.admin;

import com.movie.response.MonthlyRevenue;
import com.movie.response.StaticResponse;
import com.movie.response.StaticMovieResponse;
import com.movie.service.admin.OrderService;
import com.movie.service.admin.StaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/static")
public class StaticController {

    @Autowired
    private StaticService staticService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/gettongso")
    public StaticResponse gettongso() {
        StaticResponse result = new StaticResponse();
        result.setTongSoUser(staticService.tongSoUser());
        result.setTongSoCategory(staticService.tongSoCategory());
        result.setTongSoMovie(staticService.tongSoMovie());
        return result;
    }

    @GetMapping("/getphimvip")
    public StaticMovieResponse gettongsovip() {
        StaticMovieResponse result = new StaticMovieResponse();
        result.setTongSoNoVipMovie(staticService.tongSoNoVipmovie());
        result.setTongSoVipMovie(staticService.tongSoVipmovie());
        return result;
    }

    @GetMapping("/monthly-revenue")
    public List<MonthlyRevenue> getMonthlyRevenue() {
        return orderService.getMonthlyRevenue();
    }
}
