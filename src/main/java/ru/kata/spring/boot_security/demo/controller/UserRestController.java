package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;


@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<User> userPage(Authentication auth){
        User user = (User) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}

