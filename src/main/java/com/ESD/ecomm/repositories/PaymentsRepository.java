package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.Payment;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface PaymentsRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder(Order order);

    List<Payment> findByPaymentStatus(PaymentStatus status);

    boolean existsByTransactionId(String transactionId);
}
