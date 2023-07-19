package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    Collection<User> getAll();

    User add(User user);

    User update(User user);

    User getById(int id);

    User deleteById(int id);

    List<Integer> addFriendship(int firstId, int secondId);

    List<Integer> removeFriendship(int firstId, int secondId);

    List<User> getFriendsListById(int id);

    List<User> getCommonFriendsList(int firstId, int secondId);
}