package ru.yandex.practicum.filmorate.exception;

import lombok.Data;

@Data
public class ResponseBody {
    private final String error;
    private final String description;

}
