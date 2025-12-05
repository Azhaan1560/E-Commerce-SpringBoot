package com.ESD.ecomm.dto.cart_item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemUpdateDTO {

    @NotNull(message = "Cart Item ID is required")
    private Long cartItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative. Use 0 to remove item")
    private Integer quantity;
}
