package com.sparta.springscheduler.controller;


import com.sparta.springscheduler.dto.ScheduleDeleteRequestDto;
import com.sparta.springscheduler.dto.ScheduleRequestDto;
import com.sparta.springscheduler.dto.ScheduleResponseDto;
import com.sparta.springscheduler.dto.ScheduleUpdateRequestDto;
import com.sparta.springscheduler.service.ScheduleService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {


    private final ScheduleService scheduleService;


    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.scheduleService = new ScheduleService(jdbcTemplate);
    }



    // 일정 등록 api
    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(requestDto);
    }


    // id 값을 파라미터로 받아서 해당하는 일정 단건의 정보 조회하기
    @GetMapping("/schedules/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable Integer id) {
        return scheduleService.getSchedule(id);

    }


    // 조건에 따른 일정 목록 조회 api (조건이 없을 수도 있음) + 페이징 기능 추가
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getAllSchedules(
            @RequestParam(required = false) LocalDate updateDate, @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return scheduleService.getAllSchedules(updateDate, username, page, size);

    }





    // 선택 일정 수정 (할일, 작성자명 만 수정 가능, 요청 시 비밀번호 함께 전달)
    @PutMapping("/schedules/{id}")
    public Integer updateSchedule(@PathVariable Integer id, @RequestBody ScheduleUpdateRequestDto requestDto) {
        return scheduleService.updateSchedule(id, requestDto);
    }


    // 선택한 일정 삭제 (비밀번호 함께 전달)
    @DeleteMapping("/schedules/{id}")
    public Integer deleteSchedule(@PathVariable Integer id, @RequestBody ScheduleDeleteRequestDto requestDto) {
        return scheduleService.deleteSchedule(id,requestDto);
    }


}
