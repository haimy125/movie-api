package com.movie.controller.api.admin;

import com.movie.dto.ScheduleDTO;
import com.movie.service.admin.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping("/getAll")
    public List<ScheduleDTO> getAll(){
        List<ScheduleDTO> result = scheduleService.getAll();
        return result;
    }
}
