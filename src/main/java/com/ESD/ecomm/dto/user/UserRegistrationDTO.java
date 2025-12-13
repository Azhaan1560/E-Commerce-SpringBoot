package com.ESD.ecomm.dto.user;

import com.ESD.ecomm.enums.UserRoles;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User registration information")
public class UserRegistrationDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    @Schema(description = "Unique username (3-50 characters)", example = "john_doe", required = true)
    private String username;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    @Schema(description = "User's first name", example = "John", required = true)
    private String firstname;

    @Size(max = 50)
    @Schema(description = "User's last name", example = "Doe")
    private String lastname;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Schema(description = "Valid email address", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Password (minimum 6 characters)", example = "securePassword123", required = true)
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be 11 digits")
    @Schema(description = "Phone number (11 digits)", example = "12345678901", required = true)
    private String phoneNumber;

    @Schema(description = "User role in the system", example = "CUSTOMER", defaultValue = "CUSTOMER")
    private UserRoles userRole; // default may be USER in controller
}
