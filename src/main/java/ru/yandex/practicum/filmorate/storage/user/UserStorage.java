package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();
    User addUser(User user);
    User updateUser(User user);
    void deleteUserByID(Integer id);
    User findUserByID(Integer id);
    boolean addFriendship(Integer userID, Integer friendID);
    boolean removeFriend(Integer userID, Integer friendID);
    boolean idIsPresent(Integer id);
}
