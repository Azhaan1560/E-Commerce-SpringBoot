package com.ESD.ecomm.mappers;
import com.ESD.ecomm.dto.order_item.*;
import com.ESD.ecomm.entities.OrderItem;
import com.ESD.ecomm.entities.Product;
import java.math.BigDecimal;
public class OrderItemsMappers {

    // OrderItemRequestDTO -> OrderItem entity
    public static OrderItem toOrderItem(OrderItemRequestDTO dto, Product product) {
        if (dto == null || product == null) return null;

        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .discount(dto.getDiscount() != null ? dto.getDiscount() : BigDecimal.ZERO)
                .build();

        item.calculateSubtotal(); // compute subtotal
        return item;
    }

    // OrderItem entity -> OrderItemResponseDTO
    public static OrderItemResponseDTO toOrderItemResponseDTO(OrderItem item) {
        if (item == null) return null;

        return OrderItemResponseDTO.builder()
                .id(item.getId())
                .product(item.getProduct() != null ? ProductMappers.toProductResponseDTO(item.getProduct()) : null)
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .discount(item.getDiscount())
                .subtotal(item.getSubtotal())
                .createdAt(item.getCreatedAt())
                .build();
    }
}
