package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Slf4j
@Component
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
        if (!users.containsKey(id))
            throw new ObjectNotFoundException("Пользователь с id=" + id + "не найден");
        users.remove(id);
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
    public Collection<User> getUsers() {
        Collection<User> allUsers = users.values();
        log.debug("Отправлены все пользователи");
        if (allUsers.isEmpty()) {
            allUsers.addAll(users.values());
        }
        return allUsers;
    }

    @Override
    public User getUser(long userId) {
        return users.get(userId);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        updateUser(user);
        updateUser(friend);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.deleteFriends(friendId);
        friend.deleteFriends(userId);
        updateUser(user);
        updateUser(friend);
    }

}