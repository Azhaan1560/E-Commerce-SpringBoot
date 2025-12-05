package com.ESD.ecomm.dto.shipment;

import com.ESD.ecomm.enums.ShippingMethod;
import com.ESD.ecomm.enums.ShipmentStatus;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentUpdateDTO {

    private Long id;

    @Size(max = 100)
    private String trackingNumber;

    @Size(max = 100)
    private String carrier;

    private ShippingMethod shippingMethod;

    private ShipmentStatus shipmentStatus;

    private LocalDate estimatedDeliveryDate;

    private LocalDateTime actualDeliveryDate;

    private BigDecimal shippingCost;
}
