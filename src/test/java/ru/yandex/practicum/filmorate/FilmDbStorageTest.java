package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() {
        Film newFilm = Film.builder()
                .id(1)
                .name("Film1")
                .description("Film1 description")
                .releaseDate(LocalDate.of(2020, 01, 01))
                .duration(90)
                .mpa(MPA.builder()
                        .id(1)
                        .name("MPA1")
                        .build())
                .rate(5)
                .build();

        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        filmDbStorage.addFilm(newFilm);

        Film savedFilm = filmDbStorage.getFilmById(1);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }
}
