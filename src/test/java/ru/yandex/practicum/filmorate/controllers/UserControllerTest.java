package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user = User.builder()
                .name("Testov")
                .email("test@email.ru")
                .birthday(LocalDate.of(2000, 2, 20))
                .login("Testovy")
                .build();
    }

    @Test
    void addUserAndGetUserList() {
        userController.addUser(user);
        List<User> list = userController.getAllUsers();
        assertEquals(1, userController.getAllUsers().size());
        assertEquals(1, list.get(0).getId());
        assertEquals(user.getName(), list.get(0).getName());
        assertEquals(user.getEmail(), list.get(0).getEmail());
        assertEquals(user.getBirthday(), list.get(0).getBirthday());
        assertEquals(user.getLogin(), list.get(0).getLogin());
    }

    @Test
    void addUserWithBlankEmail() {
        user.setEmail("");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
        assertEquals(ValidationException.class, exception.getClass());
    }

    @Test
    void addUserWithWrongEmail() {
        user.setEmail("testemail.ru");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
        assertEquals(ValidationException.class, exception.getClass());
    }

    @Test
    void addUserWithBlankLogin() {
        user.setLogin("");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
        assertEquals(ValidationException.class, exception.getClass());
    }

    @Test
    void addUserWithWrongLogin() {
        user.setLogin("Test ovy");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
        assertEquals(ValidationException.class, exception.getClass());
    }

    @Test
    void addUserWithFutureBirthday() {
        user.setBirthday(LocalDate.of(2135, 11, 30));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
        assertEquals(ValidationException.class, exception.getClass());
    }

    @Test
    void addUserWithNullName() {
        user.setName(null);
        userController.addUser(user);
        List<User> list = userController.getAllUsers();
        assertEquals("Testovy", list.get(0).getName());
    }

    @Test
    void addUserWithBlankName() {
        user.setName("");
        userController.addUser(user);
        List<User> list = userController.getAllUsers();
        assertEquals("Testovy", list.get(0).getName());
    }

    @Test
    void updateUser() {
        userController.addUser(user);
        User user2 = User.builder()
                .id(1)
                .name("Test")
                .email("test1@email.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .login("Testov")
                .build();
        userController.updateUser(user2);
        List<User> list = userController.getAllUsers();
        assertEquals("Test", list.get(0).getName());
        assertEquals("test1@email.com", list.get(0).getEmail());
        assertEquals("Testov", list.get(0).getLogin());
    }

    @Test
    void updateUserWithWrongId() {
        userController.addUser(user);
        User user2 = User.builder()
                .id(2)
                .name("Test")
                .email("test@email.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .login("Testov")
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.updateUser(user2);
                });
        assertEquals("Пользователя с ID 2 нет в базе", exception.getMessage());
    }
}