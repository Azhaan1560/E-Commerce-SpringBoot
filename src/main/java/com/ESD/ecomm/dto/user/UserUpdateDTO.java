package com.ESD.ecomm.dto.user;

import com.ESD.ecomm.enums.UserRoles;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {

    @Size(min = 3, max = 50)
    private String username;

    @Size(max = 50)
    private String firstname;

    @Size(max = 50)
    private String lastname;

    @Email
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be 11 digits")
    private String phoneNumber;

    private UserRoles userRole;

    private Boolean isActive;
    private Boolean isEmailVerified;
}
