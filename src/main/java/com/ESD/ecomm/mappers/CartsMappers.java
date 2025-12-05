package com.ESD.ecomm.mappers;
import com.ESD.ecomm.dto.cart.CartResponseDTO;
import com.ESD.ecomm.entities.Cart;
import java.util.stream.Collectors;

public class CartsMappers {

    // Cart entity -> CartResponseDTO
    public static CartResponseDTO toCartResponseDTO(Cart cart) {
        if (cart == null) return null;

        return CartResponseDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser() != null ? cart.getUser().getId() : null)
                .cartItems(cart.getCartItems().stream()
                        .map(com.ESD.ecomm.mappers.CartItemMappers::toCartItemResponseDTO)
                        .collect(Collectors.toList()))
                .totalItems(cart.getTotalItems())
                .totalPrice(cart.getTotalPrice())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}
