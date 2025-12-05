package com.ESD.ecomm.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
import com.ESD.ecomm.dto.order_item.OrderItemRequestDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    private Long shippingAddressId; // Optional update

    private String orderNotes;

    private List<OrderItemRequestDTO> orderItems; // Optional update to items
}
