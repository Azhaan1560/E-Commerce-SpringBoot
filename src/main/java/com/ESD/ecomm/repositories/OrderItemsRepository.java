package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.OrderItem;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);

    void deleteByOrder(Order order);

    boolean existsByOrderAndProduct(Order order, Product product);
}

