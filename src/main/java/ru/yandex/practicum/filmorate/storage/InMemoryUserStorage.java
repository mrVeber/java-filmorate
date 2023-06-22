package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private Map<Long, User> users = new HashMap<>();

    private long idCounter = 0;

    @Override
    public void addUser(User user) {
        checkUser(user);
        user.setId(++idCounter);

        users.put(idCounter, user);
        log.info("Создан пользователь: " + user);
    }

    @Override
    public void deleteUser(long id) {
        if (!users.containsKey(id))
            throw new ObjectNotFoundException("Пользователь с id=" + id + " не найден");
        users.remove(id);
        log.info("Удалён пользователь: (id=" + id + ")");
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId()))
            throw new ObjectNotFoundException("Пользователь с id=" + user.getId() + " не найден");
        checkUser(user);
        users.put(user.getId(), user);
        log.info("Пользователь " + user + "обновлён");
    }

    @Override
    public Map<Long, User> getUsers() {
        log.info("Отправлены все пользователи");
        return users;
    }

    private User checkUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
