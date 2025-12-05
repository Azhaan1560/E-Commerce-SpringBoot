package com.ESD.ecomm.dto.product;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateDTO {

    @NotNull(message = "Product ID is required")
    private Long id;

    @Size(max = 200)
    private String prodName;

    private String prodDescription;

    @Size(max = 12)
    private String sku;

    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    private BigDecimal prodPrice;

    @DecimalMin(value = "0.0", message = "Discounted price cannot be negative")
    private BigDecimal discountedPrice;

    private Integer stockQuantity;
    private Integer minStockLevel;
    private String brand;
    private Double weight;
    private String dimensions;
    private Boolean isActive;
    private Boolean isFeatured;
    private Long categoryId;
    private List<Long> tagIds;
}
