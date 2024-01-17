package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) { // Использование @Primary ?
        this.userStorage = userStorage;
    }

    public void addToFriends(int id, int friendId) {
        userStorage.addToFriends(id, friendId);
    }

    public void deleteFromFriends(int id, int friendId) {
        userStorage.deleteFromFriends(id, friendId);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> friendsUser1 = userStorage.getFriendsOfUser(id);
        List<User> friendsUser2 = userStorage.getFriendsOfUser(otherId);

        friendsUser1.retainAll(friendsUser2);

        return friendsUser1;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllFriends(int id) {
        return userStorage.getFriendsOfUser(id);
    }
}
