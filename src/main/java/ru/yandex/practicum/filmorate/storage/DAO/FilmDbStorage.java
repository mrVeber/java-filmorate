package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Film> findAll() {
        String sql = "SELECT films.*, m.* " +
                "FROM films " +
                "JOIN mpa m ON m.MPA_ID = films.mpa_id";

        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm);
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        addGenres(film);

        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ?" +
                "WHERE FILM_ID = ?";
        deleteGenres(film);
        addGenres(film);
        int result = jdbcTemplate.update(sql,
                film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (result < 1) throw new DataException("Фильм не найден в базе");

        return film;
    }

    @Override
    public Optional<Film> getById(long id) {
        String sql = "SELECT films.*, m.* " +
                "FROM films " +
                "JOIN mpa m ON m.MPA_ID = films.mpa_id " +
                "WHERE films.film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, FilmDbStorage::makeFilm, id);
            return Optional.ofNullable(film);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int deleteById(long id) {
        String sql = "DELETE FROM films WHERE FILM_ID = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Film> addLike(long filmId, long userId) {
        String sql = "MERGE INTO films_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);

        return getById(filmId);
    }

    @Override
    public Optional<Film> removeLike(long filmId, long userId) {
        String sql = "DELETE FROM films_likes " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);

        return getById(filmId);
    }

    @Override
    public List<Film> getBestFilms(int count, Integer genre, Integer year) {
        final StringBuilder sqlBuilder = new StringBuilder(
                "SELECT films.FILM_ID, films.name, description, release_date, duration, m.mpa_id, m.name " +
                        "FROM films " +
                        "LEFT JOIN films_likes fl ON films.FILM_ID = fl.film_id " +
                        "LEFT JOIN mpa m ON m.MPA_ID = films.mpa_id "
        );
        if (genre != null && year != null) {
            sqlBuilder.append("LEFT JOIN film_genre fg ON films.FILM_ID = fg.film_id " +
                    "WHERE fg.genre_id = :genre " +
                    "AND EXTRACT(YEAR FROM cast(release_date AS date)) = :year ");
        }
        if (genre != null && year == null) {
            sqlBuilder.append("LEFT JOIN film_genre fg ON films.FILM_ID = fg.film_id " +
                    "WHERE fg.GENRE_ID = :genre ");
        }
        if (genre == null && year != null) {
            sqlBuilder.append("WHERE EXTRACT(YEAR FROM cast(release_date AS date)) = :year ");
        }
        sqlBuilder.append(
                "GROUP BY films.FILM_ID, " +
                        "fl.film_id IN (SELECT film_id " +
                        "FROM films_likes) " +
                        "ORDER BY COUNT(fl.film_id) DESC " +
                        "LIMIT :count ;"
        );

        final String sql = sqlBuilder.toString();

        SqlParameterSource parameters = new MapSqlParameterSource("count", count)
                .addValue("genre", genre)
                .addValue("year", year);
        return namedParameterJdbcTemplate.query(sql, parameters, FilmDbStorage::makeFilm);
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        String sql = "SELECT f.*, M.* " +
                "FROM FILMS_LIKES " +
                "JOIN FILMS_LIKES fl ON fl.FILM_ID = FILMS_LIKES.FILM_ID " +
                "JOIN FILMS f on f.film_id = fl.film_id " +
                "JOIN MPA M on f.mpa_id = M.MPA_ID " +
                "WHERE fl.USER_ID = ? AND FILMS_LIKES.USER_ID = ?";

        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, userId, friendId);
    }


    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long duration = rs.getLong("duration");
        Mpa mpa = new Mpa(rs.getInt("mpa.mpa_id"), rs.getString("mpa.name"));

        return new Film(id, name, description, releaseDate, duration, mpa, new LinkedHashSet<>());
    }

    private void addGenres(Film film) {
        if (film.getGenres() != null) {
            String updateGenres = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(
                    updateGenres, film.getGenres(), film.getGenres().size(),
                    (ps, genre) -> {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, genre.getId());
                    });
        } else film.setGenres(new LinkedHashSet<>());
    }

    private void deleteGenres(Film film) {
        String deleteGenres = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(deleteGenres, film.getId());
    }

}
