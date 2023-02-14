package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    protected int nextID = 1;
    protected Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        log.debug("Текущее количество пользователей в базе: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        UserValidator.validate(user);
        user.setId(nextID);
        users.put(user.getId(), user);
        nextID++;
        log.debug("Добавлен пользователь: {}; его ID: {}; всего пользователей в базе: {}", user.getLogin(), user.getId(), users.size());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с ID - " + user.getId() + " не найден в базе");
        }
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}; его ID: {}", user.getLogin(), user.getId());
        return user;
    }
}
