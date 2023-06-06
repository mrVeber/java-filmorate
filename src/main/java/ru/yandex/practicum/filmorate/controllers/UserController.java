package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationUserService;

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
    public User addUser(@RequestBody User user) {
        User validUser = ValidationUserService.validUsers(user);
        validUser.setId(++id);
        users.put(id, validUser);
        log.debug("Пользователь добавлен: {}", validUser);
        return users.get(id);
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("В запросе передан пользователь с некорректным ID: {}", user.getId());
            throw new ValidationException("Пользователя с ID " + user.getId() + " нет в базе");
        }
        User validUser = ValidationUserService.validUsers(user);
        users.put(validUser.getId(), validUser);
        log.debug("Пользователь с ID: {}, обновлен: {}", validUser.getId(), validUser);
        return users.get(validUser.getId());
    }
}
