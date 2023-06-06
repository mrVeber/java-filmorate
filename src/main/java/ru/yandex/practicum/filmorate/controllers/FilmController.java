package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidationFilmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/film")
public class FilmController {

    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping()
    public List<Film> getAllFilms() {
        log.debug("Поступил запрос на получение списка фильмов {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        log.debug("Поступил запрос на добавление фильма");
        Film validFilm = ValidationFilmService.validFilms(film);
        validFilm.setId(++id);
        films.put(validFilm.getId(), validFilm);
        log.debug("Фильм с названием '{}' и идентификатором '{}' добавлен", validFilm.getName(), validFilm.getId());
        return films.get(id);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.debug("В запросе передан фильм с некорректным ID: {}", film.getId());
            throw new ValidationException("Фильма с ID " + film.getId() + " в базе не существует");
        }
        Film validFilm = ValidationFilmService.validFilms(film);
        films.put(validFilm.getId(), validFilm);
        log.debug("Фильм с идентификатором {} обновлен", validFilm.getId());
        return films.get(validFilm.getId());
    }
}
