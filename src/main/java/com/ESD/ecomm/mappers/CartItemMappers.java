package com.ESD.ecomm.mappers;
import com.ESD.ecomm.dto.cart_item.*;
import com.ESD.ecomm.entities.Cart_Items;
import com.ESD.ecomm.entities.Product;

public class CartItemMappers {

    // CartItemRequestDTO -> Cart_Items entity
    public static Cart_Items toCartItem(CartItemRequestDTO dto, Product product, com.ESD.ecomm.entities.Cart cart) {
        if (dto == null || product == null || cart == null) return null;

        Cart_Items cartItem = Cart_Items.builder()
                .cart(cart)
                .product(product)
                .quantity(dto.getQuantity())
                .price(product.getProdPrice()) // Assuming price is product price at time of adding
                .build();

        cartItem.calculateSubtotal(); // compute subtotal
        return cartItem;
    }

    // CartItemUpdateDTO -> update existing Cart_Items entity
    public static void updateCartItemFromDTO(CartItemUpdateDTO dto, Cart_Items cartItem) {
        if (dto == null || cartItem == null) return;

        if (dto.getQuantity() != null) {
            cartItem.setQuantity(dto.getQuantity());
            cartItem.calculateSubtotal(); // recalculate subtotal
        }
    }

    // Cart_Items entity -> CartItemResponseDTO
    public static CartItemResponseDTO toCartItemResponseDTO(Cart_Items cartItem) {
        if (cartItem == null) return null;

        return CartItemResponseDTO.builder()
                .id(cartItem.getId())
                .product(com.ESD.ecomm.mappers.ProductMappers.toProductResponseDTO(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .subtotal(cartItem.getSubtotal())
                .addedAt(cartItem.getAddedAt())
                .build();
    }
}
