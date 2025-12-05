package com.ESD.ecomm.dto.cart_item;

import com.ESD.ecomm.dto.product.ProductResponseDTO;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO {

    private Long id;
    private ProductResponseDTO product; // Nested product info
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private LocalDateTime addedAt;
}
