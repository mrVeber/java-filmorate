package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.validators.ReleaseDate;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Film {
    @PositiveOrZero
    private long id;
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private Integer duration;
    @JsonIgnore
    private List<Long> likes = new ArrayList<>();

    public void addLike(long id) {
        likes.add(id);
    }

    public void deleteLike(long id) {
        likes.remove(id);
    }
}
