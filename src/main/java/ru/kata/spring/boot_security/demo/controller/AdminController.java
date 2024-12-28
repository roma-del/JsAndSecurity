package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String findAllUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "adminPage";
    }

    @GetMapping("/userCreateForm")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", List.of("ROLE_USER", "ROLE_ADMIN"));
        return "userCreateForm";
    }

    @PostMapping("/userCreateForm")
    public String createUser(@ModelAttribute User user,
                             @RequestParam(value = "chose_role") String[] roles,
                             Model model,
                             BindingResult bindingResult) {
        try {
            userService.save(user, Arrays.asList(roles));
            return "redirect:/admin";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("allRoles", List.of("ROLE_USER", "ROLE_ADMIN"));
            model.addAttribute("errorMessage", "User with this username already exists.");
            model.addAttribute("user", new User());

            return "userCreateForm";
        }
    }

    @GetMapping("/userDelete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/userEditForm/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("users", user);
        return "userEditForm";
    }

    @PostMapping("/userEditForm/{id}")
    public String editUser(User user) {
        userService.edit(user);
        return "redirect:/admin";
    }

    @GetMapping("/checkUserPaper/{id}")
    public String userPage(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "user";
    }
}


