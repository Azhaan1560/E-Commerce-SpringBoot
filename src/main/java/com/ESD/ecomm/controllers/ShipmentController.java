package com.ESD.ecomm.controllers;
import com.ESD.ecomm.dto.shipment.*;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.Shipment;
import com.ESD.ecomm.entities.UserAddress;
import com.ESD.ecomm.services.OrderService;
import com.ESD.ecomm.services.ShipmentService;
import com.ESD.ecomm.services.UserAddressService;
import com.ESD.ecomm.mappers.ShipmentMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final OrderService orderService;
    private final UserAddressService userAddressService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService,
                              OrderService orderService,
                              UserAddressService userAddressService) {
        this.shipmentService = shipmentService;
        this.orderService = orderService;
        this.userAddressService = userAddressService;
    }

    // Create a new shipment
    @PostMapping
    public ResponseEntity<ShipmentResponseDTO> createShipment(@Valid @RequestBody ShipmentRequestDTO dto) {
        Order order = orderService.getOrderById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        UserAddress address = userAddressService.getAddressById(dto.getShippingAddressId())
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        Shipment shipment = ShipmentMappers.toShipment(dto);
        shipment.setOrder(order);
        shipment.setShippingAddress(address);

        shipment = shipmentService.saveShipment(shipment);

        return new ResponseEntity<>(ShipmentMappers.toShipmentResponseDTO(shipment), HttpStatus.CREATED);
    }

    // Update shipment details
    @PutMapping
    public ResponseEntity<ShipmentResponseDTO> updateShipment(@Valid @RequestBody ShipmentUpdateDTO dto) {
        Shipment shipment = shipmentService.getShipmentByOrder(
                orderService.getOrderById(dto.getId())
                        .orElseThrow(() -> new RuntimeException("Order not found"))
        ).orElseThrow(() -> new RuntimeException("Shipment not found"));

        ShipmentMappers.updateShipmentFromDTO(dto, shipment);
        shipment = shipmentService.saveShipment(shipment);

        return ResponseEntity.ok(ShipmentMappers.toShipmentResponseDTO(shipment));
    }

    // Get shipment by order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShipmentResponseDTO> getShipmentByOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Shipment shipment = shipmentService.getShipmentByOrder(order)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        return ResponseEntity.ok(ShipmentMappers.toShipmentResponseDTO(shipment));
    }

    // Get shipments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShipmentResponseDTO>> getShipmentsByStatus(@PathVariable String status) {
        List<Shipment> shipments = shipmentService.getShipmentsByStatus(
                com.ESD.ecomm.enums.ShipmentStatus.valueOf(status.toUpperCase())
        );

        List<ShipmentResponseDTO> response = shipments.stream()
                .map(ShipmentMappers::toShipmentResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Get shipment tracking info
    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<ShipmentTrackingDTO> getShipmentTracking(@PathVariable String trackingNumber) {
        Shipment shipment = shipmentService.existsByTrackingNumber(trackingNumber)
                ? shipmentService.getShipmentsByStatus(null).stream()
                .filter(s -> s.getTrackingNumber().equals(trackingNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shipment not found"))
                : null;

        if (shipment == null) throw new RuntimeException("Shipment not found");

        return ResponseEntity.ok(ShipmentMappers.toShipmentTrackingDTO(shipment));
    }
}
