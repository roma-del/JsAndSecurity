package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public void editWithRoles(User user, List<String> roles);
    void save(User user, List<String> roles);
    void edit(User user);
    void delete(Long id);
    List<User> findAll();
    User findUserById(Long id);
    UserDetails loadUserByUsername(String username);
}
