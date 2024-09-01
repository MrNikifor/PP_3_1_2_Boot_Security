package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exeption.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем " + username + " не найден"));
    }

    @Override
    public void saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getAllRoles() != null) {
            Set<Role> validRoles = new HashSet<>();
            for (Role role : user.getAllRoles()) {
                if (roleService.roleExists(role.getId())) {
                    validRoles.add(role);
                }
            }
            user.setAllRoles(validRoles);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User showUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean usernameExists(String username, Integer userId) {
        User existingUser = userRepository.findByUsername(username);
        return existingUser != null && (userId == null || existingUser.getId() != userId);
    }

    @Override
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + user.getId() + " не найден"));

        existingUser.setUsername(user.getUsername());
        existingUser.setAge(user.getAge());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getAllRoles() != null) {
            existingUser.setAllRoles(user.getAllRoles());
        }

        userRepository.save(existingUser);
    }


    @Override
    public void deleteById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
        userRepository.delete(user);
    }
}