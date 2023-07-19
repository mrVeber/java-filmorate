package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;

@Component
public class InMemoryFilmStorage {

 add-database

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
        if (films.remove(id) == null) throw new ObjectNotFoundException("Фильм с id=" + id + "не найден");
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
    public List<Film> getFilms() {
        log.debug("Отправлены все фильмы");
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilm(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Set<Film> getPopularFilms(long size) {
        return getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(size)
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public void like(long filmId, long userId) {
        Film film = films.get(filmId);
        film.addLike(userId);
    }

    @Override
    public void dislike(long filmId, long userId) {
        if (filmId < 0 || userId < 0) {
            throw new ObjectNotFoundException("id фильма/юзера не может быть отрицательным!");
        }
        Film film = films.get(filmId);
        film.deleteLike(userId);
    }
 main
}