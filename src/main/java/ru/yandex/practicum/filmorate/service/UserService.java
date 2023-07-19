package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User add(User user) {
        validate(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        validate(user);
        return userStorage.update(user);
    }

    public User getUser(long id) {
        log.debug("Пользователь (id=" + id + ")");
       return Optional.ofNullable(userStorage.getUser(id))
               .orElseThrow(() -> new ObjectNotFoundException("Пользователя с идентификатором " + id + " нет!"));
    }

    public User getById(int id) {
        return userStorage.getById(id);
    }

    public User deleteById(int id) {
        return userStorage.deleteById(id);

    }

    public List<Integer> addFriendship(int firstId, int secondId) {
        return userStorage.addFriendship(firstId, secondId);
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

    public List<Integer> removeFriendship(int firstId, int secondId) {
        return userStorage.removeFriendship(firstId, secondId);
    }

    public List<User> getFriendsListById(int id) {
        return userStorage.getFriendsListById(id);
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {
        return userStorage.getCommonFriendsList(firstId, secondId);
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }

    private void validateId(long userId, long friendId) {
        userStorage.getUser(userId);
        userStorage.getUser(friendId);
    }
    }
}