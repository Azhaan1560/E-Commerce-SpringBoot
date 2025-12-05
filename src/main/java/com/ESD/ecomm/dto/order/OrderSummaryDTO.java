package com.ESD.ecomm.dto.order;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummaryDTO {

    private Long id;
    private String orderNumber;
    private String orderStatus;
    private BigDecimal finalAmount;
    private LocalDateTime createdAt;
}
