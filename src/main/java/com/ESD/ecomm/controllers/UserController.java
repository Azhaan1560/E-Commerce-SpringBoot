package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.user.*;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.exception.ResourceNotFoundException;
import com.ESD.ecomm.mappers.UserMappers;
import com.ESD.ecomm.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users in the e-commerce system")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // GET ALL USERS (Admin only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users in the system. Admin access required.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers()
                .stream()
                .map(UserMappers::toUserResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // GET USER BY ID (Authenticated users)
    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a specific user by their unique identifier.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return ResponseEntity.ok(UserMappers.toUserResponseDTO(user));
    }

    // UPDATE USER (Authenticated users can update their own profile)
    @PutMapping("/{id}")
    @Operation(
            summary = "Update user profile",
            description = "Update an existing user's information. Users can update their own profile.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "User update information")
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
    @Operation(
            summary = "Delete user",
            description = "Delete a user from the system. Admin access required.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "User ID to delete", required = true, example = "1")
            @PathVariable Long id) {
        // Verify user exists before deletion
        userService.getUserById(id)
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
    @Operation(
            summary = "Check if email exists",
            description = "Verify if an email address is already registered in the system. Public endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email existence check completed",
                    content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    public ResponseEntity<Boolean> checkEmailExists(
            @Parameter(description = "Email address to check", required = true, example = "user@example.com")
            @RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    // CHECK USERNAME EXISTS (Public)
    @GetMapping("/exists/username")
    @Operation(
            summary = "Check if username exists",
            description = "Verify if a username is already taken in the system. Public endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Username existence check completed",
                    content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    public ResponseEntity<Boolean> checkUsernameExists(
            @Parameter(description = "Username to check", required = true, example = "john_doe")
            @RequestParam String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }
}