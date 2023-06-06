package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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

    void addTestFilmsWithNonName() {
        Film film1 = new Film(1, "", "desc",
                LocalDate.of(2000, 12,20), 120);
        assertEquals("", film1.getName());
    }

    void addTestFilmsWithNonDesc() {
        Film film1 = new Film(1, "Film1", "",
                LocalDate.of(2000, 12,20), 120);
        assertEquals("", film1.getDescription());
    }

    void addTestFilmsWithMoreDesc() {
        Film film1 = new Film(1, "Film1", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                LocalDate.of(2000, 12,20), 120);
        assertEquals(200, film1.getDescription().length());
    }

    void addTestFilmWithPositiveDuration() {
        Film film1 = new Film(1, "Film1", "",
                LocalDate.of(2000, 12,20), -120);
        assertEquals(0, film1.getDuration());
    }
}