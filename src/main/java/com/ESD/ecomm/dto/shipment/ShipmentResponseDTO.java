package com.ESD.ecomm.dto.shipment;

import com.ESD.ecomm.enums.ShippingMethod;
import com.ESD.ecomm.enums.ShipmentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponseDTO {

    private Long id;
    private Long orderId;
    private Long shippingAddressId;
    private String trackingNumber;
    private String carrier;
    private ShippingMethod shippingMethod;
    private ShipmentStatus shipmentStatus;
    private LocalDate estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private BigDecimal shippingCost;
    private LocalDateTime shippedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
