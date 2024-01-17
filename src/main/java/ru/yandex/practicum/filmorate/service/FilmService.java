package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) { // Добавление в фильм лайка по id пользователя
        filmStorage.addLikeToFilm(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) { // Удаление из фильма лайка по id пользователя
        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> getMostPopularFilms(int filmsToShow) {
        return filmStorage.getMostPopularFilms(filmsToShow);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }

    public MPA getMpaById(int id) {
        return filmStorage.getMpaById(id);
    }

    public List<MPA> getAllMpa() {
        return filmStorage.getAllMpa();
    }
}
