package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
                             @RequestParam(value = "chose_role", required = false) String[] roles,
                             Model model) {
        if (roles == null) {
            model.addAttribute("allRoles", List.of("ROLE_USER", "ROLE_ADMIN"));
            model.addAttribute("user", new User());
            return "userCreateForm";
        }
        try {
            userService.save(user, Arrays.asList(roles));
            return "redirect:/admin";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("allRoles", List.of("ROLE_USER", "ROLE_ADMIN"));
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
        model.addAttribute("user", user);
        model.addAttribute("allRoles", List.of("ROLE_USER", "ROLE_ADMIN"));
        return "userEditForm";
    }

    @PostMapping("/userEditForm/{id}")
    public String editUser(@PathVariable("id") Long id,
                           @ModelAttribute User user,
                           @RequestParam(value = "choose_role", required = false) String[] roles,
                           Model model) {
        if (roles == null) {
            model.addAttribute("allRoles", List.of("ROLE_USER", "ROLE_ADMIN"));
            model.addAttribute("user", user);
            return "redirect:/admin/userEditForm/{id}";
        }
        try {
            userService.editWithRoles(user, List.of(roles));
            return "redirect:/admin";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("allRoles", List.of("ROLE_USER", "ROLE_ADMIN"));
            model.addAttribute("user", user);
            return "redirect:/admin/userEditForm/{id}";
        }
    }

    @GetMapping("/checkUserPaper/{id}")
    public String userPage(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "user";
    }
}
