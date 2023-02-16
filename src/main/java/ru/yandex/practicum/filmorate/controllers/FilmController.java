package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    protected int nextID = 1;
    protected Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        log.debug("Текущее количество фильмов в базе: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        FilmValidator.validate(film);
        film.setId(nextID);
        films.put(film.getId(), film);
        nextID++;
        log.debug("Добавлен фильм: {}; его ID: {}; всего фильмов в базе: {}", film.getName(), film.getId(), films.size());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с ID - " + film.getId() + " не найден в базе");
        }
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.debug("Обновлен фильм: {}; его ID: {}", film.getName(), film.getId());
        return film;
    }
}
