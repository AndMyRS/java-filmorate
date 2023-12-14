package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public void addLike(int film, int id) { // Добавление в фильм лайка по id пользователя
        filmStorage.getFilmById(film).getLikes().add(id);
        updateFilm(getFilmById(id));
    }

    public void deleteLike(int film, int id) { // Удаление из фильма лайка по id пользователя
        filmStorage.getFilmById(film).getLikes().remove(id);
        updateFilm(getFilmById(id));
    }

    public List<Film> getMostPopularFilms(int filmsToShow) { // Список самых популярных фильмов. Кол-во в filmsToShow
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(filmsToShow)
                .collect(Collectors.toList());
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
}
