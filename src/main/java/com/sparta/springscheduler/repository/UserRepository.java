package com.sparta.springscheduler.repository;

import com.sparta.springscheduler.dto.ScheduleUpdateRequestDto;
import com.sparta.springscheduler.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Objects;

public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO user (username, email, createdAt, updatedAt) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(user.getCreatedAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(user.getUpdatedAt())); // 수정일이 없으면 NULL로 처리 가능

            return preparedStatement;
        },keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue()); // ID를 설정하여 null이 아닌 상태로 만듭니다.
    }

    public void update(Integer id, ScheduleUpdateRequestDto requestDto) {
        String sql = "UPDATE user SET username = ?, updatedAt = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getUpdatedAt(), id);
    }

    
    // 유저를 찾는 메서드 (username, email) 을 통해서
    public User findByEmail(String email) {
        // DB 조회
        String sql = "SELECT * FROM user WHERE email = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setCreatedAt(resultSet.getTimestamp("createdAt").toLocalDateTime());
                user.setUpdatedAt(resultSet.getTimestamp("updatedAt").toLocalDateTime());
                return user;
            }
            else{
                return null;
            }
        }, email);
    }


    public User findById(Integer id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setCreatedAt(resultSet.getTimestamp("createdAt").toLocalDateTime());
                user.setUpdatedAt(resultSet.getTimestamp("updatedAt").toLocalDateTime());
                return user;
            }
            else{
                return null;
            }
        },id);
    }

    
}
