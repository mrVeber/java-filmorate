package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        userService.deleteById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<Long> addFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.followUser(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<Long> removeFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.unfollowUser(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable long id) {
        return userService.getFriendsListById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriendsList(id, otherId);
    }

}