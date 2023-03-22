package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class MPA {
    private int id;
    @NotEmpty(message = "Наименование рейтинга не может быть пустым")
    @NotBlank(message = "Наименование рейтинга не может быть пустым")
    private String name;
}
