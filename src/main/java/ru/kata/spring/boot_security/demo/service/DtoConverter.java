package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DtoConverter {
    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());

        // Извлекаем имена ролей
        List<String> roleNames = user.getAllRoles().stream()
                .map(Role::getName) // Получаем имя роли
                .collect(Collectors.toList());

        userDto.setRoles(roleNames); // Устанавливаем роли в UserDto
        return userDto;
    }
}

