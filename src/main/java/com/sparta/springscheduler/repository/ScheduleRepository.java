package com.sparta.springscheduler.repository;

import com.sparta.springscheduler.dto.ScheduleResponseDto;
import com.sparta.springscheduler.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Schedule save(Schedule schedule) {
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
        // post 요청에 대한 응답을 할때 id 값을 응답하기 위한 코드이다.

        return schedule;
    }


    public Schedule findSchedule(Integer id) {
        return findById(id);
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


    public List<ScheduleResponseDto> findAll(LocalDate updateDate, String username) {
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
}
