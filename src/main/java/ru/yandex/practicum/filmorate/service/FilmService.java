package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    public List<Film> findAll() {
        List<Film> films = filmStorage.findAll();
        genreStorage.loadGenres(films);

        log.info("Список фильмов отправлен");

        return films;
    }

    public Film create(Film film) {
        log.info("Фильм добавлен");

        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.info("Фильм {} обновлен", film.getId());

        return filmStorage.update(film);
    }

    public Film getById(long id) {
        Film film = filmStorage.getById(id).orElseThrow(() -> {
            log.warn("Фильм с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Фильм не найден");
        });
        genreStorage.loadGenres(Collections.singletonList(film));
        log.info("Фильм с id {} отправлен", id);

        return film;
    }

    public void deleteById(long id) {
        log.info("Удалить фильм {}", id);
        int result = filmStorage.deleteById(id);
        if (result == 0) throw new ObjectNotFoundException("Фильм не найден");
    }

    public Film addLike(long filmId, long userId) {
        if (filmStorage.getById(filmId).isEmpty() || userStorage.getById(userId).isEmpty()) {
            log.warn("Пользователь c id {} или фильм с id {} не найден.", userId, filmId);
            throw new ObjectNotFoundException("Пользователь или фильм не найдены");
        }
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
        return filmStorage.addLike(filmId, userId).orElseThrow();
    }

    public Film removeLike(long filmId, long userId) {
        if (userStorage.getById(userId).isEmpty()) {
            log.warn("Пользователь {} не найден.", userId);
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);

        return filmStorage.removeLike(filmId, userId).orElseThrow(() -> {
            log.warn("Фильм {} не найден.", filmId);
            throw new ObjectNotFoundException("Фильм не найден");
        });
    }

    public List<Film> getBestFilms(int count, Integer genreId, Integer year) {
        log.info("Запрошен список популярных фильмов. " +
                "Параметры: count={}, genreId={}, year={}", count, genreId, year);
        List<Film> films = filmStorage.getBestFilms(count, genreId, year);
        genreStorage.loadGenres(films);

        return films;
    }
}