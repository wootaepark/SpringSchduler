package com.sparta.springscheduler.service;

import com.sparta.springscheduler.dto.ScheduleRequestDto;
import com.sparta.springscheduler.dto.ScheduleResponseDto;
import com.sparta.springscheduler.entity.Schedule;
import com.sparta.springscheduler.repository.ScheduleRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

public class ScheduleService {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {

        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);

        Schedule saveSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(saveSchedule);

    }



    public ScheduleResponseDto getSchedule(Integer id) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        Schedule schedule = scheduleRepository.findSchedule(id);

        if(schedule == null) {
            throw new IllegalArgumentException("Schedule with id " + id + " not found");
        }
        return new ScheduleResponseDto(schedule);
    }




    public List<ScheduleResponseDto> getAllSchedules(LocalDate updateDate, String username) {

        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        return scheduleRepository.findAll(updateDate, username);



    }
}
