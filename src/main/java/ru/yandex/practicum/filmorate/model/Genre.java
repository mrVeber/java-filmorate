package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private int id;
    private String name;
}
