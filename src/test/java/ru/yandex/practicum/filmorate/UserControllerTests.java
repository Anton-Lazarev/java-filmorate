package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTests {
    UserController controller;
    User user;

    @BeforeEach
    void beforeEach() {
        controller = new UserController();
        user = new User();
        user.setEmail("123@123.ru");
        user.setLogin("testLogin");
        user.setName("TestingName");
        user.setBirthday(LocalDate.of(2002, 2, 23));
    }

    @Test
    void correctSaveUser() {
        controller.saveUser(user);
        assertEquals(1, user.getId());
    }

    @Test
    void correctSaveSeveralUsers() {
        controller.saveUser(user);
        controller.saveUser(user);
        controller.saveUser(user);
        controller.saveUser(user);
        controller.saveUser(user);
        controller.saveUser(user);
        controller.saveUser(user);
        assertEquals(7, controller.getAllUsers().size());
    }

    @Test
    void getExceptionWhenSaveUserAndEmailIncorrect() {
        user.setEmail("dfghjkasdj.ru");
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.saveUser(user));
        assertEquals("Передана некорректная почта", exception.getMessage());
    }

    @Test
    void getExceptionWhenSaveUserAndLoginIsNull() {
        user.setLogin(null);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.saveUser(user));
        assertEquals("Логин пользователя не может быть пустым", exception.getMessage());
    }

    @Test
    void getExceptionWhenSaveUserAndLoginWithBlank() {
        user.setLogin("my new login");
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.saveUser(user));
        assertEquals("Логин пользователя не может содержать пробелы", exception.getMessage());
    }

    @Test
    void nameEqualsLoginWhenNameIsNull() {
        user.setName(null);
        controller.saveUser(user);
        assertEquals("testLogin", user.getName());
    }

    @Test
    void nameEqualsLoginWhenNameIsBlank() {
        user.setName("         ");
        controller.saveUser(user);
        assertEquals("testLogin", user.getName());
    }

    @Test
    void getExceptionWhenSaveUserAndBirthdayInFuture() {
        user.setBirthday(LocalDate.of(2120, 6, 13));
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.saveUser(user));
        assertEquals("Введена дата рождения в будущем", exception.getMessage());
    }

    @Test
    void correctUpdateUser() {
        controller.saveUser(user);
        User update = new User();
        update.setId(user.getId());
        update.setEmail("update@update.com");
        update.setLogin("UPDATED");
        update.setName("updatedName");
        update.setBirthday(LocalDate.of(2001, 1, 24));
        controller.updateUser(update);
        assertEquals(update.getLogin(), controller.getAllUsers().get(controller.getAllUsers().size() - 1).getLogin());
    }

    @Test
    void correctUpdateUserWhenNameIsNull() {
        controller.saveUser(user);
        User update = new User();
        update.setId(user.getId());
        update.setEmail("update@update.com");
        update.setLogin("UPDATED");
        update.setName(null);
        update.setBirthday(LocalDate.of(2001, 1, 24));
        controller.updateUser(update);
        assertEquals(update.getLogin(), controller.getAllUsers().get(controller.getAllUsers().size() - 1).getName());
    }

    @Test
    void getExceptionWhenUpdateUserAndEmailIncorrect() {
        controller.saveUser(user);
        User update = new User();
        update.setId(user.getId());
        update.setEmail("update.com");
        update.setLogin("UPDATED");
        update.setName("updatedName");
        update.setBirthday(LocalDate.of(2001, 1, 24));
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateUser(update));
        assertEquals("Передана некорректная почта", exception.getMessage());
    }

    @Test
    void getExceptionWhenUpdateUserAndLoginIsNull() {
        controller.saveUser(user);
        User update = new User();
        update.setId(user.getId());
        update.setEmail("updated@update.com");
        update.setLogin(null);
        update.setName("updatedName");
        update.setBirthday(LocalDate.of(2001, 1, 24));
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateUser(update));
        assertEquals("Логин пользователя не может быть пустым", exception.getMessage());
    }

    @Test
    void getExceptionWhenUpdateUserAndLoginWithBlank() {
        controller.saveUser(user);
        User update = new User();
        update.setId(user.getId());
        update.setEmail("updated@update.com");
        update.setLogin("updated new login");
        update.setName("updatedName");
        update.setBirthday(LocalDate.of(2001, 1, 24));
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateUser(update));
        assertEquals("Логин пользователя не может содержать пробелы", exception.getMessage());
    }

    @Test
    void getExceptionWhenUpdateUserAndBirthdayInFuture() {
        controller.saveUser(user);
        User update = new User();
        update.setId(user.getId());
        update.setEmail("updated@update.com");
        update.setLogin("UPDATED");
        update.setName("updatedName");
        update.setBirthday(LocalDate.of(2111, 11, 21));
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.updateUser(update));
        assertEquals("Введена дата рождения в будущем", exception.getMessage());
    }
}
