package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;


public interface UserService {
    User findById(Long id);

    User findByUsername(String username);

    void create(User user);

    void update(User user);

    List<User> findAll();

    void deleteById(Long id) throws EntityNotFoundException;
}