package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.order.*;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.OrderItem;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.entities.UserAddress;
import com.ESD.ecomm.enums.OrderStatus;
import com.ESD.ecomm.services.OrderItemService;
import com.ESD.ecomm.services.OrderService;
import com.ESD.ecomm.services.ProductService;
import com.ESD.ecomm.services.UserService;
import com.ESD.ecomm.services.UserAddressService;
import com.ESD.ecomm.mappers.OrdersMappers;
import com.ESD.ecomm.dto.order_item.OrderItemRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final UserService userService;
    private final UserAddressService userAddressService;

    @Autowired
    public OrderController(OrderService orderService,
                           OrderItemService orderItemService,
                           ProductService productService,
                           UserService userService,
                           UserAddressService userAddressService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.userService = userService;
        this.userAddressService = userAddressService;
    }

    // --- Private helper to map and save order items ---
    private List<OrderItem> mapAndSaveOrderItems(List<OrderItemRequestDTO> itemDTOs) {
        return itemDTOs.stream().map(dto -> {
            Product product = productService.getProductById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));
            OrderItem item = OrdersMappers.toItem(dto, product);
            orderItemService.saveOrderItem(item);
            return item;
        }).collect(Collectors.toList());
    }

    // Create new order
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserAddress address = userAddressService.getAddressById(dto.getShippingAddressId())
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        // Map and save order items
        List<OrderItem> items = mapAndSaveOrderItems(dto.getOrderItems());

        Order order = OrdersMappers.toOrder(dto, user, address, items);
        // Link items to order
        items.forEach(item -> item.setOrder(order));
        orderService.saveOrder(order);

        return new ResponseEntity<>(OrdersMappers.toResponseDTO(order), HttpStatus.CREATED);
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrdersByUser(null) // Replace null with user validation if needed
                .stream().filter(o -> o.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return ResponseEntity.ok(OrdersMappers.toResponseDTO(order));
    }

    // Get all orders for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderSummaryDTO>> getOrdersForUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<OrderSummaryDTO> orders = orderService.getOrdersByUser(user)
                .stream().map(OrdersMappers::toSummaryDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    // Update order
    @PutMapping
    public ResponseEntity<OrderResponseDTO> updateOrder(@Valid @RequestBody OrderUpdateDTO dto) {
        Order order = orderService.getOrdersByUser(null) // Replace null with proper lookup
                .stream().filter(o -> o.getId().equals(dto.getOrderId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"));

        UserAddress address = null;
        if (dto.getShippingAddressId() != null) {
            address = userAddressService.getAddressById(dto.getShippingAddressId())
                    .orElseThrow(() -> new RuntimeException("Address not found"));
        }

        List<OrderItem> items = null;
        if (dto.getOrderItems() != null && !dto.getOrderItems().isEmpty()) {
            items = mapAndSaveOrderItems(dto.getOrderItems());
        }

        OrdersMappers.updateOrder(dto, order, address, items);
        orderService.saveOrder(order);
        return ResponseEntity.ok(OrdersMappers.toResponseDTO(order));
    }

    // Update order status
    @PatchMapping("/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(@Valid @RequestBody OrderStatusUpdateDTO dto) {
        Order order = orderService.getOrdersByUser(null)
                .stream().filter(o -> o.getId().equals(dto.getOrderId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(OrderStatus.valueOf(dto.getOrderStatus()));
        orderService.saveOrder(order);

        return ResponseEntity.ok(OrdersMappers.toResponseDTO(order));
    }

    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderService.deleteOrder(order);
        return ResponseEntity.noContent().build();
    }
}

