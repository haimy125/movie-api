package com.movie.controller.api.admin;

import com.movie.controller.output.MonthlyRevenue;
import com.movie.controller.output.Static_OutPut;
import com.movie.controller.output.Static_movie_Output;
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
    public Static_OutPut gettongso() {
        Static_OutPut result = new Static_OutPut();
        result.setTongSoUser(staticService.tongSoUser());
        result.setTongSoCategory(staticService.tongSoCategory());
        result.setTongSoMovie(staticService.tongSoMovie());
        return result;
    }

    @GetMapping("/getphimvip")
    public Static_movie_Output gettongsovip() {
        Static_movie_Output result = new Static_movie_Output();
        result.setTongSoNoVipMovie(staticService.tongSoNoVipmovie());
        result.setTongSoVipMovie(staticService.tongSoVipmovie());
        return result;
    }

    @GetMapping("/monthly-revenue")
    public List<MonthlyRevenue> getMonthlyRevenue() {
        return orderService.getMonthlyRevenue();
    }
}
