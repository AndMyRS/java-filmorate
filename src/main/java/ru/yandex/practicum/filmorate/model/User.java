package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

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

}
