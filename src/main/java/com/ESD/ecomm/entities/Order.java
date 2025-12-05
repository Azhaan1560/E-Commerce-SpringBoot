package com.ESD.ecomm.entities;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ESD.ecomm.enums.OrderStatus;
@Entity
@Table(name="orders",
        indexes = {
                @Index(name="orders_idx_user", columnList="user_id"),
                @Index(name="orders_idx_order_number", columnList="order_number"),
                @Index(name="orders_idx_status", columnList="order_status"),
                @Index(name="orders_idx_created", columnList="created_at")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="shipping_address_id", nullable=false)
    private UserAddress shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name="order_number", nullable=false, unique=true, length=50)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="order_status", nullable=false, length=20)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(name="total_amount", nullable=false, precision=10, scale=2)
    private BigDecimal totalAmount;

    @Column(name="discount_amount", precision=10, scale=2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name="shipping_cost", precision=10, scale=2)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(name="tax_amount", precision=10, scale=2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name="final_amount", nullable=false, precision=10, scale=2)
    private BigDecimal finalAmount;

    @Column(name="order_notes", columnDefinition="TEXT")
    private String orderNotes;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
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

    public void calculateFinalAmount() {
        this.finalAmount = totalAmount.add(shippingCost).add(taxAmount).subtract(discountAmount);
    }
}
