package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void createFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    public Film getFilm(long id) {
        log.info("Фильм (id=" + id + ")");
        return Optional.ofNullable(filmStorage.getFilm(id))
                .orElseThrow(() -> new ObjectNotFoundException("Фильм с id " + id + "не найден"));
    }

    public Collection<Film> getFilms() {
       return filmStorage.getFilms();
    }

    public Collection<Film> getPopularFilms(long count) {
       return filmStorage.getPopularFilms(count);
    }

    public void like(long filmId, long userId) {
        filmStorage.like(filmId, userId);
    }

    public void dislike(long filmId, long userId) {
        filmStorage.dislike(filmId, userId);
    }
}