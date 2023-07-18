package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
 add-database
import java.util.List;

import java.util.Optional;
 main

public interface FilmStorage {

    Collection<Film> findAll();

    Film add(Film film);

    Film update(Film film);

    Film getById(int id);

 add-database
    Film deleteById(int id);

    Optional<Film> getFilm(long id);
 main

    Film addLike(int filmId, int userId);

    Film removeLike(int filmId, int userId);

    List<Film> getBestFilms(int count);
}