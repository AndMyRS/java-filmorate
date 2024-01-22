package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        User newUser = createFirstUser();
        userStorage.addUser(newUser);

        User savedUser = userStorage.getUserById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testGetAllUsers() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        User user1 = createFirstUser();
        User user2 = createSecondUser();

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        List<User> users = userStorage.getAllUsers();

        assertThat(users)
                .hasSize(2);

        assertThat(users.get(0))
                .isEqualTo(user1);

        assertThat(users.get(1))
                .isEqualTo(user2);

    }

    @Test
    public void testUpdateUser() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        User user1 = createFirstUser();

        userStorage.addUser(user1);

        user1.setName("User1 updated");
        userStorage.updateUser(user1);

        User savedUser = userStorage.getUserById(user1.getId());

        assertThat(savedUser)
                .isEqualTo(user1);

        assertThat(savedUser)
                .hasFieldOrPropertyWithValue("name", "User1 updated");
    }

    private User createFirstUser() {
        return User.builder()
                .id(1)
                .email("email@email.com")
                .login("userlogin")
                .name("User Name")
                .birthday(LocalDate.of(2000, 10, 10))
                .isFriendshipConfirmed(false)
                .build();
    }

    private User createSecondUser() {
        return User.builder()
                .id(2)
                .email("email2@email.com")
                .login("userlogi2n")
                .name("User Name2")
                .birthday(LocalDate.of(2000, 10, 10))
                .isFriendshipConfirmed(false)
                .build();
    }
}
