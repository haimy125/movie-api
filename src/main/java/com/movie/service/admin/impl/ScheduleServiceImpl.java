package com.movie.service.admin.impl;

import com.movie.dto.ScheduleDTO;
import com.movie.entity.Schedule;
import com.movie.repository.admin.ScheduleRepository;
import com.movie.service.admin.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ScheduleDTO> getAll() {

        List<ScheduleDTO> result = new ArrayList<>();
        List<Schedule> Schedule = scheduleRepository.findAll();
        if (Schedule == null)
            throw new RuntimeException("Không có bộ liên quan");
        for (Schedule schedule : Schedule) {
            ScheduleDTO dto = modelMapper.map(schedule, ScheduleDTO.class);
            dto.setUserAdd(null);
            dto.setUserUpdate(null);
            result.add(dto);
        }
        return result;
    }
}
