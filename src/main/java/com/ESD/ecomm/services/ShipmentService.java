package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.Shipment;
import com.ESD.ecomm.enums.ShipmentStatus;
import com.ESD.ecomm.repositories.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public Optional<Shipment> getShipmentByOrder(Order order) {
        return shipmentRepository.findByOrder(order);
    }

    public List<Shipment> getShipmentsByStatus(ShipmentStatus status) {
        return shipmentRepository.findByShipmentStatus(status);
    }

    public Shipment saveShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public boolean existsByTrackingNumber(String trackingNumber) {
        return shipmentRepository.existsByTrackingNumber(trackingNumber);
    }
}
