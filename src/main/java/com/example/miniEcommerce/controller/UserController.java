package com.example.miniEcommerce.controller;

import com.example.miniEcommerce.model.User;
import com.example.miniEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // CREATE
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // TODO: Validate user data
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // READ: Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // READ: Get user by email
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> userOpt = userService.findUserByEmail(email);
        return userOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User savedUser = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(savedUser);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
