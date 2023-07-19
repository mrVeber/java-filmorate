package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Collection<Film> findAll();

    Film add(Film film);

    Film update(Film film);

    Film getById(int id);

    Film deleteById(int id);

    Optional<Film> getFilm(long id);

    Film addLike(int filmId, int userId);

    Film removeLike(int filmId, int userId);

    List<Film> getBestFilms(int count);
}