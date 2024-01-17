package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.*;

@Component("inMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;

    private HashMap<Integer, Film> films = new HashMap<>();

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else throw new IllegalArgumentException(String.format("No film found with id %d", id));
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        if (film.getId() == 0) {
            film.setId(++id);
        }
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        if (films.containsKey(film.getId())) {
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            return film;
        } else throw new RuntimeException("No such film in filmorate");
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Genre getGenreById(int id) {
        return null;
    }

    @Override
    public void addLikeToFilm(int filmId, int userId) {

    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {

    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return null;
    }

    @Override
    public MPA getMpaById(int id) {
        return null;
    }

    @Override
    public List<MPA> getAllMpa() {
        return null;
    }

    public static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Validation failed");
            throw new ValidationException("Invalid film data: please check release date");
        }
    }
}
