package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.user.*;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.mappers.UserMappers;
import com.ESD.ecomm.security.JwtUtil;
import com.ESD.ecomm.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints for registration, login, and token verification")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER NEW USER
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account and return JWT token")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO dto) {

        // Check if email already exists
        if (userService.existsByEmail(dto.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Email already exists"));
        }

        // Check if username already exists
        if (userService.existsByUsername(dto.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Username already exists"));
        }

        // Hash password before saving
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Create and save user
        User user = UserMappers.toUser(dto);
        User savedUser = userService.saveUser(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getUserRole().name());

        // Return response with token and user data
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createAuthResponse(token, UserMappers.toUserResponseDTO(savedUser), "User registered successfully"));
    }

    // LOGIN USER
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO dto) {

        // Find user by email
        Optional<User> userOpt = userService.getUserByEmail(dto.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Invalid email or password"));
        }

        User user = userOpt.get();

        // Verify password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Invalid email or password"));
        }

        // Check if user account is active
        if (!user.isActive()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse("Account is inactive. Please contact support."));
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getUserRole().name());

        // Return response with token and user data
        return ResponseEntity.ok(createAuthResponse(token, UserMappers.toUserResponseDTO(user), "Login successful"));
    }

    // VERIFY TOKEN
    @GetMapping("/verify")
    @Operation(summary = "Verify JWT token", description = "Check if the provided JWT token is valid")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Invalid token format. Use 'Bearer <token>'"));
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);
            Optional<User> userOpt = userService.getUserByUsername(username);

            if (userOpt.isPresent() && jwtUtil.validateToken(token, username)) {
                User user = userOpt.get();

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Token is valid");
                response.put("user", UserMappers.toUserResponseDTO(user));

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("Invalid or expired token"));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Invalid token: " + e.getMessage()));
        }
    }

    // Helper method: Create authentication success response
    private Map<String, Object> createAuthResponse(String token, UserResponseDTO user, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("token", token);
        response.put("user", user);
        return response;
    }

    // Helper method: Create error response
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}