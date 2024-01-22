package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Genre {
    private int id;

    @NotBlank(message = "Genre name cannot be empty")
    private String name;
}
