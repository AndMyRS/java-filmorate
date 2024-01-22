package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) { // Использование @Primary ?
        this.userStorage = userStorage;
    }

    public void addToFriends(int id, int friendId) {
        log.info("Add friend to user with id {}", id);
        userStorage.addToFriends(id, friendId);
    }

    public void deleteFromFriends(int id, int friendId) {
        log.info("Delete friend by user with id {}", id);
        userStorage.deleteFromFriends(id, friendId);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        log.info("Get common friends for users with id {} and {}", id, otherId);
        List<User> friendsUser1 = userStorage.getFriendsOfUser(id);
        List<User> friendsUser2 = userStorage.getFriendsOfUser(otherId);

        friendsUser1.retainAll(friendsUser2);

        return friendsUser1;
    }

    public List<User> getAllUsers() {
        log.info("Get all users");
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        log.info("Get user with id {}", id);
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        log.info("Add user {}", user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.info("Update user {}", user);
        return userStorage.updateUser(user);
    }

    public List<User> getAllFriends(int id) {
        log.info("Get all friends of user with id {}", id);
        return userStorage.getFriendsOfUser(id);
    }
}
