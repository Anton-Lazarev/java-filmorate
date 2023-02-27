package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmStorage filmStorage = new InMemoryFilmStorage();

    @GetMapping
    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        return filmStorage.updateFilm(film);
    }
}
