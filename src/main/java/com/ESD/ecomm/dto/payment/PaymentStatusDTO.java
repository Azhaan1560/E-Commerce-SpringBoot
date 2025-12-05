package com.ESD.ecomm.dto.payment;

import com.ESD.ecomm.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatusDTO {

    @NotNull(message = "Payment ID is required")
    private Long id;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;
}
