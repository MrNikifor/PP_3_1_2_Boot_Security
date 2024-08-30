package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exeption.RoleNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Role getRoleByName(String roleName) {
        return roleRepository.findRoleByName(roleName);
    }

    @Override
    @Transactional
    public Role findRoleById(int id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Роль с ID " + id + " не найдена"));
    }

    @Override
    @Transactional
    public boolean roleExists(int roleId) {
        return roleRepository.existsById(roleId);
    }
}