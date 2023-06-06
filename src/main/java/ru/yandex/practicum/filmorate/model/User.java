package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @NotBlank
    @Email(regexp = "^[A-Z0-9+_.-]+@[A-Z0-9.-]+$", message = "Некорректный email!")
    private String email;
    @NotBlank
    @Pattern(regexp = "\\s*", message = "Логин не должен содержать пробелов.")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthdate) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthdate;
    }
}
