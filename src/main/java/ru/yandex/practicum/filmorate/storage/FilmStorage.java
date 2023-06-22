package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    void addFilm(Film film);

    void deleteFilm(long id);

    void updateFilm(Film film);

    Map<Long, Film> getFilms();

}
