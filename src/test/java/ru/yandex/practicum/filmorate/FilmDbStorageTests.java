package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDbStorageTests {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Order(1)
    @Test
    public void getEmptyFilmsWhenWeDontHaveFilms() {
        assertEquals(0, filmStorage.getAllFilms().size());
    }

    @Order(2)
    @Test
    public void correctAddFilm() {
        Film film = Film.builder()
                .name("firstFilmName")
                .description("description for FIRST film")
                .releaseDate(LocalDate.of(2000, 7, 18))
                .duration(110)
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new ArrayList<>())
                .build();
        Film createdFilm = filmStorage.addFilm(film);
        assertEquals(1, createdFilm.getId());
        assertEquals("firstFilmName", createdFilm.getName());
    }

    @Order(3)
    @Test
    public void getExceptionWhenIncorrectIdInUpdating() {
        Film film = Film.builder()
                .id(158)
                .name("UpdatedFilmName")
                .description("UpdatedDescription")
                .releaseDate(LocalDate.of(2000, 7, 18))
                .duration(110)
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new ArrayList<>())
                .build();
        final FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> filmStorage.updateFilm(film));
        assertEquals("Фильм с ID - 158 не найден в базе", exception.getMessage());
    }

    @Order(4)
    @Test
    public void correctFilmUpdating() {
        Film film = Film.builder()
                .id(1)
                .name("UpdatedFilmName")
                .description("UpdatedDescription")
                .releaseDate(LocalDate.of(2000, 7, 18))
                .duration(110)
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new ArrayList<>())
                .build();
        Film updatedFilm = filmStorage.updateFilm(film);
        assertEquals(1, updatedFilm.getId());
        assertEquals("UpdatedFilmName", updatedFilm.getName());
        assertEquals("UpdatedDescription", updatedFilm.getDescription());
    }

    @Order(5)
    @Test
    public void correctGettingAllFilmsAfterSeveralNewFilms() {
        Film secondFilm = Film.builder()
                .name("SecondFilm")
                .description("SecondFilmDescription")
                .releaseDate(LocalDate.of(2020, 4, 25))
                .duration(90)
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new ArrayList<>())
                .build();
        Film thirdFilm = Film.builder()
                .name("ThirdFilm")
                .description("ThirdFilmDescription")
                .releaseDate(LocalDate.of(2015, 1, 19))
                .duration(145)
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new ArrayList<>())
                .build();
        filmStorage.addFilm(secondFilm);
        filmStorage.addFilm(thirdFilm);
        assertEquals(3, filmStorage.getAllFilms().size());
    }

    @Order(6)
    @Test
    public void getExceptionWhenIncorrectIdInGettingById() {
        final FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> filmStorage.getFilmByID(852));
        assertEquals("Фильм с ID - 852 не найден в базе", exception.getMessage());
    }

    @Order(7)
    @Test
    public void correctGettingFilmByID() {
        Film film = filmStorage.getFilmByID(2);
        assertEquals("SecondFilm", film.getName());
        assertEquals("SecondFilmDescription", film.getDescription());
    }

    @Order(8)
    @Test
    public void getTopWhenDontHaveLikes() {
        List<Film> top = filmStorage.findTopLikedFilms(10);
        assertEquals(3, top.size());
    }

    @Order(9)
    @Test
    public void getExceptionWhenIncorrectIdInAddingLike() {
        final FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> filmStorage.addLike(50, 1));
        assertEquals("Фильм с ID - 50 не найден в базе", exception.getMessage());
    }

    @Order(10)
    @Test
    public void correctLikeAdding() {
        User user = User.builder()
                .email("222@222.ru")
                .login("testLogin")
                .name("TestingName")
                .birthday(LocalDate.of(2002, 2, 23))
                .build();
        userStorage.addUser(user);
        boolean added = filmStorage.addLike(3, 1);
        assertTrue(added);
    }

    @Order(11)
    @Test
    public void getFalseIfAddSameLike() {
        boolean added = filmStorage.addLike(3, 1);
        assertFalse(added);
    }

    @Order(12)
    @Test
    public void getCorrectTopFilmsAfterLikes() {
        List<Film> top = filmStorage.findTopLikedFilms(10);
        assertEquals(3, top.size());
        assertEquals("ThirdFilm", top.get(0).getName());
    }

    @Order(13)
    @Test
    public void correctDeletingLike() {
        boolean deleted = filmStorage.removeLike(3, 1);
        assertTrue(deleted);
    }

    @Order(14)
    @Test
    public void getFalseWhenTryDeleteAlreadyDeletedLike() {
        boolean deleted = filmStorage.removeLike(3, 1);
        assertFalse(deleted);
    }

    @Order(15)
    @Test
    public void getExceptionWhenIncorrectFilmIdInDeletingLike() {
        final FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> filmStorage.removeLike(147, 1));
        assertEquals("Фильм с ID - 147 не найден в базе", exception.getMessage());
    }

    @Order(16)
    @Test
    public void getCorrectTopFilmsAfterRemovingLikes() {
        List<Film> top = filmStorage.findTopLikedFilms(10);
        assertEquals(3, top.size());
        assertEquals("UpdatedFilmName", top.get(0).getName());
    }

    @Order(17)
    @Test
    public void getFalseWhenIdIsNotPresentInBase() {
        assertFalse(filmStorage.idIsPresent(567));
    }

    @Order(18)
    @Test
    public void getTrueWhenIdIsPresentInBase() {
        assertTrue(filmStorage.idIsPresent(2));
    }
}
