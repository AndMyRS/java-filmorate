package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.LinkedHashSet;
import java.util.HashSet;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() {

        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        Film newFilm = createFirstFilm();

        filmDbStorage.addFilm(newFilm);

        Film savedFilm = filmDbStorage.getFilmById(newFilm.getId());

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testGetAllFilms() {

        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        Film newFilm = createFirstFilm();

        Film newFilm2 = createSecondFilm();

        filmDbStorage.addFilm(newFilm);
        filmDbStorage.addFilm(newFilm2);

        List<Film> films = filmDbStorage.getAllFilms();
        assertThat(films)
                .isNotNull();

        assertThat(films.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);

        assertThat(films.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm2);
    }

    @Test
    public void testAddLikeToFilm() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);

        Film newFilm = createFirstFilm();
        User newUser = createUser();

        filmDbStorage.addFilm(newFilm);
        userDbStorage.addUser(newUser);
        filmDbStorage.addLikeToFilm(newFilm.getId(), newUser.getId());

        Film savedFilm = filmDbStorage.getAllFilms().get(0);
        Set<Integer> likes = savedFilm.getLikes();

        assertThat(likes)
                .isNotEmpty()
                .containsExactly(1);
    }

    @Test
    public void testUpdateFilm() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        Film newFilm = createFirstFilm();

        filmDbStorage.addFilm(newFilm);

        newFilm.setName("Film1 Updated");
        filmDbStorage.updateFilm(newFilm);
        Film savedFilm = filmDbStorage.getAllFilms().get(0);

        assertThat(savedFilm)
                .hasFieldOrPropertyWithValue("name", "Film1 Updated");
    }

    @Test
    public void testGetGenreById() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        Genre genre = filmDbStorage.getGenreById(1);

        assertThat(genre)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void testGetMpaById() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        MPA mpa = filmDbStorage.getMpaById(1);

        assertThat(mpa)
                .hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void testGetMostPopularFilms() {

        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);

        Film film1 = createFirstFilm();
        Film film2 = createSecondFilm();
        film1.setLikes(Set.of(1));

        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);

        User user1 = createUser();
        userDbStorage.addUser(user1);

        filmDbStorage.addLikeToFilm(film1.getId(), user1.getId());

        List<Film> popularFilms = filmDbStorage.getMostPopularFilms(2);

        assertThat(popularFilms)
                .isNotNull()
                .hasSize(2);

        assertThat(popularFilms.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);

        assertThat(popularFilms.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film2);
    }

    private Film createFirstFilm() {
        return Film.builder()
                .id(1)
                .name("Film1")
                .description("Film1 description")
                .releaseDate(LocalDate.of(2020, 01, 01))
                .duration(90)
                .likes(new HashSet<>())
                .genres(new LinkedHashSet<>())
                .mpa(MPA.builder()
                        .id(1)
                        .name("G")
                        .build())
                .rate(0)
                .build();
    }

    private Film createSecondFilm() {
        return Film.builder()
                .id(2)
                .name("Film2")
                .description("Film2 description")
                .releaseDate(LocalDate.of(2020, 01, 01))
                .duration(90)
                .likes(new HashSet<>())
                .genres(new LinkedHashSet<>())
                .mpa(MPA.builder()
                        .id(1)
                        .name("G")
                        .build())
                .rate(0)
                .build();
    }

    private User createUser() {
        return User.builder()
                .id(1)
                .email("email@email.com")
                .login("userlogin")
                .name("User Name")
                .birthday(LocalDate.of(2000, 10, 10))
                .friends(new HashSet<>())
                .isFriendshipConfirmed(false)
                .build();
    }
}
