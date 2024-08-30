package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AdditionalService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.validators.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final AdditionalService additionalService;
    private final UserValidator userValidator;

    public AdminController(UserService userService, RoleService roleService, AdditionalService additionalService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.additionalService = additionalService;
        this.userValidator = userValidator;
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model, Principal principal) {
        model.addAttribute("newUser", new User());
        additionalService.createModelForView(model, principal);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("activeTab", "usersTable");
        return "adminPage";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") int id, Model model) {
        User user = userService.showUserById(id);
        model.addAttribute("userIter", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "editUser";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("userIter") @Valid User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "editUser";
        }

        userService.updateUser(user);
        return "redirect:/users/admin";
    }

    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult,
                          Principal principal, Model model) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            additionalService.createModelForView(model, principal);
            model.addAttribute("activeTab", "addUser");
            model.addAttribute("roles", roleService.getAllRoles());
            return "adminPage";
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

        userService.saveUser(user);
        return "redirect:/users/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") int id) {
        if (id > 0) {
            userService.deleteById(id);
        }
        return "redirect:/users/admin";
    }
}
