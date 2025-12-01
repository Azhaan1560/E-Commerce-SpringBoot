package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.OrderItem;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.repositories.OrderItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public OrderItemService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

    public List<OrderItem> getItemsByOrder(Order order) {
        return orderItemsRepository.findByOrder(order);
    }

    public Optional<OrderItem> getItemByOrderAndProduct(Order order, Product product) {
        return orderItemsRepository.findByOrderAndProduct(order, product);
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemsRepository.save(orderItem);
    }

    public void deleteItemsByOrder(Order order) {
        orderItemsRepository.deleteByOrder(order);
    }

    public boolean existsByOrderAndProduct(Order order, Product product) {
        return orderItemsRepository.existsByOrderAndProduct(order, product);
    }
}
