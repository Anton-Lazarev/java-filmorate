package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.LocalDateAfter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NotEmpty(message = "Название фильма не может быть пустым")
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма должно быть меньше 200 символов")
    private String description;
    @LocalDateAfter(value = "28.12.1895", message = "Дата выпуска фильма должна быть старше 28.12.1895")
    private LocalDate releaseDate;
    @Min(value = 1, message = "Длительность фильма должна быть больше 0")
    private int duration;
}
