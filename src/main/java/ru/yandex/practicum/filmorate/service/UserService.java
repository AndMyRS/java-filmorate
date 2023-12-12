package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(int id, int friendId) {
        User firstUser = userStorage.getUserById(id);
        User secondUser = userStorage.getUserById(friendId);

        Set<Integer> firstUserFriends = firstUser.getFriends();
        Set<Integer> secondUserFriends = secondUser.getFriends();

        firstUserFriends.add(friendId);
        secondUserFriends.add(id);

        userStorage.getUserById(id).setFriends(firstUserFriends);
        userStorage.getUserById(friendId).setFriends(secondUserFriends);

        updateUser(firstUser);
        updateUser(secondUser);

    }

    public void deleteFromFriends(int id, int friendId) {
        User user = userStorage.getUserById(id);
        Set<Integer> friends = user.getFriends();
        friends.remove(friendId);
        user.setFriends(friends);
        updateUser(user);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        Set<Integer> user1Friends = userStorage.getUserById(id).getFriends();
        Set<Integer> user2Friends = userStorage.getUserById(otherId).getFriends();
        Set<Integer> commonFriends = new HashSet<>();
        commonFriends.addAll(user1Friends);
        commonFriends.retainAll(user2Friends);
        List<User> friends = new ArrayList<>();
        for (Integer friendId : commonFriends) {
            User friend = userStorage.getUserById(friendId);
            friends.add(friend);
        }
        return friends;
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
        Set<Integer> friendIds = userStorage.getUserById(id).getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer friendId : friendIds) {
            User friend = userStorage.getUserById(friendId);
            friends.add(friend);
        }
        return friends;
    }
}
