# java-filmorate
Template repository for Filmorate project.

![Filmorate DB scheme.
Examples of queries
- getAllFilms
- "SELECT f.*, r.name as rating_name, g.name as genre_name, g.id as genre_id, fl.user_id as like_user_id FROM films f" +
                " LEFT JOIN ratings r ON f.mpa = r.id" +
                " LEFT JOIN film_genres fg ON f.id = fg.film_id" +
                " LEFT JOIN genres g ON fg.genre_id = g.id" +
                " LEFT JOIN film_likes fl ON f.id = fl.film_id";
  - getFilmById
  - "SELECT f.*, r.name as rating_name, g.name as genre_name, g.id as genre_id, fl.user_id as like_user_id FROM films f" +
                " LEFT JOIN ratings r ON f.mpa = r.id" +
                " LEFT JOIN film_genres fg ON f.id = fg.film_id" +
                " LEFT JOIN genres g ON fg.genre_id = g.id" +
                " LEFT JOIN film_likes fl ON f.id = fl.film_id WHERE f.id=?";
    - getMostPopularFilm
    - "SELECT f.*, r.name as rating_name, g.name as genre_name, g.id as genre_id, fl.user_id as like_user_id FROM films f" +
                " LEFT JOIN ratings r ON f.mpa = r.id" +
                " LEFT JOIN film_genres fg ON f.id = fg.film_id" +
                " LEFT JOIN genres g ON fg.genre_id = g.id" +
                " LEFT JOIN film_likes fl ON f.id = fl.film_id" +
                " GROUP BY f.id, f.name, f.description, release_date, duration, mpa, rate, r.name, g.name, g.id, fl.user_id" +
                " ORDER BY (SELECT COUNT(user_id) FROM film_likes WHERE film_id = f.id) DESC  LIMIT(?)";.](https://ibb.co/QnJPNKM)
