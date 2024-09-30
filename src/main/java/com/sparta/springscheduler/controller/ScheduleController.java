package com.sparta.springscheduler.controller;


import com.sparta.springscheduler.dto.ScheduleRequestDto;
import com.sparta.springscheduler.dto.ScheduleResponseDto;
import com.sparta.springscheduler.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 일정 등록 api
    @PostMapping("/schedules")
    public ScheduleResponseDto createdSchedule(@RequestBody ScheduleRequestDto requestDto) {
        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);

        // db 저장

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO schedule (username, email, password, title, content, scheduledDate) VALUES (?, ?, ?, ?, ?, ?) ";

        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            PreparedStatement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getUsername());
                    preparedStatement.setString(2, schedule.getEmail());
                    preparedStatement.setString(3, schedule.getPassword());
                    preparedStatement.setString(4, schedule.getTitle());
                    preparedStatement.setString(5, schedule.getContent());
                    preparedStatement.setDate(6, Date.valueOf(schedule.getScheduledDate()));


                    return preparedStatement;
                },
                keyHolder);

        // db insert 후 받아온 기본 키 확인하기
        Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        schedule.setId(id);

        return new ScheduleResponseDto(schedule);
    }

    // id 값을 파라미터로 받아서 해당하는 일정 단건의 정보 조회하기
    @GetMapping("/schedules/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable Integer id) {

        Schedule schedule = findById(id);
        if(schedule == null) {
            throw new IllegalArgumentException("Schedule with id " + id + " not found");

        }

        return new ScheduleResponseDto(schedule);

    }


    // 조건에 따른 일정 목록 조회 api
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getAllSchedules(@RequestParam(required = false) LocalDate updateDate, @RequestParam(required = false) String username) {

        // 기본 SQL 쿼리
        String sql = "SELECT * FROM schedule";
        List<Object> params = new ArrayList<>();

        // 조건 추가
        boolean hasWhereClause = false;

        if (username != null && !username.isEmpty()) {
            sql += " WHERE username = ?";
            params.add(username);
            hasWhereClause = true;
        }

        if (updateDate != null) {
            sql += (hasWhereClause ? " AND" : " WHERE") + " DATE(updatedAt) = ?";
            params.add(updateDate);
        }

        sql += " ORDER BY updatedAt DESC";

        // PreparedStatement에 인자를 바인딩하면서 쿼리 실행
        return jdbcTemplate.query(sql, params.toArray(), new RowMapper<ScheduleResponseDto>() {

            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Integer id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String title = rs.getString("title");
                String content = rs.getString("content");
                LocalDate scheduledDate = rs.getDate("scheduledDate").toLocalDate();
                LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updatedAt").toLocalDateTime();
                return new ScheduleResponseDto(id, username, email, title, content,scheduledDate, createdAt, updatedAt);
            }
        });
    }

    private Schedule findById(Integer id) {
        // DB 조회
        String sql = "SELECT * FROM schedule WHERE id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(resultSet.getInt("id"));
                schedule.setUsername(resultSet.getString("username"));
                schedule.setEmail(resultSet.getString("email"));
                schedule.setScheduledDate(resultSet.getDate("scheduledDate").toLocalDate());
                schedule.setTitle(resultSet.getString("title"));
                schedule.setContent(resultSet.getString("content"));
                schedule.setCreatedAt(resultSet.getTimestamp("createdAt").toLocalDateTime());
                schedule.setUpdatedAt(resultSet.getTimestamp("updatedAt").toLocalDateTime());
                return schedule;
            }
            else{
                return null;
            }
        },id);
    }

}
