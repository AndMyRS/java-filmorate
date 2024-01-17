package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;

    private HashMap<Integer, User> users = new HashMap<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else throw new IllegalArgumentException(String.format("User with id %d not found", id));
    }

    public User addUser(User user) {
        validateUser(user);
        if (user.getId() == 0) {
            user.setId(++id);
        }
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        validateUser(user);
        if (users.containsKey(user.getId())) {
            Set<Integer> friends = users.get(user.getId()).getFriends();
            users.remove(user.getId());
            user.setFriends(friends);
            users.put(user.getId(), user);
            return user;
        } else throw new RuntimeException("No such user in filmorate");
    }

    @Override
    public List<User> getFriendsOfUser(int id) {
        return null;
    }

    @Override
    public void addToFriends(int id, int friendId) {

    }

    @Override
    public void deleteFromFriends(int id, int friendId) {

    }

    public static void validateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
