package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Integer> addFriendship(int followedId, int followerId) {
        final String sqlForWriteQuery = "INSERT INTO user_friends (user_id, friend_id, status) " +
                "VALUES (?, ?, ?)";
        final String sqlForUpdateQuery = "UPDATE user_friends SET status = ? " +
                "WHERE user_id = ? AND friend_id = ?";
        final String checkMutualQuery = "SELECT * FROM user_friends WHERE user_id = ? AND friend_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkMutualQuery, followedId, followerId);

        if (userRows.first()) {
            jdbcTemplate.update(sqlForUpdateQuery, FriendshipStatus.CONFIRMED.toString(), followedId, followerId);
        } else {
            jdbcTemplate.update(sqlForWriteQuery, followedId, followerId, FriendshipStatus.REQUIRED.toString());
        }

        log.info("Пользователь c id {} подписался на пользователя с id {}", followedId, followerId);
        return List.of(followedId, followerId);
    }

    @Override
    public List<Integer> removeFriendship(int followedId, int followerId) {
        final String sqlQuery = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sqlQuery, followedId, followerId);
        log.info("Пользователь c id {} отписался от пользователя c id {}", followerId, followedId);
        return List.of(followedId, followerId);
    }
}
