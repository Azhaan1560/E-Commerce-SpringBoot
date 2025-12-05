package com.ESD.ecomm.dto.order_item;

import com.ESD.ecomm.dto.product.ProductResponseDTO;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {

    private Long id;
    private ProductResponseDTO product;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal subtotal;
    private LocalDateTime createdAt;
}
