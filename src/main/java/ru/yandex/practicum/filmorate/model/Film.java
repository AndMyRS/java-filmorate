package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder(access = AccessLevel.PUBLIC)
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

    private LinkedHashSet<Genre> genres;

    private MPA mpa;

    private int rate;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa", mpa.getId());

        return values;
    }
}
