package com.ESD.ecomm.dto.product;

import com.ESD.ecomm.dto.category.CategoryResponseDTO;
import com.ESD.ecomm.dto.product_tags.ProductTagResponseDTO;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;
    private String prodName;
    private String prodDescription;
    private String sku;
    private BigDecimal prodPrice;
    private BigDecimal discountedPrice;
    private Integer stockQuantity;
    private Integer minStockLevel;
    private String brand;
    private Double weight;
    private String dimensions;
    private Boolean isActive;
    private Boolean isFeatured;
    private BigDecimal averageRating;
    private Integer totalReviews;

    private CategoryResponseDTO category; // Nested category info
    private List<ProductTagResponseDTO> tags; // Nested tags info

    private String createdAt;
    private String updatedAt;
}
