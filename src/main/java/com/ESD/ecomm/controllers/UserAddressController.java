package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.address.*;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.entities.UserAddress;
import com.ESD.ecomm.services.UserAddressService;
import com.ESD.ecomm.services.UserService;
import com.ESD.ecomm.mappers.AddressMappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class UserAddressController {

    private final UserAddressService userAddressService;
    private final UserService userService;

    @Autowired
    public UserAddressController(UserAddressService userAddressService,
                                 UserService userService) {
        this.userAddressService = userAddressService;
        this.userService = userService;
    }

    // Get all addresses for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> getAddresses(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<AddressResponseDTO> addresses = userAddressService.getAddressesByUser(user).stream()
                .map(AddressMappers::toAddressResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(addresses);
    }

    // Add a new address
    @PostMapping
    public ResponseEntity<AddressResponseDTO> addAddress(@Valid @RequestBody AddressRequestDTO dto) {
        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserAddress address = AddressMappers.toUserAddress(dto);
        address.setUser(user);
        userAddressService.saveAddress(address);
        return new ResponseEntity<>(AddressMappers.toAddressResponseDTO(address), HttpStatus.CREATED);
    }

    // Update an address
    @PutMapping
    public ResponseEntity<AddressResponseDTO> updateAddress(@Valid @RequestBody AddressUpdateDTO dto) {
        UserAddress address = userAddressService.getAddressById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        AddressMappers.updateUserAddressFromDTO(dto, address);
        userAddressService.saveAddress(address);
        return ResponseEntity.ok(AddressMappers.toAddressResponseDTO(address));
    }

    // Delete an address
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        UserAddress address = userAddressService.getAddressById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        userAddressService.deleteAddress(address);
        return ResponseEntity.noContent().build();
    }
}
