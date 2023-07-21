package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FriendStorage {

    List<Integer> addFriendship(int firstId, int secondId);

    List<Integer> removeFriendship(int firstId, int secondId);
}
