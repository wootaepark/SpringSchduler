package com.sparta.springscheduler.entity;

import com.sparta.springscheduler.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private String title;
    private String content;
    private LocalDate scheduledDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Schedule(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.scheduledDate = requestDto.getScheduledDate();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }


}
