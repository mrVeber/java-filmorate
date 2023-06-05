package main.java.ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import main.java.ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    void addTestFilms() {
        Film film1 = new Film(1, "Film1", "desc",
                LocalDateTime.of(2000, 12,20,00,00), 120);
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
                LocalDateTime.of(2002, 12, 22, 00,00), 130);
        filmController.updateFilm(filmTestUpdate);
        assertEquals(1, filmController.getAllFilms().size());
    }
}