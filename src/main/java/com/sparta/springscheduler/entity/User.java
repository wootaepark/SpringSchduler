package com.sparta.springscheduler.entity;

import com.sparta.springscheduler.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class User {

    private Integer id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // User가 여러 개의 Schedule을 가질 수 있는 관계
    private List<Schedule> schedules = new ArrayList<>();


    public User(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    // 스케줄을 추가하는 메서드
    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }
}
