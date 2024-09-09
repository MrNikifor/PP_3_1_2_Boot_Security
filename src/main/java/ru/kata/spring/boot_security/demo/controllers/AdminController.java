package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exeption.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AdditionalService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.validators.UserValidator;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RoleService roleService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = userService.showUserById(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден!");
        }
        return ResponseEntity.ok(user);
    }


    @PutMapping("/users")
    public ResponseEntity<User> updateUserByUsername(@RequestParam("username") String username, @RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }


        User existingUser = userService.findByUsername(username);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        if (userService.usernameExists(user.getUsername(), existingUser.getId())) {
            throw new ValidationException("Имя пользователя уже существует");
        }


        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setAge(user.getAge());
        existingUser.setAllRoles(user.getAllRoles());

        userService.updateUser(existingUser);

        return ResponseEntity.ok(existingUser);
    }



    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {

        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}



/*@Controller
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
        return "admin-page";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") int id, Model model) {
        User user = userService.showUserById(id);
        model.addAttribute("userIter", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "edit-user";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("userIter") @Valid User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "edit-user";
        }

        if (userService.usernameExists(user.getUsername(), user.getId())) {
            bindingResult.rejectValue("username", "error.userIter", "Пользователь с таким именем уже существует.");
            model.addAttribute("roles", roleService.getAllRoles());
            return "edit-user";
        }

        userService.updateUser(user);
        return "redirect:/users/admin";
    }

    @GetMapping("/add-new")
    public String showAddUserForm(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "add-new-user";
    }

    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult,
                          Principal principal, Model model) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            additionalService.createModelForView(model, principal);
            model.addAttribute("activeTab", "addUser");
            model.addAttribute("roles", roleService.getAllRoles());
            return "add-new-user";
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
}*/
