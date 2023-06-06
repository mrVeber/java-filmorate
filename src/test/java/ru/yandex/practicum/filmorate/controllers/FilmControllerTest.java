package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = Film.builder()
                .name("Film1")
                .description("desc1")
                .releaseDate(LocalDate.of(1970, 5, 30))
                .duration(83)
                .build();
    }

    @Test
    void addFilmAndGetFilmList() {
        filmController.addFilm(film);
        List<Film> list = filmController.getAllFilms();
        assertEquals(1, filmController.getAllFilms().size());
        assertEquals(film.getName(), list.get(0).getName());
        assertEquals(film.getDescription(), list.get(0).getDescription());
        assertEquals(film.getDuration(), list.get(0).getDuration());
        assertEquals(film.getReleaseDate(), list.get(0).getReleaseDate());
    }

    @Test
    void addFilmWithNullNameShouldThrowNullPointerException() {

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    Film film2 = Film.builder()
                            .name("")
                            .description("desc1")
                            .releaseDate(LocalDate.of(1970, 5, 30))
                            .duration(83)
                            .build();
                    ;
                    filmController.addFilm(film2);
                });
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    void addFilmWithBlankNameShouldThrowException() {
        film.setName("");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.addFilm(film);
                });
        assertEquals("Название фильма - обязательно к заполнению", exception.getMessage());
    }

    @Test
    void addFilmWithBigDescriptionShouldThrowException() {
        film.setDescription("Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.addFilm(film);
                });
        assertEquals("Длина описания не должна превышать 200 символов", exception.getMessage());
    }

    @Test
    void addFilmWithOldReleaseDateShouldThrowException() {
        film.setReleaseDate(LocalDate.of(1800, 10, 4));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.addFilm(film);
                });
        assertEquals("Дата релиза не может быть раньше 1895-12-28", exception.getMessage());
    }

    @Test
    void addFilmWithMinusDurationShouldThrowException() {
        film.setDuration(-120);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.addFilm(film);
                });
        assertEquals("Продолжительность не может быть 0 или отрицательной", exception.getMessage());
    }

    @Test
    void updateFilm() {
        filmController.addFilm(film);
        Film film2 = Film.builder()
                .id(1)
                .name("Film1")
                .description("Decs1")
                .releaseDate(LocalDate.of(1990, 6, 4))
                .duration(83)
                .build();
        filmController.updateFilm(film2);
        List<Film> list = filmController.getAllFilms();
        assertEquals("Film1", list.get(0).getName());
        assertEquals("Decs1", list.get(0).getDescription());
    }

    @Test
    void updateFilmWithWrongId() {
        filmController.addFilm(film);
        Film film2 = Film.builder()
                .id(2)
                .name("Film2")
                .description("desc2")
                .releaseDate(LocalDate.of(1970, 5, 30))
                .duration(83)
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.updateFilm(film2);
                });
        assertEquals("Фильма с ID 2 в базе не существует", exception.getMessage());
    }
}