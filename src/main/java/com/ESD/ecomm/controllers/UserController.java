package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.user.*;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.exception.ResourceNotFoundException;
import com.ESD.ecomm.mappers.UserMappers;
import com.ESD.ecomm.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // GET ALL USERS (Admin only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers()
                .stream()
                .map(UserMappers::toUserResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // GET USER BY ID (Authenticated users)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return ResponseEntity.ok(UserMappers.toUserResponseDTO(user));
    }

    // UPDATE USER (Authenticated users can update their own profile)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO dto
    ) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Hash password if it's being updated
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        UserMappers.updateUserFromDTO(dto, user);
        User updatedUser = userService.saveUser(user);

        return ResponseEntity.ok(UserMappers.toUserResponseDTO(updatedUser));
    }

    // DELETE USER (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userService.deleteUser(id);

        return ResponseEntity.ok()
                .body(java.util.Map.of(
                        "success", true,
                        "message", "User deleted successfully"
                ));
    }

    // CHECK EMAIL EXISTS (Public)
    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    // CHECK USERNAME EXISTS (Public)
    @GetMapping("/exists/username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }
}