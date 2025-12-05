package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.cart_item.CartItemRequestDTO;
import com.ESD.ecomm.dto.cart_item.CartItemResponseDTO;
import com.ESD.ecomm.dto.cart_item.CartItemUpdateDTO;
import com.ESD.ecomm.entities.Cart;
import com.ESD.ecomm.entities.Cart_Items;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.mappers.CartItemMappers;
import com.ESD.ecomm.services.CartItemService;
import com.ESD.ecomm.services.CartService;
import com.ESD.ecomm.services.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartItemController(CartItemService cartItemService,
                              CartService cartService,
                              ProductService productService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.productService = productService;
    }

    // Add item to cart
    @PostMapping
    public ResponseEntity<CartItemResponseDTO> addToCart(@Valid @RequestBody CartItemRequestDTO dto) {
        // Fetch cart
        Optional<Cart> optionalCart = cartService.getCartById(dto.getCartId());
        if (optionalCart.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Cart cart = optionalCart.get();

        // Fetch product
        Optional<Product> optionalProduct = productService.getProductById(dto.getProductId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Product product = optionalProduct.get();

        // Check if product already in cart
        Optional<Cart_Items> existingItem = cartItemService.getCartItem(cart, product);
        Cart_Items cartItem;
        if (existingItem.isPresent()) {
            // Update quantity
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
            cartItem.calculateSubtotal();
        } else {
            // Create new cart item
            cartItem = CartItemMappers.toCartItem(dto, product, cart);
            cart.addItem(cartItem);
        }

        cartItemService.saveCartItem(cartItem);

        return ResponseEntity.ok(CartItemMappers.toCartItemResponseDTO(cartItem));
    }

    // Update cart item quantity
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponseDTO> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemUpdateDTO dto) {

        Optional<Cart_Items> optionalCartItem = cartItemService.getAllCartItems().stream()
                .filter(ci -> ci.getId() == cartItemId)
                .findFirst();

        if (optionalCartItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cart_Items cartItem = optionalCartItem.get();
        CartItemMappers.updateCartItemFromDTO(dto, cartItem);
        cartItemService.saveCartItem(cartItem);

        return ResponseEntity.ok(CartItemMappers.toCartItemResponseDTO(cartItem));
    }

    // Remove item from cart
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        Optional<Cart_Items> optionalCartItem = cartItemService.getAllCartItems().stream()
                .filter(ci -> ci.getId() == cartItemId)
                .findFirst();

        if (optionalCartItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        cartItemService.deleteItemsByCart(optionalCartItem.get().getCart());
        return ResponseEntity.noContent().build();
    }

    // Get all items in a cart
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItems(@PathVariable Long cartId) {
        Optional<Cart> optionalCart = cartService.getCartById(cartId);
        if (optionalCart.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<CartItemResponseDTO> items = cartItemService.getItemsByCart(optionalCart.get())
                .stream()
                .map(CartItemMappers::toCartItemResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }
}
