package com.sparta.springscheduler.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ScheduleUpdateRequestDto {
    private String title;
    private String content;
    private String username;
    private String password; // 함께 전달될 비밀번호
    private LocalDate scheduledDate; // 일정 예정 날짜
    private LocalDateTime updatedAt = LocalDateTime.now();// 현재 시간으로 설정
}
