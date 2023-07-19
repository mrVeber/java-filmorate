package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;
import java.util.Collection;
import java.util.List;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Collection<Film> findAll() {
        log.info("Список фильмов отправлен");
        return filmStorage.findAll();
    }
  
    private final UserStorage userStorage;

    public void createFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }


    public Film update(Film film) {
        return filmStorage.update(film);
    }
  
    public Film getFilm(long id) {
        log.debug("Фильм (id=" + id + ")");
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new ObjectNotFoundException("Фильм с id " + id + "не найден"));
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public Film deleteById(int id) {
        return  filmStorage.deleteById(id);
    }

    public Film addLike(int filmId, int userId) {
        return filmStorage.addLike(filmId, userId);
    }

    public Film removeLike(int filmId, int userId) {
        return filmStorage.removeLike(filmId, userId);
    }


    public List<Film> getBestFilms(int count) {
        return filmStorage.getBestFilms(count);
    }
  
    private void validateLike(long filmId, long userId) {
        filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
    }
}