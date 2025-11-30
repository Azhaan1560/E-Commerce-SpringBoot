package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findByUserAndOrderStatus(User user, OrderStatus status);

    Optional<Order> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);
}

