package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.category.*;
import com.ESD.ecomm.entities.Category;
import com.ESD.ecomm.mappers.CategoryMappers;
import com.ESD.ecomm.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // CREATE CATEGORY
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        if (categoryService.existsByName(dto.getName())) {
            return ResponseEntity.badRequest().body("Category name already exists");
        }

        Category category = CategoryMappers.toCategory(dto);
        categoryService.saveCategory(category);

        return ResponseEntity.ok(CategoryMappers.toCategoryResponseDTO(category));
    }

    // GET ALL CATEGORIES
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories()
                .stream()
                .map(CategoryMappers::toCategoryResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    // GET ACTIVE CATEGORIES
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponseDTO>> getActiveCategories() {
        List<CategoryResponseDTO> categories = categoryService.getActiveCategories()
                .stream()
                .map(CategoryMappers::toCategoryResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    // GET CATEGORY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        if (categoryOpt.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(CategoryMappers.toCategoryResponseDTO(categoryOpt.get()));
    }

    // UPDATE CATEGORY
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDTO dto
    ) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        if (categoryOpt.isEmpty()) return ResponseEntity.notFound().build();

        Category category = categoryOpt.get();
        CategoryMappers.updateCategoryFromDTO(dto, category);
        categoryService.saveCategory(category);

        return ResponseEntity.ok(CategoryMappers.toCategoryResponseDTO(category));
    }

    // DELETE CATEGORY
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        if (categoryOpt.isEmpty()) return ResponseEntity.notFound().build();

        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    // GET SUBCATEGORIES
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryResponseDTO>> getSubcategories(@PathVariable Long id) {
        Optional<Category> parentOpt = categoryService.getCategoryById(id);
        if (parentOpt.isEmpty()) return ResponseEntity.notFound().build();

        List<CategoryResponseDTO> subcategories = categoryService.getSubcategories(parentOpt.get())
                .stream()
                .map(CategoryMappers::toCategoryResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(subcategories);
    }

    // CHECK NAME EXISTS
    @GetMapping("/exists")
    public ResponseEntity<?> existsByName(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.existsByName(name));
    }
}
