package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.Shipment;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.enums.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByOrder(Order order);

    List<Shipment> findByShipmentStatus(ShipmentStatus status);

    boolean existsByTrackingNumber(String trackingNumber);
}

