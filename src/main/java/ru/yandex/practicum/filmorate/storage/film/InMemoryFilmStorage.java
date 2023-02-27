package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    protected int nextID = 1;
    protected Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        log.debug("Текущее количество фильмов в базе: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(nextID);
        films.put(film.getId(), film);
        nextID++;
        log.debug("Добавлен фильм: {}; его ID: {}; всего фильмов в базе: {}", film.getName(), film.getId(), films.size());
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с ID - " + film.getId() + " не найден в базе");
        }
        films.put(film.getId(), film);
        log.debug("Обновлен фильм: {}; его ID: {}", film.getName(), film.getId());
        return film;
    }

    public void deleteFilmByID(Integer id) {
        if (!films.containsKey(id)) {
            throw new ValidationException("Фильм с ID - " + id + " не найден в базе");
        }
        films.remove(id);
        log.debug("Фильм с ID {} удалён из базы, в базе осталось {} фильмов", id, films.size());
    }
}
