package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Slf4j
@Component("LikesDbStorage")
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addLike(int filmId, int userId) {
        final String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery, filmId, userId);
        return filmDbStorage.getById(filmId);
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        final String sqlQuery = "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Пользователь c id {} удалил лайк к фильму {}", userId, filmId);
        return filmDbStorage.getById(filmId);
    }
}
