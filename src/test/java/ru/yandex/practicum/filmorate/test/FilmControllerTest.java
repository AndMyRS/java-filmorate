package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    private Validator validator;

    @BeforeEach
    public void setup() {
        filmController = new FilmController();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void getAllFilms() {
        Film film1 = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2020, 01, 01))
                .duration(100)
                .build();

        Film film2 = Film.builder()
                .name("name2")
                .description("description2")
                .releaseDate(LocalDate.of(2021, 01, 01))
                .duration(100)
                .build();

        filmController.addFilm(film1);
        filmController.addFilm(film2);

        List<Film> films = filmController.getAllFilms();
        assertEquals(2, films.size());

    }

    @Test
    void addFilm() {
        Film film1 = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2020, 01, 01))
                .duration(100)
                .build();

        filmController.addFilm(film1);
        List<Film> films = filmController.getAllFilms();
        assertEquals(film1, films.get(0));
        assertEquals(1, films.get(0).getId());

    }

    @Test
    void updateFilm() {
        Film film1 = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2020, 01, 01))
                .duration(100)
                .build();

        Film film2 = Film.builder()
                .name("name2")
                .description("description2")
                .releaseDate(LocalDate.of(2021, 01, 01))
                .duration(100)
                .build();

        filmController.addFilm(film1);

        assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film2);
        });

    }

    @Test
    void testValidatingFilm() {
        Film film1 = Film.builder()
                .name("")
                .description("description1")
                .releaseDate(LocalDate.of(1700, 01, 01))
                .duration(-90)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());

        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film1);
        });
    }
}