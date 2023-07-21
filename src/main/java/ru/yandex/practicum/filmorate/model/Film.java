package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validators.ReleaseDate;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Film {

    @PositiveOrZero
    private int id;
    @NotBlank(message = "Не правильное название фильма")
    private String name;
    @NotNull(message = "Отсутствует описание фильма")
    @Size(max = 200, message = "слишком длинное описание, больше 200 символов")
    private String description;
    @NotNull
    @ReleaseDate
    private LocalDate releaseDate;
    @Min(value = 1, message = "Неправильная продолжительность фильма")
    @Positive
    private long duration;
    @NotNull
    private Mpa mpa;
    private List<Genre> genres;
//    private Set<Genre> genres;
//    private LinkedHashSet<Genre> genres;

}
