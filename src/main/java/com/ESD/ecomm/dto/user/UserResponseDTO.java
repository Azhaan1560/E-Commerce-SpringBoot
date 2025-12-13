package com.ESD.ecomm.dto.user;

import com.ESD.ecomm.enums.UserRoles;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User response data transfer object containing user information")
public class UserResponseDTO {

    @Schema(description = "Unique user identifier", example = "1")
    private long id;

    @Schema(description = "User's unique username", example = "john_doe")
    private String username;
    
    @Schema(description = "User's first name", example = "John")
    private String firstname;
    
    @Schema(description = "User's last name", example = "Doe")
    private String lastname;
    
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "User's phone number", example = "12345678901")
    private String phoneNumber;

    @Schema(description = "User's role in the system", example = "CUSTOMER")
    private UserRoles userRole;

    @Schema(description = "Whether the user account is active", example = "true")
    private boolean isActive;
    
    @Schema(description = "Whether the user's email has been verified", example = "false")
    private boolean isEmailVerified;

    @Schema(description = "Account creation timestamp", example = "2024-01-15 10:30:00")
    private String createdAt;
    
    @Schema(description = "Last update timestamp", example = "2024-01-15 10:30:00")
    private String updatedAt;
}
