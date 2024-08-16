package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.exeption.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        model.addAttribute("all_users", userService.getAllUsers());
        return "admin/admin_home";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/add_user";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("newUser") User newUser,
                          @RequestParam("roles") List<String> roles) {
        if (roles.contains("ROLE_USER")) {
            newUser.setAll_roles(roleService.getRole("ROLE_USER"));
        }
        if (roles.contains("ROLE_ADMIN")) {
            newUser.setAll_roles(roleService.getRole("ROLE_ADMIN"));
        }
        userService.saveUser(newUser);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String showUserById(@PathVariable("id") int id, Model model) {
        try {
            User user = userService.showUserById(id);
            model.addAttribute("user", user);
            return "admin/selected_user";
        } catch (UserNotFoundException e) {
            return "redirect:/error_page";
        }
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("user", userService.showUserById(id));
            return "admin/edit_user";
        } catch (UserNotFoundException e) {
            return "redirect:/error_page";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        try {
            userService.updateUserById(id, user);
            return "redirect:/admin";
        } catch (UserNotFoundException e) {
            return "redirect:/error_page";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        try {
            userService.removeUserById(id);
            return "redirect:/admin";
        } catch (UserNotFoundException e) {
            return "redirect:/error_page";
        }
    }


}