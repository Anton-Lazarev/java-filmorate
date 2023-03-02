package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;

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
        List<User> friends = new ArrayList<>();
        Set<Integer> friendIDs = userStorage.findUserByID(userID).getFriends();
        if (friendIDs == null || friendIDs.isEmpty()) {
            log.debug("Запрошен список друзей для пользователя с ID {} ; запрошенный список пуст", userID);
            return friends;
        }
        for (int id : friendIDs) {
            friends.add(userStorage.findUserByID(id));
        }
        log.debug("Запрошен список друзей для пользователя с ID {} ; итоговый список размером {}", userID, friends.size());
        return friends;
    }

    public List<User> getFriendsCrossing(int userID, int anotherUserID) {
        List<User> collectedFriends = new ArrayList<>();
        Set<Integer> userFriends = userStorage.findUserByID(userID).getFriends();
        Set<Integer> anotherUserFriends = userStorage.findUserByID(anotherUserID).getFriends();
        if (userFriends == null || anotherUserFriends == null) {
            log.debug("Запрошен список общих друзей у пользователей ID {} и {} ; итоговый список пуст", userID, anotherUserID);
            return collectedFriends;
        }
        //Создаём новый сет, чтоб сеты внутри пользователей не перетирались
        Set<Integer> crossedFriends = new HashSet<>(userFriends);
        crossedFriends.retainAll(anotherUserFriends);
        for (int id : crossedFriends) {
            collectedFriends.add(userStorage.findUserByID(id));
        }
        log.debug("Запрошен список общих друзей у пользователей ID {} и {} ; итоговый список размером {}", userID, anotherUserID, collectedFriends.size());
        return collectedFriends;
    }

    public boolean checkUserIdInStorage(Integer id) {
        log.debug("Запрошена проверка ID {} в базе пользователей", id);
        if (!userStorage.idIsPresent(id)) {
            throw new UserNotFoundException("Пользователь с ID - " + id + " не найден в базе");
        }
        return true;
    }
}
