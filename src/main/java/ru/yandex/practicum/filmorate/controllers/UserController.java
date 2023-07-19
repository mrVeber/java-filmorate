package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public User deleteById(@PathVariable int id) {
        return userService.deleteById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<Integer> addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriendship(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<Integer> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.removeFriendship(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        return userService.getFriendsListById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriendsList(id, otherId);
    }
}