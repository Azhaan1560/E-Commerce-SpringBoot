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
@Schema(description = "Data transfer object for updating user information")
public class UserUpdateDTO {

    @Size(min = 3, max = 50)
    @Schema(description = "Username (3-50 characters)", example = "john_doe_updated")
    private String username;

    @Size(max = 50)
    @Schema(description = "First name (max 50 characters)", example = "John")
    private String firstname;

    @Size(max = 50)
    @Schema(description = "Last name (max 50 characters)", example = "Doe")
    private String lastname;

    @Email
    @Schema(description = "Valid email address", example = "john.doe.updated@example.com")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "New password (minimum 6 characters)", example = "newSecurePassword123")
    private String password;

    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be 11 digits")
    @Schema(description = "Phone number (11 digits)", example = "12345678901")
    private String phoneNumber;

    @Schema(description = "User role in the system", example = "CUSTOMER")
    private UserRoles userRole;

    @Schema(description = "Account active status", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Email verification status", example = "true")
    private Boolean isEmailVerified;
}
