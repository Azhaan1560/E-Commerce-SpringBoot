package com.ESD.ecomm.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Order status is required")
    private String orderStatus; // Enum as string
}
