package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void createUser(User user) {
        userStorage.addUser(user);
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    public User getUser(long id) {
        if (!userStorage.getUsers().containsKey(id))
            throw new ObjectNotFoundException("Пользователь с id=" + id + " не найден");
        log.info("Пользователь (id=" + id + ")");
        return userStorage.getUsers().get(id);
    }

    public Map<Long, User> getUsers() {
        return userStorage.getUsers();
    }

    public List<User> getFriends(long id) {
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(id))
            throw new ObjectNotFoundException("Пользователь с id=" + id + " не найден");
        User user = users.get(id);
        log.info("Отправлены все друзья пользователя (id=" + id + ")");
        return user.getFriends()
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        Map<String, User> users = getTwoUsers(id, otherId);
        Set<Long> commonFriends = new HashSet<>(users.get("user1").getFriends());
        commonFriends.retainAll(users.get("user2").getFriends());
        log.info("Отправленные пользователями общие друзья (id=" + id + "/" + otherId + ") ");
        return commonFriends
                .stream()
                .map(userStorage.getUsers()::get)
                .collect(Collectors.toList());
    }

    public void addFriend(long id, long friendId) {
        Map<String, User> users = getTwoUsers(id, friendId);
        users.get("user1").getFriends().add(friendId);
        users.get("user2").getFriends().add(id);
        log.info("Пользователи (id=" + id + "/" + friendId + ") подружились");
    }

    public void deleteFriend(long id, long friendId) {
        Map<String, User> users = getTwoUsers(id, friendId);
        users.get("user1").getFriends().remove(friendId);
        users.get("user2").getFriends().remove(id);
        log.info("Пользователи (id=" + id + "/" + friendId + ") перестали быть друзьями");
    }

    private Map<String, User> getTwoUsers(long id1, long id2) {
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(id1))
            throw new ObjectNotFoundException("Пользователь с id=" + id1 + " не найден");
        if (!users.containsKey(id2))
            throw new ObjectNotFoundException("Пользователь с id=" + id2 + " не найден");
        User user1 = users.get(id1);
        User user2 = users.get(id2);

        Map<String, User> result = new HashMap<>();
        result.put("user1", user1);
        result.put("user2", user2);

        return result;
    }
}
