package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
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
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class AdminRestController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public Map<String, Object> findAllUsers(@AuthenticationPrincipal User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("usernav", user);
        data.put("users", userService.findAll());
        data.put("allRoles", roleService.findAll());
        return data;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findAllUsers(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping("/userCreateForm")
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user) {
        Logger log = Logger.getLogger(AdminRestController.class.getName());
        log.warning("Creating user " + user.getUsername() +  user.getAge() + user.getPassword() + user.getFirstName() + user.getLastName());
        log.info("roles" + user.getRoles());
        try {
            userService.save(user, List.of(user.getRoles().split(" ")));
        } catch (DataIntegrityViolationException e) {
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/userDelete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
    }

    @PostMapping("/userEditForm/{id}")
    public ResponseEntity<String> editUser(@PathVariable("id") Long id,
                                           @RequestBody User user) {
        if (user.getPassword() == null) {
            user.setPassword(userService.findUserById(id).getPassword());
        }
        Logger log = Logger.getLogger(AdminRestController.class.getName());

        log.warning(user.getPassword() + " " + user.getAge() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getUsername() + " " + user.getId());
        log.info(" " + user.getRoles());
        try {
            user.setRoles(roleService.editRoles(user.getRoles()));
            userService.edit(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User updated");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User not updated");
        }
    }
}
