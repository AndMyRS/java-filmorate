package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User getUserById(int id) {
        String sql1 = "SELECT EXISTS (SELECT 1 FROM users WHERE id=?)";
        String sql2 = "SELECT * FROM users WHERE id=?";
        if (jdbcTemplate.queryForObject(sql1, Boolean.class, id)) {
            return jdbcTemplate.queryForObject(sql2, this::mapRowToUser, id);
        } else throw new IllegalArgumentException("No such user in filmorate");
    }

    @Override
    public User addUser(User user) {
        validateUser(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Integer id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email=?, login=?, name=?, birthday=? WHERE id=?";
        int rows = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), user.getId());
        if (rows > 0) {
            return user;
        } else throw new IllegalArgumentException("No such user in filmorate");
    }

    @Override
    public List<User> getFriendsOfUser(int id) {
        String sql = "SELECT users.* FROM users JOIN friends ON users.id = friends.friend_id WHERE friends.user_id=?";
        return jdbcTemplate.query(sql, this::mapRowToUser, id);
    }

    @Override
    public void addToFriends(int id, int friendId) {
        String sql1 = "SELECT EXISTS (SELECT 1 FROM users WHERE id=?)";
        if (jdbcTemplate.queryForObject(sql1, Boolean.class, friendId)) {
            String sql2 = "INSERT INTO friends(user_id, friend_id) VALUES(?, ?)";
            jdbcTemplate.update(sql2, id, friendId);
        } else throw new IllegalArgumentException("No such user with friend_id in filmorate");
    }

    @Override
    public void deleteFromFriends(int id, int friendId) {
        String sql = "DELETE FROM friends WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(sql, id, friendId);
    }

    public User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getObject("birthday", LocalDate.class))
                .build();
    }

    public static void validateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
