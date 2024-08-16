package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


public interface UserService {
    User findByUsername(String username);

    void saveUser(User user);

    User showUserById(int id);

    List<User> getAllUsers();

    void updateUser(User user);

    void removeUserById(int id);

    public void deleteById(int id);
}