package com.sparta.springscheduler.dto;

import com.sparta.springscheduler.entity.Schedule;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private Integer id;
    private String username;
    private String email;
    private String title;
    private String content;
    private LocalDate scheduledDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer user_id;

    // POST 로 입력 후 응답하기 위한 DTO
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.username = schedule.getUser().getUsername();
        this.email = schedule.getUser().getEmail();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.scheduledDate = schedule.getScheduledDate();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
        this.user_id = schedule.getUser().getId();
    }


    // GET Method 로 정보 불러올 때 응답하기 위한 DTO
    public ScheduleResponseDto(Integer id, String username, String email, String title, String content, LocalDate scheduledDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.title = title;
        this.content = content;
        this.scheduledDate = scheduledDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}

