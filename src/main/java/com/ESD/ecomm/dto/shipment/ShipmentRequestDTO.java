package com.ESD.ecomm.dto.shipment;

import com.ESD.ecomm.enums.ShippingMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentRequestDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Shipping address ID is required")
    private Long shippingAddressId;

    @Size(max = 100)
    private String trackingNumber;

    @Size(max = 100)
    private String carrier;

    private ShippingMethod shippingMethod = ShippingMethod.STANDARD;

    private LocalDate estimatedDeliveryDate;

    private BigDecimal shippingCost;
}
