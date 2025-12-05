package com.ESD.ecomm.dto.shipment;

import com.ESD.ecomm.enums.ShipmentStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentTrackingDTO {

    private String trackingNumber;
    private ShipmentStatus shipmentStatus;
    private LocalDateTime shippedAt;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime actualDelivery;
}
