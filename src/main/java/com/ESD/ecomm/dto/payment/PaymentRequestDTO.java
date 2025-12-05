package com.ESD.ecomm.dto.payment;

import com.ESD.ecomm.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 10)
    private String currency = "USD";

    @Size(max = 100)
    private String transactionId;

    private String paymentGatewayResponse;
}
