package com.sparta.springscheduler.service;

import com.sparta.springscheduler.dto.*;
import com.sparta.springscheduler.entity.Schedule;
import com.sparta.springscheduler.entity.User;
import com.sparta.springscheduler.repository.ScheduleRepository;
import com.sparta.springscheduler.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleService {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }


    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        UserRepository userRepository = new UserRepository(jdbcTemplate);
        User user = userRepository.findByEmail(requestDto.getEmail());

        if (user == null) {
            user = new User(requestDto);
            userRepository.save(user);
        }

        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto, user);
        // 관계 매핑
        user.addSchedule(schedule); // 유저가 있어야 일정이 존재하므로


        // 스케줄 저장
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


    public List<ScheduleResponseDto> getAllSchedules(LocalDate updateDate, String username, int page, int size) {
        // 페이징 객체 생성
        Paging paging = new Paging(page, size);

        // ScheduleRepository를 사용하여 조건과 페이징을 기반으로 일정 목록 조회
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        List<ScheduleResponseDto> schedules = scheduleRepository.findAll(updateDate, username, paging);

        // 요청한 페이지가 범위를 넘어선 경우 빈 배열 반환
        if (schedules.isEmpty() && page > 0) {
            return new ArrayList<>(); // 빈 배열 반환
        }

        return schedules; // 조회한 일정 목록 반환
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
            return -1; // 비밀번호 불일 치 시 (관련 로직은 프런트에서 처리)
        } else {
            throw new IllegalArgumentException("Schedule with id " + id + " not found");
        }

    }


}
