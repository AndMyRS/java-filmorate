package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void getAllFilms() {
        FilmController filmController = new FilmController();
        Film film1 = new Film("name1", "description1", LocalDate.of(2020, 01, 01), 90);
        Film film2 = new Film("name2", "description2", LocalDate.of(2021, 01, 01), 100);
        filmController.addFilm(film1);
        filmController.addFilm(film2);

        List<Film> films = filmController.getAllFilms();
        assertEquals(2, films.size());

    }

    @Test
    void addFilm() {
        FilmController filmController = new FilmController();
        Film film1 = new Film("name1", "description1", LocalDate.of(2020, 01, 01), 90);
        filmController.addFilm(film1);
        List<Film> films = filmController.getAllFilms();
        assertEquals(film1, films.get(0));
        assertEquals(1, films.get(0).getId());

        Film film2 = new Film("", "description2", LocalDate.of(2021, 01, 01), 100);
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film2);
        });
    }

    @Test
    void updateFilm() {
        FilmController filmController = new FilmController();
        Film film1 = new Film("name1", "description1", LocalDate.of(2020, 01, 01), 90);
        Film film2 = new Film("name2", "description2", LocalDate.of(2021, 01, 01), 100);
        filmController.addFilm(film1);

        assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film2);
        });

    }

    @Test
    void validateFilm() {
        FilmController filmController = new FilmController();
        Film film1 = new Film("", "description1", LocalDate.of(2020, 01, 01), 90);
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film1);
        });
        Film film2 = new Film("film2", "description1", LocalDate.of(1700, 01, 01), 90);
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film2);
        });
        Film film3 = new Film("film3", "description1", LocalDate.of(2020, 01, 01), -90);
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film3);
        });
    }
}