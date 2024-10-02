package com.sparta.springscheduler.repository;

import com.sparta.springscheduler.dto.ScheduleResponseDto;
import com.sparta.springscheduler.dto.ScheduleUpdateRequestDto;
import com.sparta.springscheduler.entity.Schedule;
import com.sparta.springscheduler.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
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

        String sql = "INSERT INTO schedule (password, title, content, scheduledDate, user_id) VALUES (?, ?, ?, ?, ?) ";

        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            PreparedStatement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getPassword());
                    preparedStatement.setString(2, schedule.getTitle());
                    preparedStatement.setString(3, schedule.getContent());
                    preparedStatement.setDate(4, Date.valueOf(schedule.getScheduledDate()));

                    // 유저 ID를 설정
                    if (schedule.getUser() != null) {
                        preparedStatement.setInt(5, schedule.getUser().getId());
                    } else {
                        preparedStatement.setNull(5, Types.INTEGER); // 유저 정보가 없으면 NULL 처리
                    }

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


    public List<ScheduleResponseDto> findAll(LocalDate updateDate, String username) {

        // 기본 SQL 쿼리 (JOIN 사용)
        String sql = "SELECT s.*, u.username, u.email FROM schedule s " +
                "LEFT JOIN user u ON s.user_id = u.id"; // user_id로 schedule과 user 테이블을 조인
        List<Object> params = new ArrayList<>();

        // 조건 추가
        boolean hasWhereClause = false;

        if (username != null && !username.isEmpty()) {
            sql += " WHERE username = ?";
            params.add(username);
            hasWhereClause = true;
        }

        if (updateDate != null) {
            sql += (hasWhereClause ? " AND" : " WHERE") + " DATE(s.updatedAt) = ?";
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


    public void update(Integer id, ScheduleUpdateRequestDto requestDto) {
        String sql = "UPDATE schedule SET title = ?, content= ?, scheduledDate=?, updatedAt = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getContent(),
                requestDto.getScheduledDate(), requestDto.getUpdatedAt(), id);
        UserRepository userRepository = new UserRepository(jdbcTemplate);
        userRepository.update(id, requestDto);
    }


    public void delete(Integer id) {
        // memo 삭제
        String sql = "DELETE FROM schedule WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    public Schedule findById(Integer id) {
        // DB 조회
        String sql = "SELECT * FROM schedule WHERE id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(resultSet.getInt("id"));
                schedule.setScheduledDate(resultSet.getDate("scheduledDate").toLocalDate());
                schedule.setTitle(resultSet.getString("title"));
                schedule.setContent(resultSet.getString("content"));
                schedule.setPassword(resultSet.getString("password"));
                schedule.setCreatedAt(resultSet.getTimestamp("createdAt").toLocalDateTime());
                schedule.setUpdatedAt(resultSet.getTimestamp("updatedAt").toLocalDateTime());
                // user_id 조회
                Integer userId = resultSet.getInt("user_id");

                // UserRepository를 통해 User 객체 조회
                UserRepository userRepository = new UserRepository(jdbcTemplate);
                User user = userRepository.findById(userId);

                // schedule에 User 객체 설정
                schedule.setUser(user);

                return schedule;
            }
            else{
                return null;
            }
        },id);
    }

}
