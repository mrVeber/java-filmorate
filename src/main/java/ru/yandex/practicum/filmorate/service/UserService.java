package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addUser(User user) {
        checkUser(user);
        userStorage.addUser(user);
    }

    public void updateUser(User user) {
        checkUser(user);
        userStorage.updateUser(user);
    }

    public User getUser(long id) {
       return userStorage.getUser(id).orElseThrow(() -> new ObjectNotFoundException("Пользователя с таким id" + id + "нет"));
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public Collection<User> getFriends(long id) {
        User user = getUser(id);
        return user.getFriends().stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        User user = getUser(id);
        User otherUser = getUser(otherId);
        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(this::getUser)
                .collect(Collectors.toList());
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
        userStorage.getUser(userId);
        userStorage.getUser(friendId);
    }
}