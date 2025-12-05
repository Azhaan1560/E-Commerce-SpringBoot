package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.product_tags.*;
import com.ESD.ecomm.entities.Product_Tags;
import com.ESD.ecomm.services.ProductTagsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product-tags")
public class ProductTagsController {

    private final ProductTagsService productTagsService;

    @Autowired
    public ProductTagsController(ProductTagsService productTagsService) {
        this.productTagsService = productTagsService;
    }

    // -----------------------------------------------------
    // ✔ CREATE TAG
    // -----------------------------------------------------
    @PostMapping
    public ResponseEntity<?> createTag(@Valid @RequestBody ProductTagRequestDTO dto) {
        Product_Tags tag = Product_Tags.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .build();
        try {
            tag = productTagsService.createTag(tag);
            return ResponseEntity.ok(toResponseDTO(tag));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // -----------------------------------------------------
    // ✔ GET ALL TAGS
    // -----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ProductTagResponseDTO>> getAllTags() {
        List<ProductTagResponseDTO> tags = productTagsService.getAllTags()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tags);
    }

    // -----------------------------------------------------
    // ✔ GET TAG BY ID
    // -----------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable Long id) {
        return productTagsService.getTagById(id)
                .map(tag -> ResponseEntity.ok(toResponseDTO(tag)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // -----------------------------------------------------
    // ✔ UPDATE TAG
    // -----------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Long id, @Valid @RequestBody ProductTagUpdateDTO dto) {

        return productTagsService.getTagById(id)
                .map(tag -> {
                    if (dto.getName() != null) tag.setName(dto.getName());
                    if (dto.getSlug() != null) tag.setSlug(dto.getSlug());
                    try {
                        tag = productTagsService.updateTag(tag);
                        return ResponseEntity.ok(toResponseDTO(tag));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // -----------------------------------------------------
    // ✔ DELETE TAG
    // -----------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        productTagsService.deleteTag(id);
        return ResponseEntity.ok("Tag deleted successfully");
    }

    // -----------------------------------------------------
    // ✔ HELPER: ENTITY -> RESPONSE DTO
    // -----------------------------------------------------
    private ProductTagResponseDTO toResponseDTO(Product_Tags tag) {
        return ProductTagResponseDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .slug(tag.getSlug())
                .createdAt(tag.getCreatedAt())
                .build();
    }
}
