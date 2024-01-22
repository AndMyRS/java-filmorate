package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        log.info("Add like to film with id {}", filmId);
        filmStorage.addLikeToFilm(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        log.info("Delete like from film with id {}", filmId);
        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> getMostPopularFilms(int filmsToShow) {
        log.info("Get most popular films");
        return filmStorage.getMostPopularFilms(filmsToShow);
    }

    public List<Film> getAllFilms() {
        log.info("Get all films");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        log.info("Get film with id {}", id);
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        log.info("Add film", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Update film {}", film);
        return filmStorage.updateFilm(film);
    }

    public List<Genre> getAllGenres() {
        log.info("Get all genres}");
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        log.info("Get genre with id {}", id);
        return filmStorage.getGenreById(id);
    }

    public MPA getMpaById(int id) {
        log.info("Get Mpa with id {}", id);
        return filmStorage.getMpaById(id);
    }

    public List<MPA> getAllMpa() {
        log.info("Get all Mpa");
        return filmStorage.getAllMpa();
    }
}
