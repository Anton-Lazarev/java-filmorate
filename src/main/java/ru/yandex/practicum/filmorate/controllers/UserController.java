package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserByID(@PathVariable int id) {
        return userService.getUserByID(id);
    }

    @PutMapping("/{userID}/friends/{friendID}")
    public String addFriendship(@PathVariable int userID, @PathVariable int friendID) {
        return userService.addFriendship(userID, friendID);
    }

    @DeleteMapping("/{userID}/friends/{friendID}")
    public String removeFriendship(@PathVariable int userID, @PathVariable int friendID) {
        return userService.removeFriendship(userID, friendID);
    }

    @GetMapping("/{userID}/friends")
    public List<User> getFriendsOfUser(@PathVariable int userID) {
        return userService.getFriendsOfUser(userID);
    }

    @GetMapping("/{userID}/friends/common/{anotherID}")
    public List<User> getFriendsCrossing(@PathVariable int userID, @PathVariable int anotherID) {
        return userService.getFriendsCrossing(userID, anotherID);
    }
}
