package com.ESD.ecomm.controllers;
import com.ESD.ecomm.dto.wishlist.AddToWishListDTO;
import com.ESD.ecomm.dto.wishlist.WishListResponseDTO;
import com.ESD.ecomm.dto.wishlist.WishListUpdateDTO;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.entities.WishList;
import com.ESD.ecomm.services.ProductService;
import com.ESD.ecomm.services.UserService;
import com.ESD.ecomm.services.WishListService;
import com.ESD.ecomm.mappers.WishListMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public WishListController(WishListService wishListService,
                              UserService userService,
                              ProductService productService) {
        this.wishListService = wishListService;
        this.userService = userService;
        this.productService = productService;
    }

    // Get wishlist for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<WishListResponseDTO> getWishlist(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WishList wishlist = wishListService.getWishlistByUser(user)
                .orElseGet(() -> {
                    // Create empty wishlist if not exists
                    WishList newWishlist = WishList.builder().user(user).build();
                    return wishListService.addProductToWishlist(newWishlist, null); // initially empty
                });

        return ResponseEntity.ok(WishListMappers.toDTO(wishlist));
    }

    // Add product to wishlist
    @PostMapping("/add")
    public ResponseEntity<WishListResponseDTO> addProduct(@Valid @RequestBody AddToWishListDTO dto) {
        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productService.getProductById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        WishList wishlist = wishListService.getWishlistByUser(user)
                .orElseGet(() -> WishList.builder().user(user).build());

        wishlist = wishListService.addProductToWishlist(wishlist, product);

        return new ResponseEntity<>(WishListMappers.toDTO(wishlist), HttpStatus.OK);
    }

    // Remove product from wishlist
    @PostMapping("/remove")
    public ResponseEntity<WishListResponseDTO> removeProduct(@Valid @RequestBody AddToWishListDTO dto) {
        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productService.getProductById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        WishList wishlist = wishListService.getWishlistByUser(user)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        wishlist = wishListService.removeProductFromWishlist(wishlist, product);

        return ResponseEntity.ok(WishListMappers.toDTO(wishlist));
    }

    // Update wishlist with full list of products
    @PutMapping("/update")
    public ResponseEntity<WishListResponseDTO> updateWishlist(@Valid @RequestBody WishListUpdateDTO dto) {
        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WishList wishlist = wishListService.getWishlistByUser(user)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        List<Product> products = new ArrayList<>();
        if (dto.getProductIds() != null) {
            for (Long productId : dto.getProductIds()) {
                Product product = productService.getProductById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
                products.add(product);
            }
        }

        // Replace the wishlist products
        wishlist.getProducts().clear();
        wishlist.getProducts().addAll(products);
        wishlist = wishListService.addProductToWishlist(wishlist, null); // save updated wishlist

        return ResponseEntity.ok(WishListMappers.toDTO(wishlist));
    }
}
