package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDto {
    private int id;
    private String username;
    private String email;
    private int age;
    private List<String> roles; // Список имен ролей

    // Конструктор по умолчанию
    public UserDto() {
    }

    // Конструктор с параметрами
    public UserDto(int id, String username, String email, int age, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.roles = roles;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // Метод для преобразования User в UserDto
    public static UserDto fromUser(User user) {
        List<String> roleNames = user.getAllRoles().stream()
                .map(Role::getName) // Получаем имена ролей
                .collect(Collectors.toList());
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getAge(), roleNames);
    }
}

