package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
 add-database

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
 main

@Component
 add-database
public class InMemoryUserStorage  {


public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public void addUser(User user) {
        user.setId(++id);
        users.put(id, user);
        log.debug("Создан пользователь: " + user);
    }

    @Override
    public void deleteUser(long id) {
        if (users.remove(id) == null) throw new ObjectNotFoundException("Пользователь с id=" + id + "не найден");
        log.debug("Удалён пользователь c id {}", id);
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId()))
            throw new ObjectNotFoundException("Пользователь с id=" + user.getId() + " не найден");
        users.put(user.getId(), user);
        log.debug("Пользователь {} обновлён", user);
    }

    @Override
    public List<User> getUsers() {
        log.debug("Отправлены все пользователи");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(long id) {
        return users.get(id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        if (userId < 0 || friendId < 0) {
            throw new ObjectNotFoundException("id Пользователя/друга не может ыбть отрицательным");
        }
        User user = users.get(userId);
        User friend = users.get(friendId);
        if (!user.getFriends().contains(friend.getId())) {
            user.addFriend(friendId);
            friend.addFriend(userId);
        } else {
            throw new ObjectNotFoundException("Пользователь с так id=" + id + "уже у вас в друзьях");
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        if (user.getFriends().contains(friendId)) {
            user.deleteFriends(friendId);
            friend.deleteFriends(userId);
        } else {
            throw new ValidationException("Пользователь с Id=" + id + " уже удалён");
        }
    }
 main
}