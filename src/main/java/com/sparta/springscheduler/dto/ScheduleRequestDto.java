package com.sparta.springscheduler.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleRequestDto {

    private String username;
    private String email;
    private String password;
    private String title;
    private String content;
    private LocalDate scheduledDate; // 일정 예정 날짜



}
