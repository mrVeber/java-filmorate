package ru.yandex.practicum.filmorate.valid;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class ValidationFilmService {

    private static LocalDate date = LocalDate.of(1895,12,28);

    public void validDateFilm(Film film) {
        if (film.getReleaseDate().isBefore(date)) {
    throw new ValidationException("Дата релиза не может быть раньше " + date);
        }
    }
}
