package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final JdbcTemplate jdbcTemplate;
    private final FriendStorage friendStorage;

    public List<Integer> addFriendship(int firstId, int secondId) {
        validateFr(firstId, secondId);
        return friendStorage.addFriendship(firstId, secondId);
    }

    public List<Integer> removeFriendship(int firstId, int secondId) {
        return friendStorage.removeFriendship(firstId, secondId);
    }

    private void validateFr(int followedId, int followerId) {
        final String check = "SELECT * FROM users WHERE id = ?";
        SqlRowSet followingRow = jdbcTemplate.queryForRowSet(check, followedId);
        SqlRowSet followerRow = jdbcTemplate.queryForRowSet(check, followerId);

        if (!followingRow.next() || !followerRow.next()) {
            log.info("Пользователи с id {} и c id {} не найдены", followedId, followerId);
            throw new ObjectNotFoundException("Пользователи не найдены");
        }
    }
}
