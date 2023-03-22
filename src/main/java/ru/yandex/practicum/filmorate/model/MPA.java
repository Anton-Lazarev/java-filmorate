package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class MPA {
    private int id;
    @NotEmpty(message = "Наименование рейтинга не может быть пустым")
    @NotBlank(message = "Наименование рейтинга не может быть пустым")
    private String name;
}
