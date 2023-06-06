package ru.yandex.practicum.filmorate.service;

import org.junit.platform.commons.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class ValidationFilmService {

    private static final LocalDate date = LocalDate.of(1895,12,28);

    public static Film validFilms(Film film) {
        if (film.getReleaseDate().isBefore(date)) {
    throw new ValidationException("Дата релиза не может быть раньше " + date);
        }
        if (StringUtils.isBlank(film.getName())) {
            log.debug("В запросе передан фильм с пустым названием");
            throw new ValidationException("Название фильма - обязательно к заполнению");
        }
        if (film.getDescription().length() > 200) {
            log.debug("В запросе передан фильм с описанием более 200 символов");
            throw new ValidationException("Длина описания не должна превышать 200 символов");
        }
        if (film.getDuration() <= 0) {
            log.debug("В запросе передан фильм с с продолжительностью {}", film.getDuration());
            throw new ValidationException("Продолжительность не может быть 0 или отрицательной");
        }
        return film;
    }
}
