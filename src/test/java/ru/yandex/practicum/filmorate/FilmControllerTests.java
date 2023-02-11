package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTests {
    FilmController controller;
    Film film;

    @BeforeEach
    void beforeEach() {
        controller = new FilmController();
        film = new Film();
        film.setName("firstName");
        film.setDescription("description for FIRST film");
        film.setReleaseDate(LocalDate.of(2000, 7, 18));
        film.setDuration(50);
    }

    @Test
    void correctAddFilm() {
        controller.saveFilm(film);
        assertEquals(1, film.getId());
    }

    @Test
    void correctAddSeveralFilms() {
        controller.saveFilm(film);
        controller.saveFilm(film);
        controller.saveFilm(film);
        controller.saveFilm(film);
        controller.saveFilm(film);
        assertEquals(5, controller.getAllFilms().size());
    }

    @Test
    void getExceptionWhenSaveFilmAndNameIsNull() {
        film.setName(null);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.saveFilm(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void getExceptionWhenSaveFilmAndVeryBigDescription() {
        film.setDescription("qwertyuiop[]asdfghjkl;'zxcvbnm,.//.,mnbvcxz';lkjhgfdsa][poiuytrewq" +
                "qwertyuiop[]asdfghjkl;'zxcvbnm,.//.,mnbvcxz';lkjhgfdsa][poiuytrewq" +
                "qwertyuiop[]asdfghjkl;'zxcvbnm,.//.,mnbvcxz';lkjhgfdsa][poiuytrewq" +
                "Описание фильма должно быть меньше 200 символов");
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.saveFilm(film));
        assertEquals("Описание фильма должно быть меньше 200 символов", exception.getMessage());
    }

    @Test
    void getExceptionWhenSaveFilmAndReleaseDateIncorrect() {
        film.setReleaseDate(LocalDate.of(1800, 10, 25));
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.saveFilm(film));
        assertEquals("Дата выпуска фильма должна быть старше 28.12.1895", exception.getMessage());
    }

    @Test
    void getExceptionWhenSaveFilmAndDuration0() {
        film.setDuration(0);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.saveFilm(film));
        assertEquals("Длительность фильма должна быть больше 0", exception.getMessage());
    }

    @Test
    void getExceptionWhenSaveFilmAndDurationLowerZero() {
        film.setDuration(-50);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.saveFilm(film));
        assertEquals("Длительность фильма должна быть больше 0", exception.getMessage());
    }

    @Test
    void correctUpdateFilm() {
        controller.saveFilm(film);
        Film update = new Film();
        update.setId(film.getId());
        update.setDescription("updated description for tests");
        update.setName("UPDATING");
        update.setReleaseDate(LocalDate.of(1900, 12, 24));
        update.setDuration(74);
        controller.updateFilm(update);
        assertEquals(update.getName(), controller.getAllFilms().get(controller.getAllFilms().size() - 1).getName());
    }

    @Test
    void getExceptionWhenUpdateFilmAndIdIncorrect() {
        controller.saveFilm(film);
        Film update = new Film();
        update.setId(50);
        update.setDescription("updated description for tests");
        update.setName("UPDATING");
        update.setReleaseDate(LocalDate.of(2005, 11, 12));
        update.setDuration(124);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateFilm(update));
        assertEquals("Фильм с ID - 50 не найден в базе", exception.getMessage());
    }

    @Test
    void getExceptionWhenUpdateFilmAndNameNull() {
        controller.saveFilm(film);
        Film update = new Film();
        update.setId(film.getId());
        update.setDescription("updated description for tests");
        update.setName(null);
        update.setReleaseDate(LocalDate.of(2005, 11, 12));
        update.setDuration(124);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateFilm(update));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void getExceptionWhenUpdateFilmAndBigDescription() {
        controller.saveFilm(film);
        Film update = new Film();
        update.setId(film.getId());
        update.setDescription("updated description for tests updated description for tests updated description for tests" +
                "updated description for tests updated description for tests updated description for tests" +
                "updated description for tests updated description for tests updated description for tests");
        update.setName("UPDATING");
        update.setReleaseDate(LocalDate.of(2005, 11, 12));
        update.setDuration(124);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateFilm(update));
        assertEquals("Описание фильма должно быть меньше 200 символов", exception.getMessage());
    }

    @Test
    void getExceptionWhenUpdateFilmAndReleaseDateIncorrect() {
        controller.saveFilm(film);
        Film update = new Film();
        update.setId(film.getId());
        update.setDescription("updated description for tests");
        update.setName("UPDATING");
        update.setReleaseDate(LocalDate.of(1000, 1, 1));
        update.setDuration(124);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateFilm(update));
        assertEquals("Дата выпуска фильма должна быть старше 28.12.1895", exception.getMessage());
    }

    @Test
    void getExceptionWhenUpdateFilmAndDuration0() {
        controller.saveFilm(film);
        Film update = new Film();
        update.setId(film.getId());
        update.setDescription("updated description for tests");
        update.setName("UPDATING");
        update.setReleaseDate(LocalDate.of(2002, 11, 23));
        update.setDuration(0);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateFilm(update));
        assertEquals("Длительность фильма должна быть больше 0", exception.getMessage());
    }

    @Test
    void getExceptionWhenUpdateFilmAndDurationLowerZero() {
        controller.saveFilm(film);
        Film update = new Film();
        update.setId(film.getId());
        update.setDescription("updated description for tests");
        update.setName("UPDATING");
        update.setReleaseDate(LocalDate.of(2002, 11, 23));
        update.setDuration(-457);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateFilm(update));
        assertEquals("Длительность фильма должна быть больше 0", exception.getMessage());
    }
}
