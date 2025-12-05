package com.ESD.ecomm.mappers;
import com.ESD.ecomm.dto.order.*;
import com.ESD.ecomm.dto.order_item.OrderItemRequestDTO;
import com.ESD.ecomm.dto.order_item.OrderItemResponseDTO;
import com.ESD.ecomm.entities.Order;
import com.ESD.ecomm.entities.OrderItem;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.entities.UserAddress;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersMappers {

    // OrderRequestDTO → Order entity
    public static Order toOrder(OrderRequestDTO dto, User user, UserAddress address, List<OrderItem> items) {
        return Order.builder()
                .user(user)
                .shippingAddress(address)
                .orderNotes(dto.getOrderNotes())
                .orderItems(items)
                .totalAmount(items.stream()
                        .map(OrderItem::getSubtotal)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add))
                .discountAmount(java.math.BigDecimal.ZERO)
                .shippingCost(java.math.BigDecimal.ZERO)
                .taxAmount(java.math.BigDecimal.ZERO)
                .finalAmount(java.math.BigDecimal.ZERO)
                .build();
    }

    // OrderUpdateDTO → update Order entity
    public static void updateOrder(OrderUpdateDTO dto, Order order, UserAddress address, List<OrderItem> items) {
        if (dto.getShippingAddressId() != null) {
            order.setShippingAddress(address);
        }
        if (dto.getOrderNotes() != null) {
            order.setOrderNotes(dto.getOrderNotes());
        }
        if (items != null && !items.isEmpty()) {
            order.getOrderItems().clear();
            order.getOrderItems().addAll(items);
        }
    }

    // Order entity → OrderResponseDTO
    public static OrderResponseDTO toResponseDTO(Order order) {
        List<OrderItemResponseDTO> itemDTOs = order.getOrderItems().stream()
                .map(OrdersMappers::toItemResponseDTO)
                .collect(Collectors.toList());

        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .shippingAddressId(order.getShippingAddress().getId())
                .orderNotes(order.getOrderNotes())
                .orderStatus(order.getOrderStatus().name())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .shippingCost(order.getShippingCost())
                .taxAmount(order.getTaxAmount())
                .finalAmount(order.getFinalAmount())
                .orderItems(itemDTOs)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    // Order entity → OrderSummaryDTO
    public static OrderSummaryDTO toSummaryDTO(Order order) {
        return OrderSummaryDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getOrderStatus().name())
                .finalAmount(order.getFinalAmount())
                .createdAt(order.getCreatedAt())
                .build();
    }

    // OrderItemRequestDTO → OrderItem entity
    public static OrderItem toItem(OrderItemRequestDTO dto, Product product) {
        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .discount(dto.getDiscount() != null ? dto.getDiscount() : java.math.BigDecimal.ZERO)
                .build();
        item.calculateSubtotal();
        return item;
    }

    // OrderItem entity → OrderItemResponseDTO
    public static OrderItemResponseDTO toItemResponseDTO(OrderItem item) {
        return OrderItemResponseDTO.builder()
                .id(item.getId())
                .product(ProductMappers.toProductResponseDTO(item.getProduct()))
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .discount(item.getDiscount())
                .subtotal(item.getSubtotal())
                .createdAt(item.getCreatedAt())
                .build();
    }
}

