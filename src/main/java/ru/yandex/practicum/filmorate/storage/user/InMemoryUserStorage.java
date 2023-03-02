package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.*;

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
        user.setId(nextID);
        users.put(user.getId(), user);
        nextID++;
        log.debug("Добавлен пользователь: {}; его ID: {}; всего пользователей в базе: {}", user.getLogin(), user.getId(), users.size());
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь с ID - " + user.getId() + " не найден в базе");
        }
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}; его ID: {}", user.getLogin(), user.getId());
        return user;
    }

    public void deleteUserByID(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с ID - " + id + " не найден в базе");
        }
        users.remove(id);
        log.debug("Пользователь с ID {} удалён из базы, в базе осталось {} пользователей", id, users.size());
    }

    public User findUserByID(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с ID - " + id + " не найден в базе");
        }
        log.debug("Пользователь с ID {} запрошен из базы", id);
        return users.get(id);
    }

    public boolean addFriendship(Integer userID, Integer friendID) {
        if (!users.containsKey(userID) || !users.containsKey(friendID)) {
            throw new UserNotFoundException("Один из пользователей отсутствует в базе, добавление друга невозможно");
        }
        boolean successAdded = addFriend(users.get(userID), users.get(friendID));
        //Дружба - процесс обоюдный, поэтому сразу добавляем исходного пользователя к другу
        addFriend(users.get(friendID), users.get(userID));

        if (successAdded) {
            log.debug("Пользователь с ID {} успешно добавлен в друзья к пользователю с ID {}", friendID, userID);
        } else {
            log.debug("Пользователь с ID {} уже находится в друзьях у пользователя с ID {}", friendID, userID);
        }
        return successAdded;
    }

    public boolean removeFriend(Integer userID, Integer friendID) {
        if (!users.containsKey(userID)) {
            throw new UserNotFoundException("Пользователь с ID - " + userID + " не найден в базе");
        }
        boolean successRemoved = removeFriend(users.get(userID), users.get(friendID));
        //Удаление из друзей подобно дружбе - процесс обоюдный, поэтому сразу убираем исходного пользователя у друга
        removeFriend(users.get(friendID), users.get(userID));

        if (successRemoved) {
            log.debug("Пользователь с ID {} успешно удален из друзей у пользователя с ID {}", friendID, userID);
        } else {
            log.debug("Пользователь с ID {} отсутствует в друзьях у пользователя с ID {}", friendID, userID);
        }
        return successRemoved;
    }

    public boolean idIsPresent(Integer id) {
        return users.containsKey(id);
    }

    private boolean addFriend(User user, User friend) {
        Set<Integer> allFriends = user.getFriends();
        if (allFriends == null) {
            allFriends = new HashSet<>();
        }
        boolean isAdded = allFriends.add(friend.getId());
        user.setFriends(allFriends);
        return isAdded;
    }

    private boolean removeFriend(User user, User friend) {
        Set<Integer> allFriends = user.getFriends();
        if (allFriends == null) {
            return false;
        }
        boolean isRemoved = allFriends.remove(friend.getId());
        user.setFriends(allFriends);
        return isRemoved;
    }
}
