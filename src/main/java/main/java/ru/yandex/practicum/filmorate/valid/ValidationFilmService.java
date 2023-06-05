package main.java.ru.yandex.practicum.filmorate.valid;

import main.java.ru.yandex.practicum.filmorate.exception.ValidationException;
import main.java.ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDateTime;

public class ValidationFilmService {

    private static LocalDateTime date = LocalDateTime.of(1895,12,28, 00,00);

    public void validDateFilm(Film film) {
        if (film.getReleaseDate().isBefore(date)) {
            throw new ValidationException("Дата релиза не может быть раньше " + date);
        }
    }
}