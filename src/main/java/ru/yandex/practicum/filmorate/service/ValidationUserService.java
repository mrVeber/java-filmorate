package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class ValidationUserService {

    public static User validUsers(User user) {
        if (StringUtils.isBlank(user.getEmail()) || StringUtils.isBlank(user.getLogin())) {
            log.debug("Передан пользователь с пустым email: {}, или логином: {}", user.getEmail(), user.getLogin());
            throw new ValidationException("Поля email и логин - обязательны к заполнению");
        }
        if (!user.getEmail().contains("@") || user.getLogin().contains(" ")) {
            log.debug("Передан неправильный email: {}, или логином с пробелом: {}", user.getEmail(), user.getLogin());
            throw new ValidationException("Email должен содержать '@', а логин не должен содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Передана неправильная дата рождения пользователя: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (StringUtils.isBlank(user.getName())) {
            log.debug("Вместо пустого имени присваивается логин");
            user.setName(user.getLogin());
        }
        return user;
    }
}
