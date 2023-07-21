package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface LikeStorage {

    Film addLike(int filmId, int userId);

    Film removeLike(int filmId, int userId);
}
