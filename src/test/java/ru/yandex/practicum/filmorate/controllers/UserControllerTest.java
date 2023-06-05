package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    void createTestUser() {
        User user1 = new User (1, "email@test.com", "Testov", "test",
                LocalDateTime.of(2000,12,31, 00, 00));
        userController.addUser(user1);
    }
    @Test
    void getAllUsers() {
        assertEquals(0, userController.getAllUsers().size());
    }

    @Test
    void addUser() {
        createTestUser();
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void updateUser() {
        createTestUser();
        User userUpdate = new User (1, "update@email.ru", "Testov1", "Test1",
                LocalDateTime.of(2001,02,28,00,00));
        assertEquals(1, userController.getAllUsers().size());
    }
}