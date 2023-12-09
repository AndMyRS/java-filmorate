package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private int id = 0;

    private HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        log.info("POST request received");
        validateUser(user);
        if (user.getId() == 0) {
            user.setId(++id);
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT request received");
        validateUser(user);
        if (user.getId() == 0) {
            user.setId(++id);
        }
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), user);
            return user;
        } else throw new ValidationException("No such user in filmorate");
    }

    public static void validateUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
