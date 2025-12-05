package com.ESD.ecomm.dto.cart;

import com.ESD.ecomm.dto.cart_item.CartItemResponseDTO;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {

    private Long id;
    private Long userId; // Owner of the cart
    private List<CartItemResponseDTO> cartItems; // List of cart items
    private Integer totalItems;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
