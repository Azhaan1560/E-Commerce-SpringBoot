package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.cart.AddToCartDTO;
import com.ESD.ecomm.dto.cart_item.CartItemRequestDTO;
import com.ESD.ecomm.dto.cart_item.CartItemResponseDTO;
import com.ESD.ecomm.dto.cart_item.CartItemUpdateDTO;
import com.ESD.ecomm.entities.Cart;
import com.ESD.ecomm.entities.Cart_Items;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.mappers.CartItemMappers;
import com.ESD.ecomm.services.CartItemService;
import com.ESD.ecomm.services.CartService;
import com.ESD.ecomm.services.ProductService;

import com.ESD.ecomm.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public CartItemController(CartItemService cartItemService, CartService cartService,
                              UserService userService, ProductService productService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    // ==================== USER ENDPOINTS ====================

    // GET MY CART ITEMS
    @GetMapping("/my-items")
    public ResponseEntity<?> getMyCartItems(Authentication authentication) {
        String username = authentication.getName();

        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = userOpt.get();
        Optional<Cart> cartOpt = cartService.getCartByUser(user);

        if (cartOpt.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        Cart cart = cartOpt.get();
        List<Cart_Items> items = cartItemService.getItemsByCart(cart);
        List<CartItemResponseDTO> itemDTOs = items.stream()
                .map(CartItemMappers::toCartItemResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(itemDTOs);
    }

    // UPDATE MY CART ITEM QUANTITY
    @PutMapping("/my-items/{itemId}")
    public ResponseEntity<?> updateMyCartItem(@PathVariable Long itemId,
                                              @Valid @RequestBody CartItemUpdateDTO dto,
                                              Authentication authentication) {
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

        // Find the cart item
        Optional<Cart_Items> itemOpt = cart.getCartItems().stream()
                .filter(item -> item.getId() == itemId)
                .findFirst();

        if (itemOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart item not found or does not belong to you");
        }

        Cart_Items cartItem = itemOpt.get();

        // If quantity is 0, remove the item
        if (dto.getQuantity() == 0) {
            cart.removeItem(cartItem);
            cartItemService.deleteItemsByCart(cart);
            cartService.saveCart(cart);
            return ResponseEntity.ok("Item removed from cart");
        }

        // Check stock availability
        Product product = cartItem.getProduct();
        if (product.getStockQuantity() < dto.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // Update quantity
        cartItem.setQuantity(dto.getQuantity());
        cartItem.calculateSubtotal();
        cartItemService.saveCartItem(cartItem);

        // Recalculate cart totals
        cart = cartService.saveCart(cart);

        return ResponseEntity.ok(CartItemMappers.toCartItemResponseDTO(cartItem));
    }

    // REMOVE ITEM FROM MY CART
    @DeleteMapping("/my-items/{itemId}")
    public ResponseEntity<?> removeMyCartItem(@PathVariable Long itemId,
                                              Authentication authentication) {
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

        // Find the cart item
        Optional<Cart_Items> itemOpt = cart.getCartItems().stream()
                .filter(item -> item.getId() == itemId)
                .findFirst();

        if (itemOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart item not found or does not belong to you");
        }

        Cart_Items cartItem = itemOpt.get();
        cart.removeItem(cartItem);
        cartService.saveCart(cart);

        return ResponseEntity.ok("Item removed from cart successfully");
    }

    // ==================== ADMIN ENDPOINTS ====================

    // GET ALL CART ITEMS (Admin only)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCartItems() {
        List<Cart_Items> allItems = cartItemService.getAllCartItems();
        List<CartItemResponseDTO> itemDTOs = allItems.stream()
                .map(CartItemMappers::toCartItemResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(itemDTOs);
    }

    // GET CART ITEMS BY CART ID (Admin only)
    @GetMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCartItemsByCartId(@PathVariable Long cartId) {
        Optional<Cart> cartOpt = cartService.getCartById(cartId);

        if (cartOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found");
        }

        Cart cart = cartOpt.get();
        List<Cart_Items> items = cartItemService.getItemsByCart(cart);
        List<CartItemResponseDTO> itemDTOs = items.stream()
                .map(CartItemMappers::toCartItemResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(itemDTOs);
    }

    // GET CART ITEMS BY USER ID (Admin only)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCartItemsByUserId(@PathVariable Long userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = userOpt.get();
        Optional<Cart> cartOpt = cartService.getCartByUser(user);

        if (cartOpt.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        Cart cart = cartOpt.get();
        List<Cart_Items> items = cartItemService.getItemsByCart(cart);
        List<CartItemResponseDTO> itemDTOs = items.stream()
                .map(CartItemMappers::toCartItemResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(itemDTOs);
    }

    // GET SPECIFIC CART ITEM (Admin only)
    @GetMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCartItemById(@PathVariable Long itemId) {
        List<Cart_Items> allItems = cartItemService.getAllCartItems();
        Optional<Cart_Items> itemOpt = allItems.stream()
                .filter(item -> item.getId() == itemId)
                .findFirst();

        if (itemOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart item not found");
        }

        return ResponseEntity.ok(CartItemMappers.toCartItemResponseDTO(itemOpt.get()));
    }

    // UPDATE ANY CART ITEM (Admin only)
    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCartItem(@PathVariable Long itemId,
                                            @Valid @RequestBody CartItemUpdateDTO dto) {
        List<Cart_Items> allItems = cartItemService.getAllCartItems();
        Optional<Cart_Items> itemOpt = allItems.stream()
                .filter(item -> item.getId() == itemId)
                .findFirst();

        if (itemOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart item not found");
        }

        Cart_Items cartItem = itemOpt.get();
        Cart cart = cartItem.getCart();

        // If quantity is 0, remove the item
        if (dto.getQuantity() == 0) {
            cart.removeItem(cartItem);
            cartService.saveCart(cart);
            return ResponseEntity.ok("Item removed from cart");
        }

        // Check stock availability
        Product product = cartItem.getProduct();
        if (product.getStockQuantity() < dto.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // Update quantity
        cartItem.setQuantity(dto.getQuantity());
        cartItem.calculateSubtotal();
        cartItemService.saveCartItem(cartItem);

        // Recalculate cart totals
        cartService.saveCart(cart);

        return ResponseEntity.ok(CartItemMappers.toCartItemResponseDTO(cartItem));
    }

    // DELETE ANY CART ITEM (Admin only)
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long itemId) {
        List<Cart_Items> allItems = cartItemService.getAllCartItems();
        Optional<Cart_Items> itemOpt = allItems.stream()
                .filter(item -> item.getId() == itemId)
                .findFirst();

        if (itemOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart item not found");
        }

        Cart_Items cartItem = itemOpt.get();
        Cart cart = cartItem.getCart();

        cart.removeItem(cartItem);
        cartService.saveCart(cart);

        return ResponseEntity.ok("Cart item deleted successfully");
    }

    // ADD ITEM TO SPECIFIC USER'S CART (Admin only)
    @PostMapping("/user/{userId}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addItemToUserCart(@PathVariable Long userId,
                                               @Valid @RequestBody AddToCartDTO dto) {
        Optional<User> userOpt = userService.getUserById(userId);
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

        return ResponseEntity.ok("Item added to user's cart successfully");
    }
}