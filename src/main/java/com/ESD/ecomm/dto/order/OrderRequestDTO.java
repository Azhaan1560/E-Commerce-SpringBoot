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
public class OrderRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Shipping address ID is required")
    private Long shippingAddressId;

    private String orderNotes;

    @NotNull(message = "Order items are required")
    private List<OrderItemRequestDTO> orderItems; // Nested order items
}
