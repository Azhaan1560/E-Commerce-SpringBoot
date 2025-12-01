package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.Payment;
import com.ESD.ecomm.enums.PaymentStatus;
import com.ESD.ecomm.repositories.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentsRepository paymentsRepository;

    @Autowired
    public PaymentService(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    public Optional<Payment> getPaymentByOrder(Order order) {
        return paymentsRepository.findByOrder(order);
    }

    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentsRepository.findByPaymentStatus(status);
    }

    public Payment savePayment(Payment payment) {
        return paymentsRepository.save(payment);
    }

    public boolean existsByTransactionId(String transactionId) {
        return paymentsRepository.existsByTransactionId(transactionId);
    }
}
