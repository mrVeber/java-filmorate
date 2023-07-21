package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAll() {
        final String sqlQuery = "SELECT * FROM users";

        log.info("Список пользователей отправлен");
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public User add(User user) {
        final String sqlQuery = "INSERT INTO users (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES ( ?, ?, ?, ?)";
        KeyHolder generatedId = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, generatedId);

        log.info("Пользователь с id {} отправлен", user.getId());
        user.setId(generatedId.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        final String checkQuery = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, user.getId());

        if (!userRows.next()) {
            log.warn("Пользователь с id {} не найден", user.getId());
            throw new ObjectNotFoundException("Пользователь не найден");
        }

        final String sqlQuery = "UPDATE users SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Пользователь c id {} обновлен", user.getId());
        return user;
    }

    @Override
    public User getById(int id) {
        final String sqlQuery = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (!userRows.next()) {
            log.warn("Пользователь с id {} не найден.", id);
            throw new ObjectNotFoundException("Пользователь не найден");
        }

        final String checkQuery = "select * from users where id = ?";

        log.info("Пользователь с id {} отправлен", id);
        return jdbcTemplate.queryForObject(checkQuery, this::makeUser, id);
    }

    @Override
    public User deleteById(int id) {
        final String sqlQuery = "DELETE FROM users WHERE id = ?";
        User user = getById(id);

        jdbcTemplate.update(sqlQuery, id);
        log.info("Пользователь с id {} удален", id);
        return user;
    }

    @Override
    public List<User> getFriendsListById(int id) {
        final String checkQuery = "SELECT * FROM users WHERE id = ?";
        SqlRowSet followingRow = jdbcTemplate.queryForRowSet(checkQuery, id);

        if (!followingRow.next()) {
            log.warn("Пользователь с id {} не найден", id);
            throw new ObjectNotFoundException("Пользователь не найден");
        }

        final String sqlQuery = "SELECT id, email, login, name, birthday " +
                "FROM USERS " +
                "LEFT JOIN user_friends mf on users.id = mf.friend_id " +
                "where user_id = ? AND status LIKE 'REQUIRED'";

        log.info("Запрос получения списка друзей пользователя с id {} выполнен", id);
        return jdbcTemplate.query(sqlQuery, this::makeUser, id);
    }

    @Override
    public List<User> getCommonFriendsList(int followedId, int followerId) {
        final String sqlQuery = "SELECT id, email, login, name, birthday " +
                "FROM user_friends AS mf " +
                "LEFT JOIN users u ON u.id = mf.friend_id " +
                "WHERE mf.user_id = ? AND mf.friend_id IN ( " +
                "SELECT friend_id " +
                "FROM user_friends AS mf " +
                "LEFT JOIN users AS u ON u.id = mf.friend_id " +
                "WHERE mf.user_id = ? )";

        log.info("Список общих друзей {} и {} отправлен", followedId, followerId);
        return jdbcTemplate.query(sqlQuery, this::makeUser, followedId, followerId);
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String name = resultSet.getString("name");
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }
}
