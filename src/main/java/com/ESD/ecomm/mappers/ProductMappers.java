package com.ESD.ecomm.mappers;

import com.ESD.ecomm.dto.product.*;
import com.ESD.ecomm.entities.Category;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.Product_Tags;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMappers {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ProductRequestDTO -> Product entity
    public static Product toProduct(ProductRequestDTO dto, Category category, List<Product_Tags> tags) {
        if (dto == null) return null;

        return Product.builder()
                .prodName(dto.getProdName())
                .prodDescription(dto.getProdDescription())
                .sku(dto.getSku())
                .prodPrice(dto.getProdPrice())
                .discountedPrice(dto.getDiscountedPrice())
                .stockQuantity(dto.getStockQuantity())
                .minStockLevel(dto.getMinStockLevel())
                .brand(dto.getBrand())
                .weight(dto.getWeight() != null ? dto.getWeight() : 0.0)
                .dimensions(dto.getDimensions())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .isFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false)
                .category(category)
                .tags(tags != null ? tags : Collections.emptyList())
                .build();
    }

    // ProductUpdateDTO -> update existing Product entity
    public static void updateProductFromDTO(ProductUpdateDTO dto, Product product, Category category, List<Product_Tags> tags) {
        if (dto == null || product == null) return;

        if (dto.getProdName() != null) product.setProdName(dto.getProdName());
        if (dto.getProdDescription() != null) product.setProdDescription(dto.getProdDescription());
        if (dto.getSku() != null) product.setSku(dto.getSku());
        if (dto.getProdPrice() != null) product.setProdPrice(dto.getProdPrice());
        if (dto.getDiscountedPrice() != null) product.setDiscountedPrice(dto.getDiscountedPrice());
        if (dto.getStockQuantity() != null) product.setStockQuantity(dto.getStockQuantity());
        if (dto.getMinStockLevel() != null) product.setMinStockLevel(dto.getMinStockLevel());
        if (dto.getBrand() != null) product.setBrand(dto.getBrand());
        if (dto.getWeight() != null) product.setWeight(dto.getWeight());
        if (dto.getDimensions() != null) product.setDimensions(dto.getDimensions());
        if (dto.getIsActive() != null) product.setIsActive(dto.getIsActive());
        if (dto.getIsFeatured() != null) product.setIsFeatured(dto.getIsFeatured());
        if (category != null) product.setCategory(category);
        if (tags != null) product.setTags(tags);
    }

    // Product entity -> ProductResponseDTO
    public static ProductResponseDTO toProductResponseDTO(Product product) {
        if (product == null) return null;

        return ProductResponseDTO.builder()
                .id(product.getId())
                .prodName(product.getProdName())
                .prodDescription(product.getProdDescription())
                .sku(product.getSku())
                .prodPrice(product.getProdPrice())
                .discountedPrice(product.getDiscountedPrice())
                .stockQuantity(product.getStockQuantity())
                .minStockLevel(product.getMinStockLevel())
                .brand(product.getBrand())
                .weight(product.getWeight())
                .dimensions(product.getDimensions())
                .isActive(product.getIsActive())
                .isFeatured(product.getIsFeatured())
                .averageRating(product.getAverageRating())
                .totalReviews(product.getTotalReviews())
                .category(CategoryMappers.toCategoryResponseDTO(product.getCategory()))
                .tags(product.getTags() != null
                        ? product.getTags().stream()
                        .map(ProductTagsMappers::toProductTagResponseDTO)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .createdAt(product.getCreatedAt() != null ? product.getCreatedAt().format(formatter) : null)
                .updatedAt(product.getUpdatedAt() != null ? product.getUpdatedAt().format(formatter) : null)
                .build();
    }
}
