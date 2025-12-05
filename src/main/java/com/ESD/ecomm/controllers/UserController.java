package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.user.*;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.mappers.UserMappers;
import com.ESD.ecomm.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // -----------------------------------------------------
    // ✔ REGISTER USER
    // -----------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO dto) {

        if (userService.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (userService.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = UserMappers.toUser(dto);
        userService.saveUser(user);

        return ResponseEntity.ok(UserMappers.toUserResponseDTO(user));
    }

    // -----------------------------------------------------
    // ✔ LOGIN
    // -----------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO dto) {

        Optional<User> userOpt = userService.getUserByEmail(dto.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userOpt.get();

        // NOTE: In real production → compare hashed passwords
        if (!user.getPassword().equals(dto.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        return ResponseEntity.ok(UserMappers.toUserResponseDTO(user));
    }

    // -----------------------------------------------------
    // ✔ GET ALL USERS
    // -----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers()
                .stream()
                .map(UserMappers::toUserResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // -----------------------------------------------------
    // ✔ GET USER BY ID
    // -----------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        Optional<User> userOpt = userService.getUserById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(UserMappers.toUserResponseDTO(userOpt.get()));
    }

    // -----------------------------------------------------
    // ✔ UPDATE USER
    // -----------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO dto
    ) {
        Optional<User> userOpt = userService.getUserById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        UserMappers.updateUserFromDTO(dto, user);
        userService.saveUser(user);

        return ResponseEntity.ok(UserMappers.toUserResponseDTO(user));
    }

    // -----------------------------------------------------
    // ✔ DELETE USER
    // -----------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        Optional<User> userOpt = userService.getUserById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // -----------------------------------------------------
    // ✔ CHECK EMAIL EXISTS
    // -----------------------------------------------------
    @GetMapping("/exists/email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    // -----------------------------------------------------
    // ✔ CHECK USERNAME EXISTS
    // -----------------------------------------------------
    @GetMapping("/exists/username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }
}
