package com.ESD.ecomm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User login credentials")
public class UserLoginDTO {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User's password", example = "securePassword123", required = true)
    private String password;
}
