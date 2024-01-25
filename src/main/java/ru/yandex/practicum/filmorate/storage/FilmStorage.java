package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film getFilmById(int id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Genre> getAllGenres();

    Genre getGenreById(int id);

    void addLikeToFilm(int filmId, int userId);

    void deleteLikeFromFilm(int filmId, int userId);

    List<Film> getMostPopularFilms(int count);

    MPA getMpaById(int id);

    List<MPA> getAllMpa();
}
