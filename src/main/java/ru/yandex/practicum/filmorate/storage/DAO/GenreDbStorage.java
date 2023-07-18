package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        String sqlQuery = "SELECT * FROM genre";

        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    @Override
    public Genre getById(int id) {
        final String sqlQuery = "SELECT * FROM genre WHERE genre_id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (!genreRows.next()) {
            log.warn("Жанр {} не найден.", id);
            throw new ObjectNotFoundException("Жанр не найден");

        }
        return jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, id);
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("genre_id");
        String nameGenre = resultSet.getString("name");

        return new Genre(id, nameGenre);
    }
}
