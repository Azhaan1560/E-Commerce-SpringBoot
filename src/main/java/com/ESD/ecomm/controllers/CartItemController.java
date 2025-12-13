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
import org.springframework.web.server.ResponseStatusException;

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

    // USER ENDPOINTS
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

    @DeleteMapping("/my-items/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Ensure only authenticated users can access
    public ResponseEntity<?> removeMyCartItem(@PathVariable Long itemId,
                                              Authentication authentication) {

        // Get authenticated user (make sure your principal returns username correctly)
        String username = authentication.getName();

        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Fetch user's cart
        Cart cart = cartService.getCartByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        // Find the cart item (use .equals for Long comparison!)
        Cart_Items cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found or does not belong to you"));

        // Remove the item and save cart
        cart.removeItem(cartItem);
        cartService.saveCart(cart);

        return ResponseEntity.ok("Item removed from cart successfully");
    }
}