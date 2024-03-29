package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class MPA {
    private int id;

    @NotBlank(message = "MPA name cannot be empty")
    private String name;
}
