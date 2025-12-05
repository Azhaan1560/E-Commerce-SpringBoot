package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.cart.AddToCartDTO;
import com.ESD.ecomm.dto.cart.CartResponseDTO;
import com.ESD.ecomm.entities.Cart;
import com.ESD.ecomm.entities.Cart_Items;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.mappers.CartsMappers;
import com.ESD.ecomm.services.CartItemService;
import com.ESD.ecomm.services.CartService;
import com.ESD.ecomm.services.ProductService;
import com.ESD.ecomm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final UserService userService; // Assuming you have a UserService to fetch users

    @Autowired
    public CartController(CartService cartService,
                          CartItemService cartItemService,
                          ProductService productService,
                          UserService userService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.userService = userService;
    }

    // Get cart by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDTO> getCartByUser(@PathVariable Long userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Cart> cartOpt = cartService.getCartByUser(userOpt.get());
        if (cartOpt.isEmpty()) return ResponseEntity.ok().body(null); // Or create new cart

        return ResponseEntity.ok(CartsMappers.toCartResponseDTO(cartOpt.get()));
    }

    // Add product to cart
    @PostMapping("/user/{userId}/add")
    public ResponseEntity<CartResponseDTO> addToCart(@PathVariable Long userId,
                                                     @Valid @RequestBody AddToCartDTO addToCartDTO) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOpt.get();

        // Fetch or create cart
        Cart cart = cartService.getCartByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartService.saveCart(newCart);
        });

        // Fetch product
        Optional<Product> productOpt = productService.getProductById(addToCartDTO.getProductId());
        if (productOpt.isEmpty()) return ResponseEntity.badRequest().build();

        Product product = productOpt.get();

        // Check if item already exists
        Optional<Cart_Items> existingItem = cartItemService.getCartItem(cart, product);
        if (existingItem.isPresent()) {
            Cart_Items item = existingItem.get();
            item.setQuantity(item.getQuantity() + addToCartDTO.getQuantity());
            item.calculateSubtotal();
            cartItemService.saveCartItem(item);
        } else {
            Cart_Items cartItem = com.ESD.ecomm.mappers.CartItemMappers.toCartItem(
                    com.ESD.ecomm.dto.cart_item.CartItemRequestDTO.builder()
                            .cartId(cart.getId())
                            .productId(product.getId())
                            .quantity(addToCartDTO.getQuantity())
                            .build(),
                    product,
                    cart
            );
            cart.addItem(cartItem);
            cartItemService.saveCartItem(cartItem);
        }

        // Update cart totals
        cartService.saveCart(cart);

        return ResponseEntity.ok(CartsMappers.toCartResponseDTO(cart));
    }

    // Clear a cart
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Cart> cartOpt = cartService.getCartByUser(userOpt.get());
        if (cartOpt.isEmpty()) return ResponseEntity.notFound().build();

        Cart cart = cartOpt.get();
        cart.getCartItems().clear();
        cart.setTotalItems(0);
        cart.setTotalPrice(java.math.BigDecimal.ZERO);
        cartService.saveCart(cart);

        return ResponseEntity.noContent().build();
    }
}
