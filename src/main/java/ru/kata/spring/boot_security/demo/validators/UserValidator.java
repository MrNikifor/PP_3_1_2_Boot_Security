package ru.kata.spring.boot_security.demo.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));

        optionalUser.ifPresent(existingUser -> {
            if (existingUser.getId() != user.getId()) {
                errors.rejectValue("username", "", "Пользователь с таким именем уже существует");
            }
        });
    }
}
