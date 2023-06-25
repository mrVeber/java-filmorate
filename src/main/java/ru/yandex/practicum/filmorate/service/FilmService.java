package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void createFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    public Film getFilm(long id) {
        log.info("Фильм (id=" + id + ")");
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new ObjectNotFoundException("Фильм с id " + id + "не найден"));
    }

    public Collection<Film> getFilms() {
       return filmStorage.getFilms();
    }

    public Collection<Film> getPopularFilms(long count) {
       return filmStorage.getPopularFilms(count);
    }

    public void like(long filmId, long userId) {
        validateLike(filmId,userId);
        filmStorage.like(filmId, userId);
    }

    public void dislike(long filmId, long userId) {
        validateLike(filmId,userId);
        filmStorage.dislike(filmId, userId);
    }

    private void validateLike(long filmId, long userId) {
        filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
    }
}