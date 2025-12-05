package com.ESD.ecomm.mappers;

import com.ESD.ecomm.dto.address.*;
import com.ESD.ecomm.entities.UserAddress;
import java.time.format.DateTimeFormatter;

public class AddressMappers {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // AddressRequestDTO -> UserAddress entity
    public static UserAddress toUserAddress(AddressRequestDTO dto) {
        if (dto == null) return null;
        return UserAddress.builder()
                // user should be set in service layer using userId
                .addressType(dto.getAddressType() != null ? dto.getAddressType() : com.ESD.ecomm.enums.AddressType.HOME)
                .fullName(dto.getFullName())
                .phoneNumber(dto.getPhoneNumber())
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .zipCode(dto.getZipCode())
                .isDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false)
                .build();
    }

    // AddressUpdateDTO -> update existing UserAddress entity
    public static void updateUserAddressFromDTO(AddressUpdateDTO dto, UserAddress address) {
        if (dto == null || address == null) return;

        if (dto.getAddressType() != null) address.setAddressType(dto.getAddressType());
        if (dto.getFullName() != null) address.setFullName(dto.getFullName());
        if (dto.getPhoneNumber() != null) address.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getAddressLine1() != null) address.setAddressLine1(dto.getAddressLine1());
        if (dto.getAddressLine2() != null) address.setAddressLine2(dto.getAddressLine2());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getState() != null) address.setState(dto.getState());
        if (dto.getCountry() != null) address.setCountry(dto.getCountry());
        if (dto.getZipCode() != null) address.setZipCode(dto.getZipCode());
        if (dto.getIsDefault() != null) address.setIsDefault(dto.getIsDefault());
    }

    // UserAddress entity -> AddressResponseDTO
    public static AddressResponseDTO toAddressResponseDTO(UserAddress address) {
        if (address == null) return null;

        return AddressResponseDTO.builder()
                .id(address.getId())
                .userId(address.getUser() != null ? address.getUser().getId() : null)
                .addressType(address.getAddressType())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .zipCode(address.getZipCode())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
}

