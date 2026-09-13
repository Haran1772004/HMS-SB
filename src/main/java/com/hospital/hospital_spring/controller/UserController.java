package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.User;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {

        return userService.takeUserByUsername(username);
    }

    @GetMapping
    public List<User> getAllUsers() {

        return userService.takeAllUsers();
    }
    
    @GetMapping("/pending/{role}")
    public List<User> getPendingUsersByRole(
            @PathVariable String role) {

        return userService.takePendingUsersByRole(role);
    }

    @PostMapping
    public void createUser(@RequestBody User user) {

        userService.joinUser(user);
    }

    @PutMapping("/{username}/status")
    public void updateStatus(
            @PathVariable String username,
            @RequestParam AccountStatus status) {

        userService.updateStatus(username, status);
    }

    @DeleteMapping("/{username}")
    public void removeUser(@PathVariable String username) {

        userService.removeUser(username);
    }
}