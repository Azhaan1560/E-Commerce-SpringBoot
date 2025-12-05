package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.payment.*;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.Payment;
import com.ESD.ecomm.services.OrderService;
import com.ESD.ecomm.services.PaymentService;
import com.ESD.ecomm.mappers.PaymentMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @Autowired
    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    // Create a new payment
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO dto) {
        Order order = orderService.getOrderById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = PaymentMappers.toPayment(dto, order);
        payment = paymentService.savePayment(payment);

        return new ResponseEntity<>(PaymentMappers.toPaymentResponseDTO(payment), HttpStatus.CREATED);
    }

    // Update payment details
    @PutMapping
    public ResponseEntity<PaymentResponseDTO> updatePayment(@Valid @RequestBody PaymentUpdateDTO dto) {
        Payment payment = paymentService.getPaymentByOrder(orderService.getOrderById(dto.getId())
                        .orElseThrow(() -> new RuntimeException("Payment or Order not found")))
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        PaymentMappers.updatePaymentFromDTO(dto, payment);
        payment = paymentService.savePayment(payment);

        return ResponseEntity.ok(PaymentMappers.toPaymentResponseDTO(payment));
    }

    // Update payment status
    @PatchMapping("/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(@Valid @RequestBody PaymentStatusDTO dto) {
        Payment payment = paymentService.getPaymentByOrder(orderService.getOrderById(dto.getId())
                        .orElseThrow(() -> new RuntimeException("Payment or Order not found")))
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        PaymentMappers.updatePaymentStatusFromDTO(dto, payment);
        payment = paymentService.savePayment(payment);

        return ResponseEntity.ok(PaymentMappers.toPaymentResponseDTO(payment));
    }

    // Get payment by order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = paymentService.getPaymentByOrder(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return ResponseEntity.ok(PaymentMappers.toPaymentResponseDTO(payment));
    }

    // Get all payments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(@PathVariable String status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(
                com.ESD.ecomm.enums.PaymentStatus.valueOf(status.toUpperCase())
        );

        List<PaymentResponseDTO> response = payments.stream()
                .map(PaymentMappers::toPaymentResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
