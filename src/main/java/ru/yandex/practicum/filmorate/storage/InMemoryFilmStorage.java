package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 0;

    @Override
    public void addFilm(Film film) {
        film.setId(++idCounter);
        films.put(idCounter, film);
        log.debug("Создан фильм {}", film);
    }

    @Override
    public void deleteFilm(long id) {
        if (!films.containsKey(id))
            throw new ObjectNotFoundException("Фильм с id=" + id + " не найден");
        films.remove(id);
        log.debug("Удалён фильм: id= {}", id);
    }

    @Override
    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId()))
            throw new ObjectNotFoundException("Фильм с id=" + film.getId() + " не найден");
        films.put(film.getId(), film);
        log.debug("Обновлен фильм: {}", film);
    }

    @Override
    public Collection<Film> getFilms() {
        Collection<Film> allFilms = films.values();
        if(allFilms.isEmpty()) {
            allFilms.addAll(films.values());
        }
        log.debug("Отправлены все фильмы");
        return allFilms;
    }

    @Override
    public Film getFilm(long id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> getPopularFilms(long size) {
        return getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(size)
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public void like(long filmId, long userId) {
        Film film = films.get(filmId);
        film.addLike(userId);
        updateFilm(film);
    }

    @Override
    public void dislike(long filmId, long userId) {
        Film film = films.get(filmId);
        film.deleteLike(userId);
        updateFilm(film);
    }
}