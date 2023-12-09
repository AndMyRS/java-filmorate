package ru.yandex.practicum.filmorate.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        userController = new UserController();

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // Добавить валидацию при пустом методе. Отдельный метод?

    @Test
    public void testUserValidation() {
        User user = User.builder()
                .email("dsljdfl.com")
                .login("")
                .birthday(LocalDate.of(2050, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(3, violations.size());

    }
}
