package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    protected int nextID = 1;
    protected Map<Integer, User> users = new HashMap<>();

    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей в базе: {}", users.size());
        return new ArrayList<>(users.values());
    }

    public User addUser(User user) {
        UserValidator.validate(user);
        user.setId(nextID);
        users.put(user.getId(), user);
        nextID++;
        log.debug("Добавлен пользователь: {}; его ID: {}; всего пользователей в базе: {}", user.getLogin(), user.getId(), users.size());
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с ID - " + user.getId() + " не найден в базе");
        }
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}; его ID: {}", user.getLogin(), user.getId());
        return user;
    }

    public void deleteUserByID(Integer id) {
        if (!users.containsKey(id)) {
            throw new ValidationException("Пользователь с ID - " + id + " не найден в базе");
        }
        users.remove(id);
        log.debug("Пользователь с ID {} удалён из базы, в базе осталось {} пользователей", id, users.size());
    }
}
