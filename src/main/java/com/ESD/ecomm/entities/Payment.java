package com.ESD.ecomm.entities;
import com.ESD.ecomm.enums.PaymentMethod;
import com.ESD.ecomm.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payments",
        indexes = {
                @Index(name="pay_idx_order", columnList="order_id"),
                @Index(name="pay_idx_transaction", columnList="transaction_id"),
                @Index(name="pay_idx_status", columnList="payment_status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_method", nullable = false, length=20)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_status", nullable = false, length=20)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name="transaction_id", unique = true, length=100)
    private String transactionId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 10)
    private String currency = "USD";

    @Column(name="payment_gateway_response", columnDefinition = "TEXT")
    private String paymentGatewayResponse;

    @Column(name="paid_at")
    private LocalDateTime paidAt;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
