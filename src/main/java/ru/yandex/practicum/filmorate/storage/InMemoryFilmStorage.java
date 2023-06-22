package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 0;

    @Override
    public void addFilm(Film film) {
        film.setId(++idCounter);
        films.put(idCounter, film);
        log.info("Создан фильм " + film);
    }

    @Override
    public void deleteFilm(long id) {
        if (!films.containsKey(id))
            throw new ObjectNotFoundException("Фильм с id=" + id + " не найден");
        films.remove(id);
        log.info("Удалён фильм: (id=" + id + ")");
    }

    @Override
    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId()))
            throw new ObjectNotFoundException("Фильм с id=" + film.getId() + " не найден");
        films.put(film.getId(), film);
        log.info("Обновлен фильм: " + film);
    }

    @Override
    public Map<Long, Film> getFilms() {
        log.info("Отправлены все фильмы");
        return films;
    }

}
