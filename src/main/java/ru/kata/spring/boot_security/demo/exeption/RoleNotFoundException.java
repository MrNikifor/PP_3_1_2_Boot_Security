package ru.kata.spring.boot_security.demo.exeption;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
