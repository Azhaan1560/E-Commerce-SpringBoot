package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.product.*;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.Category;
import com.ESD.ecomm.entities.Product_Tags;
import com.ESD.ecomm.services.ProductService;
import com.ESD.ecomm.services.CategoryService;
import com.ESD.ecomm.services.ProductTagsService;
import com.ESD.ecomm.mappers.ProductMappers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductTagsService productTagsService;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             ProductTagsService productTagsService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productTagsService = productTagsService;
    }

    // -----------------------------------------------------
    // ✔ CREATE PRODUCT
    // -----------------------------------------------------
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestDTO dto) {

        Optional<Category> categoryOpt = categoryService.getCategoryById(dto.getCategoryId());
        if (categoryOpt.isEmpty()) return ResponseEntity.badRequest().body("Invalid Category ID");

        List<Product_Tags> tags = new ArrayList<>();
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            for (Long tagId : dto.getTagIds()) {
                productTagsService.getTagById(tagId).ifPresent(tags::add);
            }
        }

        Product product = ProductMappers.toProduct(dto, categoryOpt.get(), tags);
        product = productService.saveProduct(product);

        return ResponseEntity.ok(ProductMappers.toProductResponseDTO(product));
    }

    // -----------------------------------------------------
    // ✔ GET ALL PRODUCTS
    // -----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts()
                .stream()
                .map(ProductMappers::toProductResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    // -----------------------------------------------------
    // ✔ GET PRODUCT BY ID
    // -----------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(ProductMappers.toProductResponseDTO(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // -----------------------------------------------------
    // ✔ UPDATE PRODUCT
    // -----------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @Valid @RequestBody ProductUpdateDTO dto) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) return ResponseEntity.notFound().build();

        Product product = productOpt.get();

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryService.getCategoryById(dto.getCategoryId())
                    .orElse(null);
        }

        List<Product_Tags> tags = null;
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            tags = new ArrayList<>();
            for (Long tagId : dto.getTagIds()) {
                productTagsService.getTagById(tagId).ifPresent(tags::add);
            }
        }

        ProductMappers.updateProductFromDTO(dto, product, category, tags);
        product = productService.saveProduct(product);

        return ResponseEntity.ok(ProductMappers.toProductResponseDTO(product));
    }

    // -----------------------------------------------------
    // ✔ DELETE PRODUCT
    // -----------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // -----------------------------------------------------
    // ✔ FILTER / SEARCH PRODUCTS
    // -----------------------------------------------------
    @PostMapping("/filter")
    public ResponseEntity<List<ProductResponseDTO>> filterProducts(@RequestBody ProductFilterDTO filter) {

        List<Product> products = productService.getAllProducts();

        // Filter by category
        if (filter.getCategoryId() != null) {
            Optional<Category> categoryOpt = categoryService.getCategoryById(filter.getCategoryId());
            if (categoryOpt.isPresent()) {
                products = products.stream()
                        .filter(p -> p.getCategory().getId().equals(filter.getCategoryId()))
                        .collect(Collectors.toList());
            }
        }

        // Filter by tags
        if (filter.getTagIds() != null && !filter.getTagIds().isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getTags().stream()
                            .anyMatch(tag -> filter.getTagIds().contains(tag.getId())))
                    .collect(Collectors.toList());
        }

        // Filter by price range
        if (filter.getMinPrice() != null && filter.getMaxPrice() != null) {
            products = products.stream()
                    .filter(p -> p.getProdPrice().doubleValue() >= filter.getMinPrice().doubleValue() &&
                            p.getProdPrice().doubleValue() <= filter.getMaxPrice().doubleValue())
                    .collect(Collectors.toList());
        }

        // Filter by active / featured
        if (filter.getIsActive() != null) {
            products = products.stream()
                    .filter(p -> filter.getIsActive().equals(p.getIsActive()))
                    .collect(Collectors.toList());
        }

        if (filter.getIsFeatured() != null) {
            products = products.stream()
                    .filter(p -> filter.getIsFeatured().equals(p.getIsFeatured()))
                    .collect(Collectors.toList());
        }

        List<ProductResponseDTO> response = products.stream()
                .map(ProductMappers::toProductResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
