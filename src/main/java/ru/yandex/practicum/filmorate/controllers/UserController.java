package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    UserStorage userStorage = new InMemoryUserStorage();

    @GetMapping
    public List<User> getUsers() {
        return userStorage.getAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        return userStorage.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        return userStorage.updateUser(user);
    }
}
