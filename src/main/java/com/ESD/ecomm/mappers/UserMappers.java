package com.ESD.ecomm.mappers;

import com.ESD.ecomm.dto.user.*;
import com.ESD.ecomm.entities.User;
import java.time.format.DateTimeFormatter;

public class UserMappers {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static User toUser(UserRegistrationDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .username(dto.getUsername())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .userRole(dto.getUserRole() != null ? dto.getUserRole() : com.ESD.ecomm.enums.UserRoles.CUSTOMER)
                .active(true)
                .emailVerified(false)
                .build();
    }

    public static User toUser(UserRequestDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .username(dto.getUsername())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .userRole(dto.getUserRole())
                .active(true)
                .emailVerified(false)
                .build();
    }

    public static UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) return null;
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .userRole(user.getUserRole())
                .isActive(user.isActive())
                .isEmailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null)
                .updatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().format(formatter) : null)
                .build();
    }

    public static void updateUserFromDTO(UserUpdateDTO dto, User user) {
        if (dto == null || user == null) return;

        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getFirstname() != null) user.setFirstname(dto.getFirstname());
        if (dto.getLastname() != null) user.setLastname(dto.getLastname());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getUserRole() != null) user.setUserRole(dto.getUserRole());
        if (dto.getIsActive() != null) user.setActive(dto.getIsActive());
        if (dto.getIsEmailVerified() != null) user.setEmailVerified(dto.getIsEmailVerified());
    }
}
