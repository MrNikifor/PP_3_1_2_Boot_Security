package ru.kata.spring.boot_security.demo.exeption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String massage) {
        super(massage);
    }
}
