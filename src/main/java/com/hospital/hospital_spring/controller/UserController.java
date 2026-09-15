package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.User;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

   
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {

        return userService.takeAllUsers();
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {

        return userService.takeUserByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending/{role}")
    public List<User> getPendingUsersByRole(
            @PathVariable String role) {

        return userService.takePendingUsersByRole(role);
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public void createUser(@RequestBody User user) {

        userService.joinUser(user);
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}/status")
    public void updateStatus(
            @PathVariable String username,
            @RequestParam AccountStatus status) {

        userService.updateStatus(username, status);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    public void removeUser(@PathVariable String username) {

        userService.removeUser(username);
    }
}
