package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.order_item.OrderItemRequestDTO;
import com.ESD.ecomm.dto.order_item.OrderItemResponseDTO;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.OrderItem;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.services.OrderItemService;
import com.ESD.ecomm.services.OrderService;
import com.ESD.ecomm.services.ProductService;
import com.ESD.ecomm.mappers.OrderItemsMappers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders/{orderId}/items")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService,
                               OrderService orderService,
                               ProductService productService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.productService = productService;
    }

    // Get all items for an order
    @GetMapping
    public ResponseEntity<List<OrderItemResponseDTO>> getOrderItems(@PathVariable Long orderId) {
        Order order = orderService.getOrderByNumber(orderId.toString())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        List<OrderItemResponseDTO> items = orderItemService.getItemsByOrder(order)
                .stream()
                .map(OrderItemsMappers::toOrderItemResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    // Add new item to order
    @PostMapping
    public ResponseEntity<OrderItemResponseDTO> addOrderItem(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDTO dto) {

        Order order = orderService.getOrderByNumber(orderId.toString())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productService.getProductById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem orderItem = OrderItemsMappers.toOrderItem(dto, product);
        orderItem.setOrder(order);
        orderItemService.saveOrderItem(orderItem);

        return new ResponseEntity<>(OrderItemsMappers.toOrderItemResponseDTO(orderItem), HttpStatus.CREATED);
    }

    // Update an existing item
    @PutMapping("/{itemId}")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @Valid @RequestBody OrderItemRequestDTO dto) {

        Order order = orderService.getOrderByNumber(orderId.toString())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderItem orderItem = orderItemService.getItemByOrderAndProduct(order,
                        productService.getProductById(dto.getProductId())
                                .orElseThrow(() -> new RuntimeException("Product not found")))
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        // Update quantity, price, discount
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        orderItem.setDiscount(dto.getDiscount() != null ? dto.getDiscount() : orderItem.getDiscount());
        orderItem.calculateSubtotal();

        orderItemService.saveOrderItem(orderItem);

        return ResponseEntity.ok(OrderItemsMappers.toOrderItemResponseDTO(orderItem));
    }

    // Delete an item from an order
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {

        Order order = orderService.getOrderByNumber(orderId.toString())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderItem orderItem = orderItemService.getItemsByOrder(order)
                .stream()
                .filter(oi -> oi.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        orderItemService.deleteItemsByOrder(order); // Or delete specific item repository method
        return ResponseEntity.noContent().build();
    }
}
