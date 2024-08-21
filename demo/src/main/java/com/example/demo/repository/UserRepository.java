package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.User;

@Repository
public class UserRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    };

    private static final RowMapper<String> USERNAME_ROW_MAPPER = (rs, i) -> {
        String username = rs.getString("username");
        return username;
    };

     /**
     * user情報を挿入するメソッド user
     */
    public void insertUser(User user) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(user);
        String sql = """
                INSERT INTO users
                (username, password)
                VALUES (:username, :password);
                """;
        template.update(sql, param);
    }

    /**
     * email(username)からuserを取得するメソッド user
     */
    public User findUserByUsername(String username) {
        String sql = """
                SELECT * FROM users
                WHERE username = :username;
                """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("username", username);
        try {
            User user = template.queryForObject(sql, param, USER_ROW_MAPPER);
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 全てのusernameを取得するメソッド user
     */
    public List<String> findAllUsername() {
        String sql = """
                SELECT username FROM users;
                """;
        List<String> usernameList = template.query(sql, USERNAME_ROW_MAPPER);
        return usernameList;
    }




}
