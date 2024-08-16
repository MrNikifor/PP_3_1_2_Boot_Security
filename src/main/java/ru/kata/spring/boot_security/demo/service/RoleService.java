package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    List<Role> getRole(String roleName);

    List<Role> findAll();

    List<Role> findAllById(List<Integer> ids);
}