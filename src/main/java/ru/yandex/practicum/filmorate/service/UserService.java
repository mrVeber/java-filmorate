package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addUser(User user) {
        checkUser(user);
        userStorage.addUser(user);
    }

    public void updateUser(User user) {
        checkUser(user);
        userStorage.updateUser(user);
    }

    public User getUser(long id) {
       return Optional.ofNullable(userStorage.getUser(id))
               .orElseThrow(() -> new ObjectNotFoundException("Пользователь с идентификатором " + id + "не найден"));
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public Collection<User> getFriends(long id) {
        User user = getUser(id);
        Collection<User> friends = new ArrayList<>();
        for (long ids : user.getFriends()) {
            friends.add(getUser(ids));
        }
        return friends;
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        User user = getUser(id);
        User otherUser = getUser(otherId);
        Collection<User> commonFriends = new ArrayList<>();
        if (user.getFriends() == null || otherUser.getFriends() == null) {
            return Collections.emptyList();
        }
        for (long ids : user.getFriends()) {
            if (otherUser.getFriends().contains(ids)) {
                commonFriends.add(getUser(ids));
            }
        }
        return commonFriends;
    }

    public void addFriend(long id, long friendId) {
        validateId(id, friendId);
        userStorage.addFriend(id, friendId);
        log.debug("Пользователи id= {} / {} подружились", id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        validateId(id, friendId);
        userStorage.deleteFriend(id, friendId);
        log.debug("Пользователи id= {} / {} перестали быть друзьями",id, friendId);
    }

    private User checkUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private void validateId(long userId, long friendId) {
        Optional.ofNullable(userStorage.getUser(userId))
                .orElseThrow(() -> new ObjectNotFoundException
                        ("Пользователь с идентификатором " + userId + " не зарегистрирован!"));
        Optional.ofNullable(userStorage.getUser(friendId))
                .orElseThrow(() -> new ObjectNotFoundException
                        ("Пользователь с идентификатором " + friendId + " не зарегистрирован!"));
    }
}