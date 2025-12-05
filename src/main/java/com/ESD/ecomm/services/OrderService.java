package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.enums.OrderStatus;
import com.ESD.ecomm.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getOrdersByUserAndStatus(User user, OrderStatus status) {
        return orderRepository.findByUserAndOrderStatus(user, status);
    }

    public Optional<Order> getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public boolean existsByOrderNumber(String orderNumber) {
        return orderRepository.existsByOrderNumber(orderNumber);
    }

    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

}
