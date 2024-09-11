package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, Validator validator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void create(User user) {
        validateUser(user, true);
        user.setPassword(passwordEncoder.encode(user.getRawPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        validateUser(user, false);
        if (!user.getRawPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getRawPassword()));
        } else {
            user.setPassword(findById(user.getId()).getPassword());
        }
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return new HashSet<User>(userRepository.findAll()).stream().toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws EntityNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public void validateUser(User user, boolean isNew) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        StringBuilder sb = new StringBuilder();
        if (isNew && user.getRawPassword().isEmpty()) {
            sb.append("User password must not be empty");
            sb.append("; ");
        }
        if (isNew || (!Objects.equals(user.getUsername(), userRepository.findById(user.getId()).get().getUsername()))) {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                sb.append("User with this username already exists");
                sb.append("; ");
            }
        }
        if (user.getRoles().isEmpty()) {
            sb.append("User roles must not be empty");
            sb.append("; ");
        }
        if (!violations.isEmpty()) {
            for (ConstraintViolation<User> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
                sb.append("; ");
            }
        }
        if (!sb.isEmpty()) {
            throw new ConstraintViolationException(sb.toString(), violations);
        }
    }
}