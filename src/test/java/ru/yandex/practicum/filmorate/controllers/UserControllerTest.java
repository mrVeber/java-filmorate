package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    void createTestUser() {
        User user1 = new User(1, "email@test.com", "Testov", "test",
                LocalDate.of(2000,12,31));
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
        User userUpdate = new User(1, "update@email.ru", "Testov1", "Test1",
                LocalDate.of(2001,02,28));
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void addUserWithNonEmail() {
        User user1 = new User(1, "", "Testov", "test",
                LocalDate.of(2000,12,31));
        assertEquals("", user1.getEmail());
    }

    @Test
    void addUserWithNonLogin() {
        User user1 = new User(1, "email@test.com", "", "test",
                LocalDate.of(2000,12,31));
        assertEquals("", user1.getLogin());
    }

    @Test
    void addUserWithNullBirthDay() {
        User user1 = new User(1, "email@test.com", "Testov1", "test",
                LocalDate.of(00,00,00));
        assertEquals(LocalDate.of(00,00,00), user1.getBirthday());
    }
}