package com.ESD.ecomm.dto.payment;

import com.ESD.ecomm.enums.PaymentMethod;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentUpdateDTO {

    private Long id;

    private PaymentMethod paymentMethod;

    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 10)
    private String currency;

    @Size(max = 100)
    private String transactionId;

    private String paymentGatewayResponse;
}
