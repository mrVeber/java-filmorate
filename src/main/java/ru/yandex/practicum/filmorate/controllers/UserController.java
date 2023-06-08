package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Поступил запрос на получение пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        user.setId(++id);
        checkUser(user);
        users.put(id, user);
        log.debug("Пользователь добавлен: {}", user);
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        checkUser(user);
        if (!users.containsKey(user.getId())) {
            log.debug("В запросе передан пользователь с некорректным ID: {}", user.getId());
            throw new ValidationException("Пользователя с ID " + user.getId() + " нет в базе");
        }
        users.put(user.getId(), user);
        log.debug("Пользователь с ID: {}, обновлен: {}", user.getId(), user);
        return user;
    }

    private User checkUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
