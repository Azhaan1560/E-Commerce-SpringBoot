package com.ESD.ecomm.entities;
import com.ESD.ecomm.enums.ShipmentStatus;
import com.ESD.ecomm.enums.ShippingMethod;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="shipments",
        indexes = {
                @Index(name="idx_order", columnList="order_id"),
                @Index(name="idx_tracking", columnList="tracking_number"),
                @Index(name="idx_status", columnList="shipment_status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private UserAddress shippingAddress;

    @Column(name = "tracking_number", unique = true, length = 100)
    private String trackingNumber;

    @Column(length = 100)
    private String carrier;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method", nullable = false, length = 20)
    private ShippingMethod shippingMethod = ShippingMethod.STANDARD;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status", nullable = false, length = 20)
    private ShipmentStatus shipmentStatus = ShipmentStatus.PREPARING;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;

    @Column(name = "shipping_cost", precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

