package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {

    private int id;

    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Login cannot be empty")
    private String login;

    private String name;

    @Past(message = "Date of birth cannot be in the future")
    private LocalDate birthday;

    private Set<Integer> friends; // Коллекция id пользователей, добавленных в друзья

    private boolean isFriendshipConfirmed;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);

        return values;
    }
}
