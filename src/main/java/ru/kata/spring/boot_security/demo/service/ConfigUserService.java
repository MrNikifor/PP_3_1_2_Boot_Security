package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exeption.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfigUserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с именем " + username + " не найден");
        }
        return user;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", username));
        }
        return user;
    }
    /*@Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return new org.springframework.security.core.userdetails.User(
                    "admin",
                    "$2a$10$.Y2hHiBAHr9WOWFB2C21AuZOXNdkGMRp0jvii/LI83QnJl7YE5FE2",
                    true,
                    true,
                    true,
                    true,
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        } else if ("user".equals(username)) {
            return new org.springframework.security.core.userdetails.User(
                    "user",
                    "$2a$10$muhavE5kfwwEuqjPYu4J7uD8u67.WLW0TCj9N/XXvA/R2Fd8DiL0O",
                    true,
                    true,
                    true,
                    true,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }
        throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", username));
    }*/
}