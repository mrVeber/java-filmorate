package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.valid.ValidationFilmService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/film")
public class FilmController {
    private ValidationFilmService validationFilmService;

    private Set<Film> films = new HashSet<>();

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Поступил запрос на получение списка фильмов");
        return List.copyOf(films);
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Поступил запрос на добавление фильма");
        validationFilmService.validDateFilm(film);
        films.add(film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        if(!films.contains(film)) {
            log.info("Поступил запрос на обновление фильма");
            validationFilmService.validDateFilm(film);
            films.add(film);
        }
        return film;
    }
}
