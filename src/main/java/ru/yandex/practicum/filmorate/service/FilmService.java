package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.ValidationException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        FilmValidator.validate(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        FilmValidator.validate(film);
        return filmStorage.updateFilm(film);
    }

    public Film findFilmByID(Integer id) {
        return filmStorage.getFilmByID(id);
    }

    public String addLike(Integer filmID, Integer userID) {
        if (!userService.checkUserIdInStorage(userID)) {
            throw new UserNotFoundException("Пользователь с ID - " + userID + " не найден в базе");
        }
        boolean success = filmStorage.addLike(filmID, userID);
        if (success) {
            return String.format("Фильм с ID %d получил лайк от пользователя с ID %d", filmID, userID);
        } else {
            return String.format("Пользователь с ID %d уже лайкнул фильм с ID %d", userID, filmID);
        }
    }

    public String removeLike(Integer filmID, Integer userID) {
        if (!userService.checkUserIdInStorage(userID)) {
            throw new UserNotFoundException("Пользователь с ID - " + userID + " не найден в базе");
        }
        boolean success = filmStorage.removeLike(filmID, userID);
        if (success) {
            return String.format("Лайк от пользователя с ID %d удален у фильма с ID %d", userID, filmID);
        } else {
            return String.format("Лайк от пользователя с ID %d отсутствует у фильма с ID %d", userID, filmID);
        }
    }

    public List<Film> findTopLikedFilms(Integer count) {
        if (count <= 0) {
            throw new ValidationException("Количество выводимых фильмов должно быть больше 0");
        }
        //Здесь ниже в сортировках используем отрицательное значение компаратора, чтоб добиться убывающего порядка
        if (filmStorage.getAllFilms().size() <= count) {
            return filmStorage.getAllFilms().stream().sorted(Comparator.comparingInt(film -> - film.getLikes().size()))
                    .collect(Collectors.toList());
        }
        return filmStorage.getAllFilms().stream().sorted(Comparator.comparingInt(film -> - film.getLikes().size()))
                .limit(count).collect(Collectors.toList());
    }
}
