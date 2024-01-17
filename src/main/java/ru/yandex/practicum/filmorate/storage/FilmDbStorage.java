package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.*, r.name as rating_name, g.name as genre_name, g.id as genre_id, fl.user_id as like_user_id FROM films f" +
                " LEFT JOIN ratings r ON f.mpa = r.id" +
                " LEFT JOIN film_genres fg ON f.id = fg.film_id" +
                " LEFT JOIN genres g ON fg.genre_id = g.id" +
                " LEFT JOIN film_likes fl ON f.id = fl.film_id";

        return jdbcTemplate.query(sql, rs -> {
            return getFilms(rs);
        });
    }

    private List<Film> getFilms(ResultSet rs) throws SQLException {
        List<Film> list = new ArrayList<Film>();
        Map<Integer, Film> filmIdFilmMap = new HashMap<>();
        Map<Integer, Genre> genreIdGenreMap = new HashMap<>();
        Map<Integer, Integer> likeIdLikeMap = new HashMap<>();
        while (rs.next()) {
            int filmId = rs.getInt("id");
            Film film = filmIdFilmMap.get(filmId);
            if (film == null) {
                film = Film.builder()
                        .id(filmId)
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .duration(rs.getInt("duration"))
                        .rate(rs.getInt("rate"))
                        .build();

                MPA mpa = MPA.builder()
                        .id(rs.getInt("mpa")).build();
                mpa.setName(rs.getString("rating_name"));
                film.setMpa(mpa);

            }
            int genreId = rs.getInt("genre_id");
            if (genreId != 0) {
            Genre genre = genreIdGenreMap.get(genreId);
            if (genre == null) {
                genre = Genre.builder()
                        .id(genreId)
                        .name(rs.getString("genre_name")).build();
                if (film.getGenres() == null) {
                    film.setGenres(new LinkedHashSet<>());
                }
                film.getGenres().add(genre);
                genreIdGenreMap.put(genreId, genre);
                }
            } else film.setGenres(new LinkedHashSet<>());

            int userId = rs.getInt("like_user_id");
            if (userId != 0) {
                if (film.getLikes() == null) {
                    film.setLikes(new HashSet<>());
                }
                film.getLikes().add(userId);
            } else film.setLikes(new HashSet<>());

            filmIdFilmMap.put(filmId, film);

        }
        list.addAll(filmIdFilmMap.values());
        return list;
    }


    @Override
    public Film getFilmById(int id) {
        String sql1 = "SELECT EXISTS (SELECT 1 FROM films WHERE id=?)";
        String sql2 =
        "SELECT f.*, r.name as rating_name, g.name as genre_name, g.id as genre_id, fl.user_id as like_user_id FROM films f" +
                " LEFT JOIN ratings r ON f.mpa = r.id" +
                " LEFT JOIN film_genres fg ON f.id = fg.film_id" +
                " LEFT JOIN genres g ON fg.genre_id = g.id" +
                " LEFT JOIN film_likes fl ON f.id = fl.film_id WHERE f.id=?";

        if (jdbcTemplate.queryForObject(sql1, Boolean.class, id)) {
            List<Film> films = jdbcTemplate.query(sql2, this::mapRowToFilm, id); // this::mapRowToFilm
            return films.get(0);
        } else throw new IllegalArgumentException("No such film in filmorate");
    }


    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(id);

        if (film.getGenres() != null) {
            List<Integer> genreIds = new ArrayList<>();
            for (Genre genre : film.getGenres()) {
                genreIds.add(genre.getId());
            }

            for (Integer genreId : genreIds) {
                jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", id, genreId);
            }
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa=?, rate=? WHERE ID=?";
        int rows = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        if (rows > 0) {
            List<Integer> genreIds = new ArrayList<>();
            int id = film.getId();
            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres()) {
                    genreIds.add(genre.getId());
                }

                jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", id);

                for (Integer genreId : genreIds) {
                    jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", id, genreId);
                }
            }
            return film;
        } else throw new IllegalArgumentException("No such film in filmorate");
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build());
    }

    @Override
    public Genre getGenreById(int id) {
        String sql1 = "SELECT EXISTS (SELECT 1 FROM genres WHERE id=?)";
        String sql2 = "SELECT * FROM genres WHERE id=?";
        if (jdbcTemplate.queryForObject(sql1, Boolean.class, id)) {
            return jdbcTemplate.queryForObject(sql2, (rs, rowNum) -> Genre.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build(), id);
        } else throw new IllegalArgumentException("No such genre in filmorate");
    }

    @Override
    public void addLikeToFilm(int filmId, int userId) {
        String sql = "INSERT INTO film_likes(film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        String sql1 = "SELECT EXISTS (SELECT 1 FROM users WHERE id=?)";
        if (jdbcTemplate.queryForObject(sql1, Boolean.class, userId)) {
            String sql2 = "DELETE FROM film_likes WHERE film_id=? AND user_id=?";
            jdbcTemplate.update(sql2, filmId, userId);
        } else throw new IllegalArgumentException("No such user with friend_id in filmorate");
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sql = "SELECT f.*, r.name as rating_name, g.name as genre_name, g.id as genre_id, fl.user_id as like_user_id FROM films f" +
                " LEFT JOIN ratings r ON f.mpa = r.id" +
                " LEFT JOIN film_genres fg ON f.id = fg.film_id" +
                " LEFT JOIN genres g ON fg.genre_id = g.id" +
                " LEFT JOIN film_likes fl ON f.id = fl.film_id" +
                " GROUP BY f.id, f.name, f.description, release_date, duration, mpa, rate, r.name, g.name, g.id, fl.user_id" +
                " ORDER BY (SELECT COUNT(user_id) FROM film_likes WHERE film_id = f.id) DESC  LIMIT(?)";

        return jdbcTemplate.query(sql, new ResultSetExtractor<List<Film>>() {
            @Override
            public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
                return getFilms(rs);
            }
        }, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .build();

        MPA mpa = MPA.builder()
                .id(rs.getInt("mpa")).build();

        mpa.setName(rs.getString("rating_name"));
        film.setMpa(mpa);

        HashSet<Genre> genres = new LinkedHashSet<>();
        do {
            String genreName = rs.getString("genre_name");
            if (genreName != null) {
                Genre genre = Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(genreName)
                        .build();
                genres.add(genre);
            }
        } while (rs.next() && rs.getInt("id") == film.getId());
        film.setGenres((LinkedHashSet<Genre>) genres);

        Set<Integer> likes = new HashSet<>();

        while (rs.next() && rs.getInt("id") == film.getId()) {

            likes.add(rs.getInt("like_user_id"));
        }

        film.setLikes(likes);

        return film;
    }

    @Override
    public MPA getMpaById(int id) {
        String sql1 = "SELECT EXISTS (SELECT 1 FROM ratings WHERE id=?)";
        if (jdbcTemplate.queryForObject(sql1, Boolean.class, id)) {
            String sql2 = "SELECT * FROM ratings WHERE id=?";
            return jdbcTemplate.queryForObject(sql2, ((rs, rowNum) -> MPA.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build()), id);
        } else throw new IllegalArgumentException("No such MPA in filmorate");
    }

    @Override
    public List<MPA> getAllMpa() {
        String sql = "SELECT * FROM ratings";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> MPA.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build()));
    }

    public static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Validation failed");
            throw new ValidationException("Invalid film data: please check release date");
        }
    }

    private Film mapFilmRow(ResultSet rs, Map<Integer, Film> filmIdFilmMap, Map<Integer, Genre> genreIdGenreMap) throws SQLException {
        int filmId = rs.getInt("id");
        Film film = filmIdFilmMap.get(filmId);
        if (film == null) {
            film = Film.builder()
                    .id(filmId)
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .rate(rs.getInt("rate"))
                    .build();
            MPA mpa = MPA.builder().id(rs.getInt("mpa")).build();
            mpa.setName(rs.getString("rating_name"));
            film.setMpa(mpa);

            filmIdFilmMap.put(filmId, film);
        }

        int genreId = rs.getInt("genre_id");
        if (genreId != 0) {
            Genre genre = genreIdGenreMap.get(genreId);
            if (genre == null) {
                genre = Genre.builder()
                        .id(genreId)
                        .name(rs.getString("genre_name")).build();
                if (film.getGenres() == null) {
                    film.setGenres(new LinkedHashSet<>());
                }
                film.getGenres().add(genre);
                genreIdGenreMap.put(genreId, genre);
            }
        } else {
            film.setGenres(new LinkedHashSet<>());
        }

        int userId = rs.getInt("like_user_id");
        if (userId != 0) {
            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
            film.getLikes().add(userId);
        } else {
            film.setLikes(new HashSet<>());
        }

        return film;
    }
}
