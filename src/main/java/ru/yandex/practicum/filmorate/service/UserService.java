package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {
    private final UserStorage userDbStorage;
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;

    public List<User> findAll() {
        log.info("Список пользователей отправлен");

        return userDbStorage.findAll();
    }

    public User create(User user) {
        validate(user);
        log.info("Пользователь добавлен");

        return userDbStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        log.info("Пользователь {} обновлен", user.getId());

        return userDbStorage.update(user);
    }

    public User getById(long id) {
        log.info("Пользователь с id {} отправлен", id);

        return userDbStorage.getById(id).orElseThrow(() -> {
            log.warn("Пользователь с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Пользователь не найден");
        });
    }

    public void deleteById(long id) {
        log.info("Удалить пользователя {}", id);
        int result = userDbStorage.deleteById(id);
        if (result == 0) throw new ObjectNotFoundException("Пользователь не найден");
    }

    public List<Long> followUser(long followerId, long followingId) {
        usersValidation(followerId, followingId);
        log.info("Пользователь {} подписался на {}", followerId, followingId);
        return userDbStorage.followUser(followerId, followingId);
    }

    public List<Long> unfollowUser(long followerId, long followingId) {
        log.info("Пользователь {} отписался от {}", followerId, followingId);
        return userDbStorage.unfollowUser(followerId, followingId);
    }

    public List<User> getFriendsListById(long id) {
        getById(id);
        log.info("Запрос получения списка друзей пользователя {} выполнен", id);

        return userDbStorage.getFriendsListById(id);
    }

    public List<User> getCommonFriendsList(long firstId, long secondId) {
        usersValidation(firstId, secondId);
        log.info("Список общих друзей {} и {} отправлен", firstId, secondId);

        return userDbStorage.getCommonFriendsList(firstId, secondId);
    }

    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }

    public void usersValidation(long followerId, long followingId) {
        getById(followerId);
        getById(followingId);
    }
}