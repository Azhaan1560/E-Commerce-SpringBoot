package com.ESD.ecomm.dto.payment;

import com.ESD.ecomm.enums.PaymentMethod;
import com.ESD.ecomm.enums.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private BigDecimal amount;
    private String currency;
    private String paymentGatewayResponse;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
