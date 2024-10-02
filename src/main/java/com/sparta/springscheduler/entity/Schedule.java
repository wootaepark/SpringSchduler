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
    private String password;
    private String title;
    private String content;
    private LocalDate scheduledDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // Schedule이 소속된 User를 참조하는 필드

    @Setter// User 설정 메서드
    private User user;


    public Schedule(ScheduleRequestDto requestDto, User user) {
        this.password = requestDto.getPassword();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.scheduledDate = requestDto.getScheduledDate();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.user = user;
    }


}
