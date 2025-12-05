package com.ESD.ecomm.mappers;

import com.ESD.ecomm.dto.shipment.*;
import com.ESD.ecomm.entities.Shipment;

public class ShipmentMappers {

    // ShipmentRequestDTO -> Shipment entity
    public static Shipment toShipment(ShipmentRequestDTO dto) {
        if (dto == null) return null;

        return Shipment.builder()
                .trackingNumber(dto.getTrackingNumber())
                .carrier(dto.getCarrier())
                .shippingMethod(dto.getShippingMethod())
                .estimatedDeliveryDate(dto.getEstimatedDeliveryDate())
                .shippingCost(dto.getShippingCost())
                .build();
    }

    // ShipmentUpdateDTO -> update existing Shipment entity
    public static void updateShipmentFromDTO(ShipmentUpdateDTO dto, Shipment shipment) {
        if (dto == null || shipment == null) return;

        if (dto.getTrackingNumber() != null) shipment.setTrackingNumber(dto.getTrackingNumber());
        if (dto.getCarrier() != null) shipment.setCarrier(dto.getCarrier());
        if (dto.getShippingMethod() != null) shipment.setShippingMethod(dto.getShippingMethod());
        if (dto.getShipmentStatus() != null) shipment.setShipmentStatus(dto.getShipmentStatus());
        if (dto.getEstimatedDeliveryDate() != null) shipment.setEstimatedDeliveryDate(dto.getEstimatedDeliveryDate());
        if (dto.getActualDeliveryDate() != null) shipment.setActualDeliveryDate(dto.getActualDeliveryDate());
        if (dto.getShippingCost() != null) shipment.setShippingCost(dto.getShippingCost());
    }

    // Shipment entity -> ShipmentResponseDTO
    public static ShipmentResponseDTO toShipmentResponseDTO(Shipment shipment) {
        if (shipment == null) return null;

        return ShipmentResponseDTO.builder()
                .id(shipment.getId())
                .orderId(shipment.getOrder() != null ? shipment.getOrder().getId() : null)
                .shippingAddressId(shipment.getShippingAddress() != null ? shipment.getShippingAddress().getId() : null)
                .trackingNumber(shipment.getTrackingNumber())
                .carrier(shipment.getCarrier())
                .shippingMethod(shipment.getShippingMethod())
                .shipmentStatus(shipment.getShipmentStatus())
                .estimatedDeliveryDate(shipment.getEstimatedDeliveryDate())
                .actualDeliveryDate(shipment.getActualDeliveryDate())
                .shippingCost(shipment.getShippingCost())
                .shippedAt(shipment.getShippedAt())
                .createdAt(shipment.getCreatedAt())
                .updatedAt(shipment.getUpdatedAt())
                .build();
    }

    // Optional: Shipment entity -> ShipmentTrackingDTO
    public static ShipmentTrackingDTO toShipmentTrackingDTO(Shipment shipment) {
        if (shipment == null) return null;

        return ShipmentTrackingDTO.builder()
                .trackingNumber(shipment.getTrackingNumber())
                .shipmentStatus(shipment.getShipmentStatus())
                .shippedAt(shipment.getShippedAt())
                .estimatedDelivery(shipment.getEstimatedDeliveryDate() != null ?
                        shipment.getEstimatedDeliveryDate().atStartOfDay() : null)
                .actualDelivery(shipment.getActualDeliveryDate())
                .build();
    }
}
