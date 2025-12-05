package com.ESD.ecomm.dto.order;

import com.ESD.ecomm.dto.order_item.OrderItemResponseDTO;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id;
    private String orderNumber;
    private Long userId;
    private Long shippingAddressId;
    private String orderNotes;
    private String orderStatus; // Enum as String
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingCost;
    private BigDecimal taxAmount;
    private BigDecimal finalAmount;
    private List<OrderItemResponseDTO> orderItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
