package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {

    private int id;

    @NotBlank(message = "Film name cannot be empty")
    private String name;

    @Size(max = 200, message = "Description cannot be longer than {max} characters")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Film duration cannot be negative")
    private int duration;

    private Set<Integer> likes; // Коллекция id пользователей, поставивших лайк

}
