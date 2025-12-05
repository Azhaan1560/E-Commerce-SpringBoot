package com.ESD.ecomm.mappers;

import com.ESD.ecomm.dto.payment.*;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.Payment;

public class PaymentMappers {

    // PaymentRequestDTO -> Payment entity
    public static Payment toPayment(PaymentRequestDTO dto, Order order) {
        if (dto == null || order == null) return null;

        Payment payment = Payment.builder()
                .order(order)
                .paymentMethod(dto.getPaymentMethod())
                .amount(dto.getAmount())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "USD")
                .transactionId(dto.getTransactionId())
                .paymentGatewayResponse(dto.getPaymentGatewayResponse())
                .build();

        return payment;
    }

    // Payment entity -> PaymentResponseDTO
    public static PaymentResponseDTO toPaymentResponseDTO(Payment payment) {
        if (payment == null) return null;

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentGatewayResponse(payment.getPaymentGatewayResponse())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    // Update Payment from PaymentUpdateDTO
    public static void updatePaymentFromDTO(PaymentUpdateDTO dto, Payment payment) {
        if (dto == null || payment == null) return;

        if (dto.getPaymentMethod() != null) payment.setPaymentMethod(dto.getPaymentMethod());
        if (dto.getAmount() != null) payment.setAmount(dto.getAmount());
        if (dto.getCurrency() != null) payment.setCurrency(dto.getCurrency());
        if (dto.getTransactionId() != null) payment.setTransactionId(dto.getTransactionId());
        if (dto.getPaymentGatewayResponse() != null) payment.setPaymentGatewayResponse(dto.getPaymentGatewayResponse());
    }

    // Update Payment status from PaymentStatusDTO
    public static void updatePaymentStatusFromDTO(PaymentStatusDTO dto, Payment payment) {
        if (dto == null || payment == null) return;

        if (dto.getPaymentStatus() != null) {
            payment.setPaymentStatus(dto.getPaymentStatus());
            if (dto.getPaymentStatus() == com.ESD.ecomm.enums.PaymentStatus.COMPLETED) {
                payment.setPaidAt(java.time.LocalDateTime.now());
            }
        }
    }
}
