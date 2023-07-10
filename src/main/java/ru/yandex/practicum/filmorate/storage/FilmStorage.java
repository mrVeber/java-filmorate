package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    void addFilm(Film film);

    void deleteFilm(long id);

    void updateFilm(Film film);

    Collection<Film> getFilms();

    Optional<Film> getFilm(long id);

    void like(long filmId, long userId);

    void dislike(long filmId, long userId);

    Collection<Film> getPopularFilms(long size);
}