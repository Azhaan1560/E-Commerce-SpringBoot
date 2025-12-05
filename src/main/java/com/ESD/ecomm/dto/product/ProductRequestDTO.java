package com.ESD.ecomm.dto.product;

import com.ESD.ecomm.dto.category.CategoryResponseDTO;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 200)
    private String prodName;

    @NotBlank(message = "Product description is required")
    private String prodDescription;

    @NotBlank(message = "SKU is required")
    @Size(max = 12)
    private String sku;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal prodPrice;

    @DecimalMin(value = "0.0", message = "Discounted price cannot be negative")
    private BigDecimal discountedPrice;

    private Integer stockQuantity;
    private Integer minStockLevel;

    @Size(max = 100)
    private String brand;

    private Double weight;

    @Size(max = 50)
    private String dimensions;

    private Boolean isActive;
    private Boolean isFeatured;

    @NotNull(message = "Category is required")
    private Long categoryId; // we only need the ID to link to Category

    private List<Long> tagIds; // List of Product_Tags IDs
}
