package com.ESD.ecomm.dto.category;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {

    private Long id;

    private String name;
    private String description;
    private String slug;

    private Boolean isActive;

    private Long parentCategoryId;     // Only ID to avoid infinite recursion
    private String parentCategoryName; // Optional convenience field

    private String createdAt;
    private String updatedAt;
}
