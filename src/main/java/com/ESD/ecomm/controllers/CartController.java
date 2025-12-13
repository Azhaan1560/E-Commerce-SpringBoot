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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;

    @Autowired
    public CartController(CartService cartService, UserService userService,
                          ProductService productService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    // GET MY CART - Authenticated user gets their own cart
    @GetMapping("/my-cart")
    public ResponseEntity<?> getMyCart(Authentication authentication) {
        String username = authentication.getName();

        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = userOpt.get();
        Optional<Cart> cartOpt = cartService.getCartByUser(user);

        if (cartOpt.isEmpty()) {
            // Create new cart if doesn't exist
            Cart newCart = Cart.builder()
                    .user(user)
                    .cartItems(new ArrayList<>())
                    .totalPrice(BigDecimal.ZERO)
                    .totalItems(0)
                    .build();
            Cart savedCart = cartService.saveCart(newCart);
            return ResponseEntity.ok(CartsMappers.toCartResponseDTO(savedCart));
        }

        return ResponseEntity.ok(CartsMappers.toCartResponseDTO(cartOpt.get()));
    }

    // ADD ITEM TO MY CART
    @PostMapping("/my-cart/items")
    public ResponseEntity<?> addItemToMyCart(@Valid @RequestBody AddToCartDTO dto,
                                             Authentication authentication) {
        String username = authentication.getName();

        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = userOpt.get();

        // Get or create cart
        Cart cart = cartService.getCartByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .user(user)
                    .cartItems(new ArrayList<>())
                    .totalPrice(BigDecimal.ZERO)
                    .totalItems(0)
                    .build();
            return cartService.saveCart(newCart);
        });

        // Validate product
        Optional<Product> productOpt = productService.getProductById(dto.getProductId());
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }

        Product product = productOpt.get();

        // Check stock availability
        if (product.getStockQuantity() < dto.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // Check if item already exists in cart
        Optional<Cart_Items> existingItemOpt = cartItemService.getCartItem(cart, product);

        if (existingItemOpt.isPresent()) {
            // Update quantity
            Cart_Items existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + dto.getQuantity();

            if (product.getStockQuantity() < newQuantity) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Insufficient stock for requested quantity");
            }

            existingItem.setQuantity(newQuantity);
            existingItem.calculateSubtotal();
            cartItemService.saveCartItem(existingItem);
        } else {
            // Add new item
            Cart_Items newItem = Cart_Items.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(dto.getQuantity())
                    .price(product.getDiscountedPrice() != null ?
                            product.getDiscountedPrice() : product.getProdPrice())
                    .build();
            newItem.calculateSubtotal();
            cart.addItem(newItem);
            cartItemService.saveCartItem(newItem);
        }

        // Save updated cart
        cart = cartService.saveCart(cart);

        return ResponseEntity.ok(CartsMappers.toCartResponseDTO(cart));
    }

    // CLEAR MY CART
    @DeleteMapping("/my-cart/clear")
    public ResponseEntity<?> clearMyCart(Authentication authentication) {
        String username = authentication.getName();

        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = userOpt.get();
        Optional<Cart> cartOpt = cartService.getCartByUser(user);

        if (cartOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found");
        }

        Cart cart = cartOpt.get();
        cartItemService.deleteItemsByCart(cart);
        cart.getCartItems().clear();
        cart.setTotalItems(0);
        cart.setTotalPrice(BigDecimal.ZERO);
        cartService.saveCart(cart);

        return ResponseEntity.ok("Cart cleared successfully");
    }

    // ==================== ADMIN ENDPOINTS ====================

    // GET ALL CARTS (Admin only)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCarts() {
        List<Cart> carts = cartService.getCartById(0L)
                .map(Collections::singletonList)
                .orElse(new ArrayList<>());

        // Get all users and their carts
        List<User> users = userService.getAllUsers();
        List<CartResponseDTO> cartDTOs = new ArrayList<>();

        for (User user : users) {
            Optional<Cart> cartOpt = cartService.getCartByUser(user);
            cartOpt.ifPresent(cart -> cartDTOs.add(CartsMappers.toCartResponseDTO(cart)));
        }

        return ResponseEntity.ok(cartDTOs);
    }

    // GET CART BY USER ID (Admin only)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCartByUserId(@PathVariable Long userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = userOpt.get();
        Optional<Cart> cartOpt = cartService.getCartByUser(user);

        if (cartOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found for user");
        }

        return ResponseEntity.ok(CartsMappers.toCartResponseDTO(cartOpt.get()));
    }

    // GET CART BY CART ID (Admin only)
    @GetMapping("/{cartId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCartById(@PathVariable Long cartId) {
        Optional<Cart> cartOpt = cartService.getCartById(cartId);

        if (cartOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found");
        }

        return ResponseEntity.ok(CartsMappers.toCartResponseDTO(cartOpt.get()));
    }
}