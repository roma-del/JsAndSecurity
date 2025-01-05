package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String findAllUsers(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("username", user);
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("newuser", new User());
        List<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);
        return "adminPage";
    }

    @PostMapping("/userCreateForm")
    public String createUser(@ModelAttribute User user,
                             @RequestParam(value = "role") String[] roles) {
        try {
            userService.save(user, Arrays.asList(roles));
            return "redirect:/admin";
        } catch (DataIntegrityViolationException e) {
        }
        return "redirect:/admin";
    }

    @GetMapping("/userDelete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/userEditForm/{id}")
    public String editUser(@ModelAttribute User user,
                           @RequestParam(value = "choose_role") String[] roles) {
        try {
            user.setRoles(roleService.editRoles(roles));
            userService.edit(user);
            return "redirect:/admin";
        } catch (DataIntegrityViolationException e) {
        }
        return "redirect:/admin";
    }

    @GetMapping("/checkUserPaper/{id}")
    public String userPage(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "user";
    }
}
