package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping()
    public List<Film> getAllFilms() {
        log.debug("Поступил запрос на получение списка фильмов {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("Фильм с названием '{}' и идентификатором '{}' добавлен", film.getName(), film.getId());
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.debug("В запросе передан фильм с некорректным ID: {}", film.getId());
            throw new ValidationException("Фильма с ID " + film.getId() + " в базе не существует");
        }
        films.put(film.getId(), film);
        log.debug("Фильм с идентификатором {} обновлен", film.getId());
        return film;
    }
}
