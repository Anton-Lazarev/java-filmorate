package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage storage) {
        this.userStorage = storage;
    }

    public User addUser(User user) {
        UserValidator.validate(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        UserValidator.validate(user);
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserByID(int id) {
        return userStorage.findUserByID(id);
    }

    public String addFriendship(int userID, int friendID) {
        boolean success = userStorage.addFriendship(userID, friendID);
        if (success) {
            return String.format("Пользователь с ID %d успешно добавлен в друзья к пользователю с ID %d", friendID, userID);
        } else {
            return String.format("Пользователь с ID %d уже дружит с пользователем, ID %d", friendID, userID);
        }
    }

    public String removeFriendship(int userID, int friendID) {
        boolean success = userStorage.removeFriend(userID, friendID);
        if (success) {
            return String.format("Пользователь с ID %d успешно удален из друзей у пользователя с ID %d", friendID, userID);
        } else {
            return String.format("Пользователь с ID %d уже отсутствует в друзьях у пользователя, ID %d", friendID, userID);
        }
    }

    public List<User> getFriendsOfUser(int userID) {
        log.debug("Запрошен список общих друзей у пользователей ID {}", userID);
        Set<Integer> friends = userStorage.findUserByID(userID).getFriends();
        if (friends.isEmpty()) {
            return new ArrayList<>();
        }
        return friends.stream()
                .map(userStorage::findUserByID)
                .collect(Collectors.toList());
    }

    public List<User> getFriendsCrossing(int userID, int anotherUserID) {
        final Set<Integer> userFriends = userStorage.findUserByID(userID).getFriends();
        final Set<Integer> anotherUserFriends = userStorage.findUserByID(anotherUserID).getFriends();

        log.debug("Запрошен список общих друзей у пользователей ID {} и {}", userID, anotherUserID);
        return userFriends.stream()
                .filter(anotherUserFriends::contains)
                .map(userStorage::findUserByID)
                .collect(Collectors.toList());
    }

    public boolean checkUserIdInStorage(Integer id) {
        log.debug("Запрошена проверка ID {} в базе пользователей", id);
        if (!userStorage.idIsPresent(id)) {
            throw new UserNotFoundException("Пользователь с ID - " + id + " не найден в базе");
        }
        return true;
    }
}
