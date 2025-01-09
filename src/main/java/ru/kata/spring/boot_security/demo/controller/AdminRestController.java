package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class AdminRestController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> findAllUsers(@AuthenticationPrincipal User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", user);
        data.put("users", userService.findAll());
        data.put("allRoles", roleService.findAll());
//        data.put("newuser", new User());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/userCreateForm")
    public ResponseEntity<String> createUser(@ModelAttribute User user,
                             @RequestParam(value = "role") String[] roles) {
        try {
            userService.save(user, Arrays.asList(roles));
            return ResponseEntity.status(HttpStatus.CREATED).body("User created");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
    }

    @GetMapping("/userDelete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
    }

    @PostMapping("/userEditForm/{id}")
    public ResponseEntity<String> editUser(@ModelAttribute User user,
                           @RequestParam(value = "choose_role") String[] roles) {
        try {
            user.setRoles(roleService.editRoles(roles));
            userService.edit(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User updated");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User not updated");
        }
    }

}
