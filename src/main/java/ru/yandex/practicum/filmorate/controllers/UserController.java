package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Set<User> users = new HashSet<>();

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Поступил запрос на получение пользователей");
        return List.copyOf(users);
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        if (users.contains(user))
            log.info("Поступил запрос на добавление пользователя");
            users.add(user);
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Поступил запрос на обновление пользователя");
        users.add(user);
        return user;
    }
}
