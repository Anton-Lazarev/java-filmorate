package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class Genre {
    private int id;
    @NotEmpty(message = "Наименование жанра не может быть пустым")
    @NotBlank(message = "Наименование жанра не может быть пустым")
    private String name;
}
