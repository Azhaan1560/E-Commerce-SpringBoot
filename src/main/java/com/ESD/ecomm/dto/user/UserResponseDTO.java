package com.ESD.ecomm.dto.user;

import com.ESD.ecomm.enums.UserRoles;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private long id;

    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;

    private UserRoles userRole;

    private boolean isActive;
    private boolean isEmailVerified;

    private String createdAt;
    private String updatedAt;
}
