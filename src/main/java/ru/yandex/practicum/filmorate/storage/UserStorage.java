package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserStorage {
    void addUser(User user);

    void deleteUser(long id);

    void updateUser(User user);

    Collection<User> getUsers();

    User getUser(long id);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);
}