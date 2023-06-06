package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.valid.ValidationFilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    private FilmController filmController;

    void addTestFilms() {
        Film film1 = new Film(1, "Film1", "desc",
                LocalDate.of(2000, 12,20), 120);
        filmController.addFilm(film1);
    }

    @Test
    void getAllFilms() {
        assertEquals(0, filmController.getAllFilms());
    }

    @Test
    void addFilm() {
        addTestFilms();
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void updateFilm() {
        addTestFilms();

        Film filmTestUpdate = new Film(1, "filmTestUpdate", "desc",
                LocalDate.of(2002, 12, 22), 130);
        filmController.updateFilm(filmTestUpdate);
        assertEquals(1, filmController.getAllFilms().size());
    }
}