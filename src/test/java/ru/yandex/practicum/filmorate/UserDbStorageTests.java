package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDbStorageTests {
    private static EmbeddedDatabase emdb;
    private static JdbcTemplate jdbcTemplate;
    private static UserDbStorage storage;

    @BeforeAll
    static void setUp() {
        emdb = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(emdb);
        storage = new UserDbStorage(jdbcTemplate);
    }

    @AfterAll
    static void tearDown() {
        emdb.shutdown();
    }

    @Order(1)
    @Test
    public void getEmptyUserListWhenUsersEmpty() {
        assertEquals(0, storage.getAllUsers().size());
    }

    @Order(2)
    @Test
    public void correctAddUser() {
        User user = User.builder()
                .email("111@111.ru")
                .login("testLogin")
                .name("TestingName")
                .birthday(LocalDate.of(2002, 2, 23))
                .build();
        User currUser = storage.addUser(user);
        assertEquals(1, currUser.getId());
        assertEquals("testLogin", currUser.getLogin());
    }

    @Order(3)
    @Test
    public void getExceptionWhenIncorrectIdInUpdating() {
        User user = User.builder()
                .email("222@222.ru")
                .login("testLogin")
                .name("TestingName")
                .birthday(LocalDate.of(2002, 2, 23))
                .build();
        storage.addUser(user);
        user.setLogin("newLogin");
        user.setId(987);
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> storage.updateUser(user));
        assertEquals("Пользователь с ID 987 не найден в базе", exception.getMessage());
    }

    @Order(4)
    @Test
    public void getCorrectAllUsersAfterSecondUser() {
        assertEquals(2, storage.getAllUsers().size());
    }

    @Order(5)
    @Test
    public void correctUpdatingUser() {
        User user = User.builder()
                .id(2)
                .email("333@333.ru")
                .login("UPDATED")
                .name("TestingName")
                .birthday(LocalDate.of(2002, 2, 23))
                .build();
        storage.updateUser(user);
        assertEquals("UPDATED", storage.findUserByID(2).getLogin());
    }

    @Order(6)
    @Test
    public void getExceptionWhenIncorrectIdInGetUserByID() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> storage.findUserByID(741));
        assertEquals("Пользователь с ID 741 не найден в базе", exception.getMessage());
    }

    @Order(7)
    @Test
    public void getCorrectUserByID() {
        assertEquals("111@111.ru", storage.findUserByID(1).getEmail());
    }

    @Order(8)
    @Test
    public void getExceptionWhenIncorrectUserIdInFriendship() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> storage.addFriendship(338, 1));
        assertEquals("Один из пользователей отсутствует в базе, регистрация дружбы невозможна", exception.getMessage());
    }

    @Order(9)
    @Test
    public void getExceptionWhenIncorrectFriendIdInFriendship() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> storage.addFriendship(2, 222));
        assertEquals("Один из пользователей отсутствует в базе, регистрация дружбы невозможна", exception.getMessage());
    }

    @Order(10)
    @Test
    public void correctAddFriendship() {
        User user = User.builder()
                .email("444@444.ru")
                .login("ThirdUser")
                .name("TestingName")
                .birthday(LocalDate.of(2002, 2, 23))
                .build();
        storage.addUser(user);
        User anotherUser = User.builder()
                .email("555@555.ru")
                .login("FourthUser")
                .name("TestingName")
                .birthday(LocalDate.of(2002, 2, 23))
                .build();
        storage.addUser(anotherUser);

        storage.addFriendship(1, 3);
        storage.addFriendship(1, 4);
        assertEquals(2, storage.getFriendsOfUser(1).size());
    }

    @Order(11)
    @Test
    public void getExceptionWhenIncorrectUserInRemovingFriendship() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> storage.removeFriendship(147, 1));
        assertEquals("Один из пользователей отсутствует в базе, удаление дружбы невозможно", exception.getMessage());
    }

    @Order(12)
    @Test
    public void getExceptionWhenIncorrectFriendInRemovingFriendship() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> storage.removeFriendship(1, 500));
        assertEquals("Один из пользователей отсутствует в базе, удаление дружбы невозможно", exception.getMessage());
    }

    @Order(13)
    @Test
    public void correctRemovingFriendship() {
        storage.removeFriendship(1, 4);
        assertEquals(1, storage.getFriendsOfUser(1).size());
    }

    @Order(14)
    @Test
    public void correctGetEmptyFriendsCrossing() {
        assertEquals(0, storage.getFriendsCrossing(1, 2).size());
    }

    @Order(15)
    @Test
    public void correctGetFriendsCrossing() {
        storage.addFriendship(2, 3);
        List<User> friends = storage.getFriendsCrossing(1, 2);
        assertEquals(3, friends.get(0).getId());
    }

    @Order(16)
    @Test
    public void getTrueWhenIdPresented() {
        assertTrue(storage.idIsPresent(4));
    }

    @Order(17)
    @Test
    public void getFalseWhenIdNotPresent() {
        assertFalse(storage.idIsPresent(85858));
    }
}
