package com.sparta.springscheduler.service;

import com.sparta.springscheduler.dto.ScheduleDeleteRequestDto;
import com.sparta.springscheduler.dto.ScheduleRequestDto;
import com.sparta.springscheduler.dto.ScheduleResponseDto;
import com.sparta.springscheduler.dto.ScheduleUpdateRequestDto;
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

        if (schedule == null) {
            throw new IllegalArgumentException("Schedule with id " + id + " not found");
        }
        return new ScheduleResponseDto(schedule);
    }


    public List<ScheduleResponseDto> getAllSchedules(LocalDate updateDate, String username) {

        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        return scheduleRepository.findAll(updateDate, username);


    }

    public Integer updateSchedule(Integer id, ScheduleUpdateRequestDto requestDto) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        Schedule schedule = scheduleRepository.findById(id);

        if (schedule != null) {
            if (requestDto.getPassword().equals(schedule.getPassword())) {
                scheduleRepository.update(id, requestDto);
                return id; // 올바른 패스워드가 들어온 경우
            }

            return -1; // 올바른 패스워드가 들어오지 않은 경우
        } else {
            throw new IllegalArgumentException("Schedule with id " + id + " not found");
        }


    }



    public Integer deleteSchedule(Integer id, ScheduleDeleteRequestDto requestDto) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        Schedule schedule = scheduleRepository.findSchedule(id);

        if (schedule != null) {
            if (schedule.getPassword().equals(requestDto.getPassword())) {
                scheduleRepository.delete(id);
                return id;
            }
            return -1;
        } else {
            throw new IllegalArgumentException("Schedule with id " + id + " not found");
        }

    }



}
